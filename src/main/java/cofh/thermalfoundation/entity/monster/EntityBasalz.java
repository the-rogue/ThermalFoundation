package cofh.thermalfoundation.entity.monster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import cofh.core.CoFHProps;
import cofh.core.entity.EntitySelectorInRangeByType;
import cofh.core.util.CoreUtils;
import cofh.lib.util.helpers.ItemHelper;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.helpers.ServerHelper;
import cofh.thermalfoundation.ThermalFoundation;
import cofh.thermalfoundation.entity.projectile.EntityBasalzBolt;
import cofh.thermalfoundation.item.TFItems;

public class EntityBasalz extends EntityMob {

	static int entityId = -1;

	static boolean enable = true;
	static boolean restrictLightLevel = true;

	static int spawnLightLevel = 8;

	static int spawnWeight = 10;
	static int spawnMin = 1;
	static int spawnMax = 4;

	static {
		String category = "Mob.Basalz";
		String comment = "";

		comment = "Set this to false to disable Basalzes entirely. Jerk.";
		enable = ThermalFoundation.config.get(category, "Enable", enable, comment);

		category = "Mob.Basalz.Spawn";

		comment = "Set this to false for Basalzes to spawn at any light level.";
		restrictLightLevel = ThermalFoundation.config.get(category, "Light.Limit", restrictLightLevel, comment);

		comment = "This sets the maximum light level Basalzes can spawn at, if restricted.";
		spawnLightLevel = MathHelper.clamp(ThermalFoundation.config.get(category, "Light.Level", spawnLightLevel, comment), 0, 15);

		comment = "This sets the minimum number of Basalzes that spawn in a group.";
		spawnMin = MathHelper.clamp(ThermalFoundation.config.get(category, "MinGroupSize", spawnMin, comment), 1, 10);

		comment = "This sets the maximum light number of Basalzes that spawn in a group.";
		spawnMax = MathHelper.clamp(ThermalFoundation.config.get(category, "MaxGroupSize", spawnMax, comment), spawnMin, 24);

		comment = "This sets the relative spawn weight for Basalzes.";
		spawnWeight = ThermalFoundation.config.get(category, "SpawnWeight", spawnWeight, comment);
	}

	public static void initialize() {

		if (!enable) {
			return;
		}
		try {
			EntityRegistry.registerModEntity(EntityBasalz.class, "Basalz", CoreUtils.getEntityId(), ThermalFoundation.instance, CoFHProps.ENTITY_TRACKING_DISTANCE, 1, true, 0x606060, 0xB3ABA3);
		} catch (Exception e) {
			ThermalFoundation.log.error("Another mod is improperly using the Entity Registry. This is REALLY bad. Using a mod-specific ID instead.", e);
		}
		
		// Add Basalz spawn to Mountain biomes
		List<Biome> validBiomes = new ArrayList<Biome>(Arrays.asList(BiomeDictionary.getBiomesForType(Type.MOUNTAIN)));

		// Add Basalz spawn to Wasteland biomes
		for (Biome biome : BiomeDictionary.getBiomesForType(Type.WASTELAND)) {
			if (!validBiomes.contains(biome)) {
				validBiomes.add(biome);
			}
		}
		// Remove Basalz spawn from End biomes
		for (Biome biome : BiomeDictionary.getBiomesForType(Type.END)) {
			if (validBiomes.contains(biome)) {
				validBiomes.remove(biome);
			}
		}
		EntityRegistry.addSpawn(EntityBasalz.class, spawnWeight, spawnMin, spawnMax, EnumCreatureType.MONSTER, validBiomes.toArray(new Biome[0]));
	}

	/** Random offset used in floating behaviour */
	protected float heightOffset = 0.5F;

	/** ticks until heightOffset is randomized */
	protected int heightOffsetUpdateTime;
	protected int firingState;

	public static final String SOUND_AMBIENT = CoreUtils.getSoundName(ThermalFoundation.modId, "mobBasalzAmbient");
	public static final String SOUND_ATTACK = CoreUtils.getSoundName(ThermalFoundation.modId, "mobBasalzAttack");
	public static final String SOUND_LIVING[] = { CoreUtils.getSoundName(ThermalFoundation.modId, "mobBasalzBreathe0"),
			CoreUtils.getSoundName(ThermalFoundation.modId, "mobBasalzBreathe1") };

	protected static final int SOUND_AMBIENT_FREQUENCY = 400; // How often it does ambient sound loop

	public EntityBasalz(World world) {

		super(world);
		this.experienceValue = 10;
	}

	@Override
	protected void applyEntityAttributes() {

		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
	}

	@Override
	protected void entityInit() {

		super.entityInit();
		this.dataManager.addObject(16, new Byte((byte) 0));
	}

	@Override
	protected String getLivingSound() {

		return SOUND_LIVING[this.rand.nextInt(2)];
	}

	@Override
	protected SoundEvent getHurtSound() {

		return SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.blaze.hurt"));
	}

	@Override
	protected SoundEvent getDeathSound() {

		return SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.blaze.death"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float par1) {

		return 0xF000F0;
	}

	@Override
	public float getBrightness(float par1) {

		return 2.0F;
	}

