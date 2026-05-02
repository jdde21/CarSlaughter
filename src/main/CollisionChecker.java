package main;

import entity.Entity;

public class CollisionChecker {
	
	GamePanel gp;
	
	public CollisionChecker(GamePanel gp)
	{
		this.gp = gp;
	}
	
	public void checkTile(Entity entity)
	{
		int playerTile = gp.tileSize;
		int entityLeftWorldX = entity.worldX + entity.solidArea.x;
		int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
		int entityTopWorldY = entity.worldY + entity.solidArea.y;
		int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;
		
		int entityLeftCol = entityLeftWorldX/playerTile;
		int entityRightCol = entityRightWorldX/playerTile;
		int entityTopRow = entityTopWorldY/playerTile;
		int entityBottomRow = entityBottomWorldY/playerTile;
		
		int tileNum1, tileNum2;
		
		switch(entity.direction)
		{	
			case "up":
				entityTopRow = (entityTopWorldY - entity.speed)/playerTile;
				tileNum1 = gp.tileManager.mapTileNum[entityTopRow][entityLeftCol];
				tileNum2 = gp.tileManager.mapTileNum[entityTopRow][entityRightCol];
				
				if (gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision)
				{
					entity.collisionOn = true;
				}
				
				break;
			case "down":
				entityBottomRow = (entityBottomWorldY + entity.speed)/playerTile;
				tileNum1 = gp.tileManager.mapTileNum[entityBottomRow][entityLeftCol];
				tileNum2 = gp.tileManager.mapTileNum[entityBottomRow][entityRightCol];
				
				if (gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision)
				{
					entity.collisionOn = true;
				}
				break;
			case "left":
				entityLeftCol = (entityLeftWorldX - entity.speed)/playerTile;
				tileNum1 = gp.tileManager.mapTileNum[entityTopRow][entityLeftCol];
				tileNum2 = gp.tileManager.mapTileNum[entityBottomRow][entityLeftCol];
				
				if (gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision)
				{
					entity.collisionOn = true;
				}
				break;
			case "right":
				entityRightCol = (entityRightWorldX + entity.speed)/playerTile;
				tileNum1 = gp.tileManager.mapTileNum[entityTopRow][entityRightCol];
				tileNum2 = gp.tileManager.mapTileNum[entityBottomRow][entityRightCol];
				
				if (gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision)
				{
					entity.collisionOn = true;
				}
				break;
		}
	}
	
	
}
