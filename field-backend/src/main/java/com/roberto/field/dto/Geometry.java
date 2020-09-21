package com.roberto.field.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roberto.field.controller.support.FieldNotFoundException;

public class Geometry {

	@JsonIgnore
	public static final int exteriorIdx = 0; //only supports exterior coordinates

	private String type = "Polygon";

	private BigDecimal[][][] coordinates; // this version supports only ONE (exterior) object (different of rfc7946)

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@JsonIgnore
	public BigDecimal[][] getExteriorGeometry() {
		if(coordinates == null || coordinates.length == 0) {
			throw new FieldNotFoundException("No coordinates found.");
		}
		return coordinates[exteriorIdx];
	}
	
	public BigDecimal[][][] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(BigDecimal[][][] coordinates) {
		this.coordinates = coordinates;
	}

}
