package fr.kazejiyu.gameoflife.game;

import java.util.stream.Stream;

import fr.kazejiyu.gameoflife.util.math.Coordinates;

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
public interface Generation {
	
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
    Generation nextGeneration();

    /**
     * Returns a stream that contains the next generations.
     * @return a stream that contains the next generations
     */
    default Stream<Generation> nextGenerations() {
        return Stream.iterate(this, Generation::nextGeneration);
    }
}
