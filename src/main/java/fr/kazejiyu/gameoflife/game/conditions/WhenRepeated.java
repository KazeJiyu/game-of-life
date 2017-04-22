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

import java.util.HashSet;
import java.util.Set;

import fr.kazejiyu.gameoflife.game.GameOfLifeConfiguration;
import fr.kazejiyu.gameoflife.game.World;


/**
 * Aimed to end a game's evolution if the current generation has already been encountered.
 * 
 * This classe's name has been chosen in order to fit well with 
 * {@link GameOfLifeConfiguration#stop(java.util.function.Predicate)}'s syntax.
 * 
 * @author Emmanuel Chebbi
 */
public class WhenRepeated implements Condition {
	
    private static Set <World> evolution = new HashSet<>();

    /**
     * @return whether an equivalent {@code World} has already been processed
     */
    @Override
    public boolean test(World world) {
        return ! evolution.add(world);
    }
}
