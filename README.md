# OData v4 Service with Spring Boot and Olingo

This project is an attempt to build an **OData v4 service** using **Spring Boot** and the **Apache Olingo** library in Java. The goal is to expose a RESTful OData API that interacts with a JPA-based database.

## Technologies Used
- **Spring Boot**
- **Apache Olingo (OData v4)**
- **JPA (Java Persistence API)**
- **H2/PostgreSQL/MySQL** (depending on configuration)

## Purpose
This repository serves as an experimental implementation to explore how **OData v4** can be integrated into a **Spring Boot** application using **Apache Olingo**. The service will support querying, filtering, and modifying entity data via standard OData endpoints.

## Status
🚧 Work in progress – contributions and improvements are welcome!

I followed these tutorials:
- SAP Community Tutorial: [Building an OData Service with Spring Boot using Olingo](https://community.sap.com/t5/application-development-blog-posts/building-an-odata-service-with-a-spring-boot-java-application-using-olingo/ba-p/13503545)

- Baeldung Tutorial: [Olingo and OData in Java](https://www.baeldung.com/olingo)

My issue likely arises because these tutorials were built for Java 8 or older Java versions, while I am using Java 17. 
Probably Java 17 has removed some older APIs that existed in Java 8, making some libraries incompatible like "No access to javax.ws.rs.core.Application" because ODataApplication.class (look at JerseyConfig.class) extends javax.ws.rs.core.Application, which is no longer available in Java 17.



## How to Run
1. Clone the repository
2.  Run the SpringBoot application

