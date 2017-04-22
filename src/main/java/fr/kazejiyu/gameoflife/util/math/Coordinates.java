package fr.kazejiyu.gameoflife.util.math;

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

/**
 * A point located in the space whith an ordinate and an abscissa value.
 * 
 * @author Emmanuel Chebbi
 */
public interface Coordinates {
	
	/**
	 * @return the value on the abscissa axis.
	 */
    int x();

    /**
     * @return the value on the ordinate axis.
     */
    int y();

    /**
     * @return the addition of the coordinates and {@code rhs}
     */
    Coordinates add(Coordinates rhs);

    /**
     * @return a {@code Coordinate} object corresponding located at {@code x}, {@code y}.
     */
    static Coordinates of(int x, int y) {
        return new CartesianCoordinates(x, y);
    }
}
