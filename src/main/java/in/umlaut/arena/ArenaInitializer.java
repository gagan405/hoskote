package in.umlaut.arena;

import in.umlaut.arena.arenaobjects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by gbm on 27/09/15.
 */
public class ArenaInitializer {
    ArenaObjectPool pool = ArenaObjectPool.getInstance();
    ArenaIdGenerator idGenerator = ArenaIdGenerator.getInstance();

    private static final String LEVEL1_DESC =
            "Simplest Arena you will ever find! \n\n"
                    + "Explore and experiment with objects you find.\n"
                    + "Your aim is to find the key to the door. \n";

    private static final String LEVEL2_DESC =
            "Arena level 2 \n\n"
                    + "Explore and experiment with objects you find.\n"
                    + "Your aim is to find the key to the door. \n";


    public ArenaInitializer() {
    }

    private Arena initializeArena() {

        ArenaBuilder builder = new ArenaBuilder();
        builder.setName("Arena Level 1")
                .setDescription(LEVEL1_DESC)
                .setShortDescription("Can't get any simpler!!")
                .setPointsForClearingThis(1000l)
                .setLevel(1);

        return builder.build();
    }

    private Arena initializeArenaLevel2() {

        ArenaBuilder builder = new ArenaBuilder();
        builder.setName("Arena Level 2")
                .setDescription(LEVEL2_DESC)
                .setShortDescription("A bit more than level 1")
                .setPointsForClearingThis(2000l)
                .setLevel(2);

        return builder.build();
    }

    public Arena getSimplestArena() {
        Arena arena = initializeArena();

        ArenaObjectBuilder keyBuilder = new ArenaObjectBuilder();
        keyBuilder.setId(idGenerator.getNextId(true));
        keyBuilder.setName("Key to the door");
        keyBuilder.setExplore("I am the key to the door. Pick me up and get on the road to freedom!");
        keyBuilder.setIsThisTheKey(true);
        keyBuilder.setAction(Actions.PICK);
        keyBuilder.setActionResult(new ActionResult(null, true, true, true, null, 500l));

        GenericArenaObject key = keyBuilder.build();
        pool.addObject(key);
        key.setOnAction(object -> null);

        ArenaObjectBuilder boxBuilder = new ArenaObjectBuilder();
        boxBuilder.setExplore("I am a glass box. There might be something inside me. Why don't you find out ?");
        boxBuilder.setName("Glass box");
        boxBuilder.setId(idGenerator.getNextId(true));
        List<ArenaObject> containedObject = new ArrayList<>();
        containedObject.add(key);
        boxBuilder.setContainedObjects(containedObject);
        boxBuilder.setContainerArena(arena);
        boxBuilder.setAction(Actions.BREAK);
        boxBuilder.setActionResult(new ActionResult(key, true, false, false, null, 200l));
        boxBuilder.setOnAction(object -> object.setDone(true));

        GenericArenaObject glassBox = boxBuilder.build();
        pool.addObject(glassBox);
        key.setContainer(glassBox);

        ArenaObjectBuilder hammerBuilder = new ArenaObjectBuilder();
        hammerBuilder.setExplore("I am Thor's hammer. Use me to smash things up beyond recognition!");
        hammerBuilder.setAction(Actions.PICK);
        hammerBuilder.setId(idGenerator.getNextId(true));
        hammerBuilder.setContainerArena(arena);
        hammerBuilder.setName("Thor's hammer");
        hammerBuilder.setActionResult(new ActionResult(null, true, true, false, null, 200l));
        hammerBuilder.setOnAction(object -> object.setDone(true));

        GenericArenaObject hammer = hammerBuilder.build();
        pool.addObject(hammer);
        glassBox.setActionOperator(hammer);

        arena.addObject(hammer, ArenaLayout.EAST);
        arena.addObject(glassBox, ArenaLayout.WEST);

        return arena;
    }

