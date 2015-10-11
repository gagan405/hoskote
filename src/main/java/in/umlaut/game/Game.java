package in.umlaut.game;

import in.umlaut.arena.Arena;

import in.umlaut.arena.ArenaLayout;
import in.umlaut.arena.ArenaObject;
import in.umlaut.arena.arenaobjects.ActionResult;
import in.umlaut.arena.arenaobjects.Actions;
import in.umlaut.player.Player;
import in.umlaut.views.KbInputHandler;

import java.util.*;

/**
 * Created by gbm on 26/09/15.
 */
public class Game implements KbInputHandler{
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
    private PlayerPos position = PlayerPos.ARENA;

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

    protected ArenaObject getSelectedObject() {
        return selectedObject;
    }

    protected List<ArenaObject> getFoundObjects() {
        return foundObjects;
    }

    protected boolean isDirectionSelected() {
        return isDirectionSelected;
    }

    protected ArenaLayout getLayoutSelected() {
        return layoutSelected;
    }

    private ArenaObject selectedObject;
    private List<ArenaObject> foundObjects = new ArrayList<>();
    private boolean isKeyFound;
    private boolean isExit;
    private boolean isDirectionSelected;
    private ArenaLayout layoutSelected;

    public Game(GameBuilder builder){
        this.id = builder.getId();
        this.player = builder.getPlayer();
        this.arena = builder.getArenaInstance();
        this.score = builder.getScore();
        this.selectedObject = builder.getSelectedObject();
        this.foundObjects = builder.getFoundObjects() == null ? new ArrayList<>() : builder.getFoundObjects();
        this.isDirectionSelected = builder.isDirectionSelected();
        this.layoutSelected = builder.getLayout();
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
                    if(position.equals(PlayerPos.ARENA)) {
                        arena.explore();
                        showHelpMessage();
                    }else{
                        showHelpMessageForObjectView();
                    }
                    break;
                case "F" :
                    displayScoreAndFoundItems();
                    break;
                case "Q" :
                    handleQuit();
                    break;
                case "R" :
                    resetSelections();
                    break;
                case "E" :
                    if(position.equals(PlayerPos.ARENA)){
                        handleDirectionSelection(token);
                    }else{
                        handleExploreWithinObject(token);
                    }
                    break;
                case "W" :
                case "S" :
                case "N" :
                    if(position.equals(PlayerPos.ARENA)){
                        handleDirectionSelection(token);    
                    }else{
                        System.out.println("Invalid input. You have already chosen an object " + selectedObject.getName());
                    }
                    break;
                default:
                    if(token.toUpperCase().startsWith("C")){
                        handleChooseWhenInArena(token);
                    }else {
                        //process action
                        ActionResult result = processAction(token.toUpperCase());
                        if(result != null) {
                            updateGameState(result);
                            resetSelections();
                        }
                    }
            }
            if(isKeyFound || isExit){
                break;
            }
            token = readInput(in);
        }

    }

    private ActionResult processAction(String token) {
        if(selectedObject == null){
            System.out.println("No item has been selected. Cannot do any action!");
            return null;
        }
        ActionResult result = null;
        String[] tokens = token.split(" ");
        Actions action = Actions.valueOf(tokens[0]);
        if(action == null){
            System.out.println("Invalid input. No action found.");
            return null;
        }
        if(tokens.length > 1){
            try{
                int id = Integer.parseInt(tokens[1]);
                Optional<ArenaObject> object = foundObjects.stream().filter(o -> o.getId() == id)
                        .findAny();
                if(object.isPresent()){
                    result = selectedObject.apply(action, object.get());
                    if(result != null){
                        //object is successfully used.. we can remove it from found objects
                        foundObjects.remove(object.get());
                    }
                } else{
                    System.out.println("No object has been discovered with the given id");
                    result = selectedObject.apply(action, Arrays.copyOfRange(tokens, 1, tokens.length));
                }
            }catch (NumberFormatException e){
                result = selectedObject.apply(action, Arrays.copyOfRange(tokens, 1, tokens.length-1));
            }
        } else {
            result = selectedObject.apply(action, new String[]{});
        }
        return result;
    }

    private void updateGameState(ActionResult result) {
        ArenaObject foundObject = result.getObject();
        score += result.getPoints();

        if(result.isFound()) foundObjects.add(selectedObject);
        if(result.isDone()) arena.removeObject(selectedObject.getId());
        if(result.isKeyFound()) handleFinalization();

        if(foundObject != null){
            arena.addObject(foundObject, layoutSelected);
            System.out.println("You found an object. Choose the same direction and explore to check what it is!");
        }
    }

    private void showHelpMessageForObjectView() {
        StringBuilder sb = new StringBuilder();
        sb.append("You have selected the object "+ selectedObject.getName() + " \n")
                .append("Press E to explore the actions that you can take on this object \n")
                .append("Press R to reset the selection");
        System.out.println(sb.toString());
    }

    private void handleChooseWhenInArena(String token) {
        if (!isDirectionSelected) {
            System.out.println("Please choose a direction first to explore");
        } else {
            chooseObjectInArena(token);
        }
    }

    private void handleExploreWithinObject(String token) {
        if(!token.toUpperCase().equalsIgnoreCase("E")){
            showHelpMessageForObjectView();
            return;
        }
        selectedObject.explore();
        selectedObject.exploreContainedObjects();
        selectedObject.exploreActions();
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

    private void showInitialMessage() {
        System.out.println(initialMessage);
    }

    private void showHelpMessage(){
        System.out.println(helpMessage);
    }

    private void displayFoundItems() {
        if(!foundObjects.isEmpty()) {
            System.out.println("Objects found till now : ");
            foundObjects.stream().forEach(o -> System.out.println("ID: " + o.getId() + " Name : " + o.getName()));
        } else {
            System.out.println("You have not found any object yet. Keep trying.");
        }
    }

    private void handleFinalization() {
        isKeyFound = true;
        System.out.println("Congratulations. You have found the key. You are now qualified to play the next level.");
        score = score + arena.getPointsForClearingThis();
        System.out.println("Your total score is : " + score);
        observers.stream().forEach(o -> o.handleGameUpdates(id, player, score, arena, true));
    }

    private void displayScoreAndFoundItems() {
        displayFoundItems();
        displayScore();
    }

    private void displayScore() {
        System.out.println("\nPresent score : " + score);
    }

    private void resetSelections() {
        isDirectionSelected = false;
        layoutSelected = null;
        System.out.println("Chosen direction is reset");
        if(selectedObject != null){
            selectedObject = null;
            System.out.println("Chosen object is reset");
        }
        position = PlayerPos.ARENA;
    }

    private void chooseObjectInArena(String token) {
        try {
            int itemId = Integer.parseInt(token.substring(1));
            if(selectedObject == null) {
                selectedObject = arena.chooseObject(itemId, layoutSelected);
                if(selectedObject == null){
                    return;
                }
            } else {
                selectedObject = selectedObject.chooseObject(itemId);
            }
            position = PlayerPos.ARENA_OBJECT;
            System.out.println("Requested item is selected. Press 'R' to reset. Press E to explore this item.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please try again");
        }
    }

    public void addObserver(GameObserver observer){
        this.observers.add(observer);
    }
}
