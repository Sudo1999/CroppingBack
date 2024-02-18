package com.geomatys.crop.entities;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Cutout {

    int x;
    int y;
    int width;
    int height;

    public Cutout(int x, int y,  int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
