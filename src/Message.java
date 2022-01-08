import bagel.Font;
import bagel.Window;
import bagel.util.Point;

/**
 * Represents a message, and includes key message actions such as drawing and positioning of messages to be
 * displayed to the screen.
 */
public class Message {
    // Font styles
    private final int FONT_SIZE;
    private final Font FONT;

    /**
     * Takes in font and fontSize, and instantiates a Message.
     * @param font Font object, which is the font to be used for messsages.
     * @param fontSize integer, the font size to be used for messages.
     */
    public Message(Font font, int fontSize) {
        this.FONT = font;
        this.FONT_SIZE = fontSize;
    }

    /**
     * Takes in message, a string, and draws the string at the centre of the screen.
     * Returns nothing.
     * @param message Message object, the message to be displayed on the screen.
     * @return void
     */
    public void drawStringCentred(String message) {
        Point point = getStringCentre(message);
        FONT.drawString(message, point.x, point.y);
    }

    /**
     * Takes message, paddingX, paddingY, and draws the String at the centre of the screen
     * with optional padding from the centre.
     * Returns nothing.
     * @param message Message object, a string to be displayed on the screen.
     * @param paddingX double, the horizontal padding.
     * @param paddingY double, the vertical padding.
     * @return void
     */
    public void drawStringCentred(String message, double paddingX, double paddingY) {
        Point point = getStringCentre(message);
        FONT.drawString(message, point.x + paddingX, point.y +
                paddingY);
    }

    /**
     * Takes in message, point, and draws the message at that point.
     * Returns nothing.
     * @param message a string to be displayed on the screen.
     * @param point Point object which the message is to be drawn.
     * @return void
     */
    public void drawString(String message, Point point) {
        FONT.drawString(message, point.x, point.y);
    }


    private Point getStringCentre(String message) {
        double stringWidth, leftPadding, topPadding;
        stringWidth = FONT.getWidth(message);

        // Centre horizontally
        leftPadding = ((double) Window.getWidth() - stringWidth) / 2;

        // Mathematically centre vertically
        topPadding = ((double)Window.getHeight() + (double)FONT_SIZE) / 2;

        // Uncomment below to visually centre vertically
        // topPadding = ((double)Window.getHeight() + (double)FONT_SIZE / 2) / 2;

        return new Point(leftPadding, topPadding);
    }
}
