package in.umlaut.arena.arenaobjects;

import in.umlaut.arena.Arena;
import in.umlaut.arena.ArenaObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by gbm on 27/09/15.
 */
public class GenericArenaObject implements ArenaObject {
    private static final String INVALID_ACTION = "Invalid action request. Please explore this object for details.";
    private int id;
    private String name;
    private String explore;

    private Actions action;

    private ArenaObject actionOperator;
    private String actionInput;
    private ActionResult result;

    private ArenaObject container;
    private Arena containerArena;
    private Map<Integer, ArenaObject> objects;
    private Function<ArenaObject, Void> onAction;

    private boolean isDone = false;

    private boolean isThisTheKey;
    private boolean canExploreContainedItems;

    public GenericArenaObject(ArenaObjectBuilder builder){
        this.id = builder.getId();
        this.container = builder.getContainer();
        this.actionOperator = builder.getActionOperator();
        this.containerArena = builder.getContainingArena();
        this.name = builder.getName();
        this.isThisTheKey = builder.isThisTheKey();
        this.explore = builder.getExplore();
        this.onAction = builder.getOnAction();
        this.action = builder.getAction();
        this.result = builder.getResult();
        this.actionInput = builder.getActionInput();
        fillObjectsMap(builder.getContainedObjects());
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

    public void setActionOperator(ArenaObject actionOperator) {
        this.actionOperator = actionOperator;
    }

    @Override
    public Void setDone(boolean done) {
        this.isDone = done;
        return null;
    }

    @Override
    public void explore() {
        System.out.println(id + "\t\t" + name + "\t\t" + explore);
    }

    @Override
    public void exploreActions() {
        if(this.action != null)
            System.out.println(new StringBuilder().append(this.action.getMessage()).toString());
    }

    @Override
    public boolean canExploreContainedObjects() {
        return this.canExploreContainedItems;
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
    public ActionResult apply(Actions action, String[] args) {
        if(isDone()){
            System.out.println("Action has already been taken on this object");
            return null;
        }
        if(args != null && args.length > 0){
            return apply(action, String.join(" ", args));
        }
        if(isActionable(action)) {
            onAction.apply(this);
            return this.result;
        }
        System.out.println(INVALID_ACTION);
        return null;
    }


    @Override
    public ActionResult apply(Actions action, ArenaObject object) {
        if(isDone()){
            System.out.println("Action has already been taken on this object");
            return null;
        }
        if(isActionable(object, action)){
            this.onAction.apply(this);
            return this.result;
        }
        System.out.println(INVALID_ACTION);
        return null;
    }

    public ActionResult apply(Actions action, String input) {
        if(isDone()){
            System.out.println("Action has already been taken on this object");
            return null;
        }
        if(isActionable(action, input)){
            this.onAction.apply(this);
            return this.result;
        }
        System.out.println(INVALID_ACTION);
        return null;
    }

    @Override
    public String getName() {
        return name;
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

    public boolean isActionable(Actions action) {
        return this.action.equals(action) && this.actionOperator == null && this.actionInput == null;
    }

    @Override
    public boolean isActionable(ArenaObject object, Actions action) {
        return this.action.equals(action) && this.actionOperator != null
                && this.actionOperator.getId() == object.getId();
    }

    public boolean isActionable(Actions action, String input){
        return this.action.equals(action) && this.actionOperator == null
                && this.actionInput.equalsIgnoreCase(input);
    }

    @Override
    public boolean isThisTheKey() {
        return isThisTheKey;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public void exploreContainedObjects() {
        if(this.canExploreContainedItems)
            this.objects.values().stream().forEach(o -> o.explore());
        else if(this.objects.isEmpty()){}
        else
            System.out.println("Cannot explore contained objects yet. May be you need to take some action on this first.");
    }

    public void setOnAction(Function<ArenaObject, Void> onAction) {
        this.onAction = onAction;
    }

    public void setContainer(ArenaObject container) {
        this.container = container;
    }
}
