import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Lincoln Patton, Brandon Duke
 * @version 10/14/16
 */

public class GUI  extends JFrame {
    //settings window
    AI Steve = new AI(null, 0);
    Connect5 board;
    JPanel settingsWindow = new JPanel();
    SpinnerNumberModel heightModel;
    SpinnerNumberModel widthModel;
    SpinnerNumberModel lengthModel;
    SpinnerNumberModel playerCountModel;
    SpinnerNumberModel aiCountModel;


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GUI gui = new GUI();
                    gui.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public GUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(100, 100, 450, 300);
        this.setContentPane(settingsWindow);
        settingsWindow.setLayout(new BoxLayout(settingsWindow, BoxLayout.PAGE_AXIS));
        heightModel = new SpinnerNumberModel(3, 3, 10, 1);
        widthModel = new SpinnerNumberModel(3, 3, 10, 1);
        lengthModel = new SpinnerNumberModel(3, 3, 10, 1);
        JSpinner height = new JSpinner(heightModel);
        JSpinner width = new JSpinner(widthModel);
        JSpinner length = new JSpinner(lengthModel);
        JLabel heightLbl = new JLabel("Height of Board");
        JLabel widthLbl = new JLabel("Width of Board");
        JLabel lengthLbl = new JLabel("Length of Board");
        JLabel playerLbl = new JLabel("Number of PLayers");
        JLabel aiLbl = new JLabel("Number of AIs, any more than players results in all AI players");
        settingsWindow.add(heightLbl);
        settingsWindow.add(height);
        settingsWindow.add(widthLbl);
        settingsWindow.add(width);
        settingsWindow.add(lengthLbl);
        settingsWindow.add(length);
        playerCountModel = new SpinnerNumberModel(2, 2, 5, 1);
        aiCountModel = new SpinnerNumberModel(0, 0, 5, 1);
        JSpinner playerCount = new JSpinner(playerCountModel);
        JSpinner aiCount = new JSpinner(aiCountModel);
        settingsWindow.add(playerLbl);
        settingsWindow.add(playerCount);
        settingsWindow.add(aiLbl);
        settingsWindow.add(aiCount);
        JButton submit = new JButton("Select These Options");
        submit.addActionListener(new SubmitButton());
        settingsWindow.add(submit);
    }

    private class SubmitButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            board = new Connect5((int) lengthModel.getNumber(), (int) widthModel.getNumber(), (int) heightModel.getNumber(), (int) playerCountModel.getNumber());
            dispose();
            GameView boardGUI = new GameView();
            Steve = new AI(board.getBoard() , (int) playerCountModel.getNumber());
            Steve.start();
        }
    }

    private class GameView extends JFrame {
        //the playin board
        SpinnerNumberModel xCountModel;
        SpinnerNumberModel yCountModel;
        JPanel gameWindow = new JPanel(new BorderLayout());
        private BoardPanel boardPanel = new BoardPanel();
        JLabel statusLbl, playerLbl;
        JSpinner xSpinner, ySpinner;
        JButton commitBtn;


        public GameView() {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setBounds(100, 100, 1700, 950);
            this.setContentPane(gameWindow);

            JButton startbtn = new JButton("Start");
            JButton resetBtn = new JButton("Reset");
            resetBtn.addActionListener(new ResetButton());
            startbtn.addActionListener(new StartButton());
            JPanel subPanel1 = new JPanel();
            subPanel1.add(startbtn);
            subPanel1.add(resetBtn);
            gameWindow.add(subPanel1, BorderLayout.PAGE_START);

            JPanel subPanel2 = new JPanel();
            gameWindow.add(subPanel2, BorderLayout.CENTER);

            xCountModel = new SpinnerNumberModel(0, 0, (int) widthModel.getNumber()-1, 1);
            yCountModel = new SpinnerNumberModel(0, 0, (int) lengthModel.getNumber()-1, 1);
            xSpinner = new JSpinner(xCountModel);
            ySpinner = new JSpinner(yCountModel);
            xSpinner.setPreferredSize(new Dimension(50,25));
            ySpinner.setPreferredSize(new Dimension(50,25));
            JLabel xlbl = new JLabel("x");
            JLabel ylbl = new JLabel("y");
            JPanel subPanel3 = new JPanel();
            subPanel3.add(xlbl);
            subPanel3.add(xSpinner);
            subPanel3.add(ylbl);
            subPanel3.add(ySpinner);

            commitBtn = new JButton("Commit Move");
            commitBtn.addActionListener(new commitMove());
            Box subPanel4 = new Box(BoxLayout.PAGE_AXIS);
            subPanel4.add(subPanel3);
            subPanel4.add(commitBtn);
            gameWindow.add(subPanel4, BorderLayout.EAST);

            playerLbl = new JLabel("New Game");
            statusLbl = new JLabel("Status: No New Messages");
            statusLbl.setFont(new Font("Default", Font.BOLD, 20));
            playerLbl.setFont(new Font("Default", Font.BOLD, 20));
            JPanel subPanel5 = new JPanel();
            subPanel5.add(playerLbl);
            subPanel5.add(Box.createRigidArea(new Dimension(200, 0)));
            subPanel5.add(statusLbl);
            gameWindow.add(subPanel5, BorderLayout.PAGE_END);

            JLabel background1 = new JLabel(new ImageIcon("src/ing/Connect5Key.png"));
            gameWindow.add(background1, BorderLayout.WEST);

            boardPanel.setBackground(Color.CYAN);
            gameWindow.add(boardPanel, BorderLayout.CENTER);

            xSpinner.setEnabled(false);
            ySpinner.setEnabled(false);
            commitBtn.setEnabled(false);

            setVisible(true);
        }

        private class BoardPanel extends JPanel{
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                int z = (int) heightModel.getNumber();
                int y = (int) widthModel.getNumber();
                int x = (int) lengthModel.getNumber();
                int counter = 1;
                int scaleFactor = 0;
                int n = x + y + z;

                if (n<=30) scaleFactor = 15;
                if (n<=20) scaleFactor = 20;
                if (n<=10) scaleFactor = 30;


                int xVal = scaleFactor*x-scaleFactor;
                int yVal = (scaleFactor*y*z)+(scaleFactor*(z-1));

                if (z>5) {
                    xVal = (scaleFactor*x*2)+scaleFactor; //we need 2 per row b/c too tall
                    if (z%2 != 0) yVal = yVal/2 + (2*scaleFactor);
                    else yVal = yVal/2;
                }
                for (int i = 0; i < z; i++) {
                    for (int j = 0; j < y; j++) {
                        for (int k = 0; k < x; k++) {
                            g.setColor(Color.BLACK);
                            g.drawRect(xVal, yVal, scaleFactor, scaleFactor);
                            switch (board.getBoard()[i][j][k]){
                                case 1: {
                                    g.setColor(Color.RED);
                                    g.fillRect(xVal, yVal, scaleFactor, scaleFactor);
                                    break;
                                }
                                case 2: {
                                    g.setColor(Color.GREEN);
                                    g.fillRect(xVal, yVal, scaleFactor, scaleFactor);
                                    break;
                                }
                                case 3: {
                                    g.setColor(Color.BLUE);
                                    g.fillRect(xVal, yVal, scaleFactor, scaleFactor);
                                    break;
                                }
                                case 4: {
                                    g.setColor(Color.YELLOW);
                                    g.fillRect(xVal, yVal, scaleFactor, scaleFactor);
                                    break;
                                }
                                case 5: {
                                    g.setColor(Color.ORANGE);
                                    g.fillRect(xVal, yVal, scaleFactor, scaleFactor);
                                    break;
                                }
                            }
                            try {
                                for (int l = 0; l < 5; l++) {
                                    if (k == board.getWinTiles()[l][0] && j == board.getWinTiles()[l][1] && i == board.getWinTiles()[l][2]) {
                                        g.setColor(Color.MAGENTA);
                                        g.fillRect(xVal, yVal, scaleFactor, scaleFactor);
                                    }
                                }
                            }
                            catch (Exception e){}

                            xVal = xVal - scaleFactor;
                        }
                        yVal = yVal - scaleFactor;
                        if (z<=5) xVal = scaleFactor*x-scaleFactor;
                        else {
                            if (counter != 2) xVal = (scaleFactor*x*2)+scaleFactor;
                            else xVal = (xVal + (scaleFactor*x));
                        }
                    }
                    if (z>5) g.drawString("Floor: " + i,xVal-scaleFactor,yVal+scaleFactor);
                    if (z<=5) yVal = yVal - scaleFactor;
                    else {
                        if (counter != 2) {
                            yVal = yVal + (scaleFactor*y);
                            xVal = (xVal - (scaleFactor*x)) - scaleFactor;
                        }
                        else {
                            xVal = (scaleFactor*x*2)+scaleFactor;
                            yVal = yVal-scaleFactor;
                        }
                    }
                    counter++;
                    if (counter > 2) counter = 1;
                    if (z<=5) g.drawString("Floor: " + i,xVal-scaleFactor,yVal+(2*scaleFactor));
                }
            }
        }

        private class ResetButton implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                dispose();
                GUI gui = new GUI();
                gui.setVisible(true);
            }
        }

        private class StartButton implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                xSpinner.setEnabled(true);
                ySpinner.setEnabled(true);
                commitBtn.setEnabled(true);
            }
        }

        private class commitMove implements ActionListener {
            int winner;
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    if (!board.getCurrentPlayer().move((int) xCountModel.getNumber(), (int) yCountModel.getNumber())) {
                        playerLbl.setText("It's " + board.getCurrentPlayer().playerNumber + "'s turn");
                        statusLbl.setText("Status: Game in Progress");
                        winner = board.getCurrentPlayer().playerNumber;
                        if ((int) board.getCurrentPlayer().playerNumber >= (int) aiCountModel.getNumber() - (int) playerCountModel.getNumber()) {
                            Integer[] moveCoords = Steve.getMove(45);
                            board.getCurrentPlayer().move(moveCoords[0],moveCoords[1]);
                        }
                        Steve.update(board.getBoard());
                    }
                    else{
                        playerLbl.setText("WINNER: Player " + winner);
                        statusLbl.setText("Status: GAME OVER");
                        xSpinner.setEnabled(false);
                        ySpinner.setEnabled(false);
                        commitBtn.setEnabled(false);
                    }
                }
                catch (Exception e){
                    statusLbl.setText("Status: Invalid Move");
                }
                boardPanel.paintComponent(boardPanel.getGraphics());
            }
        }
    }
}
