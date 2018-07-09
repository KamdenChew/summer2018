package game;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.IOException;

public class Game implements Runnable {

	private final boolean DEBUG_FPS = false;
	
	private int width;
	private int height;
	private static final String TITLE = "Dungeon Explorer";
	private int difficulty;
	protected Dungeon dungeon;
	private Thread thread;
	private boolean running;
	
	private Display display;
	private BufferStrategy bs;
	private Graphics graphics;
	
	//States
	private State gameState;
	private State mainMenuState;
	
	//Keyboard Input
	private KeyManager keyManager;
	
	public Game(int difficulty) {
		this.difficulty = difficulty;
		
		this.dungeon = new Dungeon(difficulty);
		this.height = (dungeon.getData().getNumRows()) * 50;
		this.width = (dungeon.getData().getNumColumns()) * 50;
		
		keyManager = new KeyManager();
	}
	
	private void init() {
		//Prepare Display
		this.display = new Display(TITLE, width, height);
		this.display.getFrame().addKeyListener(keyManager);
		
		//Initialize Assets
		Assets.init();
		
		//Initialize States
		gameState = new GameState(this);
		mainMenuState = new MainMenuState(this);
		
		State.setState(gameState);
	}
	
	public KeyManager getKeyManager() {
		return this.keyManager;
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
		graphics.clearRect(0, 0, width, height);
		
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
