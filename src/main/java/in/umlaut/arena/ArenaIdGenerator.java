package in.umlaut.arena;

/**
 * Created by gbm on 27/09/15.
 */
public class ArenaIdGenerator {
    private int id = 0;
    private int objectId = 0;

    private static ArenaIdGenerator idGenerator;

    private ArenaIdGenerator(){
    }

    public static ArenaIdGenerator getInstance(){
        if(idGenerator == null){
            idGenerator = new ArenaIdGenerator();
        }
        return idGenerator;
    }

    public int getNextId(){
        id = id + 1;
        return id;
    }

    public int getNextId(boolean isObject){
        objectId = objectId + 1;
        return objectId;
    }
}
