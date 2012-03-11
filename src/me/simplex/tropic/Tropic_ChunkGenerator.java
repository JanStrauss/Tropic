package me.simplex.tropic;

import java.util.List;
import java.util.Random;

import me.simplex.tropic.noise.FMB_RMF;
import me.simplex.tropic.noise.Voronoi;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class Tropic_ChunkGenerator extends ChunkGenerator {
	private int currheight;
	private long usedSeed;

	private FMB_RMF n_p;
	private SimplexNoiseGenerator ground_nouise;
	private Voronoi cliffs;

	private List<BlockPopulator> populators;

	public Tropic_ChunkGenerator(long seed, List<BlockPopulator> buildPopulators) {
		this.usedSeed = seed;
		this.n_p = new FMB_RMF(seed);
		this.populators = buildPopulators;
		this.ground_nouise = new SimplexNoiseGenerator(seed);
		this.cliffs = new Voronoi(64, true, seed, 16, Voronoi.DistanceMetric.Squared, 4);
	}

	/**
	 * Sets the Material at the given Location
	 * 
	 * @param chunk_data
	 *            Chunk byte array
	 * @param x
	 *            coordinate
	 * @param y
	 *            coordinate
	 * @param z
	 *            coordinate
	 * @param material
	 *            to set at the coordinates
	 */
	private static void setMaterialAt(byte[][] chunk_data, int x, int y, int z, Material material) {
		int sec_id = (y >> 4);
		int yy = y & 0xF;
		if (chunk_data[sec_id] == null) {
			chunk_data[sec_id] = new byte[4096];
		}
		chunk_data[sec_id][(yy << 8) | (z << 4) | x] = (byte) material.getId();
	}

	@Override
	public byte[][] generateBlockSections(World world, Random random, int xchunk, int zchunk, BiomeGrid biomes) {
		checkSeed(world.getSeed());
		byte[][] result = new byte[16][];

		final int SEELEVEL = 60;
		final int TIMBERLIMIT = 110;

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				currheight = 50;
				currheight = Math.max(currheight, genGroundNoise(x, z, xchunk, zchunk) + 60);

				final int DIRTHEIGHT = random.nextInt(5);
				final boolean DO_GRASS = random.nextBoolean();

				for (int y = 0; y <= currheight; y++) {
					if (y == currheight) {
						if (currheight < SEELEVEL - 1) {
							setMaterialAt(result, x, y, z, Material.DIRT);
						} else {
							if (y == SEELEVEL - 1 || y == SEELEVEL || y == SEELEVEL + 1) {
								setMaterialAt(result, x, y, z, Material.SAND);
							} else {
								if (y >= TIMBERLIMIT) {
									if (DO_GRASS) {
										setMaterialAt(result, x, y, z, Material.GRASS);
									} else {
										setMaterialAt(result, x, y, z, Material.STONE);
									}
								} else {
									setMaterialAt(result, x, y, z, Material.GRASS);
								}
							}
						}
					} else if (y >= TIMBERLIMIT) {
						setMaterialAt(result, x, y, z, Material.STONE);
					} else if (currheight - y <= DIRTHEIGHT) {
						setMaterialAt(result, x, y, z, Material.DIRT);
					} else {
						setMaterialAt(result, x, y, z, Material.STONE);
					}
				}
				if (currheight + 1 <= SEELEVEL) {
					for (int y = currheight + 1; y <= SEELEVEL; y++) {
						setMaterialAt(result, x, y, z, Material.STATIONARY_WATER);
					}
				}
				
				setMaterialAt(result, x, 0, z, Material.BEDROCK);
				setMaterialAt(result, x, 1, z, Material.BEDROCK);
				if (random.nextBoolean()) {
					setMaterialAt(result, x, 2, z, Material.BEDROCK);
				}
				
				biomes.setBiome(x, z, Biome.JUNGLE);
			}
		}
		
		return result;
	}

	private int genGroundNoise(int x, int z, int xchunk, int zchunk) {
		double x_calc = ((x + xchunk * 16) + Integer.MAX_VALUE / 2) * 0.003d;
		double z_calc = ((z + zchunk * 16) + Integer.MAX_VALUE / 2) * 0.003d;

		double temp = n_p.noise_FractionalBrownianMotion(x_calc, z_calc, 0, 6, 0.45f, 1.5f);
		double ground = ground_nouise.noise(x_calc, z_calc, 4, 0.25, 0.125) * 33;
		double cliff = cliffs.get((x + xchunk * 16) / 250.0f, (z + zchunk * 16) / 250.0f) * 120;

		double noise = ground + (Math.abs((n_p.noise_RidgedMultiFractal(x_calc, z_calc, 0, 4, 2.85f, 0.45f, 1.0f)) + (.05f * temp)) * 55);
		noise = noise - cliff;

		return (int) Math.round(noise);
	}

	/**
	 * Sets the Noise generators to use the specified seed
	 * 
	 * @param seed
	 */
	public void changeSeed(Long seed) {
		this.n_p = new FMB_RMF(seed);
		this.ground_nouise = new SimplexNoiseGenerator(seed);
		this.cliffs = new Voronoi(64, true, seed, 16, Voronoi.DistanceMetric.Squared, 4);
	}

	/**
	 * Checks if the Seed that is currently used by the Noise generators is the
	 * same as the given seed. If not {@link Generator.changeSeed()} is called.
	 * 
	 * @param worldSeed
	 */
	private void checkSeed(Long worldSeed) {
		if (worldSeed != usedSeed) {
			usedSeed = worldSeed;
			changeSeed(worldSeed);
		}
	}

	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		return populators;
	}
}
