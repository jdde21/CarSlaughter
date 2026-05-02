package tile;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager {
	
	GamePanel gp;
	public Tile[] tile;
	public int mapTileNum[][];
	boolean once = true;
	
	public TileManager(GamePanel gp) {
		this.gp = gp;
		
		
		tile = new Tile[90];
		gp.mapTileNum = new int[gp.maxScreenRow][gp.maxScreenCol];
		this.mapTileNum = gp.mapTileNum;
		
		getTileImage();
		loadMap("/maps/final_map.txt");
	}
	
	public void getTileImage() {
		try {
			
			
			for (int i = 1; i <= 89; i++) {
			    tile[i] = new Tile();
			    String fileName = String.format("/tiles/%03d.png", i);
			    System.out.println(fileName);
			    tile[i].image = ImageIO.read(getClass().getResourceAsStream(fileName));
			    if ((i >= 17 && i <= 27) || (i == 32 || i == 39 || i == 55 || i == 64) || (i >= 64 && i <= 74) || (i == 27 || i == 36 || i == 44 || i == 59))
			    {
			    	tile[i].collision = true;
			    }
			}
			
//			tile[0] = new Tile();
//			tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));
//			
//			tile[1] = new Tile();
//			tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
//			tile[1].collision = true;
//			
//			tile[2] = new Tile();
//			tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
//			tile[2].collision = true;
//			
//			tile[3] = new Tile();
//			tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/earth.png"));
//			
//			tile[4] = new Tile();
//			tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));
//			
//			tile[5] = new Tile();
//			tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/sand.png"));

			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadMap(String map) {
		try {
			InputStream is = getClass().getResourceAsStream(map);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0;
			System.out.println(gp.maxScreenCol);
			System.out.println(gp.maxScreenRow);
			for (int i = 0; i < gp.maxScreenRow; i++) {
				String line = br.readLine();
				String numbers[] = line.split(" ");
				for (int j = 0; j < gp.maxScreenCol; j++) {
					System.out.print(numbers[j] + " ");
					mapTileNum[i][j] = Integer.parseInt(numbers[j]);
				}
				System.out.println(" yow");
			}
			
		} catch (Exception e) {
			
		}
	}
	
	public void draw(Graphics g2) {		
		
		// implementing camera
		for (int i = 0; i < gp.maxScreenRow; i++) {
			for (int j = 0; j < gp.maxScreenCol; j++) {
				
				int tileNum = mapTileNum[i][j];
				
				if (tileNum == 2 && once)
				{
					System.out.println("x: " + j * gp.tileSize + "y: " + i * gp.tileSize);
				}
				g2.drawImage(tile[tileNum].image, j * gp.tileSize, i * gp.tileSize, gp.tileSize, gp.tileSize, null);
//				int worldX = j * gp.tileSize;
//				int worldY = i * gp.tileSize;
//				
//				// need na muna natin yung difference in distance nung tile and nung player in the world map
//				
//				int screenX = (worldX - gp.player.worldX) + gp.player.screenX;
//				int screenY = (worldY - gp.player.worldY) + gp.player.screenY;
//				if (screenX >= -50 && screenX <= 768)
//				{
//					if (screenY >= -50 && screenY <= 576)
//					{
//						g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
//					}
//				}
			}
		}
		once = false;

	}
}
