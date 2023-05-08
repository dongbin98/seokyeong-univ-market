# SKU_Market   
SeoKyeong University Market Android Native Application   
서경대학교 학생들을 위한 중고거래 모바일 애플리케이션입니다.  

`MVVM` `dataviewbinding` `Retrofit` `Coroutine` `RxJava`

## 1. Co-Development Environment   
### 1. 1 Environments
- Windows 10
- Android Studio / Kotlin / Java
- Firebase
- Github 공동개발

### 1. 2 Role
- `염동빈`
  - 서경대학교 학생인증, 회원가입 및 로그인 구현 `Firebase Authentication` `Retrofit` `Coroutine`
  - 실시간 1:1 채팅방 구현 `Firebase RealtimeDatabase`
  - 중고거래 게시판 구현 `Firebase RealtimeDatabase + Storage` `Coroutine`
  
- `양승협`
  - 중고거래 게시판 구현 `Firebase RealtimeDatabase + Storage`
  
## 2. Project Architecture   
```bash
├── adapters
│   ├── ChatAdapter.kt
│   ├── ChatListAdapter.kt
│   ├── FrameAdapter.kt
│   ├── ImageAdapter.kt
│   └── PostAdapter.kt
├── model
│   ├── Chat.kt
│   ├── ChatList.kt
│   ├── ChatRoom.kt
│   ├── ChatUser.kt
│   ├── Post.kt
│   ├── PostList.kt
│   ├── SkuAuth.kt
│   ├── SkuAuthResponse.kt
│   ├── User.kt
│   └── UserResponse.kt
├── SkuAuthApi.kt
├── SkuAuthProvider.kt
├──base
│   ├── BaseActivity.kt
│   └── BaseFragment.kt
├── repository
│   └── FirebaseRepository.kt
├── rx
│   └── AutoClearedDisposable.kt
├── ui
│   ├── chat
│   │   ├── ChatActivity.kt
│   │   ├── ChatListFragment.kt
│   │   ├── ChatListViewModel.kt
│   │   ├── ChatRoomOutDialog.kt
│   │   └── ChatViewModel.kt
│   ├── login
│   │   ├── LoginActivity.kt
│   │   └── LoginViewModel.kt
│   ├── main
│   │   ├── MainActivity.kt
│   │   └── MainViewModel.kt
│   ├── mypage
│   │   ├── MyPageFragment.kt
│   │   └── MyPageViewModel.kt
│   ├── post
│   │   ├── PostDetailActivity.kt
│   │   ├── PostDetailViewModel.kt
│   │   ├── PostListFragment.kt
│   │   ├── PostListViewModel.kt
│   │   ├── UplaodPostActivity.kt
│   │   └── UploadPostViewModel.kt
│   └── register
│       ├── RegisterActivity.kt
│       ├── RegisterViewModel.kt
│       └── RegisterViewModelFactory.kt
├──util
│   ├── DateFormat.kt
│   ├── LinearLayoutManagerWrapper.java
│   ├── Resource.kt
│   └── SafeCall.kt
```
## 3. Firebase   
### 3.1 Realtime Database
```bash
├── ChatRoom
│   └── chatRooms
│       └── chatRoomId
│           ├── messages
│           │   └── messageId
│           │       ├── uid
│           │       ├── name
│           │       ├── time
│           │       ├── message
│           │       └── imageUrl
│           └── users
│               └── userId1
│                   ├── join
│                   └── time
│               └── userId2
│                   ├── join
│                   └── time
├── Post
│   └── posts
│       └── postId
│           ├── uid
│           ├── title
│           ├── time
│           ├── content
│           ├── price
│           └── images
│               └── imageUrl(List)
└── User
    └── users
        └── userId
            ├── uid
            ├── name
            └── profileImage
```
### 3.2 Storage
```bash
├── ChatRoom
│   └── chatRoomId
├── Post
│   └── postId
└── profile
    └── userId
```

## 4. Result   
- 로그인 및 프로필 편집
<p align="left">
  <img src="https://user-images.githubusercontent.com/77912766/236757305-df787f5d-1c0a-46de-8342-59bdade79608.gif"/>
</p>

- 게시글 CRUD (갱신 제외)
<p align="left">
  <img src="https://user-images.githubusercontent.com/77912766/236757349-37ea3b4a-2b71-414f-a4c8-82ade862bb69.gif"/>
</p>

- 채팅
<p align="left">
  <img src="https://user-images.githubusercontent.com/77912766/236757392-3023ae6f-4e90-4019-908d-d25309b8f9e0.gif"/>
</p>

## 5. 향후 계획   
- BottomNavigation을 이용한 FragmentManager 처리 → AAC Navigation (완료)   
- 회원가입 시 Retrofit Callback 처리 → Retrofit + RxJava (완료)   
- 게시글 사진 여러개 등록 (완료)   
- 게시글을 통해 대화 요청 (완료)   
- 게시글 CRUD 처리 (Update 제외 완료)
