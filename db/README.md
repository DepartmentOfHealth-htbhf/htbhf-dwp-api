db
=============

This sub-project is responsible for the definition of the Database Schema, using the Postgresql dialect.


DB upgrade scripts
-------------

Database migrations will be performed by [Flyway](https://flywaydb.org/documentation/), invoked automagically by spring
(Search [Spring Boot properties](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)
for 'flyway' to find the relevant properties, as configured in api/src/main/resources/application.yml).

Flyway uses migration scripts in src/main/resources/db.migration.
There is a strong naming convention for these scripts:
```
V_major_minor__description.sql
```
For example: `V1_001__create_table.sql`.

Please note that to ensure correct ordering on the filesystem minor version numbers should be 3 digits long.

Try to ensure that each migration script runs as quickly as possible, and avoid actions that create broader or longer-lasting locks than necessary.
Try not to make large data changes in the same script as a EXCLUSIVE lock task like `alter table`.
Flyway creates a transaction per migration script and if you mix the two the non blocking data task becomes a blocking one because of the lock required for the schema change, for example.
When altering indexes make sure to use CONCURRENT wherever possible.

See the following links for an idea of what is likely to create blocking locks:
- https://www.citusdata.com/blog/2018/02/15/when-postgresql-blocks/
- https://www.postgresql.org/docs/9.4/mvcc-intro.html


Commands
-------------
To (re)create the database and user:

```
sudo -u postgres psql
    DROP DATABASE IF EXISTS eligibility_api;
    DROP USER IF EXISTS eligibility_admin;
    CREATE USER eligibility_admin WITH ENCRYPTED PASSWORD 'eligibility_admin';
    CREATE DATABASE eligibility_api;
    GRANT ALL PRIVILEGES ON DATABASE eligibility_api TO eligibility_admin;
    \q
```

Accessing PaaS databases
-------------
Sourced from: https://docs.cloud.service.gov.uk/deploying_services/postgresql/#connect-to-a-postgresql-service-from-your-local-machine

Having logged into the Paas:
```
cf login -a ${CF_API} -u ${CF_USER} -p "${CF_PASS}" -s ${CF_SPACE} -o ${CF_ORG}
```
(And having already created the database: `cf create-service postgres small-ha-10.5 htbhf-eligibility-api-postgres-postgres`)

And installed the `conduit` plugin:
```
cf install-plugin conduit
```
To use the psql command line, run: (you will need to have `psql` installed - see https://www.postgresql.org/download/)
```
cf conduit htbhf-eligibility-api-postgres -- psql
```
Or, to connect the conduit and use a different application:
```
cf conduit htbhf-eligibility-api-postgres
```
The output will provide connection details - the username & password change every time.
Note that the jdbcuri includes `&ssl=true` - which should be removed for a successful connection.

Setting the preferred maintenance window
------------
Source from https://docs.cloud.service.gov.uk/deploying_services/postgresql/#postgresql-maintenance-amp-backups

Having logged into the Paas:
```
cf login -a ${CF_API} -u ${CF_USER} -p "${CF_PASS}" -s ${CF_SPACE} -o ${CF_ORG}
```
(And having already created the database: `cf create-service postgres small-ha-10.5 htbhf-eligibility-api-postgres`)

Set the preferred maintenance window (For production this is currently every Sunday between 03:00 am and 3:30 am)
```
cf update-service htbhf-eligibility-api-postgres -c '{"preferred_maintenance_window": "Sun:03:00-Sun:03:30"}'
```
