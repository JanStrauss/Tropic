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

public class Populator_Flowers extends BlockPopulator {

	@Override
	public void populate(World world, Random random, Chunk source) {
		int chance = random.nextInt(100);
		if (chance < 25) {
			int flowercount = random.nextInt(5) + 2;
			int type = random.nextInt(100);
			for (int t = 0; t <= flowercount; t++) {
				int flower_x = random.nextInt(15);
				int flower_z = random.nextInt(15);

				Block handle = getHighestBlock(source, flower_x, flower_z);
				if (handle != null) {
					if (handle.getType() == Material.AIR) {
						if (type < 33) {
							handle.setType(Material.RED_ROSE);
						} else {
							handle.setType(Material.YELLOW_FLOWER);
						}
					}
				}
			}
		}
	}

	/**
	 * Iteratively determines the highest grass block
	 *
	 * @param chunk
	 * @param x
	 * @param z
	 * @return Block highest non-air
	 */
	private Block getHighestBlock(Chunk chunk, int x, int z) {
		Block block = null;
		// Return the highest block
		for (int i = chunk.getWorld().getMaxHeight(); i >= 0; i--)
			if ((block = chunk.getBlock(x, i, z)).getTypeId() == 2)
				return block.getRelative(0, 1, 0);
		// And as a matter of completeness, return the lowest point
		return block;
	}
}
