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
package me.simplex.tropic.populators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

public class Populator_Sugarcane extends BlockPopulator {

	private int canePatchChance;
	private Material cane;

	private BlockFace[] faces = { BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST };

	public Populator_Sugarcane() {
		this.canePatchChance = 80;
		cane = Material.SUGAR_CANE_BLOCK;
	}

	@Override
	public void populate(World world, Random random, Chunk source) {

		// Check if we should plant a flower patch here
		if (random.nextInt(100) < canePatchChance) {
			for (int i = 0; i < 16; i++) {
				Block b;
				if (random.nextBoolean())
					b = getHighestBlock(source, random.nextInt(16), i);
				else
					b = getHighestBlock(source, i, random.nextInt(16));
				
				if (b != null) {
					createCane(b, random);
				}
			}
		}
	}

	public void createCane(Block b, Random rand) {
		// Is water nearby?
		boolean create = false;
		for (BlockFace face : faces) {
			if (b.getRelative(face).getType().name().toLowerCase().contains("water")) {
				create = true;
			}
		}
		// Only create if water is nearby
		if (!create)
			return;

		for (int i = 1; i < rand.nextInt(3) + 3; i++)
			b.getRelative(0, i, 0).setType(cane);
	}

	/**
	 * Iteratively determines the highest grass/sand block
	 * 
	 * @param chunk
	 * @param x
	 * @param z
	 * @return Block highest non-air
	 */
	public Block getHighestBlock(Chunk chunk, int x, int z) {
		Block block = null;
		// Return the highest block
		for (int i = chunk.getWorld().getMaxHeight(); i >= 0; i--) {
			if ((block = chunk.getBlock(x, i, z)).getTypeId() == 9 || (block = chunk.getBlock(x, i, z)).getTypeId() == 8) {
				return null;
			}
			if ((block = chunk.getBlock(x, i, z)).getTypeId() == 2 || (block = chunk.getBlock(x, i, z)).getTypeId() == 12) {
				return block;
			}
		}
		return null;
	}
}
