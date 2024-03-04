package com.image.crop.controller;

import com.image.crop.service.FrameService;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
