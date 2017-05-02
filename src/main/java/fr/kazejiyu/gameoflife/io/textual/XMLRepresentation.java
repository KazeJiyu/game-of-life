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

import fr.kazejiyu.gameoflife.game.Generation;

/**
 * An XML representation of one or several {@link Generation}s.
 * 
 * @author Emmanuel Chebbi
 */
public final class XMLRepresentation extends TextualRepresentation {
	
    private int currentGen = 0;

    private final StringBuilder xml;

    public XMLRepresentation() {
        this.xml = new StringBuilder("<evolution>\n");
    }

    @Override
    public String textToWrite() {
        return xml.toString();
    }

    @Override
    public void onCompleted() {
        xml.append("</evolution>");
    }

    @Override
    public void onError(Throwable e) {
        System.err.println("AN ERROR OCCURED: " + e.getMessage());
    }

    @Override
    public void onNext(Generation generation) {
        xml.append("    <world generation=\"" + (++currentGen) + "\">\n");
        xml.append(asXML(generation));
        xml.append("    </world\n");
    }

    /**
     * @return the XML representation of {@code game}
     */
    private String asXML(Generation game) {
        String content = game.toString();
        StringBuilder sb = new StringBuilder("        <cells>\n");

        int returnSkiped = 0;

        for (int row = 0; row < game.rows(); row++) {
            sb.append("            ");
            for (int col = 0; col < game.cols(); col++) {
                char curr;

                do {
                    int linearIndex = row * game.cols() + col + returnSkiped;

                    if (linearIndex >= content.length())
                        throw new IllegalArgumentException(
                                "the result's format of game.toString() method is not as expected");

                    curr = content.charAt(linearIndex);

                    if (curr == '\n')
                        returnSkiped++;

                } while (curr == '\n');

                String cell = (curr == 'o') ? "<cell alive=\"true\" row=\"" + row + "\" col=\"" + col + "\"/>"
                        : "<cell alive=\"false\" row=\"" + row + "\" col=\"" + col + "\"/>";
                sb.append(cell);
            }
            sb.append("\n");
        }
        sb.append("        </cells>\n");

        return sb.toString();
    }
}
