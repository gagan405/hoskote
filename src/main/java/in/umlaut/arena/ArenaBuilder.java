package in.umlaut.arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gbm on 27/09/15.
 */
public class ArenaBuilder {
    ArenaIdGenerator idGenerator = ArenaIdGenerator.getInstance();
    private int id = idGenerator.getNextId();

    private String description;
    private String shortDescription;
    private String name;
    private int level;
    private Long pointsForClearingThis;

    private Map<ArenaLayout, List<ArenaObject>> layoutArenaObjectMap;

    public ArenaBuilder(){
        this.layoutArenaObjectMap = new HashMap<>();
    }

    public ArenaBuilder addObject(ArenaObject object, ArenaLayout layout){
        List<ArenaObject> objects;
        if(layoutArenaObjectMap.containsKey(layout)){
            objects = layoutArenaObjectMap.get(layout);
        }else{
            objects = new ArrayList<>();
        }
        objects.add(object);
        this.layoutArenaObjectMap.put(layout, objects);
        return this;
    }

    public ArenaBuilder setLevel(int level){
        this.level = level;
        return this;
    }

    public ArenaBuilder setName(String name){
        this.name = name;
        return this;
    }

    public ArenaBuilder setPointsForClearingThis(Long points){
        this.pointsForClearingThis = points;
        return this;
    }

    public ArenaBuilder setDescription(String description){
        this.description = description;
        return this;
    }

    public ArenaBuilder setShortDescription(String shortDescription){
        this.shortDescription = shortDescription;
        return this;
    }

    public Arena build(){
        return new Arena(id, name, level, pointsForClearingThis, description, shortDescription, layoutArenaObjectMap);
    }

}
