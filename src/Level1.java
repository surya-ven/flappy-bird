import bagel.Image;
import bagel.Input;
import bagel.Keys;
import java.util.LinkedList;
import java.util.Random;

/**
 * Represents Level 1, with the ability to update the main elements on the screen.
 */
public class Level1 extends Level {
    // Images and messages
    private final Image BIRD_WING_DOWN = new Image("res/level-1/birdWingDown.png");
    private final Image BIRD_WING_UP = new Image("res/level-1/birdWingUp.png");
    private final Image BACKGROUND = new Image("res/level-1/background.png");
    private final String SHOOT_MESSAGE = "PRESS 'S' TO SHOOT";
    private final int SHOOT_MESSAGE_TOP_PADDING = 68;

    // Level flow
    private final int START_LIVES = 6;
    private static final int MAX_SCORE = 30;
    private final int GAP_MAX = 500;
    private final int GAP_MIN = 100;

    /**
     * Takes in initialScore, then instantiates a Level.
     * @param initialScore integer, representing the initial score which the level should start at.
     */
    public Level1(int initialScore) {
        super(initialScore, MAX_SCORE);
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
    @Override
    public void update(Input input, LinkedList<Pipes> pipes, Bird bird, LinkedList<Weapon> weapons,
                       Background background, TimeScale timeScale, Message message) {
        bird.drawLifeBar();
        timeScale.timeScale(input);
        updatePipes(pipes, timeScale);
        updateWeapons(weapons, pipes, bird, timeScale);

        // Collisions
        bird.collisionCheck(bird, pipes, getBIRD_DAMAGE());
        bird.outOfBoundsCheck(bird, background, getBIRD_DAMAGE());

        // Determine if bird is alive
        if (bird.getIsAlive()) {
            updateBird(input, pipes, bird, message);
        } else {
            setIsGameOver(!getIsGameOver());
        }
    }

    @Override
    protected void updatePipes(LinkedList<Pipes> pipes, TimeScale timeScale) {
        double multiplier = 0;
        int gapStartY = 0;
        Random random = new Random();
        LinkedList<Pipes> cleanUpPile = new LinkedList<>();

        // Get random gap start Y value
        gapStartY = (GAP_MIN + getEXCLUSIVE()) + random.nextInt(GAP_MAX - GAP_MIN - getEXCLUSIVE());

        // Pipe movement
        if (!getIsInitialRender()) {
            setFrameCountPipes(getFrameCountPipes() + 1);

            // Add unused/destroyed pipes to clean up
            for (Pipes pipe: pipes) {
                if (!pipe.getExists()) {
                    cleanUpPile.addFirst(pipe);
                    continue;
                }
                pipe.move();
            }

            // Remove from cleanup
            for (Pipes pipe: cleanUpPile) {
                pipes.remove(pipe);
            }

            // Add pipes
            if (getFrameCountPipes() >= Math.round(getPipeInterval())) {
                setFrameCountPipes(0);
                if (random.nextBoolean()) {
                    pipes.addFirst(new PlasticPipes(gapStartY, timeScale));
                } else {
                    pipes.addFirst(new SteelPipes(gapStartY, timeScale));
                }
            }

        } else {
            // Initial render
            setFrameCountPipes(0);
            setIsInitialRender(!getIsInitialRender());
            if (random.nextBoolean()) {
                pipes.addFirst(new PlasticPipes(gapStartY, timeScale));
            } else {
                pipes.addFirst(new SteelPipes(gapStartY, timeScale));
            }
        }

        // Remove pipes
        if (pipes.size() > 0 && pipes.getLast().isOutOfFrame()) {
            pipes.removeLast();
        }

        // pipes.getLast().timeScale(input);
        multiplier = pipes.getLast().getSpeedMultiplier();
        setPipeInterval(getINITIAL_PIPE_INTERVAL() / multiplier);

        // Display pipes
        for (Pipes pipe: pipes) {
            pipe.draw();
        }

    }

    private void updateWeapons(LinkedList<Weapon> weapons, LinkedList<Pipes> pipes, Bird bird, TimeScale timeScale) {
        boolean hasCollided = true, isWeaponInterval;
        int weaponInterval;
        double weaponY;
        Weapon newWeapon = null;
        Random random = new Random();
        LinkedList<Weapon> cleanUpPile = new LinkedList<>();
        double velocity;

        for (Weapon weapon: weapons) {
            // Add unused/destroyed weapons to clean up
            if (!weapon.getExists()) {
                cleanUpPile.addFirst(weapon);
                continue;
            }

            // Weapon is shot
            if (weapon.getIsShot()) {
                weapon.shotRangeCheck();

                // Determine if weapon hits pipe
                for (Pipes pipe: pipes) {
                    if (weapon.detectShotCollision(pipe)) {
                        setScore(getScore() + 1);
                    }
                }
            }

            // Move
            if (!weapon.getIsPickedUp()) {
                weapon.move();
            }

            // Picked up weapon
            if (bird.getWeapon() != weapon && weapon.intersects(bird.getBird())) {
                bird.pickUpWeapon(weapon);
            }
        }

        // Remove from clean up
        for (Weapon weapon: cleanUpPile) {
            weapons.remove(weapon);
        }

        // Weapon spawning
        weaponInterval = (int)(getPipeInterval() * getWEAPON_MULTIPLIER());
        isWeaponInterval = random.nextDouble() < getWEAPON_PROBABILITY();

        if (getFrameCountPipes() == weaponInterval && isWeaponInterval) {
            // Loop until no collisions detected
            while (hasCollided) {
                weaponY = GAP_MIN + random.nextDouble() * (GAP_MAX - GAP_MIN);
                velocity = Weapon.INITIAL_VELOCITY * Math.pow(TimeScalable.MULTIPLIER,
                        timeScale.getTimeScale() - 1);

                // Choose weapon
                if (random.nextBoolean()) {
                    newWeapon = new Rock(velocity, weaponY, timeScale);
                } else {
                    newWeapon = new Bomb(velocity, weaponY, timeScale);
                }

                // Determine if weapon intersects itself
                if (!(weapons.size() > 0 && newWeapon.intersects(weapons.getFirst().getWeapon()))) {
                    hasCollided = false;
                }

                if (!hasCollided) {
                    weapons.addFirst(newWeapon);
                }
            }
        }

        // Draw
        for (Weapon weapon: weapons) {
            weapon.draw();
        }

        // Remove weapons
        if (weapons.size() > 0 && weapons.getLast().isOutOfFrame()) {
            weapons.removeLast();
        }

    }

    @Override
    protected void updateBird(Input input, LinkedList<Pipes> pipes, Bird bird, Message message) {
        // Bird movement
        if (input.wasPressed(Keys.SPACE)) {
            bird.move();
        } else {
            bird.fall();
        }
        // Flap and draw bird
        bird.draw();

        // Attack
        if (bird.triggeredAttack(input)) {
            bird.attack();
        }

        updateScore(pipes, bird, message);
    }

    /**
     * Level's interval message, returns nothing.
     * @param message Message object, used to display messages.
     * @return void
     */
    @Override
    public void levelIntervalMessage(Message message) {
        message.drawStringCentred(SHOOT_MESSAGE, getNO_PADDING(), SHOOT_MESSAGE_TOP_PADDING);
    }

    /**
     * Creates a bird object and returns it.
     * @return Bird Newly created bird object.
     */
    @Override
    public Bird createBird() {
        return new Bird(BIRD_WING_DOWN, BIRD_WING_UP, START_LIVES);
    }

    /**
     * Creates a background object and returns it.
     * @return Background Newly created background object.
     */
    @Override
    public Background createBackground() {
        return new Background(BACKGROUND);
    }
}
