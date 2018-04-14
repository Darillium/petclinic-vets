#!/bin/bash

set -x

mysql -h"$MYSQL_HOST" -u"$MYSQL_ROOT_USER" -p"$MYSQL_ROOT_PASSWORD"

for i in {30..0}; do
  if echo 'SELECT 1' | mysql -h"$MYSQL_HOST" -u"$MYSQL_ROOT_USER" -p"$MYSQL_ROOT_PASSWORD" &> /dev/null; then
  	break
  fi
  echo 'MySQL init process in progress - count is $i...'
  sleep 1
done

echo "$0: creating database: $MYSQL_DATABASE"

mysql -h"$MYSQL_HOST" -u"$MYSQL_ROOT_USER" -p"$MYSQL_ROOT_PASSWORD" <<MYSQL_SCRIPT
CREATE DATABASE IF NOT EXISTS $MYSQL_DATABASE;
ALTER DATABASE $MYSQL_DATABASE
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;
CREATE USER IF NOT EXISTS '$MYSQL_USER'@'%';
SET PASSWORD FOR '$MYSQL_USER'@'%' = '$MYSQL_PASSWORD';
GRANT ALL PRIVILEGES ON $MYSQL_DATABASE.* TO '$MYSQL_USER'@'%';
FLUSH PRIVILEGES;
MYSQL_SCRIPT

echo "$0: running /sql_scripts/initDB.sql"; mysql -h"$MYSQL_HOST" -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" -D"$MYSQL_DATABASE" < /sql_scripts/initDB.sql;

echo "$0: running /sql_scripts/populateDB.sql"; mysql -h"$MYSQL_HOST" -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" -D"$MYSQL_DATABASE" < /sql_scripts/populateDB.sql;
