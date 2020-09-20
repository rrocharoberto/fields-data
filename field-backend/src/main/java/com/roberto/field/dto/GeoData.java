package com.roberto.field.dto;

import java.util.Properties;

public class GeoData {

	private String type = "Feature"; // TODO: hard coded. Check for changing it in new versions.
	private Properties properties = new Properties(); // TODO: hard coded. Check for changing it in new versions.
	private Geometry geometry;
	
	public GeoData() {

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

}
