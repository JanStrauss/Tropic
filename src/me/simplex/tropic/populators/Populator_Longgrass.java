package me.simplex.tropic.populators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

public class Populator_Longgrass extends BlockPopulator {

	@Override
	public void populate(World world, Random random, Chunk source) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int chance = random.nextInt(100);
				if (chance < 75) {
					Block handle = getHighestBlock(source, x, z);
					if (handle != null) {
						if (handle.getType() == Material.AIR && handle.getLightLevel() >= 4) {
							handle.setTypeIdAndData(Material.LONG_GRASS.getId(), (byte) (random.nextInt(100) < 10 ? 2 : 1), false);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Iteratively determines the highest grass block
	 * @param chunk
	 * @param x
	 * @param z
	 * @return Block highest non-air
	 */
	private Block getHighestBlock(Chunk chunk, int x, int z) {
		Block block = null;
		// Return the highest block
		for(int i=chunk.getWorld().getMaxHeight(); i>=0; i--)
			if((block = chunk.getBlock(x, i, z)).getTypeId() == 2)
				return block.getRelative(0, 1, 0);
		// And as a matter of completeness, return the lowest point
		return block;
	}
}
