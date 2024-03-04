package com.image.crop.service.implement;

import com.image.crop.service.FileService;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileImplement implements FileService {

    private final Path storeLocation;
    public FileImplement() {
        this.storeLocation = Paths.get("store");
        System.out.println("In FileImplement, this.storeLocation = " + this.storeLocation);
        //System.out.println("Absolute path => " + this.storeLocation.toAbsolutePath());
    }

    /* Usage example: Suppose we want to capture a web page and save it to a file :
    Path destination = ...
    URI uri = URI.create("http://www.example.com/");
    try (InputStream input = uri.toURL().openStream()) {
      Files.copy(input, destination);
    }
    */

    @Override
    public void storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }
        try {
            //Path storePath = Paths.get(storeLocation + "/" + file.getOriginalFilename());
            String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            Path storePath = storeLocation.resolve(filename);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, storePath, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
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
        return storeLocation.resolve(filename);      // path1.resolve(path2) => path1/path2
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
            return Files.walk(this.storeLocation, 1)
                    .filter(path -> !path.equals(this.storeLocation))
                    .map(this.storeLocation::relativize);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to read stored files", e);
        }
    }

    @Override
    public String[] getAllPaths() {
        try {
            String[] result;
            return Files.walk(this.storeLocation, 1)
                    .filter(path -> !path.equals(this.storeLocation))
                    .map(path -> path.toString())
                    .toArray(String[]::new);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read stored files", e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(storeLocation.toFile());
    }
}
