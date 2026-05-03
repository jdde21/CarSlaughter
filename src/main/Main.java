package main;

import java.util.ArrayList;

import javax.swing.JFrame;

import entity.Player;

public class Main {
	

	public GamePanel gp;
	public String title;
	
	public Main(GamePanel gp, String title) {
		this.gp = gp;
		this.title = title;
		this.start();
	}
	
	public void start()
	{
		JFrame window = new JFrame();
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle(title);
		
		window.add(gp);
		window.pack();
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		gp.startGameThread();
	}

}
