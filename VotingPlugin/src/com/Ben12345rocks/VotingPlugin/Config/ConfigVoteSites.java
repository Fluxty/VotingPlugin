package com.Ben12345rocks.VotingPlugin.Config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.Ben12345rocks.AdvancedCore.Util.Misc.ArrayUtils;
import com.Ben12345rocks.AdvancedCore.YML.YMLFile;
import com.Ben12345rocks.VotingPlugin.Main;
import com.Ben12345rocks.VotingPlugin.Objects.VoteSite;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigVoteSites.
 */
public class ConfigVoteSites extends YMLFile {

	/** The instance. */
	static ConfigVoteSites instance = new ConfigVoteSites();

	/** The plugin. */
	static Main plugin = Main.plugin;

	/**
	 * Gets the single instance of ConfigVoteSites.
	 *
	 * @return single instance of ConfigVoteSites
	 */
	public static ConfigVoteSites getInstance() {
		return instance;
	}

	/**
	 * Instantiates a new config vote sites.
	 */
	public ConfigVoteSites() {
		super(new File(Main.plugin.getDataFolder(), "VoteSites.yml"));
	}

	/**
	 * Generate vote site.
	 *
	 * @param siteName
	 *            the site name
	 */
	public void generateVoteSite(String siteName) {
		String org = siteName;
		siteName = siteName.replace(".", "_");
		plugin.getLogger().warning("VoteSite " + siteName + " doe not exist, generaterating one...");
		setEnabled(siteName, true);
		setServiceSite(siteName, org);
		setVoteURL(siteName, "VoteURL");
		setVoteDelay(siteName, 24);
		ArrayList<String> rewards = new ArrayList<String>();
		rewards.add(siteName.replace(".", "_"));
		setRewards(siteName, rewards);

		plugin.loadVoteSites();
		plugin.getLogger().info("Loaded default values into file");
	}

	/**
	 * Gets the cumulative rewards.
	 *
	 * @param siteName
	 *            the site name
	 * @return the cumulative rewards
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> getCumulativeRewards(String siteName) {

		return (ArrayList<String>) getData(siteName).getList("Cumulative.Rewards", new ArrayList<String>());

	}

	/**
	 * Gets the cumulative reward votes amount.
	 *
	 * @param siteName
	 *            the site name
	 * @return the cumulative reward votes amount
	 */
	public int getCumulativeRewardVotesAmount(String siteName) {
		return getData(siteName).getInt("Cumulative.Votes");
	}

	/**
	 * Gets the data.
	 *
	 * @param siteName
	 *            the site name
	 * @return the data
	 */
	public ConfigurationSection getData(String siteName) {
		ConfigurationSection data = getData().getConfigurationSection("VoteSites." + siteName);
		return data;
	}

	/**
	 * Gets the priority.
	 *
	 * @param siteName
	 *            the site name
	 * @return the priority
	 */
	public int getPriority(String siteName) {
		return getData(siteName).getInt("Priority");
	}

	/**
	 * Gets the rewards.
	 *
	 * @param siteName
	 *            the site name
	 * @return the rewards
	 */
	public String getRewardsPath(String siteName) {
		return "VoteSites." + siteName + ".Rewards";
	}

	/**
	 * Gets the service site.
	 *
	 * @param siteName
	 *            the site name
	 * @return the service site
	 */
	public String getServiceSite(String siteName) {
		return getData(siteName).getString("ServiceSite");
	}

	public String getDisplayName(String site) {
		return getData(site).getString("Name");
	}

	/**
	 * Gets the vote delay.
	 *
	 * @param siteName
	 *            the site name
	 * @return the vote delay
	 */
	public int getVoteDelay(String siteName) {
		return getData(siteName).getInt("VoteDelay");
	}

	/**
	 * Gets the vote site enabled.
	 *
	 * @param siteName
	 *            the site name
	 * @return the vote site enabled
	 */
	public boolean getVoteSiteEnabled(String siteName) {
		return getData(siteName).getBoolean("Enabled");
	}

	/**
	 * Gets the vote site file.
	 *
	 * @param siteName
	 *            the site name
	 * @return the vote site file
	 */
	public File getVoteSiteFile(String siteName) {
		File dFile = new File(plugin.getDataFolder() + File.separator + "VoteSites", siteName + ".yml");
		FileConfiguration data = YamlConfiguration.loadConfiguration(dFile);
		if (!dFile.exists()) {
			try {
				data.save(dFile);
			} catch (IOException e) {
				plugin.getLogger().severe(ChatColor.RED + "Could not create VoteSites/" + siteName + ".yml!");

			}
		}
		return dFile;

	}

	/**
	 * Gets the vote sites.
	 *
	 * @return the vote sites
	 */
	@Deprecated
	public ArrayList<VoteSite> getVoteSites() {
		if (plugin.getVoteSites() != null) {
			return plugin.getVoteSites();
		} else {
			plugin.loadVoteSites();
			return plugin.getVoteSites();
		}
	}

	/**
	 * Gets the vote sites load.
	 *
	 * @return the vote sites load
	 */
	public ArrayList<VoteSite> getVoteSitesLoad() {
		ArrayList<VoteSite> voteSites = new ArrayList<VoteSite>();
		ArrayList<String> voteSiteNames = getVoteSitesNames();
		if (voteSiteNames != null) {
			for (String site : voteSiteNames) {
				if (getVoteSiteEnabled(site) && !site.equalsIgnoreCase("null")) {
					if (!siteCheck(site)) {
						plugin.getLogger().warning("Failed to load site " + site + ", see above");
					} else {
						voteSites.add(new VoteSite(site));
					}
				}
			}
		}

		Collections.sort(voteSites, new Comparator<VoteSite>() {
			@Override
			public int compare(VoteSite v1, VoteSite v2) {
				int v1P = v1.getPriority();
				int v2P = v2.getPriority();

				if (v1P < v2P) {
					return 1;
				}
				if (v1P > v2P) {
					return -1;
				}

				return 0;
			}
		});

		return voteSites;
	}

