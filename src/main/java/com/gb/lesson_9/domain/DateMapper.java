package com.gb.lesson_9.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateMapper {
    public static LocalDate mapStringToLocalDate(String localDate) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(localDate,pattern);
    }
}
