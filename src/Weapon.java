import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Represents an abstract weapon, allowing it to be controlled, fired, and drawn.
 */
public abstract class Weapon extends ControllableObject implements Fireable, Drawable {
    // Dimensions
    private final double INITIAL_X = Window.getWidth();
    private final double SPEED_AFTER_SHOOTING = 5;

    // Weapon logic
    private double initialShotX;
    private double weaponX;
    private double weaponY;
    private boolean isPickedUp;
    private boolean isShot;
    private int shotDistanceFrames;
    private boolean exists;
    private TimeScale timeScale;
    private Rectangle weapon;

    /**
     * The initial velocity of a weapon.
     */
    public static final double INITIAL_VELOCITY = 3;

    /**
     * Takes in velocity, weaponY, image, and creates a Weapon.
     * @param velocity double, velocity at which the weapon initially starts at.
     * @param weaponY double, Y value of the weapon.
     * @param image The Image object which represents the weapon.
     */
    public Weapon(double velocity, double weaponY, Image image) {
        super(velocity);
        this.weaponX = INITIAL_X;
        this.weaponY = weaponY;
        this.weapon = new Rectangle(getWeaponX(), getWeaponY(), image.getWidth(), image.getHeight());
        this.isPickedUp = false;
        this.isShot = false;
        this.shotDistanceFrames = 0;
        this.exists = true;
        this.timeScale = null;
    }

    /**
     * Takes in velocity, weaponY, scale, image, and creates a Weapon.
     * @param velocity double, velocity at which the weapon initially starts at.
     * @param weaponY double, Y value of the weapon.
     * @param image The Image object which represents the weapon.
     * @param scale The TimeScale object which determines the speed of objects within the window.
     */
    public Weapon(double velocity, double weaponY, TimeScale scale, Image image) {
        this(velocity, weaponY, image);
        this.timeScale = scale;
    }

    /**
     * Checks whether an attack is triggered.
     * @param input The Input object processing the key input.
     * @return boolean Returns true if an attack is triggered as true, and false otherwise.
     */
    @Override
    public boolean triggered(Input input) {
        if (input.wasPressed(Keys.S) && isPickedUp) {
            isPickedUp = false;
            isShot = true;
            return true;
        }
        return false;
    }

    /**
     * Shoots the weapon, returns nothing.
     * @return void
     */
    @Override
    public void shoot() {
        if (isShot) {
            setVelocity(SPEED_AFTER_SHOOTING);
            initialShotX = getWeaponX();
        }
    }

    /**
     * Moves weapons based on a given step value.
     * Returns nothing.
     * @return void
     */
    public void move() {
        // calculate and change to new position
        if (getScale() != null) {
            setVelocity(Weapon.INITIAL_VELOCITY * Math.pow(TimeScalable.MULTIPLIER, getTimeScale() - 1));
        } else {
            setVelocity(Weapon.INITIAL_VELOCITY);
        }

        // Weapon is shot
        if (getIsShot()) {
            setShotDistanceFrames(getShotDistanceFrames() + 1);
            setWeaponX(getWeaponX() + getVelocity());
            setWeapon(new Point(getWeaponX(), getWeaponY()));
        }
        // Weapon is not shot
        else {
            setWeaponX(getWeaponX() - getVelocity());
            setWeapon(new Point(getWeaponX(), getWeaponY()));
        }
    }

    protected void moveTo(double x, double y) {
        setWeaponX(x);
        setWeaponY(y);
        setWeapon(new Point(x, y));
    }

    /**
     * Checks whether a weather a rectangle intersects a weapon.
     * @param rectangle The Rectangle object which the collision is checked on.
     * @return boolean Returns true if the weapon intersects the rectangle and false otherwise.
     */
    public boolean intersects(Rectangle rectangle) {
        if (getWeapon().intersects(rectangle)) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether a collision occurred between a pipe and a weapon.
     * @param pipes Pipe object on which to check if a collision happened.
     * @return boolean Returns true if a collision occurred, and false otherwise.
     */
    public abstract boolean detectShotCollision(Pipes pipes);

    /**
     * Sets the isPickedUp value to the value specified, returns nothing.
     * @param isPickedUp boolean, true if the weapon has been picked up and false otherwise.
     * @return void
     */
    public void setPickedUp(boolean isPickedUp) {
        this.isPickedUp = isPickedUp;
    }

    protected double getWeaponX() {
        return weaponX;
    }

    protected double getWeaponY() {
        return weaponY;
    }

    protected void setWeaponX(double weaponX) {
        this.weaponX = weaponX;
    }

    protected void setWeaponY(double weaponY) {
        this.weaponY = weaponY;
    }

    /**
     * Draws weapon to the window, returns nothing.
     * @return void
     */
    @Override
    public abstract void draw();

    /**
     * Checks whether the weapon is outside the window.
     * @return boolean Returns true if the weapon is outside the window, and false otherwise.
     */
    public abstract boolean isOutOfFrame();

    /**
     * Gets the isShot value.
     * @return boolean Returns whether the weapon has been shot.
     */
    public boolean getIsShot() {
        return isShot;
    }

    /**
     * Gets the isPickedUp value.
     * @return boolean Returns whether the weapon has been picked up.
     */
    public boolean getIsPickedUp() {
        return isPickedUp;
    }

    /**
     * Moves the weapon alongside the bird, returns nothing.
     * @param bird The bird object with which the weapon moves with.
     * @return void
     */
    public abstract void moveWithBird(Bird bird);

    protected void perish() {
        exists = false;
    }

    /**
     * Gets the exists value.
     * @return boolean Returns whether the pipe exists.
     */
    public boolean getExists() {
        return exists;
    }

    /**
     * Checks whether the weapon has reached its shot range, and if so, perishes.
     * @return void
     */
    public abstract void shotRangeCheck();

    protected TimeScale getScale() {
        return timeScale;
    }

    protected double getTimeScale() {
        return timeScale.getTimeScale();
    }

    protected int getShotDistanceFrames() {
        return shotDistanceFrames;
    }

    protected void setShotDistanceFrames(int shotDistanceFrames) {
        this.shotDistanceFrames = shotDistanceFrames;
    }

    /**
     * Gets the weapon rectangle.
     * @return Rectangle The rectangle representing the weapon within the class.
     */
    public Rectangle getWeapon() {
        return weapon;
    }

    protected void setWeapon(Point point) {
        weapon.moveTo(point);
    }
}
