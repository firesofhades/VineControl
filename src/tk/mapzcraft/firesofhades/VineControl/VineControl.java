package tk.mapzcraft.firesofhades.VineControl;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.material.Vine;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;

public class VineControl extends JavaPlugin {
	public static ArrayList<String> aVines = new ArrayList<String>();
	public static final Logger log = Logger.getLogger("Minecraft");
	private final VineControlBlockListener blockListener = new VineControlBlockListener(
			this);
	private final VineControlPlayerListener playerListener = new VineControlPlayerListener(
			this);
	File configFile;
	public FileConfiguration config;

	// maxCut >
	// 0!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
	public void onEnable() {
		getServer().getPluginManager().registerEvents(blockListener, this);
		getServer().getPluginManager().registerEvents(playerListener, this);
		getCommand("vinecontrol").setExecutor(new VineControlCommand(this));
		configFile = new File(getDataFolder(), "config.yml");

		try {
			firstRun();
		} catch (Exception e) {
			e.printStackTrace();
		}
		config = new YamlConfiguration();
		config.options()
				.header("WARNING: "
						+ System.getProperty("line.separator")
						+ "Never make max_cut bigger than max_length,"
						+ System.getProperty("line.separator")
						+ "it might remove entire vines or delete blocks above your vines!!!!!!"
						+ System.getProperty("line.separator")
						+ System.getProperty("line.separator")
						+ "make sure that min_length + max_cut is smaller then max_length!!!!"
						+ System.getProperty("line.separator")
						+ "i.e: if min_length = 1 and max_cut = 5"
						+ System.getProperty("line.separator")
						+ "1+5 = 6, so max_length should be atleast 6!!!!"
						+ System.getProperty("line.separator")
						+ "otherwise vines will be cut shorter than the min_lenght!!!!!"
						+ System.getProperty("line.separator")
						+ System.getProperty("line.separator")
						+ "if vines have not reached their min_length,"
						+ System.getProperty("line.separator")
						+ "they will not be cut if they are near the ground"
						+ System.getProperty("line.separator")
						+ "so a long min_length may have adverse effects"
						+ System.getProperty("line.separator")
						+ System.getProperty("line.separator")
						+ "regions should be defined within their worlds"
						+ System.getProperty("line.separator")
						+ "the examples below show the correct structure"
						+ System.getProperty("line.separator")
						+ "region names/ID's are case-sensetive!!!"
						+ System.getProperty("line.separator")
						+ "when configuring new worlds or regions"
						+ System.getProperty("line.separator")
						+ "all values that are not defined will be inherited from"
						+ System.getProperty("line.separator")
						+ "world config or global config");
		config.options().copyHeader(true);
		loadYamls();

	}

	public void onDisable() {

	}

