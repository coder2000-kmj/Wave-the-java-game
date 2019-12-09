package com.tutorial.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 15755241831643964L;
	public static final int WIDTH=640,HEIGHT=WIDTH/12*9;
	private Thread thread;
	private boolean running=false;
	public static boolean paused=false;
	public int diff=0;
	private Handler handler; 
	private Random r;
	private HUD hud;
	private Spawn spawner;
	private Menu menu;
	private Shop shop;
	public enum STATE{
		Menu,Select,Game,Help,Shop,End
	};
	public static STATE gameState=STATE.Menu;
	public static BufferedImage sprite_sheet;
	
	public Game(){
		BufferedImageLoader loader=new BufferedImageLoader();
		try {
		sprite_sheet=loader.loadImage("/sprite_sheet.png");
		System.out.println("Loaded");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		spawner =new Spawn(handler,hud,this);
		handler=new Handler();
		hud=new HUD();
		shop=new Shop(handler,hud);
		menu=new Menu(this, handler,hud);
		this.addKeyListener(new KeyInput(handler,this));
		this.addMouseListener(menu);
		this.addMouseListener(shop);
		
		AudioPlayer.load();
		AudioPlayer.getMusic("music").loop();
		
		new Window(WIDTH,HEIGHT,"Let's Build a Game",this);
		
//		sprite_sheet=loader.loadImage("/sprite_sheet.png");
		
		spawner =new Spawn(handler,hud,this);
	
		r=new Random();
		if(gameState==STATE.Game)
		{
			handler.addObject(new Player(WIDTH/2-32,HEIGHT/2-32,ID.Player,handler));
		    handler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH- 50),r.nextInt(Game.HEIGHT- 50),ID.BasicEnemy,handler));
		}else {
			for(int i=0;i<20;i++) {
				handler.addObject(new MenuParticle(r.nextInt(WIDTH),r.nextInt(HEIGHT),ID.MenuParticle,handler));
			}
				
		}
	    
	}
	
	public synchronized void start()
{
	thread=new Thread(this);
	thread.start();
	running=true;
}
	
	public synchronized void stop()
{
	try
	{
		thread.join();
		running=false;
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
}
	public void run()
	{
		this.requestFocus(); //no need to press the screen
		long lastTime=System.nanoTime();
		double amountofticks=60.0;
		double ns= 1000000000/amountofticks;
		
		double delta=0;
		long timer=System.currentTimeMillis();
		int frames=0;
		while(running)
		{
			long now=System.nanoTime();
			delta +=(now - lastTime)/ns;
			lastTime=now;
			while(delta>=1)
			{
				tick();
				delta--;
			}
			if(running)
				render();
			frames++;
			
			if(System.currentTimeMillis() - timer>1000)
			{
				timer+=1000;
				//  System.out.println("FPS: "+frames);
				frames=0;
			}
			
		}
		stop();
	}
	private void tick()
	{
		
		
		if(gameState==STATE.Game)
		{
			if(!paused)
			{
				hud.tick();
				spawner.tick();
				handler.tick();
				if(HUD.HEALTH<=0) {
					HUD.HEALTH=100;
					
					gameState=STATE.End;
					handler.clearEnemys();
					for(int i=0;i<20;i++) {
						handler.addObject(new MenuParticle(r.nextInt(WIDTH),r.nextInt(HEIGHT),ID.MenuParticle,handler));
					}
			}
			
				
			}
		}
		else if(gameState==STATE.Menu||gameState==STATE.End||gameState==STATE.Select)
		{
			menu.tick();
			handler.tick();
		}
		
	}
	private void render()
	{
		BufferStrategy bs= this.getBufferStrategy();
		if(bs==null)
		{
			this.createBufferStrategy(3);
			return;
		}
		Graphics g=bs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		if(paused)
		{	
			g.setColor(Color.white);
			g.drawString("PAUSED", 100, 100);
		}
		if(gameState==STATE.Game)
		{
			hud.render(g);
			handler.render(g);
		}
		else if(gameState==STATE.Shop){
			shop.render(g);
		}
		else if(gameState==STATE.Menu||gameState==STATE.Help||gameState==STATE.End||gameState==STATE.Select)
		{
			menu.render(g);
			handler.render(g);
		}
	
		
		g.dispose();
		bs.show();
	}
	public static int clamp(float x,float min,float max)
	{
		if(x>=max)
			return (int) (x=max);
		else if(x<=min)
			return (int) (x=min);
		else
			return (int) x;
	}
	
	
		public static void main(String[]args)
	
{
	new Game();
}
}
