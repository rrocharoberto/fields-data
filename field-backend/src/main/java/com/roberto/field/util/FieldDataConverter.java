package com.roberto.field.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roberto.field.controller.support.FieldNotFoundException;
import com.roberto.field.dto.Boundary;
import com.roberto.field.dto.Field;
import com.roberto.field.dto.GeoData;
import com.roberto.field.dto.Geometry;
import com.roberto.field.entities.BoundaryEntity;
import com.roberto.field.entities.CoordinateEntity;
import com.roberto.field.entities.FieldEntity;
import com.roberto.field.service.WeatherService;

public class FieldDataConverter {

	private static final int latitudeIdx = 0;
	private static final int longitudeIdx = 1;
	
	private Logger logger = LoggerFactory.getLogger(WeatherService.class);

	public FieldDataConverter() {
	}

	/**
	 * Converts JSON field object to FieldEntity object.
	 * @param field
	 * @return
	 */
	public FieldEntity convertJSONToFieldEntity(Field field) {
		logger.debug("retrieveWeatherHistory");
		
		if (field.getId() == null) {
			throw new FieldNotFoundException("Field with null id.");
		}
		if (field.getName() == null) {
			throw new FieldNotFoundException("Field with null name.");
		}
		if (field.getCountryCode() == null) {
			throw new FieldNotFoundException("Field with null country code.");
		}

		FieldEntity entity = new FieldEntity(field.getId(), field.getName(), field.getCountryCode());

		return entity;
	}

	/**
	 * Converts JSON boundary object to BoundaryEntity object.
	 * @param boundary
	 * @return
	 */
	public BoundaryEntity convertJSONToBoundaryEntity(Boundary boundary) {
		logger.debug("convertJSONToBoundaryEntity");
		
		if (boundary.getId() == null) {
			throw new FieldNotFoundException("Boundary with null id.");
		}
		return new BoundaryEntity(boundary.getId());
	}

	/**
	 * Converts JSON boundary object to a list of CoordinateEntity objects.
	 * @param boundary
	 * @return
	 */
	public List<CoordinateEntity> convertJSONToCoordinateEntity(Boundary boundary) {
		logger.debug("convertJSONToCoordinateEntity");
		
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
		logger.debug("convertFieldEntityToJSON");
		
		Field field = new Field();
		field.setId(entity.getId());
		field.setName(entity.getName());
		field.setCreated(DateUtil.convertDateToString(entity.getCreated()));
		field.setUpdated(DateUtil.convertDateToString(entity.getUpdated()));
		field.setCountryCode(entity.getCountryCode());

		return field;
	}

	public Boundary convertBoundaryEntityToJSON(BoundaryEntity boundaryEntity) {
		logger.debug("convertBoundaryEntityToJSON");
		
		Boundary boundary = new Boundary();
		boundary.setId(boundaryEntity.getId());
		boundary.setCreated(DateUtil.convertDateToString(boundaryEntity.getCreated()));// check Daniel's response
		boundary.setUpdated(DateUtil.convertDateToString(boundaryEntity.getUpdated()));// check Daniel's response

		return boundary;
	}

	public GeoData convertCoordinateEntityToJSON(List<CoordinateEntity> coordinateEntities) {
		logger.debug("convertCoordinateEntityToJSON");
		
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