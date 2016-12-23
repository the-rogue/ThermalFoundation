package cofh.lib.world;

import static cofh.lib.world.WorldGenMinableCluster.canGenerateInBlock;
import static cofh.lib.world.WorldGenMinableCluster.selectBlock;
import gnu.trove.iterator.TLongObjectIterator;
import gnu.trove.map.hash.TLongObjectHashMap;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.feature.WorldGenerator;
import cofh.lib.util.WeightedRandomBlock;

public class WorldGenMassiveTree extends WorldGenerator {

	/**
	 * Contains three sets of two values that provide complimentary indices for a given 'major' index - 1 and 2 for 0, 0 and 2 for 1, and 0 and 1 for 2.
	 */
	private static final byte[] otherCoordPairs = new byte[] { (byte) 2, (byte) 0, (byte) 0, (byte) 1, (byte) 2, (byte) 1 };
	private static final float PI = (float) Math.PI;

	/* Running variables */
	private Random rand = new Random();

	private World worldObj;
	/** Contains a list of a points at which to generate groups of leaves. */
	private int[][] leafNodes;
	private BlockPos basePos = new BlockPos(0, 0, 0);
	private int heightLimit = 0;
	private int height;
	private int leafBases;
	private int leafNodesLength;
	private int density;

	/* Setup variables */
	private final List<WeightedRandomBlock> leaves;
	private final List<WeightedRandomBlock> trunk;
	private final WeightedRandomBlock[] genBlock;
	private boolean generated = false;

	public boolean smoothLogs = false;
	public boolean slopeTrunk = false;
	public boolean safeGrowth = true;
	public boolean treeChecks = true;
	public boolean fastPlacement = false;
	public boolean relightBlocks = true;
	public WeightedRandomBlock[] genSurface = null;

	public int minHeight = -1;
	public int leafDistanceLimit = 4;
	public int heightLimitLimit = 250;

	public float heightAttenuation = 0.45f;
	public float branchDensity = 1.0f;
	public float branchSlope = 0.381f;
	public float scaleWidth = 1.0f;
	public int trunkSize = 11;

	public WorldGenMassiveTree(List<WeightedRandomBlock> resource, List<WeightedRandomBlock> leaf, List<WeightedRandomBlock> block) {

		super(false);
		trunk = resource;
		leaves = leaf;
		genBlock = block.toArray(new WeightedRandomBlock[block.size()]);
	}

	private void setup() {

		leafBases = MathHelper.ceiling_float_int(heightLimit * heightAttenuation);
		density = Math.max(1, (int) (1.382D + Math.pow(branchDensity * heightLimit / 13.0D, 2.0D)));
		chunkMap = new TLongObjectHashMap<Chunk>((int) (scaleWidth * heightLimit));
	}

	private float layerSize(int par1) {

		if (par1 < leafBases) {
			return -1.618F;
		} else {
			float var2 = heightLimit * .5F;
			float var3 = heightLimit * .5F - par1;
			float var4;

			if (var3 == 0.0F) {
				var4 = var2;
			} else if (Math.abs(var3) >= var2) {
				return 0.0F;
			} else {
				var4 = (float) Math.sqrt(var2 * var2 - var3 * var3);
			}

			var4 *= 0.5F;
			return var4;
		}
	}

	/**
	 * Generates a list of leaf nodes for the tree, to be populated by generateLeaves.
	 */
	private void generateLeafNodeList() {

		int var1 = density;

		int[] basePos = { this.basePos.getX(), this.basePos.getY(), this.basePos.getZ() };
		int[][] var2 = new int[var1 * heightLimit][4];
		int var3 = basePos[1] + heightLimit - leafDistanceLimit;
		int var4 = 1;
		int var5 = basePos[1] + height;
		int var6 = var3 - basePos[1];
		var2[0][0] = basePos[0];
		var2[0][1] = var3;
		var2[0][2] = basePos[2];
		var2[0][3] = var5;
		--var3;

		while (var6 >= 0) {
			int var7 = 0;
			float var8 = this.layerSize(var6);

			if (var8 > 0.0F) {
				for (float var9 = 0.5f; var7 < var1; ++var7) {
					float var11 = scaleWidth * var8 * (rand.nextFloat() + 0.328f);
					float var13 = rand.nextFloat() * 2.0f * PI;
					int var15 = MathHelper.floor_double(var11 * Math.sin(var13) + basePos[0] + var9);
					int var16 = MathHelper.floor_double(var11 * Math.cos(var13) + basePos[2] + var9);
					int[] var17 = new int[] { var15, var3, var16 };
					int[] var18 = new int[] { var15, var3 + leafDistanceLimit, var16 };

					if (this.checkBlockLine(var17, var18) == -1) {
						int t;
						double var20 = Math.sqrt((t = basePos[0] - var17[0]) * t + (t = basePos[2] - var17[2]) * t);
						int var22 = (int) (var20 * branchSlope);

						int[] var19 = new int[] { basePos[0], Math.min(var17[1] - var22, var5), basePos[2] };

						if (this.checkBlockLine(var19, var17) == -1) {
							var2[var4][0] = var15;
							var2[var4][1] = var3;
							var2[var4][2] = var16;
							var2[var4][3] = var19[1];
							++var4;
						}
					}
				}
			}
			--var3;
			--var6;
		}

		leafNodes = var2;
		leafNodesLength = var4;
	}

