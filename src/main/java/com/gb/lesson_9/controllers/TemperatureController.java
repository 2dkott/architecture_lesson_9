package com.gb.lesson_9.controllers;

import com.gb.lesson_9.domain.TemperatureData;
import com.gb.lesson_9.sevices.persistence.TemperatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity getTemperatureById(@PathVariable int id) {
        TemperatureData result = temperatureRepository.getTemperatureById(id);
        if (result != null) return ResponseEntity.ok(result);
        return ResponseEntity.badRequest().body("Temperature was not found");
    }

    @PostMapping("/temperatures")
    public int addTemperature(@RequestBody TemperatureData temperatureData) {
        return temperatureRepository.saveTemperature(temperatureData);
    }

    @PutMapping("/temperatures/{id}")
    public ResponseEntity putTemperature(@RequestBody TemperatureData updatedTemperatureData, @PathVariable int id) {
        TemperatureData result = temperatureRepository.getTemperatureById(id);
        if (result != null) {
            try {
                result.setTemperature(updatedTemperatureData.getTemperature());
                result.setDate(updatedTemperatureData.getDate());
                result = temperatureRepository.updateTemperature(result);
                return ResponseEntity.ok(result);
            }
            catch (Exception e) {
                return ResponseEntity.badRequest().body(e);
            }

        }
        return ResponseEntity.badRequest().body("Temperature was not found");

    }

    @DeleteMapping("/temperatures/{id}")
    public ResponseEntity deleteTemperature(@PathVariable int id) {
        TemperatureData result = temperatureRepository.getTemperatureById(id);
        if (result != null) {
            try {
                temperatureRepository.deleteTemperatureById(id);
                return ResponseEntity.ok(result);
            }
            catch (Exception e) {
                return ResponseEntity.badRequest().body(e);
            }
        }
        return ResponseEntity.badRequest().body("Temperature was not found");

    }
}
