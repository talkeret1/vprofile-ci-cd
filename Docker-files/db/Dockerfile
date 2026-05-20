FROM mysql:8.0.33

LABEL "Name"="Vprofile Project mySQL Database"

ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_DATABASE=accounts
ENV MYSQL_USER=admin_vp
ENV MYSQL_PASSWORD=admin_vp

# COPY db_backup.sql docker-entrypoint-initdb.d/vprofile-project-mysql-db.sql