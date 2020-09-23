package com.roberto.field.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roberto.field.controller.support.FieldException;
import com.roberto.field.controller.support.FieldNotFoundException;
import com.roberto.field.dao.FieldDAO;
import com.roberto.field.dto.GeoData;
import com.roberto.field.dto.heatherHistory.HistoricalWeatherData;
import com.roberto.field.dto.heatherHistory.PolygonDataRequest;
import com.roberto.field.dto.heatherHistory.PolygonDataResponse;
import com.roberto.field.dto.heatherHistory.WeatherHistory;
import com.roberto.field.entities.FieldEntity;
import com.roberto.field.util.FieldDataConverter;
import com.roberto.field.util.WeatherHistoryConverter;

/**
 * Retrieves the weather historical data from external API.
 * 
 * @author roberto
 *
 */
@Service
public class WeatherService {

	@Autowired
	public FieldDAO dao;

	@Autowired
	private WeatherServiceDataRetriever weatherDataRetriever;

	private FieldDataConverter fieldConverter = new FieldDataConverter();
	private WeatherHistoryConverter whConverter = new WeatherHistoryConverter();
	
	/**
	 * Main business logic for retrieving weather history from OpenWeather Agro Monitoring API
	 * Steps:
	 * 1) retrieve field from database
	 * 2) convert field coordinates to geoJson
	 * 3) prepare polygon request
	 * 4) perform polygon create at OpenWeather API
	 * 5) retrieve weather history from OpenWeather API
	 * 6) convert to WeatherHistory internal format.
	 * @param fieldId
	 * @return
	 */
	public WeatherHistory retrieveWeatherHistory(String fieldId) {
		if(fieldId == null) {
			throw new FieldException("Field can not null.");
		}
		
		Optional<FieldEntity> fieldEntity = dao.findById(fieldId); // retrieve field/polygon info from database
		if(!fieldEntity.isPresent()) {
			throw new FieldNotFoundException("Field not found with id: " + fieldId);
		}
		
		GeoData geoJson = fieldConverter.convertCoordinateEntityToJSON(fieldEntity.get().getBoundary().getCoordinates());

		// create polygon request
		PolygonDataRequest polygonReq = new PolygonDataRequest(fieldId, geoJson); // using the fieldId as name of polygon

		PolygonDataResponse polygonResponse = weatherDataRetriever.doCreatePolygon(polygonReq);
		if(polygonResponse == null) {
			throw new FieldException("No Polygon data received from external API. Field: " + fieldId);
		}
		
		//TODO: save the polygon id for future requests
		
		List<HistoricalWeatherData> historicalWeather = weatherDataRetriever.doRetrieveHistoricalWeather(polygonResponse.getId());
		if(historicalWeather == null) {
			throw new FieldException("No historical weather data received from external API. Field: " + fieldId);
		}
		
		return whConverter.convertHistoricalWeatherToJSON(historicalWeather);
	}

}
