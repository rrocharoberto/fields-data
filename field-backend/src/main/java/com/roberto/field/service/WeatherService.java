package com.roberto.field.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.ReactiveHttpInputMessage;


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

import reactor.core.publisher.Mono;

/**
 * Provides the CRUD operations for Rest API.
 * 
 * @author roberto
 *
 */
@Service
public class WeatherService {

	Logger logger = LoggerFactory.getLogger(WeatherService.class);
	
	// http://api.agromonitoring.com/agro/1.0/polygons?appid={appid}
	private String polygonAPIURL;
	
	//http://api.agromonitoring.com/agro/1.0/weather/history?polyid={polygonId}&appid={appId}&start={startPeriod}&end={endPeriod}
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

	public WeatherHistory retrieveWeatherHistory(String fieldId) {

		// retrieve field/polygon info from database
		Optional<FieldEntity> fieldEntity = dao.findById(fieldId);
		if(!fieldEntity.isPresent()) {
			throw new FieldNotFoundException("Field not found with id: " + fieldId);
		}
		
		GeoData geoJson = fieldConverter.convertCoordinateEntityToJSON(fieldEntity.get().getBoundary().getCoordinates());

		// create polygon request
		PolygonDataRequest polygonReq = new PolygonDataRequest();
		polygonReq.setName(fieldId); // using the fieldId as name of polygon
		polygonReq.setGeo_json(geoJson);

		PolygonDataResponse polygonResponse = this.doCreatePolygon(polygonReq);
		logger.info("Polygon created id: ", polygonResponse.getId());
		
		List<HistoricalWeatherData> historicalWeather = getHistoricalWeather(polygonResponse.getId());
		
		WeatherHistory weatherData = whConverter.convertHistoricalWeatherToJSON(historicalWeather);
		
		return weatherData;
	}

	private PolygonDataResponse doCreatePolygon(PolygonDataRequest polygonReq) {
		
		String url = polygonAPIURL + "?" + "appid=" + appId;
		logger.info(url);

		WebClient webClient = WebClient.builder().baseUrl(url).filter(logRequest()).filter(logResponse())
				.build();

		 PolygonDataResponse response = webClient.post().contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.just(polygonReq), PolygonDataRequest.class).accept(MediaType.APPLICATION_JSON).retrieve()
				.bodyToFlux(PolygonDataResponse.class).blockFirst();

		return response;
	}
	
	private List<HistoricalWeatherData> getHistoricalWeather(String polygonId) {
		
		String url = historicalWeatherAPIURL
						.concat("?appid=").concat(appId)
						.concat("&polyid=").concat(polygonId)
						.concat("&start=").concat(getNow())
						.concat("&end=").concat(getSevenDaysBehind());
		
		logger.info(url);

		List<HistoricalWeatherData> list = WebClient.create(url)
		        .get()
		        .accept(MediaType.APPLICATION_JSON)
		        .retrieve()
		        .bodyToFlux(HistoricalWeatherData.class)
		        .collectList()
		        .log()
		        .block();
		return list;
	}
	
	private String getNow() {
		LocalDateTime now = LocalDateTime.now();
				
		return Long.toString(now.toEpochSecond(ZoneOffset.UTC));
	}
	private String getSevenDaysBehind() {
		LocalDateTime sevenDaysBehind = LocalDateTime.now().minusDays(6);
		
		return Long.toString(sevenDaysBehind.toEpochSecond(ZoneOffset.UTC));
	}

	private ExchangeFilterFunction logRequest() {
		return (clientRequest, next) -> {
			logger.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
			
			clientRequest.headers().forEach((name, values) -> values.forEach(value -> logger.info("{}={}", name, value)));
			logger.debug("Body: ", clientRequest.body());
			return next.exchange(clientRequest);
		};
	}

	private ExchangeFilterFunction logResponse() {
		return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
			
			logger.debug("Response: {}", clientResponse.headers().asHttpHeaders().get("property-header"));
			
			return Mono.just(clientResponse);
		});
	}

}
