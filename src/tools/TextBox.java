package tools;

public class TextBox {
    private String text;
    private Integer x, y, boxHeight, boxWidth, textHeightMax, textWidthMax, timeToLive;
    private Boolean alive = null;
    private Boolean paused;
    private int timeAlive = 0;
    private int borderThickness = 2;
    private int padding = 10;

    // Für text verfügbare Zeichen:
    // 1. Alphabet:      A-Z;
    // 2. Umlaute: Ä;  Ö;  Ü;  ß;
    // 3. Zahlen:        0-9;
    // 4. Sonderzeichen: ".";  ",";  ":";  "!";  "?";  "(";  ")";  Leerzeichen
    // SONDERREGELUNG: um ein Enterzeichen einzufügen, schreibe "(e)"
    // (Leerzeichen oder anderes Sonderzeichen (alle außer "(") davor ist immer noch notwendig, um das vorherige Wort abzuschließen)
    public TextBox(String text, int x, int y, int width, int height, int timeToLive, TextBoxManager textBoxManager) {
        this.text = text.toLowerCase();
        this.x = x;
        this.y = y;
        this.boxWidth = width;
        this.boxHeight = height;
        this.timeToLive = timeToLive;
        this.paused = true;
        textHeightMax = boxHeight - 2 * (borderThickness + padding);
        textWidthMax = boxWidth - 2 * (borderThickness + padding);
        textBoxManager.addTextBox(this);
    }

    public TextBox(String text, int x, int y, int width, int height, boolean alive, TextBoxManager textBoxManager) {
        this.text = text.toLowerCase();
        this.x = x;
        this.y = y;
        this.boxWidth = width;
        this.boxHeight = height;
        this.alive = alive;
        this.timeToLive = null;
        this.paused = true;
        textHeightMax = boxHeight - 2 * (borderThickness + padding);
        textWidthMax = boxWidth - 2 * (borderThickness + padding);
        textBoxManager.addTextBox(this);
    }

    public String getText() {
        return text;
    }

    public Boolean getAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
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

    public Integer getTimeToLive() {
        return timeToLive;
    }

    public void increaseTimeAlive() {
        timeAlive++;
    }

    public Integer getTimeAlive() {
        return timeAlive;
    }
}
