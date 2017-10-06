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

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import fr.kazejiyu.gameoflife.game.patterns.Pattern;
import fr.kazejiyu.gameoflife.game.rules.Rule;
import fr.kazejiyu.gameoflife.util.math.Coordinates;

/**
 * An immutable implementation of {@link Generation}.
 * 
 * @author Emmanuel Chebbi
 */
public final class ImmutableGeneration implements Generation {
    /**
     * The cells of the generation that are alive.
     */
    private final Set<Coordinates> aliveCells;

    /**
     * The width of the world.
     */
    private final int width;

    /**
     * The height of the world.
     */
    private final int height;

    /**
     * Helps to determine whether a cell will be alive at next generation.
     */
    private final BiPredicate<Generation, Entry<Coordinates, Long>> isCellAlive;

    /**
     * A set containing the coordinates from (-1,-1) to (1,1) except (0,0).
     * By adding each coordinate of this set to a cell, we can get the coordinates
     * of the cells' neighbours.
     * 
     * @see #neighbours(Stream)
     */
    private static final Set<Coordinates> NEIGHBOUR_DELTAS = neighbourDeltas();

    /**
     * Creates a new generation from given pattern.
     * <br>
     * The width and the height of the world are initialized to pattern's ones.
     * 
     * @param pattern
     * 			The pattern to initialize the world with.
     */
    public ImmutableGeneration(Pattern pattern) {
        this(pattern.cells, pattern.width, pattern.height);
    }

    /**
     * Creates a new generation from given pattern.
     * 
     * @param pattern
     * 			The pattern to initialize the world with.
     * @param width
     * 			The width of the world.
     * @param height
     * 			The height of the world.
     */
    public ImmutableGeneration(Pattern pattern, int width, int height) {
        this(pattern.cells, width, height);
    }

    /**
     * Creates a new generation creating living cells for each coordinate
     * of <code>aliveCells</code>.
     * <br><br>
     * The rule followed is {@link Rule#GAME_OF_LIFE}.
     * 
     * @param aliveCells
     * 			The coordinates of the cells that live.
     * @param width
     * 			The width of the world.
     * @param height
     * 			The height of the world.
     */
    public ImmutableGeneration(Collection<Coordinates> aliveCells, int width, int height) {
        this(aliveCells, width, height, Rule.GAME_OF_LIFE);
    }

    /**
     * Creates a new generation creating living cells for each coordinate
     * of <code>aliveCells</code>.
     * 
     * @param aliveCells
     * 			The coordinates of the cells that live.
     * @param width
     * 			The width of the world.
     * @param height
     * 			The height of the world.
     * @param isCellAlive
     * 			The rule that determines whether a cell will be alive at next generation.
     */
    public ImmutableGeneration(Collection<Coordinates> aliveCells, int width, int height, BiPredicate<Generation, Entry<Coordinates, Long>> isCellAlive) {
        this.width = width;
        this.height = height;
        this.isCellAlive = Objects.requireNonNull(isCellAlive);
        this.aliveCells = Collections.unmodifiableSet(new HashSet<>(aliveCells));
    }

    @Override
    public int rows() {
        return height;
    }

    @Override
    public int cols() {
        return width;
    }

    @Override
    public int nbOfCellsAlive() {
        return aliveCells.size();
    }

    @Override
    public boolean isAliveAt(Coordinates coord) {
        return aliveCells.contains(coord);
    }

    /**
     * @return an unmodifiable set that contains coordinates from (-1,-1) to (1,1) except (0,0).
     * @see #NEIGHBOUR_DELTAS
     * @see #neighbours(Stream)
     */
    private static Set<Coordinates> neighbourDeltas() {
        Set<Coordinates> deltas = new HashSet<>();

        for (int x = -1; x <= 1; ++x)
            for (int y = -1; y <= 1; ++y)
                if (x != 0 || y != 0)
                    deltas.add(Coordinates.of(x, y));

        return Collections.unmodifiableSet(deltas);
    }

    /**
     * @return A stream containing all the neighbours of each given cell.
     *		   If <code>cells</code> contains coordinates that are next to
     *		   each other then the resulting stream will contain duplicates.   
     */
    private Stream<Coordinates> neighbours(Stream<Coordinates> cells) {
        return cells.flatMap(cell -> NEIGHBOUR_DELTAS.stream().map(cell::add))
                    .filter(this::isCellWithinWorld);
    }

    private boolean isCellWithinWorld(Coordinates cell) {
        return (0 <= cell.x()) && (cell.x() < width) 
            && (0 <= cell.y()) && (cell.y() < height);
    }

    @Override
    public ImmutableGeneration nextGeneration() {
        Set<Coordinates> next = neighbours(aliveCells.stream())
                // map each coord to its number of occurence
                .collect(groupingBy(p -> p, counting()))
                .entrySet().stream()
                // keep alive cells
                .filter(e -> isCellAlive.test(this, e))
                .map(Entry::getKey)
                .collect(toSet());

        return new ImmutableGeneration(next, width, height, isCellAlive);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (isAliveAt(x, y))
                    sb.append("o");
                else
                    sb.append(".");
            }
            if (y != height - 1)
                sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((aliveCells == null) ? 0 : aliveCells.hashCode());
        result = prime * result + height;
        result = prime * result + ((isCellAlive == null) ? 0 : isCellAlive.hashCode());
        result = prime * result + width;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof ImmutableGeneration))
            return false;

        ImmutableGeneration other = (ImmutableGeneration) obj;
        if (height != other.height || width != other.width)
            return false;

        if (aliveCells == null) {
            if (other.aliveCells != null)
                return false;
        } else if (!aliveCells.equals(other.aliveCells))
            return false;

        if (isCellAlive == null) {
            if (other.isCellAlive != null)
                return false;
        } else if (!isCellAlive.equals(other.isCellAlive))
            return false;

        return true;
    }
}
