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
- [x] Validation 실패(400)도 같은 응답 형태로 통일

### Phase 5 — 마무리, Docker로 전체 실행

- [x] 백엔드 `Dockerfile` 작성 + `docker-compose.yml`에 통합
- [x] 검색 기능 (`findByStoreNameContaining`, 쿼리 메서드 이름만으로 SQL 자동 생성)
- [x] TODO 정리 (최소 커밋) — 코드 전체 확인, 남은 TODO/FIXME 없음
- [x] README 완성 (프로젝트 3줄 소개, 실행 방법, API 목록 표, 아키텍처 다이어그램, 배운 것)
- [x] 월간 회고

**Month 1 완료 조건**
- [x] `docker compose up` 하나로 API가 뜬다
- [x] CRUD 5개 + 검색이 동작한다
- [x] 에러 응답 형식이 통일되어 있다
- [x] 요청이 Controller → Service → Repository → DB로 가는 흐름을 종이에 그릴 수 있다
- [x] 28일 연속 커밋

> 마지막 항목이 이 달의 전부다. 기능 개수는 중요하지 않다.

---

## Month 2 — 모듈러 모놀리스

도메인 기준 패키지 재구성. 지금은 계층별도 아니고 `com.ham.invoiceapp` 하나에 클래스 10개가 평평하게 들어있는 상태 — 여기서 "도메인 기준 패키지"로 옮겨간다. `Invoice` 하나뿐이면 재구성 실익이 잘 안 보이므로, 두 번째 도메인(`Vendor`)을 추가해 실제 모듈 경계를 체감하는 것까지 포함한다.

### Phase 1 — 기존 코드를 `invoice/` 패키지로 이동

- [x] `Invoice`/`InvoiceRequest`/`InvoiceResponse`/`InvoiceController`/`InvoiceService`/`InvoiceRepository`/`InvoiceNotFoundException`/`GlobalExceptionHandler`/`ErrorResponse`를 `com.ham.invoiceapp.invoice` 패키지로 이동
- [x] 패키지 밖에서 쓸 필요 없는 클래스(예: `ErrorResponse`, 헬퍼 성격 클래스)를 package-private으로 좁혀서 캡슐화 확인

**완료 조건**: 이동 후 `./mvnw spring-boot:run` + `requests.http` 전체 요청 재확인, 기존 동작 그대로 유지

### Phase 2 — 두 번째 도메인(`Vendor`) 추가

- [x] `Vendor` 엔티티 신규 작성 (`com.ham.invoiceapp.vendor` 패키지)
- [x] `@ManyToOne` 연관관계 매핑 — "왜 문자열 대신 연관관계로 쪼개는가" 개념 정리, `Invoice.vendor` 필드 추가 + `InvoiceService`에서 조회·연결까지 완료(단, `Invoice.storeName`은 아직 병행 유지 — 완전 제거는 다음 항목)
- [x] `Invoice.storeName`(문자열) 필드 제거, 검색 기능을 `Vendor` 기준으로 전환 — 기존 데이터 백필(psql) 후 `vendorId` `@NotNull` 적용, `findAllByVendor_StoreNameContaining`으로 전환
- [x] `Vendor` 자체도 3계층(Controller/Service/Repository) 갖춘 독립 도메인으로 완성

**완료 조건**: `Invoice`가 `Vendor`를 참조하는 구조로 동작(완료) / 기존 검색 기능이 `Vendor` 기준으로도 동작(완료) / `Vendor` 자체 CRUD 5개 + 잘못된 `vendorId` 참조 시 404(완료)

### Phase 3 — 모듈 경계 규칙 정리

- [x] 도메인 간 참조 규칙 정리 (직접 참조 허용 범위, 순환 참조 금지)
- [x] `reference/0008` 아키텍처 다이어그램을 두 도메인 구조로 갱신

**모듈 경계 규칙 (2026-09-08 확정)**

