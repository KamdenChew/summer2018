package game;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.IOException;

public class Game implements Runnable {

	private final boolean DEBUG_FPS = false;
	
	private static final int WIDTH = 350;
	private static final int HEIGHT = 350;
	private static final String TITLE = "Dungeon Explorer";
	private int difficulty;
	protected Dungeon dungeon;
	private Thread thread;
	private boolean running;
	
	private Display display;
	private BufferStrategy bs;
	private Graphics graphics;
	
	
	//Main States
	private State gameState;
	private State mainMenuState;
	private State townState;
	
	//User Inputs
	private KeyManager keyManager;
	private MouseManager mouseManager;
	
	public Game(int difficulty) {
		this.difficulty = difficulty;
		
		this.dungeon = new Dungeon(difficulty);
//		this.height = (dungeon.getData().getNumRows()) * 50;
//		this.width = (dungeon.getData().getNumColumns()) * 50;
		
		keyManager = new KeyManager();
		mouseManager = new MouseManager();
	}
	
	private void init() {
		//Prepare Display
		this.display = new Display(TITLE, WIDTH, HEIGHT);
		this.display.getFrame().addKeyListener(keyManager);
		this.display.getFrame().addMouseListener(mouseManager);
		this.display.getFrame().addMouseMotionListener(mouseManager);
		this.display.getCanvas().addMouseListener(mouseManager);
		this.display.getCanvas().addMouseMotionListener(mouseManager);
		
		//Initialize Assets
		Assets.init();
		
		//Initialize States
		gameState = new GameState(this);
		mainMenuState = new MainMenuState(this);
		townState = new TownState(this);
		
		State.setState(mainMenuState);
	}
	
	public State getMainMenuState() {
		return mainMenuState;
	}

	public State getTownState() {
		return townState;
	}

	public State getGameState() {
		return gameState;
	}

	public Display getDisplay() {
		return display;
	}

	public KeyManager getKeyManager() {
		return this.keyManager;
	}
	
	public MouseManager getMouseManager() {
		return this.mouseManager;
	}
	
	private void tick() {
		keyManager.tick();
		
		if(State.getState() != null) {
			State.getState().tick();
		}
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
		graphics.clearRect(0, 0, WIDTH, HEIGHT);
		
		//Drawing
//		graphics.setColor(Color.red);
//		graphics.fillRect(10, 50, 50, 70);
//		graphics.drawImage(Assets.empty, x, 20, null);
		if(State.getState() != null) {
			State.getState().render(graphics);
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
				tick();
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
