package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.dto.request.filter.EntityFilter;
import ru.clevertec.ecl.dto.request.filter.impl.TagFilter;
import ru.clevertec.ecl.exception.ServiceException;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.service.util.mapper.TagMapper;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.dto.request.TagRequest;
import ru.clevertec.ecl.dto.response.TagResponse;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagServiceImpl implements TagService {

    private final TagRepository tagRep;
    private final TagMapper mapper;

    @Override
    public List<TagResponse> findAll(EntityFilter filter, Pageable pageable) {
        return tagRep.findAll(Example.of(mapper.toFrom((TagFilter) filter)), pageable)
                .stream().map(mapper::toFrom).toList();
    }

    @Override
    public TagResponse findById(Long id) {
        return mapper.toFrom(tagRep.findById(id)
                .orElseThrow(() -> new ServiceException(String.format("Tag with id = %d not found", id))));
    }

    @Override
    @Transactional
    public TagResponse save(TagRequest tag) {
        return mapper.toFrom(tagRep.save(mapper.toFrom(tag)));
    }

    @Override
    @Transactional
    public TagResponse update(TagRequest tag) {
        Tag tagEntity = tagRep.findById(tag.id()).orElseThrow(() ->
                new ServiceException(String.format("Tag with id = %d not found", tag.id())));
        tagEntity.setName(tag.name());
        return mapper.toFrom(tagEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Tag> tag = tagRep.findById(id);
        tagRep.delete(tag.orElseThrow(
                () -> new ServiceException(String.format("Gift certificate with id = '%d' not found", id))));
    }

    @Override
    public Tag findByName(String name) {
        return tagRep.findByName(name);
    }

    @Override
    public TagResponse findMostUsedByUser() {
        return mapper.toFrom(tagRep.findMostUsedByUser());
    }

}