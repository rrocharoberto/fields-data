package com.roberto.field.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roberto.field.controller.support.FieldNotFoundException;
import com.roberto.field.controller.support.ResponseStatus;
import com.roberto.field.dao.FieldDAO;
import com.roberto.field.dto.Boundary;
import com.roberto.field.dto.Field;
import com.roberto.field.dto.GeoData;
import com.roberto.field.entities.BoundaryEntity;
import com.roberto.field.entities.CoordinateEntity;
import com.roberto.field.entities.FieldEntity;
import com.roberto.field.util.FieldDataConverter;

/**
 * Provides the CRUD operations for Rest API.
 * 
 * @author roberto
 *
 */
@Service
public class FieldService {

	@Autowired
	public FieldDAO dao;

	private FieldDataConverter converter = new FieldDataConverter();

	/**
	 * Retrieve all Fields.
	 * 
	 * @return
	 */
	public List<Field> getAllFields() {
		Iterable<FieldEntity> entities = dao.findAll();

		List<Field> fields = new ArrayList<Field>();

		entities.forEach((field) -> {
			fields.add(convertEntityFieldToDTO(field));
		});
		return fields;
	}

	/**
	 * Retrieve a specific field.
	 * 
	 * @param fieldId
	 * @return
	 */
	public Field getFieldById(String fieldId) {

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
	public ResponseStatus createField(Field newField) {
		Optional<FieldEntity> existingField = dao.findById(newField.getId());
		if (existingField.isPresent()) {
			throw new FieldNotFoundException("Field already exists with id: " + newField.getId());
		}
		BoundaryEntity existingBoundary = dao.findBoundaryById(newField.getBounderies().getId());
		if (existingBoundary != null) {
			throw new FieldNotFoundException("Field has already boundary with id: " + newField.getBounderies().getId());
		}

		FieldEntity fieldEntity = convertDTOFieldToEntity(newField);
		dao.save(fieldEntity);
		return ResponseStatus.success("Field created successfully.");
	}

	/**
	 * Updates an existing field, if it exists.
	 * 
	 * @param fieldId
	 * @param theField
	 * @return
	 */
	public ResponseStatus updateField(String fieldId, Field theField) {
		FieldEntity fieldEntity = convertDTOFieldToEntity(theField);

		Optional<FieldEntity> existingField = dao.findById(fieldId);
		if (!existingField.isPresent()) {
			throw new FieldNotFoundException("Error updating field: " + fieldId + ". No field found.");
		}
		dao.deleteCoodinatesOfBoundary(fieldEntity.getBoundary().getId()); // cleans up the coordinates of current
																			// boundary
		dao.save(fieldEntity);
		return ResponseStatus.success("Field updated successfully.");
	}

	/**
	 * Delete a field, if it exists.
	 * 
	 * @param fieldId
	 * @return
	 */
	public ResponseStatus deleteField(String fieldId) {
		Optional<FieldEntity> existingField = dao.findById(fieldId);
		if (!existingField.isPresent()) {
			throw new FieldNotFoundException("Error deleting field: " + fieldId + ". No field found.");
		}
		dao.deleteById(fieldId);
		return ResponseStatus.success("Field deleted successfully.");
	}

	/**
	 * Convert entity (database) field object to JSON object.
	 * 
	 * @param entity
	 * @return the field ready to send via JSON.
	 */
	private Field convertEntityFieldToDTO(FieldEntity entity) {
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
		GeoData geoData = converter.convertJSONToCoordinateEntity(entity.getBoundary().getCoordinates());

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
		List<CoordinateEntity> coodinates = converter.convertJSONToCoordinateEntity(field.getBounderies());

		fieldEntity.setBoundary(boundary);
		boundary.setField(fieldEntity); // because of cascade configuration in the entities

		boundary.setCoordinates(coodinates);
		return fieldEntity;
	}
}