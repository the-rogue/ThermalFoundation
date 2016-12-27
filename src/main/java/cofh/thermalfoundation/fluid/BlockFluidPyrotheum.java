package cofh.thermalfoundation.fluid;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cofh.core.fluid.BlockFluidInteractive;
import cofh.lib.util.BlockWrapper;
import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.helpers.ServerHelper;
import cofh.thermalfoundation.ThermalFoundation;

public class BlockFluidPyrotheum extends BlockFluidInteractive {

	Random random = new Random();

	public static final int LEVELS = 5;
	public static final Material materialFluidPyrotheum = new MaterialLiquid(MapColor.TNT);

	private static boolean effect = true;
	private static boolean enableSourceFall = true;

	public BlockFluidPyrotheum() {

		super("thermalfoundation", TFFluids.fluidPyrotheum, Material.LAVA, "pyrotheum");
		setQuantaPerBlock(LEVELS);
		setTickRate(10);

		setHardness(1000F);
		setLightOpacity(1);
		setParticleColor(1.0F, 0.7F, 0.15F);
	}

	@Override
	public boolean preInit() {

		super.preInit();

		addInteraction(Blocks.COBBLESTONE.getDefaultState(), Blocks.STONE.getDefaultState());
		addInteraction(Blocks.GRASS.getDefaultState(), Blocks.DIRT.getDefaultState());
		addInteraction(Blocks.SAND.getDefaultState(), Blocks.GLASS.getDefaultState());
		addInteraction(Blocks.WATER.getDefaultState(), Blocks.STONE.getDefaultState());
		addInteraction(Blocks.FLOWING_WATER.getDefaultState(), Blocks.STONE.getDefaultState());
		addInteraction(Blocks.CLAY.getDefaultState(), Blocks.HARDENED_CLAY.getDefaultState());
		addInteraction(Blocks.ICE.getDefaultState(), Blocks.STONE.getDefaultState());
		addInteraction(Blocks.SNOW.getDefaultState(), Blocks.AIR.getDefaultState());
		addInteraction(Blocks.SNOW_LAYER.getDefaultState(), Blocks.AIR.getDefaultState());

		for (int i = 0; i < 8; i++) {
			addInteraction(Blocks.STONE_STAIRS, i, i, Blocks.STONE_BRICK_STAIRS, i);
		}

		String category = "Fluid.Pyrotheum";
		String comment = "Enable this for Fluid Pyrotheum to be worse than lava.";
		effect = ThermalFoundation.config.get(category, "Effect", true, comment);

		comment = "Enable this for Fluid Pyrotheum Source blocks to gradually fall downwards.";
		enableSourceFall = ThermalFoundation.config.get(category, "Fall", enableSourceFall, comment);

		return true;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {

		if (!effect) {
			return;
		}
		if (ServerHelper.isClientWorld(world)) {
			return;
		}
		if (entity instanceof EntityPlayer) {

		} else if (entity instanceof EntityCreeper) {
			world.createExplosion(entity, entity.posX, entity.posY, entity.posZ, 6.0F, entity.worldObj.getGameRules().getBoolean("mobGriefing"));
			entity.setDead();
		}
	}

	@Override
	public int getLightValue(IBlockState state) {

		return TFFluids.fluidPyrotheum.getLuminosity();
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {

		return effect ? 800 : 0;
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {

		return 0;
	}

	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face) {

		return effect && face.ordinal() > EnumFacing.UP.ordinal() && world.getBlockState(new BlockPos(pos).add(0, -1, 0)).getBlock() != this;
	}

	@Override
	public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {

		return effect;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

		if (effect) {
			checkForInteraction(world, pos);
		}
		if (enableSourceFall && state.getBlock().getMetaFromState(world.getBlockState(pos)) == 0) {
			BlockPos pos2 = new BlockPos(pos).add(0, densityDir, 0);
			IBlockState blockstate = world.getBlockState(pos2);

			if (blockstate.getBlock() == this && blockstate.getBlock().getMetaFromState(blockstate) != 0 || blockstate.getBlock().isFlammable(world, pos2, EnumFacing.UP)) {
				world.setBlockState(pos2, this.getDefaultState(), 3);
				world.setBlockToAir(pos);
				return;
			}
		}
		super.updateTick(world, pos, state, rand);
	}

	protected void checkForInteraction(World world, BlockPos pos) {

		if (world.getBlockState(pos).getBlock() != this) {
			return;
		}

		for (int i = 0; i < 6; i++) {
			BlockPos pos2 = new BlockPos(pos).add(BlockHelper.SIDE_COORD_MOD[i][0], BlockHelper.SIDE_COORD_MOD[i][1], BlockHelper.SIDE_COORD_MOD[i][2]);

			interactWithBlock(world, pos2);
		}
		interactWithBlock(world, new BlockPos(pos).add(-1, 0, -1));
		interactWithBlock(world, new BlockPos(pos).add(-1, 0, 1));
		interactWithBlock(world, new BlockPos(pos).add(1, 0, -1));
		interactWithBlock(world, new BlockPos(pos).add(1, 0, 1));
	}

	protected void interactWithBlock(World world, BlockPos pos) {

		Block block = world.getBlockState(pos).getBlock();

		if (block == Blocks.AIR || block == this) {
			return;
		}
		IBlockState state = world.getBlockState(pos);
		BlockWrapper result;

		if (hasInteraction(state)) {
			result = getInteraction(state);
			world.setBlockState(pos, result.blockstate, 3);
			triggerInteractionEffects(world, pos);
		} else if (block.isFlammable(world, pos, EnumFacing.UP)) {
			world.setBlockState(pos, Blocks.FIRE.getDefaultState());
		} else if (world.isSideSolid(pos, EnumFacing.UP) && world.isAirBlock(new BlockPos(pos).add(0, 1, 0))) {
			world.setBlockState(new BlockPos(pos).add(0, 1, 0), Blocks.FIRE.getDefaultState(), 3);
		}
	}

	protected void triggerInteractionEffects(World world, BlockPos pos) {

		if (random.nextInt(16) == 0) {
			world.playSound(null, new BlockPos(pos).add(0.5F, 0.5F, 0.5F), SoundEvent.REGISTRY.getObject(new ResourceLocation("block.fire.extinguish")), SoundCategory.BLOCKS, 0.5F, 2.2F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
		}
	}

}