	/**
	 * Gets the vote sites names.
	 *
	 * @return the vote sites names
	 */
	public ArrayList<String> getVoteSitesNames() {
		ArrayList<String> siteNames = new ArrayList<String>();
		if (getData().isConfigurationSection("VoteSites")) {
			siteNames = ArrayUtils.getInstance().convert(getData().getConfigurationSection("VoteSites").getKeys(false));
		}

		for (int i = siteNames.size() - 1; i >= 0; i--) {
			// plugin.getLogger().info(siteNames.get(i));
			if (!getVoteSiteEnabled(siteNames.get(i)) || siteNames.get(i).equalsIgnoreCase("null")
					|| !siteCheck(siteNames.get(i))) {
				// plugin.getLogger().info("Removed: " + siteNames.get(i));
				siteNames.remove(i);

			}

		}

		return siteNames;
	}

	/**
	 * Gets the vote URL.
	 *
	 * @param siteName
	 *            the site name
	 * @return the vote URL
	 */
	public String getVoteURL(String siteName) {
		return getData(siteName).getString("VoteURL");
	}

	/**
	 * Checks if is service site good.
	 *
	 * @param siteName
	 *            the site name
	 * @return true, if is service site good
	 */
	public boolean isServiceSiteGood(String siteName) {
		if (getServiceSite(siteName) == null) {
			return false;
		} else if (getServiceSite(siteName).equalsIgnoreCase("")) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if is vote URL good.
	 *
	 * @param siteName
	 *            the site name
	 * @return true, if is vote URL good
	 */
	public boolean isVoteURLGood(String siteName) {
		if (getVoteURL(siteName) == null) {
			return false;
		} else if (getVoteURL(siteName).equalsIgnoreCase("")) {
			return false;
		}
		return true;
	}

	@Override
	public void onFileCreation() {
		plugin.saveResource("VoteSites.yml", true);

	}

	/**
	 * Rename vote site.
	 *
	 * @param siteName
	 *            the site name
	 * @param newName
	 *            the new name
	 * @return true, if successful
	 */
	public boolean renameVoteSite(String siteName, String newName) {
		return getVoteSiteFile(siteName)
				.renameTo(new File(plugin.getDataFolder() + File.separator + "VoteSites", newName + ".yml"));
	}

	/**
	 * Sets the.
	 *
	 * @param siteName
	 *            the site name
	 * @param path
	 *            the path
	 * @param value
	 *            the value
	 */
	public void set(String siteName, String path, Object value) {
		// String playerName = user.getPlayerName();
		ConfigurationSection data = getData(siteName);
		if (data == null) {
			getData().createSection("VoteSites." + siteName);
			data = getData(siteName);
		}
		data.set(path, value);
		saveData();
	}

	/**
	 * Sets the cumulative rewards.
	 *
	 * @param siteName
	 *            the site name
	 * @param value
	 *            the value
	 */
	public void setCumulativeRewards(String siteName, ArrayList<String> value) {
		set(siteName, "Cumulative.Rewards", value);
	}

	public void setCumulativeVotes(String siteName, int value) {
		set(siteName, "Cumulative.Votes", value);
	}

	/**
	 * Sets the enabled.
	 *
	 * @param siteName
	 *            the site name
	 * @param disabled
	 *            the disabled
	 */
	public void setEnabled(String siteName, boolean disabled) {
		set(siteName, "Enabled", disabled);
	}

	/**
	 * Sets the priority.
	 *
	 * @param siteName
	 *            the site name
	 * @param value
	 *            the value
	 */
	public void setPriority(String siteName, int value) {
		set(siteName, "Priority", value);
	}

	/**
	 * Sets the rewards.
	 *
	 * @param siteName
	 *            the site name
	 * @param value
	 *            the value
	 */
	public void setRewards(String siteName, ArrayList<String> value) {
		set(siteName, "Rewards", value);
	}

	/**
	 * Sets the service site.
	 *
	 * @param siteName
	 *            the site name
	 * @param serviceSite
	 *            the service site
	 */
	public void setServiceSite(String siteName, String serviceSite) {
		set(siteName, "ServiceSite", serviceSite);
	}

	/**
	 * Sets the vote delay.
	 *
	 * @param siteName
	 *            the site name
	 * @param voteDelay
	 *            the vote delay
	 */
	public void setVoteDelay(String siteName, int voteDelay) {
		set(siteName, "VoteDelay", voteDelay);
	}

	/**
	 * Sets the vote URL.
	 *
	 * @param siteName
	 *            the site name
	 * @param url
	 *            the url
	 */
	public void setVoteURL(String siteName, String url) {
		set(siteName, "VoteURL", url);
	}

	/**
	 * Site check.
	 *
	 * @param siteName
	 *            the site name
	 * @return true, if successful
	 */
	public boolean siteCheck(String siteName) {
		boolean pass = true;
		if (!isServiceSiteGood(siteName)) {
			plugin.getLogger().warning("Issue with ServiceSite in site " + siteName + ", votes may not work properly");
			pass = false;
		}
		if (!isVoteURLGood(siteName)) {
			plugin.getLogger().warning("Issue with VoteURL in site " + siteName);
		}
		return pass;
	}

	public void setDisplayName(String siteName, String value) {
		set(siteName, "Name", value);
	}

}
