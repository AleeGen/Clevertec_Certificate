package ru.clevertec.taskspring.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.taskspring.dao.impl.GiftCertificateDAO;
import ru.clevertec.taskspring.dao.impl.TagDAO;
import ru.clevertec.taskspring.entity.GiftCertificate;
import ru.clevertec.taskspring.entity.Tag;
import ru.clevertec.taskspring.exception.ServiceException;
import ru.clevertec.taskspring.service.EntityService;

import java.util.List;

@Service
public class GiftCertificateService implements EntityService<GiftCertificate> {

    private final GiftCertificateDAO certDAO;
    private final TagDAO tagDAO;

    public GiftCertificateService(GiftCertificateDAO certDAO, TagDAO tagDAO) {
        this.certDAO = certDAO;
        this.tagDAO = tagDAO;
    }

    @Override
    public List<GiftCertificate> findAll() {
        return certDAO.findAll();
    }

    @Override
    public GiftCertificate findById(int id) {
        return certDAO.findById(id)
                .orElseThrow(() -> new ServiceException(String.format("Gift certificate with id = %d not found", id)));
    }

    @Transactional
    @Override
    public void save(GiftCertificate giftCertificate) {
        saveTagIfNotExist(giftCertificate.getName());
        if (!certDAO.save(giftCertificate)) {
            throw new ServiceException(String.format("Failed to save the gift certificate: %s.", giftCertificate));
        }
    }

    @Transactional
    @Override
    public void update(GiftCertificate giftCertificate) {
        saveTagIfNotExist(giftCertificate.getName());
        if (!certDAO.update(giftCertificate)) {
            throw new ServiceException(String.format("Failed to update the gift certificate: %s.", giftCertificate));
        }
    }

    @Override
    public void delete(int id) {
        if (!certDAO.delete(id)) {
            throw new ServiceException(String.format("Failed to delete the gift certificate with id: %d.", id));
        }
    }

    public List<GiftCertificate> findByPartialDescription(String partialDescription){
        return certDAO.findByPartialDescription(partialDescription);
    }

    /**
     * It is not recommended to use only the "save" method. Failure to check for the presence of a "tag"
     * can lead to an exception that cannot be ignored. If the exception is ignored, a record will be created
     * in the "gift_sertificate" table, the value of "name" of which will not match in the "tag" table.
     */
    private void saveTagIfNotExist(String name) {
        if (tagDAO.findByName(name).isEmpty()) {
            if (!tagDAO.save(Tag.builder().name(name).build())) {
                throw new ServiceException(String.format(
                        "The gift certificate could not be saved due to an error when creating a tag named %s.", name));
            }
        }
    }

}