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
        System.out.println("Lanterna exercise version 8");

        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false);
        boolean continueReadingInput = true;

        //Create wall
        final char WallChar = '\u2588';
        List<Position> wall = new ArrayList<>();
        CreateWall(wall);
        PrintWall(terminal, WallChar, wall);

        //Create an enemy that hunt the player
        Enemy enemy1 = new Enemy();
        PrintEnemy(terminal, enemy1);

        //Create player
        Player player = new Player(24, 40);
        PrintPlayer(terminal, player);

        //Create bomb
        Bomb bomb1 = new Bomb();
        PrintBomb(terminal, bomb1);

        //List of projectiles (Empty at start)
        List<Projectile> projectileList = new ArrayList<>();

        //Repeat game

        do {
            player.setOldRow(player.getRow());
            player.setOldColumn(player.getColumn());

            enemy1.setOldRow(enemy1.getRow());
            enemy1.setOldColumn(enemy1.getColumn());

            bomb1.setOldRow((bomb1.getRow()));
            bomb1.setColumn(bomb1.getColumn());

            //wait for user input
            int index = 0;
            int projectileIndex = 0;
            KeyStroke keyStroke = null;


        /*    do {
                index++;
                if(index % 100 == 0) {
                    DropBomb(player, bomb1);
                    PrintBomb(terminal, bomb1);
                }
            } while (bomb1.isAlive());*/

            do {
                Thread.sleep(5);
                keyStroke = terminal.pollInput();
                index++;
                projectileIndex++;
                if(index % 100 == 0) {
                    DropBomb(player, bomb1);
                    PrintBomb(terminal, bomb1);
                }
                if(projectileIndex % 100 == 0) {
                    MoveProjectiles(projectileList, terminal);
                }
            }while (keyStroke==null);

            KeyType type = keyStroke.getKeyType();

            switch (type) {
                case ArrowDown:
                  /*  player.movePlayerDown(); //move player
                    FollowPlayer(player, enemy1); //move enemy */
                    PrintGameOver(terminal, player); //Print GAME OVER
                    continueReadingInput = false;
                    break;

                case ArrowUp:
                  /*  player.movePlayerUp();//move player
                    FollowPlayer(player, enemy1); //move enemy */
                    projectileList.add(new Projectile(player.getRow()-1, player.getColumn(), terminal));
                    continue;

                case ArrowLeft:
                    player.movePlayerLeft(); //move player
                    FollowPlayer(player, enemy1); //move enemy
                    break;

                case ArrowRight:
                    player.movePlayerRight(); //move player
                    FollowPlayer(player, enemy1); //move enemy
                    break;

            }

            /*Check if inside screen
            continueReadingInput = isInScreen(terminal,player); */

            if (continueReadingInput == true) {

                //Check if player moved into the walls
                boolean CrashWall = false;
                for (Position p : wall) {
                    if (p.getX() == player.getColumn() && p.getY() == player.getRow())
                        CrashWall = true;
                }
                if (CrashWall == true ){  // || type.equals(KeyType.ArrowUp)) {
                    player.setColumn(player.getOldColumn());
                    player.setRow(player.getOldRow());
                } else {
                    player.increasePoints(1); // increase player point by 1
                    PrintPlayer(terminal, player); //Move the player
                    PrintEnemy(terminal, enemy1); //Move the enemy
                    PrintWall(terminal, WallChar, wall);


                    index++;
                    if(index % 5 == 0) {
                        DropBomb(player, bomb1);
                        PrintBomb(terminal, bomb1);
                    }

                    projectileIndex++;
                    if(projectileIndex % 5 ==0) {
                        MoveProjectiles(projectileList, terminal);
                    }


                    //Check the bomb position
                    boolean CrashEnemy = false;
                    if (enemy1.getColumn() == player.getColumn() && enemy1.getRow() == player.getRow()) {
                        CrashEnemy = true;
                    }
                    if (CrashEnemy == true) {
                        continueReadingInput = false;
                        PrintGameOver(terminal, player); //Print GAME OVER
                    }
                }
            }
        } while (continueReadingInput);

    }

    public static void CreateWall(List<Position> wall) throws Exception {
        //Create walls
        int wallrow = 22;
        int wallcolumn = 0;

        //Add horisontal walls
        for (int i = 10; i < 20; i++) {
            wall.add(new Position(i, wallrow));
        }
        for (int i = 60; i < 70; i++) {
            wall.add(new Position(i, wallrow));
        }
        //Add vertical walls
        for (int i = 0; i < 24; i++) {
            wall.add(new Position(wallcolumn, i));
        }
        for (int i = 0; i < 24; i++) {
            wall.add(new Position(80, i));
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
        PrintPoints(terminal, player,76,24);
    }
    public static void PrintPoints(Terminal terminal, Player player, int posColumn, int posRow) throws Exception {
        char pointEntal = '0';
        char pointTiotal = ' ';
        char pointHundratal = ' ';
        int hundratal = 0;
        int tiotal = 0;
        int ental = 0;
        int tempPoints = player.getPoints();
        // hundred
        if (tempPoints > 99) {
            hundratal = player.getPoints()/100;
            pointHundratal = Character.forDigit(hundratal, 10);
            tempPoints -=(hundratal*100);
            pointTiotal = '0';
        }
        if (tempPoints > 10) {
            tiotal = tempPoints/10;
            pointTiotal = Character.forDigit(tiotal, 10);
            tempPoints -=(tiotal*10);
        }
        pointEntal = Character.forDigit(tempPoints, 10);

        //Print points in lower right corner
        terminal.setCursorPosition(posColumn, posRow);
        terminal.putCharacter(pointHundratal);
        terminal.setCursorPosition(posColumn+1, posRow);
        terminal.putCharacter(pointTiotal);
        terminal.setCursorPosition(posColumn+2, posRow);
        terminal.putCharacter(pointEntal);
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

    public static void PrintBomb(Terminal terminal, Bomb bomb) throws Exception {
        terminal.setForegroundColor(TextColor.ANSI.MAGENTA);
        terminal.setCursorPosition(bomb.getColumn(), bomb.getRow());
        terminal.putCharacter(bomb.getSymbol());
        terminal.setCursorPosition(bomb.getOldColumn(), bomb.getOldRow());
        terminal.putCharacter(' ');
        terminal.flush();
    }

    public static void PrintGameOver(Terminal terminal, Player player) throws Exception {
        String GameOver = "GAME OVER";
        int GameOverRow = 12;
        int GameOverColumn = 40;
        for (int i = 0; i < GameOver.length(); i++) {
            terminal.setCursorPosition(GameOverColumn, GameOverRow);
            terminal.putCharacter(GameOver.charAt(i));
            GameOverColumn += 1;
        }
        terminal.flush();

        terminal.setForegroundColor(TextColor.ANSI.WHITE);
        PrintPoints(terminal, player,43,13); //Print points
        terminal.setCursorPosition(46, 13);
        terminal.putCharacter('p');
        terminal.flush();

        Thread.sleep(2000);
        System.out.println("Quit ");
        terminal.close();
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

    public static void DropBomb(Player player, Bomb bomb) {
        bomb.moveBombDown();
    }
    
    public static void MoveProjectiles(List<Projectile> projectileList, Terminal terminal) throws Exception{
        for (Projectile projectile : projectileList) {
            projectile.PrintProjectile(projectile, terminal);
        }
    }

    public static boolean isInScreen(Terminal terminal, Player player) throws Exception {
        //Check if outside screen
        if (player.getRow() == 0 || player.getColumn() == 0 || player.getRow() > 24 || player.getColumn() > 80) {
            System.out.println("Quit ");
            terminal.close();
            return (false);
        } else
            return (true);
    }
}
