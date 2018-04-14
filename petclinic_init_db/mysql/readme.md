# Petclinic Database initialization in docker container

If you want to run Petclinic database initialization in a docker container this place is for you.

## Build the docker image
To build the docker image that will run the database initialization task just invoke the build-image.sh script from the local directory:
```
./build-image.sh
```

To see the content of the script [build-image.sh](./build-image.sh) - follow the script.

After successful run of this script you should see the following output:
```
Successfully built 99ef4a7c18d2
Successfully tagged petclinic-db-init:latest
```
## Running the db-initialization container
before running the db-initialization container, make sure that the mysql container with Petclinic database is up and running.
```
$ docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                    NAMES
afd46fae5ef6        mysql:5.7.20        "docker-entrypoint.s…"   22 hours ago        Up 9 seconds        0.0.0.0:3306->3306/tcp   mysql-standalone
```

Now we are ready to run the db-init container.
```
docker run --link=mysql-standalone -e MYSQL_HOST=mysql-standalone -e MYSQL_USER=root -e MYSQL_PASSWORD=petclinic -e MYSQL_DATABASE=petclinic petclinic-db-init
```
Where mysql-standalone is the name of the docker container with MySQL petclinic database that we have started earlier.
In addition, all the environment variables (e.g.: MYSQL_USER, MYSQL_PASSWORD, etc.) should match the values that we used when we started that mysql-standalone container.

## Verify that the petclinic database was populated
Connect to the mysql database container either via mysql client or via ssh-ing into the docker container.
```
docker exec -i -t mysql-standalone bash -l
```


Once inside the container, connect to the MySQL DB via mysql client:
```
mysql -uroot -ppetclinic
```

issue following sql commands:
```
mysql> use petclinic;
mysql> show tables;
+---------------------+
| Tables_in_petclinic |
+---------------------+
| owners              |
| pets                |
| specialties         |
| types               |
| vet_specialties     |
| vets                |
| visits              |
+---------------------+
7 rows in set (0.01 sec)

mysql> select * from vets;
+----+------------+-----------+
| id | first_name | last_name |
+----+------------+-----------+
|  1 | James      | Carter    |
|  2 | Helen      | Leary     |
|  3 | Linda      | Douglas   |
|  4 | Rafael     | Ortega    |
|  5 | Henry      | Stevens   |
|  6 | Sharon     | Jenkins   |
+----+------------+-----------+
6 rows in set (0.00 sec)
```
