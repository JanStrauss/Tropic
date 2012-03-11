package me.simplex.tropic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import me.simplex.tropic.populators.Populator_Bush;
import me.simplex.tropic.populators.Populator_Caves;
import me.simplex.tropic.populators.Populator_Delayed;
import me.simplex.tropic.populators.Populator_Flowers;
import me.simplex.tropic.populators.Populator_Glowshroom;
import me.simplex.tropic.populators.Populator_Gravel;
import me.simplex.tropic.populators.Populator_Lake_And_Creek;
import me.simplex.tropic.populators.Populator_Lava_Lakes;
import me.simplex.tropic.populators.Populator_Longgrass;
import me.simplex.tropic.populators.Populator_Melon;
import me.simplex.tropic.populators.Populator_Mushrooms;
import me.simplex.tropic.populators.Populator_OreVein;
import me.simplex.tropic.populators.Populator_Ores;
import me.simplex.tropic.populators.Populator_Pumpkin;
import me.simplex.tropic.populators.Populator_Sugarcane;
import me.simplex.tropic.populators.Populator_Tree_Medium;
import me.simplex.tropic.populators.Populator_Tree_Small;
import me.simplex.tropic.populators.Populator_Tree_World;
import me.simplex.tropic.populators.Populator_Water_Lily;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class Tropic extends JavaPlugin {
	private Logger log;
	private Tropic_ChunkGenerator wgen;
	
	@Override
	public void onDisable() {}

	@Override
	public void onEnable() {
		log = getLogger();
	}
	
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return new Tropic_ChunkGenerator(1337, buildPopulators());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("player only command");
			return true;
		}
		
		Player player = (Player)sender;
		if (!player.hasPermission("tropic.command")) {
			player.sendMessage("Y U TRY COMMAND IF U NO HAZ PERMISSION?!");
			return true;
		}
		
		if (command.getName().equalsIgnoreCase("tropic")) {
			String worldname = "world_tropic";
			long seed = new Random().nextLong();
			//seed = 1337;
			// remove hardcoded seed
			switch (args.length) {
			case 0:  // /tropic
				break;
			case 1: // /tropic penisland
				worldname 	= args[0];
				break;
			case 2: // /tropic penisland 666
				worldname 	= args[0];
				seed 		= buildSeed(args[1]);
				break;
			default: return false;
			}
			
			if (worldExists(worldname)) {
				player.sendMessage(ChatColor.BLUE+"[Tropic] World "+ChatColor.WHITE+worldname+ChatColor.BLUE+" already exists. Porting to this world...");
				World w = getServer().getWorld(worldname);
				player.teleport(w.getSpawnLocation());
				return true;
			}
			else {
				player.sendMessage(ChatColor.BLUE+"[Tropic] Generating world "+ChatColor.WHITE+worldname+ChatColor.BLUE+" with seed "+ChatColor.WHITE+seed+ChatColor.BLUE+"...");
				wgen = new Tropic_ChunkGenerator(seed, buildPopulators());
				World w = WorldCreator.name(worldname).environment(Environment.NORMAL).seed(seed).generator(wgen).createWorld();
				log.info(player.getName()+" created a new world: "+worldname+" with seed "+seed);
				player.sendMessage("[Tropic] done. Porting to the generated world");
				player.teleport(w.getSpawnLocation());
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Builds a seed from a string
	 * 
	 * @param String seed user input
	 * @return long seed
	 */
	private long buildSeed(String s){
		long ret;
		try {
			ret = Long.parseLong(s);
		} catch (NumberFormatException e) {
			ret = s.hashCode();
		}
		return ret;
	}
	
	/**
	 * Build a List of all Populators
	 * @return a ArrayList<BlockPopulator> that contains all populators for a tropic world
	 */
	private List<BlockPopulator> buildPopulators(){
		List<BlockPopulator> populators_delayed = new ArrayList<BlockPopulator>();
		populators_delayed.add(new Populator_Tree_Medium());
		populators_delayed.add(new Populator_Tree_Small());
		populators_delayed.add(new Populator_Bush());
		populators_delayed.add(new Populator_Glowshroom());
		populators_delayed.add(new Populator_Melon());
		populators_delayed.add(new Populator_Pumpkin());
		populators_delayed.add(new Populator_Gravel());
		populators_delayed.add(new Populator_Sugarcane());
		populators_delayed.add(new Populator_Flowers());
		populators_delayed.add(new Populator_Mushrooms());
		populators_delayed.add(new Populator_Longgrass());
		populators_delayed.add(new Populator_Water_Lily());
		
		List<BlockPopulator> populators_main = new ArrayList<BlockPopulator>();
		populators_main.add(new Populator_Lake_And_Creek());
		populators_main.add(new Populator_Lava_Lakes());
		populators_main.add(new Populator_Caves());
		populators_main.add(new Populator_Ores());
		populators_main.add(new Populator_OreVein());
		populators_main.add(new Populator_Tree_World());
		
		populators_main.add(new Populator_Delayed(populators_delayed, this, getServer().getScheduler()));
		
		//populators_main.clear();
		return populators_main;
	}
	
	/**
	 * Checks if a world exists
	 * @param wname
	 * @return
	 */
	private boolean worldExists(String wname){
		List<World> worlds = getServer().getWorlds();
		for (World world : worlds) {
			if (world.getName().equalsIgnoreCase(wname)) {
				return true;
			}
		}
		return false;
	}

}
