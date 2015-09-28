package in.umlaut.arena;

/**
 * Created by gbm on 27/09/15.
 */
public enum ArenaLayout {
    EAST("E"),SOUTH("S"),WEST("W"),NORTH("N");

    private String shortForm;

    ArenaLayout(String e) {
        this.shortForm = e;
    }

    public static ArenaLayout fromInitial(String initial){
        for(ArenaLayout layout : ArenaLayout.values()){
            if(layout.shortForm.toUpperCase().startsWith(initial.toUpperCase().charAt(0) + "")){
                return layout;
            }
        }
        return null;
    }
}
