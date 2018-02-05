# A Java Game of Life

This is a Java 8 implementation of Conway's Game of Life. The Game is a popular exercise, commonly discovered while learning programming.

If you don't know it, you're invited to check [Wikipedia](https://en.wikipedia.org/wiki/Conway's_Game_of_Life).

## Motivations

This project is originally a school project, but I improved it in order to introduce myself to Java 8 and to reactive programming, 
more specifically to the [RxJava](https://github.com/ReactiveX/RxJava) library.

## Implementation

> _*Disclaimer*_: This project has not been designed to be as simple as possible, but to produce a Game of Life easily configurable.
> If you're looking for a short and reactive Java 8 implementation, take a look at this gist : [ReactiveGameOfLife.java](https://gist.github.com/timyates/112627bf46040a8099ac) by [Tim Yates](https://gist.github.com/timyates).

The [Generation](https://github.com/KazeJiyu/game-of-life/blob/master/src/main/java/fr/kazejiyu/gameoflife/game/Generation.java)
class represents a set of cells, which is equivalent to one Game's generation. It provides several methods
to launch the evolution (i.e. to generate next generations) with tailored arguments. However, since the various parameters
can confuse the code, the [Evolution](https://github.com/KazeJiyu/game-of-life/blob/master/src/main/java/fr/kazejiyu/gameoflife/game/Evolution.java)
class provides a higher-level interface to configure a Game of Life.

The following code snippet shows how to:

- Create a 6*6 game area,
- Locate a [Glider](https://en.wikipedia.org/wiki/Glider_(Conway's_Life)) at its center,
- Launch the evolution with tailored options.

```java
public class Main {
    
    private static final int WIDTH = 6;
    private static final int HEIGHT = 6;

    public static void main(String[] args) {
        
        new Evolution().size(WIDTH, HEIGHT)
                // There are several variants of Conway's game of life.
                // this method makes able to select the one to use
                .followRule(Rule.GAME_OF_LIFE)
                // Populate the world with a pattern located at its center
                .populateWith(Pattern.GLIDER.transformToCenter(WIDTH, HEIGHT))
                // Print each generation in the console
                .forEach(new PrintToStream())
                // Write each generation in an XML file
                .forEach(new WrittenOnCompleted("test.xml", new XMLRepresentation()))
                // Write each generation in an HTML file
                .forEach(new WrittenOnCompleted("test.html", new HTMLRepresentation()))
                // Stop the evolution if :
                // - a sequence is repeated
                // - the world becomes stable
                // - the world becomes empty
                .stop(new WhenRepeated().or(new WhenStable()).or(new WhenEmpty()))
                // Generate 100 generations
                .evolve(100);
    }
}
```

## How to run

The code presented above corresponds to the class [fr.kazejiyu.gameoflife.Main](https://github.com/KazeJiyu/game-of-life/blob/master/src/main/java/fr/kazejiyu/gameoflife/Main.java).
It can be run easily by executing the following steps:

- Clone the repository:
```
git clone https://github.com/KazeJiyu/game-of-life
```
- Run the following command to build the sources:
```
cd game-of-life/
./gradlew build
```
- Then execute the generated JAR:
```
cd /build/libs/
java -jar game-of-life.jar
```
