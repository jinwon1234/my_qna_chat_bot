# ❓ MyQnA — 나만의 QnA 챗봇 플랫폼  ( 우아한테크코스 8기 프리코스 오픈미션 )
> 자신을 소개하는 PDF 업로드만 하면 나만의 QnA 챗봇이 자동으로 생성되는 서비스

**🔗 서비스 URL**: https://myqna.jinwon.click  
**🔗 간단 체험 URL(우테코 자소서 기반 QnA 챗봇)** https://myqna.jinwon.click/link/36c06871-bd21-44b8-81b2-baf7e1b653d9
<br>
**🔗 MyQnA 제작을 위한 학습 일지** https://www.notion.so/MyQnA-2b53af6c79c8806aad65e4eb6ad72658?source=copy_link

`MyQnA`는 사용자가 업로드한 PDF 문서를 기반으로 **나만의 QnA 챗봇**을 생성해주는 서비스입니다.  


---

## 제작 이유
<img width="2862" height="996" alt="image" src="https://github.com/user-attachments/assets/e9de7b51-4a96-4638-8f25-2900f9fb0b00" />

일명 인스타그램의 `무물보(무엇이든 물어보세요)` 기능은 MZ 세대에서 큰 유행을 타고 있는 소통 방식입니다.
간단히 말해, 스토리를 올린 사람이 질문을 열어두면 스토리를 본 사람들이 자유롭게 질문을 보내는 것입니다.

그러나 인스타그램 무물보에는 불편한점이 분명히 존재합니다.
1. 유명인·크리에이터는 질문량이 너무 많아 모든 질문에 답변하기 어렵다.
2. 익명성이 보장되지 않아 질문을 하기가 어렵다.
3. 자신에 대한 기본 정보나 소개를 반복해서 설명해야 하는 불편함이 있다.

이러한 문제들을 해결하고자 자신을 소개하는 PDF 파일 하나로 자신을 소개할 수 있는 `MyQnA`를 제작하게 되었습니다.

---

## 기능 소개

### 1. 개인 QnA 챗봇 생성
<img width="1704" height="774" alt="image" src="https://github.com/user-attachments/assets/6d618766-96ab-4049-b145-dffd123558d4" />

- 이름 입력을 PDF 업로드하면 나만의 QnA 챗봇 링크가 생성됩니다.
- 예: `/link/{userId}` → 다른 사람과 공유 가능 (userId는 UUID로 생성)


### 2. PDF Embedding (문서 임베딩)
<img width="763" height="435" alt="Frame 7" src="https://github.com/user-attachments/assets/8dc2a03e-df3c-441c-872a-7e62a6082cb6" />

- 업로드된 `PDF` 파일을 텍스트를 기준으로 토큰 기준으로 `chunking`합니다.
- `chunk`들에 `metadata(owner=uuid)` 추가해, 나에 대한 정보만 QnA 챗봇이 답변을 하도록합니다.
- `spring ai`가 `open ai`의 `embedding model`을 통해 청크 데이터들을 `vector db`에 저장할 수 있도록 벡터화합니다.
- `PostgreSQL` + `pgvector`를 이용해 벡터 저장

### 3. 링크 생성
<img width="1568" height="508" alt="image" src="https://github.com/user-attachments/assets/63dd3d35-1fa3-4ea2-a0e7-81d3961c15de" />

- 링크 생성이 완료되고 해당 링크를 공유하고, `QnA 시작하기` 버튼을 누르면 `QnA`를 위한 채팅방이 생성됩니다.
- 채팅방은 uuid를 통해 고유한 아이디를 갖습니다.

### 4. QnA 채팅
<img width="1520" height="1446" alt="image" src="https://github.com/user-attachments/assets/608b987c-df22-4977-9f02-783f3da69679" />

- 질문이 들어오면 임베딩된 사용자 정보와 비교하여 코사인 유사도 0.3 이상, 최대 6개의 관련 청크를 검색해 답변에 활용합니다.
- `멀티턴(Multi-turn)` 대화를 지원하여 이전 메시지의 문맥을 고려해 자연스러운 이어말하기가 가능합니다.
- 모든 채팅 내역은 채팅방의 ID를 기준으로 저장되며, 각 질문자마다 독립적인 대화방 히스토리가 유지됩니다.

---


## 파일 구조
```plaintext
└── src/
    ├── main/
    │   ├── resources/
    │   │   ├── application.properties
    │   │   └── templates/
    │   │       ├── link.mustache 
    │   │       ├── chat.mustache
    │   │       ├── home.mustache
    │   │       └── error/
    │   │           └── error-page.mustache
    │   └── java/
    │       └── wowa/
    │           └── myqna/
    │               ├── MyqnaApplication.java
    │               ├── global/
    │               │   ├── GlobalExceptionHandler.java
    │               │   ├── exception/
    │               │   │   └── ApplicationCustomException.java
    │               │   └── message/
    │               │       └── ErrorMessage.java
    │               ├── config/
    │               │   ├── DocumentReaderConfig.java
    │               │   └── AIConfig.java
    │               ├── user/
    │               │   ├── domain/
    │               │   │   └── UserEntity.java
    │               │   ├── service/
    │               │   │   └── UserLowService.java
    │               │   └── repository/
    │               │       └── UserRepository.java
    │               ├── embedding/
    │               │   ├── service/
    │               │   │   └── EmbeddingService.java
    │               │   ├── view/
    │               │   │   └── EmbeddingViewController.java
    │               │   ├── controller/
    │               │   │   └── EmbeddingController.java
    │               │   └── dto/
    │               │       ├── EmbeddingRequestDto.java
    │               │       └── EmbeddingResponseDto.java
    │               └── chat/
    │                   ├── domain/
    │                   │   └── ChatMessage.java
    │                   ├── service/
    │                   │   ├── ChatService.java
    │                   │   └── ChatMessageLowService.java
    │                   ├── view/
    │                   │   └── ChatViewController.java
    │                   ├── controller/
    │                   │   └── ChatController.java
    │                   ├── repository/
    │                   │   └── ChatMessageRepository.java
    │                   └── dto/
    │                       └── ChatRequestDto.java
    └── test/
        └── java/
            └── wowa/
                └── myqna/
                    └── MyqnaApplicationTests.java
```

---