	@SuppressWarnings("deprecation")
	private void genLeafLayer(int x, int y, int z, final int size) {

		int t;
		final int X = x;
		final int Z = z;
		final float maxDistSq = size * size;

		for (int xMod = -size; xMod <= size; ++xMod) {
			x = X + xMod;
			final int xDistSq = xMod * xMod + (((t = xMod >> 31) ^ xMod) - t);

			for (int zMod = 0; zMod <= size;) { // negative values handled below
				final float distSq = xDistSq + zMod * zMod + zMod + 0.5f;

				if (distSq > maxDistSq) {
					break;
				} else {
					for (t = -1; t <= 1; t += 2) {
						z = Z + zMod * t;
						IBlockState blockstate = worldObj.getBlockState(new BlockPos(x, y, z));
						Block block = blockstate.getBlock();

						if (safeGrowth ? canGenerateInBlock(worldObj, new BlockPos(x, y, z), genBlock)
								&& (!treeChecks || (block.isAir(blockstate, worldObj, new BlockPos(x, y, z)) || block.isLeaves(blockstate, worldObj, new BlockPos(x, y, z)) || block.canBeReplacedByLeaves(
										blockstate, worldObj, new BlockPos(x, y, z)))) : blockstate.getBlockHardness(worldObj, new BlockPos(x, y, z)) >= 0) {
							WeightedRandomBlock b = selectBlock(worldObj, leaves);
							this.setBlockAndNotifyAdequately(worldObj, new BlockPos(x, y, z), b.block.getStateFromMeta(b.metadata));
						}
					}
					++zMod;
				}
			}
		}
	}

	/**
	 * Generates the leaf portion of the tree as specified by the leafNodes list.
	 */
	private void generateLeaves() {

		int[][] leafNodes = this.leafNodes;

		for (int i = 0, e = leafNodesLength; i < e; ++i) {
			int[] n = leafNodes[i];
			int x = n[0], yO = n[1], z = n[2];
			int y = 0;

			for (int var5 = y + leafDistanceLimit; y < var5; ++y) {
				int size = (y != 0) & y != leafDistanceLimit - 1 ? 3 : 2;
				genLeafLayer(x, yO++, z, size);
			}
		}
	}

	private int[] placeScratch = new int[3];

	@SuppressWarnings("deprecation")
	private void placeBlockLine(int[] par1, int[] par2, List<WeightedRandomBlock> block, int meta) {

		int t;
		int[] var4 = placeScratch;
		byte var6 = 0;

		for (byte i = 0; i < 3; ++i) {
			int a = par2[i] - par1[i], b = ((t = a >> 31) ^ a) - t;
			var4[i] = a;
			if (b > ((a = var4[var6]) ^ (t = a >> 31)) - t) {
				var6 = i;
			}
		}

		if (var4[var6] != 0) {
			byte var7 = otherCoordPairs[var6];
			byte var8 = otherCoordPairs[var6 + 3];
			byte var9;

			if (var4[var6] > 0) {
				var9 = 1;
			} else {
				var9 = -1;
			}

			float var10 = (float) var4[var7] / (float) var4[var6];
			float var12 = (float) var4[var8] / (float) var4[var6];
			int var16 = var4[var6] + var9;

			int[] var14 = var4;

			for (int var15 = 0; var15 != var16; var15 += var9) {
				var14[var6] = MathHelper.floor_float(par1[var6] + var15 + 0.5F);
				var14[var7] = MathHelper.floor_float(par1[var7] + var15 * var10 + 0.5F);
				var14[var8] = MathHelper.floor_float(par1[var8] + var15 * var12 + 0.5F);
				int var18 = var14[0] - par1[0];
				var18 = ((t = var18 >> 31) ^ var18) - t;
				int var19 = var14[2] - par1[2];
				var19 = ((t = var19 >> 31) ^ var19) - t;
				int var20 = Math.max(var18, var19);

				int var17 = meta;
				if (smoothLogs & var20 > 0) {
					if (var18 == var20) {
						var17 |= 4;
					} else if (var19 == var20) {
						var17 |= 8;
					}
				}

				WeightedRandomBlock par3 = selectBlock(worldObj, trunk);
				this.setBlockAndNotifyAdequately(worldObj, new BlockPos(var14[0], var14[1], var14[2]), par3.block.getStateFromMeta(par3.metadata| var17));
			}
		}
	}

