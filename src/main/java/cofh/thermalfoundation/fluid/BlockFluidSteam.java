package cofh.thermalfoundation.fluid;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import cofh.core.fluid.BlockFluidCoFHBase;

public class BlockFluidSteam extends BlockFluidCoFHBase {

	public static final int LEVELS = 8;
	public static final Material materialFluidSteam = new MaterialLiquid(MapColor.SILVER);

	// private static boolean effect = true;

	public BlockFluidSteam() {

		super("thermalfoundation", TFFluids.fluidSteam, materialFluidSteam, "steam");
		setQuantaPerBlock(LEVELS);
		setTickRate(2);

		setHardness(1F);
		setLightOpacity(0);
		setParticleColor(0.9F, 0.9F, 0.9F);
	}

	@Override
	public boolean preInit() {
		super.preInit();
		return true;
	}

}
