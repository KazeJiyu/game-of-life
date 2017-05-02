package fr.kazejiyu.gameoflife.game.rules;

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

import java.util.Map.Entry;
import java.util.function.BiPredicate;

import fr.kazejiyu.gameoflife.game.Generation;
import fr.kazejiyu.gameoflife.util.math.Coordinates;

/**
 * The rules that a {@link Generation} can follow in order to determine whether
 * a cell will be alive at the next generation.
 * 
 * @author <a href="mailto:emmanuel.chebbi@outlook.fr">Emmanuel Chebbi</a>
 */
public enum Rule implements BiPredicate<Generation, Entry<Coordinates, Long>> {
    
    /**
     * Basic rule of Conway's game of life.
     * <dl>
     *  <dt>Conditions for life</dt>
     *      <dd>A cell borns if it has 3 neighbouring cells that are alive.</dd>
     *  <dt>Unchange state</dt>
     *      <dd>If a cell has 2 neighbouring cells that are alive, its state remains the same.</dd>
     * </dl>
     */
    GAME_OF_LIFE() {
        @Override
        public boolean test(Generation world, Entry<Coordinates, Long> e) {
            int nbrNeighbours = e.getValue().intValue();
            Coordinates coordinates = e.getKey();

            return nbrNeighbours == 3 || (nbrNeighbours == 2 && world.isAliveAt(coordinates));
        }
    },

    /**
     * A rule very similar to the {@link #GAME_OF_LIFE}.
     * <dl>
     *  <dt>Conditions for life</dt>
     *      <dd>A cell borns if it has 3 or 6 neighbouring cells that are alive.</dd>
     *  <dt>Unchange state</dt>
     *      <dd>If a cell has 2 neighbouring cells that are alive, its state remains the same.</dd>
     * </dl>
     */
    HIGH_LIFE() {
        @Override
        public boolean test(Generation world, Entry<Coordinates, Long> e) {
            int nbrNeighbours = e.getValue().intValue();
            Coordinates coordinates = e.getKey();

            if (world.isAliveAt(coordinates))
                return nbrNeighbours == 2 || nbrNeighbours == 3;

            return nbrNeighbours == 3 || nbrNeighbours == 6;
        }
    },

    /**
     * A rule that defines symmetric dead and alive states.
     * <dl>
     *  <dt>Conditions for life</dt>
     *      <dd>A cell borns if it has 3, 6, 7 or 8 neighbouring cells that are alive.</dd>
     *  <dt>Unchange state</dt>
     *      <dd>If a cell has 4 neighbouring cells that are alive, its state remains the same.</dd>
     * </dl>
     */
    DAY_AND_NIGHT() {
        @Override
        public boolean test(Generation world, Entry<Coordinates, Long> e) {
            int nbrNeighbours = e.getValue().intValue();
            Coordinates coordinates = e.getKey();

            if (world.isAliveAt(coordinates))
                return nbrNeighbours == 3 || nbrNeighbours == 4 || nbrNeighbours == 6 || nbrNeighbours == 7
                        || nbrNeighbours == 8;

            return nbrNeighbours == 3 || nbrNeighbours == 6 || nbrNeighbours == 7 || nbrNeighbours == 8;
        }
    },

    /**
     * A rule that favour the birth of new cells within big figures.
     * <dl>
     *  <dt>Conditions for life</dt>
     *      <dd>A cell borns if it has 3 or 4 neighbouring cells that are alive.</dd>
     * </dl>
     */
    LIFE_3_4() {
        @Override
        public boolean test(Generation world, Entry<Coordinates, Long> e) {
            int nbrNeighbours = e.getValue().intValue();

            return nbrNeighbours == 3 || nbrNeighbours == 4;
        }
    },

    /**
     * Another rule.
     * <dl>
     *  <dt>Conditions for life</dt>
     *      <dd>A cell borns if it was dead and has 2 neighbouring cells that are alive.</dd>
     * </dl>
     */
    SEEDS() {
        @Override
        public boolean test(Generation world, Entry<Coordinates, Long> e) {
            int nbrNeighbours = e.getValue().intValue();
            Coordinates coordinates = e.getKey();

            if (world.isAliveAt(coordinates))
                return false;

            return nbrNeighbours == 2;
        }
    };
}
