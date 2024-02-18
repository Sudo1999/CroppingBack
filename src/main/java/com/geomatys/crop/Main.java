package com.geomatys.crop;

import com.geomatys.crop.entities.Frame;
import com.geomatys.crop.entities.Rectangle;
import com.geomatys.crop.repository.FrameRepository;
import com.geomatys.crop.repository.RectangleRepository;
import java.util.stream.Stream;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("Hello world!");
    }

    @Bean
    CommandLineRunner init(RectangleRepository rectangleRepository, FrameRepository frameRepository) {
        // Je pose que l'image fait par définition 10 de largeur et 10 de hauteur
        return args -> {
            Stream.of("0,0,1,1", "5,5,5,5", "3,3,4,4")
                .forEach(values -> {
                    int upX = Integer.valueOf(values.split(",")[0]);
                    int upY = Integer.valueOf(values.split(",")[1]);
                    int width = Integer.valueOf(values.split(",")[2]);
                    int height = Integer.valueOf(values.split(",")[3]);
                    Rectangle rectangle = new Rectangle(upX, upY, width, height);
                    rectangleRepository.save(rectangle);
            });
            rectangleRepository.findAll().forEach(System.out::println);

            Frame imageChat = new Frame();
            imageChat.setPath("./uploads/Frame-chat.jpg");
            frameRepository.save(imageChat);
        };
    }
}
