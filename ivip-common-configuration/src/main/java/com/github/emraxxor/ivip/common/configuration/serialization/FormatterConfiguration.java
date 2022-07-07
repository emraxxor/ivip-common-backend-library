package com.github.emraxxor.ivip.common.configuration.serialization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;


@Configuration
public class FormatterConfiguration {

    @Bean
    public Formatter<Instant> instantFormatter() {
        return new Formatter<>() {
            @Override
            @NonNull
            public Instant parse(@NonNull String text, @NonNull Locale locale) {
                return Instant.parse(text);
            }

            @Override
            @NonNull
            public String print(@NonNull Instant object, @NonNull Locale locale) {
                return DateTimeFormatter.ISO_INSTANT.format(object);
            }
        };
    }

    @Bean
    public Formatter<LocalDate> localDateFormatter() {
        return new Formatter<>() {
            @Override
            @NonNull
            public LocalDate parse(@NonNull String text, @NonNull Locale locale) {
                try {
                    return LocalDate.parse(text, DateTimeFormatter.ISO_DATE);
                } catch (DateTimeParseException ex) {
                    return LocalDate.parse(text, DateTimeFormatter.ISO_DATE_TIME);
                }
            }

            @Override
            @NonNull
            public String print(@NonNull LocalDate object, @NonNull Locale locale) {
                return object.toString();
            }
        };
    }

    @Bean
    public Formatter<LocalDateTime> localDatetimeFormatter() {
        return new Formatter<>() {
            @Override
            @NonNull
            public LocalDateTime parse(@NonNull String text, @NonNull Locale locale) {
                return LocalDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME);
            }

            @Override
            @NonNull
            public String print(@NonNull LocalDateTime object, @NonNull Locale locale) {
                return object.toString();
            }
        };
    }

    @Bean
    public Formatter<LocalTime> localTimeFormatter() {
        return new Formatter<>() {
            @Override
            @NonNull
            public LocalTime parse(@NonNull String text, @NonNull Locale locale) {
                return LocalTime.parse(text, DateTimeFormatter.ISO_TIME);
            }

            @Override
            @NonNull
            public String print(@NonNull LocalTime object, @NonNull Locale locale) {
                return object.toString();
            }
        };
    }
}
