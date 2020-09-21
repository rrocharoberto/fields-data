package com.roberto.field.dto.heatherHistory;

public class HistoricalWeatherData {

	private long dt; // Time of data calculation, unix, UTC

	private MainWeatherData main;

	public long getDt() {
		return dt;
	}

	public void setDt(long dt) {
		this.dt = dt;
	}

	public MainWeatherData getMain() {
		return main;
	}

	public void setMain(MainWeatherData main) {
		this.main = main;
	}

}
