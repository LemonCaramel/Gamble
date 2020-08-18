package Division;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;


public class Slot {
    public static ItemStack edge;
    public static ItemStack wool;
    private ItemStack lever;
    private ItemMeta meta;
    private ItemMeta woolmeta;
    private ItemMeta levermeta;
    public static ItemStack[] slotitem;
    private ItemMeta[] slotitemmeta;
    private List lore;
    private List itemlore;
    private List leverlore;
    private double accumoney;
    private int gamemoney;
    private static String st = Command.st;
    public Slot(){
        String getmoney = String.format("%.2f",Main.slotaccumoney);
        this.slotitem = new ItemStack[10];
        this.slotitemmeta = new ItemMeta[10];
        accumoney = Main.slotaccumoney;
        gamemoney = Main.slotneed;
        this.lever = new ItemStack(Material.LEVER);
        this.levermeta = this.lever.getItemMeta();
        this.lore = new ArrayList();
        this.itemlore = new ArrayList();
        this.leverlore = new ArrayList();
        this.edge = new ItemStack(Material.IRON_FENCE);
        this.wool = new ItemStack(Material.WOOL, 1, (short)15);
        this.levermeta.setDisplayName(st + ChatColor.AQUA + "슬롯머신 레버");
        this.leverlore.add(" ");
        this.leverlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "클릭시 슬롯머신을 돌립니다." + ChatColor.DARK_GRAY + " ( " + ChatColor.WHITE + "1회당 "+ ChatColor.AQUA + gamemoney + ChatColor.WHITE + "원" + ChatColor.DARK_GRAY + " )");
        this.leverlore.add(" ");
        this.levermeta.setLore(leverlore);
        this.lever.setItemMeta(levermeta);
        this.meta = this.edge.getItemMeta();
        this.woolmeta = wool.getItemMeta();
        this.meta.setDisplayName(ChatColor.DARK_GRAY +"[ "+ ChatColor.GRAY + "- " + ChatColor.DARK_GRAY +"]");
        this.woolmeta.setDisplayName(st + ChatColor.AQUA + " 현재 누적 금액");
        this.lore.add(" ");
        this.lore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "현재 " + ChatColor.RED + "슬롯머신 " + ChatColor.WHITE + "에 누적된 " + ChatColor.GOLD + "금액" + ChatColor.WHITE + "은 " +ChatColor.AQUA + getmoney + ChatColor.WHITE +"원 입니다.");
        this.lore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "일확천금의 기회를 노려보세요!");
        this.lore.add(" ");
        this.woolmeta.setLore(this.lore);
        this.edge.setItemMeta(this.meta);
        this.wool.setItemMeta(this.woolmeta);
        slotitemset();
    }
    public void createslot(Player p){
        Inventory slotinv = Bukkit.createInventory(null, 27, st + ChatColor.DARK_GRAY + "슬롯머신");
        int i,a,b,c;
        for (i = 0; i < 3; i++) {
            slotinv.setItem(i, this.edge);
            slotinv.setItem(i+9, this.edge);
            slotinv.setItem(i+18, this.edge);
            slotinv.setItem(i+6, this.edge);
            slotinv.setItem(i+15, this.edge);
            slotinv.setItem(i+24, this.edge);
            slotinv.setItem(i+3, this.wool);
            slotinv.setItem(i+21, this.wool);
        }
        a = (int)(Math.random()*10);
        b = (int)(Math.random()*10);
        c = (int)(Math.random()*10);
        slotinv.setItem(12, slotitem[a]);
        slotinv.setItem(13, slotitem[b]);
        slotinv.setItem(14, slotitem[c]);
        slotinv.setItem(16, lever);
        p.openInventory(slotinv);
    }
    public void slotitemset(){
        int i;
        this.slotitem[0] = new ItemStack(Material.NETHER_STAR);
        this.slotitem[1] = new ItemStack(Material.APPLE);
        this.slotitem[2] = new ItemStack(Material.PORK);
        this.slotitem[3] = new ItemStack(Material.GOLDEN_APPLE);
        this.slotitem[4] = new ItemStack(Material.CAKE);
        this.slotitem[5] = new ItemStack(Material.COOKIE);
        this.slotitem[6] = new ItemStack(Material.MELON);
        this.slotitem[7] = new ItemStack(Material.BEETROOT);
        this.slotitem[8] = new ItemStack(Material.POTATO_ITEM);
        this.slotitem[9] = new ItemStack(Material.EGG);
        for (i = 0; i < 10; i++){
            this.slotitemmeta[i] = this.slotitem[i].getItemMeta();
        }
        this.itemlore.add(" ");
        this.itemlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "3개의 아이템이 모두 같으면 누적된 금액의 70%를 가져갑니다!");
        this.itemlore.add(" ");
        this.slotitemmeta[0].setDisplayName(st + ChatColor.WHITE + "네더의 별");
        this.slotitemmeta[1].setDisplayName(st + ChatColor.RED + "사과");
        this.slotitemmeta[2].setDisplayName(st + ChatColor.YELLOW + "돼지고기");
        this.slotitemmeta[3].setDisplayName(st + ChatColor.GOLD + "황금사과");
        this.slotitemmeta[4].setDisplayName(st + ChatColor.WHITE + "케이크");
        this.slotitemmeta[5].setDisplayName(st + ChatColor.GOLD + "쿠키");
        this.slotitemmeta[6].setDisplayName(st + ChatColor.GREEN + "멜론");
        this.slotitemmeta[7].setDisplayName(st + ChatColor.DARK_RED + "사탕무");
        this.slotitemmeta[8].setDisplayName(st + ChatColor.GOLD + "감자");
        this.slotitemmeta[9].setDisplayName(st + ChatColor.YELLOW + "달걀");
        for (i = 0; i < 10; i++){
            this.slotitemmeta[i].setLore(itemlore);
            this.slotitem[i].setItemMeta(slotitemmeta[i]);
        }

    }
}
