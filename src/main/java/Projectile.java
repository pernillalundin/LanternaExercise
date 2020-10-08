import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;

public class Projectile {
    private int row;
    private int column;
    private int oldRow;
    private int oldColumn;
    private char symbol;
    boolean isActive;
    Terminal terminal;

    public Projectile(int row, int column, Terminal terminal) {
        this.row = row;
        this.column = column;
        this.oldRow = 20;
        this.oldColumn = 20;
        this.symbol = '\u01C1';
        this.isActive = true;
        this.terminal = terminal;

    }

    public void PrintProjectile(Projectile projectile, Terminal terminal) throws Exception{
        terminal.setForegroundColor(TextColor.ANSI.GREEN);
        oldColumn = column;
        oldRow = row;
        row -=1;
        terminal.setCursorPosition(projectile.getColumn(), projectile.getRow());
        terminal.putCharacter(projectile.getSymbol());
        terminal.setCursorPosition(projectile.getOldColumn(), projectile.getOldRow());
        terminal.putCharacter(' ');
        terminal.flush();

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

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }
}
