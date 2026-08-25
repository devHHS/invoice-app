# --- 빌드 스테이지: 메이븐 + JDK가 들어있는 이미지로 jar를 만든다. 이 스테이지의 내용물은 최종 이미지에 남지 않는다 ---
# 이미지 안의 메이븐 버전(3.9)은 이 텍스트로 고정된다 — 로컬 메이븐 버전을 바꿔도 이 값은 안 바뀐다
FROM maven:3.9-eclipse-temurin-21 AS build
# 이후 명령어들이 실행될 컨테이너 내부 작업 폴더
WORKDIR /app
# 의존성(pom.xml)과 소스(src)를 호스트에서 이미지 안으로 복사
COPY pom.xml .
COPY src ./src
# 빌드 시점에 한 번 실행 — target/invoice-app-0.0.1-SNAPSHOT.jar 생성. 이미지 빌드용이라 테스트는 건너뜀
RUN mvn package -DskipTests

# --- 실행 스테이지: 메이븐 없이 JRE(실행 환경)만 있는 가벼운 새 이미지로 시작 ---
FROM eclipse-temurin:21-jre
WORKDIR /app
# 빌드 스테이지(AS build)에서 완성된 jar 파일 하나만 골라서 복사 — 메이븐·소스 코드는 안 딸려옴
COPY --from=build /app/target/invoice-app-0.0.1-SNAPSHOT.jar app.jar
# 컨테이너가 시작될 때 실행할 명령어 — 배열 형태라 셸을 거치지 않고 바로 실행됨
ENTRYPOINT ["java", "-jar", "app.jar"]