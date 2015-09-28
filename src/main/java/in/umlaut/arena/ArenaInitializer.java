package in.umlaut.arena;

import in.umlaut.arena.arenaobjects.ArenaObjectBuilder;
import in.umlaut.arena.arenaobjects.GenericArenaObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gbm on 27/09/15.
 */
public class ArenaInitializer {
    private static final String LEVEL1_DESC =
            "Simplest Arena you will ever find! \n\n"
                    +"Explore and experiment with objects you find.\n"
                    +"Your aim is to find the key to the door. \n";


    public ArenaInitializer(){}

    private Arena initializeArena(){

        ArenaBuilder builder = new ArenaBuilder();
        builder.setName("Arena Level 1")
            .setDescription(LEVEL1_DESC)
            .setShortDescription("Can't get any simpler!!")
            .setPointsForClearingThis(1000l)
            .setLevel(1);

        return builder.build();
    }

    public Arena getSimplestArena(){
        Arena arena = initializeArena();

        ArenaObjectBuilder keyBuilder = new ArenaObjectBuilder();
        keyBuilder.setName("Key to the door");
        keyBuilder.setExplore("I am the key to the door. Pick me up and get on the road to freedom!");
        keyBuilder.setIsPickable(true);
        keyBuilder.setIsThisTheKey(true);
        keyBuilder.setPointsForFindingThis(500l);

        GenericArenaObject key = keyBuilder.build();

        ArenaObjectBuilder boxBuilder = new ArenaObjectBuilder();
        boxBuilder.setExplore("I am a glass box. There might be something inside me. Why don't you find out ?");
        boxBuilder.setName("Glass box");
        List<ArenaObject> containedObject = new ArrayList<>();
        containedObject.add(key);
        boxBuilder.setContainedObjects(containedObject);
        boxBuilder.setContainerArena(arena);
        boxBuilder.setIsUsableByOtherObject(true);
        boxBuilder.setFindObjectOnMoveOrUse(key);
        boxBuilder.setPointsForFindingThis(200l);

        GenericArenaObject glassBox = boxBuilder.build();
        key.setContainer(glassBox);

        ArenaObjectBuilder hammerBuilder = new ArenaObjectBuilder();
        hammerBuilder.setExplore("I am Thor's hammer. Use me to smash things up beyond recognition!");
        hammerBuilder.setIsPickable(true);
        hammerBuilder.setContainerArena(arena);
        hammerBuilder.setName("Thor's hammer");
        hammerBuilder.setIsUsableOnOtherObject(true);
        hammerBuilder.setPointsForFindingThis(200l);

        GenericArenaObject hammer = hammerBuilder.build();
        glassBox.setUsableByObject(hammer);

        arena.addObject(hammer, ArenaLayout.EAST);
        arena.addObject(glassBox, ArenaLayout.WEST);

        return arena;
    }
}
