package games;
/*
 * 
 * 10*10의 격자에 숨겨진 5개의 보물을 찾는 게임
 * 특정 위치를 고르면 보물을 찾거나 가장 가까운 보물과의 거리가 출력됨
 * 
 * 
 */


import java.util.Arrays;
import java.util.Random;


public class Game1 {
	public final static int MAP_SIZE = 10;
	public final static int TARGET_COUNT = 5;

	public int[] score = new int[2];

	public boolean[][] map = new boolean[MAP_SIZE][MAP_SIZE];    // target이 있는지
	public boolean[][] opened = new boolean[MAP_SIZE][MAP_SIZE]; // 확인했는지
	public int[][] targetPositions = new int[TARGET_COUNT][2];   // target의 위치 저장
	public boolean[] targetFounded = new boolean[TARGET_COUNT];  // target을 찾았는지
	public int foundCount;
	
	// map에 무작위로 5개의 target을 표시하고 그 위치를 기록함. targetFounded는 false로 초기화
	public void initGame() {
		score[0] = 0;
		score[1] = 0;
		foundCount = 0;

		// fill map and opened with false
		for (boolean[] row :    map) Arrays.fill(row, false);
		for (boolean[] row : opened) Arrays.fill(row, false);

		Random rd = new Random();
		int r, c;
		
		for (int target = 0; target < TARGET_COUNT; target++) {
			r = rd.nextInt(MAP_SIZE);
			c = rd.nextInt(MAP_SIZE);
			while (map[r][c] == true) { // 이미 target 지정된 위치면 다시 탐색
				r = rd.nextInt(MAP_SIZE);
				c = rd.nextInt(MAP_SIZE);
			}
			
			map[r][c] = true;
			targetPositions[target][0] = r;
			targetPositions[target][1] = c;
			targetFounded[target] = false;
		}
	}
	
	
	// 확인할 위치로부터 찾지 못한 target 중 가장 짧은 거리 반환
	public int getDistanceOfNearestTarget(int row, int col) {
		int minDis = MAP_SIZE * 2; // 아무리 멀어도 거리는 19 이하
		int r, c;
		
		for (int target = 0; target < TARGET_COUNT; target++) {
			if (targetFounded[target]) continue;
			
			r = targetPositions[target][0];
			c = targetPositions[target][1];
			minDis = Math.min(minDis, Math.abs(row-r) + Math.abs(col-c));
		}
		
		return minDis;
	}
	
	
	// 선택한 위치에 target이 있는지 확인
	// 가까운 target과의 거리 반환
	public int isTarget(int row, int col) {
		if (opened[row][col] == true) {
			return -1;
		}
		
		opened[row][col] = true;

		// target 찾음. founded에 기록하고 count 증가
		if (map[row][col]) {
			for (int target = 0; target < TARGET_COUNT; target++) {
				if (targetPositions[target][0] == row && targetPositions[target][1] == col) {
					targetFounded[target] = true;
					break;
				}
			}
			foundCount++;
			return 0;
		} else {
			return getDistanceOfNearestTarget(row, col);
		}
	}
	
	// target 찾음 -> 점수 증가
	public void incScore(int userNumber) {
		score[userNumber]++;
	}


	public boolean isFinished() {
		return foundCount == TARGET_COUNT;
	}
}




