package cofh.thermalfoundation.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import cofh.core.CoFHProps;
import cofh.core.util.CoreUtils;
import cofh.lib.util.helpers.ServerHelper;
import cofh.thermalfoundation.ThermalFoundation;
import cofh.thermalfoundation.entity.monster.EntityBlitz;

public class EntityBlitzBolt extends EntityThrowable {

	public static void initialize() {

		EntityRegistry.registerModEntity(EntityBlitzBolt.class, "blitzBolt", CoreUtils.getEntityId(), ThermalFoundation.instance,
				CoFHProps.ENTITY_TRACKING_DISTANCE, 1, true);
	}

	protected static class DamageSourceBlitz extends EntityDamageSource {

		public DamageSourceBlitz() {

			this(null);
		}

		public DamageSourceBlitz(Entity source) {

			super("blitz", source);
		}

		public static DamageSource causeDamage(EntityBlitzBolt entityProj, Entity entitySource) {

			return (new EntityDamageSourceIndirect("blitz", entityProj, entitySource == null ? entityProj : entitySource)).setProjectile();
		}
	}

	protected static class PotionEffectBlitz extends PotionEffect {

		public PotionEffectBlitz(Potion potion, int duration, int amplifier, boolean isAmbient, boolean showparticles) {

			super(potion, duration, amplifier, isAmbient, showparticles);
			getCurativeItems().clear();
		}

		public PotionEffectBlitz(int duration, int amplifier) {

			this(Potion.REGISTRY.getObject(new ResourceLocation("nausea")), duration, amplifier, false, true);
		}

	}

	public static DamageSource blitzDamage = new DamageSourceBlitz();
	public static PotionEffect blitzEffect = new PotionEffectBlitz(5 * 20, 2);

	/* Required Constructor */
	public EntityBlitzBolt(World world) {

		super(world);
	}

	public EntityBlitzBolt(World world, EntityLivingBase thrower) {

		super(world, thrower);
	}

	public EntityBlitzBolt(World world, double x, double y, double z) {

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
				if (pos.entityHit instanceof EntityBlitz) {
					pos.entityHit.attackEntityFrom(DamageSourceBlitz.causeDamage(this, getThrower()), 0);
				} else {
					if (pos.entityHit.attackEntityFrom(DamageSourceBlitz.causeDamage(this, getThrower()), 5F) && pos.entityHit instanceof EntityLivingBase) {
						EntityLivingBase living = (EntityLivingBase) pos.entityHit;
						living.addPotionEffect(new PotionEffect(EntityBlitzBolt.blitzEffect));
					}
				}
			}
			for (int i = 0; i < 8; i++) {
				worldObj.spawnParticle(EnumParticleTypes.CLOUD, posX, posY, posZ, this.rand.nextDouble(), this.rand.nextDouble(), this.rand.nextDouble());
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
