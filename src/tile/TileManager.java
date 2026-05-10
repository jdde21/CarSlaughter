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
	public int powerTileNum[][];
	public boolean drawShield = true;
	boolean once = true;
	
	public TileManager(GamePanel gp) {
		this.gp = gp;
		
		
		tile = new Tile[90];
		gp.mapTileNum = new int[gp.maxScreenRow][gp.maxScreenCol];
		gp.powerTileNum = new int[gp.maxScreenRow][gp.maxScreenCol];
		this.mapTileNum = gp.mapTileNum;
		this.powerTileNum = gp.powerTileNum;
		
		getTileImage();
		loadMap("/maps/final_map.txt");
	}
	
	public void getTileImage() {
		try {
			
			
			for (int i = 1; i <= 89; i++) {
			    tile[i] = new Tile();
			    String fileName = String.format("/tiles/%03d.png", i);
			    tile[i].image = ImageIO.read(getClass().getResourceAsStream(fileName));
			    if ((i >= 17 && i <= 27) || (i == 32 || i == 39 || i == 55 || i == 64) || (i >= 64 && i <= 74) || (i == 27 || i == 36 || i == 44 || i == 59))
			    {
			    	tile[i].collision = true;
			    }
			}
			
			
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
					mapTileNum[i][j] = Integer.parseInt(numbers[j]);
				}
			}
			
		} catch (Exception e) {
			
		}
	}
	
	
	public void draw(Graphics g2) {		
		

		for (int i = 0; i < gp.maxScreenRow; i++) {
			for (int j = 0; j < gp.maxScreenCol; j++) {
				
				int tileNum = mapTileNum[i][j];
				g2.drawImage(tile[tileNum].image, j * gp.tileSize, i * gp.tileSize, gp.tileSize, gp.tileSize, null);
				
				if ((i == gp.maxScreenRow/2 && j == gp.maxScreenCol/2) && drawShield)
				{
					try {
						Tile temp = new Tile();
						String fileName = "/powers/power_up_shield.png";
						temp.image = ImageIO.read(getClass().getResourceAsStream(fileName));
						temp.shield = true;
						g2.drawImage(temp.image, j * gp.tileSize, i * gp.tileSize, gp.tileSize, gp.tileSize, null);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		}
		once = false;

	}
}
