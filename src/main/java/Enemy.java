import java.util.Random;

public class Enemy {
    private int row;
    private int column;
    private int oldRow;
    private int oldColumn;
    private final char symbol;


    public Enemy() {
        Random r = new Random();
        this.row = r.nextInt(24);
        this.column = r.nextInt(80);
        this.oldRow = 24;
        this.oldColumn = 80;
        this.symbol = '\u058D';
    }

    public void moveEnemyUp() {
        oldColumn = column;
        oldRow = row;
        row -=0;
    }

    public void moveEnemyDown() {
        oldColumn = column;
        oldRow = row;
        row +=0;
    }

    public void moveEnemyLeft() {
        oldColumn = column;
        oldRow = row;
        column -=0;
    }

    public void moveEnemyRight() {
        oldColumn = column;
        oldRow = row;
        column +=0;
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

