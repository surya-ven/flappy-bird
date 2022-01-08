/**
 * Represents an abstract controllable object which is movable.
 */
public abstract class ControllableObject implements Movable {
    private final double DEFAULT_INITIAL_VELOCITY = 0;
    private double velocity;

    /**
     * Creates a controllable object with a default velocity.
     */
    public ControllableObject() {
        this.velocity = DEFAULT_INITIAL_VELOCITY;
    }

    /**
     * Instantiates a controllable object with a provided velocity.
     * @param velocity double, the initial velocity which the controllable object is set to.
     */
    public ControllableObject(double velocity) {
        this.velocity = velocity;
    }

    /**
     * Moves the object, returns nothing.
     * @return void
     */
    @Override
    public abstract void move();

    protected double getVelocity() {
        return velocity;
    }

    protected void setVelocity(double velocity) {
        this.velocity = velocity;
    }
}
