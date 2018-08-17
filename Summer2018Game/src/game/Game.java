package game;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Scanner;

public class Game implements Runnable {

	private final boolean DEBUG_FPS = false;
	
	//Display parameters
	private final int RENDER_DISTANCE = 6;
	private final int WIDTH = 50 * (RENDER_DISTANCE * 2 + 1);
	private final int HEIGHT = 50 * (RENDER_DISTANCE * 2 + 1);

	private static final String TITLE = "Dungeon Explorer";
	private boolean running;
	private boolean primaryWindow;
	private Display display;
	
	private BufferStrategy bs;
	private Graphics graphics;
	
	private Thread thread;
	private Player player;
	
	//User Inputs
	private KeyManager keyManager;
	private MouseManager mouseManager;
	private Scanner scanner = new Scanner(System.in);
	
	//Game Statistics
	private String saveName;
	private int score;
	private int numPeacefulCompleted;
	private int numEasyCompleted;
	private int numMediumCompleted;
	private int numHardCompleted;
	
	
	public Game() {
		this.primaryWindow = true;
		this.score = 0;
		this.numPeacefulCompleted = 0;
		this.numEasyCompleted = 0;
		this.numMediumCompleted = 0;
		this.numHardCompleted = 0;
		this.saveName = "";
		
		keyManager = new KeyManager();
		mouseManager = new MouseManager();
	}
	
	public boolean isInDungeon() {
		return State.getState().isDungeonState();
	}

	public String getSaveName() {
		return saveName;
	}

	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}

	public boolean isPrimaryWindow() {
		return primaryWindow;
	}

	public void setPrimaryWindow(boolean primaryWindow) {
		this.primaryWindow = primaryWindow;
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getNumPeacefulCompleted() {
		return numPeacefulCompleted;
	}

	public void setNumPeacefulCompleted(int numPeacefulCompleted) {
		this.numPeacefulCompleted = numPeacefulCompleted;
	}

	public int getNumEasyCompleted() {
		return numEasyCompleted;
	}

	public void setNumEasyCompleted(int numEasyCompleted) {
		this.numEasyCompleted = numEasyCompleted;
	}

	public int getNumMediumCompleted() {
		return numMediumCompleted;
	}

	public void setNumMediumCompleted(int numMediumCompleted) {
		this.numMediumCompleted = numMediumCompleted;
	}

	public int getNumHardCompleted() {
		return numHardCompleted;
	}

	public void setNumHardCompleted(int numHardCompleted) {
		this.numHardCompleted = numHardCompleted;
	}
	
	public void forceBs() {
		bs.show();
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
		
		State.setState(new MainMenuState(this));
	}
	
	public Scanner getScanner() {
		return scanner;
	}

	public Display getDisplay() {
		return display;
	}
	
	public int getRenderDistance() {
		return RENDER_DISTANCE;
	}

	public KeyManager getKeyManager() {
		return this.keyManager;
	}
	
	public MouseManager getMouseManager() {
		return this.mouseManager;
	}
	
	private void tick() {
		if(isPrimaryWindow()) {
			keyManager.tick();
			if(State.getState() != null) {
				State.getState().tick();
			}
		}
	}
	
	private void render() {
		if(isPrimaryWindow()) {
			this.bs = this.display.getCanvas().getBufferStrategy();
			
			//If no buffers, set to 3
			if(bs == null) {
				this.display.getCanvas().createBufferStrategy(3);
				return;
			}
			
			graphics = bs.getDrawGraphics();
			//Clear previous render
			graphics.clearRect(0, 0, WIDTH, HEIGHT);
			
			if(State.getState() != null) {
				State.getState().render(graphics);
			}
			
			//Re-render
			bs.show();
			graphics.dispose();
		}
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
