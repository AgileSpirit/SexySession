# SmartSession

A Java POC to demostrate that it is possible to make statelesse webapps. Certified 100% with no JSESSIONID !

## Feature Flipping

In order to make load testing and optimizations comparison easier, a simple *Feature Flipping* mechanism is set.

All the enable optimizations are declared as constants in infra.util.Features class.

## DB configuration

The application needs a database to store data about users and their session.
The configuration of the DB is defined in the src/main/resources/application.properties file.

### HSQLDB

The default DB is HSQLDB, an in-memory and relational database.

### PostgreSQL

As an alternative, and in order to make some more relevant tests, PostgreSQL might be set instead of HSQLDB.

To do this, you must :
  1. Declare and configure a user role and a DB in PGAdmin :
    - Add a new "connection role" with username "smarties" and password "smarties"
    - Put him rights to create and update database and database objects
    - Create a new database called "smartsession"
    - Set the database owner to be your user "smarties"
  2. Uncomment and customize the PostgreSQL configuration in the application.properties file.

## Redis configuration

By default Redis integration is disable (cf. Features.IS_ENABLED_REDIS_STORING).

To active it, you need to install and configure a Redis server & client.

SmartSession used the default configuration, that points to localhost and port 6379.

That's it.

## Connection Pool configuration

According to SmartSession architecture, database connections are a possible bottleneck.

A way to reduce this risk is to set up a database connection pool.

It is easily done with C3P0 that is well integrated with Hibernate by default.

To active C3P0 pooling :
  1. Enable Features.IS_ENABLED_HIBERNATE_C3P0_POOLING
  2. Uncomment C3P0 configuration in application.properties