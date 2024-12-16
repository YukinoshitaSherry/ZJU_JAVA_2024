import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class Position {

    final public int x;
    final public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class SudokuHelper {

    public static Position makePosition(int x, int y) {
        return new Position(x, y);
    }

    public static int randomInt(int start, int end) {
        return (int) (Math.random() * (end - start + 1)) + start;
    }

    public static void displayGrid(int[][] grid) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                System.out.print(grid[row][col] == 0 ? "0 " : grid[row][col] + " ");
            }
            System.out.println();
        }
    }

    public static boolean validateRegions(int[][] regions) {
        int[] counter = new int[10];
        for (int[] row : regions) {
            for (int value : row) {
                if (value < 1 || value > 9) return false;
                counter[value]++;
            }
        }
        for (int i = 1; i <= 9; i++) {
            if (counter[i] != 9) return false;
        }
        return true;
    }
}

class SudokuSolver {

    private static final int GRID_SIZE = 9;

    final private int[][] puzzle;
    final private int[][] regions;

    private int[][] possibilities;
    private boolean[][][] allowedNumbers;
    final private Position[][] regionCells;

    public SudokuSolver(int[][] puzzle, int[][] regions) {
        this.puzzle = puzzle;
        this.regions = regions;

        regionCells = new Position[GRID_SIZE + 1][GRID_SIZE];
        int[] cellCounter = new int[GRID_SIZE + 1];
        
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                int regionNum = regions[row][col];
                regionCells[regionNum][cellCounter[regionNum]++] = new Position(row, col);
            }
        }
    }

    public boolean checkCells() {
        boolean[] appeared = new boolean[GRID_SIZE + 1];
        for (int i = 1; i <= GRID_SIZE; i++) {
            for (int j = 1; j <= GRID_SIZE; j++) {
                appeared[j] = false; // Reset check array
            }
            for (int j = 0; j < GRID_SIZE; j++) {
                int current = puzzle[regionCells[i][j].x][regionCells[i][j].y];
                if (current != 0) {
                    if (appeared[current]) {
                        return false;
                    } else {
                        appeared[current] = true;
                    }
                }
            }
        }
        return true;
    }

    public boolean solve() {
        allowedNumbers = new boolean[GRID_SIZE][GRID_SIZE][GRID_SIZE + 1];
        possibilities = new int[GRID_SIZE][GRID_SIZE];

        return dfs(1);
    }

    public boolean dfs(double rate) {
        // Initialize: Mark all numbers as valid for all positions
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                for (int k = 1; k <= GRID_SIZE; k++) {
                    allowedNumbers[i][j][k] = true;
                }
            }
        }

        // Process current board state for number validity
        List<Position> emptys = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (puzzle[i][j] != 0) {
                    // Mark numbers invalid in same row and column
                    for (int k = 0; k < GRID_SIZE; k++) {
                        allowedNumbers[i][k][puzzle[i][j]] = false;
                        allowedNumbers[k][j][puzzle[i][j]] = false;
                    }
                    // Mark numbers invalid in same region
                    for (int k = 0; k < GRID_SIZE; k++) {
                        Position currentCell = regionCells[regions[i][j]][k];
                        allowedNumbers[currentCell.x][currentCell.y][puzzle[i][j]] = false;
                    }
                }
            }
        }

        // Find empty cells with least possible numbers
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (puzzle[i][j] == 0) {
                    emptys.add(new Position(i, j));
                    possibilities[i][j] = 0;
                    for (int k = 1; k <= GRID_SIZE; k++) {
                        if (allowedNumbers[i][j][k]) {
                            possibilities[i][j]++;
                        }
                    }
                }
            }
        }
        if (emptys.isEmpty()) {
            // Solution found
            return true;
        }
        emptys.sort(new Comparator<Position>() {
            @Override
            public int compare(Position a, Position b) {
                return possibilities[a.x][a.y] - possibilities[b.x][b.y];
            }
        });
        if (possibilities[emptys.get(0).x][emptys.get(0).y] == 0) {
            // Contradiction found
            return false;
        }

        // Randomly select a cell with minimum possible numbers
        int minimalCount = 0;
        for (int i = 0; i < emptys.size(); i++) {
            if (possibilities[emptys.get(i).x][emptys.get(i).y] == possibilities[emptys.get(0).x][emptys.get(0).y]) {
                minimalCount++;
            }
        }
        int randomIndex = SudokuHelper.randomInt(0, minimalCount - 1);
        Position currentCell = emptys.get(randomIndex);

        // Try possible numbers
        int validCount = 0;
        int[] validValues = new int[GRID_SIZE];
        for (int k = 1; k <= GRID_SIZE; k++) {
            if (allowedNumbers[currentCell.x][currentCell.y][k]) {
                validValues[validCount++] = k;
            }
        }
        for (int i = 0; i < validCount; i++) {
            int index = SudokuHelper.randomInt(0, validCount - 1);
            int temp = validValues[i];
            validValues[i] = validValues[index];
            validValues[index] = temp;
        }

        // Backtracking search
        for (int i = 0; i < validCount; i++) {
            puzzle[currentCell.x][currentCell.y] = validValues[i];
            if (dfs(rate * (i + 1) / validCount)) {
                return true;
            }
        }
        puzzle[currentCell.x][currentCell.y] = 0;
        return false;
    }
}

