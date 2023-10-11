package com.gb.lesson_9.sevices.persistence;

import com.gb.lesson_9.domain.TemperatureData;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TempTemperatureRepository implements TemperatureRepository{

    private final JdbcTemplate jdbcTemplate;

    public TempTemperatureRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        String create_query  = "CREATE TABLE IF NOT EXISTS temperature (temp_date text,temperature_c INTEGER)";
        jdbcTemplate.execute(create_query);
    }

    @Override
    public int saveTemperature(TemperatureData temperatureData) {
        String addTemperature = String.format("INSERT INTO temperature (temp_date,temperature_c) values ('%s',%s)", temperatureData.getDate(), temperatureData.getTemperature());
        jdbcTemplate.execute(addTemperature);
        return Integer.parseInt(jdbcTemplate.queryForList("select max(rowid) from temperature").get(0).get("max(rowid)").toString());
    }

    @Override
    public TemperatureData getTemperatureById(int temperatureId) {
        String getTemperatureQuery = String.format("select rowid, * from temperature where rowid=%s", temperatureId);
        List<TemperatureData> temperatureList = jdbcTemplate.query(getTemperatureQuery, (resultSet, rowNum) -> {
            TemperatureData temperature = new TemperatureData();
            temperature.setTemperature(resultSet.getInt("temperature_c"));
            temperature.setDate(mapStringToLocalDate(resultSet.getString("temp_date")));
            temperature.setId(resultSet.getInt("rowid"));
            return temperature;
        });

        return temperatureList.stream().filter(temperatureData -> temperatureData.getId()==temperatureId).findFirst().orElseThrow();
    }

    @Override
    public int deleteTemperatureById(int temperatureId) {
        String getAllTemperatures = String.format("delete from temperature where rowid=%s", temperatureId);
        jdbcTemplate.execute(getAllTemperatures);
        return temperatureId;
    }

    @Override
    public List<TemperatureData> getAll() {
        String getAllTemperatures = "select rowid, * from temperature";
        return jdbcTemplate.query(getAllTemperatures, (resultSet, rowNum) -> {
            TemperatureData temperature = new TemperatureData();
            temperature.setTemperature(resultSet.getInt("temperature_c"));
            temperature.setDate(mapStringToLocalDate(resultSet.getString("temp_date")));
            temperature.setId(resultSet.getInt("rowid"));
            return temperature;
        });
    }

    @Override
    public TemperatureData updateTemperature(TemperatureData temperatureData) {
        String updateTemperature = String.format("update temperature set temp_date='%s',temperature_c=%s where rowid=%s", temperatureData.getDate(), temperatureData.getTemperature(), temperatureData.getId());
        jdbcTemplate.execute(updateTemperature);
        return getTemperatureById(temperatureData.getId());
    }

    private LocalDate mapStringToLocalDate(String localDate) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(localDate,pattern);
    }
}
