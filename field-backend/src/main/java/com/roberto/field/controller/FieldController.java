package com.roberto.field.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roberto.field.controller.support.FieldErrorMessage;
import com.roberto.field.controller.support.FieldException;
import com.roberto.field.controller.support.FieldNotFoundException;
import com.roberto.field.controller.support.ResponseStatus;
import com.roberto.field.dto.Field;
import com.roberto.field.dto.heatherHistory.WeatherHistory;
import com.roberto.field.service.FieldService;
import com.roberto.field.service.WeatherService;

/**
 * Performs CRUD operations in endpoint: /fields
 * 
 * Create: POST on /fields
 * Retrieve one resource: GET on /fields/{fieldId}
 * Retrieve all resources: GET  on /fields
 * Update: PUT  on /fields/{fieldId}
 * Delete: DELETE on  on /fields/{fieldId}
 * 
 * 
 * Retrieves Weather History of a field in endpoint: GET /fields/{fieldId}/weather
 * 
 * @author roberto
 *
 */
@RestController
@RequestMapping("/")
public class FieldController {

	@Autowired
	private FieldService service;

	@Autowired
	private WeatherService weatherService;

	@GetMapping("/fields")
	public List<Field> getAllFields() {
		return service.getAllFields();
	}

	@GetMapping("/fields/{fieldId}")
	public Field getField(@PathVariable String fieldId) {
		return service.getFieldById(fieldId);
	}

	@PostMapping("/fields")
	public ResponseStatus createField(@RequestBody Field newField) {
		return service.createField(newField);
	}

	@PutMapping("/fields/{fieldId}")
	public ResponseStatus updateField(@PathVariable String fieldId, @RequestBody Field theField) {
		return service.updateField(fieldId, theField);
	}

	@DeleteMapping("/fields/{fieldId}")
	public ResponseStatus deleteField(@PathVariable String fieldId) {
		return service.deleteField(fieldId);
	}
	
	@GetMapping("/fields/{fieldId}/weather")
	public WeatherHistory retrieveWeatherHistory(@PathVariable String fieldId) {
		return weatherService.retrieveWeatherHistory(fieldId);
	}

	/**
	 * Handles specific FieldException and returns a consistent message back to client.
	 * @param ex
	 * @return
	 */
	@ExceptionHandler
	public ResponseEntity<FieldErrorMessage> handleException(FieldException ex) {

		FieldErrorMessage error = new FieldErrorMessage(
				HttpStatus.BAD_REQUEST.value(), 
				ex.getMessage());
		
		return new ResponseEntity<FieldErrorMessage>(error, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles specific FieldNotFoundException and returns a consistent message back to client.
	 * @param ex
	 * @return
	 */
	@ExceptionHandler
	public ResponseEntity<FieldErrorMessage> handleException(FieldNotFoundException ex) {

		FieldErrorMessage error = new FieldErrorMessage(
				HttpStatus.BAD_REQUEST.value(), 
				ex.getMessage());
		
		return new ResponseEntity<FieldErrorMessage>(error, HttpStatus.BAD_REQUEST);
	}
}