    public Arena getArenaLevel2() {
        Arena arena = initializeArenaLevel2();

        ArenaObjectBuilder keyBuilder = new ArenaObjectBuilder();
        keyBuilder.setId(idGenerator.getNextId(true));
        keyBuilder.setName("Key to the door");
        keyBuilder.setExplore("I am the key to the door. Pick me up and get on the road to freedom!");
        keyBuilder.setIsThisTheKey(true);
        keyBuilder.setAction(Actions.PICK);
        keyBuilder.setActionResult(new ActionResult(null, true, true, true, null, 500l));

        GenericArenaObject key = keyBuilder.build();
        pool.addObject(key);
        key.setOnAction(object -> null);

        ArenaObjectBuilder boxBuilder = new ArenaObjectBuilder();
        boxBuilder.setExplore("I am a safe box. There might be something inside me. Why don't you find out ? \n" +
                "You would need a 4 digit password to open me though!");
        boxBuilder.setName("Safe box");
        boxBuilder.setId(idGenerator.getNextId(true));
        List<ArenaObject> containedObject = new ArrayList<>();
        containedObject.add(key);
        boxBuilder.setContainedObjects(containedObject);
        boxBuilder.setContainerArena(arena);
        boxBuilder.setAction(Actions.INPUT);
        boxBuilder.setActionInput("3254");
        boxBuilder.setActionResult(new ActionResult(key, true, false, false, null, 200l));

        boxBuilder.setOnAction(object -> object.setDone(true));

        GenericArenaObject safeBox = boxBuilder.build();
        pool.addObject(safeBox);
        key.setContainer(safeBox);

        ArenaObjectBuilder clockBuilder = new ArenaObjectBuilder();
        clockBuilder.setExplore("I am a clock. But my battery is gone. You may want to see when I died");
        clockBuilder.setContainerArena(arena);
        clockBuilder.setName("A dead clock");
        clockBuilder.setAction(Actions.READ);
        clockBuilder.setId(idGenerator.getNextId(true));
        clockBuilder.setActionResult(new ActionResult(null, false, false, false, null, 0l));
        clockBuilder.setOnAction(arenaObject -> {
            System.out.println("I am pointing at 3:25. That fateful hour when I died!");
            return null;
        });

        GenericArenaObject clock = clockBuilder.build();
        pool.addObject(clock);


        ArenaObjectBuilder bookBuilder = new ArenaObjectBuilder();
        bookBuilder.setExplore("I am a book. Written by Sir Arthur Conan Doyle.");
        bookBuilder.setName("A Book");
        bookBuilder.setAction(Actions.READ);
        bookBuilder.setId(idGenerator.getNextId(true));
        bookBuilder.setActionResult(new ActionResult(null, false, false, false, null, 0l));
        bookBuilder.setOnAction(arenaObject -> {
            System.out.println("My title says : The Sign of Four. I am a pretty nice story of Sherlock Holmes.");
            return null;
        });

        GenericArenaObject book = bookBuilder.build();
        pool.addObject(book);

        ArenaObjectBuilder keyBuilder1 = new ArenaObjectBuilder();
        keyBuilder1.setName("Key to something else");
        keyBuilder1.setExplore("I am a key, not to the door unfortunately. Still you can pick me up and try on other objects!");
        keyBuilder1.setAction(Actions.PICK);
        keyBuilder1.setActionResult(new ActionResult(null, true, true, false, null, 200l));
        keyBuilder1.setOnAction(arenaObject -> null);
        keyBuilder1.setId(idGenerator.getNextId(true));
        keyBuilder1.setContainerArena(arena);
        GenericArenaObject key1 = keyBuilder1.build();
        pool.addObject(key1);

        ArenaObjectBuilder cupboardBuilder = new ArenaObjectBuilder();

        cupboardBuilder.setExplore("I am a cupboard. There might be something inside me. Why don't you find out ?");
        cupboardBuilder.setName("Locked cupboard");
        List<ArenaObject> containedObjectInCupBoard = new ArrayList<>();
        containedObject.add(book);
        cupboardBuilder.setContainedObjects(containedObjectInCupBoard);
        cupboardBuilder.setContainerArena(arena);
        cupboardBuilder.setId(idGenerator.getNextId(true));
        cupboardBuilder.setAction(Actions.OPEN);
        cupboardBuilder.setActionOperator(key1);
        cupboardBuilder.setOnAction(arenaObject -> null);
        cupboardBuilder.setActionResult(new ActionResult(book, true, false, false, null, 300l));

        GenericArenaObject cupBoard = cupboardBuilder.build();
        pool.addObject(cupBoard);

        book.setContainer(cupBoard);

        arena.addObject(clock, ArenaLayout.NORTH);
        arena.addObject(cupBoard, ArenaLayout.WEST);
        arena.addObject(key1, ArenaLayout.NORTH);
        arena.addObject(safeBox, ArenaLayout.WEST);

        return arena;
    }
}
