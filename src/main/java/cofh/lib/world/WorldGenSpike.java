package cofh.lib.world;

import static cofh.lib.world.WorldGenMinableCluster.canGenerateInBlock;
import static cofh.lib.world.WorldGenMinableCluster.generateBlock;

import java.util.List;
import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import cofh.lib.util.WeightedRandomBlock;

public class WorldGenSpike extends WorldGenerator {

	private final List<WeightedRandomBlock> cluster;
	private final WeightedRandomBlock[] genBlock;
	public boolean largeSpikes = true;
	public int largeSpikeChance = 60;
	public int minHeight = 7;
	public int heightVariance = 4;
	public int sizeVariance = 2;
	public int positionVariance = 3;
	public int minLargeSpikeHeightGain = 10;
	public int largeSpikeHeightVariance = 30;
	public int largeSpikeFillerSize = 1;

	public WorldGenSpike(List<WeightedRandomBlock> resource, List<WeightedRandomBlock> block) {

		cluster = resource;
		genBlock = block.toArray(new WeightedRandomBlock[block.size()]);
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {

		while (world.isAirBlock(pos) && pos.getY() > 2) {
			pos.add(0, -1, 0);
		}

		if (!canGenerateInBlock(world, pos, genBlock)) {
			return false;
		}

		int height = rand.nextInt(heightVariance) + minHeight, originalHeight = height;
		int size = height / (minHeight / 2) + rand.nextInt(sizeVariance);
		if (size > 1 && positionVariance > 0) {
			pos.add(0, rand.nextInt(positionVariance + 1) - 1, 0);
		}

		if (largeSpikes && size > 1 && (largeSpikeChance <= 0 || rand.nextInt(largeSpikeChance) == 0)) {
			height += minLargeSpikeHeightGain + rand.nextInt(largeSpikeHeightVariance);
		}

		int offsetHeight = height - originalHeight;

		for (int y = 0; y < height; ++y) {
			float layerSize;
			if (y >= offsetHeight) {
				layerSize = (1.0F - (float) (y - offsetHeight) / (float) originalHeight) * size;
			} else {
				layerSize = largeSpikeFillerSize;
			}
			int width = MathHelper.ceiling_float_int(layerSize);

			for (int x = -width; x <= width; ++x) {
				float xDist = MathHelper.abs_int(x) - 0.25F;

				for (int z = -width; z <= width; ++z) {
					float zDist = MathHelper.abs_int(z) - 0.25F;

					if ((x == 0 && z == 0 || xDist * xDist + zDist * zDist <= layerSize * layerSize)
							&& (x != -width && x != width && z != -width && z != width || rand.nextFloat() <= 0.75F)) {

						generateBlock(world, new BlockPos(pos).add(x, y, z), genBlock, cluster);

						if (y != 0 && width > 1) {
							generateBlock(world, new BlockPos(pos).add(x, - y + offsetHeight, z), genBlock, cluster);
						}
					}
				}
			}
		}

		return true;
	}
}
