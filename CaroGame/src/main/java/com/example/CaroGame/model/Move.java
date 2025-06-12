package com.example.CaroGame.model;

import lombok.Data;

@Data
public class Move {
    private int x;
    private int y;
    private String player; // "X" hoặc "O"
}
