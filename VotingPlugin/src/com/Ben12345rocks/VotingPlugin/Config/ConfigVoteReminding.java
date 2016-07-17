package com.Ben12345rocks.VotingPlugin.Config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.Ben12345rocks.VotingPlugin.Main;
import com.Ben12345rocks.VotingPlugin.Files.Files;

public class ConfigVoteReminding {

	static ConfigVoteReminding instance = new ConfigVoteReminding();

	static Main plugin = Main.plugin;

	public static ConfigVoteReminding getInstance() {
		return instance;
	}

	FileConfiguration data;

	File dFile;

	private ConfigVoteReminding() {
	}

	public ConfigVoteReminding(Main plugin) {
		ConfigVoteReminding.plugin = plugin;
	}

	public FileConfiguration getData() {
		return data;
	}

	public boolean getEnabled() {
		return getData().getBoolean("Enabled");
	}

	public int getRemindDelay() {
		int num = getData().getInt("RemindDelay");
		if (num != 0) {
			return num;
		}
		return 30;
	}

	public boolean getRemindOnLogin() {
		return getData().getBoolean("RemindOnLogin");
	}

	public boolean getRemindOnlyOnce() {
		return getData().getBoolean("RemindOnlyOnce");
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> getRewards() {
		try {
			ArrayList<String> list = (ArrayList<String>) getData().getList(
					"Rewards");
			if (list != null) {
				return list;
			} else {
				return new ArrayList<String>();
			}
		} catch (Exception ex) {
			return new ArrayList<String>();
		}
	}

	public void reloadData() {
		data = YamlConfiguration.loadConfiguration(dFile);
	}

	public void saveData() {
		Files.getInstance().editFile(dFile, data);
	}

	public void set(String path, Object value) {
		getData().set(path, value);
		saveData();
	}

	public void setup(Plugin p) {
		if (!p.getDataFolder().exists()) {
			p.getDataFolder().mkdir();
		}

		dFile = new File(p.getDataFolder(), "VoteReminding.yml");

		if (!dFile.exists()) {
			try {
				dFile.createNewFile();
				plugin.saveResource("VoteReminding.yml", true);
			} catch (IOException e) {
				Bukkit.getServer()
						.getLogger()
						.severe(ChatColor.RED
								+ "Could not create VoteReminding.yml!");
			}
		}

		data = YamlConfiguration.loadConfiguration(dFile);
	}

}