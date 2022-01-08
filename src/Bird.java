import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.LinkedList;

/*
// Packages for testing, uncomment to enable grid lines
 import bagel.Drawing;
 import bagel.Window;
 import bagel.util.Colour;
 */

/**
 * Represents a bird, allowing bird to move, draw, attack, take damage, and detect if it went through pipes.
 */
public class Bird extends ControllableObject implements Drawable, Damageable<Integer>, Attacker {
    // movement characteristics
    private final int FLAP_SWITCH = 10;
    private final int BIRD_CENTRE_X = 200;
    private final int BIRD_SPAWN_CENTRE_Y = 350;
    private final double BIRD_ACCELERATION = 0.4;
    private final int MAX_VELOCITY = 10;
    private final int JUMP_VELOCITY = -6;
    private final int RESET = 0;

    // images
    private final Image BIRD_WING_DOWN;
    private final Image BIRD_WING_UP;

    // dimensions
    private final double BIRD_WIDTH;
    private final double BIRD_HEIGHT;

    // dynamic tracking
    private double birdCentreY;
    private int wingCounter;
    private Point topLeft;

    // Level
    private boolean isAlive;

    // Collision use
    private Rectangle bird;
    private LifeBar lifeBar;

    // Weapon
    private Weapon weapon;
    private boolean isPickedUp;

    /**
     * Initialises the Bird with default values, by taking in the birdWingDown and birdWingUp Images, in addition
     * to the maxLives.
     * @param birdWingDown Image object for bird wing down.
     * @param birdWingUp Image object for bird wing up.
     * @param maxLife integer, specifying the max number of lives the bird should have.
     */
    public Bird(Image birdWingDown, Image birdWingUp, int maxLife) {
        super();

        // Images
        this.BIRD_WING_DOWN = birdWingDown;
        this.BIRD_WING_UP = birdWingUp;

        // Dimensions
        this.BIRD_WIDTH = birdWingDown.getWidth();
        this.BIRD_HEIGHT = birdWingDown.getHeight();

        // Initial position
        this.wingCounter = 0;
        this.topLeft = new Point(BIRD_CENTRE_X - BIRD_WIDTH / 2, BIRD_SPAWN_CENTRE_Y - BIRD_HEIGHT / 2);
        this.birdCentreY = BIRD_SPAWN_CENTRE_Y;
        this.bird = new Rectangle(topLeft, BIRD_WIDTH, BIRD_HEIGHT);

        // Lives
        lifeBar = new LifeBar(maxLife);
        this.isAlive = true;

        // Weapon
        this.weapon = null;
        this.isPickedUp = false;
    }

    /**
     * Moves the bird upwards with the deemed jump velocity, returns nothing.
     * @return void
     */
    @Override
    public void move() {
        setVelocity(JUMP_VELOCITY);
        setBirdY(birdCentreY + getVelocity());

        // Weapon moves with bird
        if (weapon != null && isPickedUp) {
            weapon.moveWithBird(this);
        }
    }

    /**
     * Draws lifebar to the window, returns nothing.
     * @return void
     */
    public void drawLifeBar() {
        lifeBar.draw();
    }

    /**
     * Applies gravity to bird and weapon if picked up, returns nothing.
     * @return void
     */
    public void fall() {
        if (getVelocity() < MAX_VELOCITY) {
            setVelocity(getVelocity() + BIRD_ACCELERATION);
            if (getVelocity() > MAX_VELOCITY) {
                setVelocity(MAX_VELOCITY);
            }
        }
        setBirdY(birdCentreY + getVelocity());

        // Weapon falls with bird if picked up
        if (weapon != null && isPickedUp) {
            weapon.moveWithBird(this);
        }
    }

    /**
     * Draws bird based on positioning and flaps every FLAP_SWITCH frames, returns nothing.
     * @return void
     */
    @Override
    public void draw() {
        // Wing up
        if (wingCounter == FLAP_SWITCH) {
            setWingCounter(RESET);
            BIRD_WING_UP.draw(bird.centre().x, bird.centre().y);
        }

        // Wing down
        else {
            BIRD_WING_DOWN.draw(bird.centre().x, bird.centre().y);
            setWingCounter(wingCounter + 1);
        }

        /*
        // Bird alignment test
        Drawing.drawLine(new Point(bird.centre().x, Window.getHeight()),
                        new Point(bird.centre().x, 0), 1, Colour.BLACK);
         */
    }