- **단방향 참조만 허용**: `invoice` 패키지는 `vendor` 패키지를 참조할 수 있다(`Invoice.vendor` 필드, `InvoiceService`의 `VendorRepository` 조회, `GlobalExceptionHandler`의 `VendorNotFoundException` 처리). 반대로 `vendor` 패키지는 `invoice` 패키지를 참조하지 않는다.
- **이유**: `Invoice`가 `Vendor`를 참조하는 게 도메인 의미상 자연스럽다(인보이스가 거래처를 알아야 하지, 거래처가 인보이스를 알 필요는 없다). 반대 방향 참조를 허용하면 두 패키지가 서로를 알아야 하는 **순환 참조**가 생겨, 어느 한쪽만 따로 이해하거나 테스트하기 어려워진다.
- **검증 방법**: `grep -rn "import com.ham.invoiceapp" src/main/java/com/ham/invoiceapp/vendor/`로 `vendor` 패키지 안에서 `invoice`를 참조하는 줄이 하나도 없는지 확인한다 (2026-09-08 기준 0건, 규칙 준수 확인됨).
- **다음 도메인 추가 시에도 이 규칙 유지**: 새 도메인이 `Invoice`나 `Vendor`를 참조하는 건 괜찮지만, `Invoice`/`Vendor`가 새 도메인을 거꾸로 참조하게 만들지 않는다.

### Phase 4 — 마무리

- [x] README 아키텍처 섹션 갱신
- [x] 월간 회고

**Month 2 완료 조건**
- [x] 패키지가 계층이 아니라 도메인(`invoice/`, `vendor/`) 기준으로 나뉘어 있다
- [x] `Invoice`와 `Vendor`가 연관관계로 연결되어 동작한다
- [x] 왜 도메인 기준 패키지가 계층 기준보다 나은지 설명할 수 있다 (레슨 0023 회고에서 구두 확인)

---

## Month 3 — 테스트 코드 (JUnit 5 + Mockito + MockMvc)

지금까지 5개 CRUD + 검색 + 두 도메인 연관관계까지 전부 `requests.http`로 손으로만 검증해왔다. Month 3는 이걸 자동화된 테스트로 대체한다. 새 의존성 추가는 없다 — `spring-boot-starter-test`(Initializr 생성 시 기본 포함)에 JUnit 5·Mockito·MockMvc·AssertJ가 전부 들어있다.

**순서 원칙**: 로직이 단순한 것 → 복잡한 것, 단위 테스트(Service) → 통합 테스트(Controller) 순으로 간다. 테스트 피라미드(작은 단위 테스트 다수, 큰 통합 테스트 소수) 원칙을 따른다.

### Phase 1 — VendorService 단위 테스트 (JUnit + Mockito 패턴 익히기)

- [ ] `@ExtendWith(MockitoExtension.class)`로 순수 단위 테스트 클래스 작성, `VendorRepository`를 `@Mock`으로 대체
- [ ] `save`/`findAll`/`findById`(정상)/`findById`(존재하지 않는 id → `VendorNotFoundException`) 테스트
- [ ] `update`/`delete`도 동일 패턴으로 (존재/미존재 분기 둘 다)

**완료 조건**: `./mvnw test`로 `VendorService` 테스트 전부 통과 / Repository를 mock으로 대체했기 때문에 실제 DB·Spring 컨텍스트 없이 실행됨을 설명할 수 있다

### Phase 2 — InvoiceService 단위 테스트 (더 복잡한 케이스 적용)

- [ ] `InvoiceRepository`, `VendorRepository` 둘 다 `@Mock`으로 대체
- [ ] `save` — 정상 케이스(`Vendor` 조회 성공) + 실패 케이스(존재하지 않는 `vendorId` → `VendorNotFoundException`)
- [ ] `findAll` — 검색어 없음/있음 두 분기 각각 테스트
- [ ] `toEntity`/`toResponse` 변환이 올바른지(응답의 `vendorName`이 실제 `Vendor.storeName`과 일치하는지) 검증

