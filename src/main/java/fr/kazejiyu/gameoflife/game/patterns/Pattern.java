package fr.kazejiyu.gameoflife.game.patterns;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import fr.kazejiyu.gameoflife.game.Generation;
import fr.kazejiyu.gameoflife.game.rules.Rule;
import fr.kazejiyu.gameoflife.util.math.CartesianCoordinates;
import fr.kazejiyu.gameoflife.util.math.Coordinates;

/**
 * Represents a certain configuration of cells that can be used in order
 * to populate a {@link Generation}. 
 * <br><br>
 * Instances of this class are <b>immutables</b> and, once created, are
 * unconditionnaly thread-safe.
 * 
 * @author Emmanuel Chebbi
 */
public final class Pattern {

    /**
     * The minimum number of columns required by the pattern.
     */
    public final int width;

    /**
     * The minimum number of rows required by the pattern.
     */
    public final int height;

    /**
     * The cells that compose the pattern.
     * Since this set is unmodofiable, trying to call one of its mutable method
     * will throw an exception.
     */
    public final Set<Coordinates> cells;

    /**
     * Creates a new <code>Pattern</code> initiated with given cells.
     * 
     * @param width
     *          The number of columns required by the pattern.
     * @param height
     *          The number of rows required by the pattern.
     * @param cells
     *          The cells that compose the pattern.
     */
    private Pattern(int width, int height, CartesianCoordinates... cells) {
    	this(width, height, Arrays.asList(cells));
    }

    /**
     * Creates a new <code>Pattern</code> initiated with given cells.
     * 
     * @param width
     *          The number of columns required by the pattern.
     * @param height
     *          The number of rows required by the pattern.
     * @param cells
     *          The cells that compose the pattern.
     */
    private Pattern(int width, int height, Collection <Coordinates> cells) {
        this.width = width;
        this.height = height;
        this.cells = Collections.unmodifiableSet(new HashSet<Coordinates>(cells));
    }
    
    public static Pattern fromString(String representation, char cell) {
    	int row = 0; 
		int width = 0;
    	Set <Coordinates> cells = new HashSet<>();
    	
    	for(String line : representation.split("\\r?\\n")) {
    		for(int i = 0 ; i < line.length() ; i++)
    			if( line.charAt(i) == cell )
    				cells.add(new CartesianCoordinates(i, row));
			
			++row;
			width = Math.max(width, line.length());
    	}
    	
    	return new Pattern(width, row, cells);
    }
    
    public static Pattern fromFile(String path) throws IOException {
    	return Pattern.fromFile(path, StandardCharsets.UTF_8);
    }
    
    public static Pattern fromFile(String path, Charset encoding) throws IOException {
    	byte[] encoded = Files.readAllBytes(Paths.get(path));
    	return Pattern.fromString(new String(encoded, encoding), '.');
    }
    
    /**
     * The smallest and most common oscillator.
     * <br><br>
     * This pattern is made of three cells that oscillate between a vertical
     * and an horizontal line.
     */
    public static final Pattern BLINKER = new Pattern(
                3, 3, 
                new CartesianCoordinates(0, 1), new CartesianCoordinates(1, 1), new CartesianCoordinates(2, 1)
    ); 
    
    /**
     * A simple oscillator.
     */
    public static final Pattern TOAD = new Pattern(
                4, 4, 
                new CartesianCoordinates(1, 1), new CartesianCoordinates(2, 1), new CartesianCoordinates(3, 1),
                new CartesianCoordinates(0, 2), new CartesianCoordinates(1, 2), new CartesianCoordinates(2, 2)
    );
    
    /**
     * The smallest spaceship. It's a pattern that travels accross the world. 
     */
    public static final Pattern GLIDER = new Pattern(
                5, 4, 
                new CartesianCoordinates(3, 0), new CartesianCoordinates(2, 1), new CartesianCoordinates(2, 2),
                new CartesianCoordinates(3, 2), new CartesianCoordinates(4, 2)
    );
    
    /**
     * The replicator in {@link Rule#HIGH_LIFE}.
     */
    public static final Pattern HIGH_LIFE_REPLICATOR = new Pattern(
                6, 6,
                new CartesianCoordinates(3, 1), new CartesianCoordinates(4, 1),
                new CartesianCoordinates(5, 1), new CartesianCoordinates(2, 2),
                new CartesianCoordinates(5, 2), new CartesianCoordinates(1, 3),
                new CartesianCoordinates(5, 3), new CartesianCoordinates(1, 4),
                new CartesianCoordinates(4, 4), new CartesianCoordinates(1, 5),
                new CartesianCoordinates(2, 5), new CartesianCoordinates(3, 5)
    );
    
    /**
     * Applies <code>transformation</code> to each cell of this pattern and creates
     * a new <code>Pattern</code> from the cells returned.
     * 
     * @param transformation
     *          The function that maps the cells to the new ones.
     *          
     * @return a new Pattern created from mapping the cells of this pattern.
     */
    public Pattern transform(Function<Coordinates, Coordinates> transformation) {
        return new Pattern(
                width, height,
                cells.stream()
                    .map(transformation::apply)
                    .collect(Collectors.toSet())
        );
    }

    /**
     * Returns a new <code>Pattern</code> which cells are equal to this one,
     * except that their coordinates have been changed in order to make
     * <code>origin</code> as their origin.
     * 
     * @param origin
     *          The new origin of the pattern.          
     *  
     * @return the cells of the pattern having their origin at <code>origin</code>
     */
    public Pattern transformToOrigin(Coordinates origin) {
        return transform(cell -> cell.add(origin));
    }

    /**
     * Returns a new <code>Pattern</code> which cells are equal to this one,
     * except that their coordinates have been changed in order to make
     * <code>(x,y)</code> as their origin.
     * 
     * @param x
     * 			The column of the new origin.			
     * @param y
     * 			The row of the new origin.
     * 	
     * @return the cells of the pattern having their origin at (x,y)
     */
    public Pattern transformToOrigin(int x, int y) {
        return transformToOrigin(Coordinates.of(x, y));
    }

    /**
     * Returns a new <code>Pattern</code> equal to this one, except that
     * the coordinates of the cells are changed in order to locate it
     * at the center of a {@link Generation} which size equals the specified one.
     * 
     * @param width
     * 			The width of the world.
     * @param height
     * 			The height of the world.
     * 
     * @return the coordinates of the cells of the pattern to locate it at the center of a world.
     */
    public Pattern transformToCenter(int width, int height) {
        return transformToOrigin(width/2 - this.width/2, height/2 - this.height/2);
    }

    /**
     * Returns a new <code>Pattern</code> equal to this one, except that
     * the coordinates of the cells are changed in order to locate it
     * on the up right corner of a {@link Generation} which size equals the specified one.
     * 
     * @param width
     * 			The width of the world.
     * @return the coordinates of the cells of the pattern to locate it on the upper right corner of a world.
     */
    public Pattern transformToUpRight(int width) {
        return transformToOrigin(width - this.width, 0);
    }

    @Override
    public int hashCode() {
        return cells.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Pattern))
            return false;
        
        Pattern other = (Pattern) obj;
        return cells.equals(other.cells);
    }
}
