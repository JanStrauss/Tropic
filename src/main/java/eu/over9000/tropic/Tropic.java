/*
 * Copyright 2012 s1mpl3x
 * 
 * This file is part of Tropic.
 * 
 * Tropic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Tropic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Tropic If not, see <http://www.gnu.org/licenses/>.
 */
package eu.over9000.tropic;

import eu.over9000.tropic.populators.*;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class Tropic extends JavaPlugin {
	private final Logger log = Logger.getLogger("Minecraft");
	private TropicChunkGenerator wgen;

	private static final String DEFAULT_WORLD_NAME = "world_tropic";
	private static final String WORLD_PREFIX = "world_";
	private static final List<BlockPopulator> populators = buildPopulators();

	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		wgen = new TropicChunkGenerator(populators);
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("player only command");
			return true;
		}
		final Player player = (Player) sender;
		if (!player.hasPermission("tropic.command")) {
			player.sendMessage("You don't have the permission required to use this plugin");
			return true;
		}
		if (command.getName().equalsIgnoreCase("tropic")) {
			String worldName;
			final long seed;

			switch (args.length) {
				case 0:  // /tropic
					seed = ThreadLocalRandom.current().nextLong();
					worldName = DEFAULT_WORLD_NAME;
					break;
				case 1: // /tropic WORLD_NAME
					seed = ThreadLocalRandom.current().nextLong();
					worldName = args[0];
					break;
				case 2: // /tropic WORLD_NAME SEED
					seed = buildSeed(args[1]);
					worldName = args[0];
					break;
				default:
					player.sendMessage("Syntax: /tropic <WORLD_NAME> <SEED>");
					return false;
			}

			if (!worldName.startsWith(WORLD_PREFIX)) {
				worldName = WORLD_PREFIX + worldName;
			}

			player.sendMessage(ChatColor.BLUE + "[Tropic] Generating/loading world " + ChatColor.WHITE + worldName + ChatColor.BLUE + " with seed " + ChatColor.WHITE + seed + ChatColor.BLUE + "...");
			final World world = WorldCreator.name(worldName).environment(Environment.NORMAL).seed(seed).generator(wgen).createWorld();
			log.info("[Tropic] " + player.getName() + " created/loaded world: " + worldName + " with seed " + world.getSeed());

			player.sendMessage(ChatColor.BLUE + "[Tropic] done, teleporting to spawn of the generated world");
			player.teleport(world.getSpawnLocation());

			return true;
		}
		return false;
	}

	/**
	 * Build a List of all Populators
	 *
	 * @return a ArrayList<BlockPopulator> that contains all populators for a tropic world
	 */
	private static List<BlockPopulator> buildPopulators() {
		final List<BlockPopulator> populators = new ArrayList<>();
		populators.add(new PopulatorLakeAndCreek());
		populators.add(new PopulatorLavaLakes());
		populators.add(new PopulatorCaves());
		populators.add(new PopulatorOres());
		populators.add(new PopulatorOreVein());
		populators.add(new PopulatorTreeWorld());

		populators.add(new PopulatorTreeMedium());
		populators.add(new PopulatorTreeSmall());
		populators.add(new PopulatorBush());
		populators.add(new PopulatorGlowshroom());
		populators.add(new PopulatorMelon());
		populators.add(new PopulatorPumpkin());
		populators.add(new PopulatorGravel());
		populators.add(new PopulatorSugarcane());
		populators.add(new PopulatorFlowers());
		populators.add(new PopulatorMushrooms());
		populators.add(new PopulatorLonggrass());
		populators.add(new PopulatorWaterLily());

		return populators;
	}

	/**
	 * Builds a seed from a string
	 *
	 * @param s seed user input
	 * @return long seed
	 */
	private long buildSeed(final String s) {
		try {
			return Long.parseLong(s);
		} catch (final NumberFormatException e) {
			return s.hashCode();
		}
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(final String worldName, final String id) {
		log.info("[Tropic] getDefaultWorldGenerator(" + worldName + ")");
		return wgen;
	}
}
