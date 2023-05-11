package ru.clevertec.ecl.service;

import ru.clevertec.ecl.dto.request.TagRequest;
import ru.clevertec.ecl.dto.response.TagResponse;
import ru.clevertec.ecl.entity.Tag;

public interface TagService extends BaseService<TagRequest, TagResponse> {

    Tag findByName(String name);

    /**
     * @return @return the most frequently used tag from the user with the largest amount of orders
     */
    TagResponse findMostUsedByUser();

}