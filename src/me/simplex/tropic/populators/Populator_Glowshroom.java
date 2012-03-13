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
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

public class Populator_Glowshroom extends BlockPopulator {

	@Override
	public void populate(World world, Random rnd, Chunk source) {
		if (rnd.nextInt(100) >= 7) {
			return;
		}
		
		Block base = getHighestBlock(source, rnd.nextInt(16), rnd.nextInt(16));
		if (base != null) {
			createGlowShroom(base.getRelative(0, 1, 0));
		}
	}
	
	private void createGlowShroom(Block base){
		
		// Stem
		changeBlock(base,  0, 0,  0, 100, (byte) 10);
		changeBlock(base,  0, 1,  0, 100, (byte) 10);
		changeBlock(base,  0, 2,  0, 100, (byte) 10);
		
		// Glowstone
		changeBlock(base, -1, 2, -1,  89, (byte) 0);
		changeBlock(base, -1, 2,  0,  89, (byte) 0);
		changeBlock(base, -1, 2,  1,  89, (byte) 0);
		
		changeBlock(base,  1, 2, -1,  89, (byte) 0);
		changeBlock(base,  1, 2,  0,  89, (byte) 0);
		changeBlock(base,  1, 2,  1,  89, (byte) 0);
		
		changeBlock(base,  0, 2,  1,  89, (byte) 0);
		changeBlock(base,  0, 2, -1,  89, (byte) 0);
		
		// Inner
		changeBlock(base, -1, 3, -1, 100, (byte) 0);
		changeBlock(base, -1, 3,  0, 100, (byte) 0);
		changeBlock(base, -1, 3,  1, 100, (byte) 0);
		
		changeBlock(base,  1, 3, -1, 100, (byte) 0);
		changeBlock(base,  1, 3,  0, 100, (byte) 0);
		changeBlock(base,  1, 3,  1, 100, (byte) 0);
		
		changeBlock(base,  0, 3,  1, 100, (byte) 0);
		changeBlock(base,  0, 3,  0, 100, (byte) 0);
		changeBlock(base,  0, 3, -1, 100, (byte) 0);
		
		//Top
		changeBlock(base, -1, 4, -1, 100, (byte) 1);
		changeBlock(base, -1, 4,  0, 100, (byte) 4);
		changeBlock(base, -1, 4,  1, 100, (byte) 7);
		
		changeBlock(base,  0, 4,  1, 100, (byte) 8);
		changeBlock(base,  0, 4,  0, 100, (byte) 5);
		changeBlock(base,  0, 4, -1, 100, (byte) 2);
		
		changeBlock(base,  1, 4, -1, 100, (byte) 3);
		changeBlock(base,  1, 4,  0, 100, (byte) 6);
		changeBlock(base,  1, 4,  1, 100, (byte) 9);		
		
		//North
		changeBlock(base, -1, 2, -2, 100, (byte) 1);
		changeBlock(base,  0, 2, -2, 100, (byte) 2);
		changeBlock(base,  1, 2, -2, 100, (byte) 3);
		
		changeBlock(base, -1, 3, -2, 100, (byte) 1);
		changeBlock(base,  0, 3, -2, 100, (byte) 2);
		changeBlock(base,  1, 3, -2, 100, (byte) 3);
		
		//East
		changeBlock(base,  2, 2, -1, 100, (byte) 3);
		changeBlock(base,  2, 2,  0, 100, (byte) 6);
		changeBlock(base,  2, 2,  1, 100, (byte) 9);
	
		changeBlock(base,  2, 3, -1, 100, (byte) 3);
		changeBlock(base,  2, 3,  0, 100, (byte) 6);
		changeBlock(base,  2, 3,  1, 100, (byte) 9);
		
		//South
		changeBlock(base, -1, 2,  2, 100, (byte) 7);
		changeBlock(base,  0, 2,  2, 100, (byte) 8);
		changeBlock(base,  1, 2,  2, 100, (byte) 9);
		
		changeBlock(base, -1, 3,  2, 100, (byte) 7);
		changeBlock(base,  0, 3,  2, 100, (byte) 8);
		changeBlock(base,  1, 3,  2, 100, (byte) 9);
		
		//West
		changeBlock(base, -2, 2, -1, 100, (byte) 1);
		changeBlock(base, -2, 2,  0, 100, (byte) 4);
		changeBlock(base, -2, 2,  1, 100, (byte) 7);
		
		changeBlock(base, -2, 3, -1, 100, (byte) 1);
		changeBlock(base, -2, 3,  0, 100, (byte) 4);
		changeBlock(base, -2, 3,  1, 100, (byte) 7);
	}
	
	private void changeBlock(Block base, int x, int y, int z, int mat, byte data){
		base.getRelative(x, y, z).setTypeIdAndData(mat, data, false);
	}
	
	/**
	 * Iteratively determines the highest grass block
	 * @param chunk
	 * @param x
	 * @param z
	 * @return Block highest non-air
	 */
	public Block getHighestBlock(Chunk chunk, int x, int z) {
		Block block = null;
		for(int i=chunk.getWorld().getMaxHeight(); i>=0; i--){
			if((block = chunk.getBlock(x, i, z)).getTypeId() == 2){
				return block;
			}
		}
		return block;
	}

}
