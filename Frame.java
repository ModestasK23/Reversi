package com.company;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Frame extends JPanel implements MouseListener {
    static int[][] board = new int[8][8];
    static int Boardsizeint = 8;
    static JPanel panel = new JPanel() ;
    static int turn = 1;
    static int black = 0;
    static int white = 0;
    static int free = 0;
    static int blue = 0;
    static int fontX = 10;
    static int fontY = 498;
    static int noblue = 0;
    static boolean noOneWin = false;


    public static void main(String[] args) {
        start();
        SwingUtilities.invokeLater(() -> new Frame());
    }
    static void start(){
        board[(Boardsizeint /2)-1][(Boardsizeint /2)-1] = 1;
        board[Boardsizeint /2][(Boardsizeint /2)-1] = 2;
        board[(Boardsizeint /2)-1][Boardsizeint /2] = 2;
        board[Boardsizeint /2][Boardsizeint /2] = 1;
        help();
        count();
    }

    public Frame() {
        // Lango nustatymai
        JFrame frame = new JFrame();
        frame.setTitle("Reversi");
        frame.setLocationRelativeTo(null);
        frame.setLocation(450, 150);
        panel.setPreferredSize(new Dimension(500, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().setBackground( new Color(18, 199, 24));

        // JMenuBar
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem newGame = new JMenuItem("New Game");
        JCheckBoxMenuItem help = new JCheckBoxMenuItem("Enable Help");
        JMenuItem exitGame = new JMenuItem("Close Game");
        menuBar.add(file);
        file.add(newGame);
        file.add(help);
        help.setSelected(true);
        file.addSeparator();
        file.add(exitGame);
        newGame.addActionListener(e -> {
            board = new int[Boardsizeint][Boardsizeint];
            turn = 1;
            start();
            count();
            panel.repaint();
        });
        help.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.repaint();
            }

        });
        exitGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }

        });
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int i = 0; i < Boardsizeint; i++)
                    for (int j = 0; j < Boardsizeint; j++) {
                        g.setColor(new Color(18, 199, 24));
                        g.fillRect( j * 60,   i * 60, 60, 60);
                        g.setColor(Color.black);
                        g.drawRect( j * 60,  i * 60, 60, 60);
                    }
                for(int i = 0; i < board.length; i++){
                    for(int j = 0; j < board[i].length; j++){
                        switch (board[i][j]) {
                            case 0:   break;

                            case 1:
                                g.setColor(Color.BLACK);
                                g.fillOval(5+i * 60, 5+j * 60, 50, 50);
                                break;
                            case 2:
                                g.setColor(Color.WHITE);
                                g.fillOval(5+i * 60, 5+j * 60, 50, 50);
                                break;
                            case -1:
                                if(help.getState()){
                                    g.setColor(Color.BLUE);
                                    g.fillOval(20+i * 60, 20+j * 60, 25, 25);
                                }
                                break;

                        }
                    }
                }
                g.setColor(Color.BLACK);
                g.setFont(new Font ("Courier New", Font.BOLD, 15));
                if(free == 0){
                    if(black > white){
                        g.drawString("Black win     Black = " + black + "  White = " + white, fontX, fontY);
                    }else if(black == white || noOneWin){
                        g.drawString("No one win     Black = " + black + "  White = " + white, fontX, fontY);
                    }else{
                        g.drawString("White win     Black = " + black + "  White = " + white, fontX, fontY);
                    }
                }else{
                    if(turn == 1){
                        g.drawString("Black Turn     Black = " + black + "  White = " + white, fontX, fontY);
                    }else{
                        g.drawString("White Turn     Black = " + black + "  White = " + white, fontX, fontY);
                    }
                }
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(500, 500);
            }
        };

        frame.add(panel);
        frame.setJMenuBar(menuBar);
        panel.addMouseListener(this);
        frame.pack();
        // Display frame after all components added
        frame.setVisible(true);

    }

    boolean accepted(int i,int j){
        if(i < Boardsizeint && j < Boardsizeint){
            return board[i][j] == -1;
        }
        return false;
    }

    static void help(){
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                if(board[i][j] == -1){
                    board[i][j] = 0;
                }
            } }

        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                if(board[i][j] == turn){
                    check(i,j);
                }}}
        count();
        if(blue == 0 && free != 0){
            noblue ++;
            if(turn == 1){
                turn = 2;
            }else{
                turn = 1;
            }
            if(noblue > 1){
                noOneWin = true;
            }
            if(!noOneWin){
                help();
            }
        }else{
            noblue = 0;
        }

    }

    static void check(int i,int j){
        int see ;
        int oI = i;
        int oJ = j;
        boolean done = false;
        if(turn == 1){
            see = 2;
        }else{
            see = 1;
        }

        //up
        while(i >= 0 && i < Boardsizeint && j-1 >= 0 && j-1 < Boardsizeint && !done){
            if(board[i][j-1] == see){
                if(j - 2 >= 0 && j - 2 < Boardsizeint){
                    if(board[i][j-2] == 0){
                        board[i][j-2] = -1;
                        done = true;
                    }else if(board[i][j-2] == see){
                        j = j-1;
                    }else{
                        done = true;
                    }
                }else{
                    done = true;
                }
            }else{
                done = true;
            }
        }

        i = oI;
        j = oJ;
        done = false;

        //up-right
        while(i+1 >= 0 && i+1 < Boardsizeint && j-1 >= 0 && j-1 < Boardsizeint && !done){
            if(board[i+1][j-1] == see){
                if(i + 2 < Boardsizeint && j - 2 >= 0 && j - 2 < Boardsizeint){
                    if(board[i+2][j-2] == 0){
                        board[i+2][j-2] = -1;
                        done = true;
                    }else if(board[i+2][j-2] == see){
                        j = j-1;
                        i = i+1;
                    }else{
                        done = true;
                    }
                }else{
                    done = true;
                }
            }else{
                done = true;
            }
        }
        i = oI;
        j = oJ;
        done = false;


        //right
        while(i+1 >= 0 && i+1 < Boardsizeint && j >= 0 && j < Boardsizeint && !done){
            if(board[i+1][j] == see){
                if(i + 2 >= 0 && i + 2 < Boardsizeint && j >= 0){
                    if(board[i+2][j] == 0){
                        board[i+2][j] = -1;
                        done = true;
                    }else if(board[i+2][j] == see){
                        i = i+1;
                    }else{
                        done = true;
                    }
                }else{
                    done = true;
                }
            }else{
                done = true;
            }
        }

        i = oI;
        j = oJ;
        done = false;


        //right-down
        while(i+1 >= 0 && i+1 < Boardsizeint && j+1 >= 0 && j+1 < Boardsizeint && !done){
            if(board[i+1][j+1] == see){
                if(i+2 >= 0 && i+2 < Boardsizeint && j+2 >= 0 && j+2 < Boardsizeint){
                    if(board[i+2][j+2] == 0){
                        board[i+2][j+2] = -1;
                        done = true;
                    }else if(board[i+2][j+2] == see){
                        i = i+1;
                        j = j+1;
                    }else{
                        done = true;
                    }
                }else{
                    done = true;
                }
            }else{
                done = true;
            }
        }

        i = oI;
        j = oJ;
        done = false;

        //down
        while(i >= 0 && i < Boardsizeint && j+1 >= 0 && j+1 < Boardsizeint && !done){
            if(board[i][j+1] == see){
                if(i >= 0 && j + 2 >= 0 && j + 2 < Boardsizeint){
                    if(board[i][j+2] == 0){
                        board[i][j+2] = -1;
                        done = true;
                    }else if(board[i][j+2] == see){
                        j = j+1;
                    }else{
                        done = true;
                    }
                }else{
                    done = true;
                }
            }else{
                done = true;
            }
        }

        i = oI;
        j = oJ;
        done = false;

        //down-left
        while(i-1 >= 0 && i-1 < Boardsizeint && j+1 >= 0 && j+1 < Boardsizeint && !done){
            if(board[i-1][j+1] == see){
                if(i-2 >= 0 && i-2 < Boardsizeint && j+2 >= 0 && j+2 < Boardsizeint){
                    if(board[i-2][j+2] == 0){
                        board[i-2][j+2] = -1;
                        done = true;
                    }else if(board[i-2][j+2] == see){
                        j = j+1;
                        i = i-1;
                    }else{
                        done = true;
                    }
                }else{
                    done = true;
                }
            }else{
                done = true;
            }
        }

        i = oI;
        j = oJ;
        done = false;

        //left
        while(i-1 >= 0 && i-1  < Boardsizeint && j >= 0 && j < Boardsizeint && !done){
            if(board[i-1][j] == see){
                if(i - 2 >= 0 && i - 2 < Boardsizeint && j >= 0){
                    if(board[i-2][j] == 0){
                        board[i-2][j] = -1;
                        done = true;
                    }else if(board[i-2][j] == see){
                        i = i-1;
                    }else{
                        done = true;
                    }
                }else{
                    done = true;
                }
            }else{
                done = true;
            }
        }

        i = oI;
        j = oJ;
        done = false;

        //left-up
        while(i-1 >= 0 && i-1 < Boardsizeint && j-1 >= 0 && j-1 < Boardsizeint && !done){
            if(board[i-1][j-1] == see){
                if(i-2 >= 0 && i-2 < Boardsizeint && j-2 >= 0 && j-2 < Boardsizeint){
                    if(board[i-2][j-2] == 0){
                        board[i-2][j-2] = -1;
                        done = true;
                    }else if(board[i-2][j-2] == see){
                        i = i-1;
                        j = j-1;
                    }else{
                        done = true;
                    }
                }else{
                    done = true;
                }
            }else{
                done = true;
            }
        }

    }

    static void fillAll(int i,int j){
        int see ;
        int oI = i;
        int oJ = j;
        boolean done = false;
        if(turn == 1){
            see = 2;
        }else{
            see = 1;
        }

        //up-fillall
        while(i >= 0 && i < Boardsizeint && j-1 >= 0 && j-1 < Boardsizeint && !done){
            if(board[i][j-1] == see){
                if(j - 2 >= 0 && j - 2 < Boardsizeint){
                    if(board[i][j-2] == turn){
                        for(int k = j-1;k <= oJ;k++){
                            board[i][k] = turn;
                        }
                        done = true;
                    }else if(board[i][j-2] == see){
                        j = j-1;
                    }else{
                        done = true;
                    }
                }else{
                    done = true;
                }
            }else{
                done = true;
            }
        }

        i = oI;
        j = oJ;
        done = false;

        //up-right
        while(i+1 >= 0 && i+1 < Boardsizeint && j-1 >= 0 && j-1 < Boardsizeint && !done){
            if(board[i+1][j-1] == see){
                if(i + 2 < Boardsizeint && j - 2 >= 0 && j - 2 < Boardsizeint){
                    if(board[i+2][j-2] == turn){
                        int m = i+1;
                        for(int k = j-1;k < oJ;k++){
                            board[m][k] = turn;
                            m--;
                        }
                        done = true;
                    }else if(board[i+2][j-2] == see){
                        j = j-1;
                        i = i+1;
                    }else{
                        done = true;
                    }
                }else{
                    done = true;
                }
            }else{
                done = true;
            }
        }
        i = oI;
        j = oJ;
        done = false;

        //right
        while(i+1 >= 0 && i+1 < Boardsizeint && j >= 0 && j < Boardsizeint && !done){
            if(board[i+1][j] == see){
                if(i + 2 >= 0 && i + 2 < Boardsizeint && j >= 0){
                    if(board[i+2][j] == turn){
                        for(int k = i+1;k > oI;k--){
                            board[k][j] = turn;
                          //  System.out.println("right" + k + " " + j);
                        }
                        done = true;
                    }else if(board[i+2][j] == see){
                        i = i+1;
                    }else{
                        done = true;
                    }
                }else{
                    done = true;
                }
            }else{
                done = true;
            }
        }

        i = oI;
        j = oJ;
        done = false;

        //right-down
        while(i+1 >= 0 && i+1 < Boardsizeint && j+1 >= 0 && j+1 < Boardsizeint && !done){
            if(board[i+1][j+1] == see){
                if(i+2 >= 0 && i+2 < Boardsizeint && j+2 >= 0 && j+2 < Boardsizeint){
                    if(board[i+2][j+2] == turn){
                        int m = i+1;
                        for(int k = j+1;k > oJ;k--){
                           // System.out.println("right-down" + m + " " + k);
                            board[m][k] = turn;
                            m--;
                        }
                        done = true;
                    }else if(board[i+2][j+2] == see){
                        i = i+1;
                        j = j+1;
                    }else{
                        done = true;
                    }
                }else{
                    done = true;
                }
            }else{
                done = true;
            }
        }

        i = oI;
        j = oJ;
        done = false;

        //down
        while(i >= 0 && i < Boardsizeint && j+1 >= 0 && j+1 < Boardsizeint && !done){
            if(board[i][j+1] == see){
                if(i >= 0 && j + 2 >= 0 && j + 2 < Boardsizeint){
                    if(board[i][j+2] == turn){
                        for(int k = j+1;k > oJ;k--){
                            board[i][k] = turn;
                           // System.out.println("down" + i + " " + k);
                        }
                        done = true;
                    }else if(board[i][j+2] == see){
                        j = j+1;
                    }else{
                        done = true;
                    }
                }else{
                    done = true;
                }
            }else{
                done = true;
            }
        }

        i = oI;
        j = oJ;
        done = false;

        //down-left
        while(i-1 >= 0 && i-1 < Boardsizeint && j+1 >= 0 && j+1 < Boardsizeint && !done){
            if(board[i-1][j+1] == see){
                if(i-2 >= 0 && i-2 < Boardsizeint && j+2 >= 0 && j+2 < Boardsizeint){
                    if(board[i-2][j+2] == turn){
                        int m = i-1;
                        for(int k = j+1;k > oJ;k--){
                           // System.out.println("down-left" + m + " " + k);
                            board[m][k] = turn;
                            m++;
                        }
                        done = true;
                    }else if(board[i-2][j+2] == see){
                        j = j+1;
                        i = i-1;
                    }else{
                        done = true;
                    }
                }else{
                    done = true;
                }
            }else{
                done = true;
            }
        }

        i = oI;
        j = oJ;
        done = false;

        //left
        while(i-1 >= 0 && i-1  < Boardsizeint && j >= 0 && j < Boardsizeint && !done){
            if(board[i-1][j] == see){
                if(i - 2 >= 0 && i - 2 < Boardsizeint && j >= 0){
                    if(board[i-2][j] == turn){
                        for(int k = i-2;k < oI;k++){
                            board[k][j] = turn;
                           // System.out.println("left " + k + " " + j);
                        }
                        done = true;
                    }else if(board[i-2][j] == see){
                        i = i-1;
                    }else{
                        done = true;
                    }
                }else{
                    done = true;
                }
            }else{
                done = true;
            }
        }

        i = oI;
        j = oJ;
        done = false;

        //left-up
        while(i-1 >= 0 && i-1 < Boardsizeint && j-1 >= 0 && j-1 < Boardsizeint && !done){
            if(board[i-1][j-1] == see){
                if(i-2 >= 0 && i-2 < Boardsizeint && j-2 >= 0 && j-2 < Boardsizeint){
                    if(board[i-2][j-2] == turn){
                        int m = i-1;
                        for(int k = j-1;k < oJ;k++){
                           // System.out.println("down" + m + " " + k);
                            board[m][k] = turn;
                            m++;
                        }
                        done = true;
                    }else if(board[i-2][j-2] == see){
                        i = i-1;
                        j = j-1;
                    }else{
                        done = true;
                    }
                }else{
                    done = true;
                }
            }else{
                done = true;
            }
        }

    }

    static void count(){
        black = 0;
        white = 0;
        free = 0;
        blue = 0;
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                if(board[i][j] == 1){
                    black++;
                }
                if(board[i][j] == 2){
                    white++;
                }
                if(board[i][j] == 0){
                    free++;
                }
                if(board[i][j] == -1){
                    blue++;
                }
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        int x, y, i , j ;
        x = arg0.getX();
        y = arg0.getY();
        i = x/60;
        j = y/60;
        if(accepted(i,j)){
            if(turn == 1){
                board[i][j]=turn;
                fillAll(i, j);
                turn = 2;
            }else{
                board[i][j]=turn;
                fillAll(i, j);
                turn = 1;
            }
            help();

            panel.repaint();
        }
    }
    @Override
    public void mouseEntered(MouseEvent arg0) {

    }
    @Override
    public void mouseExited(MouseEvent arg0) {
    }
    @Override
    public void mousePressed(MouseEvent arg0) {
    }
    @Override
    public void mouseReleased(MouseEvent arg0) {
    }
}
