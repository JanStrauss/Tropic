package me.simplex.tropic.populators;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.Vector;

public class Populator_Tree_Medium extends BlockPopulator {

	/* (non-Javadoc)
	 * @see org.bukkit.generator.BlockPopulator#populate(org.bukkit.World, java.util.Random, org.bukkit.Chunk)
	 */
	@Override
	public void populate(World world, Random rnd, Chunk source) {
		if (rnd.nextInt(100) < 60) {

			int x_tree = rnd.nextInt(16) + source.getX() * 16;
			int z_tree = rnd.nextInt(16) + source.getZ() * 16;
			int y_tree = world.getHighestBlockYAt(x_tree, z_tree);
			
			Block start = world.getBlockAt(x_tree, y_tree, z_tree);

			if (y_tree >= 64 && start.getRelative(BlockFace.DOWN).getType() == Material.GRASS) {
				//System.out.println("Medium Tree! " + x_tree + ", " + z_tree);
				createMediumTree(start.getLocation(), rnd);
			}
		}
	}

	/**
	 * Builds a 4 Block wide Medium high tree Tropic style
	 * 
	 * @param loc
	 * @param rnd
	 */
	private void createMediumTree(Location loc, Random rnd) {
		Set<Block> log_blocks = new HashSet<Block>();
		Set<Block> leaves_blocks = new HashSet<Block>();
		
		int height = rnd.nextInt(20) + 10;
		for (int x = 0; x < 2; x++) {
			for (int z = 0; z < 2; z++) {
				Block toHandle = loc.getBlock();
				for (int y = -1; y <= height; y++) {
					toHandle = loc.getBlock().getRelative(x, y, z);
					toHandle.setType(Material.LOG);
					toHandle.setData((byte) 3);
					// blocks.add(toHandle);
					if (y >= height * 0.33 && y <= height - 5 && rnd.nextInt(100) < 15) {
						createBranch(toHandle, log_blocks, rnd, leaves_blocks);
					}
				}
				createLeaves(toHandle, rnd, leaves_blocks);
			}
		}
		createLeavesOnChance(log_blocks, rnd, leaves_blocks);
		createVine(leaves_blocks, rnd, true);
		createVine(log_blocks, rnd, false);
	}

	/**
	 * Creates a Branch
	 * 
	 * @param toHandle
	 * @param blocks
	 * @param rnd
	 * @param leaves
	 */
	private void createBranch(Block toHandle, Set<Block> blocks, Random rnd, Set<Block> leaves) {
		int length = rnd.nextInt(10) + 3;

		Location loc = toHandle.getLocation();
		Vector direction = new Vector(rnd.nextDouble() - 0.5, rnd.nextDouble() * 0.025, rnd.nextDouble() - 0.5);

		for (int i = 0; i <= length; i++) {
			loc.add(direction);
			direction.add(new Vector(0.0, 0.033, 0.0));
			generateSphere(loc, blocks, 1);
		}
		createLeaves(loc.getBlock(), rnd, leaves);
	}

	/**
	 * Creates a sphere
	 * 
	 * @param center
	 * @param material
	 * @param blocks
	 * @param radius
	 */
	public void generateSphere(Location center, Set<Block> blocks, int radius) {
		int radius_squared = radius * radius;
		Vector c = new Vector(0, 0, 0);
		for (int x = -radius; x <= radius; x++)
			for (int z = -radius; z <= radius; z++)
				for (int y = -radius; y <= radius; y++) {
					// Calculate 3 dimensional distance
					Vector v = new Vector(x, y, z);
					// If it's within this radius gen the sphere
					if (c.distanceSquared(v) < radius_squared) {
						Block b = center.getBlock().getRelative(x, y, z);
						blocks.add(b);
						b.setType(Material.LOG);
						b.setData((byte) 3);
					}
				}
	}

	/**
	 * Creates leaves on a random chance
	 * 
	 * @param blocks
	 * @param rnd
	 * @param leaves
	 */
	private void createLeavesOnChance(Set<Block> blocks, Random rnd, Set<Block> leaves) {
		for (Block block : blocks) {
			if (rnd.nextInt(100) < 10) {
				createLeaves(block, rnd, leaves);
			}
		}
	}

	/**
	 * Creates a leaves halfsphere
	 * 
	 * @param block
	 * @param rnd
	 * @param leaves
	 */
	private void createLeaves(Block block, Random rnd, Set<Block> leaves) {
		int radius = rnd.nextInt(3) + 2;
		int radius_squared = radius * radius;
		Location center = block.getLocation();
		Vector c = new Vector(0, 0, 0);
		for (int x = -radius; x <= radius; x++)
			for (int z = -radius; z <= radius; z++)
				for (int y = 0; y < radius - 1; y++) {
					// Calculate 3 dimensional distance
					Vector v = new Vector(x, y, z);
					// If it's within this radius gen the sphere
					if (c.distanceSquared(v) <= radius_squared) {
						Block b = center.getBlock().getRelative(x, y, z);
						if (b.getType() == Material.AIR){
							b.setType(Material.LEAVES);
							b.setData((byte) 3);
							leaves.add(b);
						}
					}
				}
	}
	
	/**
	 * Creates the vines for the given blocks
	 * 
	 * @param blocks
	 * @param rnd
	 * @param leaves
	 */
	private void createVine(Set<Block> blocks, Random rnd, boolean leaves){
		HashMap<Block, BlockFace> toHandle = getOutsideBlocks(blocks);
		for (Block key : toHandle.keySet()) {
			if (rnd.nextInt(100) < (leaves ? 25 : 10)) {
				Block handle = key.getRelative(toHandle.get(key));
				for (int y = 0; y > -1 * (rnd.nextInt(12) + 3); y--) {
					if (handle.getType() == Material.AIR) {
						handle.setTypeIdAndData(Material.VINE.getId(), BlockFaceToVineData(toHandle.get(key)), false);
						handle = handle.getRelative(0, -1, 0);
					}
				}
			}
		}
	}
	
	/**
	 * Returns the blocks that are outside (facing an air-block)
	 * 
	 * @param leaves
	 * @return
	 */
	private HashMap<Block, BlockFace> getOutsideBlocks(Set<Block> leaves){
		HashMap<Block, BlockFace> outside_blocks = new HashMap<Block, BlockFace>();
		for (Block block : leaves) {
			BlockFace side = getAirFacingSide(block);
			if (side != null) {
				outside_blocks.put(block, side);
			}
		}
		return outside_blocks;
	}

	/**
	 * returns a {@link BlockFace} if the given {@link Block} faces an air-block, else null
	 * 
	 * @param block
	 * @return
	 */
	private BlockFace getAirFacingSide(Block block) {
		if (block.getRelative(BlockFace.NORTH).getType() == Material.AIR) {
			return BlockFace.NORTH;
		}
		if (block.getRelative(BlockFace.EAST).getType() == Material.AIR) {
			return BlockFace.EAST;
		}
		if (block.getRelative(BlockFace.SOUTH).getType() == Material.AIR) {
			return BlockFace.SOUTH;
		}
		if (block.getRelative(BlockFace.WEST).getType() == Material.AIR) {
			return BlockFace.WEST;
		}
		return null;
	}
	
	/**
	 * Returns the BlockData value for the given {@link BlockFace}
	 * 
	 * @param face
	 * @return
	 */
	private byte BlockFaceToVineData(BlockFace face){
		switch (face) {
			case SOUTH: return 2; // 
			case WEST:  return 4; //
			case NORTH: return 8; //
			case EAST:  return 1; //
			default:    return 0;
		}
	}
}
