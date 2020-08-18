package Division;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Slotmechnism {

    private int a,b,c;
    private Player p;
    private Main instance;
    private String st = Command.st;
    private int k = 0;
    private int taskID = 0;
    public Slotmechnism(Player p){

        this.p = p;
        this.instance = JavaPlugin.getPlugin(Main.class);
    }
    public int getint(ItemStack item) {
        if (item.getType() == Material.NETHER_STAR) {
            return 0;
        } else if (item.getType() == Material.APPLE) {
            return 1;
        } else if (item.getType() == Material.PORK) {
            return 2;
        } else if (item.getType() == Material.GOLDEN_APPLE) {
            return 3;
        } else if (item.getType() == Material.CAKE) {
            return 4;
        } else if (item.getType() == Material.COOKIE) {
            return 5;
        } else if (item.getType() == Material.MELON) {
            return 6;
        } else if (item.getType() == Material.BEETROOT) {
            return 7;
        } else if (item.getType() == Material.POTATO_ITEM) {
            return 8;
        } else if (item.getType() == Material.EGG) {
            return 9;
        }
        return 0;
    }
    public void slotcheck(int a, int b, int c){
        if (a == b && b == c){
            String getmoney = String.format("%.2f",Main.slotaccumoney*7/10);
            Hashmap.removeplayer(p);
            p.closeInventory();
            Bukkit.getServer().broadcastMessage(" ");
            Bukkit.getServer().broadcastMessage(st + ChatColor.AQUA + p.getName() + ChatColor.WHITE +"님께서 " + ChatColor.GOLD + "잭팟" + ChatColor.WHITE + "을 터트렸습니다!!! " + ChatColor.GREEN + "흭득금액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + getmoney + "원");
            Bukkit.getServer().broadcastMessage(" ");
            Logger.addlog(p.getName() + "님이 잭팟으로 " + getmoney + "원 흭득");
            instance.econ.depositPlayer(p, Float.parseFloat(getmoney));
            Main.slotaccumoney = Main.slotaccumoney*3/10;
      }
        else{
            Hashmap.removeplayer(p);
            p.sendTitle(st, ChatColor.AQUA + "아쉽게도 잭팟이 터지지 않았습니다..ㅜㅜ", 5, 50, 5);
      }
    }
    public int flow(int a) {
        if (a + 1 == 10) {
            return 0;
        } else {
            return a + 1;
        }
    }
    public void rollslot(){
        if (instance.econ.getBalance(p) < Main.slotneed){
            p.sendMessage(st + ChatColor.RED + "금액이 부족합니다.." + ChatColor.DARK_GRAY + " [ " +ChatColor.GOLD + Main.slotneed + ChatColor.WHITE + "원 " + ChatColor.DARK_GRAY + "]" );
        }
        else {
            int i;

            Hashmap.addplayer(p, Main.slotneed);
            a = getint(this.p.getOpenInventory().getItem(12));
            b = getint(this.p.getOpenInventory().getItem(13));
            c = getint(this.p.getOpenInventory().getItem(14));
            final int firststop = (int)(Math.random()*30+40);
            final int secondstop = (int)(Math.random()*30+70);
            final int laststop = (int)(Math.random()*30+100);
            instance.econ.withdrawPlayer(p, Main.slotneed);
            Main.slotaccumoney += Main.slotneed;
            String getmoney = String.format("%.2f",Main.slotaccumoney);
            List list = Slot.wool.getItemMeta().getLore();
            list.set(1, ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "현재 " + ChatColor.RED + "슬롯머신 " + ChatColor.WHITE + "에 누적된 " + ChatColor.GOLD + "금액" + ChatColor.WHITE + "은 " +ChatColor.AQUA + getmoney + ChatColor.WHITE +"원 입니다.");
            ItemMeta meta = Slot.wool.getItemMeta();
            meta.setLore(list);
            Slot.wool.setItemMeta(meta);
            for (i = 0; i < 3; i++){
                p.getOpenInventory().setItem(i+3, Slot.wool);
                p.getOpenInventory().setItem(i+21, Slot.wool);
            }
            taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
                @Override
                public void run() {
                    if (!p.getOpenInventory().getTitle().contains("슬롯머신")){
                        Bukkit.getScheduler().cancelTask(taskID);
                    }
                    else if (k < firststop) {
                        a = flow(a);
                        b = flow(b);
                        c = flow(c);
                    }
                    else if (k == firststop){
                        b = flow(b);
                        c = flow(c);
                        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 3.0f, 1.0f);
                    }
                    else if (k > firststop && k < secondstop){
                        b = flow(b);
                        c = flow(c);
                    }
                    else if (k == secondstop){
                        c = flow(c);
                        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 3.0f, 1.0f);
                    }
                    else if (k > secondstop && k < laststop){
                        c = flow(c);
                    }
                    else{
                        Bukkit.getScheduler().cancelTask(taskID);
                        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 3.0f, 1.0f);
                        slotcheck(a,b,c);
                    }
                    if (p.getOpenInventory().getTitle().contains("슬롯머신")) {
                        p.getOpenInventory().setItem(12, Slot.slotitem[a]);
                        p.getOpenInventory().setItem(13, Slot.slotitem[b]);
                        p.getOpenInventory().setItem(14, Slot.slotitem[c]);
                        k++;
                    }
                }
            },0L, 1L);

        }
        }
    }