    /**
     * Determines whether bird has passed pipes.
     * @param pipes Pipe object on which to determine if the bird crossed it.
     * @return boolean Returns true if passed and false otherwise.
     */
    public boolean throughPipes(Pipes pipes) {
        if (bird.centre().x > pipes.getBottomPipe().topRight().x &&
                bird.centre().x > pipes.getTopPipe().topRight().x) {
            pipes.birdIsThrough();
            return true;
        }
        return false;
    }


    /**
     * Returns the bird's rectangle.
     * @return Rectangle
     */
    public Rectangle getBird() {
        return bird;
    }

    private void moveBirdRect() {
        this.bird.moveTo(topLeft);
    }

    private void setWingCounter(int wingCounter) {
        this.wingCounter = wingCounter;
    }

    private void setBirdY(double birdCentreY) {
        this.birdCentreY = birdCentreY;
        this.topLeft = new Point(topLeft.x, birdCentreY - BIRD_HEIGHT / 2);
        moveBirdRect();
        /*
        // Bird debugging test
        Drawing.drawRectangle(topLeft, BIRD_WIDTH, BIRD_HEIGHT, Colour.WHITE);
         */
    }

    /**
     * Bird takes damage based off the specified damage value from the parameter.
     * @param damage integer, specifying the amount of damage bird should take.
     * @return boolean Returns whether the Bird is alive.
     */
    @Override
    public boolean takeDamage(Integer damage) {
        if (!lifeBar.decrease(damage)) {
            isAlive = !isAlive;
            return false;
        }
        return false;
    }

    /**
     * Spawns bird back to its inital starting position, returns nothing.
     * @return void
     */
    public void respawn() {
        setBirdY(BIRD_SPAWN_CENTRE_Y);
    }

    /**
     * Returns the isAlive value.
     * @return boolean Returns whether the Bird is alive.
     */
    public boolean getIsAlive() {
        return isAlive;
    }

    /**
     * Checks whether the bird collides with the pipes, return nothing.
     * @param bird Bird object, on which to check the collision for.
     * @param pipes LinkedList<Pipes> object, with pipes to check the collision for.
     * @param damage integer, the damage taken by the bird if a collision occurs.
     * @return void
     */
    public void collisionCheck(Bird bird, LinkedList<Pipes> pipes, int damage) {
        // Collision check
        if (pipes.getLast().hasCollided(bird.getBird())) {
            bird.takeDamage(damage);
            pipes.removeLast();
        }
    }

    /**
     * Checks whether the bird is within the scope of the background image by checking whether a collision
     * exists, returns nothing.
     * @param bird Bird object to check the collision for.
     * @param background Background object to check the collision for.
     * @param damage integer, the amount of damage taken by the bird if it leaves the background.
     * @return void
     */
    public void outOfBoundsCheck(Bird bird, Background background, int damage) {
        // Out of bounds
        if (background.isOutOfBounds(bird)) {
            bird.takeDamage(damage);
            if (bird.getIsAlive()) {
                bird.respawn();
            }
        }
    }

    /**
     * Weapon provided is picked up by the Bird, returns nothing.
     * @param weapon Weapon object which is being picked up by the Bird.
     * @return void
     */
    public void pickUpWeapon(Weapon weapon) {
        if (this.weapon != null) {
            this.weapon.perish();
        } else {
            isPickedUp = true;
        }
        this.weapon = weapon;
        weapon.setPickedUp(true);
    }

    /**
     * Performs single attack, returns nothing.
     * @return void
     */
    @Override
    public void attack() {
        weapon.shoot();
        weapon = null;
        isPickedUp = false;
    }

    /**
     * Checks whether an attack is triggered.
     * @param input Input object processing the key input.
     * @return boolean Returns true if an attack is triggered as true, and false otherwise.
     */
    public boolean triggeredAttack(Input input) {
        if (weapon != null && weapon.triggered(input)) {
            return true;
        }
        return false;
    }

    /**
     * Returns the weapon stored by the Bird.
     * @return Weapon The weapon stored by the bird.
     */
    public Weapon getWeapon() {
        return weapon;
    }
}
