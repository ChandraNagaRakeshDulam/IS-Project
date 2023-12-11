/*
 Group 20
 Team Members:
 RakesH Dulam,
 Harshitha Sakamuri,
 Varsha Thondalapally,
 Nikhitha Sai Boyidapu.

 Project : 3D Tic Tac Toe
 
 3D Tic-Tac-Toe, also known as Qubic or 4x4x4 Tic-Tac-Toe, is a three-dimensional variation of the classic Tic-Tac-Toe game. 
 The game is played on a 4x4x4x4 quadra grid, creating a total of 64 individual cells. 
 The objective is similar to traditional Tic-Tac-Toe: players strive to align four of their markers in a row in any 
 direction—horizontally, vertically, or diagonally.
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Random;
import java.util.concurrent.CountDownLatch;


public class App extends JFrame implements ActionListener 
{
    private JButton resetButton, newGameButton;
    private JPanel gridPanel, textPanel;
    private JLabel head0, score, head, head1, head2;
    private JRadioButton bluePawn, yellowPawn, AI_First, playerFirst, rulesButton;
    private boolean playerTurn = true;
    private int difficulty = 2;
    private int playLevel = 2;
    private int playLevelCounter = 0;
    private int playerScore = 0;
    private int AI_Score = 0;
    private JLabel victoryLabel;
    
    int[] victoryState = new int[4];
    XOButton[] victoryStateButton = new XOButton[4];
    public boolean victory = false;
    char playerPawn = 'X';
    char AI_Pawn = 'O';
    private char setup[][][];
    private XOButton[][][] gameGrid;


    private class XOButton extends JButton 
    {
        public int gridRow;
        public int gridColumn;
        public int gridBoard;
        private char player = '-';
        public void setPlayer(char player) 
        {
            this.player = player;
            this.repaint(); // Repaint the button to show the new icon
        }
        
        @Override
        protected void paintComponent(Graphics g) 
        {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
            int diameter = Math.min(getWidth(), getHeight()) - 20; // Adjust the size of the sphere
            int x = (getWidth() - diameter) / 2;
            int y = (getHeight() - diameter) / 2;
        
            // Gradient paint for a 3D effect
            GradientPaint gp = null;
            if (this.player == 'X') 
            {
                gp = new GradientPaint(
                        x, y,
                        Color.ORANGE.brighter(),
                        x + diameter, y + diameter,
                        Color.ORANGE.darker());
                g2.setPaint(gp);
                g2.fillOval(x, y, diameter, diameter); // Draw the sphere with gradient
                // Adding highlight for more 3D effect
                g2.setColor(Color.ORANGE.brighter().brighter());
                g2.fillOval(x + diameter / 5, y + diameter / 5, diameter / 2, diameter / 2);
            } 
            else if (this.player == 'O') 
            {
                gp = new GradientPaint(
                        x, y,
                        Color.BLUE.brighter(),
                        x + diameter, y + diameter,
                        Color.BLUE.darker());
                g2.setPaint(gp);
                g2.fillOval(x, y, diameter, diameter); // Draw the sphere with gradient
                // Adding highlight for more 3D effect
                g2.setColor(Color.BLUE.brighter().brighter());
                g2.fillOval(x + diameter / 5, y + diameter / 5, diameter / 2, diameter / 2);
            }
        }
        

    }


    public class ActionStep {
        int grid;
        int row;
        int column;
    }


    public App() {
        super("3D Tic-Tac-Toe!");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.white);
        setVisible(false);
    }


    public static void main(String[] args) throws Exception {
        CountDownLatch latch = new CountDownLatch(0);
        SwingUtilities.invokeLater(() -> {
            try {
                App app = new App();
                app.showWelcomeScreen();
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        latch.await();
    }


	public class RoundedButton extends JButton {
		private int cornerRadius = 25; // Adjust the roundness
	
		public RoundedButton(String label) {
			super(label);
			setOpaque(false);
			setFocusPainted(false);
			setBorderPainted(false);
			setContentAreaFilled(false);
			setFont(new Font("Roboto", Font.BOLD, 14));
		}
	
		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(getBackground());
			g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, cornerRadius, cornerRadius);
			super.paintComponent(g2);
		}
	
		@Override
		protected void paintBorder(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(getForeground());
			g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, cornerRadius, cornerRadius);
		}
	}

    
    private void showWelcomeScreen() 
    {
        JFrame welcomeFrame = new JFrame("3D - Tic Tac Toe");
        welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); // Increased padding
        welcomeFrame.add(welcomePanel);
    
        JLabel welcomeLabel = new JLabel("Welcome to Tic Tac Toe");
        welcomeLabel.setFont(new Font("Roboto", Font.BOLD, 24)); // Increased font size
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomePanel.add(welcomeLabel);
    
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 60))); // Increased spacing
    
        ImageIcon playIcon = new ImageIcon(App.class.getResource("/resources/Tic_Tac_Toe.gif")); // Replace with your icon path
    
        JLabel imageLabel = new JLabel(playIcon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomePanel.add(imageLabel);
    
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 30))); // Increased spacing
    
        JButton playGameButton = new RoundedButton("Play Game");
        playGameButton.setFont(new Font("Roboto", Font.BOLD, 20)); // Increased font size
        playGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        playGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //createBoard();
                showDifficultySelection();
                welcomeFrame.dispose();
            }
        });
    
        welcomePanel.add(playGameButton);
    
        // Use the screen size for better visibility
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        welcomeFrame.setSize((int) (screenSize.getWidth() * 0.4), (int) (screenSize.getHeight() * 0.95));
        welcomeFrame.setLocationRelativeTo(null);
        welcomeFrame.setVisible(true);
    }


    private class GridPanel extends JPanel
    {
		protected void paintComponent(Graphics g)
        {
			super.paintComponent(g);
			Graphics2D lines = (Graphics2D) g;

			lines.setStroke(new BasicStroke(2));
            lines.setColor(Color.GREEN);
			// Board 0
            lines.drawLine(80, 55, 290, 55);
            lines.drawLine(66, 95, 272, 95);
            lines.drawLine(49, 137, 252, 137);
            lines.drawLine(32, 179, 235, 179);
            lines.drawLine(20, 211, 220, 211);

            lines.drawLine(80, 55, 20, 211);
            lines.drawLine(130, 55, 70, 211);
            lines.drawLine(182, 55, 120, 211);
            lines.drawLine(234, 55, 170, 211);
            lines.drawLine(290, 55, 220, 211);

            // Board 1
            lines.drawLine(66, 270, 266, 270);
            lines.drawLine(50, 312, 248, 312);
            lines.drawLine(32, 354, 230, 354);
            lines.drawLine(20, 390, 216, 390);
            lines.drawLine(80, 234, 280, 234);

            lines.drawLine(80, 234, 20, 390);
            lines.drawLine(130, 234, 65, 390);
            lines.drawLine(180, 234, 115, 390);
            lines.drawLine(230, 234, 165, 390);
            lines.drawLine(280, 234, 215, 390);

            // Board 2
            lines.drawLine(95, 405, 290, 405);
            lines.drawLine(78, 445, 270, 445);
            lines.drawLine(56, 487, 248, 487);
            lines.drawLine(36, 529, 228, 529);
            lines.drawLine(20, 566, 210, 566);

            lines.drawLine(95, 405, 20, 566);
            lines.drawLine(140, 405, 65, 566);
            lines.drawLine(190, 405, 115, 566);
            lines.drawLine(240, 405, 165, 566);
            lines.drawLine(290, 405, 210, 566);

            // Board 3
            lines.drawLine(95, 585, 285, 585);
            lines.drawLine(78, 622, 268, 622);
            lines.drawLine(58, 664, 245, 664);
            lines.drawLine(36, 706, 226, 706);
            lines.drawLine(20, 744, 209, 744);

            lines.drawLine(95, 588, 20, 744);
            lines.drawLine(140, 588, 64, 744);
            lines.drawLine(190, 588, 116, 744);
            lines.drawLine(240, 588, 163, 744);
            lines.drawLine(285, 588, 209, 744);

			if(victory)
            {
				lines.setColor(Color.PINK);
				lines.drawLine(victoryStateButton[0].getBounds().x + 27, victoryStateButton[0].getBounds().y + 20,
				victoryStateButton[3].getBounds().x + 27, victoryStateButton[3].getBounds().y + 20);
			}
		}
	}


    private void createBoard() 
    {
        // Refactored setup board code...
        setup = new char[4][4][4];
		gameGrid = new XOButton[4][4][4];
		gridPanel = new GridPanel();
		textPanel = new JPanel();
        
        /*for board color */
        gridPanel.setBackground(Color.black);

		//Reset Button
		resetButton = new RoundedButton("Reset");
		resetButton.setBounds(350, 490, 120, 30);
		resetButton.addActionListener(new NewButtonListener());
		resetButton.setName("newGameBtn");

        //New Game Button
        newGameButton = new RoundedButton("New Game");
        newGameButton.setBounds(350, 530, 120, 30);
        newGameButton.addActionListener(new GameButtonListener());


		//X/O Radio Button
		yellowPawn = new JRadioButton("X", true);
		bluePawn = new JRadioButton("O");
		yellowPawn.setBounds(350, 420, 50, 50);
        yellowPawn.setBackground(Color.black);
        yellowPawn.setForeground(Color.yellow);
		bluePawn.setBounds(400, 420, 50, 50);
        bluePawn.setBackground(Color.black);
        bluePawn.setForeground(Color.blue);
		
		ButtonGroup chooseXO = new ButtonGroup();
		chooseXO.add(yellowPawn);
		chooseXO.add(bluePawn);
		
		pawnListener SelectionListener = new pawnListener();
		yellowPawn.addActionListener(SelectionListener);
		bluePawn.addActionListener(SelectionListener);
		
		//First move radio buttons
        head1 = new JLabel("Select First Move");
		head1.setFont(new Font("Roboto", Font.BOLD, 15));
        head1.setBounds(350, 150, 150, 40);
        head1.setForeground(Color.WHITE);

		playerFirst = new JRadioButton("Player", true);
		AI_First = new JRadioButton("AI");
		playerFirst.setBounds(350, 180, 150, 40);
        playerFirst.setBackground(Color.black);
        playerFirst.setForeground(Color.WHITE);
		AI_First.setBounds(350, 210, 150, 40);
        AI_First.setBackground(Color.black);
        AI_First.setForeground(Color.WHITE);
		
		ButtonGroup initialChoice = new ButtonGroup();
		initialChoice.add(AI_First);
		initialChoice.add(playerFirst);
		
		FirstListener initialListener = new FirstListener();
		AI_First.addActionListener(initialListener);
		playerFirst.addActionListener(initialListener);
		

		//Welcome title
		head0 = new JLabel("3D Tic-Tac-Toe Game");
		head0.setFont(new Font("Roboto", Font.BOLD, 20));
        head0.setBounds(180, 0, 250, 40);
        head0.setForeground(Color.white);

        //for heading
        head = new JLabel("3D Tic-Tac-Toe Game");
		head.setFont(new Font("Roboto", Font.BOLD, 20));
        head.setBounds(180, 0, 250, 40);
        head.setForeground(Color.white);


		//Current score panel
		score = new JLabel("You: " + playerScore + "   Computer: " + AI_Score);
		score.setFont(new Font("Roboto", Font.BOLD, 15));
        score.setBounds(350, 100, 190, 40);
        score.setForeground(Color.WHITE);

        victoryLabel = new JLabel();
        victoryLabel.setVisible(false); // Initially hidden
        victoryLabel.setBounds(350, 260, 200, 200);
        
		//Variables that shows balls where they placed within loops
		int rowTransition = 25;
		int initialRow = 89;
		int x_axis = 77;
		int y_axis = 49;
		int width = 50;
		int height = 40;

		//Variables to keep track where being placed
		int boardNumber = 0;
		int rowIndex = 0;
		int colIndex = 0;
		int slotNumber = 0;
		//Board loop
		for (int i = 0; i <= 3; i++){
			for (int j = 0; j <= 3; j++){
				for(int k = 0; k <= 3; k++){
					setup[i][j][k] = '-';
					gameGrid[i][j][k] = new XOButton();
					gameGrid[i][j][k].setFont(new Font("Roboto Bold", Font.ITALIC, 20));
					gameGrid[i][j][k].setText("");
					gameGrid[i][j][k].setContentAreaFilled(false);
					gameGrid[i][j][k].setBorderPainted(false);
					gameGrid[i][j][k].setFocusPainted(false);
					gameGrid[i][j][k].setBounds(x_axis, y_axis, width, height);
					gameGrid[i][j][k].setName(Integer.toString(slotNumber));
					gameGrid[i][j][k].gridBoard = boardNumber;
					gameGrid[i][j][k].gridRow = rowIndex;
					gameGrid[i][j][k].gridColumn = colIndex;
					gameGrid[i][j][k].addActionListener(this);


					colIndex++;
					slotNumber++;
					x_axis += 52;
					getContentPane().add(gameGrid[i][j][k]);
				}
				colIndex = 0;
				rowIndex++;
				x_axis = initialRow -= rowTransition;
				y_axis += 44.5;
			}
			rowIndex = 0;
			rowTransition = 24;
			initialRow = 82;
			boardNumber++;
			x_axis = initialRow;
			y_axis += 2.1;
		}
		gridPanel.setVisible(true);
		head0.setVisible(true);
		textPanel.add(head0);
		textPanel.add(score);
        add(head0);
        add(head);
        add(head1);
        add(score);
		add(yellowPawn);
		add(bluePawn);
		add(playerFirst);
		add(AI_First);
        add(victoryLabel);
		add(resetButton);
        add(newGameButton);
		add(gridPanel);
        
		setVisible(true);
        initiateFirstMoveIfNeeded();
    }


    private void initiateFirstMoveIfNeeded() {
        if (!playerTurn) {

            if (difficulty == 3) { // 3 represents INSANE difficulty
                AI_game();
            } else {
                AI_Random();
            }
        }
    }


    public void showDifficultySelection() 
    {
        JFrame difficultyFrame = new JFrame("Select Difficulty");
        difficultyFrame.setSize(600, 800);
        difficultyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        difficultyFrame.setLocationRelativeTo(null);
        difficultyFrame.setResizable(false); 

        JPanel difficultyPanel = new JPanel(null);
        difficultyPanel.setBackground(Color.BLACK);

        difficultyFrame.addComponentListener(new ComponentAdapter() {
            public void componentMoved(ComponentEvent e) {
                difficultyFrame.setLocationRelativeTo(null);
                
            }
        });


        head2 = new JLabel("3D - Tic Tac Toe");
        head2.setFont(new Font("Roboto", Font.BOLD, 20));
        head2.setBounds(220, 100, 200, 40);
        head2.setForeground(Color.WHITE);

        JButton easyButton = new RoundedButton("Easy");
        easyButton.setFont(new Font("Roboto", Font.BOLD, 20));
        easyButton.setBounds(190,250,200,40);
        easyButton.setBackground(Color.blue);
        easyButton.setForeground(Color.WHITE);

        JButton difficultButton = new RoundedButton("Difficult");
        difficultButton.setFont(new Font("Roboto", Font.BOLD, 20));
        difficultButton.setBounds(190,350,200,40);
        difficultButton.setBackground(Color.blue);
        difficultButton.setForeground(Color.WHITE);

        JButton insaneButton = new RoundedButton("Insane");
        insaneButton.setFont(new Font("Roboto", Font.BOLD, 20));
        insaneButton.setBounds(190,450,200,40);
        insaneButton.setBackground(Color.blue);
        insaneButton.setForeground(Color.WHITE);

        JButton rulesButton = new RoundedButton("Rules");
        rulesButton.setFont(new Font("Roboto", Font.BOLD, 15));
        rulesButton.setBounds(30,650,100,40);
        rulesButton.setBackground(Color.blue);
        rulesButton.setForeground(Color.WHITE);

        JButton quitButton = new RoundedButton("Quit");
        quitButton.setFont(new Font("Roboto", Font.BOLD, 15));
        quitButton.setBounds(450,650,100,40);
        quitButton.setBackground(Color.blue);
        quitButton.setForeground(Color.WHITE);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.addActionListener(e -> System.exit(0));

        rulesButton.addActionListener(new RulesButtonListener());
        easyButton.addActionListener(new DifficultySelectionListener(1, difficultyFrame)); // Set difficulty level for easy
        difficultButton.addActionListener(new DifficultySelectionListener(2, difficultyFrame)); // Set difficulty level for difficult
        insaneButton.addActionListener(new DifficultySelectionListener(3, difficultyFrame)); // Set difficulty level for insane

        difficultyPanel.add(easyButton);
        difficultyPanel.add(difficultButton);
        difficultyPanel.add(insaneButton);
        difficultyPanel.add(rulesButton);
        difficultyPanel.add(head2);
        difficultyPanel.add(quitButton);
        difficultyFrame.add(difficultyPanel);
        difficultyFrame.setVisible(true);
    }
    private class DifficultySelectionListener implements ActionListener 
    {
        private int selectedDifficulty;
        private JFrame difficultyFrame;

        public DifficultySelectionListener(int difficulty, JFrame difficultyFrame) 
        {
            this.selectedDifficulty = difficulty;
            this.difficultyFrame = difficultyFrame;
        }
    

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            difficulty = selectedDifficulty;

            if (difficulty == 1) {
                playLevel = 2;
            } else if (difficulty == 2) {
                playLevel = 4;
            } else {
                playLevel = 6;
            }

            
            difficultyFrame.dispose(); // Close the difficulty selection window
            App.this.setVisible(true); // Make the main game window visible
            createBoard(); // Setup the board with the selected difficulty
            
            
        }
    }


    private class RulesButtonListener implements ActionListener 
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            showRulesDialog();
        }
    }


    private class GameButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == newGameButton) {
                App.this.dispose(); // Dispose the current game window
    
                // Create a new instance of app the difficulty selection window
                SwingUtilities.invokeLater(() -> {
                    App newGame = new App();
                    newGame.showDifficultySelection();
                });
            } 
            else if (e.getSource() == rulesButton) {
                showRulesDialog();
            }
        }
    }
    
    
    private void showRulesDialog() 
    {
        String rulesMessage = "OBJECTIVE: To place four of your markers in a row horizontally, diagonally, or vertically while simultaneously \n trying to block your opponent from doing the same.\n" + //
            "\n" + //
            "GAMEPLAY: Click on any empty square and one of your markers will appear in that space..";

        JOptionPane.showMessageDialog(null, rulesMessage, "Game Rules", JOptionPane.INFORMATION_MESSAGE);
    }


    private class FirstListener implements ActionListener 
    {
        public void actionPerformed(ActionEvent arg0)
        {
            clearBoard();
            
            head0.setForeground(Color.GREEN);
            head0.setText("All the Best!");
            head0.setBounds(350, 70, 150, 40);

            if(AI_First.isSelected())
            {
                playerTurn = false;
                initiateFirstMoveIfNeeded();
            }

            else
            {
                playerTurn = true;
            }
		}

    }

    private class NewButtonListener implements ActionListener 
    {
        public void actionPerformed(ActionEvent arg0)
        {
            clearBoard();
            head0.setForeground(Color.GREEN);
            head0.setText("All the Best!");
            head0.setBounds(350, 70, 150, 40);
            if(!playerTurn)
            {
                if(difficulty == 3)
				AI_game();
                else
                AI_Random();
            }
        }
    }


    private class pawnListener implements ActionListener 
    {
        @Override
        public void actionPerformed(ActionEvent arg0)
        {
            clearBoard();	
            head0.setForeground(Color.GREEN);
            head0.setText("All the Best!");
            head0.setBounds(350, 70, 150, 40);
            if(yellowPawn.isSelected()){
                playerPawn = 'X';
                AI_Pawn = 'O';
            }
            else{
                playerPawn = 'O';
                AI_Pawn = 'X';
                }
            if(!playerTurn){
                if(difficulty == 3)
				AI_game();
                else
                AI_Random();
            }
        }

    }
    
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        // Refactored action performed code...
        XOButton button = (XOButton)e.getSource();
        setup[button.gridBoard][button.gridRow][button.gridColumn] = playerPawn;
        gameGrid[button.gridBoard][button.gridRow][button.gridColumn].setPlayer(playerPawn);
        gameGrid[button.gridBoard][button.gridRow][button.gridColumn].setEnabled(false);

        ActionStep newMove = new ActionStep();
        newMove.grid = button.gridBoard;
        newMove.row = button.gridRow;
        newMove.column = button.gridColumn;

        if(confirmVictory(playerPawn, newMove))
        {
            head0.setText("You beat me!");
            head0.setBounds(350, 70, 150, 40);
            head0.setForeground(Color.RED);
            playerScore++;
            victory = true;
            deactivateBoard();
            refreshScore();
        }
        else
        {
            AI_game();
        }
    }


    private int Predict(char c, int a, int b) 
    {
        // Refactored code...
        int sample_value1 = a;
        int sample_value2 = b;
        if(playLevelCounter <= playLevel)
        {
            playLevelCounter++;
            if(c == AI_Pawn)
            {
                int heuristicValue;
                ActionStep nextStep;
                for (int i = 0; i <= 3; i++){
                    for (int j = 0; j <= 3; j++){
                        for(int k = 0; k <= 3; k++){
                            if(setup[i][j][k] == '-'){
                                nextStep = new ActionStep();
                                nextStep.grid = i;
                                nextStep.row = j;
                                nextStep.column = k;
                                if(confirmVictory(AI_Pawn, nextStep)){
                                    setup[i][j][k] = '-';
                                    return 1000;
                                }
                                else{
                                    heuristicValue = Predict(playerPawn, sample_value1, sample_value2);
                                    if(heuristicValue > sample_value1){
                                        sample_value1 = heuristicValue;
                                        setup[i][j][k] = '-';
                                    }
                                    else{
                                        setup[i][j][k] = '-';
                                        }
                                }
                                if (sample_value1 >= sample_value2)
                                    break;
                            }
                        }
                    }
                }
                return sample_value1;
            }
            else
            {
                int heuristicValue;
                ActionStep nextStep;
                for (int i = 0; i <= 3; i++){
                    for (int j = 0; j <= 3; j++){
                        for(int k = 0; k <= 3; k++){
                            if(setup[i][j][k] == '-'){
                                nextStep = new ActionStep();
                                nextStep.grid = i;
                                nextStep.row = j;
                                nextStep.column = k;
                                if(confirmVictory(playerPawn, nextStep)){
                                    setup[i][j][k] = '-';
                                    return -1000;
                                }
                                else{
                                    heuristicValue = Predict(AI_Pawn, sample_value1, sample_value2);
                                    if(heuristicValue < sample_value2){
                                        sample_value2 = heuristicValue;
                                        setup[i][j][k] = '-';
                                    }
                                    else{
                                        setup[i][j][k] = '-';
                                    }
                                }
                                if (sample_value1 >= sample_value2)
                                    break;
                            }
                        }
                    }
                }
                
                return sample_value2;
            }
        }
        else
        {
            return heuristic();
        }
    }


    private int heuristic() 
    {
        return (evaluateAvailability(AI_Pawn) - evaluateAvailability(playerPawn));
    }

    private boolean confirmVictory(char c, ActionStep pos)
    {
        setup[pos.grid][pos.row][pos.column] = c;
        int[][] victories = 
        {
                {0, 1, 2,3}, {4, 5,6,7}, {8,9,10,11}, {12, 13, 14,15},
                {16, 17,18,19},{20,21, 22, 23}, {24, 25, 26,27},{28, 29, 30,31},
                {32, 33, 34,35},{36, 37, 38,39},{40, 41, 42,43},{44, 45, 46,47},
                {48, 49, 50,51},{52, 53, 54,55},{56, 57, 58,59},{60, 61, 62,63},
                {0,4,8,12}, {1, 5,9,13}, {2,6,10,14}, {3, 7, 11,15}, 
                {16, 20,24,28},{17,21, 25, 29}, {18, 22, 26,30},{19, 23, 27,31},
                {32, 36, 40,44},{33, 37, 41,45},{34, 38, 42,46},{35, 39, 43,47},
                {48, 52, 56,60},{49, 53, 57,61},{50, 54, 58,62},{51, 55, 59,63},
                {0, 5, 10,15}, {3, 6,9,12},{16,21,26,31}, {19, 22, 25,28}, 
                {32, 37,42,47},{35,38, 41, 44},{48, 53, 58,63},{51, 54, 57,60},
                {0, 16, 32,48}, {1, 17,33,49}, {2,18,34,50}, {3, 19, 35,51}, 
                {4, 20,36,52},{5,21, 37, 53}, {6, 22, 38,54},{7, 23, 39,55},
                {8, 24, 40,56},{9, 25, 41,57},{10, 26, 42,58},{11, 27, 43,59},
                {12, 28, 44,60},{13, 29, 45,61},{14, 30, 46,62},{15, 31, 47,63},
                {0, 20, 40,60}, {1, 21,41,61}, {2,22,42,62}, {3, 23, 43,63}, 
                {12, 24,36,48},{13,25, 37, 49}, {14, 26, 38,50},{15, 27, 39,51},
                {4, 21, 38,55},{8, 25, 42,59},{7, 22, 37,52},{11, 26, 41,56},
                {0, 17, 34,51},{3, 18, 33,48},{12, 29, 46,63},{15, 30, 45,60},
                {0, 21, 42,63},{3, 22, 41,60},{12, 25, 38,51},{15, 26, 37,48},
        };

        int[] gameBoard = new int[64];
        int counter = 0;
        for (int i = 0; i <= 3; i++)
        {
            for (int j = 0; j <= 3; j++)
            {
                for(int k = 0; k <= 3; k++)
                {
                    if(setup[i][j][k] == c)
                    {
                        gameBoard[counter] = 1;
                    }
                    else
                    {
                        gameBoard[counter] = 0;
                    }
                    counter++;
                }
            }
        }
        for (int i = 0; i <= 75; i++)
        {
            counter = 0;
            for (int j = 0; j <= 3; j++)
            {
                if(gameBoard[victories[i][j]] == 1)
                {
                    counter++;
                    victoryState[j] = victories[i][j];
                    if(counter == 4)
                    {
                        return true;
                    }
                }
            }
        }
        
        return false;
	}


    private int evaluateAvailability(char c)
    {
        int victory_count = 0;
        int[][] victories = 
        {
                {0, 1, 2,3}, {4, 5,6,7}, {8,9,10,11}, {12, 13, 14,15},
                {16, 17,18,19},{20,21, 22, 23}, {24, 25, 26,27},{28, 29, 30,31},
                {32, 33, 34,35},{36, 37, 38,39},{40, 41, 42,43},{44, 45, 46,47},
                {48, 49, 50,51},{52, 53, 54,55},{56, 57, 58,59},{60, 61, 62,63},
                {0,4,8,12}, {1, 5,9,13}, {2,6,10,14}, {3, 7, 11,15}, 
                {16, 20,24,28},{17,21, 25, 29}, {18, 22, 26,30},{19, 23, 27,31},
                {32, 36, 40,44},{33, 37, 41,45},{34, 38, 42,46},{35, 39, 43,47},
                {48, 52, 56,60},{49, 53, 57,61},{50, 54, 58,62},{51, 55, 59,63},
                {0, 5, 10,15}, {3, 6,9,12},{16,21,26,31}, {19, 22, 25,28}, 
                {32, 37,42,47},{35,38, 41, 44},{48, 53, 58,63},{51, 54, 57,60},
                {0, 16, 32,48}, {1, 17,33,49}, {2,18,34,50}, {3, 19, 35,51}, 
                {4, 20,36,52},{5,21, 37, 53}, {6, 22, 38,54},{7, 23, 39,55},
                {8, 24, 40,56},{9, 25, 41,57},{10, 26, 42,58},{11, 27, 43,59},
                {12, 28, 44,60},{13, 29, 45,61},{14, 30, 46,62},{15, 31, 47,63},
                {0, 20, 40,60}, {1, 21,41,61}, {2,22,42,62}, {3, 23, 43,63}, 
                {12, 24,36,48},{13,25, 37, 49}, {14, 26, 38,50},{15, 27, 39,51},
                {4, 21, 38,55},{8, 25, 42,59},{7, 22, 37,52},{11, 26, 41,56},
                {0, 17, 34,51},{3, 18, 33,48},{12, 29, 46,63},{15, 30, 45,60},
                {0, 21, 42,63},{3, 22, 41,60},{12, 25, 38,51},{15, 26, 37,48},
        };


        int[] gameBoard = new int[64];
        int counter = 0;
        for (int i = 0; i <= 3; i++)
        {
            for (int j = 0; j <= 3; j++)
            {
                for(int k = 0; k <= 3; k++)
                {
                    if(setup[i][j][k] == c || setup[i][j][k] == '-')
                        gameBoard[counter] = 1;
                    else	
                        gameBoard[counter] = 0;
                    counter++;
                }
            }
        }

        for (int i = 0; i <= 75; i++)
        {
            counter = 0;
            for (int j = 0; j <= 3; j++)
            {
                if(gameBoard[victories[i][j]] == 1)
                {
                    counter++;
                    victoryState[j] = victories[i][j];
                    if(counter == 4)
                    victory_count++;
                }
            }
        }
        
        return victory_count;
	}


    private void AI_Random()
    {
        Random random = new Random();
        int row = random.nextInt(4);
        int column = random.nextInt(4);
        int grid = random.nextInt(4);
        setup[grid][row][column] = AI_Pawn;
        gameGrid[grid][row][column].setPlayer(AI_Pawn);
        gameGrid[grid][row][column].setEnabled(false);
	}


    private void AI_game() 
    {
        // Refactored AI plays code...
        int bestScore;
        int heuristicValue;
        ActionStep nextStep;
        int bestScoreBoard = -1;
        int bestScoreRow = -1;
        int bestScoreColumn = -1;
        bestScore = -1000;
        check:
            for (int i = 0; i <= 3; i++){
                for (int j = 0; j <= 3; j++){
                    for(int k = 0; k <= 3; k++){
                        if(setup[i][j][k] == '-'){
                            nextStep = new ActionStep();
                            nextStep.grid = i;
                            nextStep.row = j;
                            nextStep.column = k;
                            if(confirmVictory(AI_Pawn, nextStep)){
                                setup[i][j][k] = AI_Pawn;
                                gameGrid[i][j][k].setPlayer(AI_Pawn);
                                head0.setText("I won!");
                                head0.setBounds(350, 70, 150, 40);
                                head0.setForeground(Color.RED);
                                victory = true;
                                AI_Score++;		
                                deactivateBoard();
                                refreshScore();
                                break check;
                            }
                            else{
                                if(difficulty != 1){
                                    heuristicValue = Predict(playerPawn, -1000, 1000);
                                    }
                                else{
                                    heuristicValue = heuristic();
                                    }

									playLevelCounter = 0;
                                if(heuristicValue >= bestScore){
                                    bestScore = heuristicValue;
                                    bestScoreBoard = i;
                                    bestScoreRow = j;
                                    bestScoreColumn = k;
                                    setup[i][j][k] = '-';
                                }
                                else{
                                    setup[i][j][k] = '-';	
                                }
                            }
                        }
                    }
                }
            }

        if(!victory)
        {
            setup[bestScoreBoard][bestScoreRow][bestScoreColumn] = AI_Pawn;
            gameGrid[bestScoreBoard][bestScoreRow][bestScoreColumn].setPlayer(AI_Pawn);
            gameGrid[bestScoreBoard][bestScoreRow][bestScoreColumn].setEnabled(false);
        }
    }


    public void refreshScore()
    {
	    score.setText("You: " + playerScore + "   Computer: " + AI_Score);
	}


    public void deactivateBoard()
    {
		int index = 0;
		for (int i = 0; i <= 3; i++){
			for (int j = 0; j <= 3; j++){
				for(int k = 0; k <= 3; k++){
					if(contains(victoryState, Integer.parseInt(gameGrid[i][j][k].getName()))){
						gameGrid[i][j][k].setEnabled(true);
						gameGrid[i][j][k].setForeground(Color.RED);
						victoryStateButton[index] = gameGrid[i][j][k];
						index++;
					}
					else{
						gameGrid[i][j][k].setEnabled(false);
					}
				}
			}
		}
		
		repaint();
		
	}
    

    public void clearBoard() {
        victory = false;
        playLevelCounter = 0;
        victoryState = new int[4]; 
    
        
        for (int i = 0; i < setup.length; i++) {
            for (int j = 0; j < setup[i].length; j++) {
                for (int k = 0; k < setup[i][j].length; k++) {
                    setup[i][j][k] = '-';
                    gameGrid[i][j][k].setPlayer('-'); 
                    gameGrid[i][j][k].setEnabled(true);
                }
            }
        }
    
        repaint(); // Repaint the entire board to reflect the reset state
        victoryLabel.setVisible(false); // Hide the victory label
        victoryLabel.setIcon(null); // Remove the icon
    }
    
    
    private boolean contains(int[] a, int k)
    {
        for(int i : a){
            if(k == i)
                return true;
            }
        return false;
    }		
}