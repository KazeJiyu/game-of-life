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
import java.io.FileWriter;
import java.io.IOException;

import fr.kazejiyu.gameoflife.game.Generation;
import fr.kazejiyu.gameoflife.io.WorldObserver;

/**
 * A textual representation of one or several {@link Generation}s.
 * <br><br>
 * Such a representation can be written to the disk.
 * 
 * @author Emmanuel Chebbi
 */
public abstract class TextualRepresentation implements WorldObserver {
	
    /**
     * @return the textual representation that can be written
     */
    abstract protected String textToWrite();

    /**
     * Writes the representation into the file located at <code>filePath</code>.
     * 
     * @param filePath
     * 			The path toward the file in which the representation has to be written.
     * 
     * @throws IOException if an error occurs while writting
     */
    public void write(String filePath) throws IOException {
        write(new File(filePath));
    }

    /**
     * Writes the representation into the file located at <code>filePath</code>.
     * 
     * @param target
     * 			The file in which the representation has to be written.
     * 
     * @throws IOException if an error occurs while writting
     */
    public void write(File target) throws IOException {
        try (FileWriter writer = new FileWriter(target)) {
            writer.write(textToWrite());
        }
    }
}
