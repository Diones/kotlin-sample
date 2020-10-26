Project to provide an overview and environment to exercise spring + kotlin for distributed applications. Explore AMQP, event streaming, caching and observability(metrics, logs, tracing) concepts.

# Getting Started

### Pre-requisites
| Tool   | Min Version |
| ---:   | :---        |
| Docker | 19.03.0     |
| Gradle | 19.03.0     |
| Kotlin | 1.3.72      |
| Java   | 11.0.8      |

### Local development :: docker
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

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Caching Data with Spring](https://spring.io/guides/gs/caching/)
* [Messaging with Redis](https://spring.io/guides/gs/messaging-redis/)
* [Messaging with RabbitMQ](https://spring.io/guides/gs/messaging-rabbitmq/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

