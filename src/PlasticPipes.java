import bagel.DrawOptions;
import bagel.Image;

/**
 * Represents plastic pipes, allowing pipes to move and detect collisions with bird.
 */
public class PlasticPipes extends Pipes {
    // Image
    private final static Image PIPE = new Image("res/level/plasticPipe.png");

    // Dimensions
    private final int ROTATION_DEGREES = 180;

    // Drawing and collision use
    private DrawOptions topPipeOptions = new DrawOptions();

    /**
     * Takes in gapStartY, and instantiates a plastic pipe.
     * @param gapStartY double, Y value of the gap's starting position.
     */
    public PlasticPipes(double gapStartY) {
        super(gapStartY, PIPE.getWidth());
    }

    /**
     * Takes in gapStartY, scale, and instantiates a plastic pipe.
     * @param gapStartY double, Y value of the gap's starting position.
     * @param scale The timeScale object, affecting the speed of objects.
     */
    public PlasticPipes(double gapStartY, TimeScale scale) {
        super(gapStartY, PIPE.getWidth(), scale);
    }

    /**
     * Draws both the pipes to the window.
     * Returns nothing.
     * @return void
     */
    @Override
    public void draw() {
        PIPE.drawFromTopLeft(getPipeX(), getBottomPipeY(),
                topPipeOptions.setRotation(Math.toRadians(ROTATION_DEGREES)));
        PIPE.drawFromTopLeft(getPipeX(), getTopPipeY());
    }

    /**
     * Takes in weapon and takes damage.
     * @param damage The damage object, in this case weapon, which inflicts damage on the pipes.
     * @return boolean Returns true if the pipe is alive, and false otherwise.
     */
    @Override
    public boolean takeDamage(Weapon damage) {
        perish();
        return false;
    }
}
