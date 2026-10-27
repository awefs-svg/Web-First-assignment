# [다 찾아드립니다!]

## 작성자와 서비스 소개

- 학번 / 이름: [202412937 / 박원후]
- 서비스 목적과 대상 사용자: "다 찾아드립니다!"는 대학 캠퍼스에서 발생한 분실물 정보를 학생들이 공유할 수 있도록 돕는 웹서비스입니다. 학생들은 로그인하지 않고도 분실물 제보 목록과 상세 내용을 조회할 수 있습니다. 로그인한 사용자는 잃어버리거나 발견한 물건의 특징과 위치를 게시글로 등록할 수 있습니다. 분산된 분실물 정보를 한곳에서 확인할 수 있도록 하고, 게시글 수정·삭제는 작성자 본인만 가능하도록 구성했습니다.
- 선택한 기술: [Vue] + [Spring Boot]
- 주요 기능: [공개 게시글 목록,상세 조회 / 계정 로그인과 JWT 발급 / 인증된 게시글 등록 / 작성자 본인 수정 및 삭제 / 인증,권한,입력 오류 처리, SQLite 데이터 저장]

## 보고서와 테스트 증거

- [구현 보고서 PDF](<docs/CRUD 및 로그인 구현 ( 202412937 ).pdf>)
- [테스트 증거 폴더](<docs/CRUD 및 로그인 구현 ( 사진 )/>)


## 프로젝트 구조

```text
.
├── README.md
├── .gitignore
├── frontend-vue/          # 선택한 프론트 소스와 의존성 
|   ├── package.json
|   ├──src/
|      ├──App.vue
|      ├──api.js
|   
├── backend-spring/           # 선택한 백엔드 소스와 의존성 파일
|   ├──pom.xml
|   ├──mvnw.cmd
|   ├──env.example.txt
|   ├──src/
|   |   ├──main/
|   |      ├──java/edu/classroom/board/
|   |      |    ├──AuthSErvice.java  
|   |      |    ├──BoardController.java
|   |      |    ├──DatabaseConfig.java
|   |      |    ├──PostStore.java
|   |      |    ├──Users.java    
|   |      ├──resources/
|   |      |    ├──application.properites
|   |      |    ├──Aschema.sql
|   ├──data/
|       ├──board.sqlite
└── docs/
    ├── CRUD 및 로그인 구현 (202412937, 박원후).pdf
    └── CRUD 및 로그인 구현 ( 사진 )/      # 별도 증거가 있을 때


[실제 제출 폴더 이름에 맞게 수정]

## 실행 환경

| 항목 | 사용 버전 |
|---|---|
| 운영체제 | [windows 11] |
| 프론트 런타임 Node.js | [24.14.1] |
| 백엔드 런타임 | [OpenJDK 21.0.12.1] |
| 프레임워크 | [Vue 3 /Spring Boot 3.5.16] |
| 데이터 저장 | SQLite |

## 설치와 실행

본인 PC와 교수자 PC에서 로컬로 재현할 수 있도록 실제 명령을 적으세요. 설치·빌드·실행·종료를 구분하고 각 명령을 실행할 폴더를 명시합니다. GitHub는 코드와 문서 제출용이며 외부 배포 URL은 필요하지 않습니다.

해당 프로젝트는 로컬 환경에서 실행할 수 있도록 구성했습니다. GitHub 저장소에서 프로젝트를 복제한 뒤 백엔드와 프론트엔드를 각각 설치,빌드,실행할 수 있습니다. 백엔드는 8000번 포트, 프론트엔드 빌드 미리보기 서버는 4173번 포트를 사용합니다. GitHub는 코드와 문서 제출용으로 사용하며 별도의 외부 배포 URL은 제공하지 않습니다.

### 1 저장소 복제

Windows PowerShell에서 다음 명령어를 실행합니다.

git clone https://github.com/awefs-svg/Web-First-assignment.git
cd Web-First-assignment
```

