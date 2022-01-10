# bucket-webapp


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
One of the first major decisions we made as a group was the idea of an application for our project work. We voted for the idea of making a Bucketlist application which was very unambiguously. The application allows a users to create an account where they can manage their personal Bucketlist items with their goals in the future. The CRM template provided by the lecturers served as basis of our program. We adjusted and changed the template accordingly to our visions to deliver a well designed result. We even went as far as creating a second repository dedicated to the front-end development. (https://github.com/mahgoh/bucket-frontend.git)

By determining our goal for the project we set up the scene for the next step which is the definition of user stories regarding the Bucketlist application.

### User Stories
1. As a [user], I want to have a Web app so that I can use it on different mobile devices and on desktop computers.
2. As a [user], I want to see a consistent visual appearance so that I can navigate easily, and it looks consistent.
3. As a [user], I want to use list views so that I can explore and read my business data.
4. As a [user], I want to use edit and create views so that I can maintain my data
5. As a [user], I want to create an account so that I can get access to the Web app.
6. As a [user], I want to log-in so that I can authenticate myself.
7. As a [user], I want to edit my profile so that my data is stored securely.

In addition to the minimal generic user stories, the following user stories were defined:
1.	As a [user], I want to view a timeline of achieved BLI so that I can get an overview of my past achievements.
2.	As a [user], I want to create, read, update, and delete BLI so that I can manage my bucket list.
3.	As a [user], I want to add different data to my BLI so that I can further personalize my BLI.
4.	As a [user], I want a dashboard as a list of categorized BLI so that I can get an overview of my BLI.
5.	As a [user], I want to create categories for my BLI so that I have my BLI organized.
6.	As a [user], I want a detailed view of BLI so that I can read up on the details.
7.	As a [user], I want to add a focus to my BLI so that I can see my prioritized BLI.

### Use Case / Fuctionalities
<img src="images/Use-case_BucketWebapp.PNG" width="700">

- UC-1 [Login on bucket-webapp]: Customers can log-in by entering an email address and password. As an extension, new customers may register first.
- UC-2 [Register on bucket-webapp]: Customers can register to get an account (profile) to access the bucket-webapp.
- UC-3 [Edit profile information]: Customers can edit their profile information, e.g. Email/password/name.
- UC-4 [Create a BLI]: Customers can create BLI. They can choose a title, description, date to accomplish, bucket, label, and an icon. These BLI can be edited/deleted[UC-5] and their status can be set to achieved[UC-6].
- UC-7-10: The BLI can be viewed in different views. A Customer can see all of the BLI at once / on the timeline / filtered by buckets / filtered by labels.


API
  •	Read bucket entries by filters (year, date, priority, ...)
    Limitations for first prototype: Social media aspect

## Design

### Prototype Design
After the Analysis we started with the prototype design. At first we only had the prototype design for the timeline and the dashboard. After the first designs were given we focused on the domain model and the business logic (including the API design). Simultaneously we started to code the website according to our designs. During this phase we also designed the profile page and the page to add a new item.
We used another repository for easier deployment of the prototypes (https://github.com/mahgoh/bucket-frontend.git)

The assets (HTML, CSS, JavaScript, image and font files) has been exported and will be extended in the later during implementation with jQuery, to build a dynamic website.

We used a method called brainwriting to gather ideas for certain designs of our interface. With this method the members of our group individually wrote down their ideas and presented them in a Microsoft Teams meeting to the team. The advantage of this method was that everyone of the team already had their ideas ready and thus we did not waste time to look for ideas but combined the presented ideas to create our design.
An example for the bucketlist timeline design written by hand:

<img src="images/bucketList_Design_Timeline2.png" width="300">


An example for the dashboard design written by hand:

<img src="images/Dashboard_Brainstorming_Luca_Herlig.png" width="600">

### Domain design

The domain model describes the domain objects / entities that are found in `ch.fhnw.bucket.data.domain`. Note that for clarity getters and setters are not included in the domain model.

Key points include:
* The entity **AbstractImage** is an abstract class with inheritance to both BucketItemImage and ProfilePicture. They are used to store uploaded Images as profile pictures or images for bucket items.
* The entity **BucketItem** as the idea of bucket items (similar to todo items) is at the core of our bucket-webapp. Logically, many connections to this entity exist.
* The ManyToMany relationship between BucketItem and Label. BucketItemToLabel signals the intermediate table, according to the UML standard.

![](images/bucket-domain-model.png)

### Business Logic Design

The bucket-webapp package contains classes of the following business services:

/ haben wir das?/

### Endpoint Design

The [Postman](https://www.postman.com/) API Platform was used for the endpoint design and during the implementation of the backend. Using Postman allowed us to create the API collaboratively and efficiently thanks to a synchronized workflow. Furthermore, Postman allowed us to create a user-friendly, web-view of the API documentation.

Furthermore, as a backup, the bucket-webapp-api repository (https://github.com/972C8/bucket-webapp-api) was created to automatically push API changes in Postman to Github.

**Please check out our endpoint design at https://documenter.getpostman.com/view/17679206/UVXerdXY for a user-friendly web-view of the API.**

![](images/bucket-endpoint-design-postman_doublecolumn.png)

## Implementation

### Backend Technology

The backend was initially based on a fork of https://github.com/DigiPR/acrm-webapp. The following excerpt is copied from acrm-webapp and explains the main project dependencies:

#### Dependencies according to the ACRM fork:
This Web application is relying on [Spring Boot](https://projects.spring.io/spring-boot) and the following dependencies:

- [Spring Boot](https://projects.spring.io/spring-boot)
- [Spring Web](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html)
- [Spring Data](https://projects.spring.io/spring-data)
- [Java Persistence API (JPA)](http://www.oracle.com/technetwork/java/javaee/tech/persistence-jsp-140049.html)
- [H2 Database Engine](https://www.h2database.com)
- [PostgreSQL](https://www.postgresql.org)

To bootstrap the application, the [Spring Initializr](https://start.spring.io/) has been used.

### Backend Implementation

The backend serves two main purposes: Provide an API endpoint the frontend can interact with, and interact with the database using custom business logic and through API calls by extension.

#### Domain Design:
Data classes were implemented according to the created [domain design](#domain-design) and are found under `ch.fhnw.bucket.data.domain`. Using JPA it was possible to dynamically create the database schema with the help of annotations, such as @Entity and @Id.
Furthermore, the relationship mapping was defined using JPA annotations such as @OneToOne, @OneToMany, @ManyToMany, etc. This means that there was no need to manually create a database as JPA automatically does the job for us when the annotations were used.

This example highlights the defined relationships using JPA. The @OneToOne/@OneToMany relationships from Avatar correspond to the domain model.

![](images/backend-jpa_explanation.png)

#### Support of Images (using Inheritance)
The support of uploading images both as profile pictures and for bucket items is enabled through a custom implementation that stores images as byte[] in the database and using inheritance with AbstractImage as the abstract superclass.

This is highlighted in the domain model and implemented accordingly. The abstract class AbstractImage holds the main attributes relevant to images (such as fileName, fileType and data) and the classes ProfilePicture and BucketItemImage extend it.

**Single table inheritance and discriminator:**

The image implementation uses single table inheritance and a discriminator column.
This effectively means that only asingle table is created in the database (although there are 3 classes!) and that the discriminator is used
to determine which class the particular row belongs to. More information is found at https://en.wikibooks.org/wiki/Java_Persistence/Inheritance#Single_Table_Inheritance

#### Location (Google Maps API)

When creating a bucket item, a specific location can be added. For example: the location "Big Ben, London, England" can be added to the bucket item "Run a Marathon".

The location is added as regular text and uses the **Google Maps API** to display the location using an **embeded map**.

Example:
//TODO: Add example!

More information is found at: https://developers.google.com/maps/documentation/embed/get-started

#### API Endpoint

The [API Endpoints](#endpoint-design) are found under `ch.fhnw.bucket.api`. In the pursuit of clarity, a class was created for each data class that is touched (e.g. AvatarEndpoint, BucketEndpoint, LabelEndpoint, ...).

The API endpoints call the respective business services (found under `ch.fhnw.bucket.business.service`) to perform the called methods (such as creating, reading, updating, or deleting entities).

For each entity, a repository class (found under `ch.fhnw.bucket.data.repository`) was created to interact with the database. This is a functionality offered through JPA.

![](images/backend-api_explanation.png)

### Frontend Technology

The visual appearance of the frontend was developed in a separate repository ([bucket-frontend](https://github.com/mahgoh/bucket-frontend)) with the usage of a custom component-based HTML bundler - written in [Go](https://go.dev/) specifically for this project. More about how the bundler works and how to use it is mentioned in the repository.

The power of utility classes in CSS, especially, when working in a team is incredible. We use [Tailwind CSS](https://tailwindcss.com/) to make use of this and ensure a great collaboration and maintainability in the future. During development, the Tailwind CSS Play CDN is used - a just-in-time CDN in the browser. Once completed with the design, the Tailwind CSS CLI is used to create a CSS bundle with only the used utility classes. The bundler does the same for the HTML files. These static files can then be integrated with the backend.

- [Custom HTML bundler](https://github.com/mahgoh/bucket-frontend)
- [Tailwind CSS](https://tailwindcss.com/)
- [JQuery](https://jquery.com/)

## Deployment

TODO: Deploy on Heroku  
TODO: Write section about deployment

## User Guide
This Web application can be accessed over the browser by using the following address: `https://bucket-webapp.herokuapp.com`.

## Project Management

### Roles
- Leader Backend: Tibor Haller
- Leader Frontend: Marco Kaufmann
- Frontend / Documentation: Luca Herlig
- Frontend / Documentation: Flavio Filoni

### Milestones
1. **Analysis**: Scenario ideation, use case analysis and user story writing.
2. **Prototype Design**: Creation of Bootstrap static web-design prototype.
3. **Domain Design**: Definition of domain model.
4. **Business Logic and API Design**: Definition of business logic and API.
5. **Data and API Implementation**: Implementation of data access and business logic layers, and API.
6. **Security and Frontend Implementation**: Integration of security framework and frontend realisation.
7. **Deployment**: Deployment of Web application on cloud infrastructure.

#### License
- [Apache License, Version 2.0](blob/master/LICENSE)****


