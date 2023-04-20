package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import ru.clevertec.ecl.dto.request.GiftCertificateRequest;
import ru.clevertec.ecl.dto.response.GiftCertificateResponse;
import ru.clevertec.ecl.exception.ServiceException;
import ru.clevertec.ecl.repository.impl.GiftCertificateRepository;
import ru.clevertec.ecl.repository.impl.TagRepository;
import ru.clevertec.ecl.service.BaseService;
import ru.clevertec.ecl.service.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.config.transaction.Transaction;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GiftCertificateService implements BaseService<GiftCertificateRequest, GiftCertificateResponse> {

    private final GiftCertificateRepository gcRep;
    private final TagRepository tagRep;
    private final GiftCertificateMapper mapper = Mappers.getMapper(GiftCertificateMapper.class);

    @Transaction
    @Override
    public List<GiftCertificateResponse> findAll() {
        return gcRep.findAll().stream().map(mapper::toFrom).toList();
    }

    @Transaction
    @Override
    public GiftCertificateResponse findById(Long id) {
        return mapper.toFrom(gcRep.findById(id)
                .orElseThrow(() -> new ServiceException(String.format("Gift certificate with id = %d not found", id))));
    }

    @Transaction
    @Override
    public GiftCertificateResponse save(GiftCertificateRequest gc) {
        GiftCertificate gcEntity = mapper.toFrom(gc);
        LocalDateTime now = LocalDateTime.now();
        gcEntity.setCreateDate(now);
        gcEntity.setLastUpdateDate(now);
        gcEntity.addTag(getTagByName(gc.name()));
        gcRep.save(gcEntity);
        return mapper.toFrom(gcEntity);
    }

    @Transaction
    @Override
    public GiftCertificateResponse update(GiftCertificateRequest gc) {
        GiftCertificate gcEntity = gcRep.findById(gc.id()).orElseThrow(() ->
                new ServiceException(String.format("Gift certificate with id = %d not found", gc.id())));
        gcEntity.setLastUpdateDate(LocalDateTime.now());
        String tagName = gc.name();
        gcEntity.setName(tagName);
        gcEntity.setDescription(gc.description());
        gcEntity.setPrice(gc.price());
        gcEntity.setDuration(gc.duration());
        gcEntity.setLastUpdateDate(LocalDateTime.now());
        if (gcEntity.getTags().stream().noneMatch(t -> t.getName().equals(tagName))) {
            gcEntity.addTag(getTagByName(tagName));
        }
        gcRep.update(gcEntity);
        return mapper.toFrom(gcEntity);
    }

    @Transaction
    @Override
    public void delete(Long id) {
        Optional<GiftCertificate> gc = gcRep.findById(id);
        gcRep.delete(gc.orElseThrow(() -> new ServiceException("Gift certificate with id = '%s' not found")));
    }

    /**
     * Searches for {@link GiftCertificate} with the specified name.
     * Sorts them in ascending or descending order, as well as by optional parameters,
     * taking into account their indexing.
     *
     * @param name     tag names
     * @param sortType type sorting (asc/desc)
     * @param sortBy   fields by which sorting will be performed (optional parameter)
     * @return the list of {@link GiftCertificate} in sorted form
     * @throws ServiceException if there is no field with the specified name
     */
    @Transaction
    public List<GiftCertificateResponse> findByTagName(String name, String sortType, String... sortBy) {
        sortBy = Objects.isNull(sortBy) ? new String[0] : sortBy;
        checkFields(sortBy);
        boolean isDesc = !Objects.isNull(sortType) && "desc".equals(sortType);
        return gcRep.findByTagName(name, isDesc, sortBy).stream().map(mapper::toFrom).toList();
    }

    /**
     * Searches for {@link GiftCertificate} based on their name or description.
     * Sorts them in ascending or descending order, as well as by optional parameters,
     * taking into account their indexing.
     *
     * @param part the parameter by which the search will be performed
     * @param sortType type sorting (asc/desc)
     * @param sortBy   fields by which sorting will be performed (optional parameter)
     * @return the list of {@link GiftCertificate} in sorted form
     * @throws ServiceException if there is no field with the specified name
     */
    @Transaction
    public List<GiftCertificateResponse> findByPart(String part, String sortType, String... sortBy) {
        sortBy = Objects.isNull(sortBy) ? new String[0] : sortBy;
        checkFields(sortBy);
        boolean isDesc = !Objects.isNull(sortType) && "desc".equals(sortType);
        return gcRep.findByPart(part, isDesc, sortBy).stream().map(mapper::toFrom).toList();
    }

    private Tag getTagByName(String name) {
        Tag tag = tagRep.findByName(name);
        return Objects.requireNonNullElse(tag, Tag.builder().name(name).build());
    }

    /**
     * The method checks whether there are fields with the specified names in the gift {@link GiftCertificate}
     *
     * @param fields field names
     * @throws ServiceException if there is no field with the specified name
     */
    private void checkFields(String... fields) {
        Arrays.stream(fields).forEach(name -> {
            if (Objects.isNull(ReflectionUtils.findField(GiftCertificate.class, name))) {
                throw new ServiceException(String.format("Field with name '%s' not found", name));
            }
        });
    }

}