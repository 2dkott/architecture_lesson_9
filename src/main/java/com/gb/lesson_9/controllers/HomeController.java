package com.gb.lesson_9.controllers;

import com.gb.lesson_9.domain.DateMapper;
import com.gb.lesson_9.domain.TemperatureData;
import com.gb.lesson_9.sevices.persistence.TemperatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    TemperatureRepository temperatureRepository;

    @GetMapping
    public String getHome(Model model) {
        List<TemperatureData> temperatureDataList = temperatureRepository.getAll();
        model.addAttribute("temperatures", temperatureDataList);
        return "index";
    }

    @RequestMapping("/submit")
    public String submitTemperature(@RequestParam(value = "date") String date, @RequestParam(value = "temperature") String temperature) {
        TemperatureData temp = new TemperatureData();
        temp.setTemperature(Integer.parseInt(temperature));
        temp.setDate(DateMapper.mapStringToLocalDate(date));
        temperatureRepository.saveTemperature(temp);
        return "redirect:/";
    }

    @RequestMapping("/update")
    public String updateTemperature(@RequestParam(value = "id") String id,
                                    @RequestParam(value = "date") String date,
                                    @RequestParam(value = "temperature") String temperature) {
        TemperatureData temp = new TemperatureData();
        temp.setId(Integer.parseInt(id));
        temp.setTemperature(Integer.parseInt(temperature));
        temp.setDate(DateMapper.mapStringToLocalDate(date));
        temperatureRepository.updateTemperature(temp);
        return "redirect:/";
    }

    @RequestMapping("/delete")
    public String updateTemperature(@RequestParam(value = "id") String id) {
        temperatureRepository.deleteTemperatureById(Integer.parseInt(id));
        return "redirect:/";
    }

}
