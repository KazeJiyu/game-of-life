package fr.kazejiyu.gameoflife.game;

/*
 * MIT License
 * 
 * Copyright (c) 2017 Emmanuel CHEBBI
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import static java.lang.Math.max;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import fr.kazejiyu.gameoflife.game.conditions.Condition;
import fr.kazejiyu.gameoflife.game.patterns.Pattern;
import fr.kazejiyu.gameoflife.game.rules.Rule;
import fr.kazejiyu.gameoflife.io.WorldObserver;
import fr.kazejiyu.gameoflife.util.math.Coordinates;
import rx.Observer;

/**
 * Makes easier to create and then evolve a {@link World}.
 * 
 * This utility class uses the Builder design pattern.
 * 
 * @author Emmanuel Chebbi
 */
public class GameOfLifeConfiguration {
	
    /**
     * The width of the world to generate
     */
    private int width = 5;

    /**
     * The height of the world to generate
     */
    private int height = 5;

    /**
     * Whether the user has explictly defined the width.
     * Used in {@link #initContent(Pattern)} in order to define
     * whether the size of the world should be set to pattern's one.
     */
    private boolean widthHasBeenDefined = false;

    /**
     * Whether the user has explictly defined the height.
     * Used in {@link #initContent(Pattern)} in order to define
     * whether the size of the world should be set to pattern's one.
     */
    private boolean heightHasBeenDefined = false;

    /**
     * The cells used to initially populate the world.
     */
    private Collection<Coordinates> cells = Collections.emptySet();

    /**
     * The rule that defines whether a cell is alive at world's next generation.
     * @see Rule
     */
    private BiPredicate<World, Entry<Coordinates, Long>> rule = Rule.GAME_OF_LIFE;

    /**
     * Indicates if the evolution of the world has to be stopped prematurely.
     * @see Condition
     */
    private Predicate<World> stop = g -> false;

    /**
     * Observers that will be notified with each generation of the world's evolution.
     * Should be used in order to process the evolution and then create a result.
     * @see WorldObserver
     */
    private Collection<Observer<World>> observers = new ArrayList<>();

    /**
     * Sets the width of the world.
     * 
     * @param width
     * 			The new width of the world.
     * 
     * @return the current instance. May be used in order to chain method calls
     * 
     * @see #height(int)
     * @see #size(int, int)
     */
    public GameOfLifeConfiguration width(int width) {
        this.width = width;
        this.widthHasBeenDefined = true;
        return this;
    }

    /**
     * Sets the height of the world.
     * 
     * @param height
     * 			The new height of the world.
     * 
     * @return the current instance. May be used in order to chain method calls
     * 
     * @see #width(int)
     * @see #size(int, int)
     */
    public GameOfLifeConfiguration height(int height) {
        this.height = height;
        this.heightHasBeenDefined = true;
        return this;
    }

    /**
     * Sets the size of the world.
     * 
     * @param width
     * 			The new width of the world.
     * @param height
     * 			The new height of the world.
     * 
     * @return the current instance. May be used in order to chain method calls
     * 
     * @see #width(int)
     * @see #height(int)
     */
    public GameOfLifeConfiguration size(int width, int height) {
        width(width);
        height(height);
        return this;
    }

    /**
     * Sets the rule that will be followed by the world.
     * <br><br>
     * The rule defines whether a cell will be alive at next generation.
     * <br><br>
     * Hence, given predicate will be called on each cell of the world.
     * Its parameters are, in order :
     * <ul> 
     * 	<li>the world that contains the cell,</li>
     * 	<li>the coordinates of the cell,</li>
     * 	<li>the number of neighbouring cells that are alive.</li>
     * </ul>
     * 
     * @param rule
     * 			Defines whether a cell will be alive at next generation.
     * 
     * @return the current instance. May be used in order to chain method calls
     */
    public GameOfLifeConfiguration followRule(BiPredicate<World, Entry<Coordinates, Long>> rule) {
        this.rule = rule;
        return this;
    }

    /**
     * Initialize the world with <code>cells</code>
     * 
     * @param cells
     * 			The cells used to populate the world.
     * 
     * @return the current instance. May be used in order to chain method calls
     */
    public GameOfLifeConfiguration initContent(Collection<Coordinates> cells) {
        this.cells = cells;
        return this;
    }

    /**
     * Initialize the world with given <code>pattern</code>
     * 
     * @param pattern
     * 			Defines the cells used to populate the world.
     * 
     * @return the current instance. May be used in order to chain method calls
     */
    public GameOfLifeConfiguration initContent(Pattern pattern) {
        cells = pattern.cells;

        width = widthHasBeenDefined ? max(width, pattern.width) : pattern.width;
        height = heightHasBeenDefined ? max(height, pattern.height) : pattern.height;

        return this;
    }

    @SafeVarargs
    public final GameOfLifeConfiguration forEach(Observer<World>... obs) {
        for (Observer<World> o : obs)
            observers.add(o);

        return this;
    }

    /**
     * Defines a condition that, when satisfied, stops the evolution of the game.
     * <br><br>
     * Given predicate will successively be called on each generation of the game.
     * 
     * @param predicate
     * 			When <code>true</code>, the evolution of the world ends. 
     * 
     * @return the current instance. May be used in order to chain method calls
     */
    public GameOfLifeConfiguration stop(Predicate<World> predicate) {
        this.stop = predicate;
        return this;
    }

    /**
     * Creates a world and makes it evolve for <code>generations</code>.
     *  
     * @param generations
     * 			The number of generations to generate.
     */
    @SuppressWarnings("unchecked")
    public World evolve(int generations) {
        return new ImmutableWorld(cells, width, height, rule)
        		.iterate(generations, 
        				stop,
        				observers.toArray(new Observer[observers.size()]));
    }

    /**
     * Creates a world and makes it evolve until the specified condition is reached.
     * 
     * @param until
     * 			Specifies when the evolution has to stop
     */
    public void evolveUntil(Condition until) {
        stop = until.or(stop);
        evolve(Integer.MAX_VALUE);
    }
}
