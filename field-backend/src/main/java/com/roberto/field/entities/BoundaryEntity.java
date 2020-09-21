package com.roberto.field.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "BOUNDARY")
public class BoundaryEntity implements Serializable {

	private static final long serialVersionUID = -9082282494569291153L; // generated

	public BoundaryEntity() {

	}

	@Id
	@Column(name = "BOUNDARY_ID", length = 50)
	private String id;

	@Column(name = "CREATED", nullable = false, length = 50)
	private Date created;

	private Date updated;

	@OneToOne
	@JoinColumn(name = "FIELD_FK")
	private FieldEntity field;

	@OneToMany(mappedBy = "boundary", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<CoordinateEntity> coordinates;

	public BoundaryEntity(String id, Date created) {
		super();
		this.id = id;
		this.created = created;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public FieldEntity getField() {
		return field;
	}

	public void setField(FieldEntity field) {
		this.field = field;
	}

	public List<CoordinateEntity> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<CoordinateEntity> coordinates) {
		this.coordinates = coordinates;
		for (CoordinateEntity coordinateEntity : coordinates) {
			coordinateEntity.setBoundary(this); // because of CASCADE configuration
		}
	}

}
