CREATE DATABASE field;

CREATE USER field WITH PASSWORD $DB_PASSWORD;

GRANT ALL PRIVILEGES ON DATABASE field TO field;

