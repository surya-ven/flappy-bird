import bagel.Image;
import bagel.Window;

public class Rock extends Weapon {
    private final static Image ROCK = new Image("res/level-1/rock.png");
    private final int MAX_SHOT_DISTANCE_FRAMES = 25;

    /**
     * Takes in velocity and rockY, then instantiates a Rock.
     * @param velocity double, velocity which the rock is travelling.
     * @param rockY double, Y position which the rock starts from.
     */
    public Rock(double velocity, double rockY) {
        super(velocity, rockY, ROCK);
    }

    /**
     * Takes in velocity, rockY, and timeScale, then instantiates a Rock.
     * @param velocity double, velocity which the rock is travelling.
     * @param rockY double, Y position which the rock starts from.
     * @param scale The timeScale object representing the time mechanics dictating the rock.
     */
    public Rock(double velocity, double rockY, TimeScale scale) {
        super(velocity, rockY, scale, ROCK);
    }

    /**
     * Draws weapon to the window, returns nothing.
     * @return void
     */
    @Override
    public void draw() {
        ROCK.drawFromTopLeft(getWeaponX(), getWeaponY());
    }

    /**
     * Checks whether the weapon is outside the window.
     * @return boolean Returns true if the weapon is outside the window, and false otherwise.
     */
    @Override
    public boolean isOutOfFrame() {
        if ((getWeaponX() + ROCK.getWidth()) < 0 || (getWeaponX() > Window.getWidth())) {
            return true;
        }
        return false;
    }

    /**
     * Moves the weapon alongside the bird, returns nothing.
     * @param bird The bird object, with which the weapon moves with.
     * @return void
     */
    @Override
    public void moveWithBird(Bird bird) {
        moveTo(bird.getBird().right(), bird.getBird().centre().y - ROCK.getHeight() / 2);
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
     * @param pipes Pipe object, on which the collision is to be detected on.
     * @return boolean Returns true if a collision occurred, and false otherwise.
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
