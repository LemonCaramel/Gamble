package com.division.game.gambles;

import com.division.Gamble;
import com.division.data.GameData;
import com.division.game.Game;

import java.util.UUID;

public class Poker implements Game {

    private Gamble Plugin;
    private UUID player, target;
    private boolean turn;
    private int MAX_BET;

    public Poker(UUID player, UUID target, Gamble Plugin, int value) {
        this.player = player;
        this.target = target;
        this.Plugin = Plugin;
        MAX_BET = value;
        GameData.getInstance().playGame(player, this, value);
        GameData.getInstance().playGame(target, this, value);
    }

    @Override
    public void setGUI() {

    }

    @Override
    public void play() {
        //NOT USE
    }
}
