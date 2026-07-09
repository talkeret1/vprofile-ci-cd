#!/bin/bash

set -e

if [ ! -f /var/vprofile_db_initialized ]; then

  sudo dnf update -y
  sudo dnf install -y git
  sudo wget https://dev.mysql.com/get/mysql80-community-release-el9-1.noarch.rpm
  sudo dnf install mysql80-community-release-el9-1.noarch.rpm -y
  sudo rpm --import https://repo.mysql.com/RPM-GPG-KEY-mysql-2023
  sudo dnf install mysql-community-client -y

  git clone ${GIT_REPO} /tmp/app

  # Initialize mySQL database schema

  until MYSQL_PWD=${DB_PASS} mysql -h ${DB_HOST_NAME} -u ${DB_USER} -e "SELECT 1"; do
    echo "Waiting for DB..."
    sleep 3
  done

  MYSQL_PWD=${DB_PASS} mysql -h ${DB_HOST_NAME} -u ${DB_USER} ${DB_NAME} < /tmp/app/Docker-files/db/db_backup.sql
  echo "Database schema imported successfully"

  mysql -h ${DB_HOST_NAME} -u ${DB_USER} ${DB_NAME} -e "SHOW TABLES;"
  echo "Database tables verified successfully"

  # Mark as done
  touch /var/vprofile_db_initialized
fi