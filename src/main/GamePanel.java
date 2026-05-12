package main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Random;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.PrintWriter;

import javax.swing.JPanel;

import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
	
	//SCREEN SETTINGS
	final int originalTileSize = 16;
	final int scale = 2;
	
	final public int tileSize  = originalTileSize * scale;
	
	final public int maxScreenCol = 25;
	final public int maxScreenRow = 25;
	final public int screenWidth = tileSize * maxScreenCol;
	final public int screenHeight = tileSize * maxScreenRow;
	
	// WORLD SETTINGS
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	public final int worldWidth = tileSize * maxWorldCol;
	public final int worldHeight = tileSize * maxWorldRow;
	public int mapTileNum[][];
	public int powerTileNum[][];
	public int[][] matrix = {
		    {120, 120},
		    {screenWidth - 160, 120},
		    {120, screenHeight - 160},
		    {screenWidth - 160, screenHeight - 160}
		};
	public Player myPlayer;
	public Dictionary<String, Integer> powerUps = new Hashtable<>();

    
	public int powerUpTimer = 0;
	public int numberOfPowerUps = 2;
	Random r= new Random();
	
	// FPS
	int FPS = 60;
	
	Thread gameThread;
	public CollisionChecker checker = new CollisionChecker(this);
	//public Player player = new Player(this, keyH);
	public ArrayList<Player> players;
	
	public TileManager tileManager = new TileManager(this);
		
	public GamePanel(ArrayList<Player> players, KeyHandler keyH) {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.players = players;
		powerUps.put("shield", 0);
		powerUps.put("damage", 1);
		tileManager.randomPowerUp = 0;
		
		this.addKeyListener(keyH); // will send keyboard input events to keyH
		this.setFocusable(true); // will allow this gamePanel object to receive keyboard inputs
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	@Override
	public void run() {
		
		double drawInterval = 1000000000/FPS; // 1 billion nano seconds = 1 sec
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		
		while (gameThread != null) {
			
			currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime) / drawInterval; // so within a (currenttime - lasttime) duration, ilang frames idadagdag kay delta
			lastTime = currentTime;
			
			if (delta >= 1) {
				// update information e.g., character position
				update();
				
				// draw screen w/ updated information
				repaint(); // para ma call paintComponent
				delta--;
			}
			
			
		}
		
	}
	
	public void update() {
		myPlayer.update();
	}
	
	
	public void paintComponent(Graphics g) {
	
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		
		tileManager.draw(g2);
		for (Player p : players) {
		    p.draw(g2);
		    if (p.shield || p.damage) {
		    	tileManager.drawPowerUp = false;
		    }
		}
		
		if (myPlayer.shield || myPlayer.damage) {
			tileManager.drawPowerUp = false;
		}
		
		if (!tileManager.drawPowerUp) {
			powerUpTimer++;
		}
		
		if (powerUpTimer == 700) {
			tileManager.randomPowerUp = (tileManager.randomPowerUp + 1) % numberOfPowerUps;
			powerUpTimer = 0;
			tileManager.drawPowerUp = true;
		}
		myPlayer.draw(g2);
		myPlayer.drawHearts(g2);
		//player.draw(g2);
		
		g2.dispose(); // parang na frefree up lang yung ginamit natin pang drawing
	}
};


























