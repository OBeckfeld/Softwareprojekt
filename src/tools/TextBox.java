package tools;

public class TextBox {
    private TextBoxManager textBoxManager;
    private String text;
    private int x, y, boxHeight, boxWidth, textHeightMax, textWidthMax, timeToLive;
    private int timeAlive = 0;
    private int borderThickness = 2;
    private int padding = 10;

    public TextBox(String text, int x, int y, int width, int height, int timeToLive, TextBoxManager textBoxManager) {
        this.text = text.toLowerCase();
        this.x = x;
        this.y = y;
        this.boxWidth = width;
        this.boxHeight = height;
        this.timeToLive = timeToLive;
        this.textBoxManager = textBoxManager;
        textHeightMax = boxHeight - 2 * (borderThickness + padding);
        textWidthMax = boxWidth - 2 * (borderThickness + padding);
        textBoxManager.addTextBox(this);
    }

    public String getText() {
        return text;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getBoxHeight() {
        return boxHeight;
    }

    public int getBoxWidth() {
        return boxWidth;
    }

    public int getTextHeightMax() {
        return textHeightMax;
    }

    public int getTextWidthMax() {
        return textWidthMax;
    }

    public int getBorderThickness() {
        return borderThickness;
    }

    public int getPadding() {
        return padding;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public void increaseTimeAlive() {
        timeAlive++;
    }

    public int getTimeAlive() {
        return timeAlive;
    }
}
