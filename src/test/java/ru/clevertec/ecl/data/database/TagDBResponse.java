package ru.clevertec.ecl.data.database;

import ru.clevertec.ecl.data.builder.impl.dto.response.TagResBuilder;
import ru.clevertec.ecl.dto.response.TagResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;

public class TagDBResponse {

    private final long sizeTable = 10L;

    public long getSize() {
        return sizeTable;
    }

    public List<TagResponse> all() {
        return LongStream.range(1, sizeTable + 1).boxed().map(this::byContent).toList();
    }

    public List<TagResponse> withPageable() {
        return LongStream.range(6, 9).boxed().map(i -> byContent(sizeTable - i)).toList();
    }

    public List<TagResponse> withFilter() {
        return List.of(byContent(8L));
    }

    public List<TagResponse> empty() {
        return Collections.emptyList();
    }

    public TagResponse byContent(Long id) {
        return TagResBuilder.aTagRes().withId(id).withName("n" + id).build();
    }

    public TagResponse mostUsedByUser() {
        return byContent(1L);
    }

    public TagResponse byContent(Long id, String name) {
        return TagResBuilder.aTagRes().withId(id).withName(name).build();
    }

}