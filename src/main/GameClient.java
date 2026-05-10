package main;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

import entity.Player;

public class GameClient {

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;
    public static ArrayList<Player> players = new ArrayList<>();
    static int[] positions = {0, 50, 25, 30};
    
	public static int[][] matrix;
	
    public static void main(String[] args) throws IOException {

        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        System.out.println("Connected to server!");
        
        KeyHandler keyH = new KeyHandler();
        GamePanel gamePanel = new GamePanel(players, keyH);
        Player player = new Player(gamePanel, keyH, positions[0], positions[0]);
        gamePanel.myPlayer = player;        
       
        
        

        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        player.out = out;
        
    

        // Thread to listen for server messages
        new Thread(() -> { 
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                	String[] parts = msg.split(" ");
                	System.out.println(parts);
                	System.out.println("checkthis: " + msg);
                	if (parts.length > 1)
                	{
                		
                		if (parts[0].equals("id"))
                		{
                			 matrix = new int[][] { 
                	    		    {120, 120},
                	    		    {gamePanel.screenWidth - 160, 120},
                	    		    {120, gamePanel.screenHeight - 160},
                	    		    {gamePanel.screenWidth - 160, gamePanel.screenHeight - 160}
                	    		};
                	    		
                			gamePanel.myPlayer.playerID = Integer.parseInt(parts[1]);
                			if (gamePanel.myPlayer.playerID % 2 != 0)
                			{
                				player.direction = "left";
                			}
                			player.worldX = matrix[gamePanel.myPlayer.playerID][0];
                			player.worldY = matrix[gamePanel.myPlayer.playerID][1];
                			new Main(gamePanel, "Player " + player.playerID);
                		}
                		
                		else	
                		{
                			int id = gamePanel.myPlayer.playerID;
                        	String direction = parts[0];
                        	int sentID = Integer.parseInt(parts[1]);
                        
                        	if (direction.equals("up"))
                        	{
                        		
                        		if (id < sentID)
                        		{
                        			sentID--;
                        		}
                        		
                    			players.get(sentID).up = true;
                    			players.get(sentID).down = false;
                    			players.get(sentID).right = false;
                    			players.get(sentID).left = false;
                    			//players.get(sentID).space = false;
                    			players.get(sentID).updateFromOther();
                        	
                        	}
                        	if (direction.equals("down"))
                        	{
                        		
                        		if (id < sentID)
                        		{
                        			sentID--;
                        		}
                        		
                        		players.get(sentID).up = false;
                    			players.get(sentID).down = true;
                    			players.get(sentID).right = false;
                    			players.get(sentID).left = false;
                    			players.get(sentID).space = false;
                    			players.get(sentID).updateFromOther();
                        		
                        	}
                        	if (direction.equals("right"))
                        	{
                        		
                        		if (id < sentID)
                        		{
                        			sentID--;
                        		}
                        		
                        		players.get(sentID).up = false;
                    			players.get(sentID).down = false;
                    			players.get(sentID).right = true;
                    			players.get(sentID).left = false;
                    			players.get(sentID).space = false;
                    			players.get(sentID).updateFromOther();
                        		
                        	}
                        	if (direction.equals("left"))
                        	{
                        		
                        		if (id < sentID)
                        		{
                        			sentID--;
                        		}
                        		
                        		players.get(sentID).up = false;
                    			players.get(sentID).down = false;
                    			players.get(sentID).right = false;
                    			players.get(sentID).left = true;
                    			players.get(sentID).space = false;
                    			players.get(sentID).updateFromOther();
                        		
                        	}
                        	if (direction.equals("space"))
                        	{
                        		if (id < sentID)
                        		{
                        			sentID--;
                        		}
                        		players.get(sentID).up = false;
                    			players.get(sentID).down = false;
                    			players.get(sentID).right = false;
                    			players.get(sentID).left = false;
                        		players.get(sentID).space = true;
                        		players.get(sentID).updateFromOther();
                        	}
                		}
                		
                    	
                	}
                	
                	else
                	{
                		System.out.println("Other player: " + msg);
                		String[] words = msg.split(",");
                		int counter = Integer.parseInt(words[1]);
                		
                		int[][] temp = new int[][] { 
        	    		    {120, 120},
        	    		    {gamePanel.screenWidth - 160, 120},
        	    		    {120, gamePanel.screenHeight - 160},
        	    		    {gamePanel.screenWidth - 160, gamePanel.screenHeight - 160}
        	    		};
        	    		
                        // Hardcoded spawn positions
                        int x = temp[counter][0];
                        int y = temp[counter][1];
               
                        
                        
                        // IMPORTANT: no KeyHandler for other players
                        Player newPlayer = new Player(gamePanel, null, x, y);
                    	if (counter % 2 != 0)
            			{
            				newPlayer.direction = "left";
            			}
                        players.add(newPlayer);
                	}
        
                }
            } catch (IOException e) {
                System.out.println("Disconnected from server");
            }
        }).start();

        // Send input to server
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            out.println(input);
        } 
    }
}