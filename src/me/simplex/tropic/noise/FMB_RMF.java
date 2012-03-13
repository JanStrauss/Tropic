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
package me.simplex.tropic.noise;

import java.util.Random;

public class FMB_RMF {

	private static Random rand;

	private static int[] ms_p = new int[512];

	public FMB_RMF(long seed) {
		rand = new Random(seed);
		int nbVals = (1 << 8); 
		int[] ms_perm = new int[nbVals];
	
		for (int i = 0; i < nbVals; i++) {
			ms_perm[i] = -1;
		}
		for (int i = 0; i < nbVals; i++) {
			while (true) {
				int p = rand.nextInt(256);
				if (ms_perm[p] == -1) {
					ms_perm[p] = i;
					break;
				}
			}
		}

		for (int i = 0; i < nbVals; i++) {
			ms_p[nbVals + i] = ms_p[i] = ms_perm[i];
		}

	}

	private static double noise(double x, double y, double z) {
		int X = (int) x & 255;
		int Y = (int) y & 255;
		int Z = (int) z & 255;
		x -= Math.floor(x);
		y -= Math.floor(y);
		z -= Math.floor(z);
		double u = fade(x);
		double v = fade(y);
		double w = fade(z);
		int A = ms_p[X] + Y, AA = ms_p[A] + Z, AB = ms_p[A + 1] + Z;
		int B = ms_p[X + 1] + Y, BA = ms_p[B] + Z, BB = ms_p[B + 1] + Z;
		return lerp(w, lerp(v, lerp(u, grad(ms_p[AA], x, y, z), grad(ms_p[BA],x - 1, y, z)), lerp(u, grad(ms_p[AB], x, y - 1, z), grad(ms_p[BB], x - 1, y - 1, z))), lerp(v,lerp(u, grad(ms_p[AA + 1], x, y, z - 1), grad(ms_p[BA + 1],x - 1, y, z - 1)), lerp(u, grad(ms_p[AB + 1], x, y - 1,z - 1), grad(ms_p[BB + 1], x - 1, y - 1, z - 1))));
	}

	private static double fade(double t) {
		return (t * t * t * (t * (t * 6 - 15) + 10));
	}

	private static double lerp(double t, double a, double b) {
		return (a + t * (b - a));
	}

	private static double grad(int hash, double x, double y, double z) {
		int h = hash & 15;
		double u = h < 8 ? x : y;
		double v = h < 4 ? y : h == 12 || h == 14 ? x : z;

		return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
	}

	public static double ridge(double h, float offset) {
		h = Math.abs(h);
		h = offset - h;
		h = h * h;
		return h;
	}

	public double noise_RidgedMultiFractal(double x, double y, double z, int octaves, float lacunarity, float gain, float offset) {
		double sum = 0;
		float amplitude = 0.5f;
		float frequency = 1.0f;
		double prev = 1.0f;

		for (int i = 0; i < octaves; i++) {
			double n = ridge(noise(x * frequency, y * frequency, z * frequency), offset);
			sum += n * amplitude * prev;
			prev = n;
			frequency *= lacunarity;
			amplitude *= gain;
		}
		return sum;
	}

	public double noise_FractionalBrownianMotion(double x, double y, double z, int octaves, float lacunarity, float gain) {
		double frequency = 1.0f;
		double amplitude = 0.5f;
		double sum = 0.0f;

		for (int i = 0; i < octaves; i++) {
			sum += noise(x * frequency, y * frequency, z * frequency)* amplitude;
			frequency *= lacunarity;
			amplitude *= gain;
		}
		return sum;
	}

}
