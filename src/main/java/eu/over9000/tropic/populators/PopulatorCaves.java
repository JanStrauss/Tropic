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
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

/**
 * BlockPopulator for snake-based caves.
 */
public class PopulatorCaves extends BlockPopulator {

	private static boolean isGenerating = false;

	@Override
	public void populate(final World world, final Random random, final Chunk chunk) {
		if (isGenerating || random.nextInt(100) >= 5)
			return;

		final int rx = 4 + random.nextInt(8);
		final int rz = 4 + random.nextInt(8);
		int maxY = world.getHighestBlockYAt(rx, rz);
		if (maxY < 16) {
			maxY = 44;
		}

		isGenerating = true;
		final int ry = random.nextInt(maxY);
		ArrayList<Block> snake = startSnake(world, random, chunk.getBlock(rx, ry, rz));
		finishSnake(world, random, snake);

		if (random.nextInt(16) > 5) {
			if (ry > 36) {
				snake = startSnake(world, random, chunk.getBlock(rx, ry / 2, rz));
				finishSnake(world, random, snake);
			} else if (ry < 24) {
				snake = startSnake(world, random, chunk.getBlock(rx, ry * 2, rz));
				finishSnake(world, random, snake);
			}
		}
		isGenerating = false;
	}

	private ArrayList<Block> startSnake(final World world, final Random random, final Block block) {
		final ArrayList<Block> snakeBlocks = new ArrayList<>();

		int blockX = block.getX();
		int blockY = block.getY();
		int blockZ = block.getZ();
		while (world.getBlockAt(blockX, blockY, blockZ).getTypeId() != 0) {
			if (snakeBlocks.size() > 1000) {
				break;
			}

			if (random.nextInt(20) == 0) {
				blockY = blockY + 1;
			} else if (world.getBlockAt(blockX, blockY + 2, blockZ).getTypeId() == 0) {
				blockY = blockY + 2;
			} else if (world.getBlockAt(blockX + 2, blockY, blockZ).getTypeId() == 0) {
				blockX = blockX + 1;
			} else if (world.getBlockAt(blockX - 2, blockY, blockZ).getTypeId() == 0) {
				blockX = blockX - 1;
			} else if (world.getBlockAt(blockX, blockY, blockZ + 2).getTypeId() == 0) {
				blockZ = blockZ + 1;
			} else if (world.getBlockAt(blockX, blockY, blockZ - 2).getTypeId() == 0) {
				blockZ = blockZ - 1;
			} else if (world.getBlockAt(blockX + 1, blockY, blockZ).getTypeId() == 0) {
				blockX = blockX + 1;
			} else if (world.getBlockAt(blockX - 1, blockY, blockZ).getTypeId() == 0) {
				blockX = blockX - 1;
			} else if (world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId() == 0) {
				blockZ = blockZ + 1;
			} else if (world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId() == 0) {
				blockZ = blockZ - 1;
			} else if (random.nextBoolean()) {
				if (random.nextBoolean()) {
					blockX = blockX + 1;
				} else {
					blockZ = blockZ + 1;
				}
			} else {
				if (random.nextBoolean()) {
					blockX = blockX - 1;
				} else {
					blockZ = blockZ - 1;
				}
			}

			if (world.getBlockAt(blockX, blockY, blockZ).getTypeId() != 0) {
				snakeBlocks.add(world.getBlockAt(blockX, blockY, blockZ));
			}
		}

		return snakeBlocks;
	}

	private void finishSnake(final World world, final Random random, final ArrayList<Block> snakeBlocks) {
		for (final Block block : snakeBlocks) {
			final Vector center = new BlockVector(block.getX(), block.getY(), block.getZ());
			if (block.getType() != Material.AIR) {
				final int radius = 1 + random.nextInt(3);
				for (int x = -radius; x <= radius; x++) {
					for (int y = -radius; y <= radius; y++) {
						for (int z = -radius; z <= radius; z++) {
							final Vector position = center.clone().add(new Vector(x, y, z));

							if (center.distance(position) <= radius + 0.5) {
								if (canPlaceBlock(world, position.getBlockX(), position.getBlockY(), position.getBlockZ())) {
									world.getBlockAt(position.toLocation(world)).setType(Material.AIR);
								}
							}
						}
					}
				}
			}
		}
	}

	private boolean canPlaceBlock(final World world, final int x, final int y, final int z) {
		switch (world.getBlockAt(x, y, z).getType()) {
			case AIR:
			case WATER:
			case STATIONARY_WATER:
			case LAVA:
			case STATIONARY_LAVA:
				return false;
			default:
				return true;
		}
	}
}
