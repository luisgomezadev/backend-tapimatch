package com.lgsoftworks.field.infrastructure.adapter.out.persistence.repository;

import com.lgsoftworks.field.infrastructure.adapter.out.persistence.entity.FieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface FieldRepository extends JpaRepository<FieldEntity, Long>, JpaSpecificationExecutor<FieldEntity> {
    List<FieldEntity> findByVenueIdAndActiveTrue(Long venueId);
    List<FieldEntity> findByVenueId(Long venueId);
    boolean existsByVenueId(Long venueId);

    @Query("SELECT DISTINCT f.venue.id FROM FieldEntity f WHERE f.venue.id IN :venueIds")
    Set<Long> findVenueIdsWithFields(@Param("venueIds") Collection<Long> venueIds);
}
