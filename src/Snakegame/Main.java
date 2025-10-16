package practicegame;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        SnakeGame snakeGame = new SnakeGame(6, 6);
        snakeGame.start();
    }
}

class SnakeGame{
    private final char[][] board;
    private final Queue<Node> snakeBody = new LinkedList<>();
    private final Queue<Node> foodQueue = new LinkedList<>();
    private final Random random = new Random();
    private int currentRow = 0;
    private int currentCol = 0;
    private char currentHead = '>';

    public SnakeGame(int rows, int cols) {
        board = new char[rows][cols];
        snakeBody.add(new Node(0, 0));
        board[0][0] = currentHead;
        generateFoodPositions();
        placeFood();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        printBoard();

        while (true) {
            System.out.print("Enter move (U, D, L, R): ");
            char move = scanner.next().toUpperCase().charAt(0);
            if (makeMove(move)) {
                printBoard();
            } else {
                System.out.println("Game Over!");
                break;
            }
        }
        scanner.close();
    }

    private boolean makeMove(char direction){
        int newRow = currentRow, newCol = currentCol;
        char newHead;
        switch (direction) {
            case 'U': newRow--; newHead = '^'; break;
            case 'D': newRow++; newHead = 'v'; break;
            case 'L': newCol--; newHead = '<'; break;
            case 'R': newCol++; newHead = '>'; break;
            default:
                System.out.println("Invalid move! Use U, D, L, R.");
                return true;
        }

        if (!isValidMove(newRow, newCol)) {
            return false;
        }

        boolean ateFood = (board[newRow][newCol] == 'X');

        // If didn't eat food, remove the tail
        if (!ateFood) {
            Node tail = snakeBody.poll();
            board[tail.row][tail.col] = '\0';
        }

        // Add new head to the queue
        snakeBody.add(new Node(newRow, newCol));

        // Update old head position to body (only if snake length > 1)
        if (snakeBody.size() > 1) {
            board[currentRow][currentCol] = '.';
        }

        // Update current position
        currentRow = newRow;
        currentCol = newCol;
        currentHead = newHead;
        board[newRow][newCol] = currentHead;

        // Place new food if we ate
        if (ateFood) {
            placeFood();
        }

        return true;
    }

    private boolean isValidMove(int row, int col) {
        // Check bounds
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
            return false;
        }
        // Can move to empty space or food, but not to snake body
        char cell = board[row][col];
        return cell == '\0' || cell == 'X';
    }

    private void generateFoodPositions() {
        foodQueue.add(new Node(1, 0));
        foodQueue.add(new Node(2, 2));
        foodQueue.add(new Node(3, 4));
        foodQueue.add(new Node(5, 2));
        foodQueue.add(new Node(4, 5));
    }

    private void placeFood(){
        Node food;
        if (!foodQueue.isEmpty()) {
            food = foodQueue.poll();
        } else {
            do {
                food = new Node(random.nextInt(board.length), random.nextInt(board[0].length));
            } while (board[food.row][food.col] != '\0');
        }
        board[food.row][food.col] = 'X';
    }

    private void printBoard() {
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print((cell == '\0' ? '*' : cell) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

 class Node {
    final int row, col;

    Node(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
