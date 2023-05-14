package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import ru.clevertec.ecl.dto.request.filter.EntityFilter;
import ru.clevertec.ecl.dto.request.filter.impl.GiftCertificateFilter;
import ru.clevertec.ecl.dto.request.GiftCertificateRequest;
import ru.clevertec.ecl.service.util.patch.PatchRequest;
import ru.clevertec.ecl.dto.response.GiftCertificateResponse;
import ru.clevertec.ecl.exception.ServiceException;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.service.util.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.service.util.patch.Patch;
import ru.clevertec.ecl.service.util.patch.PatchResponse;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository gcRep;
    private final TagServiceImpl tagService;
    private final GiftCertificateMapper gcMapper;

    @Override
    public List<GiftCertificateResponse> findAll(EntityFilter filter, Pageable pageable) {
        GiftCertificateFilter gcFilter = (GiftCertificateFilter) filter;
        return Objects.nonNull(gcFilter.part()) || Objects.nonNull(gcFilter.tagName()) ?
                gcRep.findAll(gcFilter.tagName(), gcFilter.part(), pageable).stream().map(gcMapper::toFrom).toList() :
                gcRep.findAll(Example.of(gcMapper.toFrom(gcFilter)), pageable).stream().map(gcMapper::toFrom).toList();
    }

    @Override
    public GiftCertificateResponse findById(Long id) {
        return gcMapper.toFrom(gcRep.findById(id)
                .orElseThrow(() -> new ServiceException(String.format("Gift certificate with id = %d not found", id))));
    }

    @Override
    @Transactional
    public GiftCertificateResponse save(GiftCertificateRequest gc) {
        GiftCertificate gcEntity = gcMapper.toFrom(gc);
        LocalDateTime now = LocalDateTime.now();
        gcEntity.setCreateDate(now);
        gcEntity.setLastUpdateDate(now);
        gcEntity.setTags(getActualTags(gcEntity));
        return gcMapper.toFrom(gcRep.save(gcEntity));
    }

    @Override
    @Transactional
    public GiftCertificateResponse update(GiftCertificateRequest gc) {
        GiftCertificate gcEntity = gcRep.findById(gc.id()).orElseThrow(() ->
                new ServiceException(String.format("Gift certificate with id = %d not found", gc.id())));
        gcEntity.setLastUpdateDate(LocalDateTime.now());
        gcMapper.updateGC(gcEntity, gc);
        gcEntity.setTags(getActualTags(gcMapper.toFrom(gc)));
        gcRep.flush();
        return gcMapper.toFrom(gcEntity);
    }

    @Override
    @Transactional
    public GiftCertificateResponse patch(Long id, PatchRequest pr) {
        GiftCertificate gc = gcRep.findById(id).orElseThrow(
                () -> new ServiceException(String.format("Gift certificate with id = %d not found", id)));
        gc.setLastUpdateDate(LocalDateTime.now());
        PatchResponse patch = Patch.execute(pr, GiftCertificate.class);
        Field field = patch.modifiedField();
        field.setAccessible(true);
        if (!field.getName().equals("tags")) {
            ReflectionUtils.setField(field, gc, patch.value());
        } else {
            GiftCertificate build = GiftCertificate.builder().name(gc.getName()).tags((List<Tag>) patch.value()).build();
            gc.setTags(getActualTags(build));
        }
        gcRep.flush();
        return gcMapper.toFrom(gc);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<GiftCertificate> gc = gcRep.findById(id);
        gcRep.delete(gc.orElseThrow(() -> new ServiceException("Gift certificate with id = '%s' not found")));
    }

    private List<Tag> getActualTags(GiftCertificate gc) {
        if (gc.getTags().stream().noneMatch(t -> t.getName().equals(gc.getName()))) {
            gc.addTag(Tag.builder().name(gc.getName()).build());
        }
        return gc.getTags().stream().map(t -> Objects.requireNonNullElse(tagService.findByName(t.getName()), t)).toList();
    }

}