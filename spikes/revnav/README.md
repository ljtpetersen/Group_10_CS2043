# Reversable Navigation Spike
This spike's purpose is to investigate the possibility of a reversable
navigation system for the graphical user interface using JavaFX.

Spike Name: Reversable Navigation System

Spike Owners: James Petersen and Ian Young

Spike Branch Name: revnav-spike

Goals: Whether the development of a reversable navigation system similar to that of a web browser is achievable in a reasonable amount of time. The reversable navigation system should contain a back button, forwards button, home button, and reset/logout button.

Outcome: The outcome of the spike is that it is achievable, and a prototype demonstrating the concept is available in the GitHub repository. However, more time would be needed to iron out all issues everything.

## Compilation and Execution.
The program must be compiled with JavaFX in the classpath. As well,
when executing the `Main` class, the `--module-path` flag must be
specified with the directory the JavaFX jar files are located in,
and `--add-modules javafx.controls,javafx.fxml` flag must be added as well.

When executing the program on my machine, I used

```
$ java -cp '.:/usr/share/openjfx/lib/*' --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml Main
```
