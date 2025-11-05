# 📦 리뷰 사이트 - Review Web App

> 다양한 기준으로 제품을 평가하고, 한눈에 정보를 확인할 수 있는 사용자 중심 리뷰 사이트

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-green)
![MariaDB](https://img.shields.io/badge/MariaDB-005c84?logo=mariadb)
![Redis](https://img.shields.io/badge/Redis-DC382D?logo=redis)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?logo=thymeleaf)

---

## 📖 프로젝트 개요

- **기간**: 2024.12.16 ~ 2025.04.18
- **개인 프로젝트**
- **GitHub**: [https://github.com/jeonminhun/review](https://github.com/jeonminhun/review)

### 🧩 프로젝트 목적

제품을 구매할 때마다 흩어진 리뷰 정보를 찾는 것이 불편했고, ‘가성비’, ‘품질’, ‘디자인’, ‘내구성’ 등 다양한 기준으로 제품을 정리해 보고자 본 프로젝트를 진행하게 되었습니다.  
사용자는 별점과 후기 작성을 통해 정보를 제공하고, 데이터 차트를 통해 다른 사용자들이 남긴 리뷰 정보를 직관적으로 파악할 수 있습니다.

---

## ⚙️ 기술 스택

| 분류       | 기술                                                                 |
|------------|----------------------------------------------------------------------|
| Frontend   | JavaScript, Thymeleaf                                                |
| Backend    | Java, Spring Boot, JPA, Spring Security                              |
| DB         | MariaDB, Redis                                                       |
| 배포       | ~~Railway~~, ~~Google Cloud Run~~ *(현재 비공개)*                     |

---

## 🚀 주요 기능

- ✅ JWT 기반 인증/인가 구현
  - Access Token + Refresh Token을 통한 보안 강화
  - SSR 환경에서 Cookie를 이용한 토큰 관리
- ✅ 제품 리뷰/별점 기능
  - 다양한 평가 기준 (가성비, 품질 등)을 기반으로 별점 등록
- ✅ 제품별 데이터 차트 시각화
  - 별점 분포, 개수, 평균 등을 시각화하여 제공
- ✅ 사용자 권한별 기능 제공
  - 일반 사용자 / 관리자 권한 구분 및 접근 제어

---

## 🧪 트러블 슈팅

### 1. 서버 캐시로 인한 이미지 변경 반영 문제
- **문제**: 프로필 이미지 변경 시 변경 사항이 즉시 반영되지 않음  
- **원인**: 정적 리소스(static/) 경로 캐싱  
- **해결**: `WebConfig`에서 캐시 비활성화 설정 추가  
- **결과**: 즉시 이미지 갱신 가능

---

### 2. 유저 권한 검증 실패
- **문제**: 권한 관련 기능 사용 시 Spring Security 인증 실패  
- **원인**: DB에는 `int`로 저장된 권한 정보와 Spring Security의 `GrantedAuthority`가 매칭되지 않음  
- **해결**: `enum`으로 권한 구조 정의 + 변환 메서드 추가 + `UserDetailsService` 재정의  
- **결과**: 역할 기반 접근 제어 정상 작동

---

### 3. SSR 환경에서의 JWT 토큰 처리
- **문제**: 로컬스토리지를 사용할 수 없는 SSR 방식에서의 JWT 관리  
- **해결**: `Cookie` 기반 JWT 토큰 저장 및 인증 구현  
- **결과**: SSR 환경에서도 안정적인 로그인 기능 구현

---

### 4. 배포 후 이미지 경로 인식 오류
- **문제**: 배포 환경에서 이미지가 정상 출력되지 않음  
- **원인**: 서버 내 이미지 저장 경로가 중복 또는 미정의  
- **해결**: 이미지 저장 디렉토리 통일  
- **결과**: 배포 환경에서도 이미지 정상 출력



