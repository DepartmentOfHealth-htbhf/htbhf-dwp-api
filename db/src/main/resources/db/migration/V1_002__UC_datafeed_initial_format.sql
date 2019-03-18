CREATE TABLE dwp_uc_household (
    id uuid not null primary key,
    household_identifier varchar(50),
    file_import_number integer,
    award_date date,
    last_assessment_period_start date,
    last_assessment_period_end date,
    pregnancy_flag varchar(1),
    earnings_threshold_exceeded_flag varchar(1),
    no_of_children_under_four integer,
    created_timestamp TIMESTAMP DEFAULT NOW()
);

CREATE TABLE dwp_uc_adult (
    id uuid not null primary key,
    dwp_uc_household_id uuid not null,
    nino varchar(8),
    forename varchar(500),
    surname varchar(500),
    address_line_1 varchar(500),
    address_line_2 varchar(500),
    address_town_or_city varchar(500),
    address_postcode varchar(10)
);

ALTER TABLE dwp_uc_adult ADD FOREIGN KEY(dwp_uc_household_id) REFERENCES dwp_uc_household (id) ON DELETE CASCADE;
CREATE INDEX dwp_uc_adult_nino_idx ON dwp_uc_adult (nino);

CREATE TABLE dwp_uc_child (
    id uuid not null primary key,
    dwp_uc_household_id uuid not null,
    forename varchar(500),
    surname varchar(500),
    date_of_birth date,
    effective_date date
);

ALTER TABLE dwp_uc_child ADD FOREIGN KEY(dwp_uc_household_id) REFERENCES dwp_uc_household (id) ON DELETE CASCADE;
