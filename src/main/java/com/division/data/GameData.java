package com.division.data;

import com.division.game.Game;

import java.util.HashMap;
import java.util.UUID;

public class GameData {

    private static GameData instance;
    private HashMap<UUID, UserObject> gameMap;

    static {
        instance = new GameData();
    }

    private GameData() {
        gameMap = new HashMap<>();
    }

    public static GameData getInstance() {
        return instance;
    }

    public boolean isPlaying(UUID data) {
        return gameMap.containsKey(data);
    }

    public void playGame(UUID data, Game game, int betMoney) {
        gameMap.put(data, new UserObject(game, betMoney));
    }

    public void stopGame(UUID data) {
        gameMap.remove(data);
    }

    public UserObject getData(UUID data){
        return gameMap.get(data);
    }
}
