package Division;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;

public class Main extends JavaPlugin {
    public static Main instance;
    public Economy econ;
    public static int slotneed;
    public static double slotaccumoney;
    public static List blacklist;
    public static int diceminimum;
    public static int dicemaximum;
    public static int blackminimum;
    public static int blackmaximum;
    public static int rolletminimum;
    public static int rolletmaximum;
    public static int coinneed;
    public static int indianminimum;
    public static int indiammaximum;
    public static int cardminimum;
    public static int cardmaximum;
    private int taskid;
    private Logger loginstance;
    @Override
    public void onEnable(){
        instance = this;
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[ " + ChatColor.WHITE + "Gambling " + ChatColor.RED + "]" + ChatColor.WHITE + " 도박 플러그인이 활성화 되었습니다.");
        getCommand("도박").setExecutor(new Command());
        Bukkit.getPluginManager().registerEvents(new ClickEvent(), this);
        load();
        logstart();
        if (setupEconomy() == false){
            instance.getLogger().info("이코노미 / Vault 플러그인이 감지되지 않았습니다!");
        }
        else{
            instance.getLogger().info("플러그인이 정상적으로 로드 되었습니다.");
        }

    }
    @Override
    public void onDisable(){
        instance.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[ " + ChatColor.WHITE + "Gambling " + ChatColor.RED + "]" + ChatColor.WHITE + " 도박 플러그인이 비활성화 되었습니다.");
        save();
        getServer().getScheduler().cancelTask(taskid);
        loginstance.savelog();
    }
    private boolean setupEconomy(){
        if (instance.getServer().getPluginManager().getPlugin("Vault") == null){
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = instance.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null){
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    private void load(){
        Check chk = new Check();
        chk.filecheck();
        slotneed = chk.slotgamemoney();
        slotaccumoney = chk.slotaccumoney();
        diceminimum = chk.getdiceminimum();
        dicemaximum = chk.getdicemaximum();
        blacklist = chk.loadblacklist();
        blackminimum = chk.getblackjackminimum();
        blackmaximum = chk.getblackjackmaximum();
        rolletminimum = chk.getrolletminimum();
        rolletmaximum = chk.getrolletmaximum();
        coinneed = chk.getcoinneed();
        indianminimum = chk.getindianminimum();
        indiammaximum = chk.getindianmaximum();
        cardminimum = chk.getcardminimum();
        cardmaximum = chk.getcardmaximum();
    }
    private void save(){
        Check chk = new Check();
        chk.setslotmoney(slotneed);
        chk.setslotaccumoney(Math.round(slotaccumoney));
        chk.saveblacklist(blacklist);
        chk.saveblackminimum(blackminimum);
        chk.saveblackmaximum(blackmaximum);
        chk.savediceminimum(diceminimum);
        chk.savedicemaximum(dicemaximum);
        chk.saverolletminimum(rolletminimum);
        chk.saverolletmaximum(rolletmaximum);
        chk.savecoinmoney(coinneed);
        chk.saveindianminimum(indianminimum);
        chk.saveindianmaximum(indiammaximum);
        chk.savecardminimum(cardminimum);
        chk.savecardmaximum(cardmaximum);
    }

    private void logstart(){
        Logger log = new Logger();
        loginstance = log.getinstance();
        taskid = log.Logging();
    }
}
