package in.umlaut.arena.arenaobjects;

import in.umlaut.arena.Arena;
import in.umlaut.arena.ArenaIdGenerator;
import in.umlaut.arena.ArenaObject;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by gbm on 27/09/15.
 */
public class ArenaObjectBuilder {

    private int id;
    private String name;
    private String explore;
    private ArenaObject container;
    private Arena containingArena;

    private Actions action;
    private ArenaObject actionOperator;
    private String actionInput;
    private ActionResult result;

    private Function<ArenaObject, Void> onAction;

    private boolean isThisTheKey = false;

    private List<ArenaObject> containedObjects;

    public ArenaObjectBuilder(){

    }

    public ArenaObjectBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ArenaObjectBuilder setId(Integer id) {
        this.id = id;
        return this;
    }

    public ArenaObjectBuilder setAction(Actions action){
        this.action = action;
        return this;
    }

    public ArenaObjectBuilder setActionOperator(ArenaObject operator){
        this.actionOperator = operator;
        return this;
    }

    public ArenaObjectBuilder setActionInput(String input){
        this.actionInput = input;
        return this;
    }

    public ArenaObjectBuilder setActionResult(ActionResult result){
        this.result = result;
        return this;
    }

    public ArenaObjectBuilder setOnAction(Function<ArenaObject, Void> onAction){
        this.onAction = onAction;
        return this;
    }

    public ArenaObjectBuilder setExplore(String explore) {
        this.explore = explore;
        return this;
    }

    public ArenaObjectBuilder setContainer(ArenaObject container) {
        this.container = container;
        return this;
    }

    public ArenaObjectBuilder setContainerArena(Arena container) {
        this.containingArena = container;
        return this;
    }


    public ArenaObjectBuilder setIsThisTheKey(boolean isThisTheKey) {
        this.isThisTheKey = isThisTheKey;
        return this;
    }

    public ArenaObjectBuilder setContainedObjects(List<ArenaObject> containedObjects) {
        this.containedObjects = containedObjects;
        return this;
    }


    public GenericArenaObject build(){
        return new GenericArenaObject(this);
    }

    public String getName() {
        return name;
    }

    public String getExplore() {
        return explore;
    }

    public ArenaObject getContainer() {
        return container;
    }

    public Arena getContainingArena() {
        return containingArena;
    }

    public Actions getAction() {
        return action;
    }

    public ArenaObject getActionOperator() {
        return actionOperator;
    }

    public String getActionInput() {
        return actionInput;
    }

    public ActionResult getResult() {
        return result;
    }

    public Function<ArenaObject, Void> getOnAction() {
        return onAction;
    }

    public boolean isThisTheKey() {
        return isThisTheKey;
    }

    public List<ArenaObject> getContainedObjects() {
        return containedObjects;
    }

    public int getId(){
        return id;
    }
}
