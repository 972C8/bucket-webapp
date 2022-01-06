# A-CRM Web App

This is a minimal CRM Web App (A-CRM - Insurance Agency CRM) as a template for students. 

[![License](https://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Deploy to Heroku](https://img.shields.io/badge/deploy%20to-Heroku-6762a6.svg?longCache=true)](https://heroku.com/deploy)

> 🚧 **This is a template project**: Make sure that you adapt this documentation and the source code in this project according to your needs and use case.

#### Contents:
- [Analysis](#analysis)
  - [Scenario](#scenario)
  - [User Stories](#user-stories)
  - [Use Case](#use-case)
- [Design](#design)
  - [Prototype Design](#prototype-design)
  - [Domain Design](#domain-design)
  - [Business Logic Design](#business-logic-design)
  - [Endpoint Design](#endpoint-design)
- [Implementation](#implementation)
  - [Backend Technology](#backend-technology)
  - [Frontend Technology](#frontend-technology)
- [Deployment](#deployment)
- [User Guide](#user-guide)
- [Project Management](#project-management)
  - [Roles](#roles)
  - [Milestones](#milestones)

## Analysis

### Scenario

A-CRM (Agency Customer-Relationship-Management) is the smallest possible and lightweight demonstration tool that allows agents to manage their customer data. Agents have an own access to their customer data.

### User Stories
1.	As an insurance avatar, I want to have a Web app so that I can use it on different mobile devices and on desktop computers.
2.	As an insurance avatar, I want to see a consistent visual appearance so that I can navigate easily, and it looks consistent.
3.	As an insurance avatar, I want to use list views so that I can explore and read my business data.
4.	As an insurance avatar, I want to use edit and create views so that I can maintain my business data.
5.	As an insurance avatar, I want to create an account so that I can get access to the Web app.
6.	As an insurance avatar, I want to log-in so that I can authenticate myself.
7.	As an insurance avatar, I want to edit my profile so that my data is stored securely.

### Use Case
![](images/use-case.png)

- UC-1 [Login on A-CRM]: Agents can log-in by entering an email address and password. As an extension, new agents my register first.
- UC-2 [Register on A-CRM]: Agents can register to get an account (profile) to access the A-CRM system.
- UC-3 [Edit a customer]: Agents can create, update and delete customers.
- UC-4 [Show a customer list]: Agents can get an overview over their customers based on a customer list. As an extension they can create, update and delete customers (UC-3).

## Design

### Prototype Design

A bootstrap based static prototype has been created by using a prototyping application. 

In this case, the prototype application Bootstrap Studio has been used to create a basic user interface design based on an HTML grid, Bootstrap CSS and JavaScript, including the selection of web fonts and font-based icons.

The assets (HTML, CSS, JavaScript, image and font files) has been exported and will be extended in the later during implementation with jQuery, to build a dynamic website.

### Domain Design

The `ch.fhnw.bucket.data.domain` package contains the following domain objects / entities including getters and setters:

![](images/domain-model.png)

### Business Logic Design

The `ch.fhnw.bucket.business.service` package contains classes of the following business services:

![](images/business-service.png)

### Endpoint Design
**Path**: [`/api/customer`](/api/customer) 

**Method:** `POST`

**Sample Request**  • *Header:* `Content-Type: application/json` • *Body:*

```JSON
{
  "avatar": {
    "customers": [
      null
    ],
    "email": "string",
    "id": 0,
    "name": "string",
    "password": "string",
    "remember": "string"
  },
  "email": "string",
  "id": 0,
  "mobile": "string",
  "name": "string"
}
```

• *Optional:* `...`
  
**Success Response**  • *Code:* `200 OK` • *Sample Body:*

```JSON
{
  "avatar": {
    "customers": [
      null
    ],
    "email": "string",
    "id": 0,
    "name": "string",
    "password": "string",
    "remember": "string"
  },
  "email": "string",
  "id": 0,
  "mobile": "string",
  "name": "string"
}
```

**Error Response** • *Code:* `404 NOT FOUND`

## Implementation

### Backend Technology
This Web application is relying on [Spring Boot](https://projects.spring.io/spring-boot) and the following dependencies:

- [Spring Boot](https://projects.spring.io/spring-boot)
- [Spring Web](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html)
- [Spring Data](https://projects.spring.io/spring-data)
- [Java Persistence API (JPA)](http://www.oracle.com/technetwork/java/javaee/tech/persistence-jsp-140049.html)
- [H2 Database Engine](https://www.h2database.com)
- [PostgreSQL](https://www.postgresql.org)

To bootstrap the application, the [Spring Initializr](https://start.spring.io/) has been used.

Then the following further dependencies has been added to the project `pom.xml`:

- Swagger and Swagger UI:
```XML
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-boot-starter</artifactId>
    <version>3.0.0</version>
</dependency>
```

- Java HTML Parser and JWT:
```XML
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.14.2</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.2</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.2</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-gson</artifactId>
    <version>0.11.2</version>
    <scope>runtime</scope>
</dependency>
```

### Frontend Technology
This Web application is relying on the following frontend technology/libraries:

- jQuery
- Bootstrap

## Deployment
This spring boot has been deployed to Heroku by using a pre-configuration scripts `app.json` and `Procfile`.

## User Guide
The Web application can be accessed over the browser by using the following address: `https://***.herokuapp.com/`. And the Swagger-UI can be access using the specific page: `https://***.herokuapp.com/swagger-ui/`.

## Project Management

### Roles
- All-rounder 😉: [Andreas Martin](https://andreasmartin.ch)

### Milestones
1. **Analysis**: Scenario ideation, use case analysis and user story writing.
2. **Prototype Design**: Creation of Bootstrap static web-design prototype.
3. **Domain Design**: Definition of domain model.
4. **Business Logic and API Design**: Definition of business logic and API.
5. **Data and API Implementation**: Implementation of data access and business logic layers, and API.
6. **Security and Frontend Implementation**: Integration of security framework and frontend realisation.
7. **Deployment**: Deployment of Web application on cloud infrastructure.

#### Maintainer
- [Andreas Martin](https://andreasmartin.ch)

#### License
- [Apache License, Version 2.0](blob/master/LICENSE)
