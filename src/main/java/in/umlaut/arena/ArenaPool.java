package in.umlaut.arena;

import in.umlaut.player.Player;
import in.umlaut.player.PlayerComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by gbm on 27/09/15.
 */
public class ArenaPool {
    private ArenaInitializer initializer;
    private static ArenaPool pool = null;

    List<Arena> arenas;
    Map<Integer, Arena> idToArenaMap;

    private ArenaPool(){
        initializer = new ArenaInitializer();
        init();
    }

    public static ArenaPool getInstance(){
        if(pool == null){
            pool = new ArenaPool();
        }
        return pool;
    }
    private void init(){
        Arena level1 = initializer.getSimplestArena();
        arenas = new ArrayList<>();
        arenas.add(level1);
    }

    public List<Arena> getArenas(){
        return arenas;
    }

    public Arena getNthArena(int n){
        if(n > arenas.size()){
            return null;
        }
        idToArenaMap = this.arenas.stream().collect(Collectors.toMap( p -> p.getId(), p -> p));
        return idToArenaMap.get(n);
    }

    public String viewAllArenas(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%6s","Id"))
                .append("\t\t").append(String.format("%12s", "Name"))
                .append("\t\t").append(String.format("%18s", "Short Description"))
                .append("\t\t").append(String.format("%12s", "Level"));
        sb.append(System.lineSeparator());

        for(Arena a : arenas){
            sb.append(a.toString()).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
