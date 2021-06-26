package com.division.listener;

import com.division.data.DataManager;
import com.division.data.StockManager;
import com.division.data.UserStock;
import moe.caramel.counterzombie.api.caramel.event.ZombieSettingChangeKickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ZombieEventListener implements Listener {

    private final String header;

    public ZombieEventListener() {
        header = DataManager.getInstance().getHeader();
    }

    @EventHandler
    public void onZombieSettingChangeKick(ZombieSettingChangeKickEvent event) {
        Player target = event.getPlayer();
        UserStock stock = StockManager.getInstance().getUserStockData().get(target.getUniqueId());
        if (stock != null) stock.clear();
        target.sendMessage(header + "§c게임 탈주 패널티로 모든 주식이 삭제되었습니다.");
    }
}
