package com.geomatys.crop.entities;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Point {

    int x;
    int y;

    public Point() {
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
