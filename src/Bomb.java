import bagel.Image;
import bagel.Window;

/**
 * Represents a bomb, allowing the bomb to move with bird, detecting collision, and checking whether its
 * out of frame.
 */
public class Bomb extends Weapon {
    private static final Image BOMB = new Image("res/level-1/bomb.png");
    private final int MAX_SHOT_DISTANCE_FRAMES = 50;

    /**
     * Takes in velocity and bombY, then instantiates a Bomb.
     * @param velocity double, the velocity which the bomb is travelling.
     * @param bombY double, the Y position which the bomb starts from.
     */
    public Bomb(double velocity, double bombY) {
        super(velocity, bombY, BOMB);
    }

    /**
     * Takes in velocity, bombY, and timeScale, then instantiates a Bomb.
     * @param velocity double, the velocity which the bomb is travelling.
     * @param bombY double, the Y position which the bomb starts from.
     * @param scale TimeScale object representing the time mechanics dictating the bomb.
     */
    public Bomb(double velocity, double bombY, TimeScale scale) {
        super(velocity, bombY, scale, BOMB);
    }

    /**
     * Draws weapon to the window, returns nothing.
     * @return void
     */
    @Override
    public void draw() {
        BOMB.drawFromTopLeft(getWeaponX(), getWeaponY());
    }

    /**
     * Checks whether the weapon is outside the window.
     * @return boolean Returns true if the weapon is outside the window, and false otherwise.
     */
    @Override
    public boolean isOutOfFrame() {
        if ((getWeaponX() + BOMB.getWidth()) < 0 || (getWeaponX() > Window.getWidth())) {
            return true;
        }
        return false;
    }

    /**
     * Moves the weapon alongside the bird, returns nothing.
     * @param bird Bird object with which the weapon moves with.
     * @return void
     */
    @Override
    public void moveWithBird(Bird bird) {
        moveTo(bird.getBird().right(), bird.getBird().centre().y - BOMB.getHeight() / 2);
    }

    /**
     * Checks whether the weapon has reached its shot range, and if so, perishes.
     * @return void
     */
    @Override
    public void shotRangeCheck() {
        if (getShotDistanceFrames() >= MAX_SHOT_DISTANCE_FRAMES) {
            perish();
        }
    }

    /**
     * Checks whether a collision occurred between a pipe and a weapon.
     * @param pipes Pipe object on which to check if a collision happened.
     * @return boolean returns true if a collision occurred, and false otherwise.
     */
    @Override
    public boolean detectShotCollision(Pipes pipes) {
        boolean pipeIsAlive = true;
        // Check if collision occurs
        if (pipes.getTopPipe().intersects(getWeapon()) ||
                pipes.getBottomPipe().intersects(getWeapon())) {
            // Pipe and weapon both take damage
            pipeIsAlive = pipes.takeDamage(this);
            perish();
        }
        return !pipeIsAlive;
    }
}
