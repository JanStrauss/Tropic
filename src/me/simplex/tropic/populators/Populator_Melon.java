package me.simplex.tropic.populators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

public class Populator_Melon extends BlockPopulator {

    int chancePer100, numMelons, depositRadius;

    public Populator_Melon() {
        this.chancePer100 = 3;
        this.numMelons = 7;
        this.depositRadius = 10;
    }

    @Override
    public void populate(World world, Random random, Chunk source) {
        // Check if we should place a melon patch on this chunk
        if (random.nextInt(100) < chancePer100) {
            int x = (source.getX() << 4) + random.nextInt(16);
            int z = (source.getZ() << 4) + random.nextInt(16);

            for (int i = 0; i < random.nextInt(numMelons); i++) {
                // Pick a random spot within the radius
                int cx = x + random.nextInt(depositRadius * 2) - depositRadius;
                int cz = z + random.nextInt(depositRadius * 2) - depositRadius;

                Block base = getHighestBlock(world, cx, cz);
                if (base != null) {
					base.getRelative(0, 1, 0).setType(Material.MELON_BLOCK);
				}
            }
        }
    }
    
	/**
	 * Iteratively determines the highest grass block
	 * @param world
	 * @param x
	 * @param z
	 * @return Block highest non-air
	 */
	private Block getHighestBlock(World world, int x, int z) {
		Block block = null;
		// Return the highest block
		for(int i=world.getMaxHeight(); i>=0; i--)
			if((block = world.getBlockAt(x, i, z)).getTypeId() == 2)
				return block;
		// And as a matter of completeness, return the lowest point
		return block;
	}
}
