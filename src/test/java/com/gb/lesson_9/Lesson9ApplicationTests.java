package com.gb.lesson_9;

import com.gb.lesson_9.controllers.TemperatureController;
import com.gb.lesson_9.domain.DateMapper;
import com.gb.lesson_9.domain.TemperatureData;
import com.gb.lesson_9.sevices.persistence.TemperatureRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

//@WebMvcTest(TemperatureController.class)
@SpringBootTest(classes = {Lesson9Application.class})
@AutoConfigureMockMvc
class Lesson9ApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	TemperatureRepository repository;

	Gson gson = new Gson();

	@ParameterizedTest
	@MethodSource("getListAll")
	void testGetAll(List<TemperatureData> expectedList) throws Exception {
		Mockito.when(repository.getAll()).thenReturn(expectedList);
		String mvcResult  = mockMvc.perform(MockMvcRequestBuilders.get("/temperatures").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		List<TemperatureData> actualList = Arrays.stream(gson.fromJson(mvcResult, TemperatureDataJson[].class)).map(TemperatureDataJson::getTemperatureData).toList();
		assertThat(expectedList, is(actualList));
	}

	@Test
	void testSave() throws Exception {
		TemperatureData expectedTemperature = new TemperatureData();
		expectedTemperature.setId(1);
		expectedTemperature.setTemperature(5);
		expectedTemperature.setDate(LocalDate.now());

		Mockito.when(repository.saveTemperature(expectedTemperature)).thenReturn(expectedTemperature.getId());

		String mvcResult  = mockMvc.perform(
				MockMvcRequestBuilders.post("/temperatures")
						.contentType(MediaType.APPLICATION_JSON).content(gson.toJson(TemperatureDataJson.of(expectedTemperature))))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		assert expectedTemperature.getId() == Integer.parseInt(mvcResult);

		Mockito.verify(repository, times(1)).saveTemperature(expectedTemperature);
	}

	@Test
	void testPositivePut() throws Exception {
		TemperatureData oldTemperature = new TemperatureData();
		oldTemperature.setId(1);
		oldTemperature.setTemperature(5);
		oldTemperature.setDate(LocalDate.now());

		TemperatureData updatedTemperature = new TemperatureData();
		updatedTemperature.setId(1);
		updatedTemperature.setTemperature(6);
		updatedTemperature.setDate(LocalDate.now().minusDays(2));

		Mockito.when(repository.getTemperatureById(1)).thenReturn(oldTemperature);
		Mockito.when(repository.updateTemperature(updatedTemperature)).thenReturn(updatedTemperature);

		String mvcResult  = mockMvc.perform(
						MockMvcRequestBuilders.put("/temperatures/{id}", oldTemperature.getId())
								.contentType(MediaType.APPLICATION_JSON)
								.content(gson.toJson(TemperatureDataJson.of(updatedTemperature))))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		TemperatureData actTemperature = gson.fromJson(mvcResult, TemperatureDataJson.class).getTemperatureData();
		assert actTemperature.equals(updatedTemperature);
	}

	@Test
	void testNegativePut() throws Exception {
		TemperatureData oldTemperature = new TemperatureData();
		oldTemperature.setId(1);
		oldTemperature.setTemperature(5);
		oldTemperature.setDate(LocalDate.now());

		TemperatureData updatedTemperature = new TemperatureData();
		updatedTemperature.setId(3);
		updatedTemperature.setTemperature(6);
		updatedTemperature.setDate(LocalDate.now().minusDays(2));

		Mockito.when(repository.getTemperatureById(1)).thenReturn(oldTemperature);

		String errorMessage  = mockMvc.perform(
						MockMvcRequestBuilders.put("/temperatures/{id}", updatedTemperature.getId())
								.contentType(MediaType.APPLICATION_JSON)
								.content(gson.toJson(TemperatureDataJson.of(updatedTemperature))))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

		assert errorMessage.equals("Temperature was not found");
	}

	@Test
	void testPositiveDelete() throws Exception {
		TemperatureData temperature = new TemperatureData();
		temperature.setId(1);
		temperature.setTemperature(5);
		temperature.setDate(LocalDate.now());

		Mockito.when(repository.saveTemperature(temperature)).thenReturn(temperature.getId());

		mockMvc.perform(
						MockMvcRequestBuilders.post("/temperatures")
								.contentType(MediaType.APPLICATION_JSON).content(gson.toJson(TemperatureDataJson.of(temperature))))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		Mockito.when(repository.deleteTemperatureById(temperature.getId())).thenReturn(temperature.getId());
		Mockito.when(repository.getTemperatureById(temperature.getId())).thenReturn(temperature);

		mockMvc.perform(
						MockMvcRequestBuilders.delete("/temperatures/{id}", temperature.getId())
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		Mockito.verify(repository, times(1)).deleteTemperatureById(temperature.getId());
	}

	@Test
	void testNegativeDelete() throws Exception {
		TemperatureData temperature = new TemperatureData();
		temperature.setId(1);
		temperature.setTemperature(5);
		temperature.setDate(LocalDate.now());

		Mockito.when(repository.saveTemperature(temperature)).thenReturn(temperature.getId());

		mockMvc.perform(
						MockMvcRequestBuilders.post("/temperatures")
								.contentType(MediaType.APPLICATION_JSON).content(gson.toJson(TemperatureDataJson.of(temperature))))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		Mockito.when(repository.deleteTemperatureById(temperature.getId())).thenReturn(temperature.getId());
		Mockito.when(repository.getTemperatureById(temperature.getId())).thenReturn(temperature);

		String errorMessage = mockMvc.perform(
						MockMvcRequestBuilders.delete("/temperatures/{id}", 2)
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

		assert errorMessage.equals("Temperature was not found");
	}

	@Test
	void testPositiveGetById() throws Exception {
		TemperatureData temperature = new TemperatureData();
		temperature.setId(1);
		temperature.setTemperature(5);
		temperature.setDate(LocalDate.now());

		Mockito.when(repository.getTemperatureById(temperature.getId())).thenReturn(temperature);

		String mvcResult = mockMvc.perform(
						MockMvcRequestBuilders.get("/temperatures/{id}", temperature.getId())
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		TemperatureData actTemperature = gson.fromJson(mvcResult, TemperatureDataJson.class).getTemperatureData();
		assert actTemperature.equals(temperature);
	}

	@Test
	void testNegativeGetById() throws Exception {
		TemperatureData temperature = new TemperatureData();
		temperature.setId(1);
		temperature.setTemperature(5);
		temperature.setDate(LocalDate.now());

		Mockito.when(repository.getTemperatureById(temperature.getId())).thenReturn(temperature);

		String errorMessage = mockMvc.perform(
						MockMvcRequestBuilders.get("/temperatures/{id}", 2)
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

		assert errorMessage.equals("Temperature was not found");
	}

	private static Stream getListAll() {
		List<TemperatureData> regularTemperatureList = new ArrayList<>();
		TemperatureData regularTemperature1 = new TemperatureData();
		regularTemperature1.setId(1);
		regularTemperature1.setTemperature(5);
		regularTemperature1.setDate(LocalDate.now());

		TemperatureData regularTemperature2 = new TemperatureData();
		regularTemperature2.setId(2);
		regularTemperature2.setTemperature(10);
		regularTemperature2.setDate(LocalDate.now());

		TemperatureData regularTemperature3 = new TemperatureData();
		regularTemperature3.setId(3);
		regularTemperature3.setTemperature(100);
		regularTemperature3.setDate(LocalDate.now());

		regularTemperatureList.add(regularTemperature1);
		regularTemperatureList.add(regularTemperature2);
		regularTemperatureList.add(regularTemperature3);

		List<TemperatureData> emptyList = new ArrayList<>();

		return Stream.of(regularTemperatureList, emptyList);
	}

}
