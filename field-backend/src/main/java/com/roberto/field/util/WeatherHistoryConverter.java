package com.roberto.field.util;

import java.util.List;

import com.roberto.field.dto.heatherHistory.HistoricalWeatherData;
import com.roberto.field.dto.heatherHistory.WeatherData;
import com.roberto.field.dto.heatherHistory.WeatherHistory;

public class WeatherHistoryConverter {

	public WeatherHistoryConverter() {
	}

	public WeatherHistory convertHistoricalWeatherToJSON(List<HistoricalWeatherData> historicalWeather) {

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