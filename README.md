# Backend Application

## 목차

1. 개요
2. 포트원(결제 API, 구 아임포트) 결제 건
3. 적용 기술
4. Application Structure
5. Spring Rest Docs(REST API 개발 문서)

## 1. 개요 

안전관리 솔루션을 제공하는 플랫폼 `Backend HTTP API github` 입니다.
[Fontend github](https://github.com/SafeKingMall/frontend)가 궁금하신 분은 여기를 클릭해주세요.

**참고**
`운영 github`는 보안 상의 이유로 별도 존재

<img width="1855" alt="image" src="https://user-images.githubusercontent.com/42672362/233237853-b355829f-b458-414c-90bf-fdf80453d9e5.png">
<br>
<img width="1847" alt="image" src="https://user-images.githubusercontent.com/42672362/233238263-28744b89-b4d3-448d-99c6-eed2029dfca2.png">


[도메인] 

- [안전왕몰](https://safekingmall.com)

[시연영상]

- [결제 시연 영상](https://www.youtube.com/watch?v=X25CrNLl5fg)
- [사용 시연 영상](https://www.youtube.com/watch?v=3gPJGSg73Po)

## 2. ERD

<img width="1440" alt="image" src="https://user-images.githubusercontent.com/108928206/224705604-2b64c0cb-2762-4ede-9887-9a03ef08ddd7.png">

[데이터베이스](https://www.erdcloud.com/d/xZqG8nnoHb6RaSoRK)

## 3. 포트원(결제 API, 구 아임포트) 결제 건
### 포트원 결제 로직

<img width="730" alt="image" src="https://user-images.githubusercontent.com/42672362/233083505-a38b8962-2cb9-4944-b904-a98ed6304249.png">

### 안전왕몰 주문/결제 로직
<img width="802" alt="image" src="https://user-images.githubusercontent.com/42672362/233083436-d316b9f1-b364-43b4-a02f-b6372b5cf3db.png">



## 4. 적용 기술

| Back-End | Java 11, Spring Framework 2.7.7, Spring boot, RESTFUL API, QueryDsl 5.0.0, Spring Data Jpa 2.7.6, JPQL, Spring Security 5.7.6, Spring Batch  |
| --- | --- |
| Server | Apache, Apache Tomcat 9.0.7, cafe24 가상서버 |
| DB | MySQL, MariaDB 3.0.6, H2 2.1.214 |
| Test | junit5, AssertJ Library |
| Build 자동화 도구 | Gradle 7.6 |
| Tools | IntelliJ IDEA, Post Man, putty |
| OS | Centos |
| DevOps | AWS EC2, RDS, Route53 |


## 5. Application Structure

<img width="773" alt="image" src="https://user-images.githubusercontent.com/108928206/223898062-3d750be1-25b2-49c0-b56c-1fc3dcbe013b.png">

1. **Domain 과 Web 의 분리**
    - domain 계층은 web 계층을 의존하지 않도록 한다.
    - domain 과 web의 패키지 구조를 분리시켜 의존성을 제거하여 
    web 관련 로직 수정 시에 비즈니스 로직에 영향을 미치지 않도록 한다.
2. **CQS(Command-Query Separation) 적용**
    - command 와 query 를 분리해 동시에 쓰기와 읽기가 일어나지 않도록 하여 성능을 최적화한다.
    - cqs 적용으로 유지보수성을 높인다.

1. **OSIV 종료**
    - osiv 를 종료하여 모든 지연 로딩을 서비스단 이전에서 처리한다.
    - 따라서 controller 단에서는 지연 로딩이 일어나지 않도록 설계한다.
    

## 6. Spring Rest Docs(REST API 개발 문서)

[장바구니]

[https://safekingmall.com/docs/cart.html](https://safekingmall.com/docs/cart.html)

[장바구니_에러]

[https://safekingmall.com/docs/cart_error.html](https://safekingmall.com/docs/cart_error.html)

[JWT토큰]

[https://safekingmall.com/docs/jwtToken.html](https://safekingmall.com/docs/jwtToken.html)

[로그인]

[https://safekingmall.com/docs/login.html](https://safekingmall.com/docs/login.html)

[Member 기본 api]

[https://safekingmall.com/docs/member_info.html](https://safekingmall.com/docs/member_info.html)

[회원 가입 api]

[https://safekingmall.com/docs/member_signUp.html](https://safekingmall.com/docs/member_signUp.html)

[일반 회원 휴면 계정 복구]

[https://safekingmall.com/docs/member_signUp_dormant.html](https://safekingmall.com/docs/member_signUp_dormant.html)

[소셜 계정 휴면 복구]

[https://safekingmall.com/docs/member_signUp_dormant_social.html](https://safekingmall.com/docs/member_signUp_dormant_social.html)

[문자 인증]

[https://safekingmall.com/docs/smscontroller.html](https://safekingmall.com/docs/smscontroller.html)

[관리자]

[https://safekingmall.com/docs/admin.html](https://safekingmall.com/docs/admin.html)

## API Doc
노션을 이용하여 [API 문서](https://first-parakeet-792.notion.site/SafeKing-API-ab0719f0e30645f69a9da6e8ee1db209)를 작성했습니다.
