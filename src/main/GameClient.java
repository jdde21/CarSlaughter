package main;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

import entity.Player;

public class GameClient {

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;
    private static final String CHAT_PREFIX = "CHAT ";
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
        
        ChatWindow chatWindow = new ChatWindow(out);
        keyH.setShowChatAction(chatWindow::showAndFocus);
        chatWindow.showAndFocus();

        // Thread to listen for server messages
        new Thread(() -> { 
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                	if (msg.startsWith(CHAT_PREFIX)) {
                        handleChatMessage(msg, chatWindow);
                        return;
                    }
                	
                	String[] parts = msg.split(" ");
                	
                	if (parts.length > 1)
                	{
                		// this is where the newly connected player will go to add some more details to its sprite including
                		// the starting position and the color of the car
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
                			chatWindow.setPlayerID(gamePanel.myPlayer.playerID);
                			player.worldX = matrix[gamePanel.myPlayer.playerID][0];
                			player.worldY = matrix[gamePanel.myPlayer.playerID][1];
                			player.getPlayerImage();
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
                	// this is where the newly connected player will go to spawn the older players
                	// this is also where the older players will go to spawn the new player
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
        	    		
                      
                        int x = temp[counter][0];
                        int y = temp[counter][1];
               
                        
                       
                        Player newPlayer = new Player(gamePanel, null, x, y);
                        newPlayer.playerID = counter;
                        newPlayer.getPlayerImage();
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
    
    private static void handleChatMessage(String msg, ChatWindow chatWindow) {
        String[] parts = msg.split(" ", 3);
        if (parts.length == 3) {
            chatWindow.appendChatMessage("Player " + parts[1] + ": " + parts[2]);
        }
    }
}