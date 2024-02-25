package com.image.crop.controller;

import com.image.crop.entities.Cutout;
import com.image.crop.service.FrameService;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api")      // Point d'entrée du contrôleur
// On arrive ici avec l'adresse http://localhost:8080/api + le mapping de la méthode
public class FrameController {

//    // Constructeur de la classe avec activation du service
//    private final FrameService frameService;
//    public FrameController(FrameService frameService) {
//        this.frameService = frameService;
//    }

    @Autowired
    private FrameService frameService;

    ////  ==== Méthode /process ====  ////

    @PostMapping("/process")
    public ResponseEntity<byte[]> processFrame(@RequestParam MultipartFile file, @RequestParam Cutout cutout) {
        try {
            // Chargez l'image depuis le fichier
            BufferedImage initialImage = ImageIO.read(file.getInputStream());

            // Découpez l'image en fonction du rectangle
            BufferedImage resultImage = initialImage.getSubimage(cutout.getX(), cutout.getY(),
                    cutout.getWidth(), cutout.getHeight());

            // Convertissez l'image résultante en tableau de bytes
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(resultImage, "png", outputStream);

            return ResponseEntity.ok(outputStream.toByteArray());
        }
        catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Whitelabel Error Page : There was an unexpected error (type=Method Not Allowed, status=405)
    // En console => Resolved [org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'GET' is not supported]
    // => Your controller method supports POST requests, not GET, so sending a request to the browser will result in Method Not allowed

    public void cropImage(@PathVariable String filename) throws IOException {
        // Voir https://blog.idrsolutions.com/how-to-crop-an-image-in-java/

        // Load the image file using Java ImageIO (which is built into Java)
        Path imagePath = frameService.load(filename);
        BufferedImage image = ImageIO.read(new File(imagePath.toUri()));

        // Get a cropped version (x, y, width, height) (0,0 = top left corner)
        int x = 0, y = 0, w = 100, h = 0;
        BufferedImage subimage = image.getSubimage(x, y, w, h);

        // Save the image back to a file
        String subimageName = "Cropped-" + filename;
        Path subimagePath = frameService.load(subimageName);
        ImageIO.write(subimage, "jpeg", new File(subimagePath.toUri()));

        // Note it is important to save the image before applying any other changes as the BufferedImage object is shared
        // between the original and the new crop object.
    }

    ////  ==== Upload crop image Springboot Ajax.docx ====  ////

    /**
     * POST /uploadfile -> receive and locally save a file.
     *
     * @param file The uploaded file as Multipart file parameter in the HTTP request.
     * The RequestParam name must be the same as the attribute "name" in the Angular input tag with type file (upload.component.html).
     *             (Upload crop image Springboot Ajax.docx)
     *
     * @return a http OK status in case of success, a http 4xx status in case of errors.
     */
    @PostMapping("/uploadfile")
    public ResponseEntity<?> uploadFile( @RequestParam MultipartFile file, RedirectAttributes redirectAttributes) {
        // => [nio-8080-exec-1] .w.s.m.s.DefaultHandlerExceptionResolver : Resolved [org.springframework.web.HttpRequestMethodNotSupportedException:
        // Request method 'GET' is not supported]
        System.out.println("La fonction uploadFile du back a été appelée");
        try {
            // Get the filename and build the local file path (be sure that the application have write permissions on such directory)
            String filename = file.getOriginalFilename();
            String directory = "C:\\Users\\Martine\\Documents\\IntelliJ\\Cropping\\uploads";
            // => String directory = Paths.get("./uploads/").toString();
            String filepath = Paths.get(directory, filename).toString();

            // Save the file locally
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(filepath));
            stream.write(file.getBytes());
            stream.close();

            // Crop the image
            BufferedImage croppedImage = frameService.cropImageSquare(file.getBytes());
            String extension = FilenameUtils.getExtension(filename);
            extension = extension != null ? extension : "";
            File outputfile = new File(filepath);
            ImageIO.write(croppedImage, extension, outputfile);

            // Store the file
            frameService.store(file);
            //redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + uploadfile.getOriginalFilename() + " !");

            return new ResponseEntity<>(HttpStatus.OK);
            //return "redirect:/";
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

//    @PostMapping("/image")
//    public void addImage(@Valid @RequestBody Frame image) {
//        imageRepository.save(image);
//    }

//    @GetMapping("/image/:fullname")
//    public Frame getImage() {
//        Frame image = new Frame();
//        Example<Frame> example = Example.of(image);
//        return imageRepository.findOne(example).get();
//    }

    ////  ==== The FileUploadController ====  ////

    //  ====  Gestion fichiers images Springboot.docx  ====
    // https://github.com/spring-guides/gs-uploading-files/blob/main/complete/src/main/java/com/example/uploadingfiles/FileUploadController.java
    // https://spring.io/guides/gs/uploading-files
    @GetMapping("/")    // On arrive ici avec l'adresse http://localhost:8080/api/
    public String[] listUploadedFiles(Model model) {

        //System.out.println(model.toString());       // => Affichage dans la console => {}
        model.addAttribute("files", frameService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(FrameController.class, "serveFile",
                                path.getFileName().toString()).build().toUri().toString()).collect(Collectors.toList()));
        //System.out.println(model.toString());       // => Affichage dans la console (et dans le navigateur) =>
        // => {files=[http://localhost:8080/api/file/Image-chat.jpg, http://localhost:8080/api/file/Photo-test.jpg, (etc.)]}
        //return model.toString();

        // Je ne veux pas récupérer un String d'adresses mais une liste d'adresses :
        return frameService.getAllPaths();

        //return Paths.get("src/main/resources/uploadForm.html");
        // => "file:///C:/Users/Martine/Documents/IntelliJ/Cropping/src/main/resources/uploadForm.html"
        //return "uploadForm";
    }

    @GetMapping("/file/{filename:.+}")      // Affichage dans le navigateur d'un fichier image stocké dans uploads
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        // Le contrôleur reçoit la demande d’afficher l’image via l'url http://localhost:8080/api/file/Photo-test.jpg
        Resource file = frameService.loadAsResource(filename);
        //System.out.println(file.isFile());  // => true
        String mimeType = URLConnection.guessContentTypeFromName(file.getFilename());
        //System.out.println(mimeType);   // => image/jpeg

        long contentLength;
        try {
            contentLength = file.contentLength();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //System.out.println(contentLength);  // => 42826

        InputStream fileInputStream;
        try {
            fileInputStream = file.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //System.out.println(fileInputStream);    // => java.io.BufferedInputStream@6a5e23d7

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType)).contentLength(contentLength)
                .body(new InputStreamResource(fileInputStream));
    }

    // Si l'on souhaite à la place proposer le téléchargement du fichier, la fin de la méthode serveFile doit être modifiée :
    @GetMapping("/save/{filename:.+}")
    public ResponseEntity<Resource> saveFile(@PathVariable String filename) {

        Resource file = frameService.loadAsResource(filename);
        if (file == null) {
            return ResponseEntity.notFound().build(); }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + file.getFilename() + "\"").body(file);
        // => Le fichier est récupéré dans uploads (properties.getLocation()) et enregistré dans Téléchargements.
        // Le navigateur n'affiche rien, la page ne change pas pendant l'enregistrement du fichier.
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam MultipartFile file, RedirectAttributes redirectAttributes) {
        frameService.store(file);
        redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + " !");
        return "redirect:/";
    }
}
