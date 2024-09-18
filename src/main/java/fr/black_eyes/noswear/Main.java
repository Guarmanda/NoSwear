package fr.black_eyes.noswear;

import org.bukkit.Bukkit;
import fr.black_eyes.noswear.commands.NoSwearCommands;
import fr.black_eyes.noswear.listeners.ChatListener;
import fr.black_eyes.simpleJavaPlugin.SimpleJavaPlugin;
import fr.black_eyes.simpleJavaPlugin.Utils;
import lombok.Getter;
import lombok.Setter;


public class Main extends SimpleJavaPlugin {

	@Setter @Getter private static Config configs;
	@Getter @Setter private static Main instance;
	@Getter @Setter private BadWords badWords;
	@Getter private boolean essentials = false;

    
	public void onEnable() {
		setInstance(this);
		super.onEnable();
		if(configFiles.getLang() == null) {
			Utils.logInfo("&cConfig or data files couldn't be initialized, the plugin will stop.");
			return;
		}
		setCommandExecutor(new NoSwearCommands());

		if(Bukkit.getServer().getPluginManager().isPluginEnabled("Essentials")){
        	Utils.logInfo("Hooked into essentials for mute");
        	essentials = true;
        }
		//load bad wxords
		Utils.logInfo("Loading bad words...");
		badWords = new BadWords();

		this.getServer().getPluginManager().registerEvents(new ChatListener(), this);
        
		//load config
		setConfigs(Config.getInstance(configFiles.getConfig()));

	}


}
