# MiniGame-Java-project

Java의 swing, socket, multi-threading을 이용한 2인용 미니게임
자바프로그래밍실습 강의 기말 프로젝트 중 본인의 코드를 추려내어 다듬음

## 실행
### 로그인
프로그램을 처음 실행하면 로그인 창이 나온다.
1P를 선택하여 서버와 클라이언트를 모두 실행할 수 있고 2P는 클라이언트만 실행한다.
두 플레이어가 같은 네트워크에 연결된 상태로 2P는 1P의 로컬 IP를 입력해야 1P의 서버에 참여할 수 있다.
같은 디바이스에서 두 프로세스를 실행한다면 IP는 입력하지 않아도 된다.
### 게임 시작
게임을 선택하면 각 게임에 대한 설명이 나온다.
이후 `Start` 버튼을 눌러 게임을 시작할 수 있다.

## 게임
### 보물찾기
10x10의 격자에 5개의 보물이 숨겨져 있다.
두 플레이어는 번갈아 가며 버튼을 하나씩 클릭하여 보물을 획득할 수 있다.
보물이 있는 칸을 클릭했다면 보물을 획득하고, 그렇지 않은 칸을 클릭했다면 가장 가까운 보물과의 거리를 [맨해튼 거리](https://ko.wikipedia.org/wiki/맨해튼_거리)로 알려준다.
다섯 개의 보물이 모두 발견되면 게임이 종료되고 더 많은 보물을 획득한 플레이어가 승리한다.
### 1 to 50
1부터 50까지의 숫자 버튼을 오름차순으로 빠르게 클릭하는 플레이어가 승리한다.
### 할리갈리
두 플레이어가 총 56장의 카드를 28장씩 나누어 가지고 게임을 시작한다.
두 플레이어는 번갈아 가며 자신의 스택을 클릭해 보드에 카드를 한 장씩 내려놓는다.
보드에 같은 과일 그림이 5개가 되면 종을 클릭해 보드에 놓인 카드를 모두 가져갈 수 있다.
같은 과일 그림이 5개가 아닐 때 종을 클릭하면 벌칙으로 상대에게 카드 한 장을 준다.
게임 진행 중에 누군가의 카드가 0장이 되면 그 플레이어의 패배로 게임이 종료된다.
