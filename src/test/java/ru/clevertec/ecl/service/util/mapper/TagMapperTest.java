package ru.clevertec.ecl.service.util.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.ecl.util.builder.impl.dto.request.TagReqBuilder;
import ru.clevertec.ecl.util.builder.impl.dto.request.filter.TagFilterBuilder;
import ru.clevertec.ecl.util.builder.impl.dto.response.TagResBuilder;
import ru.clevertec.ecl.util.builder.impl.entity.TagBuilder;

import static org.assertj.core.api.Assertions.assertThat;

class TagMapperTest {

    private final TagMapper mapper = Mappers.getMapper(TagMapper.class);

    @Test
    void checkRequestToEntity() {
        var expected = TagBuilder.aTag().build();
        var request = TagReqBuilder.aTagReq().build();
        var entity = mapper.toFrom(request);
        assertThat(entity).isEqualTo(expected);
    }

    @Test
    void checkEntityToResponse() {
        var expected = TagResBuilder.aTagRes().build();
        var entity = TagBuilder.aTag().build();
        var response = mapper.toFrom(entity);
        assertThat(response).isEqualTo(expected);
    }

    @Test
    void checkResponseToEntity() {
        var expected = TagBuilder.aTag().build();
        var response = TagResBuilder.aTagRes().build();
        var entity = mapper.toFrom(response);
        assertThat(entity).isEqualTo(expected);
    }

    @Test
    void checkFilterToEntity() {
        var expected = TagBuilder.aTag().build();
        var filter = TagFilterBuilder.aTagFilter().build();
        var entity = mapper.toFrom(filter);
        assertThat(entity).isEqualTo(expected);
    }

}