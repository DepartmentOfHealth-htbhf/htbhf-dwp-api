ALTER TABLE dwp_uc_household ADD COLUMN household_member_pregnant boolean default false;
UPDATE dwp_uc_household set household_member_pregnant = true where household_member_pregnant is null and pregnancy_flag = 'Y';
ALTER TABLE dwp_uc_household DROP COLUMN pregnancy_flag;


ALTER TABLE dwp_uc_household ADD COLUMN earnings_threshold_exceeded boolean default false;
UPDATE dwp_uc_household set earnings_threshold_exceeded = true where earnings_threshold_exceeded is null and earnings_threshold_exceeded_flag = 'Y';
ALTER TABLE dwp_uc_household   DROP COLUMN earnings_threshold_exceeded_flag;