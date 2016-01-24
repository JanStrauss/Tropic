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
package eu.over9000.tropic.populators;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class PopulatorOreVein extends BlockPopulator {

	int chance = 70;

	private final Material common = Material.COAL_ORE;
	private final Material sparse = Material.IRON_ORE;
	private final Material rare = Material.GOLD_ORE;
	private final Material extra = Material.DIAMOND_ORE;

	private final int c = 25;
	private final int s = 15;
	private final int r = 5;
	private final int e = 2;

	private final int[] cl = {23, 24, 25, 32, 35, 36, 40, 41, 42, 43, 49, 50, 51, 60, 64};
	private final int[] sl = {14, 26, 29, 30, 31, 33, 35, 40, 50, 60, 61};
	private final int[] rl = {12, 14, 16, 20, 25, 40, 60};
	private final int[] el = {8, 12, 14};

	private static boolean populating = false;

	@Override
	public void populate(final World world, final Random rand, final Chunk chunk) {
		if (populating)
			return;
		if (rand.nextInt(100) > chance)
			return;
		populating = true;
		// common
		for (int i = 0; i < c; i++) {
			final Block block = chunk.getBlock(rand.nextInt(16), cl[rand.nextInt(cl.length)], rand.nextInt(16));
			if (block.getType() == Material.STONE)
				block.setType(common);
		}
		// spare
		for (int i = 0; i < s; i++) {
			final Block block = chunk.getBlock(rand.nextInt(16), sl[rand.nextInt(sl.length)], rand.nextInt(16));
			if (block.getType() == Material.STONE)
				block.setType(sparse);
		}
		// rare
		for (int i = 0; i < r; i++) {
			final Block block = chunk.getBlock(rand.nextInt(16), rl[rand.nextInt(rl.length)], rand.nextInt(16));
			if (block.getType() == Material.STONE)
				block.setType(rare);
		}
		// extra-rare
		for (int i = 0; i < e; i++) {
			final Block block = chunk.getBlock(rand.nextInt(16), el[rand.nextInt(el.length)], rand.nextInt(16));
			if (block.getType() == Material.STONE)
				block.setType(extra);
		}
		populating = false;
	}

}