class SudokuGenerator{

    private static final int GRID_SIZE = 9;
    private static final int SHUFFLE_ITERATIONS = 5;

    final private int[][] regions;
    final private int initialNumbers;

    public SudokuGenerator(int[][] regions, int initialNumbers) {
        this.regions = regions;
        this.initialNumbers = initialNumbers;
    }

    private void removeNumbers(int[][] board, int remains) {
        // Generate random position order
        Position[] positions = new Position[GRID_SIZE * GRID_SIZE];
        for (int i = 0; i <= GRID_SIZE-1; i++) {
            for (int j = 0; j <= GRID_SIZE-1; j++) {
                positions[i * GRID_SIZE + j] = new Position(i, j);
            }
        }
        for (int k = 0; k < GRID_SIZE * GRID_SIZE * SHUFFLE_ITERATIONS; k++) { // Random shuffle
            int i = SudokuHelper.randomInt(0, GRID_SIZE * GRID_SIZE - 1);
            int j = SudokuHelper.randomInt(0, GRID_SIZE * GRID_SIZE - 1);
            Position temp = positions[i];
            positions[i] = positions[j];
            positions[j] = temp;
        }

        // Remove numbers except for remains positions
        for (int i = remains; i < GRID_SIZE * GRID_SIZE; i++) {
            board[positions[i].x][positions[i].y] = 0;
        }
    }

    public int[][][] generate() {
        int[][][] result = new int[2][GRID_SIZE][GRID_SIZE];

        // Generate Sudoku with random content
        int[][] board = new int[GRID_SIZE][GRID_SIZE];
        while (true) {
            for (int i = 0; i <= GRID_SIZE-1; i++) {
                for (int j = 0; j <= GRID_SIZE-1; j++) {
                    board[i][j] = (i + j) % 9 + 1;
                }
            }

            for (int k = 0; k < GRID_SIZE * 2 * SHUFFLE_ITERATIONS; k++) {
                int type = SudokuHelper.randomInt(0, 1);
                int i = SudokuHelper.randomInt(0, GRID_SIZE - 1);
                int j = SudokuHelper.randomInt(0, GRID_SIZE - 1);
                if (type == 0) { // Swap rows
                    for (int p = 0; p < GRID_SIZE; p++) {
                        int temp = board[i][p];
                        board[i][p] = board[j][p];
                        board[j][p] = temp;
                    }
                } else { // Swap columns
                    for (int p = 0; p <= GRID_SIZE-1; p++) {
                        int temp = board[p][i];
                        board[p][i] = board[p][j];
                        board[p][j] = temp;
                    }
                }
            }

            removeNumbers(board, 18); // Initial removal
            
            SudokuSolver solver = new SudokuSolver(board, regions);
            if (!solver.checkCells()) {
                continue;
            }
            if (solver.solve()) {
                break;
            }
        }

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                result[0][i][j] = board[i][j];
                result[1][i][j] = board[i][j];
            }
        }
        removeNumbers(result[0], initialNumbers);

        return result;
    }
}

