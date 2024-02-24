package com.geomatys.crop.controller;

import com.geomatys.crop.dto.RectangleDto;
import com.geomatys.crop.repository.RectangleRepository;
import com.geomatys.crop.service.RectangleService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class RectangleController {

    @Autowired
    private RectangleRepository rectangleRepository;
    @Autowired
    private RectangleService rectangleService;

//    @GetMapping("/rectangles")
//    public List<Rectangle> getRectangles() {
//        return rectangleRepository.findAll();
//    }

//    @PostMapping("/rectangles")
//    public void addRectangle(@Valid @RequestBody Rectangle rectangle) {
//        rectangleRepository.save(rectangle);
//    }

    @GetMapping("/rectangles")
    public Iterable<RectangleDto> findAll() {
        return rectangleService.findAll();
    }

    @PostMapping("/rectangles")
    @ResponseStatus(HttpStatus.CREATED)
    public RectangleDto add(@Valid @RequestBody RectangleDto rectangleDto) {
        return rectangleService.add(rectangleDto);
    }

//    @GetMapping("{id}")
//    public RectangleDto findById(@PathVariable long id) {
//        Optional<RectangleDto> optRectangle = rectangleService.findById(id);
//        return optRectangle.orElse(null);
//    }
//
//    @GetMapping("{}")
//    public RectangleDto findRectangle(@PathVariable Point upLeft, @PathVariable Point downRight) {
//        Optional<RectangleDto> optRectangle = rectangleService.findRectangle(upLeft, downRight);
//        return optRectangle.orElse(null);
//    }

    /*
    @PutMapping
    public RectangleDto update(@Valid @RequestBody RectangleDto RectangleDto) {
        return rectangleService.update(RectangleDto).orElse(null);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        if (!rectangleService.delete(id)) {
            throw new RuntimeException("The id " + id + "doesn't exist");
        }
    }
    */
}