### 2 백엔드 준비와 실행

[선택한 백엔드의 설치·환경설정·실행 명령 작성]

(1)백엔드 폴더 이동 및 Java 버전 확인
: 저장소 최상위 폴더에서 다음 명령어를 실행합니다.

cd backend-spring
java -version

백엔드는 Java 21.0.12.1 환경에서 실행했습니다.

(2)JWT 비밀 키 생성 및 환경변수 설정
:JWT 서명에 필요한 64자리 16진수 비밀 키를 생성합니다.

[ node -e "console.log(require('node:crypto').randomBytes(32).toString('hex'))" ]

출력된 값을 복사한 뒤 현재 PowerShell 터미널의 환경변수로 등록합니다.

[ $env:JWT_SECRET = Read-Host "JWT_SECRET 입력" ]
[ $env:TOKEN_TTL = "90" ]

* JWT_SECRET 입력 요청이 나타나면 앞서 생성한 키를 붙여 넣습니다.
IntelliJ IDEA에서 실행하는 경우에는 Run -> Edit Configurations -> Environment variables에서 JWT_SECRET을 설정합니다.

-환경설정 파일: backend-spring/src/main/resources/application.properties
-환경설정 예시 파일: backend-spring/env.example.txt
-JWT_SECRET 설정: 새로 생성한 64자리 16진수 값을 환경변수에 등록
-TOKEN_TTL 기본값: 90초
-TOKEN_TTL 변경 방법: TOKEN_TTL 환경변수를 변경하거나 application.

* properties의 app.jwt.ttl 기본값을 변경한 뒤 서버를 재시작합니다. 변경된 만료 시간을 확인하려면 새로 로그인하여 JWT를 발급받습니다.
* 실제 JWT 비밀 키는 README와 GitHub에 기록하지 않습니다.

(3)백엔드 빌드
: backend-spring 폴더에서 다음 명령어를 실행합니다.

[ .\mvnw.cmd clean package ]

* Maven이 별도로 설치된 환경에서는 다음 명령어를 사용할 수도 있습니다.
* 실제 Maven 빌드 결과 BUILD SUCCESS가 출력되어 빌드가 정상 완료된 것을 확인했습니다.

(4) 백엔드 실행
: 빌드가 완료되면 backend-spring 폴더에서 다음 명령어를 실행합니다.

[ java -jar target/jwt-board-1.0.0.jar ]

* 개발 과정에서는 IntelliJ IDEA의 Run 기능을 사용했으며, 생성된 JAR 파일을 이용한 실행도 확인했습니다.
* 실행 로그에서 Tomcat started on port 8000이 출력되는 것을 확인했습니다.

- 백엔드 주소: http://127.0.0.1:8000
-정상 실행 확인 URL: http://127.0.0.1:8000/api/health

- 확인 요청: GET /api/health
- 실제 응답: HTTP 200 OK
- 종료 방법: 실행 터미널에서 Ctrl+C 또는 IntelliJ IDEA의 Stop
- 코드 변경 후 재실행: 백엔드를 종료하고 Maven으로 다시 빌드한 후 JAR 파일을 재실행합니다.

### 3 프론트 빌드와 로컬 실행
: 백엔드 서버를 실행한 상태에서 새로운 PowerShell 터미널을 열고, 저장소 최상위 폴더에서 다음 명령어를 실행합니다.

[ cd frontend-vue ]
[ npm ci ] 
[ npm run build ]
[ npm run preview ]

* npm ci를 통해 프로젝트 의존성을 설치하고, npm run build를 통해 프론트엔드 배포용 파일을 생성합니다.
* 실제 빌드 결과 Vite 빌드가 정상 완료된 것을 확인했습니다.
* npm run preview를 실행하면 프론트엔드 빌드 결과를 로컬 서버에서 확인할 수 있습니다.

- 최종 확인 주소: http://127.0.0.1:4173
- 개발 중 실행 명령어: npm run dev
- 개발 서버 기본 주소: http://127.0.0.1:5173

