import java.util.Scanner;

public final class Player extends AbstractPlayer {
    public Player(int colorOfPlayer) {
        color = colorOfPlayer;
    }

    public int doStep(int[][] field) {
        if (countCellsOfMyColor(field) == 0) {
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

    public boolean isBot() {
        return false;
    }
}
