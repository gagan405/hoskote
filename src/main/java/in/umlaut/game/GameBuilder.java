package in.umlaut.game;

import in.umlaut.arena.Arena;
import in.umlaut.arena.ArenaLayout;
import in.umlaut.arena.ArenaObject;
import in.umlaut.player.Player;

import java.util.List;

/**
 * Created by gbm on 27/09/15.
 */
public class GameBuilder {
    private Player playerInstance;
    private Arena arenaInstance;

    private Game game;

    private boolean isReconstruct;
    private boolean isDirectionSelected;
    private int id;
    private ArenaLayout layout;
    private long score;
    private List<ArenaObject> foundObjects;
    private ArenaObject selectedObject;

    public GameBuilder setPlayer(Player player) {
        this.playerInstance = player;
        return this;
    }

    public GameBuilder setSelectedObject(ArenaObject object) {
        this.selectedObject = object;
        return this;
    }

    public GameBuilder setId(Integer id) {
        this.id = id;
        return this;
    }

    public GameBuilder setScore(Long score) {
        this.score = score;
        return this;
    }

    public GameBuilder setFoundObjects(List<ArenaObject> objects) {
        this.foundObjects = objects;
        return this;
    }

    public Player getPlayer() {
        return this.playerInstance;
    }

    public GameBuilder setArena(Arena arena) {
        this.arenaInstance = arena;
        return this;
    }

    public GameBuilder setIsReconstruct(boolean isReconstruct){
        this.isReconstruct = isReconstruct;
        return this;
    }

    public GameBuilder setIsDirectionSelected(boolean isDirectionSelected){
        this.isDirectionSelected = isDirectionSelected;
        return this;
    }

    public GameBuilder setArenaLayout(ArenaLayout layout){
        this.layout = layout;
        return this;
    }

    public GameBuilder setGame(Game game){
        this.game = game;
        return this;
    }

    public boolean canStart() {
        return (playerInstance != null && arenaInstance != null) ||
                (isReconstruct && ((game != null) || (playerInstance != null && arenaInstance != null)) );
    }

    public Game build() {
        if(!isReconstruct) {
            return new Game(playerInstance, arenaInstance);
        }else
            return this.game == null ? new Game(id, playerInstance, arenaInstance, score, selectedObject, foundObjects, isDirectionSelected, layout )
                    : this.game;
    }
}
