package me.simplex.tropic.populators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

public class Populator_Sphere_Test extends BlockPopulator {

	@Override
	public void populate(World world, Random rnd, Chunk source) {

		int chance = rnd.nextInt(100);
		if (chance < 5) {
			Block center1 = source.getBlock(rnd.nextInt(16), 100, rnd.nextInt(16));
			Block center2 = source.getBlock(rnd.nextInt(16),  50, rnd.nextInt(16));
			
			long start1 = System.currentTimeMillis();
			buildReference(center1, 10);
			long end1 = System.currentTimeMillis();
			
			long start2 = System.currentTimeMillis();
			buildTest(center2, 10);
			long end2 = System.currentTimeMillis();
			
			//System.out.println("RESULT 1: " + (end1 - start1) + "ms");
			//System.out.println("RESULT 2: " + (end2 - start2) + "ms");
			System.out.println("DIFFERENCE: " + ((end2 - start2)-(end1 - start1))+ "ms");
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
