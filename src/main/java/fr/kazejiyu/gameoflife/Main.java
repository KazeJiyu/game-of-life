package fr.kazejiyu.gameoflife;

import fr.kazejiyu.gameoflife.game.GameOfLifeConfiguration;
import fr.kazejiyu.gameoflife.game.conditions.WhenEmpty;
import fr.kazejiyu.gameoflife.game.conditions.WhenRepeated;
import fr.kazejiyu.gameoflife.game.conditions.WhenStable;
import fr.kazejiyu.gameoflife.game.patterns.Pattern;
import fr.kazejiyu.gameoflife.game.rules.Rule;
import fr.kazejiyu.gameoflife.io.PrintToStream;
import fr.kazejiyu.gameoflife.io.textual.HTMLRepresentation;
import fr.kazejiyu.gameoflife.io.textual.WrittenOnCompleted;
import fr.kazejiyu.gameoflife.io.textual.XMLRepresentation;

/**
 * An example main that plays with a game of life.
 */
public class Main {
    
    private static final int WIDTH = 6;
    private static final int HEIGHT = 6;

    public static void main(String[] args) {
        
        new GameOfLifeConfiguration().size(WIDTH, HEIGHT)
                // There are several variants of Conway's game of life.
                // this method makes able to select the one to use
                .followRule(Rule.GAME_OF_LIFE)
                // Populate the world with a pattern located at its center
                .initContent(Pattern.GLIDER.transformToCenter(WIDTH, HEIGHT))
                // Print each generation in the console
                .forEach(new PrintToStream())
                // Write each generation in an XML file
                .forEach(new WrittenOnCompleted("test.xml", new XMLRepresentation()))
                // Write each generation in an HTML file
                .forEach(new WrittenOnCompleted("test.html", new HTMLRepresentation()))
                // Stop the evolution if :
                // - a sequence is repeated
                // - the world becomes stable
                // - the world becomes empty
                .stop(new WhenRepeated().or(new WhenStable()).or(new WhenEmpty()))
                // Generate 100 generations
                .evolve(100);
    }
}
