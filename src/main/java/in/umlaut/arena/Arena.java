package in.umlaut.arena;

import in.umlaut.game.PlayerPos;

import java.util.*;

/**
 * Created by gbm on 26/09/15.
 */
public class Arena {
    private int id;
    private int level;
    private Long pointsForClearingThis;
    private String name;
    private String description;
    private String shortDescription;

    private Map<Integer, ArenaObject> objects;
    private Map<ArenaLayout, List<ArenaObject>> objectsLayout;

    public Arena(int id,
                 String name,
                 int level,
                 Long pointsForClearingThis,
                 String description,
                 String shortDescription,
                 Map<ArenaLayout, List<ArenaObject>> objectsLayout){
        objectsLayout = new HashMap<>();
        this.level = level;
        this.name = name;
        this.description = description;
        this.shortDescription = shortDescription;
        this.id = id;
        this.objectsLayout = objectsLayout;
        this.pointsForClearingThis = pointsForClearingThis;
        fillObjectsMap();
    }

    protected void fillObjectsMap() {
        this.objects = new HashMap<>();
        for(List<ArenaObject> objects : this.objectsLayout.values()){
            for(ArenaObject object : objects) {
                this.objects.put(object.getId(), object);
            }
        }
    }

    public void removeObject(Integer id){
        if(this.objects.containsKey(id)) {
            ArenaObject object = this.objects.get(id);
            this.objects.remove(id);
            ArenaLayout layout = this.objectsLayout.entrySet().stream()
                    .filter(entry -> entry.getValue().stream().filter(o -> o.getId() == id).findAny().isPresent())
                    .findFirst().get().getKey();
            List<ArenaObject> objects = objectsLayout.get(layout);
            objects.remove(object);
            objectsLayout.put(layout, objects);
        }else{
            for(ArenaObject object : this.objects.values()){
                object.removeObject(id);
            }
        }
    }

    public void addObject(ArenaObject object, ArenaLayout layout){
        List<ArenaObject> objects;
        if(this.objectsLayout.containsKey(layout)){
            objects = this.objectsLayout.get(layout);
        }else {
            objects = new ArrayList<>();
        }
        objects.add(object);
        this.objectsLayout.put(layout, objects);
        fillObjectsMap();
    }

    public List<ArenaObject> getObjects(ArenaLayout layout){
        return this.objectsLayout.get(layout);
    }

    public ArenaObject chooseObject(int id, ArenaLayout layoutSelected){
        List<ArenaObject> objects = this.getObjects(layoutSelected);
        if(objects != null && !objects.isEmpty()){
            Optional<ArenaObject> object = objects.stream()
                    .filter(o -> o.getId() == id)
                    .findFirst();
            if(object.isPresent()){
                return object.get();
            }else{
                System.out.println("Invalid input. Requested item is not here. Please try again");
            }

        }else {
            System.out.println("Invalid input. No objects to choose from. Please try again");
        }
        return null;
    }

    public void addObject(ArenaObject object){
        throw new UnsupportedOperationException("Layout information is mandatory to add objects to an Arena");
    }

    public Map<Integer, ArenaObject> showContainedObjects(boolean isDeepTraverse) {
        if(isDeepTraverse){
            Map<Integer, ArenaObject> allContainedObjects = new HashMap<>();
            for(ArenaObject object : this.objects.values()){
                allContainedObjects.put(object.getId(), object);
                allContainedObjects.putAll(object.showContainedObjects(true));
            }
            return allContainedObjects;
        } else {
            return this.objects;
        }
    }

    public int getLevel() {
        return level;
    }

    public int getId(){
        return this.id;
    }

    public void explore(ArenaLayout layout){
        List<ArenaObject> objects = this.objectsLayout.get(layout);
        if(objects != null && !objects.isEmpty()){
            System.out.println("You have got some items over here!");
            objects.stream().forEach(o -> o.explore());
        } else {
            System.out.println("You have got nothing over here. Time to move on!");
        }
    }


    public void explore(){
        System.out.println(description);
    }

    public ArenaObject chooseObject(Integer id) {
        if(this.objects.containsKey(id))
            return this.objects.get(id);
        System.err.println("Invalid id entered.");
        return null;
    }

    public Long getPointsForClearingThis(){
        return pointsForClearingThis;
    }

    @Override
    public String toString(){
        return new StringBuilder()
                .append(String.format("%6s",id)).append("\t\t")
                .append(String.format("%12s",name)).append("\t\t")
                .append(String.format("%18s",shortDescription)).append("\t\t")
                .append(String.format("%12s",level))
                .toString();
    }
}
