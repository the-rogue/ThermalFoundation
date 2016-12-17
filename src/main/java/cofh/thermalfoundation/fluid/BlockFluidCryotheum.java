package cofh.thermalfoundation.fluid;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import cofh.core.fluid.BlockFluidInteractive;
import cofh.lib.util.BlockWrapper;
import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.helpers.DamageHelper;
import cofh.lib.util.helpers.ServerHelper;
import cofh.thermalfoundation.ThermalFoundation;
import cofh.thermalfoundation.block.TFBlocks;
import cofh.thermalfoundation.entity.monster.EntityBlizz;

public class BlockFluidCryotheum extends BlockFluidInteractive {

	Random random = new Random();

	public static final int LEVELS = 5;
	public static final Material materialFluidCryotheum = new MaterialLiquid(MapColor.ICE);

	private static boolean enableSourceFall = true;
	private static boolean effect = true;

	public BlockFluidCryotheum() {

		super("thermalfoundation", TFFluids.fluidCryotheum, materialFluidCryotheum, "cryotheum");
		setQuantaPerBlock(LEVELS);
		setTickRate(15);

		setHardness(1000F);
		setLightOpacity(1);
		setParticleColor(0.15F, 0.7F, 1.0F);
	}

	@Override
	public boolean preInit() {

		GameRegistry.register(this, new ResourceLocation("FluidCryotheum"));

		addInteraction(Blocks.GRASS.getDefaultState(), Blocks.DIRT.getDefaultState());
		addInteraction(Blocks.WATER.getDefaultState(), Blocks.ICE.getDefaultState());
		addInteraction(Blocks.WATER, 1, 15, Blocks.SNOW.getDefaultState());
		addInteraction(Blocks.FLOWING_WATER.getDefaultState(), Blocks.ICE.getDefaultState());
		addInteraction(Blocks.FLOWING_WATER, 1, 15, Blocks.SNOW.getDefaultState());
		addInteraction(Blocks.LAVA.getDefaultState(), Blocks.OBSIDIAN.getDefaultState());
		addInteraction(Blocks.LAVA, 1, 15, Blocks.STONE.getDefaultState());
		addInteraction(Blocks.FLOWING_LAVA.getDefaultState(), Blocks.OBSIDIAN.getDefaultState());
		addInteraction(Blocks.FLOWING_LAVA, 1, 15, Blocks.STONE.getDefaultState());
		addInteraction(Blocks.LEAVES.getDefaultState(), Blocks.AIR.getDefaultState());
		addInteraction(Blocks.TALLGRASS.getDefaultState(), Blocks.AIR.getDefaultState());
		addInteraction(Blocks.FIRE.getDefaultState(), Blocks.AIR.getDefaultState());
		addInteraction(TFBlocks.blockFluidGlowstone.getDefaultState(), Blocks.GLOWSTONE.getDefaultState());

		String category = "Fluid.Cryotheum";
		String comment = "Enable this for Fluid Cryotheum to be worse than lava, except cold.";
		effect = ThermalFoundation.config.get(category, "Effect", true, comment);

		comment = "Enable this for Fluid Cryotheum Source blocks to gradually fall downwards.";
		enableSourceFall = ThermalFoundation.config.get(category, "Fall", enableSourceFall, comment);

		return true;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {

		entity.extinguish();

		if (!effect) {
			return;
		}
		if (entity.motionY < -0.05 || entity.motionY > 0.05) {
			entity.motionY *= 0.05;
		}
		if (entity.motionZ < -0.05 || entity.motionZ > 0.05) {
			entity.motionZ *= 0.05;
		}
		if (entity.motionX < -0.05 || entity.motionX > 0.05) {
			entity.motionX *= 0.05;
		}
		if (ServerHelper.isClientWorld(world)) {
			return;
		}
		if (world.getTotalWorldTime() % 8 != 0) {
			return;
		}
		if (entity instanceof EntityZombie || entity instanceof EntityCreeper) {
			EntitySnowman snowman = new EntitySnowman(world);
			snowman.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
			world.spawnEntityInWorld(snowman);

			entity.setDead();
		} else if (entity instanceof EntityBlizz || entity instanceof EntitySnowman) {
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(new ResourceLocation("speed")), 6 * 20, 0));
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(new ResourceLocation("regeneration")), 6 * 20, 0));
		} else if (entity instanceof EntityBlaze) {
			entity.attackEntityFrom(DamageHelper.cryotheum, 10F);
		} else {
			boolean t = entity.velocityChanged;
			entity.attackEntityFrom(DamageHelper.cryotheum, 2.0F);
			entity.velocityChanged = t;
		}
	}

	@Override
	public int getLightValue(IBlockState state) {

		return TFFluids.fluidCryotheum.getLuminosity();
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

		if (effect) {
			checkForInteraction(world, pos);
		}
		if (enableSourceFall && state.getBlock().getMetaFromState(world.getBlockState(pos)) == 0) {
			BlockPos pos2 = new BlockPos(pos).add(0, densityDir, 0);
			IBlockState blockstate = world.getBlockState(pos2);

			if (blockstate.getBlock() == this && blockstate.getBlock().getMetaFromState(blockstate) != 0) {
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

		if (hasInteraction(state)) {
			BlockWrapper result = getInteraction(state);
			world.setBlockState(pos, result.blockstate, 3);
			// triggerInteractionEffects(world, x, y, z);
		} else if (world.isSideSolid(pos, EnumFacing.UP) && world.isAirBlock(new BlockPos(pos).add(0, 1, 0))) {
			world.setBlockState(new BlockPos(pos).add(0, 1, 0), Blocks.SNOW_LAYER.getDefaultState(), 3);
		}
	}

	protected void triggerInteractionEffects(World world, BlockPos pos) {

	}

}
