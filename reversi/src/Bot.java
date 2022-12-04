public class Bot extends AbstractPlayer {
    public Bot(int colorOfPlayer) {
        color = colorOfPlayer;
    }

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

    public int doStep(int[][] field) {
        if (countCellsOfMyColor(field) == 0) {
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

    public boolean isBot() {
        return true;
    }
}
