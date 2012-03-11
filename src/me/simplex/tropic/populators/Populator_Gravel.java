package me.simplex.tropic.populators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

public class Populator_Gravel extends BlockPopulator {

	@Override
	public void populate(World world, Random rnd, Chunk source) {
		int chance = rnd.nextInt(100);
		if (chance < 45) {
			Block start = source.getBlock(rnd.nextInt(16), 60, rnd.nextInt(16));
			if (start.getType() == Material.SAND) {
				replaceSandWithGravel(start, rnd.nextInt(5)+5);
			}
		}
	}
	
	private void replaceSandWithGravel(Block start, int radius) {
		Location loc = start.getLocation();
		int radius_squared = radius * radius;
		for (int x = -radius; x < radius; x++) {
			for (int y = -radius; y < radius; y++) {
				for (int z = -radius; z < radius; z++) {
					Block handle = start.getRelative(x, y, z);
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
