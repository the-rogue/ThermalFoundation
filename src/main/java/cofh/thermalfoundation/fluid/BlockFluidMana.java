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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import cofh.core.fluid.BlockFluidInteractive;
import cofh.core.util.CoreUtils;
import cofh.lib.util.BlockWrapper;
import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.helpers.MathHelper;
import cofh.thermalfoundation.ThermalFoundation;
import cofh.thermalfoundation.block.TFBlocks;

public class BlockFluidMana extends BlockFluidInteractive {

	public static final int LEVELS = 6;
	public static final Material materialFluidMana = new MaterialLiquid(MapColor.PURPLE);

	private static boolean effect = true;

	public BlockFluidMana() {

		super("thermalfoundation", TFFluids.fluidMana, materialFluidMana, "mana");
		setQuantaPerBlock(LEVELS);
		setTickRate(10);

		setHardness(2000F);
		setLightOpacity(2);
		setParticleColor(0.2F, 0.0F, 0.4F);
	}

	@Override
	public boolean preInit() {

		GameRegistry.register(this, new ResourceLocation("FluidMana"));

		addInteraction(Blocks.DIRT.getDefaultState(), Blocks.GRASS.getDefaultState());
		addInteraction(Blocks.DIRT, 1, 1, Blocks.DIRT, 2);
		addInteraction(Blocks.GLASS.getDefaultState(), Blocks.SAND.getDefaultState());
		// addInteraction(Blocks.stained_glass, -1, Blocks.glass);
		// addInteraction(Blocks.diamond_ore, -1, TFBlocks.blockOre, 1);
		// addInteraction(Blocks.cauldron, -1, Blocks.carpet, 1);
		// addInteraction(Blocks.cactus, -1, Blocks.cake, 5);
		// addInteraction(Blocks.enchanting_table, -1, Blocks.brewing_stand, 0);
		// addInteraction(Blocks.bookshelf, 0, Blocks.chest, 0);
		// addInteraction(Blocks.ender_chest, -1, TFBlocks.blockFluidEnder, 1);
		// addInteraction(Blocks.dragon_egg, -1, Blocks.bedrock, 1);
		addInteraction(Blocks.REDSTONE_ORE.getDefaultState(), Blocks.LIT_REDSTONE_ORE.getDefaultState());
		addInteraction(Blocks.LAPIS_ORE.getDefaultState(), Blocks.LAPIS_BLOCK.getDefaultState());
		addInteraction(Blocks.FARMLAND.getDefaultState(), Blocks.MYCELIUM.getDefaultState());
		for (int i = 8; i-- > 0;) {
			addInteraction(Blocks.DOUBLE_STONE_SLAB, i, i, Blocks.DOUBLE_STONE_SLAB, i + 8);
		}
		addInteraction(TFBlocks.blockOre, 2, 2, TFBlocks.blockOre, 6);
		addInteraction(TFBlocks.blockOre, 3, 3, Blocks.GOLD_ORE.getDefaultState());
		addInteraction(TFBlocks.blockStorage, 2, 2, TFBlocks.blockStorage, 6);
		addInteraction(TFBlocks.blockStorage, 3, 3, Blocks.GOLD_BLOCK.getDefaultState());

		String category = "Fluid.Mana";
		String comment = "Enable this for Fluid Mana to do...things.";
		effect = ThermalFoundation.config.get(category, "Effect", true, comment);

		return true;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {

		if (!effect) {
			return;
		}
		if (world.getTotalWorldTime() % 4 == 0) {
			if (MathHelper.RANDOM.nextInt(100) != 0) {
				return;
			}
			BlockPos pos2 = new BlockPos(pos).add(- 8 + world.rand.nextInt(17), world.rand.nextInt(8), - 8 + world.rand.nextInt(17));

			if (!world.getBlockState(pos2).getMaterial().isSolid()) {
				if (entity instanceof EntityLivingBase) {
					CoreUtils.teleportEntityTo((EntityLivingBase) entity, pos2.getX(), pos2.getY(), pos2.getZ());
				} else {
					entity.setPosition(pos2.getX(), pos2.getY(), pos2.getZ());
					entity.worldObj.playSound(null, pos2, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.PLAYERS, 1.0F, 1.0F);
					entity.playSound(SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), 1.0F, 1.0F);
				}
			}
		}
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {

		return effect ? 15 : 0;
	}

	@Override
	public int getLightValue(IBlockState state) {

		return TFFluids.fluidMana.getLuminosity();
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

		if (effect) {
			checkForInteraction(world, pos);
		}
		if (state.getBlock().getMetaFromState(world.getBlockState(pos)) == 0) {
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
			pos2.add(BlockHelper.SIDE_COORD_MOD[i][0], 0, BlockHelper.SIDE_COORD_MOD[i][2]);
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
		} else if (world.isSideSolid(pos, EnumFacing.UP) && world.isAirBlock(new BlockPos(pos).add(0, 1, 0))) {
			if (MathHelper.RANDOM.nextInt(2) == 0) {
				world.setBlockState(new BlockPos(pos).add(0, 1, 0), Blocks.SNOW_LAYER.getDefaultState(), 3);
			} else {
				world.setBlockState(new BlockPos(pos).add(0, 1, 0), Blocks.FIRE.getDefaultState(), 3);
			}
		}
	}

	protected void triggerInteractionEffects(World world, BlockPos pos) {

		if (MathHelper.RANDOM.nextInt(10) == 0) {
			world.playSound(null, new BlockPos(pos).add(0.5F, 0.5F, 0.5F), SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.experience_orb.pickup")), SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
		}
		for (int i = 0; i < 8; i++) {
			world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, pos.getX() + Math.random() * 1.1,  pos.getY() + 1.3D, pos.getZ() + Math.random() * 1.1, 0.0D, -0.5D, 0.0D);
		}
	}

}
