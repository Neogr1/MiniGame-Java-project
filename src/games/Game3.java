package games;

import java.util.Random;


public class Game3 {
	Random rd = new Random();

	public static final int TOTAL_CARDS = 56;
	public static final int FRUITS = 4;
	public static final String[] FRUITS_NAME = { "banana", "lime", "plum", "strawberry" };
	public static final int[] NUMBERS = { 1,1,1,2,2,2,3,3,3,4,4,4,5,5 }; // 과일 개수 별 확률 조절

	public static int onBoardCardCount;
	public static int[] stackCardCount = new int[2];
	public static int[][] topCard = new int[2][2]; // ((과일, 개수), (과일, 개수))
	


	public void initGame() {
		onBoardCardCount = 0;
		stackCardCount[0] = TOTAL_CARDS / 2;
		stackCardCount[1] = TOTAL_CARDS / 2;
		clearBoard();
	}
	
	public void clearBoard() {
		topCard[0] = new int[] {-1, -1};
		topCard[1] = new int[] {-1, -1};
	}
	
	// user가 종 누름. 개수의 합이 5개인 과일이 있는지 확인
	public boolean isSum5(int user) {
		int[] sums = {0, 0, 0, 0};

		if (topCard[0][0] != -1) sums[topCard[0][0]] += topCard[0][1];
		if (topCard[1][0] != -1) sums[topCard[1][0]] += topCard[1][1];

		for (int sum: sums) {
			if (sum == 5) { // 5개인 과일 있음
				// 종 누른 user가 카드 가져감
				stackCardCount[user] += onBoardCardCount;
				onBoardCardCount = 0;
				clearBoard();

				return true;
			}
		}

		// 5개인 과일 없음
		// 종 누른 user가 상대에게 카드 줌 
		stackCardCount[user]--;
		stackCardCount[1-user]++;
		
		return false;
	}
	

	public void openCard(int user) {
		onBoardCardCount++;
		stackCardCount[user]--;

		topCard[user][0] = rd.nextInt(FRUITS);
		topCard[user][1] = NUMBERS[rd.nextInt(NUMBERS.length)];
	}

	// 둘 중 한 명이 카드를 모두 잃거나 음수가 되면 게임 종료
	// 패자의 user번호 리턴
	// 끝나지 않았으면 -1 리턴
	public int isFinished() {
		if (stackCardCount[0] == 0) {
			return 0;
		}
		if (stackCardCount[1] == 0) {
			return 1;
		}
		return -1;
	}
}


