package ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.mappers;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class DateMapperTest {

    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2024, 1, 2, 3, 4, 5, 6);

    private final DateMapper dateMapper = new DateMapper();

    @Test
    void asOffsetDateTimeReturnsNullForNullTimestamp() {
        assertThat(dateMapper.asOffsetDateTime(null)).isNull();
    }

    @Test
    void asOffsetDateTimeConvertsTimestampAsUtc() {
        OffsetDateTime result = dateMapper.asOffsetDateTime(Timestamp.valueOf(LOCAL_DATE_TIME));

        assertThat(result).isEqualTo(OffsetDateTime.of(LOCAL_DATE_TIME, ZoneOffset.UTC));
    }

    @Test
    void asTimestampReturnsNullForNullOffsetDateTime() {
        assertThat(dateMapper.asTimestamp(null)).isNull();
    }

    @Test
    void asTimestampConvertsOffsetDateTimeAsUtc() {
        Timestamp result = dateMapper.asTimestamp(OffsetDateTime.of(LOCAL_DATE_TIME, ZoneOffset.UTC));

        assertThat(result).isEqualTo(Timestamp.valueOf(LOCAL_DATE_TIME));
    }

}
