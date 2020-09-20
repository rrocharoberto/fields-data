package com.roberto.field.dto;

public class Field {

	private String id;
	private String name;
	private String created;
	private String updated;
	private String countryCode;

	private Boundary bounderies;

	public Field() {

	}

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

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Boundary getBounderies() {
		return bounderies;
	}

	public void setBounderies(Boundary bounderies) {
		this.bounderies = bounderies;
	}

}
