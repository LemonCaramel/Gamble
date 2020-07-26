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
import java.util.UUID;

public class indianGui {

    public Inventory inv;
    public Player p;
    public ItemStack edge, deck;


    public indianGui(Player p){
        this.p = p;
        createInv();
        p.openInventory(inv);
    }

    public void createInv(){
        ItemMeta deck1;
        List lore1;
        inv = Bukkit.createInventory(null, 54, Command.st + ChatColor.BLACK + "인디언포커");
        edge = new ItemStack(Material.IRON_FENCE);
        ItemMeta meta = edge.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GRAY + "[ " + ChatColor.GRAY + "- " + ChatColor.DARK_GRAY + "]");
        edge.setItemMeta(meta);
        for (int i = 0; i < 9; i++){
            inv.setItem(i, edge);
            inv.setItem(45+i,edge);
        }
        for (int i = 1; i <= 4; i++){
            inv.setItem(9*i, edge);
            inv.setItem(9*(i+1)-1, edge);
        }
        deck = new ItemStack(Material.CHEST);
        deck1 = deck.getItemMeta();
        deck1.setDisplayName(Command.st + ChatColor.AQUA + "패 확인");
        lore1 = new ArrayList();
        lore1.add(" ");
        lore1.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "클릭해서 패를 확인하세요 .");
        lore1.add(" ");
        deck1.setLore(lore1);
        deck.setItemMeta(deck1);
        inv.setItem(31,deck);
        if (Hashmap.indian.get(p.getUniqueId().toString()) == null){
            //자신이 초대 당한경우
            Player target = Bukkit.getPlayer(UUID.fromString(Hashmap.getrequester(p.getUniqueId().toString())));
            ItemStack red,bet,die;
            ItemMeta red1,bet1,die1;
            List redlore,betlore,dielore;
            red = new ItemStack(Material.REDSTONE_BLOCK);
            bet = new ItemStack(Material.WOOD_BUTTON);
            die = new ItemStack(Material.STONE_BUTTON);
            red1 = red.getItemMeta();
            bet1 = bet.getItemMeta();
            die1 = die.getItemMeta();
            red1.setDisplayName(Command.st + ChatColor.RED + "상대방의 차례 입니다.");
            redlore = new ArrayList();
            redlore.add(" ");
            redlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "상대방의 현재 베팅액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Hashmap.getbetmoney(target) + "원");
            redlore.add(" ");
            red1.setLore(redlore);
            red.setItemMeta(red1);
            bet1.setDisplayName(Command.st + ChatColor.RED + "베팅(Bet)");
            betlore = new ArrayList();
            betlore.add(" ");
            betlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.RED + "현재 당신의 베팅액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Hashmap.getbetmoney(p) + "원");
            betlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.AQUA + "현재 상대의 베팅액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Hashmap.getbetmoney(target) + "원");
            betlore.add(" ");
            bet1.setLore(betlore);
            bet.setItemMeta(bet1);
            die1.setDisplayName(Command.st + ChatColor.AQUA + "다이(Die)");
            dielore = new ArrayList();
            dielore.add(" ");
            dielore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "클릭시 게임을 포기합니다. (현재 사용불가)");
            dielore.add(" ");
            die1.setLore(dielore);
            die.setItemMeta(die1);
            inv.setItem(13, red);
            inv.setItem(29, bet);
            inv.setItem(33, die);
        }
        else{
            //자신이 초대한 경우
            Player target = Bukkit.getPlayer(UUID.fromString(Hashmap.indian.get(p.getUniqueId().toString())));
            ItemStack dia,bet,die;
            ItemMeta dia1,bet1,die1;
            List dialore,betlore,dielore;
            dia = new ItemStack(Material.DIAMOND_BLOCK);
            bet = new ItemStack(Material.WOOD_BUTTON);
            die = new ItemStack(Material.STONE_BUTTON);
            dia1 = dia.getItemMeta();
            bet1 = bet.getItemMeta();
            die1 = die.getItemMeta();
            dia1.setDisplayName(Command.st + ChatColor.AQUA + "당신의 차례 입니다!");
            dialore = new ArrayList();
            dialore.add(" ");
            dialore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "베팅 혹은 다이를 선택해주세요.");
            dialore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "상대방의 현재 베팅액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Hashmap.getbetmoney(target) + "원");
            dialore.add(" ");
            dia1.setLore(dialore);
            dia.setItemMeta(dia1);
            bet1.setDisplayName(Command.st + ChatColor.RED + "베팅(Bet)");
            betlore = new ArrayList();
            betlore.add(" ");
            betlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.RED + "현재 당신의 베팅액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Hashmap.getbetmoney(p) + "원");
            betlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.AQUA + "현재 상대의 베팅액 " + ChatColor.GRAY + ": " + ChatColor.GOLD + Hashmap.getbetmoney(target) + "원");
            betlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.RED + "좌클릭시 " + ChatColor.GOLD + (int)((double)Hashmap.indiannmax.get(p.getUniqueId().toString())/10) +  ChatColor.WHITE + "원을 베팅합니다.");
            betlore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.GREEN + "우클릭시 베팅을 종료합니다. 단 상대방의 베팅액보다 크거나 같아야 합니다.");
            betlore.add(" ");
            bet1.setLore(betlore);
            bet.setItemMeta(bet1);
            die1.setDisplayName(Command.st + ChatColor.AQUA + "다이(Die)");
            dielore = new ArrayList();
            dielore.add(" ");
            dielore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "클릭시 게임을 포기합니다.");
            dielore.add(" ");
            die1.setLore(dielore);
            die.setItemMeta(die1);
            inv.setItem(13, dia);
            inv.setItem(29, bet);
            inv.setItem(33, die);
        }
    }

    public static ItemStack checkdeck(){
        ItemStack deck;
        ItemMeta deck1;
        List lore;
        int cardid;
        deck = new ItemStack(Material.PAPER);
        deck1 = deck.getItemMeta();
        cardid = (int)(Math.random()*10+1);
        deck1.setDisplayName(Command.st + ChatColor.GOLD + cardid);
        lore = new ArrayList();
        lore.add(" ");
        lore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "상대방의 패 입니다.");
        lore.add(ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "상대방의 숫자가 낮을수록 유리하며 숫자가 같을경우 무승부입니다.");
        lore.add(" ");
        deck1.setLore(lore);
        deck.setItemMeta(deck1);
        return deck;
    }
}
