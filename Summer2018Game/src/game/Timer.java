package game;

public abstract class Timer {

	/**
	 * Waits for the specified amount of milliseconds before returning
	 *
	 * @param milliseconds the amount of milliseconds we wait before returning
	 */
	public static void waitFor(int milliseconds) {
		long startTime = System.nanoTime();
		while((System.nanoTime() - startTime) / 1000000 < milliseconds) {
			
		}
		return;
	}
}
