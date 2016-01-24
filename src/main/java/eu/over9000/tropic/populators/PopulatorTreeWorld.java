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
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;


public class PopulatorTreeWorld extends BlockPopulator {
	private final Set<Block> trunkblocks = new HashSet<>();
	private final Set<Block> rootblocks = new HashSet<>();
	private final Set<Block> branchblocks = new HashSet<>();

	private final AtomicBoolean active = new AtomicBoolean(false);

	@Override
	public void populate(final World world, final Random rand, final Chunk chunk) {
		if (rand.nextFloat() > 0.01) {
			return;
		}

		if (!active.compareAndSet(false, true)) {
			return;
		}

		trunkblocks.clear();
		rootblocks.clear();
		branchblocks.clear();

		final int num = rand.nextInt(5) + 5;
		Block start = this.getHighestBlock(chunk, rand.nextInt(8), rand.nextInt(8));


		if (start.getY() < 62 || start.getY() > 110) {
			active.set(false);
			return;
		}

		//System.out.println("WorldTree! " + chunk.getX() + "," + chunk.getZ());
		//chance to have "air" trees
		if (rand.nextInt(100) < 10) {
			start = start.getRelative(0, 7, 0);
		}


		createRoots(num, start, rand, rootblocks);
		final Block branch_start = createTrunk(num, start, rand, trunkblocks);

		final Set<Block> force_leaves;
		force_leaves = createBranches(num, branch_start, rand, branchblocks);
		force_leaves.addAll(createBranches(num + 2, branch_start.getRelative(0, -10, 0), rand, branchblocks));

		final Set<Block> leavesblocks;
		leavesblocks = createLeaves(branchblocks, rand, true);
		leavesblocks.addAll(createLeaves(force_leaves, rand, false));

		buildBlocks(rootblocks, Material.LOG, (byte) 3, false);
		buildBlocks(trunkblocks, Material.LOG, (byte) 3, false);
		buildBlocks(branchblocks, Material.LOG, (byte) 3, false);
		buildBlocks(leavesblocks, Material.LEAVES, (byte) 3, true);

		createVine(leavesblocks, rand, true);
		createVine(trunkblocks, rand, false);
		createVine(rootblocks, rand, false);

		active.set(false);
	}

	/**
	 * @param num
	 * @param start
	 * @param rand
	 * @param blocks
	 */
	public void createRoots(final int num, final Block start, final Random rand, final Set<Block> blocks) {
		int radius = 3;
		for (int j = 0; j <= num + 2; j++) {

			final int length = 15 + rand.nextInt(35);

			final Vector direction = new Vector(getRandom(rand), rand.nextDouble() * 0.33, getRandom(rand));

			final Location loc = start.getLocation();
			generateSphere(loc, blocks, radius, false, true);
			// What does this even DO?
			for (int i = 0; i < length; i++) {
				loc.add(direction);
				if (loc.getY() < 50)
					break;
				direction.subtract(new Vector(0.0, 0.05, 0.0));
				generateSphere(loc, blocks, radius, false, true);
				if (rand.nextInt(100) < 10 && radius >= 2) {
					radius--;
				}
			}
		}
	}

	/**
	 * @param num
	 * @param start
	 * @param rand
	 * @param blocks
	 * @return
	 */
	public Set<Block> createBranches(final int num, final Block start, final Random rand, final Set<Block> blocks) {
		final HashSet<Block> endblocks = new HashSet<>();
		int radius = 3;
		for (int j = 0; j <= num + 3; j++) {

			final int length = 15 + rand.nextInt(15);

			final Vector direction = new Vector(getRandom(rand), rand.nextDouble() * 0.025, getRandom(rand));

			final Location loc = start.getLocation();
			generateSphere(loc, blocks, radius, true, true);
			for (int i = 0; i <= length; i++) {
				loc.add(direction);
				direction.add(new Vector(0.0, (rand.nextDouble() - 0.5) * 0.5, 0.0));
				generateSphere(loc, blocks, radius, true, true);

				if (rand.nextInt(100) < 15 && radius > 2) {
					radius--;
				}
			}
			endblocks.add(loc.getBlock());
		}
		return endblocks;
	}

	/**
	 * @param num
	 * @param block
	 * @param rand
	 * @param blocks
	 * @return
	 */
	public Block createTrunk(final int num, Block block, final Random rand, final Set<Block> blocks) {
		final int start_y = block.getY();
		int radius = rand.nextBoolean() ? 4 : 5;
		for (int y = 0; y < num * 3 + radius * 4; y = y + 3) {
			block = block.getLocation().add(rand.nextInt(2) - 1, radius / 2 + 2, rand.nextInt(2) - 1).getBlock();
			generateSphere(block.getLocation(), blocks, radius, false, rand.nextBoolean());
			if (rand.nextInt(100) < 3 && radius >= 3) {
				radius--;
			}
			if (block.getY() - start_y >= 70 - rand.nextInt(35)) {
				return block;
			}
		}
		return block;
	}

	/**
	 * @param rand
	 * @return
	 */
	public double getRandom(final Random rand) {
		double r = rand.nextDouble() * 2 - 1;
		while (Math.abs(r) < 0.3) {
			r = rand.nextDouble() * 2 - 1;
		}
		return r;
	}

