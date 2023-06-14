package gui;


import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import games.Game3;
import socket.Client;

public class GuiGame3 extends Game3 implements Designs {

	public boolean myTurn;
	public boolean playing;

	public JPanel panel;
	public JPanel infoPanel;
    public JPanel playPanel;
	public JLabel infoLabel;

	// image icons
	ImageIcon bellImageIcon;
	ImageIcon backSideImageIcon;
	ImageIcon[][] fruitsImagesIcons;

	// buttons
	JButton bellButton;
	JButton myCardOpenButton;
	
	// labels
	JLabel opponentCardOpenLabel;
	JLabel myCardLabel;
	JLabel opponentCardLabel;

	// text fileds for showing number of card
	JLabel myCardCount;
	JLabel opponentCardCount;
	
	public GuiGame3() {

		panel = new JPanel();
		panel.setBounds(gameArea);

		infoPanel = new JPanel();
		infoPanel.setPreferredSize(gameInfoArea);

		infoLabel = new JLabel("상대방 대기 중...");
		infoLabel.setFont(infoFont);
		infoPanel.add(infoLabel);
		
        playPanel = new JPanel();
		playPanel.setPreferredSize(gamePlayArea);
        
		// initialize image icons
		bellImageIcon = getImageIcon("image/bell.png", 80, 80);
		backSideImageIcon = getImageIcon("image/back_side.png", 48, 76);
		initFruitsImages();
		
        
		// bell button
		bellButton = new JButton(bellImageIcon);
		bellButton.setBorderPainted(false);
        bellButton.setContentAreaFilled(false);
        bellButton.setFocusPainted(false);
		bellButton.setBounds((int)(gamePlayArea.getWidth())/2 - 40, (int)(gamePlayArea.getHeight())/2 - 40, 80, 80);
		
		bellButton.setActionCommand("bell");
		bellButton.addActionListener(new ButtonClickListener());
		
		
		// my card open button
		myCardOpenButton = new JButton(backSideImageIcon);
		myCardOpenButton.setBorderPainted(false);
        myCardOpenButton.setContentAreaFilled(false);
        myCardOpenButton.setFocusPainted(false);
		myCardOpenButton.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		myCardOpenButton.setBounds((int)(gamePlayArea.getWidth())/2 - 26, (int)(gamePlayArea.getHeight()) - 88, 52, 80);
		
		myCardOpenButton.setActionCommand("open");
		myCardOpenButton.addActionListener(new ButtonClickListener());


		// label design
		opponentCardOpenLabel = new JLabel(backSideImageIcon);
		opponentCardOpenLabel.setBounds((int)(gamePlayArea.getWidth())/2 - 26, 12, 52, 80);
		myCardLabel = new JLabel();
		myCardLabel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		myCardLabel.setBounds((int)(gamePlayArea.getWidth())/2 - 26, (int)(gamePlayArea.getHeight()) - 176, 52, 80);
		opponentCardLabel = new JLabel();
		opponentCardLabel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		opponentCardLabel.setBounds((int)(gamePlayArea.getWidth())/2 - 26, 100, 52, 80);
		
		
		// card count design
		myCardCount = new JLabel();
		myCardCount.setBounds((int)(gamePlayArea.getWidth())/2 - 80, (int)(gamePlayArea.getHeight()) - 88, 52, 80);		
		myCardCount.setFont(game3CountFont);
		opponentCardCount = new JLabel();
		opponentCardCount.setBounds((int)(gamePlayArea.getWidth())/2 - 80, 12, 52, 80);
		opponentCardCount.setFont(game3CountFont);
		

		playPanel.setLayout(null);
		playPanel.add(bellButton);
		playPanel.add(myCardOpenButton);
		playPanel.add(opponentCardOpenLabel);
		playPanel.add(myCardLabel);
		playPanel.add(opponentCardLabel);
		playPanel.add(myCardCount);
		playPanel.add(opponentCardCount);
		
		
		playPanel.setPreferredSize(gamePlayArea);
		panel.add(infoPanel);
		panel.add(playPanel);

		playing = true;
	}
	

	// return the resized image icon
	ImageIcon getImageIcon(String path, int width, int height) {
		Image image = (new ImageIcon(path)).getImage();
		ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
		return imageIcon;
	}
	
	// initialize fruits' images
	void initFruitsImages() {
		String path;
		fruitsImagesIcons = new ImageIcon[FRUITS][6];
		for (int fruits = 0; fruits < FRUITS; fruits++) {
			for (int num = 1; num <= 5; num++) {
				path = "image/" + FRUITS_NAME[fruits] + num + ".png";
				fruitsImagesIcons[fruits][num] = getImageIcon(path, 48, 76);
			}
		}
	}

