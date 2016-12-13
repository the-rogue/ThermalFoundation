package cofh.thermalfoundation.fluid;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import cofh.core.fluid.BlockFluidCoFHBase;
import cofh.core.util.CoreUtils;
import cofh.thermalfoundation.ThermalFoundation;

public class BlockFluidEnder extends BlockFluidCoFHBase {

	public static final int LEVELS = 4;
	public static final Material materialFluidEnder = new MaterialLiquid(MapColor.GREEN);

	private static boolean effect = true;

	public BlockFluidEnder() {

		super("thermalfoundation", TFFluids.fluidEnder, materialFluidEnder, "ender");
		setQuantaPerBlock(LEVELS);
		setTickRate(20);

		setHardness(2000F);
		setLightOpacity(7);
		setParticleColor(0.05F, 0.2F, 0.2F);
	}

	@Override
	public boolean preInit() {

		GameRegistry.register(this, new ResourceLocation("FluidEnder"));

		String category = "Fluid.Ender";
		String comment = "Enable this for Fluid Ender to randomly teleport entities on contact.";
		effect = ThermalFoundation.config.get(category, "Effect", true, comment);

		return true;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {

		if (!effect || world.isRemote) {
			return;
		}
		if (world.getTotalWorldTime() % 8 == 0) {
			pos.add(-8 + world.rand.nextInt(17), world.rand.nextInt(8), -8 + world.rand.nextInt(17));

			if (!world.getBlockState(pos).getMaterial().isSolid()) {
				CoreUtils.teleportEntityTo(entity, pos.getX(), pos.getY(), pos.getZ());
			}
		}
	}

	@Override
	public int getLightValue(IBlockState state) {

		return TFFluids.fluidEnder.getLuminosity();
	}

}
