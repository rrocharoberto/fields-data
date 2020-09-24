package com.roberto.field.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.roberto.field.controller.support.GeneralExceptionHandler;
import com.roberto.field.dto.Field;
import com.roberto.field.dto.heatherHistory.WeatherHistory;
import com.roberto.field.service.FieldService;
import com.roberto.field.service.WeatherService;

/**
 * Performs CRUD operations in endpoint: /fields
 * 
 * Create: POST on /fields Retrieve one resource: GET on /fields/{fieldId}
 * Retrieve all resources: GET on /fields Update: PUT on /fields/{fieldId}
 * Delete: DELETE on on /fields/{fieldId}
 * 
 * 
 * Retrieves Weather History of a field in endpoint: GET
 * /fields/{fieldId}/weather
 * 
 * @author roberto
 *
 */
@RestController
@RequestMapping(name = "/")
public class FieldController {

	private Logger logger = LoggerFactory.getLogger(FieldController.class);

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
	@org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.CREATED)
	public void createField(@RequestBody Field newField) {
		service.createField(newField);
	}

	@PutMapping("/fields/{fieldId}")
	public Field updateField(@PathVariable String fieldId, @RequestBody Field theField) {
		return service.updateField(fieldId, theField);
	}

	@DeleteMapping("/fields/{fieldId}")
	public Field deleteField(@PathVariable String fieldId) {
		return service.deleteField(fieldId);
	}

	@GetMapping("/fields/{fieldId}/weather")
	public WeatherHistory retrieveWeatherHistory(@PathVariable String fieldId) {
		return weatherService.retrieveWeatherHistory(fieldId);
	}

	/**
	 * Handles specific FieldException and returns a consistent message back to
	 * client.
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler
	public ResponseEntity<FieldErrorMessage> handleException(FieldException ex) {
		logger.info("handleException", ex);
		
		FieldErrorMessage error = new FieldErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

		return new ResponseEntity<FieldErrorMessage>(error, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles specific FieldNotFoundException and returns a consistent message back
	 * to client.
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler
	public ResponseEntity<FieldErrorMessage> handleException(FieldNotFoundException ex) {
		logger.info("handleException", ex);
		
		FieldErrorMessage error = new FieldErrorMessage(HttpStatus.NOT_FOUND.value(), ex.getMessage());

		return new ResponseEntity<FieldErrorMessage>(error, HttpStatus.NOT_FOUND);
	}
}
