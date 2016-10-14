import sun.util.locale.provider.AvailableLanguageTags;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by brandon on 10/14/16.
 */

public class GUI  extends JFrame{
    //settings window
    Connect5 board;
    JPanel settingsWindow = new JPanel();
    SpinnerNumberModel heightModel;
    SpinnerNumberModel widthModel;
    SpinnerNumberModel lengthModel;
    SpinnerNumberModel playerCountModel;

    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable(){
            public void run(){
                try{
                    GUI gui = new GUI();
                    gui.setVisible(true);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public GUI(){
        this.setBounds(100, 100, 450, 300);
        this.setContentPane(settingsWindow);
        settingsWindow.setLayout(new BoxLayout(settingsWindow, BoxLayout.PAGE_AXIS));
        heightModel = new SpinnerNumberModel( 3, 3, 10, 1);
        widthModel = new SpinnerNumberModel( 3, 3, 10, 1);
        lengthModel = new SpinnerNumberModel( 3, 3, 10, 1);
        JSpinner height = new JSpinner(heightModel);
        JSpinner width = new JSpinner(widthModel);
        JSpinner length = new JSpinner(lengthModel);
        JLabel heightLbl = new JLabel("Height of Board");
        JLabel widthLbl = new JLabel("Width of Board");
        JLabel lengthLbl = new JLabel("Length of Board");
        JLabel playerLbl = new JLabel("Number of PLayers");
        settingsWindow.add(heightLbl);
        settingsWindow.add(height);
        settingsWindow.add(widthLbl);
        settingsWindow.add(width);
        settingsWindow.add(lengthLbl);
        settingsWindow.add(length);
        playerCountModel = new SpinnerNumberModel( 2, 2, 5, 1);
        JSpinner playerCount = new JSpinner(playerCountModel);
        settingsWindow.add(playerLbl);
        settingsWindow.add(playerCount);
        JButton submit = new JButton("Select These Options");
        submit.addActionListener(new SubmitButton());
        settingsWindow.add(submit);
    }

    private class SubmitButton implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent arg0){
            board = new Connect5( (int) lengthModel.getNumber(), (int) widthModel.getNumber(), (int) heightModel.getNumber(), (int) playerCountModel.getNumber());
        }
    }

    private class GameView extends JFrame{
        //the playign board
        JPanel gameWindow = new JPanel(new BorderLayout());
        public GameView(){
            this.setBounds(100, 100, 450, 300);
            this.setContentPane(gameWindow);
        }
    }

}
