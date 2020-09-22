package com.roberto.field.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.roberto.field.controller.support.FieldAPIException;
import com.roberto.field.controller.support.FieldException;
import com.roberto.field.controller.support.FieldNotFoundException;
import com.roberto.field.dao.FieldDAO;
import com.roberto.field.dto.GeoData;
import com.roberto.field.dto.heatherHistory.HistoricalWeatherData;
import com.roberto.field.dto.heatherHistory.PolygonDataRequest;
import com.roberto.field.dto.heatherHistory.PolygonDataResponse;
import com.roberto.field.dto.heatherHistory.WeatherHistory;
import com.roberto.field.entities.FieldEntity;
import com.roberto.field.util.DateUtil;
import com.roberto.field.util.FieldDataConverter;
import com.roberto.field.util.WeatherHistoryConverter;

import reactor.core.publisher.Mono;

/**
 * Retrieves the weather historical data from external API.
 * 
 * @author roberto
 *
 */
@Service
public class WeatherService {

	private Logger logger = LoggerFactory.getLogger(WeatherService.class);
	
	private String polygonAPIURL;
	private String historicalWeatherAPIURL;
	private String appId;

	@Autowired
	public FieldDAO dao;

	private FieldDataConverter fieldConverter = new FieldDataConverter();
	private WeatherHistoryConverter whConverter = new WeatherHistoryConverter();
	
	@Autowired
	public WeatherService(@Value("${polygon.rest.url}") String thePolygonAPIURL,
			@Value("${historical.weather.rest.url}") String theHistoricalWeatherAPIURL,
			@Value("${weather.appid}") String theAppId) {

		this.polygonAPIURL = thePolygonAPIURL;
		this.historicalWeatherAPIURL = theHistoricalWeatherAPIURL;
		this.appId = theAppId;
	}

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

		PolygonDataResponse polygonResponse = this.doCreatePolygon(polygonReq);
		if(polygonResponse == null) {
			throw new FieldException("No Polygon data received from external API. Field: " + fieldId);
		}
		
		//TODO: save the polygon id for future requests
		
		List<HistoricalWeatherData> historicalWeather = this.doRetrieveHistoricalWeather(polygonResponse.getId());
		if(historicalWeather == null) {
			throw new FieldException("No historical weather data received from external API. Field: " + fieldId);
		}
		
		return whConverter.convertHistoricalWeatherToJSON(historicalWeather);
	}

	/**
	 * Perform polygon create at OpenWeather API
	 * @param polygonReq
	 * @return
	 */
	private PolygonDataResponse doCreatePolygon(PolygonDataRequest polygonReq) {
		
		String url = polygonAPIURL + "?" + "appid=" + appId;
		logger.info(url);

		try { 
			WebClient webClient = WebClient.builder().baseUrl(url).build();
	
			PolygonDataResponse response = webClient.post()
					.contentType(MediaType.APPLICATION_JSON)
					.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
					.body(Mono.just(polygonReq), PolygonDataRequest.class)
					.accept(MediaType.APPLICATION_JSON)
					.retrieve()
					.bodyToFlux(PolygonDataResponse.class)
					.blockFirst();
	
			logger.info("Polygon created id: ", response.getId());
			return response;
		} catch (Exception ex) {
			throw new FieldAPIException(ex);
		}
	}
	
	/**
	 * Retrieve weather history from OpenWeather API
	 * @param polygonId
	 * @return
	 */
	private List<HistoricalWeatherData> doRetrieveHistoricalWeather(String polygonId) {
		
		String url = historicalWeatherAPIURL
						.concat("?appid=").concat(appId)
						.concat("&polyid=").concat(polygonId)
						.concat("&start=").concat(DateUtil.getNow())
						.concat("&end=").concat(DateUtil.getSevenDaysBehind());
		
		logger.info(url);

		try {
			List<HistoricalWeatherData> list = WebClient.create(url)
			        .get()
			        .accept(MediaType.APPLICATION_JSON)
			        .retrieve()
			        .bodyToFlux(HistoricalWeatherData.class)
			        .collectList()
			        .log()
			        .block();
			return list;
		} catch (Exception ex) {
			throw new FieldAPIException(ex);
		}
	}

}
