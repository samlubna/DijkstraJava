public final class ANSI {
    // Cursor navigation controls
    public static final String ESC = "\u001B[";
    public static final String RESET_CURSOR = ESC + "0G";
    public static final String START_COLOR = ESC + "31m";
    public static final String END_COLOR = ESC + "0m";
    public static final String ClEAR_LINE = ESC + "0K";
    public static final char UP = 'A';
    public static final char DOWN = 'B';
    public static final char LEFT = 'D';
    public static final char RIGHT = 'C';
}
