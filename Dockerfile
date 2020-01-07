# latest oracle openjdk is the basis
FROM openjdk:oracle

# set maintainer
MAINTAINER Murat Artim "muratartim@gmail.com"

# copy jar
COPY target/EquinoxDataServer-*.jar dataServer/dataServer.jar

# copy libraries
COPY target/libs dataServer/libs

# copy resources
COPY resources/config.properties dataServer/resources/config.properties

# expose server port
EXPOSE 1235

# change to server directory
WORKDIR /dataServer

# start server
CMD ["java", "-jar", "dataServer.jar", "-XX:+UseStringDeduplication", "-Xverify:none", "-server", "-XX:+UseParallelGC"]

# to build the docker image: (from where this file is located)
# docker image build -t muratartim/equinox_data_server:latest .

# to create & run the container:
# docker container run -e sftp.hostname=equinox_sftp_server 
#                      -e sftp.port=22 
#                      -e sftp.username=aurora 
#                      -e sftp.password=17891917 
#                      -e sftp.rootPath=filerRoot/ 
#                      -p 1235:1235 -d --name equinox_data_server --network equinox_network muratartim/equinox_data_server

# to start an existing container
# docker container start equinox_data_server