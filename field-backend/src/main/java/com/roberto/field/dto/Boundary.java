package com.roberto.field.dto;

public class Boundary {

	private String id;
	private String created;
	private String updated;

	private GeoData geoJson;

	public Boundary() {
		
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public GeoData getGeoJson() {
		return geoJson;
	}

	public void setGeoJson(GeoData geoJson) {
		this.geoJson = geoJson;
	}

}
