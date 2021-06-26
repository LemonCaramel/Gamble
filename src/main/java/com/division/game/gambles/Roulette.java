package com.division.game.gambles;

import com.division.Gamble;
import com.division.data.DataManager;
import com.division.data.GameData;
import com.division.file.GambleLogger;
import com.division.game.Game;
import com.division.util.EconomyAPI;
import com.division.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Roulette implements Game {

    private String header;
    private UUID target;
    private Gamble Plugin;
    private int taskID, count, stop, focus;
    private final int[] SLOT;

    public Roulette(UUID target, Gamble Plugin, int value) {
        header = DataManager.getInstance().getHeader();
        this.target = target;
        this.Plugin = Plugin;
        GameData.getInstance().playGame(target, this, value);
        taskID = 0;
        SLOT = new int[]{4, 15, 33, 40, 29, 11};
        count = 1;
        focus = 0;
        setGUI();
    }

    @Override
    public void setGUI() {
        Player p = Bukkit.getPlayer(target);
        Inventory inv = InventoryUtil.createInventory(header + "§0룰렛", 54);
        ItemStack edge = InventoryUtil.createItemStack(Material.IRON_BARS, " ");
        ItemStack btn = InventoryUtil.createItemStack(Material.STONE_BUTTON, header + "§b룰렛 돌리기", " ", " §8-  §f클릭시 룰렛을 돌립니다.", " ");
        ItemStack[] roll = new ItemStack[6];
        for (int i = 0; i < 6; i++) {
            //1~3는 0.0배 4는 0.1~0.5배 5는 0.5~2.0배 6은 1.5~3.0배
            double mul;
            if (i < 3)
                mul = 0.0;
            else if (i == 3)
                mul = Math.round((Math.random() * 1.1 + 0.1) * 10) / 10.0;
            else if (i == 4)
                mul = Math.round((Math.random() * 0.5 + 0.1) * 10) / 10.0;
            else
                mul = Math.round((Math.random() * 1.6 + 1.5) * 10) / 10.0;
            roll[i] = InventoryUtil.createItemStack(Material.BLACK_WOOL, header + "§6" + (i + 1) + "§f번째 칸", (short) 0," ", " §8-  §f배율 §7: §c" + mul + "§f배", " ");
        }
        for (int i = 0; i < 9; i++) {
            inv.setItem(i + 45, edge);
        }
        inv.setItem(4, roll[0]);
        inv.setItem(11, roll[5]);
        inv.setItem(15, roll[1]);
        inv.setItem(22, btn);
        inv.setItem(29, roll[4]);
        inv.setItem(33, roll[2]);
        inv.setItem(40, roll[3]);
        p.openInventory(inv);
    }

    @Override
    public void play() {
        Player p = Bukkit.getPlayer(target);
        p.getOpenInventory().setItem(22, new ItemStack(Material.AIR));
        stop = ThreadLocalRandom.current().nextInt(35, 61);
        taskID = Bukkit.getScheduler().runTaskTimer(Plugin, () -> {
            if (!p.getOpenInventory().getTitle().contains("룰렛"))
                Bukkit.getScheduler().cancelTask(taskID);
            else if (count == stop) {
                Bukkit.getScheduler().cancelTask(taskID);
                p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 3.0f, 1.0f);
                result();
            }
            else {
                setFocus(p.getOpenInventory().getItem(SLOT[focus]));
                returnFocus(p.getOpenInventory().getItem(SLOT[previous(focus)]));
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 3.0f, 3.0f);
                focus = flow(focus);
                count++;
            }
        }, 0L, 2L).getTaskId();
    }

    public void result() {
        Player p = Bukkit.getPlayer(target);
        Bukkit.getScheduler().runTaskLater(Plugin, () -> {
            if (GameData.getInstance().isPlaying(target)) {
                double value = getValue();
                double result = Math.round(GameData.getInstance().getData(target).getBetMoney() * value * 10) / 10.0;
                if (value == 0.0)
                    p.sendTitle(header, "§f0.0 배에 당첨되어 배팅금을 잃으셨습니다..ㅜ", 5, 50, 5);
                else {
                    p.sendTitle(header, "§c" + value + "§f배에 당첨되어 §6" + result + "§f원을 흭득하셨습니다.", 5, 50, 5);
                    GambleLogger.getInstance().addLog(p.getName() + "님이 룰렛에서 " + value + "배에 당첨되어 " + result + "원 흭득.");
                    EconomyAPI.getInstance().giveMoney(p, result);
                }
                GameData.getInstance().stopGame(target);
                p.closeInventory();
            }
        }, 20L);
    }

    public double getValue() {
        //로어 빼오기
        Player p = Bukkit.getPlayer(target);
        List<String> lore = p.getOpenInventory().getItem(SLOT[previous(focus)]).getItemMeta().getLore();
        String[] value = lore.get(1).split(":");
        value[1] = ChatColor.stripColor(value[1]).replace(" ", "").replace("배", "");
        return Double.parseDouble(value[1]);
    }

    public boolean isPlaying() {
        return taskID != 0;
    }

    public int previous(int a) {
        return a - 1 == -1 ? 5 : a - 1;
    }

    public int flow(int a) {
        return a + 1 == 6 ? 0 : a + 1;
    }

    public void setFocus(ItemStack item) {
        item.setType(Material.RED_WOOL);
    }

    public void returnFocus(ItemStack item) {
        item.setType(Material.BLACK_WOOL);
    }
}
