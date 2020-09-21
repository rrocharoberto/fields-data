package com.roberto.field.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "COORDINATE")
public class CoordinateEntity implements Serializable {

	private static final long serialVersionUID = 443730965460609134L; // generated

	public CoordinateEntity() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COORDINATE_ID")
	private int id;

	private BigDecimal latitude;
	private BigDecimal longitude;

	@ManyToOne
	@JoinColumn(name = "BOUNDARY_FK")
	private BoundaryEntity boundary;

	public CoordinateEntity(BigDecimal latitude, BigDecimal longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public BoundaryEntity getBoundary() {
		return boundary;
	}

	public void setBoundary(BoundaryEntity boundary) {
		this.boundary = boundary;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLogitude() {
		return longitude;
	}

	public void setLogitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

}
