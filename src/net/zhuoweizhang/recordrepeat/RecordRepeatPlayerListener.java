package net.zhuoweizhang.recordrepeat;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Jukebox;
import org.bukkit.inventory.ItemStack;

public class RecordRepeatPlayerListener extends PlayerListener {
	public RecordRepeatPlugin plugin;

	public Timer timer = new Timer();

	public Map<Block, RecordRepeatTask> tasks = new HashMap<Block, RecordRepeatTask>();

	/** Length of the record Cat, in milliseconds. */

	public static int CAT_LENGTH = (((3 * 60) + 5) * 1000) + 340; // 3:05.34

	public static int THIRTEEN_LENGTH = (((2 * 60) + 58) * 1000) + 90; //2:58.09

	public RecordRepeatPlayerListener(RecordRepeatPlugin plugin) {
		this.plugin = plugin;
	}

	public void onPlayerInteract(PlayerInteractEvent event){
		if (!event.isCancelled() && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (event.hasBlock() && event.getClickedBlock().getType().equals(Material.JUKEBOX)) { //jukebox
				Block block = event.getClickedBlock();
				if (((Jukebox)block.getState().getData()).getPlaying() != null) {
					//System.out.println("eject");
					if (tasks.containsKey(block)) {
						//System.out.println("Cancel event");
						tasks.get(block).cancel();
						tasks.remove(block);
					}
				} else if (event.getItem() == null) {
					return;
				} else if (event.getItem().getType().equals(Material.GREEN_RECORD)) { //Cat
					//System.out.println("Scheduled cat");
					RecordRepeatTask task = new RecordRepeatTask(block, Material.GREEN_RECORD);
					timer.schedule(task, CAT_LENGTH + 1000, CAT_LENGTH + 1000);
					tasks.put(event.getClickedBlock(), task);
				} else if (event.getItem().getType().equals(Material.GOLD_RECORD)) { //Thirteen
					RecordRepeatTask task = new RecordRepeatTask(block, Material.GOLD_RECORD);
					timer.schedule(task, THIRTEEN_LENGTH + 1000, THIRTEEN_LENGTH + 1000);
					tasks.put(event.getClickedBlock(), task);
				}
			}
		}
	}

	private class RecordRepeatTask extends TimerTask {
		public Block block;
		public Material recordType;
		public RecordRepeatTask(Block block, Material recordType) {
			this.block = block;
			this.recordType = recordType;
		}
		public void run() {
			//System.out.println("run");
			if (!block.getType().equals(Material.JUKEBOX)) {
				//System.out.println("no longer a jukebox");
				cancel();
				tasks.remove(this.block);
				return;
			}
			Jukebox jukebox = (Jukebox) block.getState().getData();
			jukebox.setPlaying(recordType);
			block.getWorld().playEffect(block.getLocation(), Effect.RECORD_PLAY, recordType.getId(), 128);
		}
	}
}
