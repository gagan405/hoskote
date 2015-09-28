package in.umlaut.arena;

import java.util.Map;

/**
 * Created by gbm on 26/09/15.
 */
public interface ArenaObject {
    int getId();
    //Explore this object
    void explore();

    //Get details on whatActions can be taken on this object
    void exploreActions();

    //Remove a contained object
    void removeObject(Integer id);

    //choose a contained object to work upon
    ArenaObject chooseObject(Integer id);

    //pick this object
    ArenaObject pick();

    String getName();

    //Move a contained object
    ArenaObject move();

    //Use one contained object on another
    ArenaObject useObjectOn(Integer id1, Integer id2);

    //Use object
    ArenaObject useObject(ArenaObject object);

    //Apply custom input
    ArenaObject applyInput(String input);

    //Add an object
    void addObject(ArenaObject object);

    //show contained objects 1 level deep
    Map<Integer, ArenaObject> showContainedObjects(boolean isDeepTraverse);

    //Return to the arena object that contains this object
    ArenaObject getContainer();

    //Return container Arena
    Arena getContainerArena();

    Long getPointsForFindingThis();

    void setContainer(ArenaObject object);
    void setUsableByObject(ArenaObject object);

    boolean isPickable();
    boolean isMovable();
    boolean isUsableBy(ArenaObject object);
    boolean canApplyInput();
    boolean isMyRoleOver();
    boolean isFound();
    boolean isUsableByOtherObject();
    boolean isThisTheKey();
}
