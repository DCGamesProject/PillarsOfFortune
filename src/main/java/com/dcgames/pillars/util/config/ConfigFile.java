package com.dcgames.pillars.util.config;

import com.dcgames.pillars.util.chat.CC;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigFile extends YamlConfiguration {
    private File file;
    private YamlConfiguration configuration;

    public ConfigFile(JavaPlugin plugin, String name) {
        this.file = new File(plugin.getDataFolder(), name);
        if (!this.file.exists()) {
            plugin.saveResource(name, false);
        }

        try {
            this.load(this.file);
        } catch (InvalidConfigurationException | IOException var4) {
            var4.printStackTrace();
        }
        this.configuration = YamlConfiguration.loadConfiguration(this.file);

    }

    public void save() {
        try {
            this.save(this.file);
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public int getInt(String path) {
        return super.getInt(path, 0);
    }

    public double getDouble(String path) {
        return super.getDouble(path, 0.0D);
    }

    public boolean getBoolean(String path) {
        return super.getBoolean(path, false);
    }

    public String getString(String path) {
        return CC.translate(super.getString(path, ""));
    }

    public String getString2(String path) {
        if (configuration.contains(path)) {
            return this.configuration.getString(path);
        }
        return null;
    }

    public List<String> getStringList(String path) {
        return super.getStringList(path).stream().map(CC::translate).collect(Collectors.toList());
    }

    public File getFile() {
        return this.file;
    }
}
