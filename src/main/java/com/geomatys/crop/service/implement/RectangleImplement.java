package com.geomatys.crop.service.implement;

import com.geomatys.crop.dto.RectangleDto;
import com.geomatys.crop.entities.Rectangle;
import com.geomatys.crop.repository.RectangleRepository;
import com.geomatys.crop.service.RectangleService;
import com.geomatys.crop.utils.StreamUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RectangleImplement implements RectangleService {

    @Autowired
    private RectangleRepository rectangleRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Iterable<RectangleDto> findAll() {
        return StreamUtils.toStream(this.rectangleRepository.findAll())
                .map(rectangleEntity -> modelMapper.map(rectangleEntity, RectangleDto.class))
                .toList();
    }

    @Override
    public RectangleDto add(RectangleDto rectangleDto) {
        Rectangle rectangleEntity = modelMapper.map(rectangleDto, Rectangle.class);
        this.rectangleRepository.save(rectangleEntity);
        return modelMapper.map(rectangleEntity, RectangleDto.class);
    }

//    @Override
//    public Optional<RectangleDto> findById(long id) {
//        return this.rectangleRepository.findById(id)
//                .map(rectangleEntity -> modelMapper.map(rectangleEntity, RectangleDto.class));
//    }

//    @Override
//    public Optional<RectangleDto> findRectangle(Point upLeft, Point downRight) {
//        Rectangle rectangle = new Rectangle(upLeft, downRight);
//        Example<Rectangle> example = Example.of(rectangle);
//        return this.rectangleRepository.findOne(example)
//                .map(rectangleEntity -> modelMapper.map(rectangleEntity, RectangleDto.class));
//    }

    /*
    @Override
    public Optional<RectangleDto> update(RectangleDto rectangleDto) {
//        Rectangle rectangle = rectangleDto.toRectangle();
//        Optional<Rectangle> optRectangle = this.rectangleRepository.findById(rectangle.getId());
//        if(optRectangle.isPresent()) {
//            this.rectangleRepository.save(rectangle);
//            return Optional.of(rectangle);
//        }
//        return Optional.empty();

        return this.rectangleRepository.findById(rectangleDto.getId())
                .map(rectangleEntity -> {
                    // Update entity object from db with dto fields
                    modelMapper.map(rectangleDto, rectangleEntity);
                    // Synchronize with database
                    rectangleRepository.save(rectangleEntity);
                    // Transform entity updated in dto
                    return modelMapper.map(rectangleEntity, RectangleDto.class);
                });
    }

    @Override
    public boolean delete(long id) {
        Optional<Rectangle> optRectangle = this.rectangleRepository.findById(id);
        if(optRectangle.isPresent()) {
            this.rectangleRepository.delete(optRectangle.get());
            return true;
        }
        return false;
    }
    */
}
