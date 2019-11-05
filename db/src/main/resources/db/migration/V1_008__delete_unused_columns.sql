ALTER TABLE dwp_uc_household DROP COLUMN award_date;
ALTER TABLE dwp_uc_household DROP COLUMN last_assessment_period_start;
ALTER TABLE dwp_uc_household DROP COLUMN last_assessment_period_end;
ALTER TABLE dwp_uc_household DROP COLUMN no_of_children_under_four;
ALTER TABLE dwp_uc_household DROP COLUMN household_member_pregnant;

ALTER TABLE dwp_uc_child DROP COLUMN forename;
ALTER TABLE dwp_uc_child DROP COLUMN surname;
ALTER TABLE dwp_uc_child DROP COLUMN effective_date;

ALTER TABLE dwp_uc_adult DROP COLUMN forename;
ALTER TABLE dwp_uc_adult DROP COLUMN address_line_2;
ALTER TABLE dwp_uc_adult DROP COLUMN address_town_or_city;
