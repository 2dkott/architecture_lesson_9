package com.gb.lesson_9.sevices.persistence;

import com.gb.lesson_9.domain.TemperatureData;

import java.util.List;

public interface TemperatureRepository {
    int saveTemperature(TemperatureData temperatureData);
    TemperatureData getTemperatureById(int temperatureId);
    void deleteTemperatureById(int temperatureId);

    List<TemperatureData> getAll();
}
