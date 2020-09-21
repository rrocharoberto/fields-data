package com.roberto.field.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FIELD")
public class FieldEntity implements Serializable {

	private static final long serialVersionUID = 8047516349903115814L; // generated

	public FieldEntity() {

	}

	@Id
	@Column(name = "FIELD_ID", length = 50)
	private String id;

	@Column(name = "NAME", nullable = false, length = 50)
	private String name;

	@Column(name = "CREATED", nullable = false, length = 50)
	private Date created;

	private Date updated;

	@Column(name = "COUNTRYCODE", nullable = false, length = 3)
	private String countryCode;

	@OneToOne(mappedBy = "field", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private BoundaryEntity boundary;

	public FieldEntity(String id, String name, Date created, String countryCode) {
		super();
		this.id = id;
		this.name = name;
		this.created = created;
		this.countryCode = countryCode;
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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public BoundaryEntity getBoundary() {
		return boundary;
	}

	public void setBoundary(BoundaryEntity boundary) {
		this.boundary = boundary;
	}

}
