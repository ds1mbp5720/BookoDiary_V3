# BookDiary_v3 (Compose · MVI · Clean Architecture · Paging3)

## 1. 프로젝트 소개

이 프로젝트는 Jetpack Compose, **Clean Architecture + MVI 패턴**을 적용하여 개발하였으며, *
*Paging3**를 통해 리스트 데이터를 로드/표시합니다.
또한 **리스트 아이템 클릭 → 상세 화면 이동** 흐름을 Compose Navigation으로 구현했습니다.
각 레이어별 테스트코드를 통한 테스트를 진행하였습니다.

* compileSdk = 35
* minSdk = 33
---

## 2. 주요 기능

* **Home: 4개의 카테고리별 가로 리스트**
    * 각 카테고리별 화면 이동시 Paging3 기반 무한 스크롤
    * LoadState(refresh/append) 기반 로딩/에러 UI 처리
* **Search: Paging 리스트**
    * Paging3 기반 무한 스크롤
    * Datastore를 통한 검색 기록 저장 및 삭제 관리
    * LoadState(refresh/append) 기반 로딩/에러 UI 처리
* **Record: 저장한 책**
    * 저장한 책 기록에 대한 RoomDB를 리스트로 제공
    * 내부 검색 및 삭제 등 관리
* **상세 화면 이동**
    * 리스트 아이템 클릭 시 ViewModel에서 **Effect(Navigate)** 발생
    * UI에서 Effect 수신 후 Navigation 수행

---

## 3. 기술 스택

* Language: Kotlin
* UI: Jetpack Compose, [Image: Coil]
* Architecture: Clean Architecture
* Pattern: MVI (State / Event / Effect)
* Async: Kotlin Coroutines / Flow
* Paging: Paging3 (paging-compose)
* DI: Hilt
* Navigation: Jetpack Compose Navigation
* DataBase: Room, Datasorce
* Network: Retrofit / OkHttp

---

## 4. 폴더/레이어 구조

```
app/
  BookDiaryApplication.kt
  MainActivity.kt

core/
  database/
    room/
  network/
    di/
    error/
    util/

data/
  book/
    di/
    repository_impl/
    local/
       dao/
       entity/
       database/
    remote/
       datasource/
       dto/
       paging
  history/
  offstore/
    model/
    repository/
    usecase/

domain/
  book/
    model/
    repository/
    usecase/
  history/
    model/
    repository/
    usecase/
  offstore/
    model/
    repository/
    usecase/

presentation/
  component/
  dialog/
  calendar/
  book/
  main/
  navigation/
  home/
  serach/
  detail/
  theme/
  setting/
  record/
  BaseViewModel.kt
  Utils.kt
```

---

## 5. 아키텍처 설명 (CleanArchitecture + MVI)

### 5.1 MVI 구성요소 (Compose, ViewModel, State, Event, Effect)

* **Event(Intent)**: UI에서 발생한 사용자 의도(검색어 변경, 아이템 클릭 등)
* **State**: 화면을 그리기 위한 단일 상태(immutable)
* **Effect**: 일회성 동작(네비게이션, 토스트, 외부 연동 등)

### 5.2 Paging3

* State로 보관시 불필요한 recomposition/재생성이 발생 고려 ViewModel은 `Flow<PagingData<T>>`를 노출하고, UI에서 `collectAsLazyPagingItems()`로 수신하는 구조를 사용

### 5.3 data Layer

* **database**: 찜 관리를 위한 내부 저장 Room
* **network**: Aladin Rest Api 통신을 통한 책 정보 호출
* **book**: 책 정보 전체를 관리 및 연결하는 역할로 찜(Room)과 Api 통신 결과를 종합하여 domain Layer에 전달

---

## 6. Navigation 설계 (NavHost)

Home 화면을 시작점으로 BottomBar를 통한 각 화면 전환
책 클릭시 상세화면 이동

---

## 7. 주요 화면

* Home: 
* Search:
* Record:
* Detail: 선택된 아이템 상세 정보 표시
* Setting: 

---

## 9. 구현 포인트/의사결정

* ViewModel은 UI 프레임워크(NavController 등)를 모르도록 설계하여 테스트 용이성 확보
* BaseViewModel 을 기반으로하여 State, Event, Effect 처리를 명확히 분리하여 데이터 흐름을 단일화했습니다. (Effect의 경우 Channel 을 통해 중복 발생 안하도록 처리)
* LoadState 기반 로딩/에러 처리로 사용자 경험 개선
* NetworkError를 통해 Api 통신에 대한 Error 처리 케이스별 관리
* route 문자열 오타 방지를 위해 Route를 sealed class로 관리
* coil사용 이미 표시 및 placeHolder, error 표시 분류

---
