package com.image.crop.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Rectangle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int upX, upY, width, height;
    //private Point upLeft;
    //private Point downRight;
    //private Set<Point> contains;

//    public Rectangle(Point upLeft, Point downRight) {
//        this.upLeft = upLeft;
//        this.downRight = downRight;
//        this.contains = new HashSet<>();
//        for (int x = upLeft.x; x < downRight.x; x++) {
//            for (int y = upLeft.y; y < downRight.y; y++) {
//                Point point = new Point(x, y);
//                contains.add(point);
//            }
//        }
//    }

    public Rectangle(int upX, int upY, int width, int height) {
        this.upX = upX;
        this.upY = upY;
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "Point " + this.upX + "," + upY + ", largeur " + width + ", hauteur " + height + ".";
    }
}
