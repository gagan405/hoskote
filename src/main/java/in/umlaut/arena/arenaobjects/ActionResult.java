package in.umlaut.arena.arenaobjects;

import in.umlaut.arena.ArenaObject;

import java.util.Optional;

/**
 * Created by gbm on 28/09/15.
 */
public class ActionResult {
    private ArenaObject foundObject;
    private boolean isFound;
    private boolean isDone;
    private boolean isKeyFound;
    private String description;
    private Long points;
    public ActionResult(ArenaObject object,
                        boolean isDone,
                        boolean isFound,
                        boolean isKeyFound,
                        String description,
                        Long points
                        ){
        this.foundObject = object;
        this.isDone = isDone;
        this.isFound = isFound;
        this.isKeyFound = isKeyFound;
        this.description = description;
        this.points = points;
    }

    public ArenaObject getObject() {
        return foundObject;
    }

    public boolean isFound() {
        return isFound;
    }

    public  boolean isKeyFound(){
        return isKeyFound;
    }

    public boolean isDone() {
        return isDone;
    }

    public String getDescription() {
        return description;
    }

    public Long getPoints(){
        return points;
    }
}
