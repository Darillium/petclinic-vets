FROM mysql
MAINTAINER Maxim Avezbakiev <maxim@darillium.io>

COPY db-init.sh /db-init.sh
RUN mkdir /sql_scripts
COPY ./sql_scripts/initDB.sql /sql_scripts/initDB.sql
COPY ./sql_scripts/populateDB.sql /sql_scripts/populateDB.sql

CMD /db-init.sh
