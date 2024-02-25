package com.image.crop.service;

import com.image.crop.dto.RectangleDto;

public interface RectangleService {

    Iterable<RectangleDto> findAll();
    // Iterable est le moins pratique de tous les types (Collection, List, Set).
    // Le type Collection est général et permet de changer le type du Repository sans rien changer ici

    RectangleDto add(RectangleDto rectangleDto);

    //Optional<RectangleDto> findById(long id);

    //Optional<RectangleDto> findRectangle(Point upLeft, Point downRight);

    /*
    Optional<RectangleDto> update(RectangleDto rectangleDto);

    boolean delete(long id);
    */
}
