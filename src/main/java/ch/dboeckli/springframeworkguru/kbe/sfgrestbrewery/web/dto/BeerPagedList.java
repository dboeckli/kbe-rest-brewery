package ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import tools.jackson.databind.JsonNode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class BeerPagedList extends PageImpl<BeerDto> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1114715135625836949L;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BeerPagedList(@JsonProperty("content") List<BeerDto> content,
                         @JsonProperty("number") Integer number,
                         @JsonProperty("size") Integer size,
                         @JsonProperty("totalElements") Long totalElements,
                         @JsonProperty("pageable") JsonNode pageable,
                         @JsonProperty("last") Boolean last,
                         @JsonProperty("totalPages") Integer totalPages,
                         @JsonProperty("sort") JsonNode sort,
                         @JsonProperty("first") Boolean first,
                         @JsonProperty("numberOfElements") Integer numberOfElements) {

        super(content,
            PageRequest.of(number != null ? number : 0, size != null ? size : 25),
            totalElements != null ? totalElements : 0L);
    }

    public BeerPagedList(List<BeerDto> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public BeerPagedList(List<BeerDto> content) {
        super(content, PageRequest.of(0, content.isEmpty() ? 1 : content.size()), content.size());
    }
}
