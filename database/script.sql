
CREATE TABLE Field (
                field_id VARCHAR(36) NOT NULL,
                name VARCHAR(50) NOT NULL,
                created TIMESTAMP NOT NULL,
                updated TIMESTAMP,
                countryCode VARCHAR(3) NOT NULL,
                CONSTRAINT field_pk PRIMARY KEY (field_id)
);


CREATE TABLE Boundary (
                boundary_id VARCHAR(36) NOT NULL,
                created TIMESTAMP NOT NULL,
                updated TIMESTAMP,
                field_fk VARCHAR(36) NOT NULL,
                polygon_id VARCHAR(36),
                CONSTRAINT boundary_pk PRIMARY KEY (boundary_id)
);
--COMMENT ON COLUMN Boundary.polygon_id IS 'Id of the polygon created in OpenWeather API.';


CREATE TABLE Coordinate (
                coordinate_id SERIAL NOT NULL,
                latitude NUMERIC(23,20) NOT NULL,
                longitude NUMERIC(23,20) NOT NULL,
                boundary_fk VARCHAR(36) NOT NULL,
                CONSTRAINT coordinate_pk PRIMARY KEY (coordinate_id)
);


ALTER TABLE Boundary ADD CONSTRAINT field_boundary_fk
FOREIGN KEY (field_fk)
REFERENCES Field (field_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE Coordinate ADD CONSTRAINT boundary_coordinate_fk
FOREIGN KEY (boundary_fk)
REFERENCES Boundary (boundary_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
