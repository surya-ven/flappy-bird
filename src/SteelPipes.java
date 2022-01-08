import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/*
// For illustrating and debugging the flame Rectangles - see below
import bagel.Drawing;
import bagel.util.Colour;
 */

/**
 * Represents steel pipes, allowing pipes to move and detect collisions with bird.
 */
public class SteelPipes extends Pipes {
    // Image
    private static final Image PIPE = new Image("res/level-1/steelPipe.png");
    private static final Image FLAME = new Image("res/level-1/flame.png");

    // Dimensions
    private final int ROTATION_DEGREES = 180;

    private final int BOMB_TEST = 0;

    // Drawing and collision use
    private DrawOptions topPipeOptions = new DrawOptions();

    // Flames
    private final int FLAME_THRESHOLD_FRAMES = 20;
    private final int FLAME_PAUSE = 3;
    private Point topFlameTopLeft;
    private Point bottomFlameTopLeft;
    private Rectangle topFlame;
    private Rectangle bottomFlame;
    private int frameCount;

    /**
     * Takes in gapStartY, and instantiates a steel pipe.
     * @param gapStartY double, Y value of the gap's starting position.
     */
    public SteelPipes(double gapStartY) {
        super(gapStartY, PIPE.getWidth());
        this.frameCount = 0;

        this.topFlameTopLeft = new Point(getPipeX(), getTopPipeY() + PIPE.getHeight());
        this.bottomFlameTopLeft = new Point(getPipeX(), getBottomPipeY() - FLAME.getHeight());

        this.topFlame = new Rectangle(topFlameTopLeft.x, topFlameTopLeft.y, FLAME.getWidth(), FLAME.getHeight());
        this.bottomFlame = new Rectangle(bottomFlameTopLeft.x, bottomFlameTopLeft.y,
                FLAME.getWidth(), FLAME.getHeight());
    }

    /**
     * Takes in gapStartY, scale, and instantiates a steel pipe.
     * @param gapStartY double, Y value of the gap's starting position.
     * @param scale The timeScale object affecting the speed of objects.
     */
    public SteelPipes(double gapStartY, TimeScale scale) {
        super(gapStartY, PIPE.getWidth(), scale);
        this.frameCount = 0;

        this.topFlameTopLeft = new Point(getPipeX(), getTopPipeY() + PIPE.getHeight());
        this.bottomFlameTopLeft = new Point(getPipeX(), getBottomPipeY() - FLAME.getHeight());

        this.topFlame = new Rectangle(topFlameTopLeft.x, topFlameTopLeft.y, FLAME.getWidth(), FLAME.getHeight());
        this.bottomFlame = new Rectangle(bottomFlameTopLeft.x, bottomFlameTopLeft.y,
                FLAME.getWidth(), FLAME.getHeight());
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

        if (frameCount > FLAME_THRESHOLD_FRAMES - FLAME_PAUSE &&
                frameCount <= FLAME_THRESHOLD_FRAMES) {
            /*

            // Uncomment to check that the flame rectangles are showing up correctly.
            // A white rectangle will show up behind the flames with the same position
            // as the flame Rectangle collision object here.

            Drawing drawing = new Drawing();
            drawing.drawRectangle(getPipeX(), getBottomPipeY() - FLAME.getHeight(),
                    FLAME.getWidth(), FLAME.getHeight(), Colour.WHITE);
            drawing.drawRectangle(getPipeX(), getTopPipeY() + PIPE.getHeight(),
                    FLAME.getWidth(), FLAME.getHeight(), Colour.WHITE);

             */

            FLAME.drawFromTopLeft(getPipeX(), getBottomPipeY() - FLAME.getHeight(),
                    topPipeOptions.setRotation(Math.toRadians(ROTATION_DEGREES)));
            FLAME.drawFromTopLeft(getPipeX(), getTopPipeY() + PIPE.getHeight());
        }
    }

    /**
     * Checks if a collision with pi[es has occurred with a rectangle.
     * Returns true if intersection exists and false otherwise.
     * @param rect The rectangle object to determine whether a collision exists.
     * @return boolean Returns true if a collision occurred, and false otherwise.
     */
    @Override
    public boolean hasCollided(Rectangle rect) {
        if (super.hasCollided(rect) || hasCollidedFlames(rect)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if a collision with flames has occurred with a rectangle.
     * Returns true if intersection exists and false otherwise.
     * @param rect The rectangle object, to determine whether a collision exists.
     * @return boolean Returns true if a collision occurred, and false otherwise.
     */
    private boolean hasCollidedFlames(Rectangle rect) {
        if (frameCount > FLAME_THRESHOLD_FRAMES - FLAME_PAUSE &&
                frameCount <= FLAME_THRESHOLD_FRAMES &&
                (rect.intersects(topFlame) || rect.intersects(bottomFlame))) {
            return true;
        }
        return false;
    }

    /**
     * Moves both pipes based on a given step value.
     * Returns nothing.
     * @return void
     */
    @Override
    public void move() {
        // Move pipes
        super.move();

        // Move flames
        topFlame.moveTo(new Point(getPipeX(), getTopPipeY() + PIPE.getHeight()));
        bottomFlame.moveTo(new Point(getPipeX(), getBottomPipeY() - FLAME.getHeight()));
        if (frameCount == FLAME_THRESHOLD_FRAMES) {
            frameCount = 0;
        }
        frameCount++;
    }

    /**
     * Takes in weapon and takes damage.
     * @param damage The damage object, in this case weapon, which inflicts damage on the pipes.
     * @return boolean Returns true if the pipe is alive, and false otherwise.
     */
    @Override
    public boolean takeDamage(Weapon damage) {
        Weapon test = new Bomb(BOMB_TEST, BOMB_TEST);
        if (damage.getClass() == test.getClass()) {
            perish();
            return false;
        }
        return true;
    }
}
