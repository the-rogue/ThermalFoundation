package cofh.thermalfoundation.entity.projectile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import cofh.core.CoFHProps;
import cofh.core.util.CoreUtils;
import cofh.lib.util.helpers.ServerHelper;
import cofh.thermalfoundation.ThermalFoundation;
import cofh.thermalfoundation.entity.monster.EntityBlizz;

public class EntityBlizzBolt extends EntityThrowable {

	public static void initialize() {

		EntityRegistry.registerModEntity(EntityBlizzBolt.class, "blizzBolt", CoreUtils.getEntityId(), ThermalFoundation.instance,
				CoFHProps.ENTITY_TRACKING_DISTANCE, 1, true);
	}

	protected static class DamageSourceBlizz extends EntityDamageSource {

		public DamageSourceBlizz() {

			this(null);
		}

		public DamageSourceBlizz(Entity source) {

			super("blizz", source);
		}

		public static DamageSource causeDamage(EntityBlizzBolt entityProj, Entity entitySource) {

			return (new EntityDamageSourceIndirect("blizz", entityProj, entitySource == null ? entityProj : entitySource)).setProjectile();
		}
	}

	protected static class PotionEffectBlizz extends PotionEffect {

		public PotionEffectBlizz(Potion potion, int duration, int amplifier, boolean isAmbient, boolean showparticles) {

			super(potion, duration, amplifier, isAmbient, showparticles);
			getCurativeItems().clear();
		}

		public PotionEffectBlizz(int duration, int amplifier) {

			this(Potion.REGISTRY.getObject(new ResourceLocation("slowness")), duration, amplifier, false, true);
		}

	}

	public static DamageSource blizzDamage = new DamageSourceBlizz();
	public static PotionEffect blizzEffect = new PotionEffectBlizz(5 * 20, 2);
	public static double accelMultiplier = 0.2D;

	/* Required Constructor */
	public EntityBlizzBolt(World world) {

		super(world);
	}

	public EntityBlizzBolt(World world, EntityLivingBase thrower) {

		super(world, thrower);
	}

	public EntityBlizzBolt(World world, double x, double y, double z) {

		super(world, x, y, z);
	}

	@Override
	protected float getGravityVelocity() {

		return 0.005F;
	}

	@Override
	protected void onImpact(RayTraceResult pos) {

		if (ServerHelper.isServerWorld(worldObj)) {
			if (pos.entityHit != null) {
				if (pos.entityHit instanceof EntityBlizz) {
					pos.entityHit.attackEntityFrom(DamageSourceBlizz.causeDamage(this, getThrower()), 0);
				} else {
					if (pos.entityHit.attackEntityFrom(DamageSourceBlizz.causeDamage(this, getThrower()), pos.entityHit.isImmuneToFire() ? 8F : 5F)
							&& pos.entityHit instanceof EntityLivingBase) {
						EntityLivingBase living = (EntityLivingBase) pos.entityHit;
						living.addPotionEffect(new PotionEffect(EntityBlizzBolt.blizzEffect));
					}
				}
			} else {
				BlockPos bpos = new BlockPos(pos.getBlockPos()).offset(pos.sideHit);

				if (worldObj.isAirBlock(bpos)) {
					IBlockState block = worldObj.getBlockState(new BlockPos(bpos).add(0, -1, 0));

					if (block != null && block.isSideSolid(worldObj, new BlockPos(bpos).add(0, -1, 0), EnumFacing.UP)) {
						worldObj.setBlockState(bpos, Blocks.SNOW_LAYER.getDefaultState());
					}
				}
			}
			for (int i = 0; i < 8; i++) {
				worldObj.spawnParticle(EnumParticleTypes.SNOWBALL, posX, posY, posZ, this.rand.nextDouble(), this.rand.nextDouble(), this.rand.nextDouble());
			}
			setDead();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float f) {

		return 0xF000F0;
	}

}