	/**
	 * @param blocks
	 * @param rand
	 * @param random
	 * @return
	 */
	private Set<Block> createLeaves(final Set<Block> blocks, final Random rand, final boolean random) {
		final Set<Block> leaves = new HashSet<>();
		for (final Block block : blocks) {
			if (!random || rand.nextInt(200) < 3) {
				final int radius = rand.nextBoolean() ? 4 : 5;
				final int radius_squared = radius * radius;
				final Location center = block.getLocation();
				final Vector c = new Vector(0, 0, 0);
				for (int x = -radius; x <= radius; x++)
					for (int z = -radius; z <= radius; z++)
						for (int y = 0; y <= radius - 1; y++) {
							// Calculate 3 dimensional distance
							final Vector v = new Vector(x, y, z);
							// If it's within this radius gen the sphere
							if (c.distanceSquared(v) <= radius_squared) {
								final Block b = center.getBlock().getRelative(x, y, z);
								leaves.add(b);
							}
						}
			}
		}
		return leaves;
	}

	/**
	 * @param center
	 * @param blocks
	 * @param radius
	 * @param ignore_height
	 * @param allow_same
	 */
	private void generateSphere(final Location center, final Set<Block> blocks, final int radius, final boolean ignore_height, final boolean allow_same) {
		if (!ignore_height) {
			if (center.getBlock().getY() < 50) {
				return;
			}
		}
		final int radius_squared = radius * radius;
		final Vector c = new Vector(0, 0, 0);
		for (int x = -radius; x <= radius; x++)
			for (int z = -radius; z <= radius; z++)
				for (int y = -radius; y <= radius; y++) {
					// Calculate 3 dimensional distance
					final Vector v = new Vector(x, y, z);
					// If it's within this radius gen the sphere
					if (c.distanceSquared(v) < radius_squared) {
						final Block b = center.getBlock().getRelative(x, y, z);
						// Check if the block is already MOSSY_COBBLESTONE
						if (checkMaterialIsModdable(b)) {
							blocks.add(b);
						}
					} else if (allow_same && (c.distanceSquared(v) == radius_squared)) {
						final Block b = center.getBlock().getRelative(x, y, z);
						if (checkMaterialIsModdable(b)) {
							blocks.add(b);
						}
					}
				}
	}

	/**
	 * Iteratively determines the highest grass block
	 *
	 * @param chunk
	 * @param x
	 * @param z
	 * @return Block highest non-air
	 */
	private Block getHighestBlock(final Chunk chunk, final int x, final int z) {
		Block block = null;
		// Return the highest block
		for (int i = 127; i >= 0; i--)
			if ((block = chunk.getBlock(x, i, z)).getTypeId() == 2)
				return block;
		// And as a matter of completeness, return the lowest point
		return block;
	}

	/**
	 * @param blocks
	 * @param rnd
	 * @param leaves
	 */
	private void createVine(final Set<Block> blocks, final Random rnd, final boolean leaves) {
		final HashMap<Block, BlockFace> toHandle = getOutsideBlocks(blocks);
		for (final Block key : toHandle.keySet()) {
			if (rnd.nextInt(100) < (leaves ? 25 : 10)) {
				Block handle = key.getRelative(toHandle.get(key));
				for (int y = 0; y > -1 * (rnd.nextInt(45) + 10); y--) {
					if (handle.getType() == Material.AIR) {
						handle.setTypeIdAndData(Material.VINE.getId(), BlockFaceToVineData(toHandle.get(key)), false);
						handle = handle.getRelative(0, -1, 0);
					} else {
						break;
					}
				}
			}
		}
	}

	/**
	 * @param leaves
	 * @return
	 */
	private HashMap<Block, BlockFace> getOutsideBlocks(final Set<Block> leaves) {
		final HashMap<Block, BlockFace> outside_blocks = new HashMap<>();
		for (final Block block : leaves) {
			final BlockFace side = getAirFacingSide(block);
			if (side != null) {
				outside_blocks.put(block, side);
			}
		}
		return outside_blocks;
	}

	/**
	 * @param block
	 * @return
	 */
	private BlockFace getAirFacingSide(final Block block) {
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
	 * @param face
	 * @return
	 */
	private byte BlockFaceToVineData(final BlockFace face) {
		switch (face) {
			case SOUTH:
				return 2; //
			case WEST:
				return 4; //
			case NORTH:
				return 8; //
			case EAST:
				return 1; //
			default:
				return 0;
		}
	}

	/**
	 * @param block
	 * @return
	 */
	private boolean checkMaterialIsModdable(final Block block) {
		return block.getType() == Material.AIR ||
				block.getType() == Material.DIRT ||
				block.getType() == Material.GRASS ||
				block.getType() == Material.WATER ||
				block.getType() == Material.STATIONARY_WATER ||
				block.getType() == Material.SAND;
	}

	/**
	 * @param blocks
	 * @param material
	 * @param data
	 * @param checkIsAir
	 */
	private void buildBlocks(final Set<Block> blocks, final Material material, final byte data, final boolean checkIsAir) {
		if (checkIsAir) {
			for (final Block b : blocks) {
				if (b.getType() == Material.AIR) {
					b.setType(material);
					b.setData(data);
				}
			}
		} else {
			for (final Block b : blocks) {
				b.setType(material);
				b.setData(data);
			}
		}
	}
}