**완료 조건**: 분기(if/else) 하나당 테스트 케이스가 최소 하나씩 대응된다는 걸 스스로 확인

### Phase 3 — Controller 통합 테스트 (MockMvc)

- [ ] `@SpringBootTest` + `@AutoConfigureMockMvc`(또는 `@WebMvcTest`) 중 어떤 걸 쓸지 차이를 먼저 이해하고 선택
- [ ] `InvoiceController`의 POST(성공/검증 실패 400)·GET(단건/전체)·PUT·DELETE를 MockMvc로 재현
- [ ] 존재하지 않는 id 조회 시 404 + `ErrorResponse` 형태까지 검증

**완료 조건**: `requests.http`로 손으로 하던 시나리오가 전부 `./mvnw test` 한 번으로 자동 검증된다

### Phase 4 — 마무리

- [ ] `VendorController`도 같은 패턴으로 최소한의 MockMvc 테스트 추가
- [ ] README에 "테스트 실행 방법"(`./mvnw test`) 한 줄 추가
- [ ] 월간 회고

**Month 3 완료 조건**
- [ ] Service 계층에 Mockito 기반 단위 테스트가 있다
- [ ] Controller 계층에 MockMvc 기반 통합 테스트가 있다
- [ ] 단위 테스트와 통합 테스트의 차이(무엇을 mock하는가, Spring 컨텍스트를 띄우는가)를 설명할 수 있다
- [ ] Month 4(Angular)부터는 새 기능을 추가할 때 테스트를 같이 작성하는 습관으로 이어간다

---

## 자주 막히는 곳 (누적)

**DB 연결 실패** — 대부분 포트 또는 비밀번호 불일치. 에러 메시지 **마지막 줄부터** 읽는다.

**로컬 실행과 컨테이너 실행 동시 충돌** — `docker-compose.yml`의 `app` 서비스(컨테이너화된 앱)와 로컬 `mvnw`/IntelliJ 실행이 동시에 8080을 쓰려고 하면 `Port 8080 was already in use`가 난다. 로컬에서 devtools 핫리로드로 개발할 때는 `docker compose stop app`으로 컨테이너 쪽만 내리고 `db`는 계속 띄워둔다. 또한 Docker Desktop이 꺼진 상태에서는 `docker compose` 명령 자체가 `dial unix .../docker.sock: no such file or directory`로 실패한다 — Docker Desktop이 켜져 있는지부터 확인.

**`ddl-auto` 설정** — 학습 단계에서는 `update`. `create-drop`은 재시작마다 데이터가 날아간다. 기존 컬럼의 생성 전략 변경(예: AUTO→IDENTITY)은 반영 못 함 — 실무에서는 Flyway/Liquibase 같은 마이그레이션 툴을 쓴다(개념만, 아직 실습 안 함).

**JSON 역직렬화 실패** — Entity/DTO에 기본 생성자가 없으면 발생.

**`@Valid`가 안 먹힘** — Controller의 `@RequestBody` 파라미터에 붙였는지 확인. Service에 붙이면 동작하지 않는다. HTTP 메서드(POST/PUT)가 아니라 파라미터에 실제로 붙어 있는지로만 결정된다.

**예외를 던지는 것과 잡는 것은 별개 단계** — `throw`만 하고 아무도 `catch`(`@ExceptionHandler`)하지 않으면 Spring 기본값인 500으로 처리된다.

**IntelliJ가 `@JoinColumn`/`@Column`에 "Cannot resolve column"이라고 경고함** — 실제 DB에는 컬럼이 있는데도 뜰 수 있다. 이 경고는 실제 DB를 매번 조회하는 게 아니라 IntelliJ Database 툴 창에 연결된 데이터소스가 캐시해둔 스키마 정보를 기준으로 검사하기 때문 — 앱을 재시작해도 이 캐시는 자동 갱신 안 됨. Database 툴 창에서 해당 데이터소스 우클릭 → Refresh로 해결.

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
