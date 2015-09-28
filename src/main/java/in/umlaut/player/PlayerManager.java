package in.umlaut.player;

import in.umlaut.arena.Arena;
import in.umlaut.game.Game;
import in.umlaut.game.GameObserver;
import in.umlaut.utils.CsvUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by gbm on 26/09/15.
 */
public class PlayerManager implements GameObserver {
    private Set<Player> players;
    private static String filePath = "/tmp/players.csv";
    private static PlayerManager manager;
    private CsvUtil csvUtil;
    private Map<Integer, Player> idToPlayerMap;

    private PlayerManager(){
        csvUtil = new CsvUtil();
        loadPlayers();
    }

    public static PlayerManager getInstance(){
        if(manager == null){
            manager = new PlayerManager();
        }
        return manager;
    }

    private void loadPlayers(){
        try {
            List<String> csvLines = csvUtil.readLines(filePath);
            players = csvLines.stream()
                    .filter(line -> !(line == null || line.isEmpty()))
                    .map(csvLine -> Player.fromCsv(csvLine))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player createNewPlayer(String name, String desc){
        Player p = new Player(name, players.size() + 1);
        p.setDescription(desc);
        if(this.players == null){
            this.players = new TreeSet<>();
        }
        if(this.players.contains(p)){
            System.out.println(p.hashCode());
            for(Player pl : this.players){
                System.out.println(pl.hashCode());
            }
            System.err.println("Player of the same name and description already exists");
            return null;
        }
        this.players.add(p);
        return p;
    }

    public Set<Player> getPlayers(){
        return this.players;
    }

    public Player getNthPlayer(int n){
        if(n > players.size()){
            return null;
        }
        idToPlayerMap = this.players.stream().collect(Collectors.toMap( p -> p.getId(), p -> p));
        return idToPlayerMap.get(n);
    }

    public String viewAllPlayers(){
        List<Player> sorted = this.players.stream().collect(Collectors.toList());
        Collections.sort(sorted, new PlayerComparator());

        if(sorted.isEmpty()){
            return "No players found. Create a new player to continue.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%6s","Id"))
                .append("\t\t").append(String.format("%12s", "Name"))
                .append("\t\t").append(String.format("%12s","Desc"))
                .append("\t\t").append(String.format("%12s","HighScores"))
                .append("\t\t").append(String.format("%12s", "Level"));
        sb.append(System.lineSeparator());

        for(Player p : sorted){
            sb.append(p.toString()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public void save(){
        List<Player> sorted = this.players.stream().collect(Collectors.toList());
        Collections.sort(sorted, new PlayerComparator());

        List<String> lines = sorted.stream().map(p -> p.toCsv()).collect(Collectors.toList());

        csvUtil.writeLines(lines, filePath, true);
    }

    public void updatePlayerScore(Player player, Long score){
        player.addScore(score);
        players.add(player);
    }

    public void updatePlayerLevel(Player player, int level){
        player.setLevel(level);
        players.add(player);
    }

    public void removePlayer(Player p){
        players.remove(p);
    }

    @Override
    public void observeGame(Game game) {
        game.addObserver(this);
    }

    @Override
    public void handleGameUpdates(Integer id, Player player, Long score, Arena arena, boolean isComplete) {
        if(isComplete){
            player.setLevel(arena.getLevel() + 1);
            player.addScore(score);
            save();
        } else {
            player.setRunningScore(score);
        }
    }

    public void clearScores(Player p){
        p.clearScores();
    }

    public void resetLevel(Player p){
        p.setLevel(1);
    }

}
