package com.roberto.field.dto.heatherHistory;

import com.roberto.field.dto.GeoData;

public class PolygonDataRequest {

	private String name;

	private GeoData geo_json;

	public PolygonDataRequest() {
	}

	public PolygonDataRequest(String name, GeoData geo_json) {
		super();
		this.name = name;
		this.geo_json = geo_json;
	}

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

	@Override
	public int hashCode() { //generated because of Mockito
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) { //generated because of Mockito
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PolygonDataRequest other = (PolygonDataRequest) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
