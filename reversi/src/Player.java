import java.util.Scanner;

// This is class of common player (human-player).
public final class Player extends AbstractPlayer {
    // This is constructor.
    // colorOfPlayer - color of current Player.
    public Player(int colorOfPlayer) {
        color = colorOfPlayer;
    }

    // This is overrided which realize the step by human-player.
    // field - game field.
    // returns the coordinate of cell on which player did step of -1 if there's no available step.
    public int doStep(int[][] field) {
        if (countScore(field) == 0) {
            return -1;
        }
        if (!canDoStep(field)) {
            return -1;
        }
        printField(field, true);
        int x = -1, y = -1;
        boolean firstStep = true;
        Scanner in = new Scanner(System.in);
        do {
            if (!firstStep) {
                System.out.println("Incorrect step!!!");
            }
            firstStep = false;
            System.out.print("Enter x coordinate of step: ");
            x = in.nextInt();
            System.out.print("Enter y coordinate of field: ");
            y = in.nextInt();
        } while (!isStepValid(field, x, y));
        return (x - 1) * 8 + (y - 1);
    }

    // This is overrided method, returns false because human-player is not bot.
    public boolean isBot() {
        return false;
    }
}
