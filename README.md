# 📚 BookRating API

사용자가 책을 등록하고, 각 책에 대해 별점을 매기고 리뷰를 남길 수 있는 REST API를 제공합니다

## 🚀 주요 기능

1. **책 등록**: 사용자가 새로운 책을 애플리케이션에 등록할 수 있습니다.
2. **별점 및 리뷰 작성**: 등록된 책에 대해 사용자가 별점을 매기고, 리뷰를 작성할 수 있습니다.
3. **리뷰 및 별점 조회**: 여러 사용자가 등록한 책에 대한 별점과 리뷰를 종합적으로 확인할 수 있습니다.

## 🛠 기술 스택

- **JDK**: Version 17
- **프레임워크**: Spring Boot 3.3.2
- **데이터베이스**: MySQL
- **배포**: AWS EC2

## 📄 API 명세

- (🔒 인증 필요) 표시가 있는 엔드포인트는 인증된 사용자만 접근할 수 있습니다. 이 API는 JWT 토큰을 통해 인증을 수행합니다.
- API의 상세 설명은 [링크](https://github.com/yyujjin/book-rating-backend/wiki/Book_rating-APIs)를 통해 확인할 수 있습니다.

> **/loginInfo**
> 
- `POST` : 로그인된 유저의 정보를 받아옵니다.

> **/tags**
> 
- `GET` : 책 등록 시 태그 정보를 받아오기 위해 호출합니다.

> **/books**
> 
- `GET` : 모든 책의 목록을 조회합니다.
- `POST` : 새로운 책을 등록합니다. (🔒 인증 필요)

> **/books/{id}**
> 
- `PATCH` : 특정 책 정보의 제목을 수정합니다. (🔒 인증 필요)
- `DELETE` : 특정 책을 삭제합니다. (🔒 인증 필요)

> **/books/{bookId}/reviews**
> 
- `GET` : 특정 책에 대한 리뷰 목록을 조회합니다.
- `POST` : 책에 대한 리뷰를 등록합니다. (🔒 인증 필요)

> **/books/{bookId}/reviews/{reviewId}**
> 
- `PATCH` : 특정 리뷰를 수정합니다. (🔒 인증 필요)
- `DELETE` : 특정 리뷰를 삭제합니다. (🔒 인증 필요)

## 🔐 인증

> **인증에 사용된 기술 및 방식**
> 
- **Spring Security**: 사용자 인증과 권한 관리를 위해 Spring Security를 사용하여, 안전하고 일관된 인증 구조를 제공합니다.
- **Google OAuth2**: 사용자의 구글 계정을 통해 인증을 수행하고, 서버는 이를 바탕으로 JWT 토큰을 생성합니다.
- **JWT (JSON Web Token)**: 인증 완료 후 서버는 JWT를 발급하고, 클라이언트는 이 토큰을 통해 각 요청에서 인증을 받습니다.

![IMG_BD018AA130F6-1](https://github.com/user-attachments/assets/6ea6cb80-c414-4c12-9ca1-15d8b59eea86)


> **단계별 인증 과정**
> 
1. **사용자 요청**: 사용자가 'Google 로그인' 버튼을 클릭하여 인증을 요청합니다.
2. **OAuth2 인증**: 구글이 사용자의 동의를 받고, 인증 코드를 서버로 전달합니다.
3. **JWT 생성**: 서버는 받은 인증 코드를 통해 JWT를 생성하고, 이를 클라이언트에 전달합니다.
4. **JWT 인증 요청**: 이후 클라이언트는 JWT를 포함해 API 요청을 보내며, 서버는 이를 확인하고 요청을 처리합니다.

## 📦 배포 흐름도

이 프로젝트는 Docker를 사용하여 배포되며, 이미지를 빌드한 후 서버 인스턴스에서 이미지를 받아 실행합니다.

![IMG_97B5B59A4FED-1](https://github.com/user-attachments/assets/c139fb0c-75c4-4031-a54e-83d01bc9f368)