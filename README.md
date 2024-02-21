# 💘 직팅

## 🎙️ 서비스 소개

**직팅**은 일대일 만남은 부담스럽지만 다양한 사람과의 만남을 가지고 싶은 직장인들을 위해 다대다 미팅을 주선합니다.

<img width="958" alt="스크린샷 2024-02-21 오전 12 54 29" src="https://github.com/SWM-Cupid/jikting-backend/assets/62989828/e234847c-aca2-4e57-95b6-103027c9b44b">

[👉🏻 서비스 바로가기](https://jikting.com/)

## 📌 주요 기능

|      기능       | 내용                                                                                                                                                                                                                    |
|:-------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|    회원 프로필     | **사용자가 등록한 프로필 정보 및 프로필 사진을 통해 타 사용자에게 정보를 제공합니다.**<br><br> 직장인 300명을 대상으로 실시한 자체 설문 내용을 바탕으로, 미팅 시 사용자가 알고 싶어하는 정보를 프로필 정보로 입력받습니다. 특히 높은 비율을 차지한 프로필 사진을 검증하기 위해 AWS Rekognition을 이용해 얼굴 유무를 파악하여 악성 사용자를 1차 방어합니다. |
|     회사 인증     | **회사 이메일을 이용해 회사 재직 여부를 인증합니다.**<br><br> 자체 보유한 회사 도메인 데이터를 기반으로 회사 이메일 인증을 진행해 사용자의 신뢰도를 높일 수 있습니다. 인증된 정보를 기반으로 재직 중인 회사를 차단하여 추천에서 회사 동료를 제외시킬 수 있습니다.                                                             |
|     팀 추천      | **팀원을 모두 고려해 팀 맞춤 상대 팀을 추천해줍니다.**<br><br> 구성이 완료된 팀에 한해 팀 추천이 진행됩니다. 팀원 수, 성별로 필터링한 후 사용자 기반 추천 알고리즘을 통해 팀을 추천해줍니다. 사용자의 행동 데이터가 없는 초기에는 인기가 많은 팀을 추천해줍니다. 추천 받은 팀에게 호감을 보내거나 추천을 거절할 수 있습니다.                         |
|     단체 채팅     | **채팅방을 통해 미팅이 성사된 팀들이 자유롭게 이야기 나눌 수 있습니다.**<br><br> 사용자들은 채팅방에서 실시간으로 채팅을 주고 받으며 미팅 일자와 장소 등을 정할 수 있습니다.                                                                                                              |                                     |

## 🛠️ Tech Stack

<img width="1451" alt="Jikting Teck Stack" src="https://github.com/SWM-Cupid/jikting-backend/assets/62989828/dd4fcc11-c5a5-4f6c-9398-287203605a17" style="width: 800px">

## 📁 산출물

- [기능 명세서](https://jeongyuneo.notion.site/9b1b0ee8b6644ea4a249f9b0a0a414ba?pvs=4)
- [와이어프레임](https://www.figma.com/file/4goKYkoLuYZ7k2RKo69iWw/회사팅?type=design&node-id=0-1&mode=design)
- [API 명세서](https://swm-cupid.github.io/jikting-docs/)

### ERD

<img width="1746" alt="Jikting ERD" src="https://github.com/SWM-Cupid/jikting-backend/assets/62989828/fd800268-e6dd-4991-97e0-8201cff75fd4" style="width: 800px; height: 400px">

### System Architecture

<img width="961" alt="Jikting Architecture" src="https://github.com/SWM-Cupid/jikting-backend/assets/62989828/5778369b-d518-40cf-81dd-5cee9e7e727f" style="width: 800px; height: 400px">
