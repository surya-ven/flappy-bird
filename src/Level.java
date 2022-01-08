import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import java.util.LinkedList;

/**
 * Represents an abstract Level, with the ability to update the main elements on the screen.
 */
public abstract class Level {
    // Font and messages
    private final String SCORE_MESSAGE = "SCORE:";
    private final int NO_PADDING = 0;
    private final Point SCORE_POINT = new Point(100, 100);

    // Game flow
    private final int BIRD_DAMAGE = 1;
    private final int INITIAL_PIPE_INTERVAL = 100;
    private final double WEAPON_MULTIPLIER = 0.5;
    private final double WEAPON_PROBABILITY = 0.7;
    private final int EXCLUSIVE = 1;

    // Level logic
    private double pipeInterval;
    private int frameCountPipes;
    private boolean isInitialRender;
    private int score;
    private boolean isGameOver;
    private boolean isLevelCompleted;
    private int maxLevelScore;

    /**
     * Takes in initialScore and maxLevelScore, then creates a Level.
     * @param initialScore integer, the initial score which the level should start at.
     * @param maxLevelScore integer, the max score for the level.
     */
    public Level(int initialScore, int maxLevelScore) {
        this.score = initialScore;
        this.maxLevelScore = maxLevelScore;
        this.isGameOver = false;
        this.isLevelCompleted = false;
        this.isInitialRender = true;
    }

    /**
     * Updates all components in a level, returns nothing.
     * @param input Input object from which to detect key commands.
     * @param pipes LinkedList<Pipes>, pipes used within the level.
     * @param bird Bird object used within the level.
     * @param weapons LinkedList<Weapon>, weapons used within the level.
     * @param background Background object used within the level.
     * @param timeScale TimeScale object used within the level
     * @param message Message object used within the level, to display messages.
     * @return void
     */
    public abstract void update(Input input, LinkedList<Pipes> pipes, Bird bird, LinkedList<Weapon> weapons,
                                   Background background, TimeScale timeScale, Message message);

    protected void updateBird(Input input, LinkedList<Pipes> pipes, Bird bird, Message message) {
        // Bird movement
        if (input.wasPressed(Keys.SPACE)) {
            bird.move();
        } else {
            bird.fall();
        }
        // Flap and draw bird
        bird.draw();

        updateScore(pipes, bird, message);
    }

    protected void updateScore(LinkedList<Pipes> pipes, Bird bird, Message message) {
        // Score count
        if (!pipes.getLast().getBirdIsThrough() && bird.throughPipes(pipes.getLast())) {
            setScore(getScore() + 1);
        }

        // Level 0
        if (score >= maxLevelScore) {
            setIsLevelCompleted(!getIsLevelCompleted());
        }

        // Score board
        message.drawString(SCORE_MESSAGE + " " + score, SCORE_POINT);
    }

    /**
     * Default level interval message, returns nothing.
     * @param message Message object, used to display messages.
     * @return void
     */
    public void levelIntervalMessage(Message message) {
        // Default - no message
    }

    /**
     * Creates a bird object and returns it.
     * @return Bird Newly created bird object.
     */
    public abstract Bird createBird();

    /**
     * Creates a background object and returns it.
     * @return Background Newly created background object.
     */
    public abstract Background createBackground();

    protected abstract void updatePipes(LinkedList<Pipes> pipes, TimeScale timeScale);

    /**
     * Gets the pipeInterval.
     * @return double The interval to spawn the pipes.
     */
    public double getPipeInterval() {
        return pipeInterval;
    }

    protected int getFrameCountPipes() {
        return frameCountPipes;
    }

    protected void setFrameCountPipes(int frameCountPipes) {
        this.frameCountPipes = frameCountPipes;
    }

    protected void setPipeInterval(double pipeInterval) {
        this.pipeInterval = pipeInterval;
    }

    /**
     * Gets the isInitialRender value.
     * @return boolean Returns whether it is the initial render for the Level.
     */
    public boolean getIsInitialRender() {
        return isInitialRender;
    }

    /**
     * Sets isInitialRender to a specified value.
     * @param isInitialRender boolean, to set the new isInitialRender value to.
     */
    public void setIsInitialRender(boolean isInitialRender) {
        this.isInitialRender = isInitialRender;
    }

    protected int getINITIAL_PIPE_INTERVAL() {
        return INITIAL_PIPE_INTERVAL;
    }

    /**
     * Gets the score from level.
     * @return int The current score within the level.
     */
    public int getScore() {
        return score;
    }

    protected void setScore(int score) {
        this.score = score;
    }

    protected double getWEAPON_MULTIPLIER() {
        return WEAPON_MULTIPLIER;
    }

    /**
     * Gets the weapon probability.
     * @return double The weapon spawn rate probability.
     */
    public double getWEAPON_PROBABILITY() {
        return WEAPON_PROBABILITY;
    }

    /**
     * Gets the exclusive value.
     * @return int Returns the value used to exclude a value from within a range by deducting this value.
     */
    public int getEXCLUSIVE() {
        return EXCLUSIVE;
    }

    /**
     * Gets the bird damage.
     * @return int The bird damage value.
     */
    public int getBIRD_DAMAGE() {
        return BIRD_DAMAGE;
    }

    /**
     * Gets the isGameOver value.
     * @return boolean Returns if the game is over.
     */
    public boolean getIsGameOver() {
        return isGameOver;
    }

    /**
     * Sets the isGameOver value to the specified value, returns nothing.
     * @param isGameOver boolean, which is the new value specifying whether the game is over.
     * @return void
     */
    public void setIsGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    /**
     * Sets the IsGameOver value to true, returns nothing.
     * @return void
     */
    public void gameOver() {
        setIsGameOver(!isGameOver);
    }

    /**
     * Gets the isLevelCompleted value.
     * @return boolean Returns whether the level is completed.
     */
    public boolean getIsLevelCompleted() {
        return isLevelCompleted;
    }

    protected void setIsLevelCompleted(boolean isLevelCompleted) {
        this.isLevelCompleted = isLevelCompleted;
    }

    // Note: Optional getter used for carrying over score count to next level if needed
    /**
     * Gets the maxLevelScore value.
     * @return int Returns the maxLevelScore for the level.
     */
    public int getMaxLevelScore() {
        return maxLevelScore;
    }

    /**
     * Gets the no padding value.
     * @return int Returns the no padding value.
     */
    public int getNO_PADDING() {
        return NO_PADDING;
    }
}
