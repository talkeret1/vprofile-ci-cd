FROM mysql:8.0.33
LABEL "Name"="Vprofile Project mySQL Database"

ENV MYSQL_ROOT_PASSWORD="vprodbpass"
ENV MYSQL_DATABASE="accounts"

COPY db_backup.sql docker-entrypoint-initdb.d/vprofile-project-mysql-db.sql

