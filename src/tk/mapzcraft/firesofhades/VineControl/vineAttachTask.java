package tk.mapzcraft.firesofhades.VineControl;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Vine;
import org.bukkit.scheduler.BukkitRunnable;

public class vineAttachTask extends BukkitRunnable {

	private final VineControl plugin;
	Block cBlock;

	public vineAttachTask(VineControl plugin, Block block) {
		this.plugin = plugin;
		cBlock = block;
	}

	public void run() {
		if (cBlock.getState().getData() instanceof Vine) {
			((Vine) cBlock.getState().getData()).putOnFace(BlockFace.UP);
		}
	}

}
