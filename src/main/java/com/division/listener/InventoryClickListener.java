package com.division.listener;

import com.division.data.CardData;
import com.division.data.DataManager;
import com.division.data.GameData;
import com.division.file.GambleLogger;
import com.division.game.Game;
import com.division.game.gambles.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import javax.smartcardio.Card;

public class InventoryClickListener implements Listener {

    private String header;

    public InventoryClickListener() {
        header = DataManager.getInstance().getHeader();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getRawSlot() != -999) {
            String title = event.getInventory().getTitle();
            Player target = (Player) event.getWhoClicked();
            if (title.contains("슬롯머신")) {
                event.setCancelled(true);
                if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.LEVER) {
                    Game current = GameData.getInstance().getData(target.getUniqueId()).getCurrent();
                    if (current instanceof SlotMachine) {
                        SlotMachine machine = (SlotMachine) current;
                        if (!machine.isRunning()) {
                            machine.play();
                            target.playSound(target.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2.0f, 1.0f);
                            GambleLogger.getInstance().addLog(target.getName() + "님이 슬롯머신 1회 사용함");
                        }
                        else
                            target.sendMessage(header + "§c슬롯머신을 이미 사용중입니다..!");
                    }
                }
            }
            else if (title.contains("주사위")) {
                event.setCancelled(true);
                if (event.getCurrentItem() != null) {
                    Game current = GameData.getInstance().getData(target.getUniqueId()).getCurrent();
                    if (current instanceof Dice) {
                        Dice dice = (Dice) current;
                        if (!dice.isPlaying()) {
                            switch (event.getRawSlot()) {
                                case 19:
                                    dice.setRole(Dice.Role.LOW);
                                    break;
                                case 22:
                                    dice.setRole(Dice.Role.MID);
                                    break;
                                case 25:
                                    dice.setRole(Dice.Role.HIGH);
                                    break;
                                default:
                                    break;
                            }
                        }
                        else if (event.getCurrentItem().getType() == Material.GOLD_BLOCK)
                            dice.checkDice(event.getCurrentItem());
                    }
                }
            }
            else if (title.contains("블랙잭")) {
                event.setCancelled(true);
                if (event.getCurrentItem() != null) {
                    Game current = GameData.getInstance().getData(target.getUniqueId()).getCurrent();
                    if (current instanceof Blackjack) {
                        Blackjack blackjack = (Blackjack) current;
                        Material type = event.getCurrentItem().getType();
                        if (type == Material.STONE_BUTTON) {
                            //insurance
                            if (target.getOpenInventory().getItem(40).getType() == Material.REDSTONE_TORCH_ON)
                                target.sendMessage(header + ChatColor.RED + "Insurance(보험) 여부를 선택해주세요. (종이 클릭시 거부)");
                            else {
                                //일반처리
                                if (event.getClick() == ClickType.RIGHT)
                                    blackjack.doubleDown();
                                else
                                    blackjack.hit();
                            }
                        }
                        else if (type == Material.WOOD_BUTTON) {
                            //insurance
                            if (target.getOpenInventory().getItem(40).getType() == Material.REDSTONE_TORCH_ON)
                                target.sendMessage(header + ChatColor.RED + "Insurance(보험) 여부를 선택해주세요. (종이 클릭시 거부)");
                            else
                                //일반처리
                                blackjack.check();
                        }
                        else if (type == Material.REDSTONE_TORCH_ON) {
                            //insurance
                            event.getInventory().setItem(40, blackjack.insurance());
                        }
                        else if (type == Material.PAPER) {
                            //insurance 거부시
                            if (target.getOpenInventory().getItem(40).getType() == Material.REDSTONE_TORCH_ON)
                                blackjack.insuranceDeny();
                        }
                    }
                }
            }
            else if (title.contains("룰렛")) {
                event.setCancelled(true);
                if (event.getCurrentItem() != null) {
                    Game current = GameData.getInstance().getData(target.getUniqueId()).getCurrent();
                    if (current instanceof Roulette && event.getCurrentItem().getType() == Material.STONE_BUTTON)
                        current.play();
                }
            }
            else if (title.contains("동전")) {
                event.setCancelled(true);
                if (event.getCurrentItem() != null) {
                    Game current = GameData.getInstance().getData(target.getUniqueId()).getCurrent();
                    if (current instanceof Coin && event.getCurrentItem().getType() == Material.RECORD_3){
                        Coin coin = (Coin) current;
                        if (event.getRawSlot() == 11)
                            coin.check(Coin.Face.FRONT);
                        else
                            coin.check(Coin.Face.BACK);
                    }
                }
            }
            else if (title.contains("인디언포커")) {
                event.setCancelled(true);
                if (event.getCurrentItem() != null) {
                    Game current = GameData.getInstance().getData(target.getUniqueId()).getCurrent();
                    if (current instanceof Indian) {
                        Indian indian = (Indian) current;
                        if (event.getRawSlot() == 31 && event.getCurrentItem().getType() == Material.CHEST)
                            indian.checkDeck(event.getCurrentItem(), target.getUniqueId());
                        else {
                            if (indian.checkTurn(target.getUniqueId())) {
                                if (target.getOpenInventory().getItem(31).getType() == Material.CHEST)
                                    target.sendMessage(header + "§f상대의 패를 확인해주세요..");
                                else if (event.getCurrentItem().getType() == Material.WOOD_BUTTON) {
                                    if (event.getClick() == ClickType.RIGHT)
                                        indian.giveTurn(target.getUniqueId());
                                    else
                                        indian.betMoney(target.getUniqueId());
                                }
                                else if (event.getCurrentItem().getType() == Material.STONE_BUTTON)
                                    indian.giveUp(target.getUniqueId());

                            }
                            else
                                target.sendMessage(header + "§f당신의 차례가 아닙니다.");
                        }
                    }
                }
            }
            else if (title.contains("카드 도박")) {
                event.setCancelled(true);
                if (event.getCurrentItem() != null) {
                    Game current = GameData.getInstance().getData(target.getUniqueId()).getCurrent();
                    if (current instanceof CardGamble) {
                        CardGamble card = (CardGamble) current;
                        Player other = Bukkit.getPlayer(CardData.getInstance().getTarget(target.getUniqueId()));
                        if (event.getCurrentItem().getType() == Material.NOTE_BLOCK){
                            other.playSound(other.getLocation(), Sound.BLOCK_NOTE_BELL, 0.7f, 1.0f);
                            target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BELL, 0.7f, 1.0f);
                        }
                        else if (event.getCurrentItem().getType() == Material.BOOK) {
                            if (event.getRawSlot() == 38)
                                card.setCard(CardGamble.Type.KING, target.getUniqueId());
                            else if (event.getRawSlot() == 40)
                                card.setCard(CardGamble.Type.CITIZEN, target.getUniqueId());
                            else
                                card.setCard(CardGamble.Type.SLAVE, target.getUniqueId());
                        }
                    }
                }
            }
            else if (title.contains("포커")) {
                event.setCancelled(true);
                if (event.getCurrentItem() != null) {
                    Game current = GameData.getInstance().getData(target.getUniqueId()).getCurrent();
                    if (current instanceof Poker) {
                        Poker poker = (Poker) current;
                        if (event.getCurrentItem().getType() == Material.ENCHANTED_BOOK) {
                            if (event.getRawSlot() == 21)
                                poker.changeDeck(target.getUniqueId(), Poker.ClickSlot.ONE);
                            else if (event.getRawSlot() == 22)
                                poker.changeDeck(target.getUniqueId(), Poker.ClickSlot.TWO);
                            else if (event.getRawSlot() == 23)
                                poker.changeDeck(target.getUniqueId(), Poker.ClickSlot.THREE);
                            else if (event.getRawSlot() == 24)
                                poker.changeDeck(target.getUniqueId(), Poker.ClickSlot.FOUR);
                            else if (event.getRawSlot() == 25)
                                poker.changeDeck(target.getUniqueId(), Poker.ClickSlot.FIVE);
                        }
                        else if (event.getCurrentItem().getType() == Material.GLOWSTONE && event.getRawSlot() == 32)
                            poker.changeTurn(target.getUniqueId());
                    }
                }
            }
        }
    }
}
