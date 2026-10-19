# invoice-app

인보이스·영수증 관리 시스템. **학습 목적 프로젝트**이자 포트폴리오.

`current_phase: Month 3 · Phase 2(InvoiceService 단위 테스트) 진행 중 — save·findAll 완료, findById부터`

## 이 저장소에서의 규칙

1. **완성 코드를 통째로 주지 말 것.** 힌트 → 방향 → 부분 코드 순서. 명시적 요청 시에만 예외.
2. **"왜"를 함께 설명할 것.** 개발자는 자기 코드를 설명할 수 있어야 한다는 게 이 프로젝트의 목표다.
3. **에러는 함께 읽을 것.** 답을 주기 전에 에러 메시지부터.
4. **현재 Phase 범위를 벗어난 기술을 제안하지 말 것.** 전체 커리큘럼과 진행 상태는 `docs/CURRICULUM.md` 참조 — 요일/주차가 아니라 Phase 단위 체크리스트다.
5. **한국어로 답할 것.**

## 범위 밖 (제안 금지)

Redis, Kafka, Kubernetes, MSA 분리, React, LangChain, RAG, GraphQL, 새 언어

## 스택

Java 21 / Spring Boot / PostgreSQL / Docker
(Angular는 W07부터, FastAPI는 W17부터)

## 구조

```
src/               Spring Boot
docs/CURRICULUM.md 전체 커리큘럼 (Phase 단위 체크리스트) — 필요할 때만 읽을 것
docs/logs/         세션별 실제 진행 기록 (날짜 기준)
```

## 커밋

- 매일 커밋. 기능 하나 = 커밋 하나.
- 금·일은 최소 커밋(10~15분 실제 개선) 허용.
- 빈 커밋·공백 커밋 금지.
