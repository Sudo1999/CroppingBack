package com.image.crop.service.implement;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties()
//@ConfigurationProperties(prefix = "frame")
public class FrameProperties {

    /**
     * Folder location for storing files
     */
    //private String location = "https://images.pexels.com/photos/19982503/";     // => Il faudrait avoir deux options de chargement, local ou URL.
    //private String location = "https://images.pexels.com/photos/19982503/pexels-photo-19982503.jpeg?";
    //private String location = "https://www.pexels.com/fr-fr/photo/neige-bois-lumineux-aube-19982503/";
    //private String location = "C:\\Users\\Martine\\Documents\\IntelliJ\\CroppingBack\\uploads";

    private String location = "uploads";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
