# SKU_Market   
SeoKyeong University Market Android Native Application   
서경대학교 학생들을 위한 중고거래 모바일 애플리케이션입니다.  
`MVVM` `databinding` `viewbinding` `retrofit`

## 1. Co-Development Environment  
### 1. 1 Environments
- Windows 10
- Android Studio / Kotlin / Java
- Firebase
- Github 공동개발

### 1. 2 Role
- `염동빈`
  - 서경대학교 학생인증 구현 완료 (Retrofit, Coroutine)
  - 로그인 기능 구현 완료(Firebase Authentication)
  - 실시간 1:1 채팅방 구현 완료(채팅 읽음처리 제외, Firebase Realtimebase)
  
- `양승협`
  - 중고거래 게시판 구현(Firebase Realtimebase)
  
### 2. Result
`게시글(임시) 및 채팅
<p align="left">
  <image src="https://user-images.githubusercontent.com/77912766/228895611-86339746-9482-4f82-9a8e-4994d46c50fe.gif"/>
  <image src="https://user-images.githubusercontent.com/77912766/228895643-c50a6432-f8f4-4b2a-a54d-501586667622.gif"/>
</p>

### 3. 향후 계획
- BottomNavigation을 이용한 FragmentManager 처리 → AAC Navigation 처리(완료)
- 회원가입 시 Retrofit Callback 처리 → Retrofit + RxJava 처리 예정
- Firebase Realtime Database Callback 처리 → Firebase + Retrofit 처리 예정
