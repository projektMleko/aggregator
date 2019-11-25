FROM openjdk:11.0.5-stretch
ADD build/libs/aggregator-search-0.1.0.jar .
EXPOSE 8000
CMD java -jar aggregator-search-0.1.0.jar

#To create:
#docker build -f Dockerfile -t aggregator_search:v[X] .

#To find image id:
#docker images

#To run:
#docker run -p 8005:8080 [IMAGE_ID]