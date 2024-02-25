package com.image.crop.dto;

import com.image.crop.entities.Rectangle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class RectangleDto {

    private Long id;
    private int upX, upY, width, height;
    //private Point upLeft;
    //private Point downRight;
    //private Set<Point> contains;

    public Rectangle toRectangle() {
        Rectangle rectangle = new Rectangle();
        rectangle.setId(this.id);
        rectangle.setUpX(this.upX);
        rectangle.setUpY(this.upY);
        rectangle.setWidth(this.width);
        rectangle.setHeight(this.height);
        return rectangle;
    }
}
