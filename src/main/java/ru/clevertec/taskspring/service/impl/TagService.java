package ru.clevertec.taskspring.service.impl;

import org.springframework.stereotype.Service;
import ru.clevertec.taskspring.dao.impl.TagDAO;
import ru.clevertec.taskspring.entity.Tag;
import ru.clevertec.taskspring.exception.ServiceException;
import ru.clevertec.taskspring.service.EntityService;

import java.util.List;

@Service
public class TagService implements EntityService<Tag> {

    private final TagDAO tagDAO;

    public TagService(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    @Override
    public List<Tag> findAll() {
        return tagDAO.findAll();
    }

    @Override
    public Tag findById(int id) {
        return tagDAO.findById(id).orElseThrow(() -> new ServiceException(String.format("Tag with id = %d not found", id)));
    }

    @Override
    public void save(Tag tag) {
        if (!tagDAO.save(tag)) {
            throw new ServiceException(String.format("Failed to save the tag: %s", tag));
        }
    }

    @Override
    public void update(Tag tag) {
        if (!tagDAO.update(tag)) {
            throw new ServiceException(String.format("Failed to update the tag: %s", tag));
        }
    }

    @Override
    public void delete(int id) {
        if (!tagDAO.delete(id)) {
            throw new ServiceException(String.format("Failed to delete the tag with id: %d", id));
        }
    }

    public List<Tag> findByPartialName(String pattern) {
        return tagDAO.findByPartialName(pattern);
    }

}