** API 연결 방식과 포트 변경 방법 **
: 프론트엔드는 api.js의 request() 함수에서 /api 경로를 이용해 요청을 전송합니다.
* 4173번 포트의 프론트엔드에서 발생한 /api 요청은 Vite 프록시 설정을 통해 8000번 포트의 Spring Boot 백엔드로 전달됩니다.
* 프록시 연결 대상이나 포트를 변경하려면 프론트엔드의 Vite 설정 파일을 수정하고 서버를 다시 실행해야 합니다.
* Microsoft Edge 개발자 도구의 Network 탭에서 다음 API 요청을 확인했습니다.

- 요청 URL: http://127.0.0.1:4173/api/posts
- 요청 메서드: GET
- 실제 응답: HTTP 200 OK

* 이를 통해 프론트엔드에서 게시글 조회 API에 요청을 전송하고 정상 응답을 수신하는 것을 확인했습니다.

- 종료: 프론트엔드와 백엔드를 실행한 각 터미널에서 Ctrl+C
- 프론트엔드 코드 변경 후: npm run build를 다시 실행한 뒤 npm run preview로 확인
- 백엔드 코드 변경 후: Maven 재빌드 및 Spring Boot 서버 재시작

### 4 테스트 계정과 DB

| 사용자 | 이메일 | 실습용 비밀번호 |
|---|---|---|
| Alice | alice@class.test | Classroom!2026 |
| Bob | bob@class.test | Classroom!2026 |

(1) SQLite 파일 위치와 자동 생성 방식
: SQLite 데이터베이스의 기본 파일 경로는 backend-spring/data/board.sqlite입니다.

application.properties => app.db.file=${DB_FILE:data/board.sqlite}

* 별도의 DB_FILE 환경변수를 설정하지 않고 backend-spring 폴더를 작업 디렉터리로 실행하면 기본 경로를 사용합니다.
* 서버 실행 시 DatabaseConfig.java에서 데이터베이스 폴더를 생성하고 SQLite에 연결합니다. schema.sql의 CREATE TABLE IF NOT EXISTS 구문을 통해 게시글 테이블이 없을 경우 자동으로 생성됩니다.

* 별도의 SQLite 데이터베이스 서버 설치는 필요하지 않습니다.

(2) DB 초기화 방법
: Spring Boot 서버를 종료한 뒤 backend-spring/data/board.sqlite 파일을 삭제하고 서버를 재실행하면 데이터베이스가 초기화됩니다.

* 이때 기존 게시글 데이터가 삭제되므로 초기화가 필요한 경우에만 수행합니다.

(3) 서버 재시작 후 데이터 유지 확인 결과
: T15 테스트에서 Alice 계정으로 게시글을 등록한 뒤 백엔드 서버를 종료하고 재실행했습니다.

* 이후 게시글 목록을 조회하여 서버 재시작 전 등록한 게시글의 제목, 내용 및 작성자 정보가 유지되는 것을 확인했습니다.

증거 위치: 구현 보고서 "테스트 증거 D – T15"

## 구현 완료 범위

| 기능 | 완료 여부 | 주요 파일 또는 함수 |
|---|---|---|
| 공개 목록과 상세 | 완료 | BoardController.java |
| 로그인과 JWT 발급 | 완료 | AuthService.java, api.js |
| JWT 검증과 내 정보 조회 | 완료 | AuthService.java, api.js |
| 글 작성과 작성자 저장 | 완료 | BoardController.java |
| 본인 글 수정·삭제 | 완료 | AuthService.java, api.js |
| 토큰 만료와 로그아웃 | 완료 | AuthService.java |
| 입력 오류와 권한 오류 처리 | 완료 | BoardController.java, AuthService.java, App.vue |


## 테스트 방법과 결과 요약

