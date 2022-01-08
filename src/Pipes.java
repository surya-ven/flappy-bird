import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Represents pipes, allowing pipes to move and detect collisions with bird.
 */
public abstract class Pipes implements Movable, Drawable, Damageable<Weapon> {
    // Dimensions
    private final double BOTTOM_PIPE_Y;
    private final double BOTTOM_PIPE_Y_RECT;
    private final double TOP_PIPE_Y;
    private final double PIPE_LENGTH_TOP;
    private final double PIPE_LENGTH_BOTTOM;
    private final double TOP_PIPE_Y_RECT = 0;
    private final int PIPE_SPACE = 168;

    public static final double INITIAL_VELOCITY = 3;

    // Position
    private double pipeX;
    private double gapStartY;
    private double pipeWidth;

    // Drawing and collision use
    private Rectangle topPipe;
    private Rectangle bottomPipe;
    private Point topPipeTopLeft;
    private Point bottomPipeTopLeft;
    private boolean exists;

    // Bird
    private boolean birdIsThrough;
    private TimeScale timeScale;


    /**
     * Takes in gapStartY, pipeWidth, and creates a Pipe.
     * @param gapStartY double, Y value of the gap's starting position.
     * @param pipeWidth double, pipe's width.
     */
    public Pipes(double gapStartY, double pipeWidth) {
        // Initial position
        this.gapStartY = gapStartY;
        this.pipeX = Window.getWidth();

        // Dimensions
        this.BOTTOM_PIPE_Y = calcBottomPipeY();
        this.TOP_PIPE_Y = calcTopPipeY();
        this.BOTTOM_PIPE_Y_RECT = BOTTOM_PIPE_Y;
        this.PIPE_LENGTH_TOP = gapStartY;
        this.PIPE_LENGTH_BOTTOM = Window.getHeight() - (gapStartY + PIPE_SPACE);
        this.pipeWidth = pipeWidth;

        // Initial Points
        this.topPipeTopLeft = new Point(pipeX, TOP_PIPE_Y_RECT);
        this.bottomPipeTopLeft = new Point(pipeX, BOTTOM_PIPE_Y_RECT);

        // Rectangles
        this.topPipe = new Rectangle(topPipeTopLeft.x, topPipeTopLeft.y, pipeWidth, getPipeLengthTop());
        this.bottomPipe = new Rectangle(bottomPipeTopLeft.x, bottomPipeTopLeft.y, pipeWidth, getPipeLengthBottom());

        this.birdIsThrough = false;
        this.exists = true;
        this.timeScale = null;
    }

    /**
     * Takes in gapStartY, pipeWidth, scale, and creates a Pipe.
     * @param gapStartY double, Y value of the gap's starting position.
     * @param pipeWidth double, pipe's width.
     * @param scale The timeScale object affecting the speed of objects.
     */
    public Pipes(double gapStartY, double pipeWidth, TimeScale scale) {
        this(gapStartY, pipeWidth);
        this.timeScale = scale;
    }

    /**
     * Draws both the pipes to the window.
     * Returns nothing.
     * @return void
     */
    @Override
    public abstract void draw();

    /**
     * Moves both pipes based on a given step value.
     * Returns nothing.
     * @return void
     */
    @Override
    public void move() {
        double velocity;

        // calculate and change to new position
        if (timeScale != null) {
            velocity = INITIAL_VELOCITY * Math.pow(TimeScalable.MULTIPLIER, timeScale.getTimeScale() - 1);
        } else {
            velocity = INITIAL_VELOCITY;
        }

        setPipeX(pipeX - velocity);
        setTopPipeTopLeft(new Point(pipeX, topPipeTopLeft.y));
        setBottomPipeTopLeft(new Point(pipeX, bottomPipeTopLeft.y));

        // move rectangles
        topPipe.moveTo(topPipeTopLeft);
        bottomPipe.moveTo(bottomPipeTopLeft);
    }

    private void setTopPipeTopLeft(Point topPipeTopLeft) {
        this.topPipeTopLeft = topPipeTopLeft;
    }

    private void setBottomPipeTopLeft(Point bottomPipeTopLeft) {
        this.bottomPipeTopLeft = bottomPipeTopLeft;
    }

    private void setPipeX(double pipeX) {
        this.pipeX = pipeX;
    }

    /**
     * Checks if a collision has occurred with a rectangle.
     * Returns true if intersection exists and false otherwise.
     * @param rect Rectangle object to determine whether a collision exists.
     * @return boolean Returns true if a collision occurred, and false otherwise.
     */
    public boolean hasCollided(Rectangle rect) {
        if (rect.intersects(topPipe) || rect.intersects(bottomPipe)) {
            return true;
        }
        return false;
    }

    /**
     * Gets the top pipe Rectangle.
     * @return Rectangle representing the top pipe.
     */
    public Rectangle getTopPipe() {
        return topPipe;
    }

    /**
     * Gets the bottom pipe Rectangle.
     * @return Rectangle representing the bottom pipe.
     */
    public Rectangle getBottomPipe() {
        return bottomPipe;
    }

    protected double calcBottomPipeY() {
        double y = gapStartY + (double)PIPE_SPACE;
        return y;
    }

    protected double calcTopPipeY() {
        double y = - (Window.getHeight() - gapStartY);
        return y;
    }

    protected double getBottomPipeY() {
        return BOTTOM_PIPE_Y;
    }

    protected double getTopPipeY() {
        return TOP_PIPE_Y;
    }

    protected double getPipeLengthTop() {
        return PIPE_LENGTH_TOP;
    }

    protected double getPipeLengthBottom() {
        return PIPE_LENGTH_BOTTOM;
    }

    protected double getPipeX() {
        return pipeX;
    }

    /**
     * Determines whether the pipe is outside the window.
     * @return boolean Returns true if pipe is outside the window, and false otherwise.
     */
    public boolean isOutOfFrame() {
        if ((pipeX + pipeWidth) < 0) {
            return true;
        }
        return false;
    }

    /**
     * Sets the birdIsThrough value to true, returns nothing.
     * @return void
     */
    public void birdIsThrough() {
        birdIsThrough = true;
    }

    /**
     * Gets the birdIsThrough value.
     * @return boolean Returns whether the bird is through the pipes, and false otherwise.
     */
    public boolean getBirdIsThrough() {
        return birdIsThrough;
    }

    /**
     * Calculated and gets the speed multiplier value.
     * @return double Returns the multiplier to be used for altering object's speeds.
     */
    public double getSpeedMultiplier() {
        return Math.pow(TimeScalable.MULTIPLIER, timeScale.getTimeScale() - 1);
    }

    protected void perish() {
        exists = false;
    }

    /**
     * Takes in weapon and takes damage.
     * @param damage The damage object, in this case weapon, which inflicts damage on the pipes.
     * @return boolean Returns true if the pipe is alive, and false otherwise.
     */
    @Override
    public abstract boolean takeDamage(Weapon damage);

    /**
     * Gets the exists value.
     * @return boolean Returns whether the pipe exists.
     */
    public boolean getExists() {
        return exists;
    }
}
