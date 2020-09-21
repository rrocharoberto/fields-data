package com.roberto.field.dto.heatherHistory;

import com.roberto.field.dto.GeoData;

public class PolygonDataRequest {

	private String name;

	private GeoData geo_json;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GeoData getGeo_json() {
		return geo_json;
	}

	public void setGeo_json(GeoData geo_json) {
		this.geo_json = geo_json;
	}

}
