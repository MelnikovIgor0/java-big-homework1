public abstract class AbstractPlayer {
    protected int color;

    private int getCell(int[][] field, int x, int y) {
        if (x < 0 || y < 0 || x > 7 || y > 7) {
            return -1;
        }
        return field[x][y];
    }

    private int getAmountClosed(int[][] field, int x, int y, int dx, int dy) {
        int amountOtherColor = 0;
        while (true) {
            x += dx;
            y += dy;
            if (x < 0 || y < 0 || x > 7 || y > 7 || field[x][y] == 0) {
                return 0;
            }
            if (field[x][y] == 3 - color) {
                amountOtherColor++;
            }
            if (field[x][y] == color) {
                return amountOtherColor;
            }
        }
    }

    protected boolean isStepValid(int[][] field, int stepX, int stepY) {
        if (stepX < 1 || stepX > 8 || stepY < 1 || stepY > 8) {
            return false;
        }
        stepX--;
        stepY--;
        if (field[stepX][stepY] != 0) {
            return false;
        }
        boolean opponentCell = false;
        for (int i = stepX - 1; i <= stepX + 1; i++) {
            for (int j = stepY - 1; j <= stepY + 1; j++) {
                if (getCell(field, i, j) == 3 - color) {
                    opponentCell = true;
                }
            }
        }
        if (!opponentCell) {
            return false;
        }
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ((i != 0 || j != 0) && getAmountClosed(field, stepX, stepY, i, j) > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canDoStep(int[][] field) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isStepValid(field, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int countScore(int[][] field) {
        int score = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (field[i][j] == color) {
                    score++;
                }
            }
        }
        return score;
    }

    protected int countCellsOfMyColor(int[][] field) {
        int kol = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (field[i][j] == color) {
                    kol++;
                }
            }
        }
        return kol;
    }

    public void printField(int[][] field, boolean nextSteps) {
        for (int i = 0; i < 3; i++) {
            System.out.println();
        }
        System.out.println("-------------------------------------");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (i + j == 0) {
                    System.out.print("|   ");
                } else if (i == 0 || j == 0) {
                    System.out.print("| " + Integer.toString(i + j) + " ");
                } else if (field[i - 1][j - 1] == 0) {
                    System.out.print("| ");
                    if (nextSteps && isStepValid(field, i, j)) {
                        System.out.print("+ ");
                    } else {
                        System.out.print("  ");
                    }
                } else if (field[i - 1][j - 1] == 1) {
                    System.out.print("| X ");
                } else if (field[i - 1][j - 1] == 2) {
                    System.out.print("| O ");
                }
            }
            System.out.println("|");
            System.out.println("-------------------------------------");
        }
        int[] scores = new int[3];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                scores[field[i][j]]++;
            }
        }
        System.out.println("Score (X: " + Integer.toString(scores[1]) + "; O: " + Integer.toString(scores[2]) + ")");
        if (!isBot()) {
            return;
        }
        if (nextSteps && color == 1) {
            System.out.println("Step of X");
        } else if (nextSteps) {
            System.out.println("Step of O");
        }
    }

    public abstract int doStep(int[][] field);

    public abstract boolean isBot();
}
