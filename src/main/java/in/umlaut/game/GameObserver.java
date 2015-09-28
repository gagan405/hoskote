package in.umlaut.game;

import in.umlaut.arena.Arena;
import in.umlaut.player.Player;

/**
 * Created by gbm on 27/09/15.
 */
public interface GameObserver {
    void observeGame(Game game);
    void handleGameUpdates(Integer id, Player player, Long score, Arena arena, boolean isComplete);
}
