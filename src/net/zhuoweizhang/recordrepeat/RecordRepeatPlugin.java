package net.zhuoweizhang.recordrepeat;

import org.bukkit.event.Event;
import org.bukkit.command.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class RecordRepeatPlugin extends JavaPlugin {

	public RecordRepeatPlayerListener playerListener = new RecordRepeatPlayerListener(this);

	@Override
	public void onEnable() {
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
	}

	public void onDisable() {
		playerListener.timer.cancel();
	}

}
