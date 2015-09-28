package in.umlaut.player;

import java.util.Comparator;

/**
 * Created by gbm on 27/09/15.
 */
public class PlayerComparator implements Comparator<Player> {
    @Override
    public int compare(Player o1, Player o2) {
        return o1.getId() - o2.getId();
    }
}
