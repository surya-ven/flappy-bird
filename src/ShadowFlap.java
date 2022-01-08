import bagel.*;
import java.util.LinkedList;

/*
// Packages for testing, uncomment to enable grid lines
import bagel.util.Colour;
import bagel.util.Rectangle;
 */

/**
 * Implementation of ShadowFlap game through partial use of my (Surya Venkatesh) project 1 code.
 * SWEN20003 Project 2, Semester 2, 2021.
 * @author: Surya Venkatesh
 */
public class ShadowFlap extends AbstractGame {
    // Font and messages
    private final int FONT_SIZE = 48;
    private final Font FONT = new Font("res/font/slkscr.ttf", FONT_SIZE);
    private final String START_MESSAGE = "PRESS SPACE TO START";
    private final String WIN_MESSAGE = "CONGRATULATIONS!";
    private final String FINAL_SCORE_MESSAGE = "FINAL SCORE:";
    private final String GAME_LOST_MESSAGE = "GAME OVER";
    private final String LEVEL_UP_MESSAGE = "LEVEL-UP!";
    private final int FINAL_SCORE_TOP_PADDING = 75;

    // Game flow
    private final int NO_PADDING = 0;
    private final int SUCCESS = 0;
    private final int INITIAL_SCORE = 0;
    private final int PAUSE_MESSAGE_FRAMES = 20;
    private final int LAST_LEVEL = 1;
    private int frameCountMessage;
    private int score;
    private boolean isGameRunning;
    private boolean isWon;

    // Game objects
    private LinkedList<Pipes> pipes;
    private LinkedList<Weapon> weapons;
    private Background background;
    private Bird bird;
    private Message message;
    private TimeScale timeScale;
    private LinkedList<Level> levels;


    public ShadowFlap() {
        super(1024, 768, "Shadow Flap");
        score = INITIAL_SCORE;
        frameCountMessage = 0;
        isGameRunning = false;
        isWon = false;

        // Add levels
        levels = new LinkedList<>();
        levels.addFirst(new Level0(INITIAL_SCORE));
        levels.addFirst(new Level1(INITIAL_SCORE));

        bird = levels.getLast().createBird();
        background = levels.getLast().createBackground();
        pipes = new LinkedList<>();
        weapons = new LinkedList<>();
        message = new Message(FONT, FONT_SIZE);
        timeScale = new TimeScale();
    }

    /**
     * The entry point for the program.
     * @return void
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }


    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     * @param input Input object from which key commands are detected.
     * @return void
     */
    @Override
    public void update(Input input) {
        checkExit(input);
        background.draw();

        // Text-centre test
        // Drawing.drawLine(new Point(0, CENTRE.y), new Point(Window.getWidth(), CENTRE.y), 1, Colour.BLACK);

        // Pre game message
        if (!isGameRunning) {
            checkStart(input);
        }

        // Game running
        if (isGameRunning) {
            if (!levels.getLast().getIsGameOver() && levels.size() > 0) {
                // Level up
                if (levels.getLast().getIsLevelCompleted()) {
                    levelUp();
                }
                // Continue through level
                else {
                    levels.getLast().update(input, pipes, bird, weapons, background, timeScale, message);
                }
                // Update score
                score = levels.getLast().getScore();
            } else {
                determineOutcome();
            }
        }
    }

    private void determineOutcome() {
        // Determine outcome
        if (isWon) {
            // Won
            message.drawStringCentred(WIN_MESSAGE);
        } else {
            // Lost
            message.drawStringCentred(GAME_LOST_MESSAGE);
            // Display final score
            message.drawStringCentred(FINAL_SCORE_MESSAGE + " " + score,
                    NO_PADDING, FINAL_SCORE_TOP_PADDING);
        }
    }

    private void checkStart(Input input) {
        if (input.wasPressed(Keys.SPACE)) {
            isGameRunning = !isGameRunning;
        }
        message.drawStringCentred(START_MESSAGE);

        // Interval message
        levels.getLast().levelIntervalMessage(message);
    }

    private void checkExit(Input input) {
        if (input.wasPressed(Keys.ESCAPE)) {
            System.exit(SUCCESS);
        }
    }

    private void levelUp() {
        // Determine if game over
        if (frameCountMessage == 0) {
            if (levels.size() == LAST_LEVEL) {
                isWon = !isWon;
                levels.getLast().gameOver();
                return;
            }
        }
        frameCountMessage++;

        // Start new level
        if (frameCountMessage >= PAUSE_MESSAGE_FRAMES) {
            frameCountMessage = 0;
            levels.removeLast();
            bird = levels.getLast().createBird();
            pipes = new LinkedList<>();
            timeScale.reset();
            background = levels.getLast().createBackground();
            isGameRunning = !isGameRunning;
        }
        message.drawStringCentred(LEVEL_UP_MESSAGE);
    }
}
