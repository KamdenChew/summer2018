import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import game.gfx.Assets;
import game.gfx.ImageLoader;

public class Game implements Runnable {

	private final boolean DEBUG_FPS = false;
	
	private static int width = 600;
	private static int height = 600;
	private static final String TITLE = "Dungeon Explorer";
	private int difficulty;
	private Dungeon dungeon;
	private Thread thread;
	private boolean running;
	
	private Display display;
	private BufferStrategy bs;
	private Graphics graphics;
	
	public Game(int difficulty) {
		this.difficulty = difficulty;
		
		this.dungeon = new Dungeon(difficulty);
		this.height = (dungeon.getData().getNumRows()) * 50;
		this.width = (dungeon.getData().getNumColumns()) * 50;
	}
	
	private void init() {
		this.display = new Display(TITLE, width, height);
		Assets.init();
	}
	
	int x = 0;
	
	private void update() {
		x += 1;
	}
	
	private void render() {
		this.bs = this.display.getCanvas().getBufferStrategy();
		
		//If no buffers, set to 3
		if(bs == null) {
			this.display.getCanvas().createBufferStrategy(3);
			return;
		}
		
		graphics = bs.getDrawGraphics();
		//Clear previous render
		graphics.clearRect(0, 0, width, height);
		
		//Drawing
//		graphics.setColor(Color.red);
//		graphics.fillRect(10, 50, 50, 70);
//		graphics.drawImage(Assets.empty, x, 20, null);
		Array2D<Integer> data = dungeon.getData();
		
		int columns = data.getNumColumns();
		int rows = data.getNumRows();
		for(int x = 0; x < columns; x++) {
			for(int y = 0; y < rows; y++) {
				if(Math.abs(data.get(x, y)) == 1) {
					graphics.drawImage(Assets.wall, x * 50, y * 50, null);
				} else if(data.get(x, y) == 0) {
					graphics.drawImage(Assets.stone, x * 50, y * 50, null);
				} else if(data.get(x, y) == 2) {
					graphics.drawImage(Assets.dirt, x * 50, y * 50, null);
				}
			}
		}
		
		//Re-render
		bs.show();
		graphics.dispose();
	}
	
	
	@SuppressWarnings("unused")
	@Override
	public void run() {
		init();
		
		//Frames Per Second
		int fps = 60;
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks =  0;
		
		while(running) {
			
			//Set current time
			now = System.nanoTime();
			
			//Update total time elapsed
			timer += (now - lastTime);
			
			//Accumulate time since last call to update and render
			delta += (now - lastTime) / timePerTick;
			
			//Reset our last computation time
			lastTime = now;
			
			//If we have accumulated 1 unit, then it's time to update and render again
			if(delta >= 1) {
				update();
				render();
				
				//Update ticks performed
				ticks++;
				
				//Reset tick update delta
				delta--;
			}
			
			if(DEBUG_FPS && timer >= 1000000000) {
				System.out.println("Frames per second: " + ticks);
				ticks = 0;
				timer = 0;
			}
		} 
		
		stop();
	}
	
	public synchronized void start() {
		if(!running) {
			running = true;
			thread = new Thread(this);
			
			//Calls run
			thread.start();
		}
	}
	
	public synchronized void stop() {
		if(running) {
			running = false;
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
