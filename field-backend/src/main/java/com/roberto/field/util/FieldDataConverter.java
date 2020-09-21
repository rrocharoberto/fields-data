package com.roberto.field.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.roberto.field.controller.support.FieldNotFoundException;
import com.roberto.field.dto.Boundary;
import com.roberto.field.dto.Field;
import com.roberto.field.dto.GeoData;
import com.roberto.field.dto.Geometry;
import com.roberto.field.entities.BoundaryEntity;
import com.roberto.field.entities.CoordinateEntity;
import com.roberto.field.entities.FieldEntity;

public class FieldDataConverter {

	private static final int latitudeIdx = 0;
	private static final int longitudeIdx = 1;

	public FieldDataConverter() {
	}

	public FieldEntity convertJSONToFieldEntity(Field field) {
		if (field.getId() == null) {
			throw new FieldNotFoundException("Field with null id.");
		}
		if (field.getName() == null) {
			throw new FieldNotFoundException("Field with null name.");
		}
		if (field.getCountryCode() == null) {
			throw new FieldNotFoundException("Field with null country code.");
		}

		// TODO: waiting Daniel's email about created and updated
		// TODO: new Date() in update won't work properly.
		FieldEntity entity = new FieldEntity(field.getId(), field.getName(), new Date(), field.getCountryCode());

		return entity;
	}

	public BoundaryEntity convertJSONToBoundaryEntity(Boundary boundary) {

		if (boundary.getId() == null) {
			throw new FieldNotFoundException("Boundary with null id.");
		}
		// TODO: waiting Daniel's email about created and updated
		BoundaryEntity entity = new BoundaryEntity(boundary.getId(), new Date());

		return entity;
	}

	public List<CoordinateEntity> convertJSONToCoordinateEntity(Boundary boundary) {

		List<CoordinateEntity> coordinates = new ArrayList<CoordinateEntity>();

		BigDecimal[][] exteriorGeometry = boundary.getGeoJson().getGeometry().getExteriorGeometry();

		for (int point = 0; point < exteriorGeometry.length; point++) {
			CoordinateEntity entity = new CoordinateEntity(exteriorGeometry[point][latitudeIdx],
					exteriorGeometry[point][longitudeIdx]);

			coordinates.add(entity);
		}
		return coordinates;
	}

	public Field convertFieldEntityToJSON(FieldEntity entity) {
		Field field = new Field();
		field.setId(entity.getId());
		field.setName(entity.getName());
		field.setCreated(DateUtil.convertDateToString(entity.getCreated()));// check Daniel's response
		field.setUpdated(DateUtil.convertDateToString(entity.getUpdated()));// check Daniel's response
		field.setCountryCode(entity.getCountryCode());

		return field;
	}

	public Boundary convertBoundaryEntityToJSON(BoundaryEntity boundaryEntity) {
		Boundary boundary = new Boundary();
		boundary.setId(boundaryEntity.getId());
		boundary.setCreated(DateUtil.convertDateToString(boundaryEntity.getCreated()));// check Daniel's response
		boundary.setUpdated(DateUtil.convertDateToString(boundaryEntity.getUpdated()));// check Daniel's response

		return boundary;
	}

	public GeoData convertCoordinateEntityToJSON(List<CoordinateEntity> coordinateEntities) {
		Geometry geometry = new Geometry();

		BigDecimal coordinatesMatrix[][][] = new BigDecimal[1][coordinateEntities.size()][2];
		int point = 0;

		for (CoordinateEntity coordinateEntity : coordinateEntities) {
			coordinatesMatrix[Geometry.exteriorIdx][point][latitudeIdx] = coordinateEntity.getLatitude();
			coordinatesMatrix[Geometry.exteriorIdx][point][longitudeIdx] = coordinateEntity.getLogitude();
			point++;
		}
		geometry.setCoordinates(coordinatesMatrix);

		GeoData geoData = new GeoData();
		geoData.setGeometry(geometry);
		return geoData;
	}

}