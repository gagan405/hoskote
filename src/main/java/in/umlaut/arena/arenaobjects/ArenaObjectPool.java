package in.umlaut.arena.arenaobjects;

import in.umlaut.arena.ArenaObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gbm on 28/09/15.
 */
public class ArenaObjectPool {
    private static ArenaObjectPool objectPool;
    private Map<Integer, ArenaObject> objectMap;

    private ArenaObjectPool(){
        this.objectMap = new HashMap<>();
    }

    public static ArenaObjectPool getInstance(){
        if(objectPool == null){
            objectPool = new ArenaObjectPool();
        }
        return objectPool;
    }

    public void addObject(ArenaObject object){
        objectMap.put(object.getId(), object);
    }

    public ArenaObject getObject(int id){
        return this.objectMap.get(id);
    }
}
