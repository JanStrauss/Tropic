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

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.Vector;

import java.util.Random;

public class PopulatorBush extends BlockPopulator {

	/* (non-Javadoc)
	 * @see org.bukkit.generator.BlockPopulator#populate(org.bukkit.World, java.util.Random, org.bukkit.Chunk)
	 */
	@Override
	public void populate(final World world, final Random rnd, final Chunk source) {
		final int runs = rnd.nextInt(7) + 4;
		for (int i = 0; i <= runs; i++) {

			final int x_bush = rnd.nextInt(16) + source.getX() * 16;
			final int z_bush = rnd.nextInt(16) + source.getZ() * 16;

			final Block start = world.getHighestBlockAt(x_bush, z_bush);

			if (start != null) {
				//System.out.println("Bush! " + x_tree + ", " + z_tree);
				//createBush(start.getLocation(), rnd);
				world.generateTree(start.getLocation(), TreeType.JUNGLE_BUSH);
			}
		}
	}

	/**
	 */
	private void createBush(final Location loc, final Random rnd) {
		final Block toHandle = loc.getBlock();
		toHandle.setType(Material.LOG);
		createLeaves(toHandle, rnd);
	}

	/**
	 */
	private void createLeaves(final Block block, final Random rnd) {
		final int radius = rnd.nextInt(3) + 2;
		final int radius_squared = radius * radius;
		final Location center = block.getLocation();
		final Vector c = new Vector(0, 0, 0);
		for (int x = -radius; x <= radius; x++)
			for (int z = -radius; z <= radius; z++)
				for (int y = 0; y <= radius - (radius == 4 ? 2 : 1); y++) {
					// Calculate 3 dimensional distance
					final Vector v = new Vector(x, y, z);
					// If it's within this radius gen the sphere
					if (c.distanceSquared(v) <= radius_squared) {
						final Block b = center.getBlock().getRelative(x, y, z);
						if (b.getType() == Material.AIR || b.getType() == Material.VINE) {
							b.setType(Material.LEAVES);
						}
					}
				}
	}

}
