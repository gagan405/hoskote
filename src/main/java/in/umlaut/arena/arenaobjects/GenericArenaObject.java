package in.umlaut.arena.arenaobjects;

import in.umlaut.arena.Arena;
import in.umlaut.arena.ArenaObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gbm on 27/09/15.
 */
public class GenericArenaObject implements ArenaObject {
    private int id;
    private Long pointsForFindingThis;
    private String name;
    private ArenaObject container;
    private Arena containerArena;
    private Map<Integer, ArenaObject> objects;

    private boolean isFound;
    private boolean isMyRoleOver;
    private boolean isThisTheKey;

    private boolean isMovable;
    private boolean isPickable;
    private boolean isUsableOnOtherObject;
    private boolean isUsableByOtherObject;

    private ArenaObject findObjectOnMoveOrUse;
    private ArenaObject usableByObject;
    private boolean canApplyInput;
    private String inputToEnter;
    private String explore;
    private ArenaObject objectToReturnOnCorrectInput;

    public GenericArenaObject(Integer id,
                              ArenaObject container,
                              Arena containerArena,
                              String name,
                              boolean isMovable,
                              boolean isPickable,
                              boolean isUsableOnOtherObject,
                              boolean isUsableByOtherObject,
                              boolean isThisTheKey,
                              ArenaObject findObjectOnMoveOrUse,
                              ArenaObject usableByObject,
                              boolean canApplyInput,
                              String inputToEnter,
                              ArenaObject objectToReturnOnCorrectInput,
                              String explore,
                              List<ArenaObject> containedObjects,
                              Long pointsForFindingThis
                              ){
        this.id = id;
        this.container = container;
        this.containerArena = containerArena;
        this.name = name;
        this.isMovable = isMovable;
        this.isPickable = isPickable;
        this.isUsableOnOtherObject = isUsableOnOtherObject;
        this.isUsableByOtherObject = isUsableByOtherObject;
        this.findObjectOnMoveOrUse = findObjectOnMoveOrUse;
        this.isThisTheKey = isThisTheKey;
        this.usableByObject = usableByObject;
        this.canApplyInput = canApplyInput;
        this.inputToEnter = inputToEnter;
        this.objectToReturnOnCorrectInput = objectToReturnOnCorrectInput;
        this.explore = explore;
        this.pointsForFindingThis = pointsForFindingThis;
        fillObjectsMap(containedObjects);
    }

    private void fillObjectsMap(List<ArenaObject> containedObjects) {
        this.objects = new HashMap<>();
        if(containedObjects != null) {
            for (ArenaObject object : containedObjects) {
                this.objects.put(object.getId(), object);
            }
        }
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public Long getPointsForFindingThis(){
        return pointsForFindingThis;
    }

    @Override
    public void explore() {
        System.out.println(id + "\t\t" + name + "\t\t" + explore);
    }

    @Override
    public void exploreActions() {
        StringBuilder sb = new StringBuilder();
        if(isMovable()){
            sb.append("You can move this object. Use M to move me aside \n");
        }
        if(isPickable()){
            sb.append("You can pick this object. Use P to pick me up \n");
        }
        if(isUsableOnOtherObject){
            sb.append("I might be used on some other object. Use P to pick me up and use when needed \n");
        }
        if(isUsableByOtherObject()){
            sb.append("I might be used by some other object. \n");
        }
        System.out.println(sb.toString());
    }

    @Override
    public void removeObject(Integer id) {
        this.objects.remove(id);
    }

    @Override
    public ArenaObject chooseObject(Integer id) {
        return this.objects.get(id);
    }

    @Override
    public ArenaObject pick() {
        if(isPickable()) {
            isFound = true;
            return this;
        }
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ArenaObject move() {
        if(isMovable()){
            isFound = true;
            isMyRoleOver = true;
            return findObjectOnMoveOrUse;
        } else return null;
    }

    @Override
    public ArenaObject useObjectOn(Integer id1, Integer id2) {
        if(objects.get(id2).isUsableBy(objects.get(id1))){
            return objects.get(id2).useObject(objects.get(id1));
        } else{
            System.err.println("Cannot use object " + id1 + " on object " + id2);
        }
        return null;
    }

    @Override
    public ArenaObject useObject(ArenaObject object) {
        if(isUsableBy(object)){
            isFound = true;
            isMyRoleOver = true;
            return findObjectOnMoveOrUse;
        }
        return null;
    }

    @Override
    public ArenaObject applyInput(String input) {
        if(canApplyInput && inputToEnter.equalsIgnoreCase(input)){
            isMyRoleOver = true;
            isFound = true;
            return objectToReturnOnCorrectInput;
        }
        return null;
    }

    @Override
    public void addObject(ArenaObject object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<Integer, ArenaObject> showContainedObjects(boolean isDeepTraverse) {
        if(isDeepTraverse){
            Map<Integer, ArenaObject> allContainedObjects = new HashMap<>();
            for(ArenaObject object : this.objects.values()){
                allContainedObjects.put(object.getId(), object);
                allContainedObjects.putAll(object.showContainedObjects(true));
            }
            return allContainedObjects;
        }else {
            return this.objects;
        }
    }

    @Override
    public ArenaObject getContainer() {
        return container;
    }

    public Arena getContainerArena(){
        return containerArena;
    }

    @Override
    public void setContainer(ArenaObject object) {
        this.container = object;
    }

    @Override
    public boolean isPickable() {
        return isPickable;
    }

    @Override
    public boolean isMovable() {
        return isMovable;
    }

    @Override
    public boolean isUsableBy(ArenaObject object) {
        return isUsableByOtherObject && usableByObject.getId() == object.getId();
    }

    @Override
    public boolean canApplyInput() {
        return canApplyInput;
    }

    @Override
    public boolean isMyRoleOver() {
        return isMyRoleOver;
    }

    @Override
    public boolean isFound() {
        return isFound;
    }

    @Override
    public boolean isUsableByOtherObject() {
        return isUsableByOtherObject;
    }

    @Override
    public boolean isThisTheKey() {
        return isThisTheKey;
    }


    public void setUsableByObject(ArenaObject object){
        this.usableByObject = object;
    }
}
