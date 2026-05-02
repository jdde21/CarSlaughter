package main;

import java.io.Serializable;

public class GameData implements Serializable{
	private static final long serialVersionUID = 1L;

    int x, y;
    String direction;


    public GameData(int x, int y, String direction) {
    	this.x = x;
        this.y = y;
        this.direction = direction;
    }
}
