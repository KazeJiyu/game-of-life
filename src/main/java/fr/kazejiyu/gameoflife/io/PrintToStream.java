package fr.kazejiyu.gameoflife.io;

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

import java.io.PrintStream;

import fr.kazejiyu.gameoflife.game.World;

/**
 * Prints the observed {@link World} to a given {@link PrintStream}.
 * 
 * @author Emmanuel Chebbi
 */
public class PrintToStream implements WorldObserver {
	
    private int currentGen = 0;

    private final PrintStream out;

    private final PrintStream err;

    /**
     * Creates a new {@code PrintToStream} that redirects to standard output and error streams.
     */
    public PrintToStream() {
        this(System.out);
    }

    /**
     * Creates a new {@code PrintToStream} that prints the {@code World} representation to
     * {@code out}, but prints error to the standard error stream.
     * 
     * @param out
     * 			The stream into which the world representations have to be printed
     */
    public PrintToStream(PrintStream out) {
        this(out, System.err);
    }

    /**
     * Creates a new {@code PrintToStream} that prints the {@code World} representation to
     * {@code out}, ands errors to {@code err}.
     * 
     * @param out
     * 			The stream into which the world representations have to be printed
     * 
     * @param err
     * 			The stream into which the errors have to be printed
     */
    public PrintToStream(PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
    }

    /**
     * Does nothing.
     */
    @Override
    public void onCompleted() {

    }

    /**
     * Prints the error message to the error stream.
     */
    @Override
    public void onError(Throwable e) {
        err.println("ERROR : " + e.getMessage());
    }

    /**
     * Prints {@code game} representation, preceded by the current generation number.
     */
    @Override
    public void onNext(World game) {
        out.println("Generation " + (++currentGen) + "");
        out.println(game);
        out.println();
    }
}
