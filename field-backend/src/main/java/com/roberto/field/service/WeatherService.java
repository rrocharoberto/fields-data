package com.roberto.field.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.roberto.field.entities.CoordinateEntity;
import com.roberto.field.entities.FieldEntity;
import com.roberto.field.util.FieldDataConverter;
import com.roberto.field.util.WeatherHistoryConverter;
import com.roberto.field.util.WeatherServiceDataRetriever;

/**
 * Retrieves the weather historical data from external API.
 * 
 * @author roberto
 *
 */
@Service
public class WeatherService {

	private Logger logger = LoggerFactory.getLogger(WeatherService.class);

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
		
		Optional<FieldEntity> fieldEntityOptional = dao.findById(fieldId); // retrieve field/polygon info from database
		if(!fieldEntityOptional.isPresent()) {
			throw new FieldNotFoundException("Field not found with id: " + fieldId);
		}
		FieldEntity fieldEntity = fieldEntityOptional.get();
		
		String polygonId = fieldEntity.getBoundary().getPolygonId();
		if(polygonId == null) { //polygon not created yet in the OpenWeather API
			
			polygonId = createAndSavePolygon(fieldEntity);
		}
		
		List<HistoricalWeatherData> historicalWeather = weatherDataRetriever.doRetrieveHistoricalWeather(polygonId);
		if(historicalWeather == null) {
			throw new FieldException("No historical weather data received from external API. Field: " + fieldId);
		}
		
		return whConverter.convertHistoricalWeatherToJSON(historicalWeather);
	}

	private String createAndSavePolygon(FieldEntity fieldEntity) {
		//add the first coordinate at the end of coordinates: API constraint: 
		//"When creating a polygon, the first and last positions are equivalent, and they MUST contain identical values"
		
		List<CoordinateEntity> coordinates = fieldEntity.getBoundary().getCoordinates();
		List<CoordinateEntity> newList = new ArrayList<CoordinateEntity>();
		newList.addAll(coordinates);
		if(!coordinates.isEmpty()) {
			newList.add(coordinates.get(0));
		}
		GeoData geoJson = fieldConverter.convertCoordinateEntityToJSON(newList);
		
		// create polygon request
		PolygonDataRequest polygonReq = new PolygonDataRequest(fieldEntity.getId(), geoJson); // using the fieldId as name of polygon
		
		logger.info("createAndSavePolygon - polygonReq: " + polygonReq.getName());
		PolygonDataResponse polygonResponse = weatherDataRetriever.doCreatePolygon(polygonReq);
		if(polygonResponse == null) {
			throw new FieldException("No Polygon data received from external API. Field: " + fieldEntity.getId());
		}
		logger.info("createAndSavePolygon - polygonResponse: " + polygonResponse.getId());

		//save the polygon id for future requests
		fieldEntity.getBoundary().setPolygonId(polygonResponse.getId());
		
		dao.save(fieldEntity);
		return polygonResponse.getId();
	}

}
