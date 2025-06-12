package com.example.CaroGame.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class GameState {
    private String[][] board = new String[20][20];
    private String turn = "X";
    private Map<String, String> players = new HashMap<>();
    private Map<String, Integer> scores = new HashMap<>();
    private String winner;

    public GameState() {
        resetBoard();
    }

    public void resetBoard() {
        for (int i = 0; i < 20; i++)
            for (int j = 0; j < 20; j++)
                board[i][j] = "";
        winner = null;
        turn = "X";
    }

    // Đổi vai X/O giữa 2 người chơi
    public void switchRoles() {
        if (players.size() == 2) {
            String playerX = players.get("X");
            String playerO = players.get("O");
            players.put("X", playerO);
            players.put("O", playerX);
        }
    }

    public boolean applyMove(Move move) {
        if (winner != null) return false;
        if (!turn.equals(move.getPlayer())) return false;
        if (!board[move.getX()][move.getY()].isEmpty()) return false;

        board[move.getX()][move.getY()] = move.getPlayer();

        if (checkWin(move.getX(), move.getY(), move.getPlayer())) {
            winner = move.getPlayer();
            // Ghi điểm cho người chơi thắng (theo tên)
            String winnerName = players.get(winner);
            scores.put(winnerName, scores.getOrDefault(winnerName, 0) + 1);

            switchRoles();
            resetBoard();
        } else {
            turn = turn.equals("X") ? "O" : "X";
        }

        return true;
    }

    private boolean checkWin(int x, int y, String symbol) {
        int[][] directions = {
                {0, 1}, {1, 0}, {1, 1}, {1, -1}
        };
        for (int[] dir : directions) {
            int count = 1;
            count += countDirection(x, y, dir[0], dir[1], symbol);
            count += countDirection(x, y, -dir[0], -dir[1], symbol);
            if (count >= 5) return true;
        }
        return false;
    }

    private int countDirection(int x, int y, int dx, int dy, String symbol) {
        int count = 0;
        for (int step = 1; step < 5; step++) {
            int nx = x + dx * step;
            int ny = y + dy * step;
            if (nx >= 0 && ny >= 0 && nx < 20 && ny < 20 && board[nx][ny].equals(symbol)) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    // Khi thêm người chơi mới, khởi tạo điểm nếu chưa có
    public boolean addPlayer(String name) {
        if (players.containsValue(name)) return false;
        if (!players.containsKey("X")) {
            players.put("X", name);
            scores.putIfAbsent(name, 0);
            return true;
        } else if (!players.containsKey("O")) {
            players.put("O", name);
            scores.putIfAbsent(name, 0);
            return true;
        }
        return false;
    }

    public boolean leave(String name) {
        String symbolToRemove = null;
        for (Map.Entry<String, String> entry : players.entrySet()) {
            if (entry.getValue().equals(name)) {
                symbolToRemove = entry.getKey();
                break;
            }
        }
        if (symbolToRemove != null) {
            players.remove(symbolToRemove);
            // Nếu người rời là người đang đi, đổi lượt cho người còn lại
            if (symbolToRemove.equals(turn)) {
                turn = players.keySet().stream().findFirst().orElse("X");
            }
            // Không xóa điểm của người rời (scores)
            return true;
        }
        return false;
    }
}