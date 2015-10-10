package in.umlaut.views;

import in.umlaut.game.Game;
import in.umlaut.game.GameBuilder;
import in.umlaut.game.GameIdGenerator;
import in.umlaut.player.PlayerManager;

import java.util.Scanner;


/**
 * Created by gbm on 26/09/15.
 */
public class StartScreen implements KbInputHandler{

    private PlayerManagementScreen playerManagementScreen;
    private ArenaManagementScreen arenaManagementScreen;
    private ResumeableGameScreen resumeableGameScreen;

    private GameBuilder gameBuilder = new GameBuilder();

    private static final String welcomeMessage =
              "Welcome to Hoskote! \n\n"
            + "You are stuck inside a room. The door is locked, and you don't have the key. \n"
            + "But the good news is, the key to the door is somewhere inside this room! \n"
            + "You are to explore the objects inside this room, and find your key using the hints or patterns. \n"
            + "The sooner you escape, the more you score. \n\n"
            + "Hope you don't starve to death locked inside for ever! \n\n";

    private static final String help =
            "Press : \n"
            + "'P'  : Go to player management to select a player \n"
            + "'A'  : Go to Arena management to view and select an Arena\n"
            + "'S'  : Start a game after selecting a player and an arena \n"
            + "'R'  : Resume a previously stalled game after selecting a player \n"
            + "'H'  : show this help message again \n"
            + "'E'  : Exit the program \n";


    public StartScreen(){
        playerManagementScreen = new PlayerManagementScreen(gameBuilder);
        arenaManagementScreen = new ArenaManagementScreen(gameBuilder);
        resumeableGameScreen = new ResumeableGameScreen(gameBuilder);
    }

    public void init(){
        displayInfo();
        handleKeyStroke();
    }

    private void displayInfo() {
        System.out.println(welcomeMessage);
        System.out.println(help);
    }

    private void displayInfoWhenEnteredScreen() {
        System.out.println("You are in the start screen.");
        System.out.println(help);
    }

    public void handleKeyStroke(){
        Scanner in = new Scanner(System.in);
        String token = readInput(in);
        while(!token.equalsIgnoreCase("E")) {
            switch (token.toUpperCase()) {
                case "H":
                    System.out.println(help);
                    break;
                case "E":
                    System.out.println("Exiting the game!");
                    break;
                case "R":
                    resumeableGameScreen.handleKeyStroke();
                    displayInfoWhenEnteredScreen();
                    break;
                case "P":
                    playerManagementScreen.handleKeyStroke();
                    displayInfoWhenEnteredScreen();
                    break;
                case "A":
                    arenaManagementScreen.handleKeyStroke();
                    displayInfoWhenEnteredScreen();
                    break;
                case "S":
                    if (gameBuilder.canStart()) {
                        gameBuilder.setId(GameIdGenerator.getInstance().getNextId());
                        Game game = gameBuilder.build();
                        game.addObserver(PlayerManager.getInstance());
                        game.handleKeyStroke();
                    } else {
                        System.out.println("Player and Arena have not been selected. Cannot start game. \n");
                    }
                    displayInfoWhenEnteredScreen();
                    break;
                default:
                    System.out.println("Invalid input. Please try again");
            }
            token = readInput(in);
        }
    }

}
