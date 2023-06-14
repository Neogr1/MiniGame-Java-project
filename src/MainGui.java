import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import socket.Client;
import gui.Designs;
import gui.GuiGame1;
import gui.GuiGame2;
import gui.GuiGame3;


class MainGui extends JFrame implements Designs {
    // size
    private final int WIDTH = 720;
    private final int HEIGHT = 600;
	
	

    // panels
    public JPanel playPanel;
    public JPanel choosePanel;
    public JPanel controlPanel;

    // buttons
    public JButton startButton;
    public JButton exitButton;

    // game GUIs
    public int gameChoice = 0;
    public GuiGame1 gg1;
    public GuiGame2 gg2;
    public GuiGame3 gg3;

    public JLabel instructionsLabel; // 게임 선택 시 보이는 설명 화면

    
    // 현재 playingPanel에 떠 있는 화면 종류
    // 0이면 메인 화면 1~3이면 게임 화면
    private int currentGame = 0;

    MainGui() {
        /*
         * frame 디자인
         */
        setTitle("Mini Game");
        setSize(WIDTH, HEIGHT);
        getContentPane().setBackground(color1);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        /*
         * playPanel: 초기화면 및 게임 화면
         */
        playPanel = new JPanel();
        playPanel.setBounds(20, 20, 470, 520);
        playPanel.setLayout(new BorderLayout());

        instructionsLabel = new JLabel("<html><h1>게임을 선택해 주세요.</h1><html>");
        instructionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionsLabel.setVerticalAlignment(SwingConstants.NORTH);

        gg1 = new GuiGame1();
        gg2 = new GuiGame2();
        gg3 = new GuiGame3();

        /*
         * choosePanel: 게임 선택 버튼
         */
        choosePanel = new JPanel();
        choosePanel.setBounds(500, 20, 180, 450);
        choosePanel.setLayout(new GridLayout(3, 1)); // 게임 목록 세로로 배치

        
        JButton game1Button = new JButton("Game 1");
        JButton game2Button = new JButton("Game 2");
        JButton game3Button = new JButton("Game 3");

        game1Button.setActionCommand("1");
        game2Button.setActionCommand("2");
        game3Button.setActionCommand("3");

        game1Button.addActionListener(new ButtonClickListener());
        game2Button.addActionListener(new ButtonClickListener());
        game3Button.addActionListener(new ButtonClickListener());

        choosePanel.add(game1Button);
        choosePanel.add(game2Button);
        choosePanel.add(game3Button);
        


        // controlPanel: 시작 및 종료 버튼
        controlPanel = new JPanel();
        controlPanel.setBounds(500, 480, 180, 60);
        
        startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(100, 30));
        startButton.setFocusPainted(false); // 버튼 클릭 시 포커스 표시 제거
        startButton.setActionCommand("start");
        startButton.addActionListener(new ButtonClickListener());
        
        exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(60, 30));
        exitButton.setFocusPainted(false); // 버튼 클릭 시 포커스 표시 제거
        exitButton.setActionCommand("exit");
        exitButton.addActionListener(new ButtonClickListener());
        
        controlPanel.setLayout(new GridLayout(1, 2));
        controlPanel.add(startButton);
        controlPanel.add(exitButton);
        

        setLayout(null);
        add(playPanel);
        add(choosePanel);
        add(controlPanel);
        
        
        // 서버로부터 메시지 받아 처리하는 쓰레드 실행
		new GetMessageThread().start();
        
        // 초기 화면 = 0 (메인 화면)
        currentGame = 0;
        playPanel.add(instructionsLabel);

        setVisible(true);
    }


    // playPanel에 있는 요소 제거
    private void clearPlayPanel() {
        playPanel.removeAll();
        playPanel.revalidate();
        playPanel.repaint();
    }


    // playPanel에 설명을 보여주는 메소드
    private void showGameInstructions(int gameNumber) {
        String instructions = "";
        
        switch (gameNumber) {
	        case 1:
	            instructions = "<html>"
                    + "<h1>Treasure Hunter</h1><br><br>"
                    + "두 플레이어는 번갈아가며 10x10의 격자로 구성된 버튼 중 하나를 클릭합니다.<br><br>"
                    + "보물이 있는 칸을 클릭하였다면 보물을 획득합니다.<br><br>"
                    + "빈 칸을 클릭하였다면 가장 가까운 보물과의 거리를 알려줍니다.<br><br>"
                    + "보물과의 거리는 맨해튼 거리로 계산됩니다.<br><br>"
                    + "총 5개의 보물이 있으며 상대방보다 많은 보물을 획득한 플레이어가 승리합니다."
                    + "</html>";
	            break;
            case 2:
            	instructions = "<html>"
                    + "<h1>1 to 50</h1><br><br>"
                    + "1부터 50까지의 숫자를 오름차순으로 빠르게 클릭하는 플레이어가 승리합니다.<br><br>"
                    + "</html>";
                break;
            case 3:
            	instructions = "<html>"
                    + "<h1>Halli Galli</h1><br><br>"
                    + "두 플레이어는 28장씩 카드를 나누어 가지고 게임을 시작합니다.<br><br>"
                    + "두 플레이어는 번갈아가며 각자의 카드를 오픈 합니다.<br><br>"
                    + "같은 과일의 개수가 5개가 되면 종을 눌러 보드에 놓인 카드를 가져갈 수 있습니다.<br><br>"
                    + "같은 과일의 개수가 5개가 아닐 때 종을 누르면 상대방에게 카드를 한 장 전달합니다.<br><br>"
                    + "보유한 카드가 0장이 되는 플레이어가 패배합니다.<br><br>"
                    + "</html>";
                break;
            default:
                // 게임을 선택하지 않고 시작을 눌렀을 경우
                instructions = "<html><h1>게임을 선택해 주세요.</h1><html>";
                break;
        }
        instructionsLabel.setText(instructions);
            
        clearPlayPanel();
        playPanel.add(instructionsLabel, BorderLayout.CENTER);
    }


    // MainGui의 버튼들에 대한 액션 리스너
    private class ButtonClickListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String command = e.getActionCommand();
            
            // 게임 시작
            if (command.equals("start")) {
                // 게임 플레이 중. 클릭 불가
                if (currentGame != 0) {
                    return;
                }

                clearPlayPanel();
                switch (gameChoice) {
                    case 1:
                        playPanel.add(gg1.panel);
                        Client.sendMessage("start " + Client.userNumber + " 1");
                        break;
                    case 2:
                        playPanel.add(gg2.panel);
                        Client.sendMessage("start " + Client.userNumber + " 2");
                        break;
                    case 3:
                        playPanel.add(gg3.panel);
                        Client.sendMessage("start " + Client.userNumber + " 3");
                        break;
                    default: // 게임 선택 안 함
                        showGameInstructions(gameChoice);
                        break;
                }

                return;
            }


            // 프로그램 종료
            else if (command.equals("exit")) {
                System.exit(0);
            }
            

            // 게임 선택 (1~3)
            else {
                // 게임 플레이 중. 클릭 불가
                if (currentGame != 0) {
                    return;
                }

                int numberChosenGame = Integer.parseInt(command); // 선택한 게임의 순번
                gameChoice = numberChosenGame;
                showGameInstructions(numberChosenGame);
            }
        }
    }



	private class GetMessageThread extends Thread {
        public String receivedMessage;
        
        @Override
        public void run() {
            while (true) {
                receivedMessage = Client.getReceivedMessage();

				// 서버가 보낸 메시지 처리
                if (receivedMessage != null) {
                    parseReceivedMessage(receivedMessage);
                }
            }
        }

        	
        // 서버가 보낸 메시지를 파싱해서 결과 처리
        public void parseReceivedMessage(String message) {
            String[] parsedMessage = message.split(" ");

            // "start [gameNumber] *[initString]"
            // 세번째 인자는 여기서는 무시해도 됨
            // 해당 게임 시작
            if (parsedMessage[0].equals("start")) {
                currentGame = Integer.parseInt(parsedMessage[1]);
                passMessageToEachGui(currentGame, message);
                return;
    		}

            // "finish *[finInfoStr]"
            // 현재 게임 종료
            else if (parsedMessage[0].equals("finish")) {
                passMessageToEachGui(currentGame, message);
                currentGame = 0;
                return;
            }

            // "EXIT"
            // 상대가 게임 종료함 -> 3초 뒤에 게임 종료
            if (parsedMessage[0].equals("EXIT")) {
                try {
                    clearPlayPanel();
                    playPanel.add(instructionsLabel);
                    for (int i = 3; i >= 1; i--) {
                        instructionsLabel.setText("The opponent has quit the game.\nThe game will end in " + i + " seconds.");
                        Thread.sleep(1000);
                    }
                    System.exit(0);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 이외의 경우:
            // 각 게임에서 메시지를 처리하도록
            else {
                passMessageToEachGui(currentGame, message);
                return;
            }
        }


        // 각 게임에서 메시지를 처리
        public void passMessageToEachGui(int gameNumber, String message) {
            switch (gameNumber) {
                case 1:
                    gg1.parseReceivedMessage(message);
                    break;
                case 2:
                    gg2.parseReceivedMessage(message);
                    break;
                case 3:
                    gg3.parseReceivedMessage(message);
                    break;
            }
        }

    }
}
