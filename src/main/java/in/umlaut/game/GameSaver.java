package in.umlaut.game;

import in.umlaut.arena.Arena;
import in.umlaut.arena.ArenaLayout;
import in.umlaut.arena.ArenaObject;
import in.umlaut.arena.ArenaPool;
import in.umlaut.arena.arenaobjects.ArenaObjectPool;
import in.umlaut.player.Player;
import in.umlaut.player.PlayerManager;
import in.umlaut.utils.CsvUtil;

import java.io.IOException;
import java.util.*;

import java.util.stream.Collectors;

/**
 * Created by gbm on 28/09/15.
 */
public class GameSaver implements GameObserver{
    private static final String DELIMITER = ";";
    private static  final  String filePath = "/tmp/saved_game.csv";

    private Map<Integer, Game> savedGames;
    private ArenaPool arenaPool;
    private PlayerManager playerManager;
    private ArenaObjectPool arenaObjectPool;
    private GameIdGenerator gameIdGenerator;

    private final CsvUtil csvUtil;
    private static GameSaver gameSaver;

    private GameSaver(){
        this.csvUtil = new CsvUtil();
        this.savedGames = new HashMap<>();
        this.playerManager = PlayerManager.getInstance();
        this.arenaPool = ArenaPool.getInstance();
        this.arenaObjectPool = ArenaObjectPool.getInstance();
        this.gameIdGenerator = GameIdGenerator.getInstance();
        loadSavedGames();
    }

    public static GameSaver getInstance(){
        if(gameSaver == null){
            gameSaver = new GameSaver();
        }
        return gameSaver;
    }

    public void deleteSavedGame(int id, Player player){
        if(savedGames.containsKey(id)){
            Game game = savedGames.get(id);
            if(game.getPlayer().getId() != player.getId()){
                System.out.println("Invalid game id to delete.");
                return;
            }
        }
        savedGames.remove(id);
        save();
    }

    public Game getSavedGame(int gameId, int playerId){
        if(savedGames.containsKey(gameId)){
            Game game = savedGames.get(gameId);
            if(game.getPlayer().getId() != playerId){
                System.out.println("Invalid game id to select.");
                return null;
            } else {
                return savedGames.get(gameId);
            }
        }
        return null;
    }

    public List<Game> getSavedGamesForPlayer(int id){
        return savedGames.values().stream().filter(o -> o.getPlayer().getId() == id)
                .collect(Collectors.toList());
    }

    private void save(){
        List<String> lines = new ArrayList<>();
        for(Game game : savedGames.values() ){
            String csv = getCsvForGame(game);
            lines.add(csv);
        }
        csvUtil.writeLines(lines, filePath, true);
    }

    public void save(Game game){
        savedGames.put(game.getId(), game);
        save();
    }

    private String getCsvForGame(Game game) {
        StringBuilder sb = new StringBuilder();
        sb.append(game.getId()).append(DELIMITER)
                .append(game.getPlayer().getId()).append(DELIMITER)
                .append(game.getArena().getId()).append(DELIMITER)
                .append(game.isDirectionSelected()).append(DELIMITER);

        if(game.isDirectionSelected()) {
            sb.append(game.getLayoutSelected().name());
        }
        sb.append(DELIMITER);

        List<String> ids = game.getFoundObjects().stream().map(o -> String.valueOf(o.getId()))
                .collect(Collectors.toList());
        if(!ids.isEmpty()) {
            String joined = String.join(",", ids);
            sb.append(joined);
        }

        sb.append(DELIMITER)
                .append(game.getScore())
                .append(DELIMITER);
        if(game.getSelectedObject() != null){
            sb.append(game.getSelectedObject().getId());
        }
        sb.append(DELIMITER);
        return sb.toString();
    }

    private Game reconstructGame(String savedGame){
        GameBuilder builder = new GameBuilder();
        builder.setIsReconstruct(true);
        String[] tokens = savedGame.split(DELIMITER, -1);
        builder.setId(Integer.parseInt(tokens[0]));
        builder.setPlayer(playerManager.getNthPlayer(Integer.parseInt(tokens[1])));

        Arena arena = arenaPool.getNthArena(Integer.parseInt(tokens[2]));
        builder.setArena(arena);

        builder.setIsDirectionSelected(Boolean.parseBoolean(tokens[3]));
        if(!tokens[4].isEmpty()){
            ArenaLayout layout = ArenaLayout.fromInitial(tokens[4]);
            builder.setArenaLayout(layout);
        }
        if(!tokens[5].isEmpty()){
            List<Integer> foundObjectIds = Arrays.asList(tokens[5].split(",", -1)).stream().map(e -> Integer.parseInt(e))
                    .collect(Collectors.toList());
            if(!foundObjectIds.isEmpty()){
                List<ArenaObject> foundObjects = foundObjectIds.stream().map(id -> arenaObjectPool.getObject(id))
                        .collect(Collectors.toList());

                builder.setFoundObjects(foundObjects);
                //remove found objects from the Arena
                foundObjectIds.stream().forEach(id -> arena.removeObject(id));
            }
        }
        builder.setScore(Long.parseLong(tokens[6]));
        if(!tokens[7].isEmpty()){
            ArenaObject selectedObject = arenaObjectPool.getObject(Integer.parseInt(tokens[7]));
            builder.setSelectedObject(selectedObject);
        }
        return builder.build();
    }

    private void loadSavedGames(){
        try {
            List<String> games = csvUtil.readLines(filePath);
            if(games.isEmpty()){
                return;
            }
            int id = 0;
            for(String s : games){
                Game game = reconstructGame(s);
                savedGames.put(game.getId(), game);
                if(game.getId() > id){
                    id = game.getId();
                }
            }
            //set id in idGenerator
            gameIdGenerator.setId(id);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void observeGame(Game game) {
        game.addObserver(this);
    }

    @Override
    public void handleGameUpdates(Integer id, Player player, Long score, Arena arena, boolean isComplete) {
        if(savedGames.containsKey(id) && isComplete){
            savedGames.remove(id);
            save();
        }
    }
}