public class Main {

    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);
        
        boolean gameInProgress = true;
        while (gameInProgress) {
            try {
                // Display welcome message and game options
                System.out.println("Welcome to Sudoku Game!");
                System.out.println("Please select mode:");
                System.out.println("1: Solve an existing Sudoku");
                System.out.println("2: Generate a new Sudoku");
                System.out.print("Your choice (1 or 2): ");
                
                int gameMode = inputScanner.nextInt();
                if (gameMode != 1 && gameMode != 2) {
                    System.out.println("Error: Invalid choice. Please enter 1 or 2.Let's try again.\n");
                    continue;
                }

                int[][] regionMatrix = new int[9][9];
                
                if (gameMode == 1) {
                    // Mode 1: Solve existing puzzle
                    System.out.println("\nPlease input the 9x9 Sudoku puzzle (use 0 for empty cells):");
                    int[][] puzzleGrid = new int[9][9];
                    for (int row = 0; row < 9; row++) {
                        for (int col = 0; col < 9; col++) {
                            puzzleGrid[row][col] = inputScanner.nextInt();
                            if (puzzleGrid[row][col] < 0 || puzzleGrid[row][col] > 9) {
                                System.out.println("Error: Invalid input. Sudoku numbers must be between 0 and 9.Let's try again.\n");
                                continue;
                            }
                        }
                    }

                    System.out.println("\nPlease input the 9x9 region matrix (1-9 for regions):");
                    for (int row = 0; row < 9; row++) {
                        for (int col = 0; col < 9; col++) {
                            regionMatrix[row][col] = inputScanner.nextInt();
                        }
                    }

                    if (!SudokuHelper.validateRegions(regionMatrix)) {
                        System.out.println("Error: Invalid region matrix. Each region number (1-9) must be used and form a connected area.Let's try again.\n");
                        continue;
                    }

                    System.out.println("\nInput Puzzle:");
                    SudokuHelper.displayGrid(puzzleGrid);
                    System.out.println("Region Matrix:");
                    SudokuHelper.displayGrid(regionMatrix);
                    System.out.println("");

                    try {
                        SudokuSolver puzzleSolver = new SudokuSolver(puzzleGrid, regionMatrix);
                        if (!puzzleSolver.checkCells()) {
                            System.out.println("Error: Invalid Sudoku puzzle. Numbers conflict within regions.Let's try again.\n");
                            continue;
                        }
                        if (puzzleSolver.solve()) {
                            System.out.println("Sudoku solved successfully:");
                            SudokuHelper.displayGrid(puzzleGrid);
                        } else {
                            System.out.println("Error: No solution exists for this Sudoku.\n");
                            continue;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Error: Invalid region matrix. Region numbers must be between 1 and 9.Let's try again.\n");
                        continue;
                    }
                } else {
                    // Mode 2: Generate new puzzle
                    System.out.println("\nHow many hints do you want? (Recommended: 25-35)");
                    System.out.print("Enter number of hints: ");
                    int hintCount = inputScanner.nextInt();
                    
                    if (hintCount < 17 || hintCount > 81) {
                        System.out.println("Error: Number of hints must be between 17 and 81.Let's try again.\n");
                        continue;
                    }

                    System.out.println("\nPlease input the 9x9 region matrix (1-9 for regions):");
                    for (int row = 0; row < 9; row++) {
                        for (int col = 0; col < 9; col++) {
                            regionMatrix[row][col] = inputScanner.nextInt();
                        }
                    }

                    if (!SudokuHelper.validateRegions(regionMatrix)) {
                        System.out.println("Error: Invalid region matrix. Each region number (1-9) must be used and form a connected area.Let's try again.\n");
                        continue;
                    }

                    try {
                        System.out.println("\nRegion Matrix:");
                        SudokuHelper.displayGrid(regionMatrix);
                        System.out.println("");

                        SudokuGenerator generator = new SudokuGenerator(regionMatrix, hintCount);
                        int[][][] generatedResult = generator.generate();

                        System.out.println("Generated Puzzle:");
                        SudokuHelper.displayGrid(generatedResult[0]);
                        System.out.println("Solution:");
                        SudokuHelper.displayGrid(generatedResult[1]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Error: Invalid region matrix. Region numbers must be between 1 and 9.Let's try again.\n");
                        continue;
                    }
                }
                
                // Ask if player wants to continue
                while (true) {
                    System.out.println("\nWould you like to play again? (y/n): ");
                    String playerChoice = inputScanner.next().trim().toLowerCase();
                    if (playerChoice.equals("y")) {
                        System.out.println("\n");
                        break;
                    } else if (playerChoice.equals("n")) {
                        gameInProgress = false;
                        System.out.println("Thank you for playing! Goodbye!");
                        break;
                    } else {
                        System.out.println("Error: Please enter 'y' for yes or 'n' for no.");
                    }
                }
                
            } catch (Exception e) {
                System.out.println("Error: Invalid input. Let's try again.\n");
                inputScanner.nextLine();
            }
        }
    }
}