	/**
	 * Places the trunk for the big tree that is being generated. Able to generate double-sized trunks by changing a field that is always 1 to 2.
	 */
	private void generateTrunk() {

		int var1 = basePos.getX();
		int var2 = basePos.getY();
		int var3 = basePos.getY() + height;
		int var4 = basePos.getZ();

		int[] var5 = new int[] { var1, var2, var4 };
		int[] var6 = new int[] { var1, var3, var4 };

		double lim = 400f / trunkSize;

		for (int i = -trunkSize; i <= trunkSize; i++) {
			var5[0] = var1 + i;
			var6[0] = var1 + i;

			for (int j = -trunkSize; j <= trunkSize; j++) {
				if ((j * j + i * i) * 4 < trunkSize * trunkSize * 5) {
					var5[2] = var4 + j;
					var6[2] = var4 + j;

					if (slopeTrunk) {
						var6[1] = var2 + sinc2(lim * i, lim * j, height) - (rand.nextInt(3) - 1);
					}

					this.placeBlockLine(var5, var6, trunk, 0);
					if (smoothLogs) {
						this.setBlockAndNotifyAdequately(worldObj, new BlockPos(var6[0], var6[1], var6[2]), null);
					}
					worldObj.getBlockState(new BlockPos(var5[0], var5[1] - 1, var5[2])).getBlock().onPlantGrow(worldObj.getBlockState(new BlockPos(var5[0], var5[1] - 1, var5[2])),worldObj, new BlockPos(var5[0], var5[1] - 1, var5[2]), new BlockPos(var1, var2, var4));
				}
			}
		}
	}

	private static final int sinc2(final double x, final double z, final int y) {

		final double pi = Math.PI, pi2 = pi / 1.5;
		double r;
		r = Math.sqrt((r = (x / pi)) * r + (r = (z / pi)) * r) * pi / 180;
		if (r == 0) {
			return y;
		}
		return (int) Math.round(y * (((Math.sin(r) / r) + (Math.sin(r * pi2) / (r * pi2))) / 2));
	}

	/**
	 * Generates additional wood blocks to fill out the bases of different leaf nodes that would otherwise degrade.
	 */
	void generateLeafNodeBases() {

		int[] start = new int[] { basePos.getX(), basePos.getY(), basePos.getZ() };
		int[][] leafNodes = this.leafNodes;

		int heightLimit = (int) (this.heightLimit * 0.2f);
		int meta = smoothLogs ? 12 : 0;
		for (int i = 0, e = leafNodesLength; i < e; ++i) {
			int[] end = leafNodes[i];
			start[1] = end[3];
			int height = start[1] - basePos.getY();

			if (height >= heightLimit) {
				this.placeBlockLine(start, end, trunk, meta);
			}
		}
	}

	private int[] checkScratch = new int[3];

