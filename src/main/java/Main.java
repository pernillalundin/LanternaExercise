import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.googlecode.lanterna.input.KeyType.ArrowDown;
import static com.googlecode.lanterna.input.KeyType.ArrowUp;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Lanterna exercise version 2");

        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();

        final char WallChar = '\u2588';
        final char BombChar = '\u058D';

        //Create walls as obsticles for player
        List<Position> wall = new ArrayList<>();
        int wallrow = 15;
        int wallcolumn = 45;
        //Add horisontal obsticle
        for (int i = 10;i<40;i++) {
            wall.add(new Position(i,wallrow));
        }
        //Add vertical obsticle
        for (int i = 5;i<20;i++) {
            wall.add(new Position(wallcolumn,i));
        }
        //Create a bomb that hunt the player
        Random r = new Random();
        Position bombPosition = new Position(r.nextInt(80), r.nextInt(24));
        int oldBombRow = bombPosition.getX();
        int oldBombColumn = bombPosition.getY();
        terminal.setForegroundColor(TextColor.ANSI.WHITE);
        terminal.setCursorPosition(bombPosition.getX(), bombPosition.getY());
        terminal.putCharacter(BombChar);
        terminal.flush();

        //Print walls
        terminal.setForegroundColor(TextColor.ANSI.RED);
        for (Position p : wall) {
            terminal.setCursorPosition(p.getX(), p.getY());
            terminal.putCharacter(WallChar);
            terminal.flush();
        }
        //move around the player
        boolean continueReadingInput = true;
        int row = 1;
        int column = 1;
        int oldRow = 1;
        int oldColumn = 1;
        final char player = '\u0398';
        terminal.setCursorVisible(false);
        terminal.setCursorPosition(column, row);
        terminal.putCharacter(player);
        terminal.flush();
        do {
            oldRow = row;
            oldColumn = column;
            oldBombRow = bombPosition.getY();
            oldBombColumn = bombPosition.getX();
            KeyStroke keyStroke = null;
            //wait for user input
            do {
                Thread.sleep(5);
                keyStroke = terminal.pollInput();
            } while (keyStroke == null);
            KeyType type = keyStroke.getKeyType();

            switch (type) {
                case ArrowDown:
                    row +=1; //move player
                    //move bomb
                    if (bombPosition.getY() < row)
                        bombPosition.setY(bombPosition.getY()+2);
                    else if (bombPosition.getY() == row) {
                        if (bombPosition.getX() < column)
                            bombPosition.setX(bombPosition.getX()+2);
                        else
                            bombPosition.setX(bombPosition.getX()-2);
                    }
                    else
                        bombPosition.setY(bombPosition.getY()-2);
                    break;
                case ArrowUp:
                    row -=1; //move player
                    //move bomb
                    if (bombPosition.getY() < row)
                        bombPosition.setY(bombPosition.getY()+2);
                    else if (bombPosition.getY() == row) {
                        if (bombPosition.getX() < column)
                            bombPosition.setX(bombPosition.getX()+2);
                        else
                            bombPosition.setX(bombPosition.getX()-2);
                    }
                    else
                        bombPosition.setY(bombPosition.getY()-2);
                    break;
                case ArrowLeft:
                    column -=1; //move player
                    //move bomb
                    if (bombPosition.getX() < column)
                        bombPosition.setX(bombPosition.getX()+2);
                    else if (bombPosition.getX() == column) {
                        if (bombPosition.getY() < row)
                            bombPosition.setY(bombPosition.getY()+2);
                        else
                            bombPosition.setY(bombPosition.getY()-2);
                    }
                    else
                        bombPosition.setX(bombPosition.getX()-2);
                    break;
                case ArrowRight:
                    column +=1; //move player
                    //move bomb
                    if (bombPosition.getX() < column)
                        bombPosition.setX(bombPosition.getX()+2);
                    else if (bombPosition.getX() == column) {
                        if (bombPosition.getY() < row)
                            bombPosition.setY(bombPosition.getY()+2);
                        else
                            bombPosition.setY(bombPosition.getY()-2);
                    }
                    else
                        bombPosition.setX(bombPosition.getX()-2);
            }
            //Check if inside screen
            if (row == 0 || column ==0 || row > 24 || column > 80) {
                continueReadingInput = false;
                System.out.println("Quit ");
                terminal.close();
            }
            else {
                //Check if player moved into the walls
                boolean CrashWall = false;
                for (Position p : wall) {
                    if (p.getX() == column && p.getY() == row)
                        CrashWall = true;
                }
                if (CrashWall == true) {
                    column = oldColumn;
                    row = oldRow;
                }
                else {
                    //Move the player
                    terminal.setForegroundColor(TextColor.ANSI.WHITE);
                    terminal.setCursorPosition(column, row);
                    terminal.putCharacter(player);
                    terminal.setCursorPosition(oldColumn, oldRow);
                    terminal.putCharacter(' ');
                    //Move the bomb
                    terminal.setForegroundColor(TextColor.ANSI.WHITE);
                    terminal.setCursorPosition(bombPosition.getX(), bombPosition.getY());
                    terminal.putCharacter(BombChar);
                    terminal.setCursorPosition(oldBombColumn, oldBombRow);
                    terminal.putCharacter(' ');

                    terminal.flush();

                    //Check the bomb position
                    boolean CrashBomb = false;
                    if (bombPosition.getX() == column && bombPosition.getY() == row) {
                        CrashBomb = true;
                    }
                    if (CrashBomb == true) {
                        continueReadingInput = false;
                        String GameOver = "GAME OVER";
                        int GameOverRow = 12;
                        int GameOverColumn = 40;
                        for (int i = 0; i<GameOver.length();i++) {
                            terminal.setCursorPosition(GameOverColumn, GameOverRow);
                            terminal.putCharacter(GameOver.charAt(i));
                            GameOverColumn +=1;
                        }
                        terminal.flush();
                        Thread.sleep(2000);
                        System.out.println("Quit ");
                        terminal.close();
                    }
                }
            }
        } while (continueReadingInput);
    }
}
