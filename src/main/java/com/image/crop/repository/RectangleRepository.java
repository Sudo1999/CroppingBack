package com.image.crop.repository;

import com.image.crop.entities.Rectangle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RectangleRepository extends JpaRepository<Rectangle, Long> {
    // (JpaRepository extends PagingAndSortingRepository which extends CrudRepository)
}
