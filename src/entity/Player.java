package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.*;

public class Player extends Entity {
	
	GamePanel gp;
	KeyHandler keyH;
	int bulletCounter = 0;
	ArrayList<int[]> barriers = new ArrayList<>();
	public int playerID;
	
	int test = 0;
	
	ArrayList<Bullet> bullets = new ArrayList<>();
	boolean switcher = false;
	
	boolean hit = false;
	int hitCounter = 0;
	int lives = 3;
	boolean decreaseLife = true;
	boolean invisible = false;
	boolean playerMove = false;
	public boolean up = false;
	public boolean down = false;
	public boolean right = false;
	public boolean left = false;
	public boolean space = false;
	public PrintWriter out;
	
	public Player(GamePanel gp, KeyHandler keyH, int worldX, int worldY) {
		this.gp = gp;
		this.keyH = keyH;
		
			
		this.worldX = worldX;
		this.worldY = worldY;
	
		
		// sinubtract by gp.tileSize/2 dahil if wala yan, hindi centered yung mismong character pero yung top left nung hitbox nung character
		
		solidArea = new Rectangle();
		solidArea.x = 16;
		solidArea.y = 8;
		solidArea.width = gp.tileSize/2;
		solidArea.height = gp.tileSize/2;
		
		setDefaultValues();
		getPlayerImage();
		barriers();
	}
	
	public void drawHearts(Graphics g2) {
		int x = gp.screenWidth/2;
		int y = gp.screenHeight/20;
		
		try {
			BufferedImage temp = ImageIO.read(getClass().getResourceAsStream("/misce/heart.png"));
			int spacing = gp.tileSize + 5; 
			for (int i = 0; i < lives; i++) {
			    int horizontalPos = (x + (i * spacing)) - 40;
			    g2.drawImage(temp, horizontalPos, y, gp.tileSize, gp.tileSize, null);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void setDefaultValues() {
		speed = 4;
		direction = "right";
	}
	
	public void getPlayerImage() {
		try {
			
		up1 = ImageIO.read(getClass().getResourceAsStream("/player/green_car_n.png"));
		up2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_2.png"));
		down1 = ImageIO.read(getClass().getResourceAsStream("/player/green_car_s.png"));
		down2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_2.png"));
		left1 = ImageIO.read(getClass().getResourceAsStream("/player/green_car_e.png"));
		left2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_2.png"));
		right1 = ImageIO.read(getClass().getResourceAsStream("/player/green_car_w.png"));
		right2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_2.png"));
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// this will update the player's position in terms of the world map, not the window/screen map
	public void updateFromOther() {
		if (up) {
			direction = "up";
			playerMove = true;
		}
		if (down) {
			direction = "down";
			playerMove = true;
		}
		if (right) {
			direction = "right";
			playerMove = true;
		}
		if (left) {
			direction = "left";
			playerMove = true;
		}
		if (space) {
			if (bulletCounter % 5 == 0)
			{
				bullets.add(new Bullet(this.gp, worldX, worldY, direction));
			}
			bulletCounter++;
		}
		
		
		collisionOn = false;
		gp.checker.checkTile(this);
		
		if (!collisionOn && playerMove)
		{
			switch(direction)
			{
			case "up":
				worldY -= speed;
				break;
			case "down":
				worldY += speed;
				break;
			case "left":
				worldX -= speed;
				break;
			case "right":
				worldX += speed;
				break;	
			}
		}
		
		playerMove = false;
		
		
//		if (up || down || right || left) {
//			spriteCounter++;
//			if (spriteCounter > 20) {
//				spriteNum = (spriteNum + 1) % 2;
//				spriteCounter = 0;
//			}
//		}
	}
	
	public void update() {
		if (lives == 0) {
			return;
		}
		
		if (keyH.upPressed) {
			out.println("up" + " " + playerID);
			direction = "up";
			playerMove = true;

			//worldY -= speed;
		}
		if (keyH.downPressed) {
			out.println("down" + " " + playerID);
			direction = "down";
			playerMove = true;
			//worldY += speed;
		}
		if (keyH.rightPressed || right) {
			out.println("right" + " " + playerID);
			direction = "right";
			playerMove = true;
			//worldX += speed;
		}
		if (keyH.leftPressed) {
			out.println("left" + " " + playerID);
			direction = "left";
			playerMove = true;
			//worldX -= speed;
		}
		if (keyH.spacePressed) {
			out.println("space" + " " + playerID);
			if (bulletCounter % 5 == 0)
			{
				bullets.add(new Bullet(this.gp, worldX, worldY, direction));
			}
			bulletCounter++;
		}
		
		
		collisionOn = false;
		gp.checker.checkTile(this);
		
		if (!collisionOn && playerMove)
		{
			switch(direction)
			{
			case "up":
				worldY -= speed;
				break;
			case "down":
				worldY += speed;
				break;
			case "left":
				worldX -= speed;
				break;
			case "right":
				worldX += speed;
				break;	
			}
		}
		
		playerMove = false;
		
		
//		if (keyH.upPressed || keyH.downPressed || keyH.rightPressed || keyH.leftPressed) {
//			spriteCounter++;
//			if (spriteCounter > 20) {
//				spriteNum = (spriteNum + 1) % 2;
//				spriteCounter = 0;
//			}
//		}
		
	}
	
	public void barriers()
	{
		for (int i = 0; i < gp.maxScreenRow; i++)
		{
			for (int j = 0; j < gp.maxScreenCol; j++)
			{
				int tileNum = gp.mapTileNum[i][j];
				if (tileNum == 1)
				{
					barriers.add(new int[]{j, i});
					System.out.println(j + " " + i);
				}
			}
		}
	}
	
	public void draw(Graphics g2) {
			
		//g2.setColor(Color.red);
		//g2.fillRect(x, y, gp.tileSize, gp.tileSize);
		BufferedImage image = null;
		
		switch(direction) {
		case "up":
			if (spriteNum == 1) {
				image = up1;
			} else {
				image = up2;
			}
			
			break;
		case "down":
			if (spriteNum == 1) {
				image = down1;
			} else {
				image = down2;
			}
			break;
		case "left":
			if (spriteNum == 1) {
				image = left1;
			} else {
				image = left2;
			}
			break;
		case "right":
			if (spriteNum == 1) {
				image = right1;
			} else {
				image = right2;
			}
		}

		Graphics2D g3 = (Graphics2D) g2;
		for (int i = 0; i < bullets.size(); i++)
		{
			Bullet b = bullets.get(i);
			try {
		        if (b == null) continue; 

		        b.update();
		        b.draw(g2);
		        b.playerHit(this);
		    } catch (Exception e) {
		        System.out.println("Bullet error: " + e.getMessage());
		    } 
		}
		if (hit && hitCounter < 500)
		{
			if (decreaseLife)
			{
				lives -= 1;
				decreaseLife = false;
			}
			if (hitCounter % 20 == 0)
			{
				invisible = !invisible;
			}
			
			if (!invisible)
			{
				g2.drawImage(image, worldX, worldY, gp.tileSize, gp.tileSize, null);
			}
			
			hitCounter++;
		}
		else 
		{
			hitCounter = 0;
			hit = false;
			decreaseLife = true;
			// if car, 50 by 34 gamitin
			g2.drawImage(image, worldX, worldY, 40, 40, null);
		}
			
	}
}





