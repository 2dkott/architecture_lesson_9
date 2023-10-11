package com.gb.lesson_9;


import com.gb.lesson_9.domain.DateMapper;
import com.gb.lesson_9.domain.TemperatureData;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TemperatureDataJson {

    private String date;

    private int temperature;

    private int id;

    public static TemperatureDataJson of(TemperatureData temperatureData) {
        TemperatureDataJson t = new TemperatureDataJson();
        t.date = temperatureData.getDate().toString();
        t.temperature = temperatureData.getTemperature();
        t.id = temperatureData.getId();
        return t;
    }

    public TemperatureData getTemperatureData() {
        TemperatureData t = new TemperatureData();
        t.setTemperature(temperature);
        t.setDate(DateMapper.mapStringToLocalDate(date));
        t.setId(id);
        return t;
    }




}
