package com.gb.lesson_9.domain;


import lombok.Data;

import java.time.LocalDate;

@Data
public class TemperatureData {

    private LocalDate date;

    private int temperature;

    private int id;

    private static int counter = 1000;

    {
        id = ++counter;;
    }
}
