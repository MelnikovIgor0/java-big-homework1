import java.util.Scanner;
import java.util.Stack;

public class Main {
    private static AbstractPlayer player1;
    private static AbstractPlayer player2;
    private static Stack<int[][]> fieldCondition = new Stack<int[][]>();
    private static int MaxScore = 0;

    private static boolean showMenu() {
        System.out.println("Welcome to 'reversi' game!!!");
        player1 = new Player(1);
        System.out.println("Select mode:");
        System.out.println("1) 2 players");
        System.out.println("2) player vs bot");
        System.out.println("3) close game");
        if (MaxScore == 0) {
            System.out.println("Nobody played game(((");
        } else {
            System.out.println("Top score of human-player: " + MaxScore);
        }
        Scanner in = new Scanner(System.in);
        int mode = -1;
        do {
            mode = in.nextInt();
        } while (mode < 1 || mode > 3);
        if (mode == 1) {
            player2 = new Player(2);
        } else {
            player2 = new Bot(2);
        }
        return (mode != 3);
    }

    private static void recolorField(int[][] field, int x, int y, int color, int dx, int dy) {
        int amountOtherColor = 0;
        int cx = x, cy = y;
        while (true) {
            x += dx;
            y += dy;
            if (x < 0 || y < 0 || x > 7 || y > 7 || field[x][y] == 0) {
                return;
            }
            if (field[x][y] == 3 - color) {
                amountOtherColor++;
            }
            if (field[x][y] == color) {
                break;
            }
        }
        if (amountOtherColor == 0) {
            return;
        }
        x = cx;
        y = cy;
        while (true) {
            x += dx;
            y += dy;
            if (x < 0 || y < 0 || x > 7 || y > 7 || field[x][y] == 0) {
                return;
            }
            if (field[x][y] == 3 - color) {
                field[x][y] = color;
            } else if (field[x][y] == color) {
                return;
            }
        }
    }

