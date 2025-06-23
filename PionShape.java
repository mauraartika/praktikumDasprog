public enum PionShape {
    X("X"),
    O("O"),
    HEART("♥"),
    STAR("★"),
    SUN("☀");

    private final String symbol;

    PionShape(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static PionShape fromString(String shape) {
        switch (shape) {
            case "X": return PionShape.X;
            case "O": return PionShape.O;
            case "♥": return PionShape.HEART;
            case "★": return PionShape.STAR;
            case "☀": return PionShape.SUN;
            default: return PionShape.X;
        }
    }
}