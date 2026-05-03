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

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        System.out.println("Connected to server!");
        
        KeyHandler keyH = new KeyHandler();
        GamePanel gamePanel = new GamePanel(players, keyH);
        Player player = new Player(gamePanel, keyH, positions[0], positions[0]);
        gamePanel.myPlayer = player;        
      
        Main main = new Main(gamePanel, "Player " + player.playerID);
        

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
                	if (parts.length > 1)
                	{
                		
                		if (parts[0].equals("id"))
                		{
                			gamePanel.myPlayer.playerID = Integer.parseInt(parts[1]);
                			System.out.println("boompanes " + gamePanel.myPlayer.playerID);
                		}
                		
                		else	
                		{
                			int id = gamePanel.myPlayer.playerID;
                			System.out.println(parts);
                        	System.out.println("msg");
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
                        		
                        		System.out.println("Other player bitch: " + msg);
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
                        		
                        		System.out.println("Other player bitch: " + msg);
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
                        		
                        		System.out.println("Other player bitch: " + msg);
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
                        		
                        		System.out.println("Other player bitch: " + msg);
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
                        
                        // Hardcoded spawn positions
                        int x = positions[0];
                        int y = positions[0];

                        // IMPORTANT: no KeyHandler for other players
                        Player newPlayer = new Player(gamePanel, null, x, y);
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