package com.image.crop.repository;

import com.image.crop.entities.Frame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrameRepository extends JpaRepository<Frame, Long> {
    // (JpaRepository extends PagingAndSortingRepository which extends CrudRepository)
}
