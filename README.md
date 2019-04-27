# library
Study

## Prepare for running
`docker build -t bpaxio/library:0.1.0-SNAPSHOT`

## Docker run

#### Library

`docker create --name library --network lib-net -p 8080:8080 bpaxio/library:0.1.0-SNAPSHOT`
`docker run -d --name library --network lib-net -p 8080:8080 bpaxio/library:0.1.0-SNAPSHOT`

#### Mongo
`docker create --name mongo --network lib-net -p 27017:27017 mongo:latest`
`docker run --name mongo --network lib-net -p 27017:27017 mongo:latest`


#### Docker-compose
`docker-compose up -d`
