Project to provide an overview and environment to exercise spring + kotlin for distributed applications. Explore AMQP, event streaming, caching and observability(metrics, logs, tracing) concepts.

This project includes two applications (user and card), docker-compose files which will create the whole environment for you, postman files (collections and environments) so will be easy to getting started with available endpoints which includes CRUD operations for users and cards as well as endpoints to health checkers, caches and others endpoints exposed by the spring boot actuator. Also, includes k8s files, so you will be able to explore a local cluster environment.
The card application depends on user in order to have a card created for a specific user. This integration uses HTTP Rest, the tool chosen here was Feign, to leverage features such as declarative rest client capability and distributed tracing feature that includes trace-id header in its requests. The integration between card and user will leverage local and distributed caching in order to improve performance and all CRUD endpoints will add message/events into rabbitmq queues as well as kafka topics.  

# Getting Started

### Pre-requisites
| Tool   | Min Version |
| ---:   | :---        |
| Docker | 19.03.0     |
| Gradle | 19.03.0     |
| Kotlin | 1.3.72      |
| Java   | 11.0.8      |

### K8s environment
You can choose any tool in order to have a local kubernetes cluster running. To this project we're going to use [Kind](https://kind.sigs.k8s.io/docs/user/quick-start/) version 0.9.0. Kind was chosen mainly because its environment that leverages docker container features.
Also, we're using [Kompose](https://github.com/kubernetes/kompose) once we already had a docker-compose structure defined in the project.

### Local development :: Docker Compose
Enter docker directory `<project>/src/main/resources/docker` and run `docker-compose up -d`.

### Dashboard access
* Portainer → Browser http://localhost:9000
* Jaeger → Browser http://localhost:16686
* Kibana → Browser http://localhost:5601
* Prometheus → Browser http://localhost:9090
* Grafana → Browser http://localhost:9090 → `user: admin, password: admin`
* RabbitMQ → Browser http://localhost:15672 → `user: guest, password: guest`
* Kafdrop → Browser http://localhost:9050
* Redis → Install [Redis Desktop Manager](https://redisdesktop.com/) and use `localhost:6379`  

### Hooking up Jaeger with ELK
1. Open the Management → Kibana → Index Patterns page.
2. Enter `jaeger-span-*` as your index pattern, and in the next step select the `statTimeMillis` field as the time field.
> Reference: https://logz.io/blog/jaeger-and-the-elk-stack/

### Configure prometheus + grafana
1. Access [Grafana](http://localhost:9090) → `user: admin, password: admin`
2. Add your first data Source → Prometheus
3. Dashboards → Manage → Choose `JVM (Micrometer)`, `Micrometer metrics` or `RabbitMQ-Overview`.

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.3.3.RELEASE/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.3.3.RELEASE/gradle-plugin/reference/html/#build-image)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.3.3.RELEASE/reference/htmlsingle/#production-ready)
* [Spring cache abstraction](https://docs.spring.io/spring-boot/docs/2.3.3.RELEASE/reference/htmlsingle/#boot-features-caching)
* [Spring Data Redis (Access+Driver)](https://docs.spring.io/spring-boot/docs/2.3.3.RELEASE/reference/htmlsingle/#boot-features-redis)
* [Spring for RabbitMQ](https://docs.spring.io/spring-boot/docs/2.3.3.RELEASE/reference/htmlsingle/#boot-features-amqp)
* [Spring for Apache Kafka](https://docs.spring.io/spring-boot/docs/2.3.3.RELEASE/reference/htmlsingle/#boot-features-kafka)
* [Kafdrop](https://github.com/obsidiandynamics/kafdrop)
* [Prometheus](https://docs.spring.io/spring-boot/docs/2.3.3.RELEASE/reference/html/production-ready-features.html#production-ready-metrics-export-prometheus)
* [Jaeger Tracing](https://www.jaegertracing.io/docs/1.19/getting-started/)
* [Elastic Stack ("ELK")](https://www.elastic.co/guide/en/elastic-stack-get-started/7.9/get-started-elastic-stack.html)
* [Portainer](https://www.portainer.io/documentation/)
* [Grafana](https://grafana.com/docs/grafana/v7.0/)
* [Docker Compose](https://docs.docker.com/compose/)
* [Kind](https://kind.sigs.k8s.io/docs/user/quick-start/)
* [Kompose](https://github.com/kubernetes/kompose)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Caching Data with Spring](https://spring.io/guides/gs/caching/)
* [Messaging with Redis](https://spring.io/guides/gs/messaging-redis/)
* [Messaging with RabbitMQ](https://spring.io/guides/gs/messaging-rabbitmq/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

