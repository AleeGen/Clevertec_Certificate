package ru.clevertec.ecl.data.database;

import ru.clevertec.ecl.data.builder.impl.dto.response.GCResBuilder;
import ru.clevertec.ecl.data.builder.impl.dto.response.TagResBuilder;
import ru.clevertec.ecl.dto.response.GiftCertificateResponse;
import ru.clevertec.ecl.dto.response.TagResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;

public class GiftCertificateDBResponse {

    private final long sizeTable = 5L;

    public long getSize() {
        return sizeTable;
    }

    public List<GiftCertificateResponse> all() {
        return LongStream.range(1, sizeTable + 1).boxed().map(this::byContent).toList();
    }

    public List<GiftCertificateResponse> withPageable() {
        return LongStream.range(2, 4).boxed().map(i -> byContent(sizeTable - i)).toList();
    }

    public List<GiftCertificateResponse> withFilterDefaultParameters() {
        return List.of(byContent(4L));
    }

    public List<GiftCertificateResponse> withFilterCustomParameters() {
        return List.of(byContent(1L), byContent(4L), byContent(5L));
    }

    public List<GiftCertificateResponse> empty() {
        return Collections.emptyList();
    }

    public GiftCertificateResponse byContent(Long id) {
        int i = Integer.parseInt(String.valueOf(id));
        int nanoSec = i * 100000000 + i * 10000000 + i * 1000000;
        List<Long> idTags = new ArrayList<>();
        switch (i) {
            case 1 -> idTags = List.of(1L);
            case 2 -> idTags = List.of(2L, 3L);
            case 3 -> idTags = List.of(4L, 5L, 6L);
            case 4 -> idTags = List.of(1L, 7L, 8L, 9L, 10L);
            case 5 -> idTags = List.of(1L, 2L, 3L, 4L, 5L);
        }
        List<TagResponse> tags = idTags.stream().map(element -> TagResBuilder.aTagRes()
                .withId(element)
                .withName("n" + element)
                .build()).toList();
        return GCResBuilder.aGCRes()
                .withId(id)
                .withName("n" + id)
                .withDescription("d" + id)
                .withPrice((double) i)
                .withDuration(i)
                .withCreateDate(LocalDateTime.of(2023, i, i, i, i * 10 + i, i * 10 + i, nanoSec))
                .withLastUpdateDate(LocalDateTime.of(2023, i, i, i, i * 10 + i, i * 10 + i, nanoSec))
                .withTags(tags)
                .build();
    }

    public GiftCertificateResponse postOrPut() {
        return GCResBuilder.aGCRes()
                .withId(6L)
                .withName("newName")
                .withDescription("newDescription")
                .withPrice(11.1)
                .withDuration(11)
                .withTags(List.of(TagResBuilder.aTagRes().withId(1L).withName("n1").build(),
                        TagResBuilder.aTagRes().withId(11L).withName("newTagName").build(),
                        TagResBuilder.aTagRes().withId(12L).withName("newName").build())).build();

    }

    public List<TagResponse> patchTags() {
        return List.of(
                TagResBuilder.aTagRes().withId(11L).withName("newTagName1").build(),
                TagResBuilder.aTagRes().withId(12L).withName("newTagName2").build(),
                TagResBuilder.aTagRes().withId(1L).withName("n1").build());
    }

}