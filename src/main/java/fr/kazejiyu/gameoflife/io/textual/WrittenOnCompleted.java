package fr.kazejiyu.gameoflife.io.textual;

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

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import fr.kazejiyu.gameoflife.game.World;

/**
 * A decorator that provides automatic save when the decored
 * <code>TextualRepresentation</code>'s {@link #onCompleted()} method is called.
 * 
 * @author <a href="mailto:emmanuel.chebbi@outlook.fr">Emmanuel Chebbi</a>
 */
public final class WrittenOnCompleted extends TextualRepresentation {
	
    /**
     * The file in which the TextualRepresentation will be written.
     */
    private final File destFile;

    /**
     * The representation to be written once completed
     */
    private final TextualRepresentation toBeSaved;

    /**
     * @param path
     * 			The path toward the file in which the representation has to be written.
     * @param toBeSaved
     * 			The representation that will be saved once completed.
     */
    public WrittenOnCompleted(String path, TextualRepresentation toBeSaved) {
        this(new File(path), toBeSaved);
    }

    /**
     * @param destFile
     * 			The file in which the representation has to be written.
     * @param toBeSaved
     * 			The representation that will be saved once completed.
     */
    public WrittenOnCompleted(File destFile, TextualRepresentation toBeSaved) {
        this.destFile = Objects.requireNonNull(destFile);
        this.toBeSaved = Objects.requireNonNull(toBeSaved);
    }

    @Override
    protected String textToWrite() {
        return toBeSaved.textToWrite();
    }

    /**
     * Calls the decorated instance's {@code onCompleted} method,
     * then tries to save it.
     */
    @Override
    public void onCompleted() {
        toBeSaved.onCompleted();

        try {
            write(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable e) {
        toBeSaved.onError(e);
    }

    @Override
    public void onNext(World t) {
        toBeSaved.onNext(t);
    }
}
