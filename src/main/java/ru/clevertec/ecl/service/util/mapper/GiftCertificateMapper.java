package ru.clevertec.ecl.service.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.clevertec.ecl.dto.request.GiftCertificateRequest;
import ru.clevertec.ecl.dto.request.filter.impl.GiftCertificateFilter;
import ru.clevertec.ecl.dto.response.GiftCertificateResponse;
import ru.clevertec.ecl.entity.GiftCertificate;

@Mapper(uses = TagMapper.class)
public interface GiftCertificateMapper {

    GiftCertificate toFrom(GiftCertificateRequest gcRequest);

    GiftCertificateResponse toFrom(GiftCertificate gc);

    GiftCertificate toFrom(GiftCertificateResponse gcResponse);

    GiftCertificate toFrom(GiftCertificateFilter gcFilter);

    @Mapping(target = "tags", ignore = true)
    void updateGC(@MappingTarget GiftCertificate gc, GiftCertificateRequest gcRequest);

}