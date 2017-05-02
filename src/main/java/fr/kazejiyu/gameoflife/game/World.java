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

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import fr.kazejiyu.gameoflife.util.math.Coordinates;
import rx.Observer;

/**
 * Conway's game of life consists in a succession of generations of a 
 * set of cells.
 * <br><br>
 * This interface defines such a generation that lives in a 2D world.
 * <br><br>
 * Since configuring an entiere world can prove to be laborious &mdash; due to
 * the numerous arguments &mdash;, the {@link Evolution} helper
 * class can ease code writting.
 * 
 * @author Emmanuel Chebbi
 */
public interface World {
	
    /**
     * Returns the number of columns within the world.
     * @return the number of columns within the world
     */
    int cols();

    /**
     * Returns the number of rows within the world.
     * @return the number of rows within the world
     */
    int rows();

    /**
     * Returns the number of cells that are alive in this generation.
     * @return the number of cells that are alive in this generation
     */
    int nbOfCellsAlive();

    /**
     * Returns whether the cell located at <code>(x,y)</code> is alive.
     * 
     * @param x
     * 			The col of the cell to look for.
     * @param y
     * 			The row of the cell to look for.
     * 
     * @return whether the cell located at <code>(x,y)</code> is alive
     */
    default boolean isAliveAt(int x, int y) {
    	return isAliveAt(Coordinates.of(x, y));
    }

    /**
     * Returns whether the cell located at <code>coord</code> is alive.
     * 
     * @param coord
     * 			The coordinates of the cell to look for.
     * 
     * @return whether the cell located at <code>coord</code> is alive
     */
    boolean isAliveAt(Coordinates coord);

    /**
     * Returns the next generation.
     * @return the next generation
     */
    World nextGeneration();

    /**
     * Returns a stream that contains the next generations.
     * @return a stream that contains the next generations
     */
    default Stream<World> nextGenerations() {
        return Stream.iterate(this, g -> g.nextGeneration());
    }

    /**
     * Returns the generation obtained after <code>nbrGenerations</code> iterations.
     * 
     * @param nbrGenerations
     * 			The number of generations to skip.
     * 
     * @return the generation resulting of an evolution of <code>nbrGenerations</code>
     */
    default World iterate(int nbrGenerations) {
        return iterate(nbrGenerations, cells -> {});
    }

    /**
     * Returns the generation obtained after <code>nbrGenerations</code> iterations.
     * <br><br>
     * Calls <code>consumer</code> on each generation wandered.
     * 
     * @param nbrGenerations
     * 			The number of generations to skip.
     * @param consumer
     * 			Will be called on each generation.
     * 
     * @return the generation resulting of an evolution of <code>nbrGenerations</code>
     */
    default World iterate(int nbrGenerations, Consumer<World> consumer) {
        return nextGenerations()
                    .limit(nbrGenerations)
                    .peek(consumer::accept)
                    .skip(nbrGenerations - 1)
                    .findAny().get();
    }

    /**
     * Returns the generation obtained after <code>nbrGenerations</code> iterations.
     * <br><br>
     * Calls the observers' <code>onNext()</code> method for each generation.
     * 
     * @param nbrGenerations
     * 			The number of generations to skip.
     * @param observers
     * 			Will be notified of each generation.
     * 
     * @return the generation resulting of an evolution of <code>nbrGenerations</code>
     */
    @SuppressWarnings("unchecked")
    default World iterate(int nbrGenerations, Observer<World>... observers) {
        return iterate(nbrGenerations, g -> false, observers);
    }

    /**
     * Returns the generation obtained after <code>nbrGenerations</code> iterations.
     * <br><br>
     * Calls the observers' <code>onNext()</code> method for each generation.
     * <br><br>
     * If the condition <code>stop</code> is satisfied before having generate 
     * <code>nbrGenerations</code> generations, the evolution is stoped.
     * 
     * @param nbrGenerations
     * 			The number of generations to skip.
     * @param stop
     * 			Indicates whether the evolution has to be stoped.
     * @param observers
     * 			Will be notified of each generation.
     * 
     * @return the generation resulting of an evolution of <code>nbrGenerations</code>
     */
    @SuppressWarnings("unchecked")
    World iterate(int nbrGenerations, Predicate<World> stop, Observer<World>... observers);
}
