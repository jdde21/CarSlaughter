package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ChatWindow {

    private final JFrame frame;
    private final JTextArea messageArea;
    private final JTextField inputField;
    private final JButton sendButton;
    private final PrintWriter out;
    private int playerID = -1;

    public ChatWindow(PrintWriter out) {
        this.out = out;

        frame = new JFrame("Chat");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(380, 300));

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        inputField = new JTextField();
        inputField.addActionListener(e -> sendMessage());

        sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage());

        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        frame.add(new JScrollPane(messageArea), BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationByPlatform(true);

        setReady(false);
    }

    public void setPlayerID(int playerID) {
        SwingUtilities.invokeLater(() -> {
            this.playerID = playerID;
            frame.setTitle("Chat - Player " + playerID);
            setReady(true);
        });
    }

    public void appendChatMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            messageArea.append(message + System.lineSeparator());
            messageArea.setCaretPosition(messageArea.getDocument().getLength());
        });
    }

    public void showAndFocus() {
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
            frame.toFront();
            inputField.requestFocusInWindow();
        });
    }

    private void sendMessage() {
        String message = inputField.getText().trim().replaceAll("\\s+", " ");
        if (message.isEmpty() || playerID < 0) {
            return;
        }

        out.println("CHAT " + message);
        appendChatMessage("Player " + playerID + ": " + message);
        inputField.setText("");
    }

    private void setReady(boolean ready) {
        inputField.setEnabled(ready);
        sendButton.setEnabled(ready);
    }
}
