package in.umlaut.game;

import in.umlaut.arena.Arena;

import in.umlaut.arena.ArenaLayout;
import in.umlaut.arena.ArenaObject;
import in.umlaut.player.Player;
import in.umlaut.views.KbInputHandler;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by gbm on 26/09/15.
 */
public class Game implements KbInputHandler{
    GameIdGenerator idGenerator = GameIdGenerator.getInstance();

    private int id;
    private static final String helpMessage =  "Press: \n"
            +"'H': Help on the Game and Arena \n"
            +"'S|W|N|E' : Choose directions in the room, Pressing 'E' will explore the region if direction is already chosen \n"
            +"'C1': Choose object 1 to act on. Use CN for nth object. Objects can be chosen only from the chosen region previously \n"
            +"'R' : Reset selections, this resets direction chosen, as well as the object \n"
            +"'E' : Explore the region to see what objects are there. If Object is chosen, it explores what actions can be taken on the Object \n"
            +"'P' : Pick up the chosen object. You must choose an object first to pick it up. All Objects might not be pickable \n"
            +"'M' : Move a chosen object. You must choose an object first to move it. All Objects might not be movable. \n"
            +"'U' : Use another object on a chosen object. The other object must be one from the objects found till now. \n"
            +"'F' : Show present score and all found objects till now. \n"
            +"'Q' : Quit the game \n\n"
            +"Any action(P|M|U) taken on the objects, resets the selections including direction. \n";

    private static final String initialMessage = "You are about to start the Game. \n"
            + "Please read carefully before you proceed. \n\n"
            + helpMessage
            +"Good luck and get going! \n";

    private List<GameObserver> observers;

    private Scanner in;

    private Player player;
    private Arena arena;
    private Long score = 0l;
    private int remainingLives = 3;

    protected Player getPlayer() {
        return player;
    }

    protected Arena getArena() {
        return arena;
    }

    protected Long getScore() {
        return score;
    }

    public int getId(){
        return id;
    }

    protected int getRemainingLives() {
        return remainingLives;
    }

    protected Date getStartTime() {
        return startTime;
    }

    protected ArenaObject getSelectedObject() {
        return selectedObject;
    }

    protected List<ArenaObject> getFoundObjects() {
        return foundObjects;
    }

    protected boolean isKeyFound() {
        return isKeyFound;
    }

    protected boolean isExit() {
        return isExit;
    }

    protected boolean isDirectionSelected() {
        return isDirectionSelected;
    }

    protected ArenaLayout getLayoutSelected() {
        return layoutSelected;
    }

    private Date startTime;

    private ArenaObject selectedObject;
    private List<ArenaObject> foundObjects = new ArrayList<>();
    private boolean isKeyFound;
    private boolean isExit;
    private boolean isDirectionSelected;
    private ArenaLayout layoutSelected;

    public Game(Player player, Arena arena){

        this.id = idGenerator.getNextId();
        this.arena = arena;
        this.player = player;
        observers = new ArrayList<>();
    }

    protected Game(Integer id, Player player, Arena arena, Long score, ArenaObject selectedObject, List<ArenaObject> foundObjects,
                   boolean isDirectionSelected, ArenaLayout layoutSelected){
        this.id = id;
        this.player = player;
        this.arena = arena;
        this.score = score;
        this.selectedObject = selectedObject;
        this.foundObjects = foundObjects;
        this.isDirectionSelected = isDirectionSelected;
        this.layoutSelected = layoutSelected;
        observers = new ArrayList<>();
    }

    @Override
    public void handleKeyStroke() {
        showInitialMessage();
        in = new Scanner(System.in);
        String token = readInput(in);
        while (true){
            switch (token.toUpperCase()){
                case "H" :
                    arena.explore();
                    showHelpMessage();
                    break;
                case "F" :
                    displayScoreAndFoundItems();
                    break;
                case "U" :
                    if(checkSelection()){
                        if(selectedObject.isUsableByOtherObject()){
                            handleUse();
                        } else{
                            System.out.println("Invalid action. You cannot use another object on this item.");
                        }
                    }else {
                        System.out.println("No object has been selected. First select an object to take action.");
                    }
                    break;
                case "P" :
                    if(checkSelection()){
                        if(selectedObject.isPickable()){
                            handlePick();
                        } else{
                            System.out.println("Invalid action. You cannot pick this object.");
                        }
                    }else {
                        System.out.println("No object has been selected. First select an object to take action.");
                    }
                    break;

                case "Q" :
                    handleQuit();
                    break;
                case "R" :
                    resetSelections();
                    break;
                case "M" :
                    if(checkSelection()){
                        if(selectedObject.isMovable()){
                            handleMove();
                        } else{
                            System.out.println("Invalid action. You cannot move this object.");
                        }
                    }else {
                        System.out.println("No object has been selected. First select an object to take action.");
                    }
                    break;
                case "E" :
                case "W" :
                case "S" :
                case "N" :
                    handleDirectionSelection(token);
                    break;
                default:
                    if(token.toUpperCase().startsWith("C")){
                        if (!isDirectionSelected) {
                            System.out.println("Please choose a direction first to explore");
                        } else {
                            boolean isSuccess = chooseObject(token);
                        }
                    }else {
                        System.out.println("Invalid input. Please try again");
                    }
            }
            if(isKeyFound || isExit){
                break;
            }
            token = readInput(in);
        }

    }

