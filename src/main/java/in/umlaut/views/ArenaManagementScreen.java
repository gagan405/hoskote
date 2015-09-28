package in.umlaut.views;

import in.umlaut.game.GameBuilder;
import in.umlaut.arena.Arena;
import in.umlaut.arena.ArenaPool;

import java.util.Scanner;

/**
 * Created by gbm on 27/09/15.
 */
public class ArenaManagementScreen implements KbInputHandler {
    ArenaPool pool = ArenaPool.getInstance();

    private static final String help =
            "Press : \n"
                    + "'V'  : View all available arenas \n"
                    + "'S1' : Select 1st arena, Enter SN for nth arena \n"
                    + "'H'  : show this help message again \n"
                    + "'E'  : Exit the screen to go back to start screen \n";

    private GameBuilder gameBuilder;

    public ArenaManagementScreen(GameBuilder builder){
        this.gameBuilder = builder;
    }

    @Override
    public void handleKeyStroke() {
        System.out.println("You are in the Arena management Screen");
        System.out.println(help);
        Scanner in = new Scanner(System.in);
        String token = readInput(in);
        while(!token.equalsIgnoreCase("E")){
            switch (token.toUpperCase()){
                case "H" :
                    System.out.println(help);
                    break;
                case "V" :
                    System.out.println(pool.viewAllArenas());
                    break;
                default:
                    if(token.toUpperCase().startsWith("S")){
                        try{
                            int arenaNum = Integer.parseInt(token.substring(1));
                            Arena arena = pool.getNthArena(arenaNum);
                            if(arena == null){
                                System.out.println("Invalid number. Arena does not exist.");
                            }else{
                                if(gameBuilder.getPlayer().getLevel() < arena.getLevel()){
                                    System.out.println("Player is not qualified to play this level. Please select a lower level Arena.");
                                } else {
                                    gameBuilder.setArena(arena);
                                    System.out.println("Arena selected for game.");
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
