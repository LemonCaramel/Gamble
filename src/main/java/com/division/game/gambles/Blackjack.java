package com.division.game.gambles;

import com.division.Gamble;
import com.division.data.DataManager;
import com.division.data.GameData;
import com.division.file.GambleLogger;
import com.division.game.Game;
import com.division.util.EconomyAPI;
import com.division.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Blackjack implements Game {

    private String header;
    private UUID target;
    private Gamble Plugin;
    private ArrayList<String> dealer, user, deck;

    public Blackjack(UUID target, Gamble Plugin, int value) {
        this.target = target;
        this.Plugin = Plugin;
        header = DataManager.getInstance().getHeader();
        dealer = new ArrayList<>();
        user = new ArrayList<>();
        deck = new ArrayList<>(Arrays.asList("♠A,♠2,♠3,♠4,♠5,♠6,♠7,♠8,♠9,10,♠Q,♠J,♠K,♥A,♥2,♥3,♥4,♥5,♥6,♥7,♥8,♥9,♥10,♥Q,♥J,♥K,♦A,♦2,♦3,♦4,♦5,♦6,♦7,♦8,♦9,♦10,♦Q,♦J,♦K,♣A,♣2,♣3,♣4,♣5,♣6,♣7,♣8,♣9,♣10,♣Q,♣J,♣K".split(",")));
        GameData.getInstance().playGame(target, this, value);
        setGUI();
    }

    public enum Reason {
        BURST,
        BLACKJACK,
        INSURANCE,
        LOW,
        HIGH,
        SAME
    }

    @Override
    public void setGUI() {
        Player p = Bukkit.getPlayer(target);
        if (p != null) {
            prepareDeck();
            ItemStack dealerDeck, userDeck;
            ItemStack edge = InventoryUtil.createItemStack(Material.IRON_FENCE, " ");
            Inventory black = Bukkit.createInventory(null, 54, header + "§0블랙잭");
            for (int i = 0; i < 9; i++) {
                black.setItem(i, edge);
                black.setItem(i + 45, edge);
            }
            for (int i = 0; i < 4; i++) {
                black.setItem((i + 1) * 9, edge);
                black.setItem((i + 2) * 9 - 1, edge);
            }
            if (getValueA1(dealer.get(0)) == getValueA11(dealer.get(0)))
                dealerDeck = InventoryUtil.createItemStack(Material.ENCHANTED_BOOK, header + "§b딜러의 패", " ", " §8-  §f" + dealer.get(0) + " + ???", " §8-  §c현재 딜러의 수치 §7: §b" + getValueA1(dealer.get(0)) + " + ??? 점");
            else
                dealerDeck = InventoryUtil.createItemStack(Material.ENCHANTED_BOOK, header + "§b딜러의 패", " ", " §8-  §f" + dealer.get(0) + " + ???", " §8-  §c현재 딜러의 수치 §7: §b" + getValueA1(dealer.get(0)) + " + ??? 점 §7/ §b" + getValueA11(dealer.get(0)) + " + ??? 점");
            if (getValueA1(user) == getValueA11(user))
                userDeck = InventoryUtil.createItemStack(Material.PAPER, header + "§a당신의 패", " ", " §8-  §f" + user.get(0) + ", " + user.get(1), " §8-  §b현재 당신의 수치 §7: §a" + getValueA1(user) + "§f점");
            else
                userDeck = InventoryUtil.createItemStack(Material.PAPER, header + "§a당신의 패", " ", " §8-  §f" + user.get(0) + ", " + user.get(1), " §8-  §b현재 당신의 수치 §7: §a" + getValueA1(user) + "§f점 §7/ §a" + getValueA11(user) + "§f점");
            ItemStack hit = InventoryUtil.createItemStack(Material.STONE_BUTTON, header + "§bHit (받기) / Double Down (x2)", " ", " §8-  §f클릭시 카드를 한장 더 받습니다.", " §8-  §f우클릭시 베팅액의 100%를 추가 베팅하며 카드를 한장만 받고 Stay처리 됩니다.", " ");
            ItemStack stay = InventoryUtil.createItemStack(Material.WOOD_BUTTON, header + "§cStay (멈추기)", " ", " §8-  §f클릭시 턴을 마칩니다.", " ");
            black.setItem(13, dealerDeck);
            black.setItem(29, stay);
            black.setItem(31, userDeck);
            black.setItem(33, hit);
            p.openInventory(black);
            if (checkInsurance()) {
                ItemStack insurance = InventoryUtil.createItemStack(Material.REDSTONE_TORCH_ON, header + "§4Insurance (보험)", " ", " §8-  §f클릭시 베팅액의 50%를 보험으로 듭니다.", " §8-  §f딜러 블랙잭이 아닌 경우 보험금은 제거됩니다.", " ");
                p.getOpenInventory().setItem(40, insurance);
                if (getValueA1(user) == 21 || getValueA11(user) == 21)
                    disableGame();
            }
            else {
                if (getValueA1(user) == 21 || getValueA11(user) == 21)
                    winGame(Reason.BLACKJACK);
            }
        }
    }

    @Override
    public void play() {
        //NOT USED
    }

    public boolean checkInsurance() {
        return dealer.get(0).contains("A") || dealer.get(0).contains("Q") || dealer.get(0).contains("J") || dealer.get(0).contains("K");
    }

    public void winGame(Reason reason) {
        Player p = Bukkit.getPlayer(target);
        disableGame();
        ItemStack dealerDeck;
        if (getValueA1(dealer) == getValueA11(dealer))
            dealerDeck = InventoryUtil.createItemStack(Material.ENCHANTED_BOOK, header + "§b딜러의 패", " ", " §8-  §f" + getCard(dealer), " §8-  §c현재 딜러의 수치 §7: §b" + getValueA1(dealer) + " 점");
        else
            dealerDeck = InventoryUtil.createItemStack(Material.ENCHANTED_BOOK, header + "§b딜러의 패", " ", " §8-  §f" + getCard(dealer), " §8-  §c현재 딜러의 수치 §7: §b" + getValueA1(dealer) + " §7/ §b" + getValueA11(dealer) + "§b점");
        p.getOpenInventory().setItem(13, dealerDeck);
        Bukkit.getScheduler().runTaskLater(Plugin, () -> {
            if (GameData.getInstance().isPlaying(target)) {
                switch (reason) {
                    case BLACKJACK:
                        EconomyAPI.getInstance().giveMoney(p, GameData.getInstance().getData(target).getBetMoney() * 2.5);
                        GambleLogger.getInstance().addLog(p.getName() + "님이 블랙잭 승리로 " + GameData.getInstance().getData(target).getBetMoney() * 2.5 + "원 흭득.");
                        p.sendTitle(header + "§6츽킨!", "§b블랙잭! " + "§c베팅액 " + GameData.getInstance().getData(target).getBetMoney() * 2.5 + " §f원 흭득!", 5, 50, 5);
                        GameData.getInstance().stopGame(target);
                        p.closeInventory();
                        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                        break;
                    case BURST:
                        EconomyAPI.getInstance().giveMoney(p, GameData.getInstance().getData(target).getBetMoney() * 2);
                        GambleLogger.getInstance().addLog(p.getName() + "님이 블랙잭 딜러 버스트 승리로 " + GameData.getInstance().getData(target).getBetMoney() * 2 + "원 흭득.");
                        p.sendTitle(header + "§a딜러 버스트!", "§f승리하셨습니다! §c베팅액 §6" + GameData.getInstance().getData(target).getBetMoney() * 2 + " §f원 흭득!", 5, 50, 5);
                        GameData.getInstance().stopGame(target);
                        p.closeInventory();
                        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                        break;
                    case INSURANCE:
                        EconomyAPI.getInstance().giveMoney(p, GameData.getInstance().getData(target).getBetMoney());
                        p.sendTitle(header, "§cInsurance §f성공! §b베팅액 §6" + GameData.getInstance().getData(target).getBetMoney() + " §f원 복구!", 5, 50, 5);
                        GambleLogger.getInstance().addLog(p.getName() + "님이 블랙잭 insurance 승리로 " + GameData.getInstance().getData(target).getBetMoney() + "원 흭득.");
                        GameData.getInstance().stopGame(target);
                        p.closeInventory();
                        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                        break;
                    case HIGH:
                        EconomyAPI.getInstance().giveMoney(p, GameData.getInstance().getData(target).getBetMoney() * 2);
                        p.sendTitle(header + "§b승리!", "§f딜러보다 수치가 높습니다! §c베팅액 §6" + GameData.getInstance().getData(target).getBetMoney() * 2 + " §f원 흭득!", 5, 50, 5);
                        GambleLogger.getInstance().addLog(p.getName() + "님이 블랙잭 일반 승리로 " + GameData.getInstance().getData(target).getBetMoney() * 2 + "원 흭득.");
                        GameData.getInstance().stopGame(target);
                        p.closeInventory();
                        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                        break;
                    case SAME:
                        EconomyAPI.getInstance().giveMoney(p, GameData.getInstance().getData(target).getBetMoney());
                        p.sendTitle(header + "§e비겼습니다!", "§f딜러와 수치가 같습니다. §c베팅액 §6" + GameData.getInstance().getData(target).getBetMoney() + " §f원 흭득!", 5, 50, 5);
                        GambleLogger.getInstance().addLog(p.getName() + "님이 블랙잭에서 비겨서 " + GameData.getInstance().getData(target).getBetMoney() + "원 흭득.");
                        GameData.getInstance().stopGame(target);
                        p.closeInventory();
                        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                        break;
                }
            }
        }, 30);
    }

    public void loseGame(Reason reason) {
        Player p = Bukkit.getPlayer(target);
        disableGame();
        ItemStack dealerDeck;
        if (getValueA1(dealer) == getValueA11(dealer))
            dealerDeck = InventoryUtil.createItemStack(Material.ENCHANTED_BOOK, header + "§b딜러의 패", " ", " §8-  §f" + getCard(dealer), " §8-  §c현재 딜러의 수치 §7: §b" + getValueA1(dealer) + " 점");
        else
            dealerDeck = InventoryUtil.createItemStack(Material.ENCHANTED_BOOK, header + "§b딜러의 패", " ", " §8-  §f" + getCard(dealer), " §8-  §c현재 딜러의 수치 §7: §b" + getValueA1(dealer) + " §7/ §b" + getValueA11(dealer) + "§b점");
        p.getOpenInventory().setItem(13, dealerDeck);
        Bukkit.getScheduler().runTaskLater(Plugin, () -> {
            if (GameData.getInstance().isPlaying(target)) {
                switch (reason) {
                    case BLACKJACK:
                        p.sendTitle(header + "§4딜러 블랙잭..", "§c패배 하셨습니다..ㅜㅜ", 5, 50, 5);
                        GameData.getInstance().stopGame(target);
                        p.closeInventory();
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 2.0f, 1.0f);
                        break;
                    case BURST:
                        p.sendTitle(header + "§c버스트..", "§c패배 하셨습니다..ㅜㅜ", 5, 50, 5);
                        GameData.getInstance().stopGame(target);
                        p.closeInventory();
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 2.0f, 1.0f);
                        break;
                    case LOW:
                        p.sendTitle(header + "§c패배하셨습니다..", "§c딜러보다 수치가 낮습니다.ㅜ", 5, 50, 5);
                        GameData.getInstance().stopGame(target);
                        p.closeInventory();
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 2.0f, 1.0f);
                        break;
                }
            }
        }, 20);
    }

    public String getCard(ArrayList<String> array) {
        StringBuilder st = new StringBuilder().append("§f");
        for (int i = 0; i < array.size(); i++) {
            if (i + 1 == array.size()) {
                st.append(array.get(i));
            }
            else {
                st.append(array.get(i)).append(", ");
            }
        }
        return st.toString();
    }

    public void disableGame() {
        Player p = Bukkit.getPlayer(target);
        if (p != null) {
            p.getOpenInventory().setItem(29, new ItemStack(Material.AIR));
            p.getOpenInventory().setItem(33, new ItemStack(Material.AIR));
        }
    }

    public ItemStack insurance() {
        Player p = Bukkit.getPlayer(target);
        if (getValueA1(dealer) == 21 || getValueA11(dealer) == 21)
            winGame(Reason.INSURANCE);
        else {
            GameData.getInstance().getData(target).setBetMoney(GameData.getInstance().getData(target).getBetMoney() / 2);
            ItemStack fail = InventoryUtil.createItemStack(Material.NETHER_STAR, header + "§7블랙잭이 아닙니다..ㅜ", " ", " §8-  §cInsurance로 인해 베팅액이 50% 감소 하였습니다.", " ");
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 2.0f, 1.0f);
            if (getValueA1(user) == 21 || getValueA11(user) == 21)
                winGame(Reason.BLACKJACK);
            return fail;
        }
        return new ItemStack(Material.AIR);
    }

    public void insuranceDeny() {
        Player p = Bukkit.getPlayer(target);
        if (getValueA1(dealer) == 21 || getValueA11(dealer) == 21) {
            p.getOpenInventory().setItem(40, new ItemStack(Material.AIR));
            loseGame(Reason.BLACKJACK);
        }
        else {
            ItemStack foot = InventoryUtil.createItemStack(Material.RABBIT_FOOT, header + "§c블랙잭이 아닙니다.", " ", " §8-  §f게임이 계속 진행됩니다.", " ");
            p.getOpenInventory().setItem(40, foot);
            p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
            if (getValueA1(user) == 21 || getValueA11(user) == 21)
                winGame(Reason.BLACKJACK);
        }
    }

    public void hit() {
        Player p = Bukkit.getPlayer(target);
        ItemStack userDeck;
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_HAT, 2.0f, 1.0f);
        int rand = ThreadLocalRandom.current().nextInt(0, deck.size());
        user.add(deck.get(rand));
        deck.remove(rand);
        if (getValueA1(user) == getValueA11(user) || getValueA11(user) > 21)
            userDeck = InventoryUtil.createItemStack(Material.PAPER, header + "§a당신의 패", " ", " §8-  §f" + getCard(user), " §8-  §b현재 당신의 수치 §7: §a" + getValueA1(user) + " §f점");
        else
            userDeck = InventoryUtil.createItemStack(Material.PAPER, header + "§a당신의 패", " ", " §8-  §f" + getCard(user), " §8-  §b현재 당신의 수치 §7: §a" + getValueA1(user) + " §f점 §7/ §a" + getValueA11(user) + "§f점");
        p.getOpenInventory().setItem(31, userDeck);
        if (getValueA1(user) > 21)
            loseGame(Reason.BURST);
    }

    public void doubleDown() {
        Player p = Bukkit.getPlayer(target);
        if (GameData.getInstance().getData(target).getBetMoney() > EconomyAPI.getInstance().getMoney(p))
            p.sendMessage(header + "§c돈이 부족합니다.");
        else {
            ItemStack userDeck;
            EconomyAPI.getInstance().steelMoney(p, GameData.getInstance().getData(target).getBetMoney());
            GameData.getInstance().getData(target).setBetMoney(GameData.getInstance().getData(target).getBetMoney() * 2);
            GambleLogger.getInstance().addLog(p.getName() + "님이 Double Down을 하여 배팅액을 늘렸습니다.");
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_HAT, 2.0f, 1.0f);
            int rand = ThreadLocalRandom.current().nextInt(0, deck.size());
            user.add(deck.get(rand));
            deck.remove(rand);
            if (getValueA1(user) == getValueA11(user) || getValueA11(user) > 21)
                userDeck = InventoryUtil.createItemStack(Material.PAPER, header + "§a당신의 패", " ", " §8-  §f" + getCard(user), " §8-  §b현재 당신의 수치 §7: §a" + getValueA1(user) + " §f점");
            else
                userDeck = InventoryUtil.createItemStack(Material.PAPER, header + "§a당신의 패", " ", " §8-  §f" + getCard(user), " §8-  §b현재 당신의 수치 §7: §a" + getValueA1(user) + " §f점 §7/ §a" + getValueA11(user) + "§f점");
            p.getOpenInventory().setItem(31, userDeck);
            if (getValueA1(user) > 21)
                loseGame(Reason.BURST);
            check();
        }
    }


    public void check() {
        Player p = Bukkit.getPlayer(target);
        p.playSound(p.getLocation(), Sound.BLOCK_LAVA_POP, 2.0f, 1.0f);
        for (int i = 0; i < 5; i++) {
            if (getValueA11(dealer) <= 16 || (getValueA11(dealer) > 21 && getValueA1(dealer) <= 16)) {
                int rand = ThreadLocalRandom.current().nextInt(0, deck.size());
                dealer.add(deck.get(rand));
                deck.remove(rand);
            }
        }
        int dealerVal, userVal;
        userVal = getValueA11(user) > 21 ? getValueA1(user) : getValueA11(user);
        dealerVal = getValueA11(dealer) > 21 ? getValueA1(dealer) : getValueA11(dealer);
        if (getValueA1(dealer) > 21)
            winGame(Reason.BURST);
        else if (dealerVal < userVal)
            winGame(Reason.HIGH);
        else if (dealerVal == userVal)
            winGame(Reason.SAME);
        else
            loseGame(Reason.LOW);
    }


    public void prepareDeck() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < 2; i++) {
            int rand;
            rand = random.nextInt(0, deck.size());
            dealer.add(deck.get(rand));
            deck.remove(rand);
            rand = random.nextInt(0, deck.size());
            user.add(deck.get(rand));
            deck.remove(rand);
        }
    }

    public int getValueA1(String... s) {
        int result = 0;
        try {
            for (String val : s) {
                String a = val.replace("A", "1")
                        .replace("♠", "")
                        .replace("♥", "")
                        .replace("♦", "")
                        .replace("♣", "")
                        .replace("Q", "10")
                        .replace("J", "10")
                        .replace("K", "10");
                result += Integer.parseInt(a);
            }
            return result;
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getValueA1(ArrayList<String> arr) {
        int result = 0;
        try {
            for (String val : arr) {
                String a = val.replace("A", "1")
                        .replace("♠", "")
                        .replace("♥", "")
                        .replace("♦", "")
                        .replace("♣", "")
                        .replace("Q", "10")
                        .replace("J", "10")
                        .replace("K", "10");
                result += Integer.parseInt(a);
            }
            return result;
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getValueA11(String... s) {
        int result = 0;
        try {
            for (String val : s) {
                String a = val.replace("A", "11")
                        .replace("♠", "")
                        .replace("♥", "")
                        .replace("♦", "")
                        .replace("♣", "")
                        .replace("Q", "10")
                        .replace("J", "10")
                        .replace("K", "10");
                result += Integer.parseInt(a);
            }
            return result;
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getValueA11(ArrayList<String> arr) {
        int result = 0;
        try {
            for (String val : arr) {
                String a = val.replace("A", "11")
                        .replace("♠", "")
                        .replace("♥", "")
                        .replace("♦", "")
                        .replace("♣", "")
                        .replace("Q", "10")
                        .replace("J", "10")
                        .replace("K", "10");
                result += Integer.parseInt(a);
            }
            return result;
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }
}
