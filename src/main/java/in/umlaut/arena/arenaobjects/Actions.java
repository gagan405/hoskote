package in.umlaut.arena.arenaobjects;

/**
 * Created by gbm on 28/09/15.
 */
public enum Actions {
    READ("I am Readable. Enter READ to readme!"),
    MOVE("I am movable. You may find something on moving me. Enter MOVE to move me aside."),
    OPEN("I can be opened. You may or may not need an opener. Use OPEN to open me."),
    BREAK("I am breakable. You may or may not need a breaking object. Use BREAK to break me."),
    PICK("I am pickable. Enter PICK to pick me up."),
    INPUT("I need an INPUT. Enter INPUT <val> to give me inputs."),
    JOIN("I need another object to join. Enter JOIN <id> to join me with an another object.");

    private String message;

    Actions(String msg){
        this.message = msg;
    }

    public String getMessage(){
        return this.message;
    }
}
