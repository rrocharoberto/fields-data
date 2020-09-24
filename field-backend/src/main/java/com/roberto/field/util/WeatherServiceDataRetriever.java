package com.roberto.field.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.roberto.field.controller.support.FieldAPIException;
import com.roberto.field.dto.heatherHistory.HistoricalWeatherData;
import com.roberto.field.dto.heatherHistory.PolygonDataRequest;
import com.roberto.field.dto.heatherHistory.PolygonDataResponse;

import reactor.core.publisher.Mono;

@Service
public class WeatherServiceDataRetriever {

	private Logger logger = LoggerFactory.getLogger(WeatherServiceDataRetriever.class);
	
	private String polygonAPIURL;
	private String historicalWeatherAPIURL;
	private String appId;

	@Autowired
	public WeatherServiceDataRetriever(@Value("${polygon.rest.url}") String thePolygonAPIURL,
			@Value("${historical.weather.rest.url}") String theHistoricalWeatherAPIURL,
			@Value("${weather.appid}") String theAppId) {

		this.polygonAPIURL = thePolygonAPIURL;
		this.historicalWeatherAPIURL = theHistoricalWeatherAPIURL;
		this.appId = theAppId;
	}
	
	/**
	 * Perform polygon create at OpenWeather API
	 * @param polygonReq
	 * @return
	 */
	public PolygonDataResponse doCreatePolygon(PolygonDataRequest polygonReq) {
		logger.info("doCreatePolygon");
		logger.debug("Polygon request name: " + polygonReq.getName());
		
		String url = polygonAPIURL + "?" + "appid=" + appId;
		logger.debug(url);

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
	
			logger.debug("Polygon created id: " + response.getId());
			return response;
		} catch (Exception ex) {
			throw new FieldAPIException(
					"Error retrieving data from Agro Monitoring Polygon API. Details: " + ex.getMessage(), ex);
		}
	}
	
	/**
	 * Retrieve weather history from OpenWeather API
	 * @param polygonId
	 * @return
	 */
	public List<HistoricalWeatherData> doRetrieveHistoricalWeather(String polygonId) {
		logger.info("doRetrieveHistoricalWeather");
		logger.debug("polygonId: " + polygonId);
		
		String url = historicalWeatherAPIURL
						.concat("?appid=").concat(appId)
						.concat("&polyid=").concat(polygonId)
						.concat("&start=").concat(DateUtil.getNow())
						.concat("&end=").concat(DateUtil.getSevenDaysBehind());
		

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
			throw new FieldAPIException(
					"Error retrieving data from Agro Monitoring Historical Weather API. Details: " + ex.getMessage(), ex);
		}
	}

}