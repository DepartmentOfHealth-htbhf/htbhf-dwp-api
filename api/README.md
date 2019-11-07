# htbhf-dwp-api

Rest API (implemented with Spring Boot) that interacts with the real DWP API.
Also checks a database to see if we have details of the NINO in the request, 
using values from the db if it matches and not calling the DWP.

## Seeded data matching

When an eligibility request is received we firstly see if the request matches any data
we have in the database by finding matching households that have an adult with the same NINO.
If a matching adult is found, then we use the following logic to build the response:

 * Identity status is `MATCHED` if NINO, Surname and DOB match the request, otherwise `NOT_MATCHED`.
 * If the Identity Status is `NOT_MATCH`, then all other response enum values are `NOT_SET`.
 * Eligibility status is `CONFIRMED` if the Id status is `MATCHED`, `NOT_CONFIRMED` if the earnings threshold for the household is true, else `NOT_SET`.
 * Address Line 1 Match is `MATCHED` if the first 6 characters match (ignoring case), else `NOT_MATCHED`.
 * Postcode match is `MATCHED` if the postcode matches ignoring whitespace and casing, else `NOT_MATCHED`.
 * Mobile match is `MATCHED` if the mobile matches exactly, `NOT_SUPPLIED` if blank else `NOT_MATCHED`. If there is no value in the database, then this is `NOT_HELD`.
 * Email match is `MATCHED` if the email matches ignoring case, `NOT_SUPPLIED` if blank else `NOT_MATCHED`. If there is no value in the database, then this is `NOT_HELD`.
 * Qualifying Benefits are `UNIVERSAL_CREDIT` if the id is matched (including the address)
 * Household identifier will always be blank.
 * Pregnant dependant date of birth will be `NOT_SUPPLIED` if not provided in the request, else `NOT_SET`.
 * The dob of children under 4 will simply be returned from the db if the id status is `MATCHED`. Any dates of birth which have become over 4 in the database are filtered out.

### building and running.
run `./gradlew clean build`

Running locally:
run `./gradlew bootRun` - either from the parent director or from the `api` directory.
