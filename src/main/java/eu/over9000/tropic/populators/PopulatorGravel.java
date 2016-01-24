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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class PopulatorGravel extends BlockPopulator {

	@Override
	public void populate(final World world, final Random rnd, final Chunk source) {
		final int chance = rnd.nextInt(100);
		if (chance < 45) {
			final Block start = source.getBlock(rnd.nextInt(16), 60, rnd.nextInt(16));
			if (start.getType() == Material.SAND) {
				replaceSandWithGravel(start, rnd.nextInt(5) + 5);
			}
		}
	}

	private void replaceSandWithGravel(final Block start, final int radius) {
		final Location loc = start.getLocation();
		final int radius_squared = radius * radius;
		for (int x = -radius; x < radius; x++) {
			for (int y = -radius; y < radius; y++) {
				for (int z = -radius; z < radius; z++) {
					final Block handle = start.getRelative(x, y, z);
					if (handle.getType() == Material.SAND) {
						if (loc.distanceSquared(handle.getLocation()) < radius_squared) {
							handle.setType(Material.GRAVEL);
						}
					}
				}
			}
		}
	}
}
