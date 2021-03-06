package com.roberto.field.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roberto.field.controller.support.FieldException;
import com.roberto.field.controller.support.FieldNotFoundException;
import com.roberto.field.dao.FieldDAO;
import com.roberto.field.dto.Boundary;
import com.roberto.field.dto.Field;
import com.roberto.field.dto.GeoData;
import com.roberto.field.entities.BoundaryEntity;
import com.roberto.field.entities.CoordinateEntity;
import com.roberto.field.entities.FieldEntity;
import com.roberto.field.util.FieldDataConverter;

/**
 * Provides the CRUD operations for field Rest API.
 * 
 * @author roberto
 *
 */
@Service
public class FieldService {

	private static final int MINIMAL_NUMBER_VALID_COORDINATES = 3;
	
	private Logger logger = LoggerFactory.getLogger(FieldService.class);

	@Autowired
	public FieldDAO dao;

	private FieldDataConverter converter = new FieldDataConverter();

	/**
	 * Retrieve all Fields.
	 * 
	 * @return
	 */
	public List<Field> getAllFields() {
		logger.info("getAllFields");
		Iterable<FieldEntity> entities = dao.findAll();

		List<Field> fields = new ArrayList<Field>();

		entities.forEach((field) -> {
			fields.add(convertEntityFieldToDTO(field));
		});
		return fields;
	}

	/**
	 * Retrieve a specific field by it's id.
	 * 
	 * @param fieldId
	 * @return
	 */
	public Field getFieldById(String fieldId) {
		logger.info("getFieldById");
		
		Optional<FieldEntity> entity = dao.findById(fieldId);
		if (!entity.isPresent()) {
			throw new FieldNotFoundException("Field not found with id: " + fieldId);
		}

		Field field = convertEntityFieldToDTO(entity.get());
		return field;
	}

	/**
	 * Creates a new field, if it does't exist.
	 * 
	 * @param newField
	 * @return
	 */
	public void createField(Field newField) {
		logger.info("createField");
		
		Optional<FieldEntity> existingField = dao.findById(newField.getId());
		if (existingField.isPresent()) {
			throw new FieldException("Field already exists with id: " + newField.getId());
		}
		BoundaryEntity existingBoundary = dao.findBoundaryById(newField.getBounderies().getId());
		if (existingBoundary != null) {
			throw new FieldException("Field has already boundary with id: " + newField.getBounderies().getId());
		}

		FieldEntity fieldEntity = convertDTOFieldToEntity(newField);
		fieldEntity.setCreated(new Date()); //set every only when the field is created
		fieldEntity.getBoundary().setCreated(new Date());
		dao.save(fieldEntity);
	}

	/**
	 * Updates an existing field, if it exists.
	 * 
	 * @param fieldId
	 * @param theField
	 * @return
	 */
	public Field updateField(String fieldId, Field theField) {
		logger.info("updateField");
		
		FieldEntity fieldEntity = convertDTOFieldToEntity(theField);

		Optional<FieldEntity> existingField = dao.findById(fieldId);
		if (!existingField.isPresent()) {
			throw new FieldNotFoundException("Error updating field: " + fieldId + ". No field found.");
		}
		dao.deleteCoodinatesOfBoundary(fieldEntity.getBoundary().getId()); // cleans up the coordinates of current
																			// boundary
		
		//keeps the created and polygonId
		fieldEntity.getBoundary().setCreated(existingField.get().getBoundary().getCreated());
		fieldEntity.setCreated(existingField.get().getCreated());
		fieldEntity.getBoundary().setPolygonId(existingField.get().getBoundary().getPolygonId());

		//set new date for updated
		fieldEntity.setUpdated(new Date()); //set every time update is performed
		fieldEntity.getBoundary().setUpdated(new Date());
		dao.save(fieldEntity);

		return theField;
	}

	/**
	 * Delete a field, if it exists.
	 * 
	 * @param fieldId
	 * @return
	 */
	public Field deleteField(String fieldId) {
		logger.info("deleteField");
		
		Optional<FieldEntity> existingField = dao.findById(fieldId);
		if (!existingField.isPresent()) {
			throw new FieldNotFoundException("Error deleting field: " + fieldId + ". No field found.");
		}
		dao.deleteById(fieldId);

		Field field = converter.convertFieldEntityToJSON(existingField.get());
		return field;
	}

	/**
	 * Convert entity (database) field object to JSON object.
	 * 
	 * @param entity
	 * @return the field ready to send via JSON.
	 */
	private Field convertEntityFieldToDTO(FieldEntity entity) {
		logger.debug("convertEntityFieldToDTO");
		
		if (entity == null) {
			throw new FieldNotFoundException("Invalid fieldEntity data. Field is null.");
		}
		if (entity.getBoundary() == null) {
			throw new FieldNotFoundException("Invalid fieldEntity data. Boundary is null.");
		}
		if (entity.getBoundary().getCoordinates() == null) {
			throw new FieldNotFoundException("Invalid fieldEntity data. Coordinate is null.");
		}

		Field field = converter.convertFieldEntityToJSON(entity);
		Boundary boundary = converter.convertBoundaryEntityToJSON(entity.getBoundary());
		GeoData geoData = converter.convertCoordinateEntityToJSON(entity.getBoundary().getCoordinates());

		field.setBounderies(boundary);
		boundary.setGeoJson(geoData);
		return field;
	}

	/**
	 * Converts JSON field object to entity (database) object.
	 * 
	 * @param field
	 * @return the field ready to persist.
	 */
	private FieldEntity convertDTOFieldToEntity(Field field) {
		logger.debug("convertDTOFieldToEntity");
		
		if (field == null) {
			throw new FieldNotFoundException("Invalid field JSON data. Field is null.");
		}
		if (field.getBounderies() == null) {
			throw new FieldNotFoundException("Invalid field JSON data. Boundary is null.");
		}
		if (field.getBounderies().getGeoJson() == null) {
			throw new FieldNotFoundException("Invalid field JSON data. GeoJson is null.");
		}

		FieldEntity fieldEntity = converter.convertJSONToFieldEntity(field);
		BoundaryEntity boundary = converter.convertJSONToBoundaryEntity(field.getBounderies());
		List<CoordinateEntity> coordinates = converter.convertJSONToCoordinateEntity(field.getBounderies());
		if(coordinates.size() < MINIMAL_NUMBER_VALID_COORDINATES) {
			throw new FieldException("Field " + field.getId() 
				+ " has insuficient number of coordinates: " + coordinates.size() 
				+ " Minimal: " + MINIMAL_NUMBER_VALID_COORDINATES);
		}

		fieldEntity.setBoundary(boundary);
		boundary.setField(fieldEntity); // because of cascade configuration in the entities

		boundary.setCoordinates(coordinates);
		return fieldEntity;
	}
}
