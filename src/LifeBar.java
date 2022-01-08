import bagel.*;

/**
 * Represents a life bar, allowing the life bar to draw and store the number of lives left for the player.
 */
public class LifeBar implements Drawable {
    // Images
    private final Image FULL_LIFE = new Image("res/level/fullLife.png");
    private final Image NO_LIFE = new Image("res/level/noLife.png");

    // Position
    private final int ERROR = -1;
    private final double LIFE_X_START = 100;
    private final double LIFE_Y = 15;
    private final double LIFE_SPACING = 50;

    // Lives
    private boolean[] lifeBar;
    private int lives;

    /**
     * Takes in lives and instantiates a LifeBar.
     * @param lives integer, the max lives that the player starts with.
     */
    public LifeBar(int lives) {
        if (lives <= 0) {
            System.out.println("Invalid lives.");
            System.exit(ERROR);
        }
        this.lives = lives;
        lifeBar = new boolean[lives];
        for (int i = 0; i < lifeBar.length; i++) {
            lifeBar[i] = true;
        }
    }

    /**
     * Takes in damage, an integer specifying the amount of damage lifeBar should take.
     * Calculates whether the entity is alive after taking damage, decreases lives, and
     * then returns true if the entity is still alive and returns false if it is not alive.
     * @param damage integer, the damage taken by the life bar.
     * @return boolean Returns true if the entity using the life bar is alive, and false otherwise.
     */
    public boolean decrease(int damage) {
        boolean isAlive = true;

        // Decrease lives and perish
        if (lives > 0 && (lives - damage) <= 0) {
            lives -= damage;
            isAlive = false;
        }
        // Decrease lives
        else if ((lives - damage) > 0) {
            lives -= damage;
        }
        // Error
        else {
            System.out.println("Error - bird already lost.");
            System.exit(ERROR);
        }
        setLifeBars(lives);
        return isAlive;
    }

    /**
     * Draws the lifeBar to the window based on its fixed coordinates.
     * Returns nothing.
     * @return void
     */
    @Override
    public void draw() {
        double lifeX = LIFE_X_START;

        // Draw hearts based on boolean values
        for (int i = 0; i < lifeBar.length; i++) {
            if (lifeBar[i]) {
                FULL_LIFE.drawFromTopLeft(lifeX, LIFE_Y);
            } else {
                NO_LIFE.drawFromTopLeft(lifeX, LIFE_Y);
            }
            // Add spacing
            if (i < lifeBar.length - 1) {
                lifeX += LIFE_SPACING;
            }
        }
    }

    private void setLifeBars(int lives) {
        // Set no lives based on lives
        if (lives < lifeBar.length && lives >= 0) {
            for (int i = lives; i < lifeBar.length; i++) {
                lifeBar[i] = false;
            }
        }
        // Error
        else if (lives >= lifeBar.length) {
            System.out.println("Error - lives to set need to be less than total lives.");
            System.exit(ERROR);
        }
    }

    /**
     * Gets the lives value.
     * @return int Returns the number of lives left.
     */
    public int getLives() {
        return lives;
    }
}
