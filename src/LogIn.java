import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LogIn extends JFrame {
	private final int WIDTH = 320;
	private final int HEIGHT = 240;
	
	public boolean started = false;
	public boolean ser;
	public String ip = null;

	JRadioButton r1;
	JRadioButton r2;
	JPanel ipPanel;
	JPanel emptyPanel;
	JTextField ipInputField;
	
	public LogIn() {

		setTitle("Mini Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setResizable(false);
		
		/*
		 * 선택 버튼 두개 생성 및 그룹화
		 */
		JPanel playerChoosePanel = new JPanel();
		ButtonGroup playerButtonGroup = new ButtonGroup();
		
		r1 = new JRadioButton("1P");
		r2 = new JRadioButton("2P");
		// 1P 기본 선택
		ser = true;
		r1.setSelected(true);

		r1.addChangeListener(new PlayerChooseListener());
		r2.addChangeListener(new PlayerChooseListener());
		// 그룹화
		playerButtonGroup.add(r1);
		playerButtonGroup.add(r2);
		// 버튼 패널에 추가
		playerChoosePanel.add(r1);
		playerChoosePanel.add(r2);
		

		/*
		 * 간격 맞추기 위한 빈 패널
		 */
		emptyPanel = new JPanel(); 
		emptyPanel.setPreferredSize(new Dimension(200,40));
		JLabel emptyLabel = new JLabel(" ");
		emptyPanel.add(emptyLabel);

		/*
		 * ip 입력 창
		 */
		ipPanel = new JPanel();
		ipPanel.setPreferredSize(new Dimension(200,40));
		JLabel ipLabel = new JLabel("IP");
		ipInputField = new JTextField(20);

		ipPanel.add(ipLabel);
		ipPanel.add(ipInputField);
		ipPanel.setVisible(false);
		
		/*
		 * 게임 입장 설명
		 */
		String instMsg = "<html><center>";
		instMsg += "1P를 선택하여 방장이 되어 서버를 열 수 있습니다.<br>";
		instMsg += "2P를 선택하여 방장이 연 서버에 입장할 수 있습니다.<br>";
		instMsg += "2P를 선택하면 1P의 로컬 IP를 입력해야 합니다.";
		instMsg += "</center></html>";
		JPanel instPanel = new JPanel();
		instPanel.setSize(200,90);
		JLabel instLabel = new JLabel(instMsg);
		instPanel.add(instLabel);

		/*
		 * 게임 시작 버튼
		 */
		JPanel startButtonPanel = new JPanel();
		JButton startButton = new JButton("Start Game");
		startButton.addActionListener(new StartButtonListener());
		startButtonPanel.add(startButton);
		startButtonPanel.setPreferredSize(new Dimension(240, 40));

		add(playerChoosePanel);
		add(emptyPanel);
		add(ipPanel);
		add(instPanel);
		add(startButtonPanel);

		// 로그인 창을 화면 중앙에 띄움
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
		setLocation((screenWidth-WIDTH)/2, (screenHeight-HEIGHT)/2);

		setVisible(true);
	}

	public class PlayerChooseListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			if (r1.isSelected()) {
				ipPanel.setVisible(false);
				emptyPanel.setVisible(true);
			}
			if (r2.isSelected()) {
				emptyPanel.setVisible(false);
				ipPanel.setVisible(true);
			}
		}
		
	}


	// start 버튼 누르면 선택한 플레이어에 따라 조건 설정 및 IP 기록
	public class StartButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			started = true;
			if (r1.isSelected()) {
				ser = true;
			}
			if (r2.isSelected()) {
				ser = false;
			}

			ip = ipInputField.getText(); // 입력한 IP 주소 가져오기

			dispose();
		}		
	}
}