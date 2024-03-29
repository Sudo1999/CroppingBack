package com.image.crop.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    void storeFile(MultipartFile file);

    BufferedImage cropImageSquare(byte[] image) throws IOException;

    Path load(String filename);

    Resource loadAsResource(String filename);

    Stream<Path> loadAll();

    String[] getAllPaths();

    void deleteAll();
}
