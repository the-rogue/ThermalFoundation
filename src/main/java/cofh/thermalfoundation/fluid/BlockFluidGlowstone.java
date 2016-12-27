package cofh.thermalfoundation.fluid;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.apache.logging.log4j.Level;

import cofh.core.fluid.BlockFluidCoFHBase;
import cofh.lib.util.helpers.ServerHelper;
import cofh.thermalfoundation.ThermalFoundation;

public class BlockFluidGlowstone extends BlockFluidCoFHBase {

	public static final int LEVELS = 6;
	public static final Material materialFluidGlowstone = new MaterialLiquid(MapColor.YELLOW);

	private static boolean effect = true;
	private static boolean enableSourceCondense = true;
	private static boolean enableSourceFloat = true;
	private static int maxHeight = 120;

	public BlockFluidGlowstone() {

		super("thermalfoundation", TFFluids.fluidGlowstone, materialFluidGlowstone, "glowstone");
		setQuantaPerBlock(LEVELS);
		setTickRate(10);

		setHardness(1F);
		setLightOpacity(0);
		setParticleColor(1.0F, 0.9F, 0.05F);
	}

	@Override
	public boolean preInit() {

		super.preInit();

		String category = "Fluid.Glowstone";
		String comment = "Enable this for Fluid Glowstone to do...something.";
		effect = ThermalFoundation.config.get(category, "Effect", true, comment);

		comment = "Enable this for Fluid Glowstone Source blocks to condense back into solid Glowstone above a given y-value.";
		enableSourceCondense = ThermalFoundation.config.get(category, "Condense", enableSourceCondense, comment);

		comment = "Enable this for Fluid Glowstone Source blocks to gradually float upwards.";
		enableSourceFloat = ThermalFoundation.config.get(category, "Float", enableSourceFloat, comment);

		int cfgHeight;
		comment = "This adjusts the y-value where Fluid Glowstone will *always* condense, if that is enabled. It will also condense above 80% of this value, if it cannot flow.";
		cfgHeight = ThermalFoundation.config.get(category, "MaxHeight", maxHeight, comment);

		if (cfgHeight >= maxHeight / 2) {
			maxHeight = cfgHeight;
		} else {
			ThermalFoundation.log.log(Level.INFO, "'Fluid.Glowstone.MaxHeight' config value is out of acceptable range. Using default: " + maxHeight + ".");
		}
		return true;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {

		if (!effect) {
			return;
		}
		if (entity instanceof EntityLivingBase) {
			if (entity.motionY < -0.2) {
				entity.motionY *= 0.5;
				if (entity.fallDistance > 20) {
					entity.fallDistance = 20;
				} else {
					entity.fallDistance *= 0.95;
				}
			}
		}
		if (ServerHelper.isClientWorld(world)) {
			return;
		}
		if (world.getTotalWorldTime() % 8 == 0 && entity instanceof EntityLivingBase && !((EntityLivingBase) entity).isEntityUndead()) {
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("speed"), 6 * 20, 0));
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("jump_boost"), 6 * 20, 0));
		}
	}

	@Override
	public int getLightValue(IBlockState state) {

		return TFFluids.fluidGlowstone.getLuminosity();
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

		if (state.getBlock().getMetaFromState(world.getBlockState(pos)) == 0) {
			if (rand.nextInt(3) == 0) {
				if (shouldSourceBlockCondense(world, state, pos)) {
					world.setBlockState(pos, Blocks.GLOWSTONE.getDefaultState());
					return;
				}
				if (shouldSourceBlockFloat(world, state, pos)) {
					world.setBlockState(new BlockPos(pos).add(0, densityDir, 0), this.getDefaultState(), 3);
					world.setBlockToAir(pos);
					return;
				}
			}
		} else if (pos.getY() + densityDir > maxHeight) {

			int quantaRemaining = quantaPerBlock - state.getBlock().getMetaFromState(world.getBlockState(pos));
			int expQuanta = -101;
			BlockPos pos2 = new BlockPos(pos).add(0, -(densityDir), 0);

			if (
					world.getBlockState(pos2) == this.getDefaultState() 
					|| world.getBlockState(new BlockPos(pos2).add(-1, 0, 0)) == this.getDefaultState() 
					|| world.getBlockState(new BlockPos(pos).add(1, 0, 0)) == this.getDefaultState()
					|| world.getBlockState(new BlockPos(pos).add(0, 0, -1)) == this.getDefaultState() 
					|| world.getBlockState(new BlockPos(pos).add(0, 0, 1)) == this.getDefaultState()
				) {
				expQuanta = quantaPerBlock - 1;

			} else {
				int maxQuanta = -100;
				maxQuanta = getLargerQuanta(world, new BlockPos(pos).add(-1, 0, 0), maxQuanta);
				maxQuanta = getLargerQuanta(world, new BlockPos(pos).add(1, 0, 0), maxQuanta);
				maxQuanta = getLargerQuanta(world, new BlockPos(pos).add(0, 0, -1), maxQuanta);
				maxQuanta = getLargerQuanta(world, new BlockPos(pos).add(0, 0, 1), maxQuanta);

				expQuanta = maxQuanta - 1;
			}
			// decay calculation
			if (expQuanta != quantaRemaining) {
				quantaRemaining = expQuanta;
				if (expQuanta <= 0) {
					world.setBlockToAir(pos);
				} else {
					world.notifyBlockUpdate(pos, state, state.withProperty(LEVEL, quantaPerBlock - expQuanta), 3);
					world.scheduleUpdate(pos, this, tickRate);
					world.notifyNeighborsOfStateChange(pos, this);
				}
			}
			return;
		}
		super.updateTick(world, pos, state, rand);
	}

	protected boolean shouldSourceBlockCondense(World world, IBlockState state, BlockPos pos) {

		return enableSourceCondense
				&& (pos.getY() + densityDir > maxHeight || pos.getY() + densityDir > world.getHeight() || pos.getY() + densityDir > maxHeight * 0.8F
						&& !canDisplace(world, new BlockPos(pos).add(0, densityDir, 0)));
	}

	protected boolean shouldSourceBlockFloat(World world, IBlockState state, BlockPos pos) {

		return enableSourceFloat && (world.getBlockState(pos.add(0, densityDir, 0)) == this && state.getBlock().getMetaFromState(world.getBlockState(pos)) != 0);
	}

}
