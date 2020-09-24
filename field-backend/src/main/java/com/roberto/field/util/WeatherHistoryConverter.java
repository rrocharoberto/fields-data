package com.roberto.field.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roberto.field.dto.heatherHistory.HistoricalWeatherData;
import com.roberto.field.dto.heatherHistory.WeatherData;
import com.roberto.field.dto.heatherHistory.WeatherHistory;
import com.roberto.field.service.WeatherService;

public class WeatherHistoryConverter {

	private Logger logger = LoggerFactory.getLogger(WeatherService.class);
	
	public WeatherHistoryConverter() {
	}

	/**
	 * Converts HistoricalWeatherData objects to WeatherHistory object.
	 * @param historicalWeather
	 * @return
	 */
	public WeatherHistory convertHistoricalWeatherToJSON(List<HistoricalWeatherData> historicalWeather) {
		logger.debug("convertHistoricalWeatherToJSON");
		
		WeatherData[] weatherDataArray = new WeatherData[historicalWeather.size()];

		for (int i = 0; i < weatherDataArray.length; i++) {
			HistoricalWeatherData hwData = historicalWeather.get(i);

			WeatherData weatherData = new WeatherData();
			weatherData.setHumidity(hwData.getMain().getHumidity());
			weatherData.setTemperature(hwData.getMain().getTemp());
			weatherData.setTemperatureMax(hwData.getMain().getTemp_max());
			weatherData.setTemperatureMin(hwData.getMain().getTemp_min());
			weatherData.setTimestamp(Long.toString(hwData.getDt()));

			weatherDataArray[i] = weatherData;
		}
		WeatherHistory weatherHistory = new WeatherHistory();
		weatherHistory.setWeatherData(weatherDataArray);
		return weatherHistory;
	}

}