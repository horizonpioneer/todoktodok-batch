# TodokTodok 배치 프로젝트

토독토독 데이터베이스에 대량의 테스트 데이터를 생성하기 위한 Spring Batch 프로젝트

## 프로젝트 구조

```
batch/
├── build.gradle
├── settings.gradle
├── src/
│   └── main/
│       ├── java/
│       │   └── todoktodok/
│       │       └── batch/
│       │           ├── TodokTodokBatchApplication.java     # 메인 애플리케이션
│       │           ├── config/
│       │           │   └── BatchConfig.java                # 배치 설정
│       │           ├── entity/                             # JPA 엔티티
│       │           │   ├── TimeStamp.java
│       │           │   ├── Member.java
│       │           │   ├── Book.java
│       │           │   ├── Discussion.java
│       │           │   ├── Comment.java
│       │           │   └── Reply.java
│       │           ├── repository/                         # JPA Repository
│       │           │   ├── MemberRepository.java
│       │           │   ├── BookRepository.java
│       │           │   ├── DiscussionRepository.java
│       │           │   ├── CommentRepository.java
│       │           │   └── ReplyRepository.java
│       │           └── job/                               # 배치 작업
│       │               ├── DataGenerationJobConfig.java
│       │               └── DataGenerationJobRunner.java
│       └── resources/
│           └── application.yml                           # 애플리케이션 설정
```

## 기능

이 배치 프로젝트는 다음과 같은 테스트 데이터를 자동으로 생성한다:

- **회원 (Member)**
- **책 (Book)**
- **토론 (Discussion)**
- **댓글 (Comment)**
- **답글 (Reply)**

## 사용 방법

### 1. 데이터베이스 설정

`src/main/resources/application.yml` 파일에서 데이터베이스 연결 정보를 수정한다:


### 2. 배치 실행

#### 방법 1: Gradle을 이용한 실행

```bash
./gradlew bootRun
```

#### 방법 2: JAR 파일 빌드 후 실행

```bash
# JAR 파일 빌드
./gradlew clean build

# JAR 파일 실행
java -jar build/libs/todoktodok-batch-0.0.1-SNAPSHOT.jar
```

#### 방법 3: IDE 실행


### 3. 실행 결과 확인

배치 작업이 실행되면 다음과 같은 단계로 진행된다:

1. 회원 데이터 생성
2. 책 데이터 생성
3. 토론 데이터 생성
4. 댓글 데이터 생성
5. 답글 데이터 생성

각 단계마다 로그가 출력되어 진행 상황을 확인할 수 있다.

## 데이터 생성량 조정

데이터 생성량을 조정하려면 `DataGenerationJobConfig.java` 파일의 각 Tasklet에서 `count` 변수를 수정한다:

```java
// 예시: 회원 데이터 생성량 조정
@Bean
public Tasklet memberGenerationTasklet() {
    return (contribution, chunkContext) -> {
        int count = 1000; // 이 값을 원하는 수로 변경
        // ...
    };
}
```

## 주의사항

1. **데이터베이스 백업**: 배치 실행 전에 데이터베이스를 백업을 진행해야 한다.
2. **중복 실행**: 배치를 여러 번 실행하면 데이터가 계속 누적된다.
3. **성능**: 대량의 데이터를 생성하므로 실행 시간이 오래 걸릴 수 있다.
4. **메모리**: 많은 데이터를 생성할 경우 JVM 메모리 설정을 조정해야 할 수 있다.

```bash
# 메모리 설정 예시
java -Xmx2g -jar build/libs/todoktodok-batch-0.0.1-SNAPSHOT.jar
```

## 기술 스택

- Java 21
- Spring Boot 3.4.7
- Spring Batch
- Spring Data JPA
- MySQL
- Gradle

## 트러블슈팅

### 데이터베이스 연결 실패

- MySQL 서버가 실행 중인지 확인한다.
- `application.yml`의 연결 정보가 올바른지 확인한다.
- 데이터베이스가 생성되어 있는지 확인한다.

### 메모리 부족 에러

- JVM 힙 메모리를 늘려서 실행한다:
  ```bash
  java -Xmx4g -jar build/libs/todoktodok-batch-0.0.1-SNAPSHOT.jar
  ```

### 외래 키 제약 조건 에러

- 데이터베이스 스키마가 백엔드 프로젝트와 일치하는지 확인한다.
- Flyway 마이그레이션이 정상적으로 실행되었는지 확인한다.
