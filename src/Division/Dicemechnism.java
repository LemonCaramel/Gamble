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

public class Dicemechnism {
    private Player p;
    private Inventory inv;
    private static String st;
    private ItemStack edge;
    private ItemMeta meta;
    private ItemStack[] items;
    private ItemMeta[] itemmeta;
    private List lore;
    public Dicemechnism(Player p){
        st = Command.st;
        this.p = p;
        this.edge = new ItemStack(Material.IRON_FENCE);
        this.meta = this.edge.getItemMeta();
        this.meta.setDisplayName(ChatColor.DARK_GRAY + "[ " + ChatColor.GRAY + "- " + ChatColor.DARK_GRAY + "]");
        this.edge.setItemMeta(this.meta);
        this.inv = Bukkit.createInventory(null, 54, st + ChatColor.RED + "주사위 도박 " + ChatColor.GRAY + ": " + ChatColor.BLACK + "진행중");
        items = new ItemStack[3];
        itemmeta = new ItemMeta[3];
        lore = new ArrayList();
    }
    public void dicecreate(int a){
        edgesetting(a);
        Main.instance.econ.withdrawPlayer(p, Hashmap.getbetmoney(p));
        p.openInventory(inv);
    }
    public void edgesetting(int a){
        int i, amount;
        for (i = 0; i < 9; i++){
            inv.setItem(i, edge);
            inv.setItem(i+45, edge);
        }
        for (i = 0; i < 4; i++){
            inv.setItem((i+1)*9, edge);
            inv.setItem((i+2)*9-1, edge);
        }
        items[0] = new ItemStack(Material.ENCHANTED_BOOK);
        items[1] = new ItemStack(Material.GOLD_BLOCK);
        items[2] = new ItemStack(Material.GOLD_BLOCK);
        for (i = 0; i < 3; i++){
            itemmeta[i] = items[i].getItemMeta();
        }
        amount = Hashmap.getbetmoney(p) + (int)((Math.random()+0.5)*Hashmap.getbetmoney(p));
        if (a == 19) {
            itemmeta[0].setDisplayName(st + ChatColor.AQUA + "주사위 도박 " + ChatColor.DARK_GRAY + "[ " + ChatColor.YELLOW + "로우" + ChatColor.DARK_GRAY + " ]");
            lore.add(" ");
            lore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "숫자가 낮을수록 유리합니다");
            lore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "현재 총 베팅금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + amount + ChatColor.WHITE + "원");
            lore.add(" ");
        }
        else if (a == 22){
            itemmeta[0].setDisplayName(st + ChatColor.AQUA + "주사위 도박 " + ChatColor.DARK_GRAY + "[ " + ChatColor.GOLD + "미드" + ChatColor.DARK_GRAY + " ]");
            lore.add(" ");
            lore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "숫자가 50에 가까울수록 유리합니다");
            lore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "현재 총 베팅금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + amount + ChatColor.WHITE + "원");
            lore.add(" ");
        }
        else{
            itemmeta[0].setDisplayName(st + ChatColor.AQUA + "주사위 도박 " + ChatColor.DARK_GRAY + "[ " + ChatColor.RED + "하이" + ChatColor.DARK_GRAY + " ]");
            lore.add(" ");
            lore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "숫자가 높을수록 유리합니다");
            lore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "현재 총 베팅금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + amount + ChatColor.WHITE + "원");
            lore.add(" ");
        }
        itemmeta[0].setLore(lore);
        itemmeta[1].setDisplayName(st + ChatColor.AQUA + p.getName() + ChatColor.WHITE + "의 " + ChatColor.GOLD + "주사위");
        itemmeta[2].setDisplayName(st + ChatColor.RED + "컴퓨터" + ChatColor.WHITE + "의 " + ChatColor.GOLD + "주사위");
        lore.remove(2);
        lore.set(1, ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "클릭시 주사위를 던집니다.");
        itemmeta[1].setLore(lore);
        itemmeta[2].setLore(lore);
        for (i = 0; i < 3; i++){
            items[i].setItemMeta(itemmeta[i]);
        }
        inv.setItem(13, items[0]);
        inv.setItem(29, items[1]);
        inv.setItem(33, items[2]);
    }
    public static ItemStack dicecheck(Player p, ItemStack item){
        if (item.getType() == Material.GOLD_BLOCK) {
            int random;
            ItemStack newitem = item;
            ItemMeta temp = newitem.getItemMeta();
            List lores = temp.getLore();
            random = (int) (Math.random() * 98 + 1);
            lores.set(1, ChatColor.DARK_GRAY + " -  " + ChatColor.GOLD + "주사위 " + ChatColor.WHITE + "눈 " + ChatColor.AQUA + random + ChatColor.WHITE + "이 나왔습니다!");
            temp.setLore(lores);
            newitem.setItemMeta(temp);
            newitem.setType(Material.PAPER);
            wincheck(p);
            return newitem;
        }
        return null;
    }
    public static void wincheck(Player p){
        List money = p.getOpenInventory().getItem(13).getItemMeta().getLore();
        String[] role = p.getOpenInventory().getItem(13).getItemMeta().getDisplayName().split("주사위 도박");
        String getmoney[] = money.get(2).toString().split(":");
        getmoney[1] = ChatColor.stripColor(getmoney[1]);
        role[1] = ChatColor.stripColor(role[1]);
        role[1] = role[1].replace("[", "");
        role[1] = role[1].replace("]", "");
        role[1] = role[1].replace(" ", "");
        //role[1]은 룰, getmoney[1]은 총 베팅액
        getmoney[1] = getmoney[1].replace(" ", "");
        getmoney[1] = getmoney[1].replace("원", "");
        int finalmoney = Integer.parseInt(getmoney[1]);
        //finalmoney로 파싱
        if (p.getOpenInventory().getItem(29).getType() == Material.PAPER && p.getOpenInventory().getItem(33).getType() == Material.PAPER){
            int playernum = getnumber(p.getOpenInventory().getItem(29));
            int com = getnumber(p.getOpenInventory().getItem(33));
            if (role[1].equals("로우")){
                //룰 - 로우
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
                    @Override
                    public void run() {
                        if (playernum < com){
                            p.closeInventory();
                            Hashmap.removeplayer(p);
                            p.sendTitle(st, ChatColor.RED + "게임" + ChatColor.WHITE + "에서 승리하여 " + ChatColor.GOLD + finalmoney + ChatColor.WHITE +"원을 흭득하셨습니다!", 5, 50, 5);
                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.0f,1.0f);
                            Main.instance.econ.depositPlayer(p, finalmoney);
                            Logger.addlog(p.getName() + "님이 주사위 도박 - 로우 에서 승리하여 " + finalmoney + "원 흭득.");
                        }
                        else if (playernum == com){
                            p.closeInventory();
                            Hashmap.removeplayer(p);
                            p.sendTitle(st, ChatColor.RED + "게임" + ChatColor.WHITE + "에서 비겨서 " + ChatColor.GOLD + (finalmoney/2) + ChatColor.WHITE +"원을 흭득하셨습니다!", 5, 50, 5);
                            p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f,1.0f);
                            Main.instance.econ.depositPlayer(p, finalmoney/2);
                            Logger.addlog(p.getName() + "님이 주사위 도박 - 로우 에서 비겨서 " + finalmoney/2 + "원 흭득.");
                        }
                        else{
                            p.closeInventory();
                            Hashmap.removeplayer(p);
                            p.sendTitle(st, ChatColor.RED + "게임에서 패배하셨습니다..ㅜㅜ",5,50, 5);
                        }
                    }
                }, 20);
            }
            else if (role[1].equals("미드")){
                //룰 - 미드
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
                    @Override
                    public void run() {
                        if (Math.abs(50-playernum) < Math.abs(50-com)){
                            p.closeInventory();
                            Hashmap.removeplayer(p);
                            p.sendTitle(st, ChatColor.RED + "게임" + ChatColor.WHITE + "에서 승리하여 " + ChatColor.GOLD + finalmoney + ChatColor.WHITE +"원을 흭득하셨습니다!", 5, 50, 5);
                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.0f,1.0f);
                            Main.instance.econ.depositPlayer(p, finalmoney);
                            Logger.addlog(p.getName() + "님이 주사위 도박 - 미드 에서 승리하여 " + finalmoney + "원 흭득.");
                        }
                        else if (Math.abs(50-playernum) == Math.abs(50-com)){
                            p.closeInventory();
                            Hashmap.removeplayer(p);
                            p.sendTitle(st, ChatColor.RED + "게임" + ChatColor.WHITE + "에서 비겨서 " + ChatColor.GOLD + (finalmoney/2) + ChatColor.WHITE +"원을 흭득하셨습니다!", 5, 50, 5);
                            p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f,1.0f);
                            Main.instance.econ.depositPlayer(p, finalmoney/2);
                            Logger.addlog(p.getName() + "님이 주사위 도박 - 로우 에서 비겨서 " + finalmoney/2 + "원 흭득.");
                        }
                        else{
                            p.closeInventory();
                            Hashmap.removeplayer(p);
                            p.sendTitle(st, ChatColor.RED + "게임에서 패배하셨습니다..ㅜㅜ",5,50, 5);
                        }
                    }
                }, 20);
            }
            else{
                //룰 - 하이
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
                    @Override
                    public void run() {
                        if (playernum > com){
                            p.closeInventory();
                            Hashmap.removeplayer(p);
                            p.sendTitle(st, ChatColor.RED + "게임" + ChatColor.WHITE + "에서 승리하여 " + ChatColor.GOLD + finalmoney + ChatColor.WHITE +"원을 흭득하셨습니다!", 5, 50, 5);
                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.0f,1.0f);
                            Main.instance.econ.depositPlayer(p, finalmoney);
                            Logger.addlog(p.getName() + "님이 주사위 도박 - 하이 에서 승리하여 " + finalmoney + "원 흭득.");
                        }
                        else if (playernum == com){
                            p.closeInventory();
                            Hashmap.removeplayer(p);
                            p.sendTitle(st, ChatColor.RED + "게임" + ChatColor.WHITE + "에서 비겨서 " + ChatColor.GOLD + (finalmoney/2) + ChatColor.WHITE +"원을 흭득하셨습니다!", 5, 50, 5);
                            p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f,1.0f);
                            Main.instance.econ.depositPlayer(p, finalmoney/2);
                            Logger.addlog(p.getName() + "님이 주사위 도박 - 하이 에서 비겨서 " + finalmoney/2 + "원 흭득.");
                        }
                        else{
                            p.closeInventory();
                            Hashmap.removeplayer(p);
                            p.sendTitle(st, ChatColor.RED + "게임에서 패배하셨습니다..ㅜㅜ",5,50, 5);
                        }
                    }
                }, 20);
            }
        }
    }
    public static int getnumber(ItemStack a){
        List getlore = a.getItemMeta().getLore();
        String[] lore = getlore.get(1).toString().split("눈");
        lore[1] = ChatColor.stripColor(lore[1]);
        lore[1] = lore[1].replace("이 나왔습니다!", "");
        lore[1] = lore[1].replace(" ", "");
        int result = Integer.parseInt(lore[1]);
        return result;
    }
}