	@Override
	public void onLivingUpdate() {

		if (ServerHelper.isServerWorld(worldObj)) {
			--this.heightOffsetUpdateTime;

			if (this.heightOffsetUpdateTime <= 0) {
				this.heightOffsetUpdateTime = 100;
				this.heightOffset = 0.5F + (float) this.rand.nextGaussian() * 3.0F;
			}
			Entity target = this.getEntityToAttack();
			if (target != null) {
				if ((target.posY + target.getEyeHeight()) > (this.posY + this.getEyeHeight() + this.heightOffset)) {
					this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
				}
			}
		}
		if (this.rand.nextInt(SOUND_AMBIENT_FREQUENCY) == 0) {
			this.worldObj.playSoundEffect(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, SOUND_AMBIENT, this.rand.nextFloat() * 0.2F + 0.1F,
					this.rand.nextFloat() * 0.3F + 0.4F);
		}
		if (!this.onGround && this.motionY < 0.0D) {
			this.motionY *= 0.6D;
		}
		for (int i = 0; i < 2; i++) {
			this.worldObj.spawnParticle("townaura", this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble()
					* (this.height * 0.2D), this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
		}
		super.onLivingUpdate();
	}

	/**
	 * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking (Animals, Spiders at day, peaceful PigZombies).
	 */
	@Override
	protected Entity findPlayerToAttack() {

		EntityPlayer player = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);
		if (player != null && this.canEntityBeSeen(player)) {
			return player;
		}
		return getClosestVictim(16.0D);
	}

	public Entity getClosestVictim(double dist) {

		AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(this.posX - dist, this.posY - dist, this.posZ - dist, this.posX + dist, this.posY + dist, this.posZ
				+ dist);
		EntitySelectorInRangeByType entsel = new EntitySelectorInRangeByType(this, dist, EntityVillager.class);
		// TODO: should this target INpc instead?
		List<Entity> entities = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, aabb, entsel);
		if (entities.isEmpty()) {
			return null;
		}
		Entity victim = null;
		double closest = Double.MAX_VALUE;
		for (Entity entity : entities) {
			double distVsq = this.getDistanceSqToEntity(entity);
			if (distVsq < closest) {
				closest = distVsq;
				victim = entity;
			}
		}
		return victim;
	}

	@Override
	protected void attackEntity(Entity target, float distance) {

		// Melee distance
		if (this.attackTime <= 0 && distance < 2.0F && target.boundingBox.maxY > this.boundingBox.minY && target.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			this.attackEntityAsMob(target);
		}
		// Within range (30)
		else if (distance < 30.0F) {
			double dX = target.posX - this.posX;
			double dZ = target.posZ - this.posZ;

			if (this.attackTime == 0) {
				++this.firingState;

				if (this.firingState == 1) {
					this.attackTime = 60;
					this.setInAttackMode(true); // Flary goodness :D
				} else if (this.firingState <= 4) {
					this.attackTime = 6;
				} else {
					this.attackTime = 80; // 100
					this.firingState = 0;
					this.setInAttackMode(false); // Unflary sadness :(
				}
				if (this.firingState > 1) {
					EntityBasalzBolt bolt = new EntityBasalzBolt(this.worldObj, this);
					bolt.posY = this.posY + this.height / 2.0F + 0.5D;
					this.playSound(SOUND_ATTACK, 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
					this.worldObj.spawnEntityInWorld(bolt);
				}
			}
			this.rotationYaw = (float) (Math.atan2(dZ, dX) * 180.0D / Math.PI) - 90.0F;
			this.hasAttacked = true;
		}
	}

	@Override
	protected void fall(float distance) {

	}

	@Override
	protected void dropFewItems(boolean wasHitByPlayer, int looting) {

		if (wasHitByPlayer) {
			int items = this.rand.nextInt(2 + looting);
			for (int i = 0; i < items; i++) {
				this.entityDropItem(ItemHelper.cloneStack(TFItems.dustObsidian, 1), 0);
			}
			items = this.rand.nextInt(2 + looting);
			for (int i = 0; i < items; i++) {
				this.entityDropItem(ItemHelper.cloneStack(TFItems.rodBasalz, 1), 0);
			}
		}
	}

	public boolean isInAttackMode() {

		return (this.dataManager.getWatchableObjectByte(16) & 1) != 0;
	}

	public void setInAttackMode(boolean mode) {

		byte b0 = this.dataManager.getWatchableObjectByte(16);

		if (mode) {
			b0 = (byte) (b0 | 1);
		} else {
			b0 &= -2;
		}
		this.dataManager.updateObject(16, Byte.valueOf(b0));
	}

	@Override
	protected boolean isValidLightLevel() {

		if (!restrictLightLevel) {
			return true;
		}
		int i = MathHelper.floor(this.posX);
		int j = MathHelper.floor(this.boundingBox.minY);
		int k = MathHelper.floor(this.posZ);

		if (this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, i, j, k) > this.rand.nextInt(32)) {
			return false;
		} else {
			int l = this.worldObj.getBlockLightValue(i, j, k);

			if (this.worldObj.isThundering()) {
				int i1 = this.worldObj.skylightSubtracted;
				this.worldObj.skylightSubtracted = 10;
				l = this.worldObj.getBlockLightValue(i, j, k);
				this.worldObj.skylightSubtracted = i1;
			}
			return l <= this.rand.nextInt(spawnLightLevel);
		}
	}

}