	void clearOnBoardCard() {
		myCardLabel.setIcon(null);
		opponentCardLabel.setIcon(null);
	}


	
	// 결과 메시지를 파싱해서 결과 처리
	public void parseReceivedMessage(String message) {
		String[] parsedMessage = message.split(" ");

		// "start [gameNumber] [firstUserNumber]"
		// 게임 시작
		if (parsedMessage[0].equals("start")) {
			int firstUserNumber = Integer.parseInt(parsedMessage[2]);
			if (firstUserNumber == Client.userNumber) {
				myTurn = true;
				infoLabel.setText("게임 시작! 당신의 차례입니다.");
			}
			else {
				myTurn = false;
				infoLabel.setText("게임 시작! 상대방의 차례입니다.");
			}

			myCardCount.setText(String.valueOf(TOTAL_CARDS / 2));
			opponentCardCount.setText(String.valueOf(TOTAL_CARDS / 2));
			playing = true;

			return;
		}


		// "finish [loser]"
		// 보물 찾음. 버튼 오픈. 게임 종료
		if (parsedMessage[0].equals("finish")) {
			String behave = parsedMessage[1];
			int loser = Integer.parseInt(parsedMessage[2]);

			int myCount = Integer.parseInt(myCardCount.getText());
			int opponentCount = Integer.parseInt(opponentCardCount.getText());
			if (loser == Client.userNumber) {
				myCardCount.setText(String.valueOf(myCount-1));
				if (behave.equals("bell")) {
					opponentCardCount.setText(String.valueOf(opponentCount+1));
				}
				infoLabel.setText("게임 종료. 패배...");
			} else {
				if (behave.equals("bell")) {
					myCardCount.setText(String.valueOf(myCount+1));
				}
				opponentCardCount.setText(String.valueOf(opponentCount-1));
				infoLabel.setText("게임 종료. 승리!");
			}

			playing = false;
			return;
		}

		// "open [user] [topFruit] [topNumber]"
		// 카드 오픈
		if (parsedMessage[0].equals("open")) {
			int user = Integer.parseInt(parsedMessage[1]);
			int topFruit = Integer.parseInt(parsedMessage[2]);
			int topNumber = Integer.parseInt(parsedMessage[3]);

			if (user == Client.userNumber) {
				myCardLabel.setIcon(fruitsImagesIcons[topFruit][topNumber]);
				int myCount = Integer.parseInt(myCardCount.getText());
				myCardCount.setText(String.valueOf(myCount-1));
			} else {
				opponentCardLabel.setIcon(fruitsImagesIcons[topFruit][topNumber]);
				int opponentCount = Integer.parseInt(opponentCardCount.getText());
				opponentCardCount.setText(String.valueOf(opponentCount-1));
			}
			
			if (Client.userNumber == user) infoLabel.setText("내가 카드 오픈!");
			else infoLabel.setText("상대가 카드 오픈!");
			myTurn = !myTurn;
			return;
		}


		// "success [user] [stackCardCount of user]"
		// 종 누르기 성공. 스택의 카드 개수 수정
		if (parsedMessage[0].equals("success")) {
			clearBoard();
			myCardLabel.setIcon(null);
			opponentCardLabel.setIcon(null);

			int user = Integer.parseInt(parsedMessage[1]);
			int count = Integer.parseInt(parsedMessage[2]);
			if (user == Client.userNumber) {
				myCardCount.setText(String.valueOf(count));
				infoLabel.setText("카드 획득!");
			} else {
				opponentCardCount.setText(String.valueOf(count));
				infoLabel.setText("이런, 상대방이 더 빨랐네요...");
			}

			return;
		}

		// "fail [user]"
		// 종 누르기 실패. 스택의 카드 개수 수정
		if (parsedMessage[0].equals("fail")) {
			int user = Integer.parseInt(parsedMessage[1]);
			int myCount = Integer.parseInt(myCardCount.getText());
			int opponentCount = Integer.parseInt(opponentCardCount.getText());
			if (user == Client.userNumber) {
				myCardCount.setText(String.valueOf(myCount-1));
				opponentCardCount.setText(String.valueOf(opponentCount+1));
				infoLabel.setText("나의 실수! 카드를 한 장 잃습니다.");
			} else {
				myCardCount.setText(String.valueOf(myCount+1));
				opponentCardCount.setText(String.valueOf(opponentCount-1));
				infoLabel.setText("상대방의 실수! 카드를 한 장 얻습니다.");
			}

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

			String command = e.getActionCommand();

			// 자신의 턴이 아님 -> 클릭 불가
			if (command.equals("open") && !myTurn) {
				infoLabel.setText("It's opponent's turn...\nPlease wait for a while");
				return;
			}

			// 서버에게 행동 메시지 전송
			// "bell [user]" 또는 "open [user]"
			Client.sendMessage(command + " " + Client.userNumber);
		}
    }
}

