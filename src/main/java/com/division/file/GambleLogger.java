package com.division.file;

import com.division.Gamble;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class GambleLogger {

    private static GambleLogger instance;
    public SimpleDateFormat format;
    private SimpleDateFormat fileFormat;
    public int taskID;
    private File logfile;
    private FileConfiguration config;
    private Gamble Plugin;
    public ArrayList<String> logArray;

    static {
        instance = new GambleLogger();
    }

    private GambleLogger() {
        Plugin = JavaPlugin.getPlugin(Gamble.class);
        logArray = new ArrayList<>();
        format = new SimpleDateFormat("[ MM-dd / HH:mm:ss ]");
        fileFormat = new SimpleDateFormat("MM.dd-HH.mm.ss");
        File folder = new File("plugins/" + Plugin.getDescription().getName() + "/log");
        if (!folder.exists())
            folder.mkdir();
    }

    public static GambleLogger getInstance() {
        return instance;
    }


    public void logging() {
        //1분단위 저장
        taskID = Bukkit.getScheduler().runTaskTimer(Plugin, () -> {
            if (logArray.size() >= 20) {
                logfile = new File("plugins/" + Plugin.getName() + "/log/" + fileFormat.format(Calendar.getInstance().getTime()) + ".log");
                config = YamlConfiguration.loadConfiguration(logfile);
                config.set("로그", logArray);
                try {
                    config.save(logfile);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                logArray.clear();
            }
        }, 0L, 1200L).getTaskId();
    }

    public void addLog(String s) {
        String time = format.format(Calendar.getInstance().getTime());
        logArray.add(time + " " + s);
    }

    public void forceSaveLog() {
        Bukkit.getScheduler().cancelTask(taskID);
        if (!logArray.isEmpty()) {
            logfile = new File("plugins/" + Plugin.getName() + "/log/" + fileFormat.format(Calendar.getInstance().getTime()) + ".log");
            config = YamlConfiguration.loadConfiguration(logfile);
            config.set("로그", logArray);
            try {
                config.save(logfile);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