	/**
	 * Checks a line of blocks in the world from the first coordinate to triplet to the second, returning the distance (in blocks) before a non-air, non-leaf
	 * block is encountered and/or the end is encountered.
	 */
	private int checkBlockLine(int[] par1, int[] par2) {

		int t;
		int[] var3 = checkScratch;
		byte var5 = 0;

		for (byte i = 0; i < 3; ++i) {
			int a = par2[i] - par1[i], b = ((t = a >> 31) ^ a) - t;
			var3[i] = a;
			if (b > ((a = var3[var5]) ^ (t = a >> 31)) - t) {
				var5 = i;
			}
		}

		if (var3[var5] == 0) {
			return -1;
		} else {
			byte var6 = otherCoordPairs[var5];
			byte var7 = otherCoordPairs[var5 + 3];
			byte var8;

			if (var3[var5] > 0) {
				var8 = 1;
			} else {
				var8 = -1;
			}

			float var9 = (float) var3[var6] / (float) var3[var5];
			float var11 = (float) var3[var7] / (float) var3[var5];
			int var14 = 0;
			int var15 = var3[var5] + var8;

			int[] var13 = var3;

			for (; var14 != var15; var14 += var8) {
				var13[var5] = par1[var5] + var14;
				var13[var6] = MathHelper.floor_float(par1[var6] + var14 * var9);
				var13[var7] = MathHelper.floor_float(par1[var7] + var14 * var11);
				int x = var13[0], y = var13[1], z = var13[2];
				IBlockState blockstate =  worldObj.getBlockState(new BlockPos(x, y, z));
				Block block = blockstate.getBlock();

				if (safeGrowth ? canGenerateInBlock(worldObj, new BlockPos(x, y, z), genBlock)
						&& (!treeChecks || !(block.isAir(blockstate, worldObj, new BlockPos(x, y, z)) || block.isReplaceable(worldObj, new BlockPos(x, y, z))
								|| block.canBeReplacedByLeaves(blockstate, worldObj, new BlockPos(x, y, z)) || block.isLeaves(blockstate, worldObj, new BlockPos(x, y, z)) || block.isWood(worldObj, new BlockPos(x, y, z)) || block instanceof BlockSapling))
						: blockstate.getBlockHardness(worldObj, new BlockPos(x, y, z)) >= 0) {
					break;
				}
			}

			return var14 == var15 ? -1 : ((t = var14 >> 31) ^ var14) - t;
		}
	}

	/**
	 * Returns a boolean indicating whether or not the current location for the tree, spanning basePos to to the height limit, is valid.
	 */
	private boolean validTreeLocation() {

		int newHeight = Math.min(heightLimit + basePos.getY(), 255) - basePos.getY();
		if (newHeight < minHeight) {
			return false;
		}
		heightLimit = newHeight;

		if (!canGenerateInBlock(worldObj, new BlockPos(basePos).add(0, -1, 0), genSurface)) {
			return false;
		} else {
			int[] minPos = new int[] { basePos.getX(), basePos.getY(), basePos.getZ() };
			int[] maxPos = new int[] { basePos.getX(), basePos.getY() + heightLimit - 1, basePos.getZ() };

			newHeight = this.checkBlockLine(minPos, maxPos);

			if (newHeight == -1) {
				newHeight = heightLimit;
			}
			if (newHeight < minHeight) {
				return false;
			}

			heightLimit = Math.min(newHeight, heightLimitLimit);
			height = (int) (heightLimit * heightAttenuation);
			if (height >= heightLimit) {
				height = heightLimit - 1;
			}
			height += rand.nextInt(heightLimit - height);

			if (safeGrowth) {
				int xPos = basePos.getX();
				int minYPos = basePos.getY();
				int maxYPos = basePos.getY() + height;
				int zPos = basePos.getZ();

				minPos = new int[] { xPos, minYPos, zPos };
				maxPos = new int[] { xPos, maxYPos, zPos };

				double lim = 400f / trunkSize;

				for (int i = -trunkSize; i <= trunkSize; i++) {
					minPos[0] = xPos + i;
					maxPos[0] = xPos + i;

					for (int j = -trunkSize; j <= trunkSize; j++) {
						if ((j * j + i * i) * 4 < trunkSize * trunkSize * 5) {
							minPos[2] = zPos + j;
							maxPos[2] = zPos + j;

							if (slopeTrunk) {
								maxPos[1] = minYPos + sinc2(lim * i, lim * j, height);
							}

							int t = checkBlockLine(minPos, maxPos);
							if (t != -1) {
								return false;
							}
						}
					}
				}
			}

			return true;
		}
	}

	public WorldGenMassiveTree setTreeScale(float height, float width, float leaves) {

		heightLimitLimit = (int) (height * 12.0D);
		minHeight = heightLimitLimit / 2;
		trunkSize = (int) Math.round((height / 2D));

		if (minHeight > 30) {
			leafDistanceLimit = 5;
		} else {
			leafDistanceLimit = minHeight / 8;
		}

		scaleWidth = width;
		branchDensity = leaves;
		return this;
	}

	public WorldGenMassiveTree setMinTrunkSize(int radius) {

		trunkSize = Math.max(radius, trunkSize);
		return this;
	}

	public WorldGenMassiveTree setLeafAttenuation(float a) {

		heightAttenuation = a;
		return this;
	}

	public WorldGenMassiveTree setSloped(boolean s) {

		slopeTrunk = s;
		return this;
	}

	public WorldGenMassiveTree setSafe(boolean s) {

		safeGrowth = s;
		return this;
	}