(1) 실행한 검사 명령과 실행 위치
* 백엔드 폴더(backend-spring)에서 Maven 빌드 및 JAR 실행을 확인했습니다.
[ .\mvnw.cmd clean package ]
[ java -jar target/jwt-board-1.0.0.jar ]

* 프론트엔드 폴더(frontend-vue)에서 의존성 설치 및 빌드 미리보기 실행을 확인했습니다.
[ npm ci ]
[ npm run build ]
[ npm run preview ]

* API 기능 검사는 Microsoft Edge 개발자 도구의 Network·Console 기능과 Postman을 이용하여 수동으로 수행했습니다.

(2) Alice/Bob 권한 구분 확인 결과 => Alice 계정으로 게시글을 작성한 뒤 Bob 계정의 정상 JWT를 이용하여 해당 게시글의 수정과 삭제를 시도했습니다.

* 두 요청 모두 HTTP 403과 FORBIDDEN 오류가 반환되었으며, 기존 게시글의 내용이 변경되거나 삭제되지 않은 것을 확인했습니다.

- 증거 위치: 테스트 증거 B. T07, T08

(3) HTTP 상태와 오류 code 확인

(4) 응답 헤더와 본문 확인
- HTTP 201 Created: 게시글 생성 성공과 Location 응답 헤더 확인
- HTTP 401 Unauthorized: 인증 오류 코드 및 보호 API의 WWW-Authenticate 응답 헤더 확인
- HTTP 204 No Content: 게시글 삭제 성공과 빈 응답 본문 확인

- 관련 증거: 보고서의 HTTP 응답 확인 기록 및 T17

(5) 토큰 없음, 변조, 만료 확인 결과
- 토큰 없음: HTTP 401 / TOKEN_REQUIRED
- 변조된 토큰: HTTP 401 / INVALID_TOKEN
- 만료된 토큰: HTTP 401 / TOKEN_EXPIRED
- 잘못된 로그인 정보: HTTP 401 / INVALID_CREDENTIALS

* 인증되지 않은 요청이나 유효하지 않은 토큰을 이용한 보호 API 요청이 서버에서 거부되는 것을 확인했습니다.

(6) 전체 T01~T17 결과와 증거
* [T01~T17 테스트 결과 및 증거](<docs/CRUD 및 로그인 구현 ( 202412937 ).pdf>) (23페이지부터)

(7) 수동 테스트 재현 절차
1) 백엔드와 프론트엔드 서버를 실행합니다.
2) 로그인하지 않은 상태에서 게시글 목록과 상세 조회를 확인합니다.
3) Alice 계정으로 로그인한 뒤 게시글을 작성하고 수정·삭제합니다.
4) Bob 계정으로 로그인하여 Alice 게시글에 대한 수정·삭제를 시도하고 HTTP 403을 확인합니다.
5) Authorization 헤더 없이 보호 API를 요청하여 HTTP 401을 확인합니다.
6) 변조되거나 만료된 JWT로 보호 API를 요청하여 인증 오류를 확인합니다.
7) 존재하지 않는 게시글 ID로 요청하여 HTTP 404를 확인합니다.
8) 게시글을 등록한 뒤 백엔드를 재시작하여 SQLite 데이터 유지 여부를 확인합니다.

## 핵심 구현 설명

1. 로그인과 JWT 발급
:사용자가 이메일과 비밀번호를 입력하면 api.js의 login() 함수에서 POST /api/auth/login 요청을 전송합니다. BoardController.java의 login()에서 입력값을 검증하고 사용자 정보를 확인한 뒤, AuthService.java의 issueToken()을 통해 JWT를 발급합니다. JWT에는 사용자 ID(sub), 발급자(iss), 대상(aud), 발급 시간(iat), 만료 시간(exp)이 포함되며 HS256 알고리즘으로 서명됩니다.

