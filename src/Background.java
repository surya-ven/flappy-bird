import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Produces a background object with a provided Image, with drawing functionality.
 */
public class Background implements Drawable {
    private final Point BG_POINT = new Point(0, 0);
    private Image background;
    private Rectangle backgroundBorder;

    /**
     * Takes in Image, sets Image as the background, and generates a rectangle
     * stored within the class.
     * @param image Image object to be set as the background.
     */
    public Background(Image image) {
        background = image;
        backgroundBorder = background.getBoundingBox();
    }

    /**
     * Takes in bird, Bird's rectangle, and determines whether the bird is outside of the
     * background area, and if so, returns True, and false otherwise.
     * @param bird Rectangle object representing the bird.
     * @return boolean
     */
    public boolean isOutOfBounds(Bird bird) {
        if (!backgroundBorder.intersects(bird.getBird())) {
            bird.respawn();
            return true;
        }
        return false;
    }

    /**
     * Draws background to the window, returns nothing.
     * @return void
     */
    @Override
    public void draw() {
        background.drawFromTopLeft(BG_POINT.x, BG_POINT.y);
    }
}
