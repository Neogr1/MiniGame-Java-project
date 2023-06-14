package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import games.Game1;
import socket.Client;

public class GuiGame1 extends Game1 implements Designs {

	public boolean myTurn;

	public boolean playing;
	
	public JPanel panel;
	public JPanel infoPanel;
    public JPanel playPanel;
    public static JButton buttons[][] = new JButton[MAP_SIZE][MAP_SIZE];
	public JLabel infoLabel;
	
	public GuiGame1() {

		panel = new JPanel();
		panel.setBounds(gameArea);

		infoPanel = new JPanel();
		infoPanel.setPreferredSize(gameInfoArea);
		
		infoLabel = new JLabel("상대방 대기 중...");
		infoLabel.setFont(infoFont);
		infoPanel.add(infoLabel);

        playPanel = new JPanel();
        playPanel.setPreferredSize(gamePlayArea);
        
		for (int r = 0; r < MAP_SIZE; r++) {
			for (int c = 0; c < MAP_SIZE; c++) {
        		JButton button = new JButton();
        		
        		// 버튼 디자인 및 위치
        		button.setBackground(color1);
				button.setFont(game1ButtonFont);
				button.setFocusPainted(false);
				
                // 버튼 클릭 이벤트
                button.setActionCommand(r+" "+c);
                button.addActionListener(new ButtonClickListener());
				
        		buttons[r][c] = button;
        		playPanel.add(button);
        	}
        }
		
		playPanel.setLayout(new GridLayout(MAP_SIZE, MAP_SIZE, 5, 5));
		panel.add(infoPanel);
		panel.add(playPanel);
	}
	

	// 게임을 시작할 때 버튼 디자인 초기화
	public void initButtons() {
		for (int r = 0; r < MAP_SIZE; r++) {
        	for (int c = 0; c < MAP_SIZE; c++) {
        		buttons[r][c].setBackground(color1);
				buttons[r][c].setText("");
        	}
        }
	}

	
	// 결과 메시지를 파싱해서 결과 처리
	public void parseReceivedMessage(String message) {
		String[] parsedMessage = message.split(" ");

		// "start [gameNumber] [firstUserNumber]"
		// 게임 시작
		if (parsedMessage[0].equals("start")) {
			initGame();
			initButtons();

			int firstUserNumber = Integer.parseInt(parsedMessage[2]);
			if (firstUserNumber == Client.userNumber) {
				myTurn = true;
				infoLabel.setText("게임 시작! 당신의 차례입니다.");
			}
			else {
				myTurn = false;
				infoLabel.setText("게임 시작! 상대방의 차례입니다.");
			}
		    
			playing = true;

			return;
		}



		// "alreadyOpen [user]"
		// 이미 열린 버튼. 아무 행동 안 함
		if (parsedMessage[0].equals("alreadyOpen")) {
				int userNumber = Integer.parseInt(parsedMessage[1]);
			if (Client.userNumber == userNumber) {
				infoLabel.setText("이미 열린 버튼입니다.");
			}
			return;
		}
		
		int r = Integer.parseInt(parsedMessage[1]);
		int c = Integer.parseInt(parsedMessage[2]);

		// "finish [r] [c] [user0score] [user1score]"
		// 보물 찾음. 버튼 오픈. 게임 종료
		if (parsedMessage[0].equals("finish")) {
			int user0score = Integer.parseInt(parsedMessage[3]);
			int user1score = Integer.parseInt(parsedMessage[4]);

		    buttons[r][c].setBackground(Color.white);

			String infoString = "<html>"+r+"행 "+c+"열 클릭! 마지막 보물 발견!<br>";
			// 경우의 수에 따라 메시지 추가
			if (Client.userNumber == 0) {
				if (user0score > user1score) {
					infoString += "<pre>나 " + user0score + " : " + user1score + " 상대        승리!</pre>";
				} else {
					infoString += "<pre>나 " + user0score + " : " + user1score + " 상대       패배...</pre>";
				}
			} else {
				if (user0score < user1score) {
					infoString += "<pre>나 " + user1score + " : " + user0score + " 상대        승리!</pre>";
				} else {
					infoString += "<pre>나 " + user1score + " : " + user0score + " 상대       패배...</pre>";
				}
			}
			infoString += "</html>";
			infoLabel.setText(infoString);

			playing = false;
			return;
		}

		// "target [r] [c] [foundCount]"
		// 보물 찾음. 버튼 오픈
		if (parsedMessage[0].equals("target")) {
			int foundCount = Integer.parseInt(parsedMessage[3]);
			infoLabel.setText(r+"행 "+c+"열 클릭! " + foundCount + "번째 보물 발견!");

		    buttons[r][c].setBackground(Color.white);
		    myTurn = !myTurn; // 턴 변경
			return;
		}

		// "empty [r] [c] [distance]"
		// 빈칸. 버튼 오픈. 가까운 보물 거리 알려줌.
		if (parsedMessage[0].equals("empty")) {
			int distance = Integer.parseInt(parsedMessage[3]);
			infoLabel.setText(r+"행 "+c+"열 클릭! " + "가장 가까운 보물과의 거리 = " + distance);

		    buttons[r][c].setBackground(Color.black);
			myTurn = !myTurn; // 턴 변경
			return;
		}
	}




    private class ButtonClickListener implements ActionListener {
    	
		@Override
		public void actionPerformed(ActionEvent e) {
			// 게임 종료됨. 클릭 불가
			if (!playing) {
				return;
			}

			// 자신의 턴이 아님 -> 클릭 불가
			if (!myTurn) {
				infoLabel.setText("상대방의 차례입니다. 잠시만 기다려 주세요.");
				return;
			}

			// 클릭한 버튼의 좌표 획득
			String rc[] = e.getActionCommand().split(" ");
			int r = Integer.parseInt(rc[0]);
			int c = Integer.parseInt(rc[1]);
			
			// 서버에게 행동 메시지 전송
			Client.sendMessage("click " + Client.userNumber + " " + r + " " + c ); // "click 0 1 2"
			
		}
    }
}
