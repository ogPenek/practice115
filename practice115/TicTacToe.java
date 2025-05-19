import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TicTacToe {
    private static final String CONFIG_FILE = "tictactoe_config.txt";
    private static final String STATS_FILE = "tictactoe_stats.txt";
    
    private static int boardSize = 3;
    private static String player1Name = "Гравець 1 (X)";
    private static String player2Name = "Гравець 2 (O)";
    
    public static void main(String[] args) {
        loadConfig();
        
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\nМеню:");
            System.out.println("1. Нова гра");
            System.out.println("2. Налаштування");
            System.out.println("3. Статистика");
            System.out.println("4. Вихід");
            System.out.print("Виберіть пункт: ");
            
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Будь ласка, введіть число від 1 до 4.");
                continue;
            }
            
            switch (choice) {
                case 1:
                    playGame(scanner);
                    break;
                case 2:
                    configureSettings(scanner);
                    break;
                case 3:
                    showStatistics();
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Невірний вибір. Будь ласка, спробуйте ще.");
            }
        }
        
        scanner.close();
    }
    
    private static void playGame(Scanner scanner) {
        char[][] board = new char[boardSize][boardSize];
        initializeBoard(board);
        
        char currentPlayer = 'X';
        boolean gameEnded = false;
        int moves = 0;
        int maxMoves = boardSize * boardSize;
        
        while (!gameEnded) {
            printBoard(board);
            
            String playerName = currentPlayer == 'X' ? player1Name : player2Name;
            System.out.println(playerName + ", ваш хід (" + currentPlayer + ").");
            
            int row = -1, col = -1;
            boolean validInput = false;
            
            while (!validInput) {
                System.out.print("Введіть рядок (1-" + boardSize + "): ");
                try {
                    row = Integer.parseInt(scanner.nextLine()) - 1;
                } catch (NumberFormatException e) {
                    System.out.println("Будь ласка, введіть число.");
                    continue;
                }
                
                System.out.print("Введіть стовпець (1-" + boardSize + "): ");
                try {
                    col = Integer.parseInt(scanner.nextLine()) - 1;
                } catch (NumberFormatException e) {
                    System.out.println("Будь ласка, введіть число.");
                    continue;
                }
                
                if (row < 0 || row >= boardSize || col < 0 || col >= boardSize) {
                    System.out.println("Невірні координати. Спробуйте ще раз.");
                } else if (board[row][col] != ' ') {
                    System.out.println("Ця клітинка вже зайнята. Спробуйте ще раз.");
                } else {
                    validInput = true;
                }
            }
            
            board[row][col] = currentPlayer;
            moves++;
            
            if (checkWin(board, currentPlayer)) {
                printBoard(board);
                String winnerName = currentPlayer == 'X' ? player1Name : player2Name;
                System.out.println("Вітаємо! " + winnerName + " переміг!");
                saveGameResult(winnerName, currentPlayer);
                gameEnded = true;
            } else if (moves == maxMoves) {
                printBoard(board);
                System.out.println("Нічия!");
                saveGameResult("Нічия", ' ');
                gameEnded = true;
            } else {
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            }
        }
    }
    
    private static void initializeBoard(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = ' ';
            }
        }
    }
    
    private static void printBoard(char[][] board) {
        System.out.println();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(" " + board[i][j] + " ");
                if (j < board[i].length - 1) {
                    System.out.print("|");
                }
            }
            System.out.println();
            
            if (i < board.length - 1) {
                for (int j = 0; j < board[i].length; j++) {
                    System.out.print("---");
                    if (j < board[i].length - 1) {
                        System.out.print("+");
                    }
                }
                System.out.println();
            }
        }
        System.out.println();
    }
    
    private static boolean checkWin(char[][] board, char player) {
        // Перевірка рядків
        for (int i = 0; i < boardSize; i++) {
            boolean win = true;
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] != player) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }
        
        // Перевірка стовпців
        for (int j = 0; j < boardSize; j++) {
            boolean win = true;
            for (int i = 0; i < boardSize; i++) {
                if (board[i][j] != player) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }
        
        // Перевірка діагоналей
        boolean win = true;
        for (int i = 0; i < boardSize; i++) {
            if (board[i][i] != player) {
                win = false;
                break;
            }
        }
        if (win) return true;
        
        win = true;
        for (int i = 0; i < boardSize; i++) {
            if (board[i][boardSize - 1 - i] != player) {
                win = false;
                break;
            }
        }
        return win;
    }
    
    private static void configureSettings(Scanner scanner) {
        System.out.println("\nПоточні налаштування:");
        System.out.println("1. Розмір поля: " + boardSize + "x" + boardSize);
        System.out.println("2. Ім'я гравця 1 (X): " + player1Name);
        System.out.println("3. Ім'я гравця 2 (O): " + player2Name);
        System.out.println("4. Повернутися до меню");
        System.out.print("Що бажаєте змінити? ");
        
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Будь ласка, введіть число від 1 до 4.");
            return;
        }
        
        switch (choice) {
            case 1:
                System.out.print("Введіть новий розмір поля (3-10): ");
                try {
                    int newSize = Integer.parseInt(scanner.nextLine());
                    if (newSize >= 3 && newSize <= 10) {
                        boardSize = newSize;
                        saveConfig();
                        System.out.println("Розмір поля змінено.");
                    } else {
                        System.out.println("Невірний розмір. Має бути від 3 до 10.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Будь ласка, введіть число.");
                }
                break;
            case 2:
                System.out.print("Введіть нове ім'я для гравця 1 (X): ");
                String newName1 = scanner.nextLine().trim();
                if (!newName1.isEmpty()) {
                    player1Name = newName1;
                    saveConfig();
                    System.out.println("Ім'я гравця 1 змінено.");
                } else {
                    System.out.println("Ім'я не може бути порожнім.");
                }
                break;
            case 3:
                System.out.print("Введіть нове ім'я для гравця 2 (O): ");
                String newName2 = scanner.nextLine().trim();
                if (!newName2.isEmpty()) {
                    player2Name = newName2;
                    saveConfig();
                    System.out.println("Ім'я гравця 2 змінено.");
                } else {
                    System.out.println("Ім'я не може бути порожнім.");
                }
                break;
            case 4:
                break;
            default:
                System.out.println("Невірний вибір.");
        }
    }
    
    private static void saveConfig() {
        try {
            FileWriter writer = new FileWriter(CONFIG_FILE);
            writer.write("boardSize=" + boardSize + "\n");
            writer.write("player1Name=" + player1Name + "\n");
            writer.write("player2Name=" + player2Name + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Помилка збереження налаштувань: " + e.getMessage());
        }
    }
    
    private static void loadConfig() {
        try {
            Scanner fileScanner = new Scanner(new File(CONFIG_FILE));
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    switch (parts[0]) {
                        case "boardSize":
                            boardSize = Integer.parseInt(parts[1]);
                            break;
                        case "player1Name":
                            player1Name = parts[1];
                            break;
                        case "player2Name":
                            player2Name = parts[1];
                            break;
                    }
                }
            }
            fileScanner.close();
        } catch (Exception e) {
            // Якщо файл не знайдено або помилка читання, використовуємо значення за замовчуванням
            System.out.println("Використовуються налаштування за замовчуванням.");
        }
    }
    
    private static void saveGameResult(String winner, char symbol) {
        try {
            FileWriter writer = new FileWriter(STATS_FILE, true);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = dateFormat.format(new Date());
            
            writer.write(dateTime + " | Розмір поля: " + boardSize + "x" + boardSize + " | ");
            writer.write("Гравець 1: " + player1Name + " (X) vs Гравець 2: " + player2Name + " (O) | ");
            writer.write("Результат: " + winner);
            if (symbol != ' ') {
                writer.write(" (" + symbol + ")");
            }
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Помилка збереження статистики: " + e.getMessage());
        }
    }
    
    private static void showStatistics() {
        System.out.println("\nСтатистика ігор:");
        try {
            Scanner fileScanner = new Scanner(new File(STATS_FILE));
            int gameCount = 0;
            
            while (fileScanner.hasNextLine()) {
                System.out.println(fileScanner.nextLine());
                gameCount++;
            }
            
            if (gameCount == 0) {
                System.out.println("Статистика відсутня.");
            } else {
                System.out.println("\nВсього ігор: " + gameCount);
            }
            
            fileScanner.close();
        } catch (Exception e) {
            System.out.println("Статистика відсутня або не може бути завантажена.");
        }
    }
}