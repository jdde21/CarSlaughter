package main;

import java.util.ArrayList;

import javax.swing.JFrame;

import entity.Player;

public class Main {
	

	public GamePanel gp;
	
	public Main(GamePanel gp) {
		this.gp = gp;
		this.start();
	}
	
	public void start()
	{
		JFrame window = new JFrame();
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("2d adventure");
		
		window.add(gp);
		window.pack();
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		gp.startGameThread();
	}

}
