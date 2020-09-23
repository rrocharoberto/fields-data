package com.roberto.field.test.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.roberto.field.dao.FieldDAO;
import com.roberto.field.dto.GeoData;
import com.roberto.field.dto.heatherHistory.HistoricalWeatherData;
import com.roberto.field.dto.heatherHistory.MainWeatherData;
import com.roberto.field.dto.heatherHistory.PolygonDataRequest;
import com.roberto.field.dto.heatherHistory.PolygonDataResponse;
import com.roberto.field.dto.heatherHistory.WeatherData;
import com.roberto.field.dto.heatherHistory.WeatherHistory;
import com.roberto.field.entities.BoundaryEntity;
import com.roberto.field.entities.CoordinateEntity;
import com.roberto.field.entities.FieldEntity;
import com.roberto.field.service.WeatherService;
import com.roberto.field.util.WeatherServiceDataRetriever;

public class WeatherServiceTests {

	@Mock
	private FieldDAO fielDAO;
	@Mock
	private WeatherServiceDataRetriever dataRetriever;

	@InjectMocks
	private WeatherService service;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCreateFieldExistingPolygon() throws Exception {

		// prepate the data
		String fieldId = "fieldId-12345";
		String boundaryId = "boundaryId-12345";
		String polyResponseId = "polyResponseId-12345";
		FieldEntity fieldEntity = getFieldEntity(fieldId, boundaryId, null);

		testRetrieveWeatherHistoryData(fieldId, boundaryId, polyResponseId, fieldEntity);
	}

	@Test
	public void testCreateFieldNonExistingPolygon() throws Exception {

		// prepate the data
		String fieldId = "fieldId-12345";
		String boundaryId = "boundaryId-12345";
		String polyResponseId = "polyResponseId-12345";
		FieldEntity fieldEntity = getFieldEntity(fieldId, boundaryId, polyResponseId);

		testRetrieveWeatherHistoryData(fieldId, boundaryId, polyResponseId, fieldEntity);
	}
	
	private void testRetrieveWeatherHistoryData(String fieldId, String boundaryId, String polyResponseId, FieldEntity fieldEntity) {

		GeoData geo_json = new GeoData();
		PolygonDataRequest polygonReq = new PolygonDataRequest(fieldId, geo_json);
		PolygonDataResponse ploygonResponse = new PolygonDataResponse();
		ploygonResponse.setId(polyResponseId);
		List<HistoricalWeatherData> historicalWeatherDataResponse = getHistoricalWeatherDataResponse();

		// configure mock

		Mockito.when(fielDAO.findById(fieldId)).thenReturn(Optional.of(fieldEntity));
		Mockito.when(dataRetriever.doCreatePolygon(polygonReq)).thenReturn(ploygonResponse);
		Mockito.when(dataRetriever.doRetrieveHistoricalWeather(polyResponseId))
				.thenReturn(historicalWeatherDataResponse);

		//call the service
		WeatherHistory weatherHistoryResponse = service.retrieveWeatherHistory(fieldId);

		//check the results
		assertNotNull(weatherHistoryResponse);

		WeatherData[] weatherDataResp = weatherHistoryResponse.getWeatherData();

		assertNotNull(weatherDataResp);
		assertEquals(historicalWeatherDataResponse.size(), weatherDataResp.length);

		for (int i = 0; i < weatherDataResp.length; i++) {
			WeatherData actual = weatherDataResp[i];
			HistoricalWeatherData expected = historicalWeatherDataResponse.get(i);

			assertNotNull(expected.getMain());
			assertEquals(expected.getMain().getHumidity(), actual.getHumidity());
			assertEquals(expected.getMain().getTemp(), actual.getTemperature());
			assertEquals(expected.getMain().getTemp_max(), actual.getTemperatureMax());
			assertEquals(expected.getMain().getTemp_min(), actual.getTemperatureMin());
		}
	}

	private List<HistoricalWeatherData> getHistoricalWeatherDataResponse() {
		long dt = 54321;

		List<HistoricalWeatherData> historicalWeatherDataResponse = new ArrayList<HistoricalWeatherData>();
		HistoricalWeatherData weatherData = new HistoricalWeatherData();
		weatherData.setDt(dt);
		MainWeatherData mainWeatherData = new MainWeatherData();
		mainWeatherData.setHumidity(50);
		mainWeatherData.setTemp(5);
		mainWeatherData.setTemp_max(10);
		mainWeatherData.setTemp_min(1);

		weatherData.setMain(mainWeatherData);

		historicalWeatherDataResponse.add(weatherData);
		return historicalWeatherDataResponse;
	}

	// these data are the same as the PDF, (but not the ids)

	private FieldEntity getFieldEntity(String fieldId, String boundaryId, String polygonId) {
		FieldEntity field = new FieldEntity();
		field.setId(fieldId);
		field.setName("Potato field");
		field.setCountryCode("DEU");

		BoundaryEntity boundary = new BoundaryEntity();
		boundary.setId(boundaryId);
		boundary.setPolygonId(polygonId);

		boundary.setCoordinates(getCoordinates());
		field.setBoundary(boundary);
		return field;
	}

	private List<CoordinateEntity> getCoordinates() {
		CoordinateEntity[] coordinates = new CoordinateEntity[] {
				new CoordinateEntity(new BigDecimal(-5.553604888914691), new BigDecimal(33.88229680420605)),
				new CoordinateEntity(new BigDecimal(-5.5516736984239685), new BigDecimal(33.88229680420605)),
				new CoordinateEntity(new BigDecimal(-5.5516736984239685), new BigDecimal(33.88372189858022)),
				new CoordinateEntity(new BigDecimal(-5.555965232847882), new BigDecimal(33.88390003370375)),
				new CoordinateEntity(new BigDecimal(-5.555965232847882), new BigDecimal(33.88229680420605)),
				new CoordinateEntity(new BigDecimal(-5.553604888914691), new BigDecimal(33.88229680420605)) };

		return Arrays.asList(coordinates);
	}
}
