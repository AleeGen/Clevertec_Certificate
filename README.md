
<h1 align="center">Gift Certificate</h1>
<h4 align="center">Rest application for working with gift certificate and tag</h4>

           Java '17'      |      Gradle '7.4.2'       |      Docker '20.10.23'      |      PostgreSQL       
---
### Stack:

- `org.springframework`
    - spring-webmvc
    - spring-orm
    - spring-boot-starter-web
    - spring-test
- `org.hibernate`
	- hibernate-core
	- hibernate-validator
- `jakarta.servlet:jakarta.servlet-api`
- `jakarta.validation:jakarta.validation-api`
- `org.postgresql:postgresql`
- `org.projectlombok:lombok`
- `cglib:cglib-nodep`
- `com.fasterxml.jackson`
- `org.mapstruct`
- `org.mockito`
- `org.assertj:assertj-core`
- `org.junit.jupiter`
	- junit-jupiter-api
	- junit-jupiter-engine
	- junit-jupiter-params
- `org.testcontainers`
	- postgresql
	- junit-jupiter
---


### Used example entity (All CRUD operations are supported):

> Request mapping: "/gcs" or "/tags"
> 
> for "/gcs" "/byTagName" and "/byPart"
> 
> with optional parameters "?sortType=(asc/desc)" and "?sortBy=(any field of the gift certificate)" N times

- ##### Get:
- Reqest	<pre> http://localhost:8080/gcs/byPart/n?sortType=desc&sortBy=price&sortBy=name </pre>
	- Response ![image](https://user-images.githubusercontent.com/100039077/235036309-1d615ff5-3e8a-4fd9-aebc-46c0ecbca3ff.png)

- ##### Post:
	- Request	![image](https://user-images.githubusercontent.com/100039077/235038571-9a87a797-d043-4f5a-b3c7-ffba6aba9cb4.png)
	- Response	![image](https://user-images.githubusercontent.com/100039077/235038653-5b980ab1-5433-46a4-8621-72b78aa500e5.png)

- ##### Put:
	- Request	![image](https://user-images.githubusercontent.com/100039077/235038135-4b2cd0ba-4007-4907-b03f-324cc1f5036d.png)
	- Response	![image](https://user-images.githubusercontent.com/100039077/235038195-ced1718d-11a2-402e-819e-41ec1b567583.png)

- ##### Delete:
	- Request	<pre>http://localhost:8080/gcs/2</pre>
	- Response	![image](https://user-images.githubusercontent.com/100039077/235039257-c607f9e8-f374-4733-8a09-9ba8bce28ec8.png)


