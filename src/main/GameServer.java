package main;
import java.io.*;
import java.net.*;
import java.util.*;

import entity.Player;

public class GameServer {

    private static final int PORT = 12345;
    private static Set<ClientHandler> clients = new HashSet<>();
    public static ArrayList<Player> players = new ArrayList<>();
    public static int mainPlayerCount = 0;
    static int playerCounter = 0;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started on port " + PORT);
        
        
        int[] positions = {0, 50, 25, 30};
        int counter = 0;
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("New player connected");
            
//            KeyHandler keyH = new KeyHandler();
//            GamePanel gamePanel = new GamePanel(players, keyH);
//            Player player = new Player(gamePanel, keyH, positions[counter], positions[counter]);
//            gamePanel.myPlayer = player;
//            
//          
//            Main main = new Main(gamePanel);
//            players.add(player);
   
            ClientHandler client = new ClientHandler(socket);
            client.clientID = playerCounter;
            clients.add(client);
            client.start();

            GameServer.broadcast("NEW_PLAYER", client); // to update old players of a new player
            sendTo(client, "NEW_PLAYER", playerCounter++); // to know how many old players there are
            sendID(client);
            counter++;
        }
    }

    public static void broadcast(String message, ClientHandler sender) {
    	String temp = message;
    	if (message.equals("NEW_PLAYER"))
    	{
    		temp = message + "," + playerCounter;
    	}
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(temp);
            }
        }
    }
    
    public static void sendTo(ClientHandler client, String message, int times) {
        for (int i = 0; i < times; i++) {
        	String temp = message + "," + i;
            client.sendMessage(temp);
        }
    }
    
    public static void sendID(ClientHandler client) {

    	client.sendMessage("id " + String.valueOf(GameServer.mainPlayerCount++));

    }

    public static void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private int clientID;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
				out = new PrintWriter(socket.getOutputStream(), true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        public void sendMessage(String msg) {
            out.println(msg);
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                    GameServer.broadcast(message, this);
                }
            } catch (IOException e) {
                System.out.println("Player disconnected");
            } finally {
                GameServer.removeClient(this);
                try { socket.close(); } catch (IOException ignored) {}
            }
        }
    }
}