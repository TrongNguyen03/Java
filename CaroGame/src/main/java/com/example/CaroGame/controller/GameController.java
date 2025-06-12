package com.example.CaroGame.controller;

import com.example.CaroGame.model.GameState;
import com.example.CaroGame.model.Move;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    private GameState gameState = new GameState();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/register")
    public void register(PlayerInfo info) {
        boolean added = gameState.addPlayer(info.getName());
        // Gửi trạng thái game mới cho tất cả client
        messagingTemplate.convertAndSend("/topic/board", gameState);
    }

    @MessageMapping("/move")
    public void handleMove(Move move) {
        gameState.applyMove(move);
        messagingTemplate.convertAndSend("/topic/board", gameState);
    }

    @MessageMapping("/reset")
    public void resetGame() {
        gameState.resetBoard();
        messagingTemplate.convertAndSend("/topic/board", gameState);
    }

    @MessageMapping("/leave")
    public void leave(PlayerInfo info) {
        String name = info.getName();
        boolean removed = gameState.leave(name);
        if (removed) {
            // Reset bàn nếu đang đấu dở (có người thắng hoặc có nước đi)
            gameState.resetBoard();
            // Gửi trạng thái game mới cho tất cả client
            messagingTemplate.convertAndSend("/topic/board", gameState);
        }
    }

    @Data
    public static class PlayerInfo {
        private String name;
    }
}
