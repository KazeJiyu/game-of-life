package fr.kazejiyu.gameoflife.game.conditions;

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

import fr.kazejiyu.gameoflife.game.Evolution;
import fr.kazejiyu.gameoflife.game.Generation;

/**
 * Aimed to end a game's evolution if the current generation is the same than the previous one.
 * 
 * This classe's name has been chosen in order to fit well with 
 * {@link Evolution#stop(java.util.function.Predicate)}'s syntax.
 * 
 * @author Emmanuel Chebbi
 */
public class WhenStable implements Condition {

	private static Generation last = null;

	/**
	 * @return whether {@code world} is equivalent to the one given
	 * 		   the last time this method has been called
	 */
    @Override
    public boolean test(Generation world) {
        boolean equals = world.equals(last);
        last = world;

        return equals;
    }
}
