package fr.black_eyes.noswear;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Black_Eyes
 * Singleton-like class to initialize and get the config options
 *
 */
public class Config {




public Boolean saveDataFileDuringReload, 
CheckForUpdates,
ConsoleMessages,
GhostMute,
Censor_message,
show_title_at_player,
send_messages_to_admins;

public String CensorShip_Character,
format_of_swearing_messages_for_admins;



/**
 * 
 */
private static Config instance = null;


/**
 * Creates a new instance replacing the old one, and re-initialize the config options
 * Usefull for reloading the plugin
 * @param config The file to get the options from
 * @return a new instance of the config options
 */
public static Config getInstance(FileConfiguration config)
{
    instance = new Config(config);
    return instance;
}

/**
 * @return the only instance of the config options. Usefull for anything but reloading the plugin.
 */
public static Config getInstance()
{
	return instance;
}


/**
 * Initialize all the config options by reading the YAML config file 
 * @param config The config file to get the options from, and initialize them
 */
public Config(FileConfiguration config) {
	saveDataFileDuringReload = config.getBoolean("SaveDataFileDuringReload");
	CheckForUpdates = config.getBoolean("CheckForUpdates");
	ConsoleMessages = config.getBoolean("ConsoleMessages");
	CensorShip_Character = config.getString("CensorShip_Character");
	GhostMute = config.getBoolean("ghost_mute_player_by_only_sending_his_bad_message_to_himself");
	Censor_message = config.getBoolean("Censor_message");
	show_title_at_player = config.getBoolean("show_title_at_player");
	format_of_swearing_messages_for_admins = config.getString("format_of_swearing_messages_for_admins_and_console");
	send_messages_to_admins = config.getBoolean("send_messages_to_admins");
}




	
}
