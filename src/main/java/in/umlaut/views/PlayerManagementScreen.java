package in.umlaut.views;

import in.umlaut.game.GameBuilder;
import in.umlaut.player.Player;
import in.umlaut.player.PlayerManager;

import java.util.Scanner;

/**
 * Created by gbm on 27/09/15.
 */
public class PlayerManagementScreen implements KbInputHandler{
    private PlayerManager manager;
    private GameBuilder gameBuilder;

    private static final String help =
            "Press : \n"
                    + "'V'  : View all available players \n"
                    + "'S1' : Select player 1, enter SN for nth Player\n"
                    + "'D1' : Delete player 1, enter DN for nth player \n"
                    + "'C1' : Clear all scores for player 1, enter CN for nth player \n"
                    + "'R1' : Reset level for player 1, enter RN for nth player \n"
                    + "'N'  : Create a new player \n"
                    + "'H'  : show this help message again \n"
                    + "'E'  : Exit the screen to go back to start screen \n";

    public PlayerManagementScreen(GameBuilder gameBuilder){
        manager = PlayerManager.getInstance();
        this.gameBuilder = gameBuilder;
    }

    public void handleKeyStroke(){
        System.out.println("You are in the Player Management Screen");
        System.out.println(help);
        Scanner in = new Scanner(System.in);
        String token = readInput(in);

        while(!token.equalsIgnoreCase("E")){
            switch (token.toUpperCase()){
                case "H" :
                    System.out.println(help);
                    break;
                case "V" :
                    System.out.println(manager.viewAllPlayers());
                    break;
                case "N" :
                    System.out.println("Enter a name for the new player : ");
                    String name = in.nextLine();
                    System.out.println("Enter a description for the new player : ");
                    String desc = in.nextLine();
                    manager.createNewPlayer(name, desc);
                    manager.save();
                    System.out.println("New player has been created. Press 'V' ro see list of players available.");
                    break;
                default:
                    if(token.toUpperCase().startsWith("S") ||
                            token.startsWith("C") ||
                            token.startsWith("R") ||
                            token.startsWith("D")){
                        try{
                            int playerNum = Integer.parseInt(token.substring(1));
                            Player player = manager.getNthPlayer(playerNum);
                            if(player == null){
                                System.out.println("Invalid number. Player does not exist.");
                            }else{
                                switch (token.toUpperCase().charAt(0)){
                                    case 'R' :
                                        manager.resetLevel(player);
                                        System.out.println("Level reset for player.");
                                        manager.save();
                                        break;
                                    case 'C' :
                                        manager.clearScores(player);
                                        System.out.println("Scores cleared for player.");
                                        manager.save();
                                        break;
                                    case 'D' :
                                        manager.removePlayer(player);
                                        System.out.println("Deleted player.");
                                        manager.save();
                                        break;
                                    case 'S' :
                                        gameBuilder.setPlayer(player);
                                        System.out.println("Player selected for game. Press 'E' to go back to start screen and select arena");
                                        System.out.println("Press 'V' to list all players and choose again.");
                                        System.out.println("Press 'H' for help.");
                                        break;
                                    default:
                                        System.out.println("Invalid input. Please try again");
                                }
                            }
                        }catch (NumberFormatException e){
                            System.out.println("Invalid input. Please try again");
                        }
                    } else {
                        System.out.println("Invalid input. Please try again");
                    }
            }
            token = readInput(in);
        }
    }
}
