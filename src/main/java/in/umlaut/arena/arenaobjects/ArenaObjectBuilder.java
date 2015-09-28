package in.umlaut.arena.arenaobjects;

import in.umlaut.arena.Arena;
import in.umlaut.arena.ArenaIdGenerator;
import in.umlaut.arena.ArenaObject;

import java.util.List;

/**
 * Created by gbm on 27/09/15.
 */
public class ArenaObjectBuilder {
    private GenericArenaObject genericArenaObject;
    private ArenaIdGenerator idGenerator = ArenaIdGenerator.getInstance();
    private ArenaObjectPool objectPool = ArenaObjectPool.getInstance();

    private String name;
    private ArenaObject container;
    private Arena containingArena;

    private boolean isMovable = false;
    private boolean isPickable = false;
    private boolean isUsableOnOtherObject = false;
    private boolean isUsableByOtherObject = false;
    private boolean isThisTheKey = false;
    private ArenaObject findObjectOnMoveOrUse;
    private ArenaObject usableObject;
    private boolean canApplyInput = false;
    private String inputToEnter;
    private String explore;
    private ArenaObject objectToReturnOnCorrectInput;
    private Long pointsForFindingThis = 0l;

    private List<ArenaObject> containedObjects;

    public ArenaObjectBuilder(){

    }

    public ArenaObjectBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ArenaObjectBuilder setContainer(ArenaObject container) {
        this.container = container;
        return this;
    }

    public ArenaObjectBuilder setPointsForFindingThis(Long points){
        this.pointsForFindingThis = points;
        return this;
    }

    public ArenaObjectBuilder setContainerArena(Arena container) {
        this.containingArena = container;
        return this;
    }

    public ArenaObjectBuilder setIsMovable(boolean isMovable) {
        this.isMovable = isMovable;
        return this;
    }

    public ArenaObjectBuilder setIsThisTheKey(boolean isThisTheKey) {
        this.isThisTheKey = isThisTheKey;
        return this;
    }

    public ArenaObjectBuilder setIsPickable(boolean isPickable) {
        this.isPickable = isPickable;
        return this;
    }

    public ArenaObjectBuilder setIsUsableOnOtherObject(boolean isUsableOnOtherObject) {
        this.isUsableOnOtherObject = isUsableOnOtherObject;
        return this;
    }

    public ArenaObjectBuilder setIsUsableByOtherObject(boolean isUsableByOtherObject) {
        this.isUsableByOtherObject = isUsableByOtherObject;
        return this;
    }

    public ArenaObjectBuilder setFindObjectOnMoveOrUse(ArenaObject findObjectOnMoveOrUse) {
        this.findObjectOnMoveOrUse = findObjectOnMoveOrUse;
        return this;
    }

    public ArenaObjectBuilder setUsableObject(ArenaObject usableObject) {
        this.usableObject = usableObject;
        return this;
    }

    public ArenaObjectBuilder setCanApplyInput(boolean canApplyInput) {
        this.canApplyInput = canApplyInput;
        return this;
    }

    public ArenaObjectBuilder setInputToEnter(String inputToEnter) {
        this.inputToEnter = inputToEnter;
        return this;
    }

    public ArenaObjectBuilder setExplore(String explore) {
        this.explore = explore;
        return this;
    }

    public ArenaObjectBuilder setContainedObjects(List<ArenaObject> containedObjects) {
        this.containedObjects = containedObjects;
        return this;
    }

    public ArenaObjectBuilder setObjectToReturnOnCorrectInput(ArenaObject objectToReturnOnCorrectInput) {
        this.objectToReturnOnCorrectInput = objectToReturnOnCorrectInput;
        return this;
    }

    public GenericArenaObject build(){
        GenericArenaObject object = new GenericArenaObject(idGenerator.getNextId(true),
                container, containingArena, name, isMovable, isPickable, isUsableOnOtherObject, isUsableByOtherObject, isThisTheKey,
                findObjectOnMoveOrUse, usableObject, canApplyInput, inputToEnter,
                objectToReturnOnCorrectInput, explore, containedObjects, pointsForFindingThis);
        objectPool.addObject(object);
        return object;
    }
}