	public Boolean vineHandle(Block cBlock) {
		BlockState bs = cBlock.getState();
		boolean ground = false;
		boolean vineAttached = false;
		boolean blackListed = false;
		boolean aboveGround = true;
		Integer vineLength = -1;
		Integer maxLength = 10;
		Integer minLength = 1;
		Integer growthRate = 100;
		Integer maxCut = 5;
		ArrayList<String> blackList = new ArrayList<String>();
		blackList.add("GLASS");
		Random generator = new Random();

		// loading global config
		maxLength = config.getInt("global.max_length", maxLength);
		minLength = config.getInt("global.min_length", minLength);
		growthRate = config.getInt("global.growthrate", growthRate);
		aboveGround = config.getBoolean("global.above_ground", aboveGround);
		if (config.contains("global.blacklist")) {
			blackList.clear();
			blackList.addAll(config.getStringList("global.blacklist"));
		}
		maxCut = config.getInt("global.max_cut", maxCut);

		// loading world config
		if (config.contains(cBlock.getWorld().getName().toString())) {
			maxLength = config.getInt(cBlock.getWorld().getName().toString()
					+ ".max_length", maxLength);
			minLength = config.getInt(cBlock.getWorld().getName().toString()
					+ ".min_length", minLength);
			growthRate = config.getInt(cBlock.getWorld().getName().toString()
					+ ".growthrate", growthRate);
			aboveGround = config.getBoolean(cBlock.getWorld().getName()
					.toString()
					+ ".above_ground", aboveGround);
			if (config.contains(cBlock.getWorld().getName().toString()
					+ ".blacklist")) {
				blackList.clear();
				blackList.addAll(config.getStringList(cBlock.getWorld()
						.getName().toString()
						+ ".blacklist"));
			}
			maxCut = config.getInt(cBlock.getWorld().getName().toString()
					+ ".max_cut", maxCut);
		}

		// loading region config
		ApplicableRegionSet rSet = WGBukkit.getRegionManager(cBlock.getWorld())
				.getApplicableRegions(cBlock.getLocation());
		Iterator<ProtectedRegion> i = rSet.iterator();
		ProtectedRegion region = null;
		ProtectedRegion pRegion = null;
		while (i.hasNext()) {
			region = i.next();
			if (config.contains(cBlock.getWorld().getName().toString()
					+ "." + region.getId().toString())) {
				if (pRegion == null) {
					pRegion = region;
				}
				if (region.getPriority() > pRegion.getPriority()) {
					pRegion = region;
				}
			}
		}
		if (pRegion != null) {
			if (config.contains(cBlock.getWorld().getName().toString() + "."
					+ pRegion.getId().toString())) {
				maxLength = config.getInt(cBlock.getWorld().getName()
						.toString()
						+ "." + pRegion.getId().toString() + ".max_length",
						maxLength);
				minLength = config.getInt(cBlock.getWorld().getName()
						.toString()
						+ "." + pRegion.getId().toString() + ".min_length",
						minLength);
				growthRate = config.getInt(cBlock.getWorld().getName()
						.toString()
						+ "." + pRegion.getId().toString() + ".growthrate",
						growthRate);
				aboveGround = config.getBoolean(cBlock.getWorld().getName()
						.toString()
						+ "." + pRegion.getId().toString() + ".above_ground",
						aboveGround);
				if (config.contains(cBlock.getWorld().getName().toString()
						+ "." + pRegion.getId().toString() + ".blacklist")) {
					blackList.clear();
					blackList.addAll(config.getStringList(cBlock.getWorld()
							.getName().toString()
							+ "." + pRegion.getId().toString() + ".blacklist"));
				}
				maxCut = config
						.getInt(cBlock.getWorld().getName().toString() + "."
								+ pRegion.getId().toString() + ".max_cut",
								maxCut);
			}
		}

		if ((maxCut + minLength) > maxLength) {
			if (pRegion != null) {
				log.warning("<VineControl> conflicting config for world: "
						+ cBlock.getWorld().getName().toString() + ", region: "
						+ pRegion.getId());
				log.warning("<VineControl> maxLength: " + maxLength.toString()
						+ " minLength: " + minLength.toString() + " maxCut: "
						+ maxCut.toString());
				log.warning("<VineControl> vines will not grow untill problem is fixed");
			} else {
				log.warning("<VineControl> conflicting config for world: "
						+ cBlock.getWorld().getName().toString());
				log.warning("<VineControl> maxLength: " + maxLength.toString()
						+ " minLength: " + minLength.toString() + " maxCut: "
						+ maxCut.toString());
				log.warning("<VineControl> vines will not grow untill problem is fixed");
			}
			cBlock.setType(Material.AIR);
			return true;
		}

		// controls growthrate
		if ((generator.nextInt(100) > growthRate - 1)
				&& !VineControl.aVines.contains(cBlock.getX() + " "
						+ cBlock.getY() + " " + cBlock.getZ() + " "
						+ cBlock.getWorld().getName())) {

		}
		// checks if the vine is next to a blacklisted block and removes it
		int listc = 0;
		if (blackList.size() > 0) {
			Block block = cBlock;
			if (block.getState().getData() instanceof Vine) {
				while (blackList.size() > listc) {
					if (Material.matchMaterial(blackList.get(listc)) == cBlock
							.getRelative(BlockFace.SOUTH).getType()) {
						blackListed = true;
					}
					if (Material.matchMaterial(blackList.get(listc)) == cBlock
							.getRelative(BlockFace.NORTH).getType()) {
						blackListed = true;
					}
					if (Material.matchMaterial(blackList.get(listc)) == cBlock
							.getRelative(BlockFace.WEST).getType()) {
						blackListed = true;
					}
					if (Material.matchMaterial(blackList.get(listc)) == cBlock
							.getRelative(BlockFace.EAST).getType()) {
						blackListed = true;
					}
					if (blackListed) {
						cBlock.setType(Material.AIR);
						return true;
					}
					listc = listc + 1;
				}
			}
		}

		// check if vine is near ground
		if (aboveGround) {
			if (!cBlock.getRelative(BlockFace.DOWN).isEmpty()
					&& !cBlock.getRelative(BlockFace.DOWN).getType()
							.equals(Material.VINE)) {
				ground = true;
			}
			if (!cBlock.getRelative(BlockFace.DOWN, 2).isEmpty()
					&& !cBlock.getRelative(BlockFace.DOWN, 2).getType()
							.equals(Material.VINE)) {
				ground = true;
			}
		}

		// the next loop will check how long a vine is by moving up the vine
		// untill it finds the block on wich the vine hangs or the vine exceeds
		// the maxLength
		while (!vineAttached) {
			if (cBlock.getState().getData() instanceof Vine) {
				bs = cBlock.getState();
				if (((Vine) bs.getData()).isOnFace(BlockFace.EAST)) {
					if (!cBlock.getRelative(BlockFace.EAST).isEmpty()) {
						vineAttached = true;
					}
				}
				if (((Vine) bs.getData()).isOnFace(BlockFace.WEST)) {
					if (!cBlock.getRelative(BlockFace.WEST).isEmpty()) {
						vineAttached = true;
					}
				}
				if (((Vine) bs.getData()).isOnFace(BlockFace.NORTH)) {
					if (!cBlock.getRelative(BlockFace.NORTH).isEmpty()) {
						vineAttached = true;
					}
				}
				if (((Vine) bs.getData()).isOnFace(BlockFace.SOUTH)) {
					if (!cBlock.getRelative(BlockFace.SOUTH).isEmpty()) {
						vineAttached = true;
					}
				}
			}
			vineLength = vineLength + 1;
			cBlock = cBlock.getRelative(BlockFace.UP);
			if (vineLength > maxLength) {
				vineAttached = true;
			}
		}

		// returning to grown vine...
		cBlock = cBlock.getRelative(BlockFace.DOWN);
		while (cBlock.getType().equals(Material.VINE)) {
			cBlock = cBlock.getRelative(BlockFace.DOWN);
		}
		cBlock = cBlock.getRelative(BlockFace.UP);

		// depending on previously obtained data a vine gets cut
		if (ground && (vineLength > minLength)
				&& vineLength <= (maxCut + minLength)) {
			Integer rLength = generator.nextInt(vineLength - minLength);

			cBlock = cBlock.getRelative(BlockFace.UP, rLength);
			cBlock.setType(Material.AIR);
			return true;
		}
		if (ground && vineLength > (minLength + maxCut)) {
			Integer rLength = generator.nextInt(maxCut + 1);

			cBlock = cBlock.getRelative(BlockFace.UP, rLength);
			cBlock.setType(Material.AIR);
			return true;
		}
		if (!ground && (vineLength > maxLength)) {
			Integer rLength = generator.nextInt(maxCut + 1);

			cBlock = cBlock.getRelative(BlockFace.UP, rLength);
			cBlock.setType(Material.AIR);
			return true;
		}
		return false;
	}

	private void firstRun() throws Exception {
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			copy(getResource("config.yml"), configFile);
		}
	}

	private void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveYamls() {
		try {
			config.save(configFile);
			loadYamls();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadYamls() {
		try {
			config.load(configFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
