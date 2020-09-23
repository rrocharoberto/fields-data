
package com.roberto.field.dto.heatherHistory;

import com.roberto.field.dto.GeoData;

public class PolygonDataResponse {

	public static final int latitudeIdx = 0;
	public static final int longitudeIdx = 0;

	private String id;
	private String name;
	private float[] center;

	private float area;
	private String user_id;

	private GeoData geo_json;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float[] getCenter() {
		return center;
	}

	public void setCenter(float[] center) {
		this.center = center;
	}

	public float getArea() {
		return area;
	}

	public void setArea(float area) {
		this.area = area;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public GeoData getGeo_json() {
		return geo_json;
	}

	public void setGeo_json(GeoData geo_json) {
		this.geo_json = geo_json;
	}

}
