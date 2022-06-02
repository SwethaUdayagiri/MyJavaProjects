import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.Random;

public class BubbleShooter extends JPanel implements ActionListener, KeyListener, Runnable {
    static boolean right=false;
    static boolean left=false;
    int ballx = 160;
    int bally = 218;
    int batx = 160;
    int baty = 245;
    int bubblex =20;
    int bubbley =20;
    int bubbleBreadth = 20;
    int bubbleHeight = 20;
    Rectangle Ball=new Rectangle(ballx,bally,8,8);
    Rectangle Bat=new Rectangle(batx,baty,40,7);
    Rectangle Bubble[][]=new Rectangle[4][14];

    int movex=-1;
    int movey=-1;
    int count=0;
    int score=0;

    boolean ballFallDown=false;
    boolean bubblesOver=false;
    String status;

    Color[] colors = new Color[] { Color.red, Color.GREEN, Color.BLUE, Color.orange };
    Color[][] randomColor=new Color[5][16];

    BubbleShooter(){
        for(int j=0;j<Bubble.length;j++){
            for(int i=0;i<Bubble[0].length;i++){
                randomColor[j][i] = colors[new Random().nextInt(4)];
            }
        }
    }
    public static void main(String arg[]){
        JFrame frame=new JFrame("Bubble shooter");
        BubbleShooter game=new BubbleShooter();
        JButton button=new JButton("Restart");
        frame.setSize(350,450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(game);
        frame.add(button, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        button.addActionListener(game);
        game.addKeyListener(game);
        game.setFocusable(true);
        Thread t=new Thread(game);
        t.start();
    }

    public void paint(Graphics g){
        g.setColor(Color.lightGray);
        g.fillRect(0,0,350,450);
        g.setColor(Color.blue);
        g.fillOval(Ball.x,Ball.y,Ball.width,Ball.height);
        g.setColor(Color.green);
        g.fill3DRect(Bat.x, Bat.y, Bat.width, Bat.height, true);
        g.setColor(Color.GRAY);
        g.fillRect(0, 251, 450, 200);
        g.drawRect(0, 0, 343, 250);
        g.setColor(Color.WHITE);
        g.setFont(new Font("serif",Font.BOLD,40));
        g.drawString("Score: "+score,100,350);
        for(int j=0;j<Bubble.length;j++) {
            for (int i = 0; i < Bubble[j].length; i++) {
                if (Bubble[j][i] != null) {
                    g.setColor(randomColor[j][i]);
                    g.fillRoundRect(Bubble[j][i].x, Bubble[j][i].y, Bubble[j][i].width,
                            Bubble[j][i].height, 20, 20);
                }
            }
        }

        if(ballFallDown==true||bubblesOver==true){
            Font f=new Font("Arial",Font.BOLD,20);
            g.setColor(Color.blue);
            g.setFont(f);
            g.drawString(status,70,150);
            ballFallDown=false;
            bubblesOver=false;
        }
    }

    @Override
    public void run() {
        createBubble();

        while (true) {
            for (int i = 0; i < Bubble.length; i++) {
                for (int j = 0; j < Bubble[i].length; j++) {
                    if (Bubble[i][j] != null) {
                        if (Bubble[i][j].intersects(Ball)) {
                            Bubble[i][j] = null;
                            score+=5;
                            movey--;
                            count++;
                        }
                    }
                }
            }
            if (count == (Bubble.length*Bubble[0].length)) {
                bubblesOver = true;
                status = "YOU WON THE GAME";
                repaint();
            }
            repaint();
            Ball.x += movex;
            Ball.y += movey;

            if (left == true) {
                Bat.x -= 3;
                right = false;
            }
            if (right == true) {
                Bat.x += 3;
                left = false;
            }
            if (Bat.x <= 4) {
                Bat.x = 4;
            } else if (Bat.x >= 298) {
                Bat.x = 298;
            }
            if (Ball.intersects(Bat)) {
                movey=-movey;
            }
            if (Ball.x <= 0 ||Ball.x>350) {
                movex = -movex;
            }
            if (Ball.y <= 0) {
                movey = -movey;
            }
            if (Ball.y >= 250) {
                ballFallDown = true;
                status = "YOU LOST THE GAME";
                repaint();
            }
            try {
                Thread.sleep(10);
            } catch (Exception ex) {
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) {
            left = true;
        }
        if(keyCode == KeyEvent.VK_RIGHT) {
            right = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) {
            left = false;
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            right = false;
        }
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        String str=e.getActionCommand();
        if(str.equals("Restart")){
            this.restart();
        }
    }
    public void restart(){
        requestFocus(true);
        initializeVariables();
        createBubble();
        repaint();
    }
    public void initializeVariables(){
        ballx=160;
        bally=218;
        batx=160;
        baty=245;
        bubblex=20;
        bubbley=20;
        Ball=new Rectangle(ballx,bally,8,8);
        Bat=new Rectangle(batx,baty,40,7);
        Bubble=new Rectangle[4][13];
        movex=-1;
        movey=-1;
        count=0;
        score=0;
        ballFallDown=false;
        bubblesOver=false;
        status=null;
    }

    public void createBubble(){
        int temp=bubblex;
        for(int j=0;j<Bubble.length;j++) {
            bubblex=temp;
            for (int i = 0; i < Bubble[j].length; i++) {
                Bubble[j][i] = new Rectangle(bubblex, bubbley, bubbleBreadth, bubbleHeight);
                bubblex += (bubbleBreadth + 1);
            }
            bubbley+=(bubbleHeight+1);
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}
}

