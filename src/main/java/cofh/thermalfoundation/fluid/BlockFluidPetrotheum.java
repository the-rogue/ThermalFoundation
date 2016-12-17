package cofh.thermalfoundation.fluid;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import cofh.core.fluid.BlockFluidInteractive;
import cofh.lib.util.BlockWrapper;
import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.helpers.ServerHelper;
import cofh.thermalfoundation.ThermalFoundation;

public class BlockFluidPetrotheum extends BlockFluidInteractive {

	Random random = new Random();

	public static final int LEVELS = 6;
	public static final Material materialFluidPetrotheum = new MaterialLiquid(MapColor.STONE);

	private static boolean enableSourceFall = true;
	private static boolean effect = true;
	private static boolean extreme = false;

	public BlockFluidPetrotheum() {

		super("thermalfoundation", TFFluids.fluidPetrotheum, materialFluidPetrotheum, "petrotheum");
		setQuantaPerBlock(LEVELS);
		setTickRate(10);

		setHardness(1000F);
		setLightOpacity(1);
		setParticleColor(0.4F, 0.3F, 0.2F);
	}

	@Override
	public boolean preInit() {

		GameRegistry.register(this, new ResourceLocation("FluidPetrotheum"));

		addInteraction(Blocks.STONE.getDefaultState(), Blocks.GRAVEL.getDefaultState());
		addInteraction(Blocks.COBBLESTONE.getDefaultState(), Blocks.GRAVEL.getDefaultState());
		addInteraction(Blocks.STONEBRICK.getDefaultState(), Blocks.GRAVEL.getDefaultState());
		addInteraction(Blocks.MOSSY_COBBLESTONE.getDefaultState(), Blocks.GRAVEL.getDefaultState());

		String category = "Fluid.Petrotheum";
		String comment = "Enable this for Fluid Petrotheum to break apart stone blocks.";
		effect = ThermalFoundation.config.get(category, "Effect", effect, comment);

		comment = "Enable this for Fluid Petrotheum to have an EXTREME effect on stone blocks.";
		extreme = ThermalFoundation.config.get(category, "Effect.Extreme", extreme, comment);

		comment = "Enable this for Fluid Petrotheum Source blocks to gradually fall downwards.";
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
		if (world.getTotalWorldTime() % 8 != 0) {
			return;
		}
		if (world.getTotalWorldTime() % 8 == 0 && entity instanceof EntityLivingBase && !((EntityLivingBase) entity).isEntityUndead()) {
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(new ResourceLocation("haste")), 30 * 20, 2));
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(new ResourceLocation("night_vision")), 30 * 20, 0));
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(new ResourceLocation("resistance")), 30 * 20, 1));
		}
	}

	@Override
	public int getLightValue(IBlockState state) {

		return TFFluids.fluidPetrotheum.getLuminosity();
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

		for (int i = 2; i < 6; i++) {
			BlockPos pos2 = new BlockPos(pos).add(BlockHelper.SIDE_COORD_MOD[i][0], BlockHelper.SIDE_COORD_MOD[i][1], BlockHelper.SIDE_COORD_MOD[i][2]);
			interactWithBlock(world, pos2);
		}
	}

	protected void interactWithBlock(World world, BlockPos pos) {

		Block block = world.getBlockState(pos).getBlock();

		if (block == Blocks.AIR || block == this) {
			return;
		}
		IBlockState state = world.getBlockState(pos);
		if (extreme && state.getMaterial() == Material.ROCK && state.getBlockHardness(world, pos) > 0) {
			block.dropBlockAsItem(world, pos, state, 0);
			world.setBlockToAir(pos);
			triggerInteractionEffects(world, pos);
		} else if (hasInteraction(state)) {
			BlockWrapper result = getInteraction(state);
			world.setBlockState(pos, result.blockstate, 3);
		}
	}

	protected void triggerInteractionEffects(World world, BlockPos pos) {

		world.playSound(null, new BlockPos(pos).add(0.5F, 0.5F, 0.5F), SoundEvent.REGISTRY.getObject(new ResourceLocation("block.stone.break")), SoundCategory.BLOCKS, 0.5F, 0.9F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F);
	}

}
