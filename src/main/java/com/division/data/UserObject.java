package com.division.data;

import com.division.game.Game;

public class UserObject {

    private Game current;
    private int betMoney; // -5인경우 아직 진행 안함

    public UserObject(Game current, int betMoney) {
        this.current = current;
        this.betMoney = betMoney;
    }

    public Game getCurrent() {
        return current;
    }

    public int getBetMoney() {
        return betMoney;
    }

    public void setCurrent(Game current){
        this.current = current;
    }

    public void setBetMoney(int betMoney){
        this.betMoney = betMoney;
    }
}
