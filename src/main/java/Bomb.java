import java.util.Random;

import java.util.Random;

public class Bomb {
        private int row;
        private int column;
        private int oldRow;
        private int oldColumn;
        private final char symbol;

        public Bomb() {
            Random r = new Random();
            this.row = 1;
            this.column = r.nextInt(80);
            this.oldRow = 0;
            this.oldColumn = 0;
            this.symbol = '\u058D';
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
    public void moveBombDown() {
        oldColumn = column;
        oldRow = row;
        row +=1;
    }
}
