import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Lanterna exercise version 4");

        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false);

        final char WallChar = '\u2588';
        final char BombChar = '\u058D';
        List<Position> wall = new ArrayList<>();

        CreateWall(wall);
        PrintWall(terminal, WallChar, wall);

        //Create a bomb that hunt the player
        Random r = new Random();
        Position bombPosition = new Position(r.nextInt(80), r.nextInt(24));
        int oldBombRow = bombPosition.getX();
        int oldBombColumn = bombPosition.getY();
        terminal.setForegroundColor(TextColor.ANSI.WHITE);
        terminal.setCursorPosition(bombPosition.getX(), bombPosition.getY());
        terminal.putCharacter(BombChar);
        terminal.flush();


        //move around the player
        boolean continueReadingInput = true;

        Player player = new Player(1, 1);
        PrintPlayer(terminal, player);

        //start position of player

        do {
            player.setOldRow(player.getRow());
            player.setOldColumn(player.getColumn());
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
                    player.movePlayerDown(); //move player
                    //move bomb
                    if (bombPosition.getY() < player.getRow())
                        bombPosition.setY(bombPosition.getY()+2);
                    else if (bombPosition.getY() == player.getRow()) {
                        if (bombPosition.getX() < player.getColumn())
                            bombPosition.setX(bombPosition.getX()+2);
                        else
                            bombPosition.setX(bombPosition.getX()-2);
                    }
                    else
                        bombPosition.setY(bombPosition.getY()-2);
                    break;
                case ArrowUp:
                    player.movePlayerUp();
                    //move bomb
                    if (bombPosition.getY() < player.getRow())
                        bombPosition.setY(bombPosition.getY()+2);
                    else if (bombPosition.getY() == player.getRow()) {
                        if (bombPosition.getX() < player.getColumn())
                            bombPosition.setX(bombPosition.getX()+2);
                        else
                            bombPosition.setX(bombPosition.getX()-2);
                    }
                    else
                        bombPosition.setY(bombPosition.getY()-2);
                    break;
                case ArrowLeft:
                    player.movePlayerLeft(); //move player
                    //move bomb
                    if (bombPosition.getX() < player.getColumn())
                        bombPosition.setX(bombPosition.getX()+2);
                    else if (bombPosition.getX() == player.getColumn()) {
                        if (bombPosition.getY() < player.getRow())
                            bombPosition.setY(bombPosition.getY()+2);
                        else
                            bombPosition.setY(bombPosition.getY()-2);
                    }
                    else
                        bombPosition.setX(bombPosition.getX()-2);
                    break;
                case ArrowRight:
                    player.movePlayerRight(); //move player
                    //move bomb
                    if (bombPosition.getX() < player.getColumn())
                        bombPosition.setX(bombPosition.getX()+2);
                    else if (bombPosition.getX() == player.getColumn()) {
                        if (bombPosition.getY() < player.getRow())
                            bombPosition.setY(bombPosition.getY()+2);
                        else
                            bombPosition.setY(bombPosition.getY()-2);
                    }
                    else
                        bombPosition.setX(bombPosition.getX()-2);
            }
            //Check if inside screen
            if (player.getRow() == 0 || player.getColumn() ==0 || player.getRow() > 24 || player.getColumn() > 80) {
                continueReadingInput = false;
                System.out.println("Quit ");
                terminal.close();
            }
            else {
                //Check if player moved into the walls
                boolean CrashWall = false;
                for (Position p : wall) {
                    if (p.getX() == player.getColumn() && p.getY() == player.getRow())
                        CrashWall = true;
                }
                if (CrashWall == true) {
                    player.setColumn(player.getOldColumn());
                    player.setRow(player.getOldRow());
                }
                else {
                    //Move the player

                    PrintPlayer(terminal, player);
                    //Move the bomb
                    terminal.setForegroundColor(TextColor.ANSI.WHITE);
                    terminal.setCursorPosition(bombPosition.getX(), bombPosition.getY());
                    terminal.putCharacter(BombChar);
                    terminal.setCursorPosition(oldBombColumn, oldBombRow);
                    terminal.putCharacter(' ');

                    terminal.flush();

                    //Check the bomb position
                    boolean CrashBomb = false;
                    if (bombPosition.getX() == player.getColumn() && bombPosition.getY() == player.getRow()) {
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

    public static void CreateWall(List<Position> wall) throws Exception {
        //Create walls
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

    }
    public static void PrintWall(Terminal terminal, char WallChar, List<Position> wall) throws Exception {
        //Print walls
        terminal.setForegroundColor(TextColor.ANSI.RED);
        for (Position p : wall) {
            terminal.setCursorPosition(p.getX(), p.getY());
            terminal.putCharacter(WallChar);
            terminal.flush();
        }
    }
    public static void PrintPlayer(Terminal terminal, Player player) throws Exception {
        terminal.setForegroundColor(TextColor.ANSI.WHITE);
        terminal.setCursorPosition(player.getColumn(), player.getRow());
        terminal.putCharacter(player.getSymbol());
        terminal.setCursorPosition(player.getOldColumn(), player.getOldRow());
        terminal.putCharacter(' ');
        terminal.flush();
    }
}