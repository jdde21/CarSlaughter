package main;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

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

        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        
        KeyHandler keyH = new KeyHandler();
        GamePanel gamePanel = new GamePanel(players, keyH);
        Player player = new Player(gamePanel, keyH, positions[0], positions[0]);
        gamePanel.myPlayer = player;
        player.out = out;

        ChatWindow chatWindow = new ChatWindow(out);
        keyH.setShowChatAction(chatWindow::showAndFocus);

        new Main(gamePanel, "Player " + player.playerID);
        chatWindow.showAndFocus();

        // Thread to listen for server messages
        new Thread(() -> { 
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    handleServerMessage(msg, gamePanel, chatWindow);
                }
            } catch (IOException e) {
                System.out.println("Disconnected from server");
            }
        }).start();
    }

    private static void handleServerMessage(String msg, GamePanel gamePanel, ChatWindow chatWindow) {
        if (msg.startsWith(CHAT_PREFIX)) {
            handleChatMessage(msg, chatWindow);
            return;
        }

        String[] parts = msg.split(" ");
        if (parts.length > 1) {
            if (parts[0].equals("id")) {
                int playerID = Integer.parseInt(parts[1]);
                gamePanel.myPlayer.playerID = playerID;
                chatWindow.setPlayerID(playerID);
                System.out.println("Assigned player ID: " + playerID);
            } else {
                handleMovementMessage(parts, gamePanel);
            }
        } else if (msg.equals("NEW_PLAYER")) {
            addRemotePlayer(gamePanel);
        }
    }

    private static void handleChatMessage(String msg, ChatWindow chatWindow) {
        String[] parts = msg.split(" ", 3);
        if (parts.length == 3) {
            chatWindow.appendChatMessage("Player " + parts[1] + ": " + parts[2]);
        }
    }

    private static void addRemotePlayer(GamePanel gamePanel) {
        System.out.println("Other player joined");

        int x = positions[0];
        int y = positions[0];

        // IMPORTANT: no KeyHandler for other players
        Player newPlayer = new Player(gamePanel, null, x, y);
        players.add(newPlayer);
    }

    private static void handleMovementMessage(String[] parts, GamePanel gamePanel) {
        String direction = parts[0];
        int sentID;

        try {
            sentID = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return;
        }

        int id = gamePanel.myPlayer.playerID;
        if (id < sentID) {
            sentID--;
        }

        if (sentID < 0 || sentID >= players.size()) {
            return;
        }

        Player player = players.get(sentID);
        player.up = false;
        player.down = false;
        player.right = false;
        player.left = false;
        player.space = false;

        switch (direction) {
        case "up":
            player.up = true;
            break;
        case "down":
            player.down = true;
            break;
        case "right":
            player.right = true;
            break;
        case "left":
            player.left = true;
            break;
        case "space":
            player.space = true;
            break;
        default:
            return;
        }

        player.updateFromOther();
    }
}
