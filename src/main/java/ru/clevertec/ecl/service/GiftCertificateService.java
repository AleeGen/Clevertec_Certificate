package ru.clevertec.ecl.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.filter.EntityFilter;
import ru.clevertec.ecl.dto.request.GiftCertificateRequest;
import ru.clevertec.ecl.service.util.patch.PatchRequest;
import ru.clevertec.ecl.dto.response.GiftCertificateResponse;

import java.util.List;

public interface GiftCertificateService extends BaseService<GiftCertificateRequest, GiftCertificateResponse> {

    List<GiftCertificateResponse> findAll(EntityFilter filter, Pageable pageable);

    /**
     * Method updates a certain field of an entity without touching others.
     *
     * @param id identifier gift certificate
     * @param pr {@link PatchRequest} from the package {@link ru.clevertec.ecl.service.util.patch}
     * @return an updated {@link GiftCertificateResponse}
     */
    GiftCertificateResponse patch(Long id, PatchRequest pr);

}