    private void handleQuit() {
        System.out.println("Do you want to save the game and resume later ?");
        String input = readInput(in);
        while (!(input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("N"))){
            System.out.println("Invalid input. Please enter Y or N");
            input = readInput(in);
        }
        if(!input.equalsIgnoreCase("N")){
            //Save the game state
            GameSaver gameSaver = GameSaver.getInstance();
            gameSaver.save(this);
            System.out.println("Game is saved!");
        }
        isExit = true;
    }

    private void handleDirectionSelection(String token) {
        if(token.toUpperCase().equalsIgnoreCase("E") && checkSelection()){
            selectedObject.exploreActions();
        } else {
            if (!isDirectionSelected) {
                isDirectionSelected = true;
                layoutSelected = ArenaLayout.fromInitial(token.toUpperCase());
                System.out.println("You are now facing " + layoutSelected.name() + " of the room. \n" +
                        "Press 'E' to explore what all you have got. \n"
                        + "Press 'R' to reset the direction selection.");
            } else {
                if(!token.toUpperCase().equalsIgnoreCase("E")){
                    System.out.println("Invalid input.");
                    System.out.println("You have already chosen a direction. Press 'E' to explore or 'R' to reset.");
                }else {
                    arena.explore(layoutSelected);
                }
            }
        }
    }

    private void showInitialMessage() {
        System.out.println(initialMessage);
    }

    private void showHelpMessage(){
        System.out.println(helpMessage);
    }

    private void handleUse() {
        System.out.println("Input the id of the object to use on this item. You can try objects found till now.");
        displayFoundItems();
        if(foundObjects.isEmpty()){
            System.out.println("You have not yet found any object to use on this object. First pick up some items to use on this.");
            return;
        }
        String token = readInput(in);
        Map<Integer, ArenaObject> integerArenaObjectMap = foundObjects.stream().collect(Collectors.toMap(
                e -> e.getId(), e -> e
        ));
        Integer id;
        try {
            id = Integer.parseInt(token);
        }catch (NumberFormatException e){
            System.out.println("Invalid id. Cannot find item to use.");
            return;
        }
        if(integerArenaObjectMap.containsKey(id)) {
            ArenaObject object = integerArenaObjectMap.get(id);
            ArenaObject foundObject = selectedObject.useObject(object);
            if(foundObject == null){
                System.out.println(object.getName() + " cannot be used on " + selectedObject.getName() + ". Try some other object.");
            }else {
                updateScoreOnFindingObject(foundObject);
                updateScoreOnFindingObject(selectedObject);
                if (isKeyFound(foundObject)) {
                    handleFinalization();
                } else {
                    foundObjects.add(foundObject);
                }
            }
        }else{
            System.out.println("Input an id from the found objects.");
        }
    }

    private void displayFoundItems() {
        if(!foundObjects.isEmpty()) {
            System.out.println("Objects found till now : ");
            foundObjects.stream().forEach(o -> System.out.println("ID: " + o.getId() + " Name : " + o.getName()));
        } else {
            System.out.println("You have not found any object yet. Keep trying.");
        }
    }

    private void handlePick() {
        ArenaObject object = selectedObject.pick();
        arena.removeObject(object.getId());
        foundObjects.add(object);
        updateScoreOnFindingObject(object);
        if(isKeyFound(object)){
            handleFinalization();
        }else {
            System.out.println("You found the object : " + object.getName());
            System.out.println("Great going");
            displayScoreAndFoundItems();
            System.out.println("Resetting selections ...");
            resetSelections();
        }
    }

    private void handleFinalization() {
        isKeyFound = true;
        System.out.println("Congratulations. You have found the key. You are now qualified to play the next level.");
        score = score + arena.getPointsForClearingThis();
        System.out.println("Your total score is : " + score);
        observers.stream().forEach(o -> o.handleGameUpdates(id, player, score, arena, true));
    }

    private boolean isKeyFound(ArenaObject object) {
        return object.isThisTheKey();
    }

    private void handleMove() {
        ArenaObject object = selectedObject.move();
        arena.removeObject(object.getId());
        foundObjects.add(object);
        updateScoreOnFindingObject(object);
        if(isKeyFound(object)){
            handleFinalization();
        }else {
            System.out.println("You found the object : " + object.getName());
            System.out.println("Great going");
            displayScoreAndFoundItems();
            System.out.println("Resetting selections ...");
            resetSelections();
        }
    }

    private void updateScoreOnFindingObject(ArenaObject object) {
        score += object.getPointsForFindingThis();
    }

    private void displayScoreAndFoundItems() {
        displayFoundItems();
        displayScore();
    }

    private void displayScore() {
        System.out.println("\nPresent score : " + score);
    }

    private boolean checkSelection() {
        return selectedObject != null;
    }

    private void resetSelections() {
        isDirectionSelected = false;
        layoutSelected = null;
        System.out.println("Chosen direction is reset");
        if(selectedObject != null){
            selectedObject = null;
            System.out.println("Chosen object is reset");
        }
    }

    private boolean chooseObject(String token) {
        boolean result = false;
        try {
            int itemId = Integer.parseInt(token.substring(1));
            List<ArenaObject> objects = arena.getObjects(layoutSelected);
            if(objects != null && !objects.isEmpty()){
                Optional<ArenaObject> object = objects.stream()
                        .filter(o -> o.getId() == itemId)
                        .findFirst();
                if(object.isPresent()){
                    selectedObject = object.get();
                    System.out.println("Requested item is selected. Press 'R' to reset. Press E to explore this item.");
                    result = true;
                }else{
                    System.out.println("Invalid input. Requested item is not here. Please try again");
                }
            }else {
                System.out.println("Invalid input. No objects to choose from. Please try again");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please try again");
        }
        return result;
    }

    public void addObserver(GameObserver observer){
        this.observers.add(observer);
    }


}
