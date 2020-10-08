public class Player {
    private int row;
    private int column;
    private int oldRow;
    private int oldColumn;
    private final char symbol;
    private int points;

    public Player(int row, int column) {
        this.row = row;
        this.column = column;
        this.oldRow = 24;
        this.oldColumn = 80;
        this.symbol = '\u0398';
        this.points = 0;
    }

    public void increasePoints(int add) {
        points += add;
    }

    public void movePlayerUp() {
        oldColumn = column;
        oldRow = row;
        row -=1;
    }

    public void movePlayerDown() {
        oldColumn = column;
        oldRow = row;
        row +=1;
    }

    public void movePlayerLeft() {
        oldColumn = column;
        oldRow = row;
        column -=1;
    }

    public void movePlayerRight() {
        oldColumn = column;
        oldRow = row;
        column +=1;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getOldRow() {
        return oldRow;
    }

    public void setOldRow(int oldRow) {
        this.oldRow = oldRow;
    }

    public int getOldColumn() {
        return oldColumn;
    }

    public void setOldColumn(int oldColumn) {
        this.oldColumn = oldColumn;
    }

    public char getSymbol() {
        return symbol;
    }
}