    private static int[][] getDeepCopy(int[][] field) {
        int[][] copy = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                copy[i][j] = field[i][j];
            }
        }
        return copy;
    }

    private static int[][] recolorField(int[][] field, int stepX, int stepY, int color) {
        int[][] fieldCopy = getDeepCopy(field);
        fieldCopy[stepX][stepY] = color;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    recolorField(fieldCopy, stepX, stepY, color, i, j);
                }
            }
        }
        return fieldCopy;
    }

    private static int stepForHuman(Player player, boolean condition) {
        if (!player.canDoStep(fieldCondition.peek())) {
            System.out.println("Step skipped because there's no available steps!");
            return -1;
        }
        if (condition) {
            System.out.println("Step of X");
        } else {
            System.out.println("Step of O");
        }
        System.out.print("1 - step; 2 - exit; 3 - possible steps; 4 - cancel a step: ");
        Scanner in = new Scanner(System.in);
        int mode = in.nextInt();
        if (mode == 1) {
            int position = player.doStep(fieldCondition.peek());
            if (position == -1) {
                return -1;
            }
            fieldCondition.push(recolorField(fieldCondition.peek(), position / 8, position % 8, player.color));
            player.printField(fieldCondition.peek(), false);
            System.out.println("Game field after player's step:");
        }
        return mode;
    }

    private static int stepForBot(Bot bot) {
        int position = bot.doStep(fieldCondition.peek());
        if (position == -1) {
            return -1;
        }
        fieldCondition.push(recolorField(fieldCondition.peek(), position / 8, position % 8, bot.color));
        bot.printField(fieldCondition.peek(), false);
        System.out.println("Game field after bot's step");
        return 1;
    }

    private static void resetField() {
        fieldCondition.clear();
        int[][] startField = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                startField[i][j] = 0;
            }
        }
        startField[3][3] = 2;
        startField[3][4] = 1;
        startField[4][4] = 2;
        startField[4][3] = 1;
        fieldCondition.push(startField);
    }

    private static void printPossibleSteps(AbstractPlayer player) {
        System.out.println("Possible steps:");
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                if (player.isStepValid(fieldCondition.peek(), i, j)) {
                    System.out.println(Integer.toString(i) + " " + Integer.toString(j));
                }
            }
        }
    }

    private static void gameProcess() {
        resetField();
        Scanner in = new Scanner(System.in);
        boolean condition = true, wasLastSkip = false;
        while (true) {
            if (condition) {
                int res = stepForHuman((Player)player1, condition);
                if (res == 1) {
                    condition = false;
                } else if (res == -1) {
                    if (wasLastSkip) {
                        break;
                    }
                    wasLastSkip = true;
                    condition = false;
                } else if (res == 2) {
                    break;
                } else if (res == 3) {
                    printPossibleSteps(player1);
                } else if (res == 4) {
                    if (player2.isBot()) {
                        if (fieldCondition.size() > 2) {
                            fieldCondition.pop();
                            fieldCondition.pop();
                            System.out.println("step canceled");
                        } else {
                            System.out.println("can not cancel a step!!!");
                        }
                    } else {
                        if (fieldCondition.size() > 1) {
                            fieldCondition.pop();
                            System.out.println("step canceled");
                            condition = !condition;
                        } else {
                            System.out.println("can not cancel a step!!!");
                        }
                        player1.printField(fieldCondition.peek(), false);
                        System.out.println("field after step cancelling (upper)");
                    }
                }
            } else {
                if (player2.isBot()) {
                    int res = stepForBot((Bot)player2);
                    if (res == 1) {
                        condition = true;
                    } else if (res == -1) {
                        if (wasLastSkip) {
                            break;
                        }
                        wasLastSkip = true;
                        condition = true;
                    }
                } else {
                    int res = stepForHuman((Player)player2, condition);
                    if (res == 1) {
                        condition = true;
                    } else if (res == -1) {
                        if (wasLastSkip) {
                            break;
                        }
                        wasLastSkip = true;
                        condition = true;
                    } else if (res == 2) {
                        break;
                    } else if (res == 3) {
                        printPossibleSteps(player1);
                    } else if (res == 4) {
                        if (player2.isBot()) {
                            if (fieldCondition.size() > 2) {
                                fieldCondition.pop();
                                fieldCondition.pop();
                                System.out.println("step canceled");
                            } else {
                                System.out.println("can not cancel a step!!!");
                            }
                        } else {
                            if (fieldCondition.size() > 1) {
                                fieldCondition.pop();
                                System.out.println("step canceled");
                                condition = !condition;
                            } else {
                                System.out.println("can not cancel a step!!!");
                            }
                        }
                        player2.printField(fieldCondition.peek(), false);
                        System.out.println("field after step cancelling (upper)");
                    }
                }
            }
        }
    }

    private static void gameFinished() {
        System.out.println("Game finished!!!");
        System.out.println("Score (X: " + Integer.toString(player1.countScore(fieldCondition.peek())) +
                "; O: " + Integer.toString(player2.countScore(fieldCondition.peek())) + ")");
        if (player1.countScore(fieldCondition.peek()) == player2.countScore(fieldCondition.peek())) {
            System.out.println("draw");
        } else if (player1.countScore(fieldCondition.peek()) > player2.countScore(fieldCondition.peek())) {
            System.out.println("first player win!");
        } else {
            System.out.println("second player win!");
        }
        if (!player1.isBot() && player1.countScore(fieldCondition.peek()) > MaxScore) {
            MaxScore = player1.countScore(fieldCondition.peek());
        }
        if (!player2.isBot() && player2.countScore(fieldCondition.peek()) > MaxScore) {
            MaxScore = player2.countScore(fieldCondition.peek());
        }
    }

    public static void main(String[] args) {
        while (showMenu()) {
            gameProcess();
            gameFinished();
        }
        System.out.println("Good bye!!!");
    }
}