FROM postgres:15-bullseye

LABEL version=1.0.0-SNAPSHOT

# Default database settings. Override them at runtime with `docker run -e ...`
# or from compose if needed.
ENV POSTGRES_DB=bank_account
ENV POSTGRES_USER=bank_account
ENV POSTGRES_PASSWORD=bank_account

EXPOSE 5432

# Initialize schema and seed data on first startup when the data directory is empty.
# COPY /home/basedev/Workspace/vsprojects/api-service/docker/postgres/initdb/ /docker-entrypoint-initdb.d/