	@Override
	public synchronized boolean generate(World world, Random par2Random, BlockPos pos) {

		// long time = System.nanoTime();
		worldObj = world;
		long var6 = par2Random.nextLong();
		rand.setSeed(var6);
		basePos = pos;
		
		if (heightLimit == 0) {
			heightLimit = heightLimitLimit;
		}
		if (minHeight < 0) {
			minHeight = 80;
		}

		if (!this.validTreeLocation()) {
			worldObj = null;
			return false;
		} else {
			generated = false;
			this.setup();
			// time = System.nanoTime() - time;
			// logger.info("Verified spawn position of massive tree in: " + time + "ns");
			// long time2 = time = System.nanoTime();
			this.generateLeafNodeList();
			// long nodes = System.nanoTime();
			this.generateLeaves();
			// long leaves = System.nanoTime();
			this.generateLeafNodeBases();
			// long bases = System.nanoTime();
			this.generateTrunk();
			// long trunk = System.nanoTime();
			// time = System.nanoTime() - time;
			// logger.info("Generated massive tree in: " + time + "ns");
			// trunk -= bases; bases -= leaves; leaves -= nodes; nodes -= time2;
			// logger.info(String.format("%s for trunk, %s for leaf nodes, %s for leaves, %s for branches", trunk, nodes, leaves, bases));
			// logger.info("\tTree contains " + blocksAdded + " Blocks");
			// time = System.nanoTime();
			if (fastPlacement) {
				for (TLongObjectIterator<Chunk> iter = chunkMap.iterator(); iter.hasNext();) {
					iter.advance();
					Chunk chunk = iter.value();
					chunk.generateSkylightMap();
					ExtendedBlockStorage[] storage = chunk.getBlockStorageArray();
					if (!relightBlocks) {
						for (int i = storage.length; i-- > 0;) {
							if (storage[i] != null) {
								// { force data array to exist if optimizations to not exist are in place
								NibbleArray a = storage[i].getSkylightArray();
								a.set(0, 0, 0, 0);
								a.set(0, 0, 0, 15);
								// }
								Arrays.fill(a.getData(), (byte) 0);
							}
						}
						chunk.resetRelightChecks();
					}
					chunk.setChunkModified();
					if (world instanceof WorldServer) {
						PlayerChunkMap manager = ((WorldServer) world).getPlayerChunkMap();
						if (manager == null) {
							continue;
						}
						PlayerChunkMapEntry watcher = manager.getOrCreateEntry(chunk.xPosition, chunk.zPosition);
						if (watcher != null) {
							watcher.sendPacket(new SPacketChunkData(chunk, -1));
						}
					}
				}
			}
			// time = System.nanoTime() - time;
			// logger.info("Lit massive tree in: " + time + "ns");
			worldObj = null;
			return generated;
		}
	}

	// private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger("Tree logger");
	// private int blocksAdded = 0;
	private TLongObjectHashMap<Chunk> chunkMap;

	@Override
	public void setBlockAndNotifyAdequately(World world, BlockPos pos, IBlockState blockstate) {

		int x = pos.getX(), y = pos.getY(), z = pos.getZ();
		if ((pos.getY() < 0) | pos.getY() > 255) {
			return;
		}
		generated = true;
		if (!fastPlacement) {
			if (blockstate != null) {
				super.setBlockAndNotifyAdequately(world, pos, blockstate);
			} else {
				world.setBlockState(pos, blockstate, 2);
			}
			return;
		}
		// ++blocksAdded;
		long lpos = ((x & 0xFFFFFFF0L) << 32) | (z  & 0xFFFFFFF0L);

		Chunk chunk = chunkMap.get(lpos);
		if (chunk == null) {
			chunk = world.getChunkFromBlockCoords(pos);
			chunkMap.put(lpos, chunk);
		}

		ExtendedBlockStorage[] storage = chunk.getBlockStorageArray();
		ExtendedBlockStorage subChunk = storage[y >> 4];
		if (subChunk == null) {
			storage[y >> 4] = subChunk = new ExtendedBlockStorage(y & ~15, !world.provider.getHasNoSky());
		}

		x &= 15;
		z &= 15;
		if (blockstate != null && subChunk.get(x, y & 15, z).getBlock().hasTileEntity(subChunk.get(x, y & 15, z))) {
			chunk.removeTileEntity(new BlockPos(x, y, z));
		}
		y &= 15;

		if (blockstate != null) {
			subChunk.set(x, y, z, blockstate);
		}
		subChunk.setExtBlocklightValue(x, y, z, 0);
	}

}
