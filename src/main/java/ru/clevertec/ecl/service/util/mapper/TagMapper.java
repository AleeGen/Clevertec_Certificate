package ru.clevertec.ecl.service.util.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.request.TagRequest;
import ru.clevertec.ecl.dto.request.filter.impl.TagFilter;
import ru.clevertec.ecl.dto.response.TagResponse;
import ru.clevertec.ecl.entity.Tag;

@Mapper
public interface TagMapper {

    Tag toFrom(TagRequest tagRequest);

    TagResponse toFrom(Tag tag);

    Tag toFrom(TagFilter tagFilter);

    Tag toFrom(TagResponse tagResponse);
}