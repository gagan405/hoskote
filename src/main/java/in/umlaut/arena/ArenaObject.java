package in.umlaut.arena;

import in.umlaut.arena.arenaobjects.ActionResult;
import in.umlaut.arena.arenaobjects.Actions;

import java.util.Map;
import java.util.function.Function;

/**
 * Created by gbm on 26/09/15.
 */
public interface ArenaObject {
    int getId();
    //Explore this object
    void explore();
    //GetName
    String getName();
    //Get details on whatActions can be taken on this object
    void exploreActions();
    //Can we explore contained objects?
    boolean canExploreContainedObjects();
    //Get Details of contained objects
    void exploreContainedObjects();
    //Remove a contained object
    void removeObject(Integer id);
    //choose a contained object to work upon
    ArenaObject chooseObject(Integer id);
    //apply action on this object
    ActionResult apply(Actions action, String[] args);
    //Apply action on this object with the help of another
    ActionResult apply(Actions action, ArenaObject object);
    //Add an object to contain inside
    void addObject(ArenaObject object);
    //show contained objects 1 level deep
    Map<Integer, ArenaObject> showContainedObjects(boolean isDeepTraverse);
    //Return to the arena object that contains this object
    ArenaObject getContainer();
    //Return container Arena
    Arena getContainerArena();

    boolean isActionable(ArenaObject object, Actions action);
    boolean isActionable(Actions action);
    boolean isActionable(Actions action, String input);
    boolean isThisTheKey();
    boolean isDone();

    void setOnAction(Function<ArenaObject, Void> onAction);
    void setContainer(ArenaObject container);
    void setActionOperator(ArenaObject actionOperator);
    Void setDone(boolean done);

}
