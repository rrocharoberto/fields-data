package com.roberto.field.test.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import com.roberto.field.FieldBackendApplication;
import com.roberto.field.dto.Boundary;
import com.roberto.field.dto.Field;
import com.roberto.field.dto.GeoData;
import com.roberto.field.dto.Geometry;

@SpringBootTest(classes = FieldBackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FieldBackendApplicationTests {

	@LocalServerPort
	private int port;

	private TestRestTemplate restTemplate = new TestRestTemplate();

	private String baseURL = "/field-backend/fields/";

	@Test
	public void testCreateFieldSuccess() throws Exception {

		Field fieldTest = getFieldDTO(Long.toString(new Random().nextLong()), Long.toString(new Random().nextLong()));

		ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort(baseURL), fieldTest,
				String.class);

		// check the response code
		assertEquals(201, response.getStatusCodeValue());

		//remove the field created
		restTemplate.delete(createURLWithPort(baseURL + fieldTest.getId()));
	}

	@Test
	public void testRetrieveField() throws Exception {

		// create a specific field for test
		Field fieldTest = getFieldDTO(Long.toString(new Random().nextLong()), Long.toString(new Random().nextLong()));
		ResponseEntity<String> responsePost = restTemplate.postForEntity(createURLWithPort(baseURL), fieldTest,
				String.class);
		assertEquals(201, responsePost.getStatusCodeValue());

		// retrieve the specific field
		ResponseEntity<Field> response = restTemplate.getForEntity(createURLWithPort(baseURL + fieldTest.getId()),
				Field.class);

		// check the results

		assertEquals(200, response.getStatusCodeValue());

		// JSONAssert.assertEquals(expected, response.getBody(), false);

		Field field = response.getBody();

		assertNotNull(field);
		assertEquals(fieldTest.getId(), field.getId());

		assertEquals("Potato field", field.getName());
		assertEquals("DEU", field.getCountryCode());
		assertNotNull(field.getBounderies());
		assertEquals(fieldTest.getBounderies().getId(), field.getBounderies().getId());

		assertNotNull(field.getBounderies().getGeoJson());
		assertNotNull(field.getBounderies().getGeoJson().getGeometry());
		assertNotNull(field.getBounderies().getGeoJson().getGeometry().getCoordinates());
		assertEquals(1, field.getBounderies().getGeoJson().getGeometry().getCoordinates().length);
		assertEquals(getCoordinates()[0].length,
				field.getBounderies().getGeoJson().getGeometry().getCoordinates()[0].length);

		// remove that field
		restTemplate.delete(createURLWithPort(baseURL + field.getId()));
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

	// these data are the same as the PDF, (but not the ids)

	private Field getFieldDTO(String fieldId, String boundaryId) {
		Field field = new Field();
		field.setId(fieldId);
		field.setName("Potato field");
		field.setCountryCode("DEU");

		Boundary boundary = new Boundary();
		boundary.setId(boundaryId);

		GeoData geoData = new GeoData();
		geoData.setType("Feature");
		geoData.setProperties(new Properties());

		Geometry geometry = new Geometry();

		geometry.setCoordinates(getCoordinates());

		geoData.setGeometry(geometry);
		boundary.setGeoJson(geoData);
		field.setBounderies(boundary);
		return field;
	}

	private BigDecimal[][][] getCoordinates() {
		return new BigDecimal[][][] { { { new BigDecimal(-5.553604888914691), new BigDecimal(33.88229680420605) },
				{ new BigDecimal(-5.5516736984239685), new BigDecimal(33.88229680420605) },
				{ new BigDecimal(-5.5516736984239685), new BigDecimal(33.88372189858022) },
				{ new BigDecimal(-5.555965232847882), new BigDecimal(33.88390003370375) },
				{ new BigDecimal(-5.555965232847882), new BigDecimal(33.88229680420605) },
				{ new BigDecimal(-5.553604888914691), new BigDecimal(33.88229680420605) } } };
	}
}
