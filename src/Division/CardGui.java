package Division;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.UUID;

public class CardGui {

    public Inventory inv;
    public Player p;
    public ItemStack edge, sign, note;
    public ItemMeta signm,notem;

    public CardGui(Player p){
        this.p = p;
        createInv();
        p.openInventory(inv);
        if (Hashmap.cardrequest.get(p.getUniqueId().toString()) == null){
            //자신이 초대 당한경우
            setstatus(p,false);
        }
        else{
            //자신이 초대한 경우
            setstatus(p,true);
        }
    }

    public void createInv(){
        inv = Bukkit.createInventory(null, 54, Command.st + ChatColor.BLACK + "카드 도박");
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
        sign = new ItemStack(Material.SIGN);
        signm = sign.getItemMeta();
        signm.setDisplayName(Command.st + ChatColor.AQUA + "카드 도박 베팅액");
        signm.setLore(Arrays.asList(" ", ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "현재 베팅액은 " + ChatColor.RED + Hashmap.getbetmoney(p) + ChatColor.WHITE + " 원 입니다.", " "));
        sign.setItemMeta(signm);
        inv.setItem(4, sign);
        note = new ItemStack(Material.NOTE_BLOCK);
        notem = note.getItemMeta();
        notem.setDisplayName(Command.st + ChatColor.AQUA + "소리 내기");
        notem.setLore(Arrays.asList(" ", ChatColor.DARK_GRAY +  " -  " + ChatColor.WHITE + "소리로 주의를 끌어봅시다!", ChatColor.DARK_GRAY + " -  " + ChatColor.RED + "과도한 어그로는 손절을 유발합니다..", " "));
        note.setItemMeta(notem);
        inv.setItem(49, note);


    }

