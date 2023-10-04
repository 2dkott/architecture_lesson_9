package com.gb.lesson_9.controllers;

import com.gb.lesson_9.domain.TemperatureData;
import com.gb.lesson_9.sevices.persistence.TemperatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TemperatureController {
    TemperatureRepository temperatureRepository;


    @Autowired
    public TemperatureController(TemperatureRepository temperatureRepository) {
        this.temperatureRepository = temperatureRepository;
    }

    @GetMapping("/temperatures")
    public List<TemperatureData> getAll() {
        return temperatureRepository.getAll();
    }

    @GetMapping("/temperatures/{id}")
    public TemperatureData getTemperatureById(@PathVariable int id) {
        return temperatureRepository.getTemperatureById(id);
    }

    @PostMapping("/temperatures")
    public int addTemperature(@RequestBody TemperatureData temperatureData) {
        return temperatureRepository.saveTemperature(temperatureData);
    }

    @PutMapping("/temperatures/{id}")
    public void putTemperature(@RequestBody TemperatureData updatedTemperatureData, @PathVariable int id) {
        TemperatureData temperatureData = temperatureRepository.getTemperatureById(id);
        temperatureData.setTemperature(updatedTemperatureData.getTemperature());
        temperatureData.setDate(updatedTemperatureData.getDate());
    }

    @DeleteMapping("/temperatures/{id}")
    public void deleteTemperature(@PathVariable int id) {
        temperatureRepository.deleteTemperatureById(id);
    }
}
