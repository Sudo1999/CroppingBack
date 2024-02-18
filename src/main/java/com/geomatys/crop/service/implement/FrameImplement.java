package com.geomatys.crop.service.implement;

import com.geomatys.crop.service.FrameService;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FrameImplement implements FrameService {

    private final Path rootLocation;
    public FrameImplement(FrameProperties properties) {
//        this.rootLocation = Paths.get("./uploads/");
        if (properties.getLocation().trim().length() == 0) {
            throw new RuntimeException("File upload location cannot be empty.");
        }
        this.rootLocation = Paths.get(properties.getLocation()).toAbsolutePath();
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @Override
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file."); }

//            String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
//            Path target = this.rootLocation.resolve(filename);
            String filename = Objects.requireNonNull(file.getOriginalFilename());
            Path target = this.rootLocation.resolve(Paths.get(filename)).normalize().toAbsolutePath();
            if (!target.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new RuntimeException("Cannot store file outside current directory."); }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
        //return filename;
    }

    @Override
    public BufferedImage cropImageSquare(byte[] image) throws IOException {
        // ImageImplement :
        // return new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        // Get a BufferedImage object from a byte array
        InputStream input = new ByteArrayInputStream(image);
        BufferedImage originalImage = ImageIO.read(input);

        // Get image dimensions
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();

        // The image is already a square
        if (height == width) {
            return originalImage;
        }

        // Compute the size of the square
        int squareSize = Math.min(height, width);

        // Coordinates of the image's middle
        int xc = width / 2;
        int yc = height / 2;

        // Crop
        BufferedImage croppedImage = originalImage.getSubimage(
                xc - (squareSize / 2),
                yc - (squareSize / 2),
                squareSize, squareSize
                // x, y, coordinates of the upper-left corner
        );
        return croppedImage;
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);      // path1.resolve(path2) => path1/path2
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path path = load(filename);
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() || resource.isReadable()) {
//                try {
//                    System.out.println("resource.getInputStream() = " + resource.getInputStream());     // => java.io.BufferedInputStream@68a6185
//                }
//                catch (IOException e) {
//                    throw new RuntimeException("Unable to print");
//                }
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new RuntimeException("MalformedURLException. Could not read file: " + filename, e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to read stored files", e);
        }
    }

    @Override
    public String[] getAllPaths() {
        try {
            String[] result;
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> path.toString())
                    .toArray(String[]::new);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read stored files", e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}
