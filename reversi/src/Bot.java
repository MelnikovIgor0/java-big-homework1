// This is class of bot-player
public final class Bot extends AbstractPlayer {
    // This is constructor.
    // colorOfPlayer - color of current Player.
    public Bot(int colorOfPlayer) {
        color = colorOfPlayer;
    }

    // This method evaluates score by probable step in specified direction.
    // field - game field.
    // (x, y) - coordinates of step.
    // (dx, dy) direction, which should be evaluated.
    // return double value, scoring of specified step.
    private double evaluateStepDirection(int[][] field, int x, int y, int dx, int dy) {
        double score = 0;
        while (true) {
            x += dx;
            y += dy;
            if (x < 0 || y < 0 || x > 7 || y > 7) {
                return 0;
            }
            if (field[x][y] == 3 - color) {
                if (x == 0 || x == 7 || y == 0 || y == 7) {
                    score += 2;
                } else {
                    score += 1;
                }
            }
            if (field[x][y] == color) {
                return score;
            }
        }
    }

    // This method evaluates specified step fully.
    // field - game field.
    // (x, y) - coordinates of step.
    // return double value, scoring of specified step.
    private double evaluateStep(int[][] field, int x, int y) {
        double score = 0;
        if ((x == 0 && y == 0) || (x == 0 && y == 7) || (x == 7 && y == 0) || (x == 7 && y == 7)) {
            score += 0.8;
        } else if (x == 0 || y == 0 || x == 7 || y == 7) {
            score += 0.4;
        }
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    score += evaluateStepDirection(field, x, y, i, j);
                }
            }
        }
        return score;
    }

    // This is overrided which realize the step by bot-player.
    // field - game field.
    // returns the coordinate of cell on which player did step of -1 if there's no available step.
    public int doStep(int[][] field) {
        if (countScore(field) == 0) {
            return -1;
        }
        if (!canDoStep(field)) {
            return -1;
        }
        int bestPosition = -1;
        double maxValue = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isStepValid(field, i + 1, j + 1)) {
                    double currentValue = evaluateStep(field, i, j);
                    if (currentValue > maxValue) {
                        maxValue = currentValue;
                        bestPosition = i * 8 + j;
                    }
                }
            }
        }
        return bestPosition;
    }

    // This is overrided method, returns false because bot-player is bot.
    public boolean isBot() {
        return true;
    }
}
