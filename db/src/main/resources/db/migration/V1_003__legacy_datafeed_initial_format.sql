CREATE TABLE dwp_legacy_household (
    id uuid not null primary key,
    household_identifier varchar(50),
    file_import_number integer,
    award varchar(500),
    address_line_1 varchar(500),
    address_line_2 varchar(500),
    address_town_or_city varchar(500),
    address_postcode varchar(10),
    created_timestamp TIMESTAMP DEFAULT NOW()
);

CREATE TABLE dwp_legacy_adult (
    id uuid not null primary key,
    dwp_legacy_household_id uuid not null,
    nino varchar(8),
    forename varchar(500),
    surname varchar(500)
);

ALTER TABLE dwp_legacy_adult ADD FOREIGN KEY(dwp_legacy_household_id) REFERENCES dwp_legacy_household (id) ON DELETE CASCADE;
CREATE INDEX dwp_legacy_adult_nino_idx ON dwp_legacy_adult (nino);

CREATE TABLE dwp_legacy_child (
    id uuid not null primary key,
    dwp_legacy_household_id uuid not null,
    forename varchar(500),
    surname varchar(500),
    date_of_birth date
);

ALTER TABLE dwp_legacy_child ADD FOREIGN KEY(dwp_legacy_household_id) REFERENCES dwp_legacy_household (id) ON DELETE CASCADE;
