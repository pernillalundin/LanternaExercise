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
        boolean continueReadingInput = true;

        //Create wall
        final char WallChar = '\u2588';
        List<Position> wall = new ArrayList<>();
        CreateWall(wall);
        PrintWall(terminal, WallChar, wall);

        //Create a enemy that hunt the player
        Enemy enemy1 = new Enemy();
        PrintEnemy(terminal, enemy1);

        //Create player
        Player player = new Player(1, 1);
        PrintPlayer(terminal, player);

        //Create bomb
        Bomb bomb1 = new Bomb();
        PrintBomb(terminal,bomb1);

        //Repeat game

        do {
            player.setOldRow(player.getRow());
            player.setOldColumn(player.getColumn());

            enemy1.setOldRow(enemy1.getRow());
            enemy1.setOldColumn(enemy1.getColumn());

           bomb1.setOldRow((bomb1.getRow()));
           bomb1.setColumn(bomb1.getColumn());

            //wait for user input
            KeyStroke keyStroke = null;

            do {
                Thread.sleep(5);
                keyStroke = terminal.pollInput();
            }
            while (keyStroke == null);
            KeyType type = keyStroke.getKeyType();

            switch (type) {
                case ArrowDown:
                    player.movePlayerDown(); //move player
                    FollowPlayer(player, enemy1); //move enemy
                    DropBomb(player,bomb1);
                    break;

                case ArrowUp:
                    player.movePlayerUp();//move player
                    FollowPlayer(player, enemy1); //move enemy
                    DropBomb(player,bomb1);
                    break;

                case ArrowLeft:
                    player.movePlayerLeft(); //move player
                    FollowPlayer(player, enemy1); //move enemy
                    DropBomb(player,bomb1);
                    break;

                case ArrowRight:
                    player.movePlayerRight(); //move player
                    FollowPlayer(player, enemy1); //move enemy
                    DropBomb(player,bomb1);
                    break;

            }
            //Check if inside screen
            continueReadingInput = isInScreen(terminal,player);

            if (continueReadingInput == true) {
                //Check if player moved into the walls
                boolean CrashWall = false;
                for (Position p : wall) {
                    if (p.getX() == player.getColumn() && p.getY() == player.getRow())
                        CrashWall = true;
                }
                if (CrashWall == true) {
                    player.setColumn(player.getOldColumn());
                    player.setRow(player.getOldRow());
                } else {
                    //Move the player

                    PrintPlayer(terminal, player);
                    //Move the enemy
                    PrintEnemy(terminal, enemy1);
                    PrintWall(terminal, WallChar, wall);
                    PrintBomb(terminal, bomb1);

                    //Check the bomb position
                    boolean CrashEnemy = false;
                    if (enemy1.getColumn() == player.getColumn() && enemy1.getRow() == player.getRow()) {
                        CrashEnemy = true;
                    }
                    if (CrashEnemy == true) {
                        continueReadingInput = false;
                        String GameOver = "GAME OVER";
                        int GameOverRow = 12;
                        int GameOverColumn = 40;
                        for (int i = 0; i < GameOver.length(); i++) {
                            terminal.setCursorPosition(GameOverColumn, GameOverRow);
                            terminal.putCharacter(GameOver.charAt(i));
                            GameOverColumn += 1;
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
        for (int i = 10; i < 40; i++) {
            wall.add(new Position(i, wallrow));
        }
        //Add vertical obsticle
        for (int i = 5; i < 20; i++) {
            wall.add(new Position(wallcolumn, i));
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

    public static void PrintEnemy(Terminal terminal, Enemy enemy) throws Exception {
        terminal.setForegroundColor(TextColor.ANSI.WHITE);
        terminal.setCursorPosition(enemy.getColumn(), enemy.getRow());
        terminal.putCharacter(enemy.getSymbol());
        terminal.setCursorPosition(enemy.getOldColumn(), enemy.getOldRow());
        terminal.putCharacter(' ');
        terminal.flush();
    }
    public static void PrintBomb(Terminal terminal,Bomb bomb) throws Exception{
        terminal.setForegroundColor(TextColor.ANSI.MAGENTA);
        terminal.setCursorPosition(bomb.getColumn(), bomb.getRow());
        terminal.putCharacter(bomb.getSymbol());
        terminal.setCursorPosition(bomb.getOldColumn(), bomb.getOldRow());
        terminal.putCharacter(' ');
        terminal.flush();
    }

    public static void FollowPlayer(Player player, Enemy enemy) {

        if (enemy.getRow() == player.getRow()) {
            if (enemy.getColumn() < player.getColumn())
                enemy.moveEnemyRight();
            else
                enemy.moveEnemyLeft();
        } else {
            if (enemy.getRow() < player.getRow())
                enemy.moveEnemyDown();
            else
                enemy.moveEnemyUp();
        }

    }
    public static void DropBomb (Player player,Bomb bomb){
        bomb.moveBombDown();
    }


    public static boolean isInScreen(Terminal terminal, Player player) throws Exception {
        //Check if outside screen
        if (player.getRow() == 0 || player.getColumn() == 0 || player.getRow() > 24 || player.getColumn() > 80) {
            System.out.println("Quit ");
            terminal.close();
            return (false);
        }
        else
            return (true);
    }
}