package shirohaNya.dglabmc.utils;

import com.google.gson.Gson;
import shirohaNya.dglabmc.ConfigManager;
import shirohaNya.dglabmc.MCWebSocketServer;
import shirohaNya.dglabmc.scripts.ScriptManager;

import java.io.File;
import java.util.*;

import static org.bukkit.Bukkit.getLogger;
import static shirohaNya.dglabmc.DGlabMC.plugin;

public class DGlabUtils {
    private DGlabUtils() throws Exception {
        throw new Exception("工具类不允许实例化");
    }

    public static void runWebSocketServer(int port) {
        plugin.mcWebSocketServer = new MCWebSocketServer(port);
        try {
            plugin.mcWebSocketServer.start();
        } catch (Exception e) {
            plugin.mcWebSocketServer.stop();
            plugin.mcWebSocketServer = null;
            getLogger().info("开启WebSocketServer失败" + e);
        }
    }

    public static void reloadConfigFile() {
        getLogger().info("正在重载");
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        ConfigManager.reload();
        ScriptManager.reloadScriptConfig();
        getLogger().info("重载结束");
    }

    public static void overwriteConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (configFile.exists()) {
            if (configFile.delete()) getLogger().info("覆盖config成功");
            else getLogger().warning("覆盖config失败");
        }

        // 复制 jar 内的 config.yml 到 plugins/YourPlugin/config.yml
        plugin.saveResource("config.yml", false); // false = 不会覆盖，但现在文件被删了就能成功复制
    }

    public static String toDGJson(String type, String clientId, String targetId, String message) {
        return "{\"type\":\"" + type + "\",\"clientId\":\"" + clientId + "\",\"targetId\":\"" + targetId + "\",\"message\":\"" + message + "\"}";
    }

    public static String toDGJson(String[] str) {
        return "{\"type\":\"" + str[0] + "\",\"clientId\":\"" + str[1] + "\",\"targetId\":\"" + str[2] + "\",\"message\":\"" + str[3] + "\"}";
    }

    public static boolean isValidUUID(String input) {
        try {
            UUID.fromString(input);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, String> toHashMap(String json) {
        return new Gson().fromJson(json, HashMap.class);
    }

}
