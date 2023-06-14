package gui;

import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import games.Game2;
import socket.Client;

public class GuiGame2 extends Game2 implements Designs {

	public boolean playing;

	public JPanel panel;
	public JPanel infoPanel;
    public JPanel playPanel;
    public static JButton buttons[][] = new JButton[SIZE][SIZE];
	public JLabel infoLabel;
	
	public GuiGame2() {

		panel = new JPanel();
		panel.setBounds(gameArea);

		infoPanel = new JPanel();
		infoPanel.setPreferredSize(gameInfoArea);

		infoLabel = new JLabel("상대방 대기 중...");
		infoLabel.setFont(infoFont);
		infoPanel.add(infoLabel);

        playPanel = new JPanel();
        playPanel.setPreferredSize(gamePlayArea);
        
        
        for (int r = 0; r < SIZE; r++) {
        	for (int c = 0; c < SIZE; c++) {
				JButton button = new JButton();
        		
        		// 버튼 디자인 및 위치
        		button.setBackground(color1);
				button.setFont(game2ButtonFont);
				button.setFocusPainted(false);
        		
                // 버튼 클릭 이벤트
                button.setActionCommand(r+" "+c);
                button.addActionListener(new ButtonClickListener());
        		
        		buttons[r][c] = button;
        		playPanel.add(button);
        	}
        }

		playPanel.setLayout(new GridLayout(SIZE, SIZE, 5, 5));
		panel.add(infoPanel);
		panel.add(playPanel);
	}
	
	
	// 게임을 시작할 때 버튼 디자인 초기화
	public void initButtons(String[] parsed) {
		for (int r = 0; r < SIZE; r++) {
			for (int c = 0; c < SIZE; c++) {
				buttons[r][c].setBackground(color1);
				buttons[r][c].setText(parsed[r*SIZE + c + 2]);
			}
		}
	}

	// 3초 기다린 후에 시작
	void wait3Sec() {
		try {
			infoLabel.setText("게임이 곧 시작됩니다.");
			Thread.sleep(1000);
			infoLabel.setText("3");
			Thread.sleep(1000);
			infoLabel.setText("2");
			Thread.sleep(1000);
			infoLabel.setText("1");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}	
		
	// 결과 메시지를 파싱해서 결과 처리
	public void parseReceivedMessage(String message) {
		String[] parsedMessage = message.split(" ");

		// "start [gameNumber] [numbers 1 ~ 25]"
		// 게임 시작. 서버에게서 받은 숫자로 타일 초기화
		if (parsedMessage[0].equals("start")) {
			initButtons(parsedMessage);

			wait3Sec();
			playing = true;
			infoLabel.setText("게임 시작! 목표: 1");
		}


		int user = Integer.parseInt(parsedMessage[1]);
		int r = Integer.parseInt(parsedMessage[2]);
		int c = Integer.parseInt(parsedMessage[3]);

		// "finish [user] [r] [c]"
		// [user]가 먼저 끝남
		if (parsedMessage[0].equals("finish")) {
			if (user == Client.userNumber) {
				buttons[r][c].setText("");
				buttons[r][c].setBackground(Color.black);
				infoLabel.setText("승리!");
			} else {
				infoLabel.setText("패배...");
			}

			playing = false;
			return;
		}

		// "target [user] [r] [c] [nextTarget] [nextLayer]"
		// 타깃 클릭함.
		if (parsedMessage[0].equals("target")) {
			if (Integer.parseInt(parsedMessage[1]) != Client.userNumber) return;

			int nextTarget = Integer.parseInt(parsedMessage[4]);
			int nextLayer = Integer.parseInt(parsedMessage[5]);

			if (nextTarget-1 > SIZE*SIZE) {
				buttons[r][c].setText("");
				buttons[r][c].setBackground(Color.black);
			} else {
				buttons[r][c].setText(String.valueOf(nextLayer));
			}

			infoLabel.setText("목표: " + String.valueOf(nextTarget));

			return;
		}

		// "notTarget [user] [r] [c]"
		// do nothing
		if (parsedMessage[0].equals("notTarget")) {
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

			// 클릭한 버튼의 좌표 획득
			String rc[] = e.getActionCommand().split(" ");
			int r = Integer.parseInt(rc[0]);
			int c = Integer.parseInt(rc[1]);
			
			// 서버에게 행동 메시지 전송
			Client.sendMessage("click " + Client.userNumber + " " + r + " " + c ); // "click 0 1 2"
		}
    }
}
