#!/bin/sh

#create sql_scripts directory
mkdir -p sql_scripts
#copy the SQL files to the current directory
cp ../../src/main/resources/db/mysql/initDB.sql ./sql_scripts/initDB.sql
cp ../../src/main/resources/db/mysql/populateDB.sql ./sql_scripts/populateDB.sql

echo "Starting the build..."
docker build -t petclinic-vets-mysql-db-init:latest .