2. 토큰 보관과 전송
: 로그인에 성공하면 api.js의 session 변수에 JWT와 사용자 정보를 저장합니다. 보호 API 요청 시 authorizationHeaders()에서 Authorization: Bearer <token> 헤더를 생성하여 서버로 전송합니다. 토큰은 브라우저의 JavaScript 메모리에 저장되므로 페이지를 새로고침하면 로그인 상태가 초기화됩니다.

3. 서버 측 JWT 검증
: AuthService.java의 currentUserId()에서 Authorization 헤더의 Bearer 토큰을 추출하고 JWT 서명과 알고리즘을 검증합니다. 이후 발급자, 대상, 발급 시간, 만료 시간, 사용자 ID 및 실제 사용자 존재 여부를 확인합니다. 토큰이 없거나 유효하지 않은 경우 HTTP 401을 반환하며, 만료된 경우 TOKEN_EXPIRED 오류를 반환합니다.

4. 게시글 작성자 권한 검사
: 게시글 작성 시 BoardController.java의 create()에서 서버가 검증한 JWT의 사용자 ID를 작성자로 저장합니다. 클라이언트가 authorId를 임의로 전달하더라도 fields()에서 허용되지 않은 JSON 필드로 판단하여 HTTP 400 BAD_INPUT을 반환합니다. 게시글 수정 및 삭제 시에는 AuthService.java의 assertOwner()를 통해 로그인한 사용자와 게시글 작성자가 일치하는지 확인합니다.

작성자가 다르면 HTTP 403 FORBIDDEN을 반환하여 요청을 거부합니다.

5. 입력값 및 오류 처리
: BoardController.java의 fields()에서 제목과 내용이 문자열인지 확인하고, 공백만 입력되었거나 최대 길이를 초과하는 경우 HTTP 400 BAD_INPUT을 반환하도록 구현했습니다. 제목은 최대 100자, 내용은 최대 5000자로 제한했습니다. 존재하지 않는 게시글은 existing()에서 HTTP 404 NOT_FOUND를 반환하며, 잘못된 게시글 ID는 postId()에서 HTTP 400 BAD_INPUT을 반환합니다.

6. 토큰 만료와 로그아웃 
: App.vue의 logout()에서 api.js의 clearSession()을 호출하여 브라우저 메모리에 저장된 JWT와 사용자 정보를 초기화합니다. 토큰이 만료된 상태에서 보호 API를 요청하면 서버에서 HTTP 401 TOKEN_EXPIRED를 반환합니다. 프론트엔드의 request() 함수는 HTTP 401 응답을 받으면 clearSession()을 호출하고, App.vue에서는 오류 코드에 따라 사용자에게 적절한 메시지를 표시합니다.

## AI 활용 요약과 참고 자료

- AI 사용 여부: 사용
- 도구와 사용 목적: ChatGPT를 활용하여 JWT 인증 구조 이해, Java 및 JavaScript 코드 이해, 테스트 절차 진행
- 반영 위치와 직접 수정·검증한 내용: AuthService.java, BoardController.java, api.js 등 JWT 인증과 게시글 처리 관련 코드를 검토 및 수정했습니다. 이후 Microsoft Edge 개발자 도구와 Postman을 통해 실제 API 응답과 기능 동작을 확인했습니다.
- 상세 활용 기록: [AI 상세 활용 기록](<docs/CRUD 및 로그인 구현 ( 202412937 ).pdf>) (57페이지부터)

## 제출 확인

- [v] README의 안내 문구를 실제 내용으로 교체했다.
- [v] 새 폴더에 clone하여 README만으로 설치·환경설정·빌드·로컬 실행했다.
- [v] 4173번 빌드 화면에서 API 연결과 전체 필수 기능을 확인했다.
- [v] GitHub에서 보고서와 증거 링크가 열린다.
- [v] 교수자가 저장소를 열람·복제할 수 있다.
- [v] 비밀 키·실제 토큰·DB·node_modules·.venv·target을 커밋하지 않았다.
- [v] 최종 push 후 저장소 URL과 제출 기준 커밋 SHA를 LMS에 제출했다.
