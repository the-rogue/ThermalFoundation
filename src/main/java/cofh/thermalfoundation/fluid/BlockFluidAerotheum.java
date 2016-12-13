package cofh.thermalfoundation.fluid;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import org.apache.logging.log4j.Level;

import cofh.core.fluid.BlockFluidCoFHBase;
import cofh.lib.util.helpers.ServerHelper;
import cofh.thermalfoundation.ThermalFoundation;

public class BlockFluidAerotheum extends BlockFluidCoFHBase {

	Random random = new Random();

	public static final int LEVELS = 6;
	public static final Material materialFluidAerotheum = new MaterialLiquid(MapColor.AIR);

	private static boolean effect = true;
	private static boolean enableSourceDissipate = true;
	private static boolean enableSourceFloat = true;
	private static int maxHeight = 120;

	public BlockFluidAerotheum() {

		super("thermalfoundation", TFFluids.fluidAerotheum, materialFluidAerotheum, "aerotheum");
		setQuantaPerBlock(LEVELS);
		setTickRate(8);

		setHardness(1F);
		setLightOpacity(0);
		setParticleColor(0.65F, 0.65F, 0.48F);
	}

	@Override
	public boolean preInit() {

		GameRegistry.register(this, new ResourceLocation("FluidAerotheum"));

		String category = "Fluid.Aerotheum";
		String comment = "Enable this for Fluid Aerotheum to do...things.";
		effect = ThermalFoundation.config.get(category, "Effect", true, comment);

		comment = "Enable this for Fluid Aerotheum Source blocks to dissipate back into air above a given y-value.";
		enableSourceDissipate = ThermalFoundation.config.get(category, "Dissipate", enableSourceDissipate, comment);

		comment = "Enable this for Fluid Aerotheum Source blocks to gradually float upwards.";
		enableSourceFloat = ThermalFoundation.config.get(category, "Float", enableSourceFloat, comment);

		int cfgHeight;
		comment = "This adjusts the y-value where Fluid Aerotheum will *always* dissipate, if that is enabled.";
		cfgHeight = ThermalFoundation.config.get(category, "MaxHeight", maxHeight, comment);

		if (cfgHeight >= maxHeight / 2) {
			maxHeight = cfgHeight;
		} else {
			ThermalFoundation.log.log(Level.INFO, "'Fluid.Aerotheum.MaxHeight' config value is out of acceptable range. Using default: " + maxHeight + ".");
		}
		return true;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {

		if (!effect) {
			return;
		}
		if (entity instanceof EntityLivingBase) {
			if (entity.motionX > 0.1) {
				entity.motionX = 0.1;
			}
			if (entity.motionZ > 0.1) {
				entity.motionZ = 0.1;
			}
			if (entity.motionY < -0.2) {
				entity.motionY *= 0.5;
				if (entity.fallDistance > 20) {
					entity.fallDistance = 20;
				} else {
					entity.fallDistance *= 0.5;
				}
			}
		} else if (entity instanceof IProjectile) {
			entity.motionX *= random.nextGaussian() * 1.5;
			entity.motionZ *= random.nextGaussian() * 1.5;
		}
		if (ServerHelper.isClientWorld(world)) {
			return;
		}
		if (world.getTotalWorldTime() % 8 == 0 && entity instanceof EntityLivingBase && !((EntityLivingBase) entity).isEntityUndead()) {
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("invisibility"), 3 * 20, 0));
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("water_breathing"), 30 * 20, 0));
		}
	}

	@Override
	public int getLightValue(IBlockState state) {

		return TFFluids.fluidAerotheum.getLuminosity();
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

		if (state.getBlock().getMetaFromState(world.getBlockState(pos)) == 0) {
			if (rand.nextInt(3) == 0) {
				if (shouldSourceBlockCondense(world, state, pos)) {
					world.setBlockToAir(pos);
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

		return enableSourceDissipate && (pos.getY() + densityDir > maxHeight || pos.getY() + densityDir > world.getHeight());
	}

	protected boolean shouldSourceBlockFloat(World world, IBlockState state, BlockPos pos) {

		return enableSourceFloat && (world.getBlockState(pos.add(0, densityDir, 0)) == this && state.getBlock().getMetaFromState(world.getBlockState(pos)) != 0);
	}

}
