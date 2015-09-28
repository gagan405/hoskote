package in.umlaut.game;

/**
 * Created by gbm on 28/09/15.
 */
public class GameIdGenerator {
    private int id = 0;

    private static GameIdGenerator idGenerator;

    private GameIdGenerator(){
    }

    public static GameIdGenerator getInstance(){
        if(idGenerator == null){
            idGenerator = new GameIdGenerator();
        }
        return idGenerator;
    }

    public int getNextId(){
        id = id + 1;
        return id;
    }

    protected void setId(int id){
        this.id = id;
    }
}
