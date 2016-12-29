package cofh.thermalfoundation.fluid;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cofh.core.fluid.BlockFluidCoFHBase;
import cofh.thermalfoundation.ThermalFoundation;

public class BlockFluidRedstone extends BlockFluidCoFHBase {

	public static final int LEVELS = 8;
	public static final Material materialFluidRedstone = new MaterialLiquid(MapColor.RED);

	private static boolean effect = true;

	public BlockFluidRedstone() {

		super("thermalfoundation", TFFluids.fluidRedstone, Material.WATER, "redstone");
		setQuantaPerBlock(LEVELS);
		setTickRate(5);

		setHardness(100F);
		setLightOpacity(2);
		setParticleColor(0.4F, 0.0F, 0.0F);
	}

	@Override
	public boolean preInit() {

		super.preInit();
		String category = "Fluid.Redstone";
		String comment = "Enable this for Fluid Redstone to emit a signal proportional to its fluid level.";
		effect = ThermalFoundation.config.get(category, "Effect", true, comment);
		return true;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {

	}

	@Override
	public boolean canProvidePower(IBlockState state) {

		return effect;
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess world, BlockPos pos, EnumFacing side) {

		return effect ? blockState.getBlock().getMetaFromState(blockState) * 2 + 1 : 0;
	}

	@Override
	public int getLightValue(IBlockState state) {

		return TFFluids.fluidRedstone.getLuminosity();
	}
}
