package com.tutorial.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.tutorial.main.Game.STATE;

public class KeyInput extends KeyAdapter{
private Handler handler;
private boolean[] keyDown=new boolean[4];
Game game;
public KeyInput(Handler handler,Game game)
{
	this.handler=handler;
	this.game=game;
	keyDown[0]=false;
	keyDown[1]=false;
	keyDown[2]=false;
	keyDown[3]=false;
}

	public void keyPressed(KeyEvent e)
	{
		int key=e.getKeyCode();
		for(int i=0;i<handler.object.size();i++)
		{
			GameObject tempObject=handler.object.get(i);
			if(tempObject.getId()==ID.Player)
			{
				//key events for player 1
				if(key==KeyEvent.VK_UP) {tempObject.setvelY(-handler.spd); keyDown[0]=true;}
				if(key==KeyEvent.VK_DOWN) {tempObject.setvelY(handler.spd); keyDown[1]=true;}
				if(key==KeyEvent.VK_RIGHT) {tempObject.setvelX(handler.spd); keyDown[2]=true;}
				if(key==KeyEvent.VK_LEFT) {tempObject.setvelX(-handler.spd); keyDown[3]=true;}
			}
		}
		if(key==KeyEvent.VK_P)
		{
			if(game.gameState==STATE.Game)
			{
				if(Game.paused) Game.paused=false;
				else Game.paused=true;
			}
		}
		if(key==KeyEvent.VK_SPACE) {
			if(game.gameState==STATE.Game) Game.gameState=STATE.Shop;
			else if( Game.gameState==STATE.Shop) game.gameState=STATE.Game;
		}
		if(key==KeyEvent.VK_ESCAPE) System.exit(1);
	
	}
	public void keyReleased(KeyEvent e)
	{
		int key=e.getKeyCode();
		for(int i=0;i<handler.object.size();i++)
		{
			GameObject tempObject=handler.object.get(i);
			if(tempObject.getId()==ID.Player)
			{
				//key events for player 1
				if(key==KeyEvent.VK_UP) keyDown[0]=false;
				if(key==KeyEvent.VK_DOWN) keyDown[1]=false;
				if(key==KeyEvent.VK_RIGHT) keyDown[2]=false;
				if(key==KeyEvent.VK_LEFT) keyDown[3]=false;
				//vertical movement
				if(!keyDown[0] && !keyDown[1]) tempObject.setvelY(0);
				//horizontal movement
				if(!keyDown[2] && !keyDown[3]) tempObject.setvelX(0);
			} 
		}
	
	}
}
