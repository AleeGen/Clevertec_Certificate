package ru.clevertec.ecl.service.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.request.GiftCertificateRequest;
import ru.clevertec.ecl.dto.response.GiftCertificateResponse;
import ru.clevertec.ecl.entity.GiftCertificate;

@Mapper
public interface GiftCertificateMapper {

    GiftCertificate toFrom(GiftCertificateRequest gcRequest);

    GiftCertificateResponse toFrom(GiftCertificate gc);

}