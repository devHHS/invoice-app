# invoice-app 커리큘럼

이 문서는 "무엇을 배울지"(콘텐츠)만 담는다. "언제 뭘 했는지"는 `docs/logs/`에 실제 날짜 기준으로 기록한다.

항목은 순서대로 진행하지만 **요일/주차에 묶이지 않는다** — 진도가 빠르거나 느려도 이 목록 자체는 바뀌지 않고, 그냥 다음 미완료 항목을 이어서 하면 된다. 각 Month 끝에는 "완료 조건"이라는 체크포인트가 있어 페이스를 가늠할 수 있다.

## Quarkus → Spring 대응표 (참고용, 한 번만)

| Quarkus | Spring Boot |
|---|---|
| `@Path` + `@GET` | `@RestController` + `@GetMapping` |
| `@ApplicationScoped` | `@Service` |
| `@Inject` | 생성자 주입 (권장) |
| `PanacheRepository` | `JpaRepository` |
| `application.yml` | `application.yml` |
| Dev Mode 핫리로드 | Spring DevTools |

---

## Month 1 — CRUD API + 계층 구조 + 예외 처리 + Docker

### Phase 1 — 프로젝트 셋업, 앱이 뜬다

- [x] Spring Initializr 셋업 (Maven, Java 21, Web·JPA·PostgreSQL·Lombok·Validation), 저장소 생성
- [x] `docker-compose.yml`로 PostgreSQL 연결, `application.yml` DB 설정
- [x] `Invoice` Entity 정의 (id, storeName, amount, issuedAt, category) — 테이블 자동 생성 확인
- [x] (안 쓰는 Hello World 스캐폴드는 이후 정리해서 제거)

**완료 조건**: `docker compose up -d`로 DB가 뜬다 / 앱 실행 시 `Invoice` 테이블이 자동 생성된다

### Phase 2 — CRUD 완성 (3계층)

- [x] `InvoiceRepository extends JpaRepository<Invoice, Long>`
- [x] `InvoiceService` — save, findAll (생성자 주입)
- [x] `InvoiceController` — POST, GET
- [x] GET(단건), PUT, DELETE
- [x] `docker exec`로 psql 접속해 데이터 직접 확인

**완료 조건**: POST / GET(전체) / GET(단건) / PUT / DELETE 5개 동작 / psql로 DB에 실제 데이터가 있음을 직접 확인

### Phase 3 — DTO 분리, 계층 책임 명확화, 입력 검증

- [x] 각 계층 상단에 "이 계층의 책임" 주석 1줄 (Controller/Service/Repository/Entity 전부)
- [x] `InvoiceRequest`/`InvoiceResponse` DTO 작성
- [x] Controller가 DTO를 주고받도록 변경
- [x] 변환 로직(`toEntity`/`toResponse`)을 Controller에서 Service로 이동
- [x] `@Valid` + `@NotNull`/`@NotBlank`/`@Positive` 입력 검증 적용 (save, update 둘 다)

**완료 조건**: API 응답에 Entity가 직접 노출되지 않는다 / 잘못된 입력이 400으로 거부된다

### Phase 4 — 예외 처리

- [x] `InvoiceNotFoundException` 정의, Service(`findById`/`update`/`delete`)에서 던지기
- [x] `@RestControllerAdvice` + `@ExceptionHandler`로 전역 처리 — 404 + JSON 에러 응답
- [ ] Validation 실패(400)도 같은 응답 형태로 통일

### Phase 5 — 마무리, Docker로 전체 실행

- [ ] 백엔드 `Dockerfile` 작성 + `docker-compose.yml`에 통합
- [ ] 검색 기능 (`findByStoreNameContaining`, 쿼리 메서드 이름만으로 SQL 자동 생성)
- [ ] TODO 정리 (최소 커밋)
- [ ] README 완성 (프로젝트 3줄 소개, 실행 방법, API 목록 표, 아키텍처 다이어그램, 배운 것/앞으로 할 것)
- [ ] 월간 회고

**Month 1 완료 조건**
- [ ] `docker compose up` 하나로 API가 뜬다
- [ ] CRUD 5개 + 검색이 동작한다
- [ ] 에러 응답 형식이 통일되어 있다
- [ ] 요청이 Controller → Service → Repository → DB로 가는 흐름을 종이에 그릴 수 있다
- [ ] 28일 연속 커밋

> 마지막 항목이 이 달의 전부다. 기능 개수는 중요하지 않다.

---

## Month 2 예고 — 모듈러 모놀리스

W05부터 도메인 기준 패키지 재구성. 이 계획의 핵심 구간이다. (상세 항목은 Month 1 마무리 시점에 채운다.)

---

## 자주 막히는 곳 (누적)

**DB 연결 실패** — 대부분 포트 또는 비밀번호 불일치. 에러 메시지 **마지막 줄부터** 읽는다.

**`ddl-auto` 설정** — 학습 단계에서는 `update`. `create-drop`은 재시작마다 데이터가 날아간다. 기존 컬럼의 생성 전략 변경(예: AUTO→IDENTITY)은 반영 못 함 — 실무에서는 Flyway/Liquibase 같은 마이그레이션 툴을 쓴다(개념만, 아직 실습 안 함).

**JSON 역직렬화 실패** — Entity/DTO에 기본 생성자가 없으면 발생.

**`@Valid`가 안 먹힘** — Controller의 `@RequestBody` 파라미터에 붙였는지 확인. Service에 붙이면 동작하지 않는다. HTTP 메서드(POST/PUT)가 아니라 파라미터에 실제로 붙어 있는지로만 결정된다.

**예외를 던지는 것과 잡는 것은 별개 단계** — `throw`만 하고 아무도 `catch`(`@ExceptionHandler`)하지 않으면 Spring 기본값인 500으로 처리된다.

## 이해 체크 (누적, 주말/세션 끝에 답할 수 있어야 한다)

- `@SpringBootApplication` 하나가 하는 일 세 가지는?
- 톰캣이 뭐고, 왜 따로 설치 안 했는데 8080이 뜨는가?
- 클래스가 왜 테이블이 되는가? 누가 그 일을 하는가?
- Repository 구현체를 안 썼는데 왜 동작하는가?
- `@Autowired` 필드 주입 대신 생성자 주입을 쓰는 이유 두 가지는?
- POST와 PUT의 차이는? 왜 나눠 쓰는가?
- Entity를 그대로 반환하면 뭐가 문제인가? (3가지)
- 변환 로직이 Controller가 아니라 Service에 있어야 하는 이유는?
- 각 계층(Controller/Service/Repository/Entity)의 책임을 한 문장씩 말하라
- 검증 애노테이션을 Entity가 아니라 DTO에 붙이는 이유는?
- 예외를 Controller에서 잡는 것과 `@RestControllerAdvice`로 잡는 것의 차이는?
- 쿼리 메서드 이름만 썼는데 왜 SQL이 만들어지는가?
- 왜 애플리케이션까지 Docker로 감싸는가? 로컬 실행과 뭐가 다른가?
