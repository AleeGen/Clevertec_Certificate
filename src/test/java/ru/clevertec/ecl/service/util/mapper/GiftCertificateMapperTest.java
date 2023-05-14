package ru.clevertec.ecl.service.util.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.data.builder.impl.dto.request.GCReqBuilder;
import ru.clevertec.ecl.data.builder.impl.dto.request.filter.GCFilterBuilder;
import ru.clevertec.ecl.data.builder.impl.dto.response.GCResBuilder;
import ru.clevertec.ecl.data.builder.impl.entity.GCBuilder;

import java.util.Collections;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class GiftCertificateMapperTest {

    @InjectMocks
    private GiftCertificateMapper mapperGC = Mappers.getMapper(GiftCertificateMapper.class);

    @Mock
    private TagMapper mapperTag;

    @Test
    void checkRequestToEntity() {
        var tagsReq = GCReqBuilder.aGCReq().build().tags();
        var tagsEnt = GCBuilder.aGC().build().getTags();
        IntStream.range(0, tagsReq.size()).forEach(i ->
                doReturn(tagsEnt.get(i))
                        .when(mapperTag)
                        .toFrom(tagsReq.get(i)));
        var expected = GCBuilder.aGC().withTags(tagsEnt).withCreateDate(null).withLastUpdateDate(null).build();
        var request = GCReqBuilder.aGCReq().withTags(tagsReq).build();
        var entity = mapperGC.toFrom(request);
        assertThat(entity).isEqualTo(expected);
    }

    @Test
    void checkEntityToResponse() {
        var tagsEnt = GCBuilder.aGC().build().getTags();
        var tagsRes = GCResBuilder.aGCRes().build().tags();
        IntStream.range(0, tagsEnt.size()).forEach(i ->
                doReturn(tagsRes.get(i))
                        .when(mapperTag)
                        .toFrom(tagsEnt.get(i)));
        var expected = GCResBuilder.aGCRes().withTags(tagsRes).build();
        var entity = GCBuilder.aGC().withTags(tagsEnt).build();
        var response = mapperGC.toFrom(entity);
        assertThat(response).isEqualTo(expected);
    }

    @Test
    void checkResponseToEntity() {
        var tagsRes = GCResBuilder.aGCRes().build().tags();
        var tagsEnt = GCBuilder.aGC().build().getTags();
        IntStream.range(0, tagsRes.size()).forEach(i ->
                doReturn(tagsEnt.get(i))
                        .when(mapperTag)
                        .toFrom(tagsRes.get(i)));
        var expected = GCBuilder.aGC().withTags(tagsEnt).build();
        var response = GCResBuilder.aGCRes().withTags(tagsRes).build();
        var entity = mapperGC.toFrom(response);
        assertThat(entity).isEqualTo(expected);
    }

    @Test
    void checkFilterToEntity() {
        var expected = GCBuilder.aGC().withTags(Collections.emptyList()).build();
        var filter = GCFilterBuilder.aGCFilter().build();
        var entity = mapperGC.toFrom(filter);
        assertThat(entity).isEqualTo(expected);
    }

    @Test
    void checkUpdateGC() {
        var expected = GCBuilder.aGC()
                .withName("afterName")
                .withDescription("afterDescription")
                .withPrice(2.0)
                .withDuration(2)
                .build();
        var entity = GCBuilder.aGC().build();
        var request = GCReqBuilder.aGCReq()
                .withName("afterName")
                .withDescription("afterDescription")
                .withPrice(2.0)
                .withDuration(2)
                .build();
        mapperGC.updateGC(entity, request);
        assertThat(entity).isEqualTo(expected);
    }

}