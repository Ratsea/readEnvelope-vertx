# readEnvelope-vertx
vert.x用拉姆达表达式和队列的形式实现一个抢红包的DEMO

#使用如下
执行test包下的T2类进行测试

#如果需要对vert.x的访问端口或线程池之类的进行修改,请修改application.yml
#切记 此项目因为用了springboot+vert.x 所以会有两个端口,一个是springboot内置的TOMCAT访问端口，默认8080,一个是vert.x设置的8089,如果需要修改，请修改application.yml对应的端口信息


