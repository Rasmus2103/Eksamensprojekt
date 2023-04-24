FROM lakruzz/lamj:latest

# Install Maven and Java
RUN apt-get update && apt-get install -y maven openjdk-11-jre

ENV PORT=8080
ENV MYSQL_PORT=3306

COPY src /src
COPY pom.xml /pom.xml
COPY mvnw /mvnw
COPY .mvn /.mvn

RUN set -ex; \
     mvn -f /pom.xml clean package; \
     mv /target/*.jar /app/; \
     rm -rf /target; \
     rm -rf /src; \
     rm -rf /pom.xml; \
     rm -rf /mvnw; \
     rm -rf /.mvn;

COPY src/mysql/init/* /docker-entrypoint-initdb.d/

EXPOSE $PORT $MYSQL_PORT

CMD set -eux; \
    lamj.init.sh; \
    java -jar /app/*.jar;

# Build like this:
# docker build  -t wishlist .

# Run like this:
# docker run -it --rm --name wishlist --pid=host -p 8080:8080 -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root wishlist
#
#   - `docker run`: This command is used to run a container from an image.
#   - `-it`: This switch allocates a pseudo-TTY and opens an interactive terminal within the container.
#   - `--rm`: This switch removes the container automatically after it exits. (useful for development, but it resets the database every time)
#   - `--name wishlist`: This sets the name of the container to "superhero5".
#   - `--pid=host`: This runs the container in the host's PID namespace. (enable this if you  want to debug the container with a debugger or if you want to be able to stop the container with CTRL-C)
#   - `-p 8080:8080`: This maps port 8080 from the container to port 8080 on the host.
#   - `-p 3306:3306`: This maps port 3306 from the container to port 3306 on the host.
#   - `-e MYSQL_ROOT_PASSWORD=root`: This environment variable sets the root password for MySQL 'root' is default.
#   - `wishlist`: This specifies the name of the image to run.
