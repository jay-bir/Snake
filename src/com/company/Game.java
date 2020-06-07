package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Random;


public class Game extends Canvas implements Runnable {
    //resolution
    private final int tileSize = 8;
    private final int tilesX = 20;
    private final int tilesY = 15;

    //graphics
    private final Display display;
    private final BufferedImage image = new BufferedImage(tilesX*tileSize, tilesY*tileSize, BufferedImage.TYPE_INT_RGB);
    private final int[] pixels =((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    //frame
    private final JFrame frame;
    private final JLabel scDisplay;
    private final JLabel pickedUp;


    //functionality
    private boolean running = false;


    //inGame
    private int score;
    private Direction direction;
    private Piece[][] tiles;// = new Piece[tilesY][tilesX];
    private Snake snake;
    private double difficulty;
    private ArrayList<Integer> empty;
    private Upgrade upgrade;
    private Piece fruit;




    public Game(){
        difficulty = 5D;

        score = 0;
        int scale = 8;
        var d = new Dimension(tilesX*tileSize* scale, tilesY*tileSize* scale);
        setPreferredSize(d);

        Keyboard key = new Keyboard();
        this.addKeyListener(key);

        display = new Display(tilesX,tilesY,tileSize);
        reset();



        frame = new JFrame();
        frame.setLayout(new GridBagLayout());
        frame.add(this, new GBC(1,0,9,1));

        JLabel scBanner = new JLabel("SCORE", JLabel.CENTER);
        scBanner.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
        scBanner.setBackground(Color.BLACK);
        scBanner.setForeground(Color.WHITE);
        scBanner.setOpaque(true);
        scBanner.setPreferredSize(new Dimension(tileSize*5*scale, tileSize*scale));
        frame.add(scBanner, new GBC(0,0));

        scDisplay = new JLabel(Integer.toString(score), JLabel.CENTER);
        scDisplay.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
        scDisplay.setBackground(Color.BLACK);
        scDisplay.setForeground(Color.WHITE);
        scDisplay.setOpaque(true);
        scDisplay.setPreferredSize(new Dimension(tileSize*5*scale, tileSize*2*scale));
        frame.add(scDisplay, new GBC(0,1));

        pickedUp = new JLabel("",JLabel.CENTER);
        pickedUp.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
        pickedUp.setBackground(Color.BLACK);
        pickedUp.setBackground(Color.BLACK);
        pickedUp.setOpaque(true);
        pickedUp.setPreferredSize(new Dimension(tileSize*5*scale, tileSize*scale));
        frame.add(pickedUp,new GBC(0,2));

        JButton s = new JButton("START");
        s.addActionListener(e->this.start());
        s.setPreferredSize(new Dimension(tileSize*5*scale,tileSize*2*scale));
        frame.add(s,new GBC(0,3));

        JButton st = new JButton("STOP");
        st.addActionListener(e->stop());
        st.setPreferredSize(new Dimension(tileSize*5*scale,tileSize*2*scale));
        frame.add(st,new GBC(0,4));

        JButton r = new JButton("RESET");
        r.addActionListener(e->reset());
        r.setPreferredSize(new Dimension(tileSize*5*scale,tileSize*scale));
        frame.add(r, new GBC(0,5));

        JButton easy = new JButton("EASY");
        easy.setPreferredSize(new Dimension(tileSize*5*scale, tileSize*2*scale));
        frame.add(easy, new GBC(0,6));
        easy.addActionListener(e->difficulty = 2);

        JButton normal = new JButton("NORMAL");
        normal.setPreferredSize(new Dimension(tileSize*5*scale, tileSize*2*scale));
        frame.add(normal,new GBC(0,7));
        normal.addActionListener(e->difficulty = 5);

        JButton hard = new JButton("HARD");
        hard.setPreferredSize(new Dimension(tileSize*5*scale,tileSize*2*scale));
        frame.add(hard,new GBC(0,8));
        hard.addActionListener(e->difficulty = 10);
    }



    public void render(){
        var bs = getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            bs = getBufferStrategy();
        }
        var g = bs.getDrawGraphics();
        display.render(tiles);
        for(int i =0; i < pixels.length; i++) {
            this.pixels[i] = display.getPixelAt(i);
        }

        g.drawImage(image, 0,0,getWidth(), getHeight(),null);

        g.dispose();
        bs.show();

    }




    public void update(){
        try {
            Piece nextTile = tiles[snake.getHead().getY() + direction.getY()][snake.getHead().getX() + direction.getX()];
            if(nextTile != null) {
                if(nextTile.isEatable){
                    score += nextTile.getValue();
                    Toolkit.getDefaultToolkit().beep();
                    scDisplay.setText(Integer.toString(score));
                    if(nextTile.getName().equals("upgrade")){
                        snake.addPart(direction);

                        if((empty = findEmpty(tiles)) != null){
                            upgrade = generateUpgrade(empty);
                            empty.remove(Integer.valueOf(upgrade.getX()+upgrade.getY()*tilesX));
                            if(empty != null){
                                fruit = generateFruit(empty);
                            }
                        }
                        else{
                            stop();
                            JOptionPane.showMessageDialog(null,"You Win");
                        }
                    }
                    else {
                        snake.move(direction);
                        pickedUp.setBackground(new Color(fruit.getColour()));
                        pickedUp.setText(fruit.getName());
                        fruit = null;
                    }
                }
                else{
                    stop();
                    JOptionPane.showMessageDialog(null,"You Lose");
                }
            }
            else snake.move(direction);

            for(int y = 0; y < tilesY; y++){
                for(int x = 0; x < tilesX; x++){
                    tiles[y][x] = null;
                }
            }

            tiles[upgrade.getY()][upgrade.getX()] = upgrade;
            if(fruit != null)  tiles[fruit.getY()][fruit.getX()] = fruit;

            for(SnakePart part: snake.getSnakeParts()){
                tiles[part.getY()][part.getX()] = part;
            }


        }
        catch(ArrayIndexOutOfBoundsException e){
            stop();
            JOptionPane.showMessageDialog(null,"You Lose");
        }

    }

    public ArrayList<Integer> findEmpty(Piece[][] t){
         ArrayList<Integer> empty = new ArrayList<>();

        for(int y = 0; y < tilesY; y++){
            for(int x = 0; x < tilesX; x++){
                if(t[y][x] == null) empty.add(x + y*tilesX);
            }
        }
        return empty;
    }

    public Upgrade generateUpgrade(ArrayList<Integer> i){
        Random random = new Random();
        int pick = random.nextInt(i.size());
        return new Upgrade(i.get(pick) % tilesX,i.get(pick)/tilesX);
    }

    public Piece generateFruit(ArrayList<Integer> i){
        Piece[] fruits = new Piece[]{new Piece(150,0xff3333,"Apple"),
                                     new Piece(250,0x00ff00,"Watermelon"),
                                     new Piece(300,0x0000ff," Blue Berry")};
        Piece fruit = null;
        Random random = new Random();
        double chance = random.nextInt(101) + difficulty * 2.5;
        if(chance > 75){
            fruit = fruits[random.nextInt(3)];
            int pick = random.nextInt(i.size());
            fruit.setCoordinates(i.get(pick) % tilesX, i.get(pick)/tilesX);
        }
        return fruit;
    }

    public synchronized void start(){
        running= true;
        Thread thread = new Thread(this, "Game");
        thread.start();


    }
    public synchronized void stop(){
        running = false;
    }

    public void reset(){
        direction = Direction.LEFT;
        tiles = new Piece[tilesY][tilesX];
        snake = new Snake();

        empty = new ArrayList<>();
        for(int y = 0; y < tilesY; y++){
            for(int x = 0; x < tilesX; x++){
                empty.add(x + y*tilesY);
            }
        }
        for(SnakePart part: snake.getSnakeParts()){
            empty.remove(part.getX() + part.getY()*tilesY);
        }
        upgrade = generateUpgrade(empty);
    }

    public void run(){
        long timer = System.currentTimeMillis();
        int updt = 0;
        int frames = 0;
        long prevTime = System.nanoTime();
        double period = 0D;
        while(running){
            long now = System.nanoTime();
            period+= (now - prevTime)*difficulty/1_000_000_000;
            prevTime = now;
            while(period >= 1) {
                updt++;
                update();
                period--;
            }
            render();
            frames++;

            if(System.currentTimeMillis() - timer > 1000){
                timer = System.currentTimeMillis();
                frame.setTitle("FPS: " + frames + " || UPS: " + updt);
                System.out.println(Integer.toHexString(snake.getHead().getColour()));
                frames = 0;
                updt = 0;
            }
        }

    }
    public static void main(String[] args) {
        Game game = new Game();
        game.frame.pack();
        game.frame.setLocationRelativeTo(null);
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setResizable(false);
        game.frame.setVisible(true);
    }

    public class Keyboard implements KeyListener {


        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_UP:{
                    if(!isContradictory(Direction.UP))
                    direction = Direction.UP;
                    break;
                }
                case KeyEvent.VK_DOWN:{
                    if(!isContradictory(Direction.DOWN))
                    direction = Direction.DOWN;
                    break;
                }
                case KeyEvent.VK_LEFT:{
                    if(!isContradictory(Direction.LEFT))
                    direction = Direction.LEFT;
                    break;
                }
                case KeyEvent.VK_RIGHT:{
                    if(!isContradictory(Direction.RIGHT))
                    direction = Direction.RIGHT;
                    break;
                }
                case KeyEvent.VK_ESCAPE:{
                    stop();
                    break;
                }
                case KeyEvent.VK_SPACE:{
                    if(!running)
                    start();
                    break;
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
    private boolean isContradictory(Direction d){
        return snake.getHead().getY() + d.getY() == snake.getSnakeParts().get(1).getY() &&
                snake.getHead().getX() + d.getX() == snake.getSnakeParts().get(1).getX();
    }

}
