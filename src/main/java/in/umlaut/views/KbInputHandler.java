package in.umlaut.views;

import java.util.Scanner;

/**
 * Created by gbm on 27/09/15.
 */
public interface KbInputHandler {
    void handleKeyStroke();

    default String readInput(Scanner in){
        String input;
        while(true){
            input = in.nextLine();
            if(input.isEmpty())
                continue;
            else break;
        }
        return input;
    }
}
