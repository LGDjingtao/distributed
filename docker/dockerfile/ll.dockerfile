FROM java:8 
MAINTAINER itfeng<xxxxxx666@163.com> 
ADD ./equipment-linkage-impl-1.0.0.jar app.jar
CMD java -jar app.jar 


