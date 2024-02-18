package com.geomatys.crop.repository;

import com.geomatys.crop.entities.Frame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrameRepository extends JpaRepository<Frame, Long> {
    // (JpaRepository extends PagingAndSortingRepository which extends CrudRepository)
}
