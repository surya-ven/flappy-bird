import bagel.Input;
import bagel.Keys;

/**
 * Represents a timescale, allowing it to increase or decrease the speed of time within the game.
 */
public class TimeScale implements TimeScalable {
    private double timeScale;

    /**
     * Instantiates a timeScale with its default value.
     */
    public TimeScale() {
        this.timeScale = TimeScalable.TIMESCALE_MIN;
    }

    /**
     * Takes in input and determines whether the timescale has been increased or decreased, returns nothing.
     * @param input The input object which allows detection of key commands.
     * @return void
     */
    @Override
    public void timeScale(Input input) {
        // Speed up
        if (input.wasPressed(Keys.L) && timeScale < TimeScalable.TIMESCALE_MAX) {
            timeScale++;
        }
        // Slow down
        else if (input.wasPressed(Keys.K) && timeScale > TimeScalable.TIMESCALE_MIN) {
            timeScale--;
        }
    }

    /**
     * Gets the timeScale value.
     * @return double Returns the current timeScale.
     */
    public double getTimeScale() {
        return timeScale;
    }

    /**
     * Resets the timeScale back to its default, returns nothing.
     * @return void
     */
    public void reset() {
        timeScale = TimeScalable.TIMESCALE_MIN;
    }
}
