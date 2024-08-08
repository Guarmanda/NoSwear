package fr.black_eyes.noswear;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import fr.black_eyes.noswear.colors.Ansi;
import fr.black_eyes.noswear.colors.Ansi.Attribute;
import fr.black_eyes.noswear.commands.NoSwearCommands;
import fr.black_eyes.noswear.listeners.ChatListener;
import lombok.Getter;
import lombok.Setter;







public class Main extends JavaPlugin {

	@Setter @Getter private static Config configs;
	@Getter @Setter private static Main instance;
	@Getter private Files configFiles;
	@Getter private Utils utils;
	@Getter @Setter private BadWords badWords;
	private static int version = 0;
	@Getter private boolean essentials = false;
	Map<String, String> replace = new HashMap<String, String>(){{
		put("&0",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString());
		put("&1",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString());
		put("&2",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString());
		put("&3",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString());
		put("&4",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString());
		put("&5",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString());
		put("&6",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString());
		put("&7",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString());
		put("&8",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString());
		put("&9",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString());
		put("&a",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString());
		put("&b",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString());
		put("&c",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.RED).bold().toString());
		put("&d",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString());
		put("&e",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString());
		put("&f",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString());
		put("&l",Ansi.ansi().a(Attribute.BLINK_SLOW).toString());
		put("&m",Ansi.ansi().a(Attribute.STRIKETHROUGH_ON).toString());
		put("&n",Ansi.ansi().a(Attribute.UNDERLINE).toString());
	}};

	
	//the way holograms are working changed a lot since 2.2.4. 
	//If user just done the update, this will be auto set to true by detecting a lacking config option
	//that appeared precisely in 2.2.4
	@Getter private boolean killOldHolograms = false;
	

	@Override
	public void onDisable() {
		backUp();
		logInfo("&aBacked up data file in case of crash");
	}
	
	/**
	 * Send a message to logs with colors, only if logs are enabled in config.
	 * @param msg the message to send
	 */
    public void logInfo(String msg) {
    	if(configFiles.getConfig() ==null || !configFiles.getConfig().isSet("ConsoleMessages") || configFiles.getConfig().getBoolean("ConsoleMessages")) {
			// use replace to replace all the keys from the map with their values
			for (Map.Entry<String, String> entry : replace.entrySet()) {
				msg = msg.replace(entry.getKey(), entry.getValue().toString());
			}
			//add reset to the end of the message
			msg = msg + Ansi.ansi().a(Attribute.RESET).toString();
			String pluginName = getDescription().getName();
			Bukkit.getLogger().info("["+pluginName+"] "+msg);
		}
	}
	
	/**
	 * Returns the version of your server (the x in 1.x)
	 * For versions >= 1.20.6, it returns for example 126 for 1.20.6, 
	 * because many things changed in 1.20.6, and that's the first minor 
	 * version that has so much impacts compared to its major version
	 * 
	 * @return The version number
	 */
	public static int getVersion() {
		if(version == 0) {
			String complete_ver = Bukkit.getBukkitVersion().split("-")[0];
			int first_digits = Integer.parseInt(complete_ver.split("[.]")[1]);
			int second_digits = -1;
			if(complete_ver.split("[.]").length>2)
				second_digits = Integer.parseInt(complete_ver.split("[.]")[2]);
			if(second_digits != -1 && (first_digits > 20  || (first_digits == 20 && second_digits >=6 ))){
				version = first_digits * 10 + second_digits;
			}else if (first_digits > 20){
				version = first_digits * 10;
			}
			else{
				version = first_digits;
			}
		}
		return version;
	}
    
	@Override
	public void onEnable() {
		setInstance(this);


		
		configFiles = new Files();
		utils = new Utils();
		logInfo("Server version: 1." + getVersion() );
		logInfo("Loading config files...");
		if(!configFiles.initFiles()) {
        	logInfo("&cConfig or data files couldn't be initialized, the plugin will stop.");
        	return;
        }
		if(Bukkit.getServer().getPluginManager().isPluginEnabled("Essentials")){
        	logInfo("Hooked into essentials for mute");
        	essentials = true;
        }
		//load bad wxords
		logInfo("Loading bad words...");
		badWords = new BadWords();


		this.getServer().getPluginManager().registerEvents(new ChatListener(), this);
		// register commands
		getCommand("noswear").setExecutor(new NoSwearCommands());
        super.onEnable();
        
        configFiles.saveConfig();
        configFiles.saveLang();
		//load config
		setConfigs(Config.getInstance(configFiles.getConfig()));

        

        if(configs.CheckForUpdates) {
        	//logInfo("Checking for update...");
        	 new Updater(this);
        }

	}
	

	/**
	 * Creates a backup of data.yml, which is sometimes lost by plugin users in some rare cases.
	 */
	public void backUp() {
		File directoryPath = new File(instance.getDataFolder() + "/backups/");
		if(!directoryPath.exists()) {
			directoryPath.mkdir();
		}
		List<String> contents = Arrays.asList(directoryPath.list());
		int i=0;
		//finding valid backup name
		if(!contents.isEmpty()) {
			while( !contents.contains(i+"data.yml")) i++;
		}
		while( contents.contains(i+"data.yml")) {
			if (contents.contains((i+10)+"data.yml")) {
				Path oldBackup = Paths.get(instance.getDataFolder() +"/backups/"+ (i)+"data.yml");
				try {
					java.nio.file.Files.deleteIfExists(oldBackup);
				} catch (IOException e) {
					e.printStackTrace();
				}
				i+=9;
			}
			i++;
		}
		
		//auto-deletion of backup to keep only the 10 last ones
		Path oldBackup = Paths.get(instance.getDataFolder() +"/backups/"+ (i-10)+"data.yml");
		try {
			java.nio.file.Files.deleteIfExists(oldBackup);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//backing up
		Path source = Paths.get(instance.getDataFolder() + "/data.yml");
	    Path target = Paths.get(instance.getDataFolder() + "/backups/"+i+"data.yml");
	    try {
	    	java.nio.file.Files.copy(source, target);
	    } catch (IOException e1) {
	        e1.printStackTrace();
	    }
	}


}
