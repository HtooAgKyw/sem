# Use a specific MySQL LTS version
FROM mysql:8.4

WORKDIR /tmp
# Copy supporting SQL and dump files
COPY test_db/*.sql /tmp/
COPY test_db/*.dump /tmp/

# MySQL automatically executes initialization scripts
# placed in /docker-entrypoint-initdb.d/
COPY test_db/employees.sql /docker-entrypoint-initdb.d/

# Root password for the lab environment
ENV MYSQL_ROOT_PASSWORD=example