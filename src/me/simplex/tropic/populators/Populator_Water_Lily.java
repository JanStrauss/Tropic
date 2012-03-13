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
import org.bukkit.generator.BlockPopulator;

public class Populator_Water_Lily extends BlockPopulator {

	@Override
	public void populate(World w, Random rnd, Chunk c) {

		for (int x = c.getX()*16; x < c.getX()*16 + 16; x++) {
			for (int z = c.getZ()*16; z < c.getZ()*16 + 16; z++) {
				Block over_water = getHighestBlock(c, x, z);
				if (over_water != null) {
					if (over_water.getType() == Material.AIR) {
						int depth = waterDepth(over_water.getRelative(0, -1, 0));
						if (depth <= 5) {
							if (rnd.nextInt(100) < (8 * (6 - depth))) {
								over_water.setType(Material.WATER_LILY);
							}
						}
					}
				}
			}
		}	
	}
	
	/**
	 * Iteratively determines the highest water
	 * @param chunk
	 * @param x
	 * @param z
	 * @return Block highest non-air
	 */
	private Block getHighestBlock(Chunk chunk, int x, int z) {
		Block block = null;
		// Return the highest block
		for(int i=chunk.getWorld().getMaxHeight(); i>=0; i--)
			if((block = chunk.getBlock(x, i, z)).getTypeId() == 8 || (block = chunk.getBlock(x, i, z)).getTypeId() == 9)
				return block.getRelative(0, 1, 0);
		// And as a matter of completeness, return the lowest point
		return block;
	}
	
	/**
	 * gets the water depth
	 * 
	 * @param surface
	 * @return
	 */
	private int waterDepth(Block surface){
		int depth = 0;
		while (surface.getTypeId() == 9 || surface.getTypeId() == 8) {
			depth++;
			surface = surface.getRelative(0,-1,0);
			if (depth > 5) {
				break;
			}
		}
		return depth;
	}
}
