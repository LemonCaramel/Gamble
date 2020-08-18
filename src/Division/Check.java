package Division;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Check {
    private Main main;
    private File file;
    private FileConfiguration config;

    public Check() {
        this.main = Main.instance;
        this.file = new File("plugins/" + this.main.getDescription().getName() + "/config.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }
    public void fileload(){
        try{
            this.config.load(file);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void filecheck() {
        if (!file.exists()) {
            this.main.saveDefaultConfig();
        }
        fileload();
    }
    public void save(){
        try{
            this.config.save(file);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public double slotaccumoney() {
        double money;
        money = this.config.getDouble("Slot.Accumoney");
        return money;
    }

    public int slotgamemoney() {
        int money;
        money = this.config.getInt("Slot.Need");
        return money;
    }
    public int getrolletminimum(){
        int money;
        money = this.config.getInt("Rollet.Minimum");
        return money;
    }
    public int getrolletmaximum(){
        int money;
        money = this.config.getInt("Rollet.Maximum");
        return money;
    }
    public int getcoinneed(){
        int money;
        money = this.config.getInt("Coin.amount");
        return money;
    }
    public int getdiceminimum(){
        int money;
        money = this.config.getInt("Dice.Minimum");
        return money;
    }
    public int getdicemaximum(){
        int money;
        money = this.config.getInt("Dice.Maximum");
        return money;
    }
    public int getblackjackminimum(){
        int money;
        money = this.config.getInt("Blackjack.Minimum");
        return money;
    }
    public int getblackjackmaximum(){
        int money;
        money = this.config.getInt("Blackjack.Maximum");
        return money;
    }
    public int getindianminimum(){
        int money;
        money = this.config.getInt("Indian.Minimum");
        if (money == 0)
            money = 1000;
        return money;
    }
    public int getindianmaximum(){
        int money;
        money = this.config.getInt("Indian.Maximum");
        if (money == 0)
            money = 4000;
        return money;
    }
    public int getcardminimum(){
        int money;
        money = this.config.getInt("Card.Minimum");
        if (money == 0)
            money = 1000;
        return money;
    }
    public int getcardmaximum(){
        int money;
        money = this.config.getInt("Card.Maximum");
        if (money == 0)
            money = 4000;
        return money;
    }
    public void setslotmoney(int a) {
        fileload();
        this.config.set("Slot.Need", a);
        save();
    }
    public void setslotaccumoney(double a) {
        fileload();
        this.config.set("Slot.Accumoney", a);
        save();
    }
    public void savecoinmoney(int a) {
        fileload();
        this.config.set("Coin.amount", a);
        save();
    }
    public void saverolletminimum(int a){
        fileload();
        this.config.set("Rollet.Minimum", a);
        save();
    }
    public void saverolletmaximum(int a){
        fileload();
        this.config.set("Rollet.Maximum", a);
        save();
    }
    public void savediceminimum(int a){
        fileload();
        this.config.set("Dice.Minimum", a);
        save();
    }
    public void savedicemaximum(int a){
        fileload();
        this.config.set("Dice.Maximum", a);
        save();
    }
    public void saveblackminimum(int a){
        fileload();
        this.config.set("Blackjack.Minimum", a);
        save();
    }
    public void saveblackmaximum(int a){
        fileload();
        this.config.set("Blackjack.Maximum", a);
        save();
    }
    public void saveindianminimum(int a){
        fileload();
        this.config.set("Indian.Minimum", a);
        save();
    }
    public void saveindianmaximum(int a){
        fileload();
        this.config.set("Indian.Maximum", a);
        save();
    }
    public void savecardminimum(int a){
        fileload();
        this.config.set("Card.Minimum", a);
        save();
    }
    public void savecardmaximum(int a){
        fileload();
        this.config.set("Card.Maximum",a);
        save();
    }
    public List loadblacklist(){
        List blacklist;
        blacklist = this.config.getList("Blacklist");
        if (blacklist == null) {
            blacklist = new ArrayList();
        }
        else if (blacklist.get(0).toString().equalsIgnoreCase("null")){
            blacklist.clear();
        }
        return blacklist;
    }

    public void saveblacklist(List list){
        fileload();
        if (list == null){
            list = new ArrayList();
            list.add("null");
        }
        else if (list.isEmpty()){
            list.add("null");
        }
        this.config.set("Blacklist", list);
        save();
    }
}

