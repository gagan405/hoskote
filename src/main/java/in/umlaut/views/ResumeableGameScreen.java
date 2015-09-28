package in.umlaut.views;

import in.umlaut.game.Game;
import in.umlaut.game.GameBuilder;
import in.umlaut.game.GameSaver;
import in.umlaut.player.Player;

import java.util.List;
import java.util.Scanner;

/**
 * Created by gbm on 28/09/15.
 */
public class ResumeableGameScreen implements KbInputHandler {

    private GameBuilder builder;
    private GameSaver gameSaver;

    private static final String help =
            "Press : \n"
                    + "'V'  : View all resumable games \n"
                    + "'S1' : Select game 1, enter SN for nth Game\n"
                    + "'D1' : Delete game 1, enter DN for nth Game \n"
                    + "'H'  : show this help message again \n"
                    + "'E'  : Exit the screen to go back to start screen \n";


    public ResumeableGameScreen(GameBuilder gameBuilder) {
        this.builder = gameBuilder;
        this.gameSaver = GameSaver.getInstance();
    }

    @Override
    public void handleKeyStroke() {
        System.out.println("You are in the Resumable Game Management Screen");
        System.out.println(help);
        Scanner in = new Scanner(System.in);
        String token = readInput(in);

        while (!token.equalsIgnoreCase("E")) {
            switch (token.toUpperCase()) {
                case "H":
                    System.out.println(help);
                    break;
                case "V":
                    if(checkPlayerSelect()) {
                        System.out.println(viewAllResumableGames());
                    }
                    break;
                default:
                    if (token.toUpperCase().startsWith("S") || token.toUpperCase().startsWith("D")) {
                        try {
                            int gameId = Integer.parseInt(token.substring(1));
                            Game game = gameSaver.getSavedGame(gameId, builder.getPlayer().getId());
                            if (game == null) {
                                System.out.println("Invalid number. Game does not exist.");
                            } else {
                                switch (token.toUpperCase().charAt(0)) {
                                    case 'D':
                                        gameSaver.deleteSavedGame(gameId, builder.getPlayer());
                                        System.out.println("Saved game deleted for player.");
                                        break;
                                    case 'S':
                                        builder.setIsReconstruct(true);
                                        builder.setGame(game);
                                        System.out.println("Resumable game is selected");
                                        break;
                                    default:
                                        System.out.println("Invalid input. Please try again");
                                }
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please try again");
                        }
                    }
            }
            token = readInput(in);
        }
    }

    private boolean checkPlayerSelect() {
        Player player = this.builder.getPlayer();
        if (player == null) {
            System.out.println("Player has not been selected yet. First select player in Player Management Screen");
            return false;
        }
        return true;
    }

    private String viewAllResumableGames() {
        Integer id = this.builder.getPlayer().getId();
        List<Game> games = gameSaver.getSavedGamesForPlayer(id);
        if(games.isEmpty()){
            return "No saved games found for the given player";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%8s","Player ID")).append("\t").append(String.format("%8s","Game ID"))
                    .append(System.lineSeparator());
            games.stream().forEach(game -> {
                sb.append(String.format("%8s",id)).append("\t").append(String.format("%8s", game.getId()))
                        .append(System.lineSeparator());
            });
            return sb.toString();
        }
    }
}
