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

import fr.kazejiyu.gameoflife.game.World;

/**
 * An HTML representation of one or several {@link World}.
 * 
 * @author Emmanuel Chebbi
 */
public final class HTMLRepresentation extends TextualRepresentation {
	
    private int currentGen = 0;

    private final StringBuilder html;

    private static final String HTML_HEADER = htmlHeader();

    public HTMLRepresentation() {
        this.html = new StringBuilder(HTML_HEADER);
    }

    @Override
    public String textToWrite() {
        return html.toString();
    }

    @Override
    public void onCompleted() {
        html.append("</center>\n</body>\n</html>");
    }

    @Override
    public void onError(Throwable e) {
        System.err.println("AN ERROR OCCURED: " + e.getMessage());
    }

    @Override
    public void onNext(World generation) {
        html.append("<h2>Génération " + (++currentGen) + "</h2>\n");
        html.append(asHTML(generation));
    }

    /**
     * @return the HTML representation of {@code game}
     */
    private String asHTML(World game) {
        String content = game.toString();
        StringBuilder sb = new StringBuilder("<table border='1' style='color:white;border-collapse:collapse'>\n");

        int returnSkiped = 0;

        for (int row = 0; row < game.rows(); row++) {
            sb.append("    <tr>\n        ");
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

                String cell = (curr == 'o') ? "<TD width='20' style='background:red';>&nbsp;</TD>"
                        : "<TD width='20' style='background:white';>&nbsp;</TD>";
                sb.append(cell);
            }
            sb.append("\n    </tr>\n");
        }
        sb.append("</table>\n");

        return sb.toString();
    }

    private static String htmlHeader() {
        return "<html>\n" + "<head>\n" + "<meta http-equiv='Content-Type' content='text/html'; charset='utf-8'>\n"
                + "<title>Jeu de la vie</title>\n" + "</head><body style='background:yellow;'>\n" + "<center>\n"
                + "<h1 align='center'>Le jeu de la vie</h1>";
    }

}