    public static void setstatus(Player p, boolean bool){
        if (!bool){
            //사용불가능
            ItemStack chest = new ItemStack(Material.RED_SHULKER_BOX);
            ItemMeta chestm = chest.getItemMeta();
            chestm.setDisplayName(Command.st + ChatColor.RED + "현재 상대의 차례입니다.");
            chest.setItemMeta(chestm);
            p.getOpenInventory().setItem(22, chest);
            p.getOpenInventory().setItem(38, new ItemStack(Material.AIR));
            p.getOpenInventory().setItem(40, new ItemStack(Material.AIR));
            p.getOpenInventory().setItem(42, new ItemStack(Material.AIR));
        }
        else{
            //사용불가능
            ItemStack chest = new ItemStack(Material.GREEN_SHULKER_BOX);
            ItemMeta chestm = chest.getItemMeta();
            chestm.setDisplayName(Command.st + ChatColor.RED + "현재 당신의 차례입니다.");
            chest.setItemMeta(chestm);
            p.getOpenInventory().setItem(22, chest);
            ItemStack king = new ItemStack(Material.BOOK);
            ItemMeta kingm = king.getItemMeta();
            kingm.setDisplayName(Command.st + ChatColor.RED + "King(왕)");
            kingm.setLore(Arrays.asList(" ", ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "클릭시 King(왕) 카드를 선택합니다.", " "));
            king.setItemMeta(kingm);
            ItemStack citizen = new ItemStack(Material.BOOK);
            ItemMeta citizenm = citizen.getItemMeta();
            citizenm.setDisplayName(Command.st + ChatColor.AQUA + "Citizen(시민)");
            citizenm.setLore(Arrays.asList(" ", ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "클릭시 Citizen(시민) 카드를 선택합니다.", " "));
            citizen.setItemMeta(citizenm);
            ItemStack slave = new ItemStack(Material.BOOK);
            ItemMeta slavem = slave.getItemMeta();
            slavem.setDisplayName(Command.st + ChatColor.GRAY + "Slave(노예)");
            slavem.setLore(Arrays.asList(" ", ChatColor.DARK_GRAY + " -  " + ChatColor.WHITE + "클릭시 Slave(노예) 카드를 선택합니다.", " "));
            slave.setItemMeta(slavem);
            p.getOpenInventory().setItem(22, chest);
            p.getOpenInventory().setItem(38, king);
            p.getOpenInventory().setItem(40, citizen);
            p.getOpenInventory().setItem(42, slave);
        }
    }
    public static void check(Player p){
        boolean win, draw;
        draw = false;
        win = false;
        ItemStack onestack, ownerstack;
        ItemMeta onemeta, ownermeta;
        Player owner = Bukkit.getServer().getPlayer(UUID.fromString(Hashmap.getcardrequester(p.getUniqueId().toString())));
        p.getOpenInventory().setItem(22, new ItemStack(Material.AIR));
        p.getOpenInventory().setItem(38, new ItemStack(Material.AIR));
        p.getOpenInventory().setItem(40, new ItemStack(Material.AIR));
        p.getOpenInventory().setItem(42, new ItemStack(Material.AIR));
        owner.getOpenInventory().setItem(22, new ItemStack(Material.AIR));
        Character ownerdeck, onesdeck;
        ownerdeck = Hashmap.cardsel.get(owner.getUniqueId().toString());
        onesdeck = Hashmap.cardsel.get(p.getUniqueId().toString());
        if (ownerdeck == null || onesdeck == null){
            p.sendMessage(Command.st + ChatColor.WHITE + "게임도중 오류가 발생하여 강제종료 되었습니다.");
            Logger.addlog("카드 도박 오류발생");
            p.closeInventory();
            owner.closeInventory();
        }
        else{
            switch (ownerdeck){
                case 'K':
                    if (onesdeck == 'K')
                        draw = true;
                    else if (onesdeck == 'C')
                        win = false;
                    else
                        win = true;
                    ownerstack = new ItemStack(Material.BOOK);
                    ownermeta = ownerstack.getItemMeta();
                    ownermeta.setDisplayName(Command.st + ChatColor.RED + "King(왕)");
                    ownermeta.setLore(Arrays.asList(" ", ChatColor.DARK_GRAY + " -  " + ChatColor.RED + owner.getName() + ChatColor.WHITE + " 의 카드입니다.", " "));
                    ownerstack.setItemMeta(ownermeta);
                    break;
                case 'C':
                    if (onesdeck == 'K')
                        win = true;
                    else if (onesdeck == 'C')
                        draw = true;
                    else
                        win = false;
                    ownerstack = new ItemStack(Material.BOOK);
                    ownermeta = ownerstack.getItemMeta();
                    ownermeta.setDisplayName(Command.st + ChatColor.AQUA + "Citizen(시민)");
                    ownermeta.setLore(Arrays.asList(" ", ChatColor.DARK_GRAY + " -  " + ChatColor.RED + owner.getName() + ChatColor.WHITE + " 의 카드입니다.", " "));
                    ownerstack.setItemMeta(ownermeta);
                    break;
                default:
                    if (onesdeck == 'K')
                        win = false;
                    else if (onesdeck == 'C')
                        win = true;
                    else
                        draw = true;
                    ownerstack = new ItemStack(Material.BOOK);
                    ownermeta = ownerstack.getItemMeta();
                    ownermeta.setDisplayName(Command.st + ChatColor.GRAY + "Slave(노예)");
                    ownermeta.setLore(Arrays.asList(" ", ChatColor.DARK_GRAY + " -  " + ChatColor.RED + owner.getName() + ChatColor.WHITE + " 의 카드입니다.", " "));
                    ownerstack.setItemMeta(ownermeta);
                    break;
            }
            if (onesdeck == 'K'){
                onestack = new ItemStack(Material.BOOK);
                onemeta = onestack.getItemMeta();
                onemeta.setDisplayName(Command.st + ChatColor.RED + "King(왕)");
                onemeta.setLore(Arrays.asList(" ", ChatColor.DARK_GRAY + " -  " + ChatColor.RED + p.getName() + ChatColor.WHITE + " 의 카드입니다.", " "));
                onestack.setItemMeta(onemeta);
            }
            else if (onesdeck == 'C'){
                onestack = new ItemStack(Material.BOOK);
                onemeta = onestack.getItemMeta();
                onemeta.setDisplayName(Command.st + ChatColor.AQUA + "Citizen(시민)");
                onemeta.setLore(Arrays.asList(" ", ChatColor.DARK_GRAY + " -  " + ChatColor.RED + p.getName() + ChatColor.WHITE + " 의 카드입니다.", " "));
                onestack.setItemMeta(onemeta);
            }
            else{
                onestack = new ItemStack(Material.BOOK);
                onemeta = onestack.getItemMeta();
                onemeta.setDisplayName(Command.st + ChatColor.GRAY + "Slave(노예)");
                onemeta.setLore(Arrays.asList(" ", ChatColor.DARK_GRAY + " -  " + ChatColor.RED + p.getName() + ChatColor.WHITE + " 의 카드입니다.", " "));
                onestack.setItemMeta(onemeta);
            }
            p.getOpenInventory().setItem(29, onestack);
            p.getOpenInventory().setItem(33, ownerstack);
            owner.getOpenInventory().setItem(29, ownerstack);
            owner.getOpenInventory().setItem(33, onestack);
            boolean finalDraw = draw;
            boolean finalWin = win;
            Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Main.instance, ()-> {
                if (p.getOpenInventory().getTitle().contains("카드 도박")) {
                    if (finalDraw) {
                        p.sendTitle(Command.st, ChatColor.WHITE + "비겼습니다!",5,50,5);
                        owner.sendTitle(Command.st, ChatColor.WHITE + "비겼습니다!",5,50,5);
                        Main.instance.econ.depositPlayer(p,(Hashmap.getbetmoney(p)));
                        Main.instance.econ.depositPlayer(owner, Hashmap.getbetmoney(owner));
                        Hashmap.removeplayer(p);
                        Hashmap.removeplayer(owner);
                        Hashmap.delcardrequest(p.getUniqueId().toString());
                        Hashmap.cardsel.remove(p.getUniqueId().toString());
                        Hashmap.cardsel.remove(owner.getUniqueId().toString());
                        p.closeInventory();
                        owner.closeInventory();
                    } else {
                        if (finalWin) {
                            p.sendTitle(Command.st, ChatColor.WHITE + "승리하셨습니다! " + ChatColor.AQUA + "흭득금액 " + ChatColor.WHITE + ": " + ChatColor.GOLD + (Hashmap.getbetmoney(p) + Hashmap.getbetmoney(owner)) + ChatColor.WHITE + " 원",5,50,5);
                            owner.sendTitle(Command.st, ChatColor.WHITE + "패배하셨습니다.. ",5,50,5);
                            Main.instance.econ.depositPlayer(p,(Hashmap.getbetmoney(p) + Hashmap.getbetmoney(owner)));
                            Hashmap.removeplayer(p);
                            Hashmap.removeplayer(owner);
                            Hashmap.delcardrequest(p.getUniqueId().toString());
                            Hashmap.cardsel.remove(p.getUniqueId().toString());
                            Hashmap.cardsel.remove(owner.getUniqueId().toString());
                            p.closeInventory();
                            owner.closeInventory();
                        } else {
                            owner.sendTitle(Command.st, ChatColor.WHITE + "승리하셨습니다! " + ChatColor.AQUA + "흭득금액 " + ChatColor.WHITE + ": " + ChatColor.GOLD + (Hashmap.getbetmoney(p) + Hashmap.getbetmoney(owner)) + ChatColor.WHITE + " 원",5,50,5);
                            p.sendTitle(Command.st, ChatColor.WHITE + "패배하셨습니다.. ",5,50,5);
                            Main.instance.econ.depositPlayer(owner,(Hashmap.getbetmoney(p) + Hashmap.getbetmoney(owner)));
                            Hashmap.removeplayer(p);
                            Hashmap.removeplayer(owner);
                            Hashmap.delcardrequest(p.getUniqueId().toString());
                            Hashmap.cardsel.remove(p.getUniqueId().toString());
                            Hashmap.cardsel.remove(owner.getUniqueId().toString());
                            p.closeInventory();
                            owner.closeInventory();
                        }
                    }
                }

            },40L);
        }
    }
}
