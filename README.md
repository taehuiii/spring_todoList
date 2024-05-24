# 🗒️ Todo List 🗒️ 

Kotlin과 Spring Boot로 구축한 간단한 Todo List 애플리케이션입니다.
***

## 🛠 사용 기술

- **Java:** 17
- **Spring Boot:** 3.2.5
- **Kotlin:** 1.9.23
- **데이터베이스:** PostgreSQL

## 🚀 주요 기능

- 할 일을 생성, 조회, 수정, 삭제할 수 있습니다.
- 할 일을 완료 처리할 수 있습니다.
- 할 일에 댓글을 작성, 수정, 삭제할 수 있습니다.
- 할 일을 오름차순/내림차순으로 정렬할 수 있습니다.
- 작성자 기준으로 할 일을 필터링할 수 있습니다.
- 할 일을 작성할 때 글자 수를 검사할 수 있습니다.
- 페이지네이션 기능 (추가 예정).
- 회원가입 및 로그인 기능 (추가 예정).

<details><summary>(추가 구현 사항)</summary>
  
- (later) pagination 기능   
- (later) 회원가입, 로그인 기능

</details> 

## 📋 사전 요구 사항

- JDK 17 이상
- Gradle
- PostgreSQL

## 📦 의존성

이 프로젝트는 다음과 같은 의존성을 사용합니다:

- `spring-boot-starter-web`: 웹 애플리케이션을 구축하기 위한 스타터.
- `jackson-module-kotlin`: Kotlin 지원을 위한 Jackson 모듈.
- `kotlin-reflect`: Kotlin 리플렉션 라이브러리.
- `spring-boot-starter-data-jpa`: Spring Data JPA를 위한 스타터.
- `postgresql`: PostgreSQL JDBC 드라이버.
- `spring-boot-starter-validation`: Hibernate Validator와 함께 Java Bean Validation을 사용하기 위한 스타터.
- `springdoc-openapi-starter-webmvc-ui`: Spring WebMVC와 Springdoc OpenAPI 통합을 위한 스타터.

## 📝 프로젝트 구조

이 프로젝트는 표준 Spring Boot 구조를 따릅니다:

```
src
├── main
│   ├── kotlin
│   │   └── com/teamsprta
│   │       └── your-package
│   │           ├── controller
│   │           ├── service
│   │           ├── repository
│   │           └── model
│   └── resources
│       └── application.properties
└── test
    └── kotlin
        └── com/teamsprta
            └── your-package
```




***


#### [ Use Case Diagram ]
<img width="500" alt="usecase" src="https://github.com/taehuiii/spring_todoList/assets/160212663/134b8647-8936-4102-8e1b-650d55d2661e">

***

#### [ Entity Relationship Diagram ]
<img width="500" alt="erd" src="https://github.com/taehuiii/spring_todoList/assets/160212663/76ca63a9-6e65-403b-9c4c-7a66177db4d8">


***

#### [ REST API ]
<img width="500" alt="api" src="https://github.com/taehuiii/spring_todoList/assets/160212663/5ea25077-3c69-4935-9b97-8c0f5ae03e68">



