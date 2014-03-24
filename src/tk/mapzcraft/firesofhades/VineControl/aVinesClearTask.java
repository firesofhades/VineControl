package tk.mapzcraft.firesofhades.VineControl;

import org.bukkit.scheduler.BukkitRunnable;

public class aVinesClearTask extends BukkitRunnable {

	private final VineControl plugin;

	public aVinesClearTask(VineControl plugin) {
		this.plugin = plugin;
	}

	public void run() {
		VineControl.aVines.remove(VineControl.aVines.get(0));
	}

}
