package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.exception.ServiceException;
import ru.clevertec.ecl.repository.impl.TagRepository;
import ru.clevertec.ecl.service.BaseService;
import ru.clevertec.ecl.service.mapper.TagMapper;
import ru.clevertec.ecl.config.transaction.Transaction;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.dto.request.TagRequest;
import ru.clevertec.ecl.dto.response.TagResponse;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TagService implements BaseService<TagRequest, TagResponse> {

    private final TagRepository tagRep;
    private final TagMapper mapper = Mappers.getMapper(TagMapper.class);

    @Override
    public List<TagResponse> findAll() {
        return tagRep.findAll().stream().map(mapper::toFrom).toList();
    }

    @Override
    public TagResponse findById(Long id) {
        return mapper.toFrom(tagRep.findById(id)
                .orElseThrow(() -> new ServiceException(String.format("Tag with id = %d not found", id))));
    }

    @Override
    public TagResponse save(TagRequest tag) {
        return mapper.toFrom(tagRep.save(mapper.toFrom(tag)));
    }

    @Override
    public TagResponse update(TagRequest tag) {
        Tag tagEntity = tagRep.findById(tag.id()).orElseThrow(() ->
                new ServiceException(String.format("Tag with id = %d not found", tag.id())));
        tagEntity.setName(tag.name());
        return mapper.toFrom(tagRep.update(tagEntity));
    }

    @Transaction
    @Override
    public void delete(Long id) {
        Optional<Tag> tag = tagRep.findById(id);
        tagRep.delete(tag.orElseThrow(() -> new ServiceException("Gift certificate with id = '%s' not found")));
    }

}