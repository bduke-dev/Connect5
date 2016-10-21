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
        }
    }

    private class GameView extends JFrame {
        //the playin board
        SpinnerNumberModel xCountModel;
        SpinnerNumberModel yCountModel;
        JPanel gameWindow = new JPanel(new BorderLayout());
        private BoardPanel boardPanel = new BoardPanel();
        JLabel statusLbl;


        public GameView() {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setBounds(100, 100, 1700, 950);
            this.setContentPane(gameWindow);

            JButton startbtn = new JButton("Start");
            JButton resetBtn = new JButton("Reset");
            resetBtn.addActionListener(new ResetButton());
            JPanel subPanel1 = new JPanel();
            subPanel1.add(startbtn);
            subPanel1.add(resetBtn);
            gameWindow.add(subPanel1, BorderLayout.PAGE_START);

            JPanel subPanel2 = new JPanel();
            gameWindow.add(subPanel2, BorderLayout.CENTER);

            xCountModel = new SpinnerNumberModel(0, 0, (int) widthModel.getNumber(), 1);
            yCountModel = new SpinnerNumberModel(0, 0, (int) lengthModel.getNumber(), 1);
            JSpinner xSpinner = new JSpinner(xCountModel);
            JSpinner ySpinner = new JSpinner(yCountModel);
            JLabel xlbl = new JLabel("x");
            JLabel ylbl = new JLabel("y");
            JPanel subPanel3 = new JPanel();
            subPanel3.add(xlbl);
            subPanel3.add(xSpinner);
            subPanel3.add(ylbl);
            subPanel3.add(ySpinner);

            JButton commitBtn = new JButton("Commit Move");
            commitBtn.addActionListener(new commitMove());
            Box subPanel4 = new Box(BoxLayout.PAGE_AXIS);
            subPanel4.add(subPanel3);
            subPanel4.add(commitBtn);
            gameWindow.add(subPanel4, BorderLayout.EAST);

            statusLbl = new JLabel("New Game");
            statusLbl.setFont(new Font("Default", Font.BOLD, 20));
            gameWindow.add(statusLbl, BorderLayout.PAGE_END);

            JLabel background1 = new JLabel(new ImageIcon("src/ing/Connect5Key.png"));
            gameWindow.add(background1, BorderLayout.WEST);

            boardPanel.setBackground(Color.CYAN);
            gameWindow.add(boardPanel, BorderLayout.CENTER);


            setVisible(true);
        }

        private class BoardPanel extends JPanel{
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                int z = (int) heightModel.getNumber();
                int y = (int) widthModel.getNumber();
                int x = (int) lengthModel.getNumber();
                int xVal = 15*x-15;
                int yVal = (15*y*z)+(15*(z-2));
                int counter = 1;

                if (z>5) {
                    xVal = (15*x*2)+15; //we need 2 per row b/c too tall
                    if (z%2 != 0) yVal = yVal/2 + 30;
                    else yVal = yVal/2;
                }
                for (int i = 0; i < z; i++) {
                    for (int j = 0; j < y; j++) {
                        for (int k = 0; k < x; k++) {
                            g.setColor(Color.BLACK);
                            g.drawRect(xVal, yVal, 15, 15);
                            switch (board.getBoard()[i][j][k]){
                                case 1: {
                                    g.setColor(Color.RED);
                                    g.fillRect(xVal, yVal, 15, 15);
                                    break;
                                }
                                case 2: {
                                    g.setColor(Color.GREEN);
                                    g.fillRect(xVal, yVal, 15, 15);
                                    break;
                                }
                                case 3: {
                                    g.setColor(Color.BLUE);
                                    g.fillRect(xVal, yVal, 15, 15);
                                    break;
                                }
                                case 4: {
                                    g.setColor(Color.YELLOW);
                                    g.fillRect(xVal, yVal, 15, 15);
                                    break;
                                }
                                case 5: {
                                    g.setColor(Color.ORANGE);
                                    g.fillRect(xVal, yVal, 15, 15);
                                    break;
                                }
                            }
                            xVal = xVal - 15;
                        }
                        yVal = yVal - 15;
                        if (z<=5) xVal = 15*x-15;
                        else {
                            if (counter != 2) xVal = (15*x*2)+15;
                            else xVal = (xVal + (15*x));
                        }
                    }
                    if (z<=5) yVal = yVal - 15;
                    else {
                        if (counter != 2) {
                            yVal = yVal + (15*y);
                            xVal = (xVal - (15*x)) - 15;
                        }
                        else {
                            xVal = (15*x*2)+15;
                            yVal = yVal-15;
                        }
                    }
                    counter++;
                    if (counter > 2) counter = 1;
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

        private class commitMove implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    board.playerz[board.getCurrentPlayer() - 1].move((int) xCountModel.getNumber(), (int) yCountModel.getNumber());
                    statusLbl.setText("It's " + board.getCurrentPlayer() + "'s turn");
                }
                catch (Exception e){
                    statusLbl.setText("Invalid Move");
                }
                boardPanel.paintComponent(boardPanel.getGraphics());
            }
        }
    }
}
