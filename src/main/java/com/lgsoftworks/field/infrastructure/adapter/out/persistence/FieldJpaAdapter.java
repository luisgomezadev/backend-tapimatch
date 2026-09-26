package com.lgsoftworks.field.infrastructure.adapter.out.persistence;

import com.lgsoftworks.field.domain.model.Field;
import com.lgsoftworks.field.domain.port.out.FieldRepositoryPort;
import com.lgsoftworks.field.infrastructure.adapter.out.persistence.entity.FieldEntity;
import com.lgsoftworks.field.infrastructure.adapter.out.persistence.mapper.FieldDboMapper;
import com.lgsoftworks.field.infrastructure.adapter.out.persistence.repository.FieldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FieldJpaAdapter implements FieldRepositoryPort {

    private final FieldRepository fieldRepository;
    private final FieldDboMapper mapper;

    @Override
    public Field save(Field field) {
        FieldEntity saved = fieldRepository.save(mapper.toDbo(field));
        return mapper.toModel(saved);
    }

    @Override
    public Optional<Field> findById(Long id) {
        return fieldRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<Field> findAllByVenueId(Long venueId) {
        return fieldRepository.findByVenueId(venueId).stream().map(mapper::toModel).toList();
    }

    @Override
    public List<Field> findByVenueId(Long venueId) {
        return fieldRepository.findByVenueIdAndActiveTrue(venueId).stream().map(mapper::toModel).toList();
    }

    @Override
    public List<Field> findPublicByVenueId(Long venueId) {
        return fieldRepository.findByVenueId(venueId).stream().map(mapper::toModel).toList();
    }
}