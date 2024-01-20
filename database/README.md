# README

This is the build project for the database.

It will rebuild a clean database from scratch using [db migrate](https://db-migrate.readthedocs.io)

# INSTRUCTIONS

-   Install latest version of [npm (inludes node)](https://www.npmjs.com/get-npm)
-   Execute 'npm install -g db-migrate' to globally install the db-migrate package
-   Execute 'npm install' from current directory (/database) to install all package dependencies
-   Create file database.json using database.sample.json with db connection details. Do not checkin this file as it contains sensitive details.
-   Execute 'npm run start:dev' to execute all migration scripts in development environment.
-   Follow the commands for [db-migrate](https://db-migrate.readthedocs.io/en/latest/Getting%20Started/the%20commands/) to create new scripts.
-   The current database resides on [ElephantSQL](https://www.elephantsql.com/)
-   Follow [instructions](https://www.elephantsql.com/docs/pgadmin.html) to connect [PgAdmin](https://www.pgadmin.org/download/) to [ElephantSQL](https://www.elephantsql.com/)

# PostgreSQL Naming Conventions

https://gist.github.com/popravich/d6816ef1653329fb1745