--generated using SQLPowerArchitect model.

CREATE TABLE Field (
                fieldId VARCHAR(50) NOT NULL,
                name VARCHAR(50) NOT NULL,
                created TIMESTAMP NOT NULL,
                updated TIMESTAMP,
                countryCode VARCHAR(3) NOT NULL,
                CONSTRAINT field_pk PRIMARY KEY (fieldId)
);


CREATE TABLE Boundary (
                boundaryId VARCHAR(50) NOT NULL,
                created TIMESTAMP NOT NULL,
                updated TIMESTAMP,
                fieldId VARCHAR(50) NOT NULL,
                CONSTRAINT boundary_pk PRIMARY KEY (boundaryId)
);


CREATE TABLE Coordinate (
                boundaryId VARCHAR(50) NOT NULL,
                latitude NUMERIC(23,20) NOT NULL,
                longitude NUMERIC(23,20) NOT NULL,
                CONSTRAINT coordinate_pk PRIMARY KEY (boundaryId)
);


ALTER TABLE Boundary ADD CONSTRAINT field_boundary_fk
FOREIGN KEY (fieldId)
REFERENCES Field (fieldId)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE Coordinate ADD CONSTRAINT boundary_coordinates_fk
FOREIGN KEY (boundaryId)
REFERENCES Boundary (boundaryId)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
