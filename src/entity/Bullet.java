package entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Bullet extends Entity{
	GamePanel gp;
	

	int test = 0;
	boolean switcher = false;
	BufferedImage image = right1;
	int playerTileSize = 32;
	
	
	public Bullet(GamePanel gp, int worldX, int worldY, String direction) {
		this.gp = gp;
		this.worldX = worldX;
		this.worldY = worldY;
		this.direction = direction;
		
		getPlayerImage();
		setDefaultValues();
	}
	
	public void getPlayerImage() {
		try {
			
		right1 = ImageIO.read(getClass().getResourceAsStream("/player/bullet_right.png"));
		left1 = ImageIO.read(getClass().getResourceAsStream("/player/bullet_left.png"));
		up1 = ImageIO.read(getClass().getResourceAsStream("/player/bullet_up.png"));
		down1 = ImageIO.read(getClass().getResourceAsStream("/player/bullet_down.png"));
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setDefaultValues() {
		speed = 6;
	}
	
	public void update() {
		if (direction == "right")
		{
			image = right1;
			worldX += speed;
		}
		else if (direction == "left")
		{
			image = left1;
			worldX -= speed;
		}
		else if (direction == "up")
		{
			image = up1;
			worldY -= speed;
		}
		else
		{
			image = down1;
			worldY += speed;
		}
		
		if (worldX < 0 || worldX > gp.screenWidth)
		{
			gp.myPlayer.bullets.remove(this);
		}
		
		if (worldY < 0 || worldY > gp.screenHeight)
		{
			gp.myPlayer.bullets.remove(this);
		}
		
	}
	
	public void playerHit(Player shooter)
	{
		
		if (gp.myPlayer != shooter)
		{
			Player temp = gp.myPlayer;
			int tempXPos = temp.worldX;
			int tempYPos = temp.worldY;
			int tempBulletX = worldX + playerTileSize/2;
			int tempBulletY = worldY + playerTileSize/2;
			
		
			if (tempBulletX >= tempXPos && tempBulletX <= tempXPos + playerTileSize)
			{
				if (tempBulletY >= tempYPos && tempBulletY <= tempYPos + playerTileSize)
	        	{
	        		System.out.println("hit " + temp.playerID);
	        		gp.myPlayer.bullets.remove(this);
	        		shooter.bullets.remove(this);
	        		if (!gp.myPlayer.shield) 
	        		{
	        			temp.hit = true;
	        		}
	        	}
			}
			
			
		}
		
		for (Player player : gp.players) {
			if (player == shooter)
			{
				continue;
			}
			int xPos = player.worldX;
			int yPos = player.worldY;
			int bulletX = worldX + playerTileSize/2;
			int bulletY = worldY + playerTileSize/2;
			
		
			if (bulletX >= xPos && bulletX <= xPos + playerTileSize)
			{
				if (bulletY >= yPos && bulletY <= yPos + playerTileSize)
            	{
            		System.out.println("hit " + player.playerID);
            		gp.myPlayer.bullets.remove(this);
            		shooter.bullets.remove(this);
            		if (!player.shield)
            		{
            			player.hit = true;
            		}
            	}
			}
			
			
        }
	}
	
	public void draw(Graphics g2) {
		
		g2.drawImage(image, worldX, worldY, playerTileSize, playerTileSize, null);
	}
		
	
}
