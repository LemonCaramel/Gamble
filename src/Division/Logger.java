package Division;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Logger {

    public static List logging;
    public static SimpleDateFormat format;
    private static SimpleDateFormat saveformat;
    public int taskid;
    private File folder;
    private File logfile;
    private FileConfiguration config;
    private Main instance;

    public Logger(){
        this.instance = Main.instance;
        this.logging = new ArrayList();
        this.folder = new File("plugins/" + this.instance.getDescription().getName() + "/log");
        this.format = new SimpleDateFormat("[ MM-dd / HH:mm:ss ]");
        this.saveformat = new SimpleDateFormat("MM.dd-HH.mm.ss");
        foldercheck();
    }

    public void foldercheck(){
        if (!folder.exists()){
            folder.mkdir();
        }
    }

    public int Logging(){
        //1분단위 저장
        taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
            @Override
            public void run() {
                if (!logging.isEmpty()){
                    logfile = new File("plugins/" + instance.getDescription().getName() + "/log/" + saveformat.format(Calendar.getInstance().getTime())+".log");
                    config = YamlConfiguration.loadConfiguration(logfile);
                    config.set("로그", logging);
                    try{
                        config.save(logfile);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    logging.clear();
                }
            }
        },0L, 1200L);
        return taskid;
    }

    public static void addlog(String s){
        String time = format.format(Calendar.getInstance().getTime());
        logging.add(time + " " + s);
    }

    public void savelog() {
        if (!logging.isEmpty()) {
            logfile = new File("plugins/" + instance.getDescription().getName() + "/log/" + saveformat.format(Calendar.getInstance().getTime()) + ".log");
            config = YamlConfiguration.loadConfiguration(logfile);
            config.set("로그", logging);
            try {
                config.save(logfile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Logger getinstance(){
        return this;
    }
}
