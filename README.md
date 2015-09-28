#Hoskote

Not really sure why this name was chosen. But that's the way it is.

Its a text based 'escape-out-of-here' game, inspired from escape games (e.g., Can you escape) on Android.

The scenario is the player is locked inside a room, with certain objects, and he has to explore the given objects
and find a key to the door.
 
##Usage

The program starts with the start screen.

1. To start a game, you first need to select a player.

An existing player can be selected, or a new one can be created. The player data to be stored is
hardcoded to be at **"/tmp/players.csv"**.

So if you want to run it on windows, you would need to make some changes.

2. After selection of a player, you must choose an Arena to play.
Right now we have only one arena, thats the simplest arena.

The arena is generated through code. Currently there is no support to plug arenas through CSV files or otherwise.
To add a new arena, we would need to add some code, although not very huge.

3. You can also choose to resume an already saved game. The saved games are stored in **"/tmp/saved_games.csv"** and
is hardcoded.

4. After selection of a Player and an Arena/Saved Game, you can proceed to play.

5. You can press 'H' on any screen to see the possible key strokes.
Your aim is to find the key to the door.

 *  Select direction to explore ('S'/'W'/'N'/'E' for South, West, North and East respectively).
 *  Explore the region selected by pressing 'E'. This will show up what objects are present in that direction.
 *  To select any other direction first Reset the selected direction by pressing R.
 *  To choose an object, press C[id]. e.g., C1 to choose object 1, C2 to choose object 2 and so on.
 *  Available ids will be shown when you 'explore' the chosen direction.
 *  To take any action on an object you need to first choose that object.
 *  After the object is chosen, you can explore that object using 'E' or you can take any action like (M)ove, (P)ick,
  (U)se etc.
 *  To choose any other object, (R)eset first.
 *  To use an object on another object, first choose the object on which action is to be taken. i.e.,
     To smash a box by a hammer:
     *   I must first pick Hammer. This will be stored as found objects for later use.
     *   I must choose the box on which this action is to be taken.
     *   I should press U after choosing the box.
     *   I can press F to see my found objects. (which shows id of hammer as 1 for example).
     *   I should press 1 to use hammer on the box.
 *  Whenever you find an object, your score increases.
 *  You can see your score and found objects by using F
 *  To quit the game before you finish, you can use Q.
 *  You can either choose to save the game or leave it.
     
##Design
  **Screens**
  
  All Screens implement an interface KbInputHandler, that shows that the concerned objects handle key stroke.
  The interface has a default method to read input from user, and hence code duplication is reduced.
  
  The starting screen is StartScreen, and we have additionally PlayerManagementScreen, ArenaManagementScreen
  and ResumableGameScreen.
  
  It is not possible now to navigate from one screen to another directly, and navigation is possible only through 
  start screen.
  
  The class 'Game' also implements the interface KbInputHandler as it does the state change of the Game based on 
  user input.
  
  **Builders**
  
  Builder patterns are used to build Arenas and Games. This is needed to make various types Arenas or Games construct dynamically.
  GameBuilder is also needed for reconstruction of the Game from a saved state.
  
  **Arena**
  
  Arena is the play area. The room where you are locked. Arena layout is divided into four regions based on directions.
  Objects can be placed in any of the locations.
   
  All objects that can be placed inside an Arena implement ArenaObject interface. This interface ensures that objects
  have their behavior defined, like pickable, movable etc.
  
  All ArenaObjects are associated with an id. This is to ensure correct reconstruction of the game from a saved state, as
  well as to track found objects and remove objects.
  
  **IdGenerators**
  
  Game, Arena and ArenaObjects have ids assigned to them. These ids are generated through singleton IdGenerator objects.
  
  **Save**
  
  Player data and Game data are saved in the form of CSV files. A utility class is used to read/write from/to the
  csv file. CSV file is created if it is not existing.
  
 **Patterns Used**
  
  * Singletons for PlayerManager, GameSaver, IdGenerators
  * Builders for ArenaBuilding, GameBuilding
  * Observers for score updation or game status saving
  
##Improvements
 
 * Screen Pool can be designed so that it becomes possible to navigate to any screen from any other screen
 * Removing hardcoding and reading the file locations from a property file
 * CLI/CSV based Arena Creation
 * Refactoring Game so that input reading from KB and state machine of Game are separated
 
##TODOs

 * Write test cases :(



