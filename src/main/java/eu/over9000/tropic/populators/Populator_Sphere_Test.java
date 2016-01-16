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

public class Populator_Sphere_Test extends BlockPopulator {

	@Override
	public void populate(World world, Random rnd, Chunk source) {

		int chance = rnd.nextInt(100);
		if (chance < 5) {
			Block center1 = source.getBlock(rnd.nextInt(16), 100, rnd.nextInt(16));
			Block center2 = source.getBlock(rnd.nextInt(16), 50, rnd.nextInt(16));

			long start1 = System.currentTimeMillis();
			buildReference(center1, 10);
			long end1 = System.currentTimeMillis();

			long start2 = System.currentTimeMillis();
			buildTest(center2, 10);
			long end2 = System.currentTimeMillis();

			//System.out.println("RESULT 1: " + (end1 - start1) + "ms");
			//System.out.println("RESULT 2: " + (end2 - start2) + "ms");
			System.out.println("DIFFERENCE: " + ((end2 - start2) - (end1 - start1)) + "ms");
		}
	}

	private void buildReference(Block start, int radius) {
		Location loc = start.getLocation();
		for (int x = -radius; x < radius; x++) {
			for (int y = -radius; y < radius; y++) {
				for (int z = -radius; z < radius; z++) {
					Block handle = start.getRelative(x, y, z);
					if (loc.distance(handle.getLocation()) <= radius) {
						handle.setType(Material.GOLD_BLOCK);
					}
				}
			}
		}
	}

	private void buildTest(Block start, int radius) {
		Location loc = start.getLocation();
		int radius_squared = radius * radius;
		for (int x = -radius; x < radius; x++) {
			for (int y = -radius; y < radius; y++) {
				for (int z = -radius; z < radius; z++) {
					Block handle = start.getRelative(x, y, z);
					if (loc.distanceSquared(handle.getLocation()) <= radius_squared) {
						handle.setType(Material.LAPIS_BLOCK);
					}
				}
			}
		}
	}
}
