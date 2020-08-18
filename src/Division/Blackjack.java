package Division;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Blackjack {
    private String st = Command.st;
    private ItemStack edge;
    private Player p;
    private ItemStack[] items;
    private ItemMeta[] metas;
    private ArrayList<String> lore;
    public Blackjack(Player p){
        edge = new ItemStack(Material.IRON_FENCE);
        ItemMeta edgemeta = edge.getItemMeta();
        edgemeta.setDisplayName(ChatColor.DARK_GRAY + "[ " + ChatColor.GRAY + "- " + ChatColor.DARK_GRAY + "]");
        edge.setItemMeta(edgemeta);
        items = new ItemStack[5];
        metas = new ItemMeta[5];
        lore = new ArrayList<>();
        this.p = p;
    }
    public void openblack(){
        int i;
        Inventory inv = Bukkit.createInventory(null, 54, st + ChatColor.BLACK + "블랙잭");
        CardHashmap.addcard("dealer", p);
        CardHashmap.addcard("dealer", p);
        CardHashmap.addcard("user", p);
        CardHashmap.addcard("user", p);
        CardHashmap.errorcheck(p);
        itemsetting();
        for (i = 0; i < 9; i++){
            inv.setItem(i, edge);
            inv.setItem(i+45, edge);
        }
        for (i = 0; i < 4; i++){
            inv.setItem((i+1)*9, edge);
            inv.setItem((i+2)*9-1, edge);
        }
        inv.setItem(13, items[0]);
        inv.setItem(29, items[1]);
        inv.setItem(31, items[2]);
        inv.setItem(33, items[3]);
        p.openInventory(inv);
        if (CardHashmap.dealer.get(p).get(0).toString().contains("A") || CardHashmap.dealer.get(p).get(0).toString().contains("Q") || CardHashmap.dealer.get(p).get(0).toString().contains("J") || CardHashmap.dealer.get(p).get(0).toString().contains("K")){
            p.getOpenInventory().setItem(40, items[4]);
            if(replacenumber(CardHashmap.user.get(p)) == 21){
                gamestop(p);
            }
        }
        else{
            if (replacenumber(CardHashmap.user.get(p)) == 21){
                Blackjackwin(p);
            }
        }
    }
    public void itemsetting(){
        int i;
        List list = Hashmap.returncards(p);
        items[0] = new ItemStack(Material.ENCHANTED_BOOK);
        items[1] = new ItemStack(Material.WOOD_BUTTON);
        items[2] = new ItemStack(Material.PAPER);
        items[3] = new ItemStack(Material.STONE_BUTTON);
        items[4] = new ItemStack(Material.REDSTONE_TORCH_ON);
        for (i = 0; i < 5; i++){
            metas[i] = items[i].getItemMeta();
        }
        metas[0].setDisplayName(st + ChatColor.AQUA + "딜러의 패");
        metas[1].setDisplayName(st + ChatColor.RED + "Stay (멈추기)");
        metas[2].setDisplayName(st + ChatColor.GREEN + "당신의 패");
        metas[3].setDisplayName(st + ChatColor.AQUA + "Hit (받기)");
        metas[4].setDisplayName(st + ChatColor.DARK_RED + "Insurance (보험)");
        lore.add(" ");
        lore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "클릭시 턴을 마칩니다.");
        lore.add(" ");
        metas[1].setLore(lore);
        lore.set(1, ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "클릭시 카드를 한장 더 받습니다.");
        metas[3].setLore(lore);
        lore.set(1, ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "클릭시 베팅액의 50%를 보험으로 듭니다.");
        lore.add(2, ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "딜러 블랙잭이 아닌경우 보험금은 제거됩니다." );
        metas[4].setLore(lore);
        List dealerlist = CardHashmap.dealer.get(p);
        lore.set(1, ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + dealerlist.get(0) + ChatColor.WHITE + " + ???");
        lore.set(2, ChatColor.DARK_GRAY + " -  " + ChatColor.RED + "현재 딜러의 수치 " + ChatColor.GRAY + ": " + ChatColor.AQUA + getnumber(dealerlist.get(0).toString()) + ChatColor.AQUA + " + ??? 점");
        metas[0].setLore(lore);
        List userlist = CardHashmap.user.get(p);
        lore.set(1, ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + userlist.get(0) + ChatColor.WHITE + ", " + ChatColor.WHITE + userlist.get(1));
        lore.set(2, ChatColor.DARK_GRAY + " -  " + ChatColor.AQUA + "현재 당신의 수치 " + ChatColor.GRAY + ": " + ChatColor.GREEN + replacenumber(userlist) + ChatColor.WHITE + " 점");
        metas[2].setLore(lore);
        for (i = 0; i < 5; i++){
            items[i].setItemMeta(metas[i]);
        }
    }
    public static int getnumber(String s){
        try{
            String a = s;
            a = a.replace("A", "11");
            a = a.replace("♠", "");
            a = a.replace("♥", "");
            a = a.replace("♦", "");
            a = a.replace("♣", "");
            a = a.replace("Q", "10");
            a = a.replace("J", "10");
            a = a.replace("K", "10");
            int value = Integer.parseInt(a);
            return value;
        } catch (NumberFormatException e){
            e.printStackTrace();
        }
        return 0;
    }
    public static int replacenumber(List list){
        int i;
        int size = list.size();
        int result = 0;
        for (i = 0; i < size; i++){
            result += getnumber(list.get(i).toString());
        }
        return result;
    }
    public static boolean insurance(Player p){
        if (p.getOpenInventory().getItem(40).getType() == Material.REDSTONE_TORCH_ON){
            return true;
        }
        return false;
    }
    public static void gamestop(Player p){
        p.getOpenInventory().setItem(29, new ItemStack(Material.AIR));
        p.getOpenInventory().setItem(33, new ItemStack(Material.AIR));
    }
    public static ItemStack insurancecheck(Player p){
        List deal = CardHashmap.dealer.get(p);
        List lore = new ArrayList();
        ItemMeta meta;
        int amount = Blackjack.replacenumber(deal);
        if (amount != 21){
            ItemStack offtorch = new ItemStack(Material.NETHER_STAR);
            meta = offtorch.getItemMeta();
            meta.setDisplayName(Command.st + ChatColor.GRAY + "블랙잭이 아닙니다..ㅜ");
            lore.add(" ");
            lore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.RED + "Insurance로 인해 베팅액이 50% 감소하였습니다.");
            lore.add(" ");
            meta.setLore(lore);
            offtorch.setItemMeta(meta);
            Hashmap.slotuse.put(p,Hashmap.getbetmoney(p)/2);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 2.0f, 1.0f);
            if (replacenumber(CardHashmap.user.get(p)) == 21){
                Blackjackwin(p);
            }
            return offtorch;
        }
        else{
            insurancewin(p);
        }
        return new ItemStack(Material.AIR);
    }
    public static void insurancewin(Player p){
        gamestop(p);
        p.getOpenInventory().setItem(13, getdealerdeck(p));
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
            @Override
            public void run() {
                if (p.getOpenInventory().getTitle().contains("블랙잭")) {
                    Main.instance.econ.depositPlayer(p, Hashmap.getbetmoney(p));
                    p.sendTitle(Command.st, ChatColor.RED + "Insurance " + ChatColor.WHITE + "성공! " + ChatColor.AQUA + "베팅액 " + ChatColor.GOLD + Hashmap.getbetmoney(p) + ChatColor.WHITE + " 원 복구!", 5, 50, 5);
                    Logger.addlog(p.getName() + "님이 블랙잭 insurance 승리로 " + Hashmap.getbetmoney(p) + "원 흭득.");
                    Hashmap.removecard(p);
                    Hashmap.removeplayer(p);
                    CardHashmap.personalclear(p);
                    p.closeInventory();
                    p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                }
            }
        },20);
    }
    public static void insurancedeny(Player p){
        if (replacenumber(CardHashmap.dealer.get(p)) == 21){
            gamestop(p);
            p.getOpenInventory().setItem(13, getdealerdeck(p));
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
                @Override
                public void run() {
                    if (p.getOpenInventory().getTitle().contains("블랙잭")) {
                        p.sendTitle(Command.st + ChatColor.DARK_RED + "딜러 블랙잭..", ChatColor.RED + "패배하셨습니다..ㅜㅜ", 5, 50, 5);
                        Hashmap.removecard(p);
                        Hashmap.removeplayer(p);
                        CardHashmap.personalclear(p);
                        p.closeInventory();
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 2.0f, 1.0f);
                    }
                }
            },20);
        }
        else{
            ArrayList<String> lore = new ArrayList();
            ItemStack foot = new ItemStack(Material.RABBIT_FOOT);
            ItemMeta meta;
            meta = foot.getItemMeta();
            meta.setDisplayName(Command.st + ChatColor.RED + "블랙잭이 아닙니다.");
            lore.add(" ");
            lore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "게임이 계속 진행됩니다.");
            lore.add(" ");
            meta.setLore(lore);
            foot.setItemMeta(meta);
            p.getOpenInventory().setItem(40, foot);
            p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
            if (replacenumber(CardHashmap.user.get(p)) == 21){
                Blackjackwin(p);
            }
        }
    }
    public static ItemStack getdealerdeck(Player p){
        //딜러 패 공개
        if (Hashmap.returncards(p) != null) {
            List list = new ArrayList();
            ItemMeta meta;
            ItemStack paper = new ItemStack(Material.ENCHANTED_BOOK);
            meta = paper.getItemMeta();
            meta.setDisplayName(Command.st + ChatColor.AQUA + "딜러의 패");
            list.add(" ");
            list.add(ChatColor.DARK_GRAY + " -  " + getcards(CardHashmap.dealer.get(p), p));
            list.add(ChatColor.DARK_GRAY + " -  " + ChatColor.RED + "현재 딜러의 수치 " + ChatColor.GRAY + ": " + ChatColor.AQUA + replacenumber(CardHashmap.dealer.get(p)) + ChatColor.WHITE + " 점");
            list.add(" ");
            meta.setLore(list);
            paper.setItemMeta(meta);
            return paper;
        }
        return new ItemStack(Material.AIR);
    }
    public static ItemStack getuserdeck(Player p){
        //유저 패 설정
        if (Hashmap.returncards(p) != null) {
            List list = new ArrayList();
            ItemMeta meta;
            ItemStack paper = new ItemStack(Material.PAPER);
            meta = paper.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + "당신의 패");
            list.add(" ");
            list.add(ChatColor.DARK_GRAY + " -  " + getcards(CardHashmap.user.get(p), p));
            list.add(ChatColor.DARK_GRAY + " -  " + ChatColor.AQUA + "현재 당신의 수치 " + ChatColor.GRAY + ": " + ChatColor.GREEN + replacenumber(CardHashmap.user.get(p)) + ChatColor.WHITE + " 점");
            list.add(" ");
            meta.setLore(list);
            paper.setItemMeta(meta);
            return paper;
        }
        return new ItemStack(Material.AIR);
    }
    public static void Blackjackwin(Player p){
        gamestop(p);
        p.getOpenInventory().setItem(13, getdealerdeck(p));
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
            @Override
            public void run() {
                if (p.getOpenInventory().getTitle().contains("블랙잭")) {
                    Main.instance.econ.depositPlayer(p, Hashmap.getbetmoney(p)*2.5);
                    Logger.addlog(p.getName() + "님이 블랙잭 승리로 " + Hashmap.getbetmoney(p)*2.5 + "원 흭득.");
                    p.sendTitle(Command.st + ChatColor.GOLD + "츽킨!", ChatColor.AQUA + "블랙잭! " + ChatColor.RED + "베팅액 " + ChatColor.GOLD + Hashmap.getbetmoney(p)*2.5 + ChatColor.WHITE + " 원 흭득!", 5, 50, 5);
                    Hashmap.removecard(p);
                    Hashmap.removeplayer(p);
                    CardHashmap.personalclear(p);
                    p.closeInventory();
                    p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                }
            }
        },20);
    }
    public static String getcards(List list, Player p){
        int i, size;
        size = list.size();
        String st = "";
        for (i = 0; i < size; i++){
            if (i+1 == size){
                st += list.get(i);
            }
            else {
                st += "" + ChatColor.WHITE + list.get(i) + ", ";
            }
        }
        return st;
    }
    public static void hitcard(String s, Player p){
        if (s.equalsIgnoreCase("dealer")){
            CardHashmap.addcard("dealer",p);
        }
        else{
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_HAT, 2.0f, 1.0f);
            CardHashmap.addcard("user",p);
            p.getOpenInventory().setItem(31, getuserdeck(p));
            if (burstcheck(CardHashmap.user.get(p))){
                burstdefeat(p);
            }
        }
    }
    public static boolean burstcheck(List list){
        if (replacenumber(list) > 21) {
            return true;
        }
        return false;
    }
    public static void burstdefeat(Player p){
        gamestop(p);
        p.getOpenInventory().setItem(13, getdealerdeck(p));
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
            @Override
            public void run() {
                if (p.getOpenInventory().getTitle().contains("블랙잭")) {
                    p.sendTitle(Command.st + ChatColor.RED + "버스트..", ChatColor.RED + "패배하셨습니다..ㅜㅜ", 5, 50, 5);
                    Hashmap.removecard(p);
                    Hashmap.removeplayer(p);
                    CardHashmap.personalclear(p);
                    p.closeInventory();
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 2.0f, 1.0f);
                }
            }
        },20);
    }
    public static void endcheck(Player p){
        p.playSound(p.getLocation(), Sound.BLOCK_LAVA_POP ,2.0f, 1.0f);
        int i;
        for (i = 0; i < 5; i++){
            if (replacenumber(CardHashmap.dealer.get(p)) <= 16){
                hitcard("dealer", p);
            }
            else{
                break;
            }
        }
        if (replacenumber(CardHashmap.dealer.get(p)) > 21){
            gamestop(p);
            p.getOpenInventory().setItem(13, getdealerdeck(p));
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
                @Override
                public void run() {
                    if (p.getOpenInventory().getTitle().contains("블랙잭")) {
                        Main.instance.econ.depositPlayer(p, Hashmap.getbetmoney(p)*2);
                        p.sendTitle(Command.st + ChatColor.GREEN + "딜러 버스트!", ChatColor.WHITE + "승리하셨습니다! " + ChatColor.RED + "베팅액 " + ChatColor.GOLD + Hashmap.getbetmoney(p)*2 + ChatColor.WHITE + " 원 흭득!", 5, 50, 5);
                        Logger.addlog(p.getName() + "님이 블랙잭 딜러 버스트 승리로 " + Hashmap.getbetmoney(p)*2 + "원 흭득.");
                        Hashmap.removecard(p);
                        Hashmap.removeplayer(p);
                        CardHashmap.personalclear(p);
                        p.closeInventory();
                        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                    }
                }
            },20);
        }
        else if (replacenumber(CardHashmap.dealer.get(p)) < (replacenumber(CardHashmap.user.get(p)))){
            gamestop(p);
            p.getOpenInventory().setItem(13, getdealerdeck(p));
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
                @Override
                public void run() {
                    if (p.getOpenInventory().getTitle().contains("블랙잭")) {
                        Main.instance.econ.depositPlayer(p, Hashmap.getbetmoney(p)*2);
                        p.sendTitle(Command.st + ChatColor.AQUA + "승리!", ChatColor.WHITE + "딜러보다 수치가 높습니다! " + ChatColor.RED + "베팅액 " + ChatColor.GOLD + Hashmap.getbetmoney(p)*2 + ChatColor.WHITE + " 원 흭득!", 5, 50, 5);
                        Logger.addlog(p.getName() + "님이 블랙잭 일반 승리로 " + Hashmap.getbetmoney(p)*2 + "원 흭득.");
                        Hashmap.removecard(p);
                        Hashmap.removeplayer(p);
                        CardHashmap.personalclear(p);
                        p.closeInventory();
                        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                    }
                }
            },20);
        }
        else if (replacenumber(CardHashmap.dealer.get(p)) == (replacenumber(CardHashmap.user.get(p)))){
            gamestop(p);
            p.getOpenInventory().setItem(13, getdealerdeck(p));
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
                @Override
                public void run() {
                    if (p.getOpenInventory().getTitle().contains("블랙잭")) {
                        Main.instance.econ.depositPlayer(p, Hashmap.getbetmoney(p));
                        p.sendTitle(Command.st + ChatColor.YELLOW + "비겼습니다!", ChatColor.WHITE + "딜러와 수치가 같습니다. " + ChatColor.RED + "베팅액 " + ChatColor.GOLD + Hashmap.getbetmoney(p) + ChatColor.WHITE + " 원 흭득!", 5, 50, 5);
                        Logger.addlog(p.getName() + "님이 블랙잭에서 비겨서 " + Hashmap.getbetmoney(p) + "원 흭득.");
                        Hashmap.removecard(p);
                        Hashmap.removeplayer(p);
                        CardHashmap.personalclear(p);
                        p.closeInventory();
                        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                    }
                }
            },20);
        }
        else{
            gamestop(p);
            p.getOpenInventory().setItem(13, getdealerdeck(p));
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
                @Override
                public void run() {
                    if (p.getOpenInventory().getTitle().contains("블랙잭")) {
                        p.sendTitle(Command.st + ChatColor.RED + "패배하셨습니다..", ChatColor.RED + "딜러보다 수치가 낮습니다.ㅜ", 5, 50, 5);
                        Hashmap.removecard(p);
                        Hashmap.removeplayer(p);
                        CardHashmap.personalclear(p);
                        p.closeInventory();
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 2.0f, 1.0f);
                    }
                }
            },20);
        }
    }
}
