package com.gb.lesson_9.sevices.persistence;

import com.gb.lesson_9.domain.TemperatureData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TempTemperatureRepository implements TemperatureRepository{

    List<TemperatureData> temperatureList = new ArrayList<>();

    @Override
    public int saveTemperature(TemperatureData temperatureData) {
        temperatureList.add(temperatureData);
        return temperatureData.getId();
    }

    @Override
    public TemperatureData getTemperatureById(int temperatureId) {
        return temperatureList.stream().filter(temperatureData -> temperatureData.getId()==temperatureId).findFirst().orElseThrow();
    }

    @Override
    public void deleteTemperatureById(int temperatureId) {
        TemperatureData toDeleteTemperature = temperatureList.stream().filter(temperatureData -> temperatureData.getId()==temperatureId).findFirst().orElseThrow();
        temperatureList.remove(toDeleteTemperature);
    }

    @Override
    public List<TemperatureData> getAll() {
        return temperatureList;
    }
}
