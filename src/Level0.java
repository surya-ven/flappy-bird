import bagel.Image;
import bagel.Input;
import java.util.LinkedList;
import java.util.Random;

/**
 * Represents Level 0, with the ability to update the main elements on the screen.
 */
public class Level0 extends Level {
    // Images
    private final Image BIRD_WING_DOWN = new Image("res/level-0/birdWingDown.png");
    private final Image BIRD_WING_UP = new Image("res/level-0/birdWingUp.png");
    private final Image BACKGROUND = new Image("res/level-0/background.png");

    // Level flow
    private final int START_LIVES = 3;
    private static final int MAX_SCORE = 10;
    private final int[] GAPS = new int[]{100, 300, 500};

    /**
     * Takes in initialScore, then instantiates a Level.
     * @param initialScore integer, the initial score which the level should start at.
     */
    public Level0(int initialScore) {
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
        gapStartY = GAPS[random.nextInt(GAPS.length)];

        // Pipe movement
        if (!getIsInitialRender()) {
            setFrameCountPipes(getFrameCountPipes() + 1);

            // Add unused/dead pipes to clean up
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
                pipes.addFirst(new PlasticPipes(gapStartY, timeScale));
            }

        } else {
            // Initial render
            setFrameCountPipes(0);
            setIsInitialRender(!getIsInitialRender());
            pipes.addFirst(new PlasticPipes(gapStartY, timeScale));
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
