package cofh.thermalfoundation.entity.monster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import cofh.core.CoFHProps;
import cofh.core.util.CoreUtils;
import cofh.lib.util.helpers.ItemHelper;
import cofh.lib.util.helpers.MathHelper;
import cofh.thermalfoundation.ThermalFoundation;
import cofh.thermalfoundation.entity.projectile.EntityBlizzBolt;
import cofh.thermalfoundation.item.TFItems;

public class EntityBlizz extends EntityMob {

	static int entityId = -1;

	static boolean enable = true;
	static boolean restrictLightLevel = true;

	static int spawnLightLevel = 8;

	static int spawnWeight = 10;
	static int spawnMin = 1;
	static int spawnMax = 4;

	static {
		String category = "Mob.Blizz";
		String comment = "";

		comment = "Set this to false to disable Blizzes entirely. Jerk.";
		enable = ThermalFoundation.config.get(category, "Enable", enable, comment);

		category = "Mob.Blizz.Spawn";

		comment = "Set this to false for Blizzes to spawn at any light level.";
		restrictLightLevel = ThermalFoundation.config.get(category, "Light.Limit", restrictLightLevel, comment);

		comment = "This sets the maximum light level Blizzes can spawn at, if restricted.";
		spawnLightLevel = MathHelper.clamp(ThermalFoundation.config.get(category, "Light.Level", spawnLightLevel, comment), 0, 15);

		comment = "This sets the minimum number of Blizzes that spawn in a group.";
		spawnMin = MathHelper.clamp(ThermalFoundation.config.get(category, "MinGroupSize", spawnMin, comment), 1, 10);

		comment = "This sets the maximum light number of Blizzes that spawn in a group.";
		spawnMax = MathHelper.clamp(ThermalFoundation.config.get(category, "MaxGroupSize", spawnMax, comment), spawnMin, 24);

		comment = "This sets the relative spawn weight for Blizzes.";
		spawnWeight = ThermalFoundation.config.get(category, "SpawnWeight", spawnWeight, comment);
	}

	public static void initialize() {

		if (!enable) {
			return;
		}
		try {
			EntityRegistry.registerModEntity(EntityBlizz.class, "Blizz", CoreUtils.getEntityId(), ThermalFoundation.instance, CoFHProps.ENTITY_TRACKING_DISTANCE, 1, true, 0xE0FBFF, 0x6BE6FF);
		} catch (Exception e) {
			ThermalFoundation.log.error("Another mod is improperly using the Entity Registry. This is REALLY bad. Using a mod-specific ID instead.", e);
		}

		// Add Blizz spawn to Cold biomes
		List<Biome> validBiomes = new ArrayList<Biome>(Arrays.asList(BiomeDictionary.getBiomesForType(Type.COLD)));

		// Add Blizz spawn to Snowy biomes (in vanilla, all snowy are also cold)
		for (Biome biome : BiomeDictionary.getBiomesForType(Type.SNOWY)) {
			if (!validBiomes.contains(biome)) {
				validBiomes.add(biome);
			}
		}
		// Remove Blizz spawn from End biomes
		for (Biome biome : BiomeDictionary.getBiomesForType(Type.END)) {
			if (validBiomes.contains(biome)) {
				validBiomes.remove(biome);
			}
		}
		EntityRegistry.addSpawn(EntityBlizz.class, spawnWeight, spawnMin, spawnMax, EnumCreatureType.MONSTER, validBiomes.toArray(new Biome[0]));
	}
	
	private static final DataParameter<Boolean> IS_IN_ATTACK_MODE = EntityDataManager.<Boolean>createKey(EntityBasalz.class, DataSerializers.BOOLEAN);

	/** Random offset used in floating behaviour */
	protected float heightOffset = 0.5F;

	/** ticks until heightOffset is randomized */
	protected int heightOffsetUpdateTime;

	public static final SoundEvent SOUND_AMBIENT = CoreUtils.createSound(ThermalFoundation.modId, "mobBlizzAmbient");
	public static final SoundEvent SOUND_ATTACK = CoreUtils.createSound(ThermalFoundation.modId, "mobBlizzAttack");
	public static final SoundEvent SOUND_LIVING[] = { CoreUtils.createSound(ThermalFoundation.modId, "mobBlizzBreathe0"),
			CoreUtils.createSound(ThermalFoundation.modId, "mobBlizzBreathe1"), CoreUtils.createSound(ThermalFoundation.modId, "mobBlizzBreathe2") };

	protected static final int SOUND_AMBIENT_FREQUENCY = 400; // How often it does ambient sound loop

	public EntityBlizz(World world) {

		super(world);
		this.experienceValue = 10;
	}
	
    protected void initEntityAI()
    {
        this.tasks.addTask(4, new EntityBlizz.AIBoltAttack(this));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityVillager>(this, EntityVillager.class, true));
    }

	@Override
	protected void applyEntityAttributes() {

		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
	}

	@Override
	protected void entityInit() {

		super.entityInit();
		this.dataManager.register(IS_IN_ATTACK_MODE, Boolean.valueOf(false));
	}

	@Override
	protected SoundEvent getAmbientSound() {

		return SOUND_LIVING[this.rand.nextInt(3)];
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

        if (!this.onGround && this.motionY < 0.0D)
        {
            this.motionY *= 0.6D;
        }

        if (this.worldObj.isRemote)
        {
            if (this.rand.nextInt(SOUND_AMBIENT_FREQUENCY) == 0 && !this.isSilent())
            {
                this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, SOUND_AMBIENT, this.getSoundCategory(), 1.0F + this.rand.nextFloat() * 0.2F + 0.1F, this.rand.nextFloat() * 0.3F + 0.4F, false);
            }

            for (int i = 0; i < 2; ++i)
            {
                this.worldObj.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)(this.height * 0.2D), this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }
        super.onLivingUpdate();
	}

    protected void updateAITasks()
    {
        --this.heightOffsetUpdateTime;

        if (this.heightOffsetUpdateTime <= 0)
        {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5F + (float)this.rand.nextGaussian() * 3.0F;
        }

        EntityLivingBase entitylivingbase = this.getAttackTarget();

        if (entitylivingbase != null && entitylivingbase.posY + (double)entitylivingbase.getEyeHeight() > this.posY + (double)this.getEyeHeight() + (double)this.heightOffset)
        {
            this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
            this.isAirBorne = true;
        }
        super.updateAITasks();
    }

	@Override
	public void fall(float distance, float damageMultiplier) {

	}

	@Override
	protected void dropFewItems(boolean wasHitByPlayer, int looting) {

		if (wasHitByPlayer) {
			int items = this.rand.nextInt(4 + looting);
			for (int i = 0; i < items; i++) {
				this.entityDropItem(new ItemStack(Items.SNOWBALL), 0);
			}
			items = this.rand.nextInt(2 + looting);
			for (int i = 0; i < items; i++) {
				this.entityDropItem(ItemHelper.cloneStack(TFItems.rodBlizz, 1), 0);
			}
		}
	}

	public boolean isInAttackMode() {

		return ((Boolean)this.dataManager.get(IS_IN_ATTACK_MODE)).booleanValue();
	}

	public void setInAttackMode(boolean mode) {
		
		this.dataManager.set(IS_IN_ATTACK_MODE, Boolean.valueOf(mode));
	}

	@Override
	protected boolean isValidLightLevel() {

		if (!restrictLightLevel) {
			return true;
		}
		
		BlockPos pos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);

		if (this.worldObj.getLightFor(EnumSkyBlock.SKY, pos) > this.rand.nextInt(32)) {
			return false;
		} else {
			int l = this.worldObj.getLightFromNeighbors(pos);

			if (this.worldObj.isThundering()) {
				int i1 = this.worldObj.skylightSubtracted;
				this.worldObj.skylightSubtracted = 10;
				l = this.worldObj.getLightFromNeighbors(pos);
				this.worldObj.skylightSubtracted = i1;
			}
			return l <= this.rand.nextInt(spawnLightLevel);
		}
	}

	static class AIBoltAttack extends EntityAIBase
    {
        private final EntityBlizz blizz;
        private int firingState;
        private int attackTime;

        public AIBoltAttack(EntityBlizz blizz)
        {
            this.blizz = blizz;
            this.setMutexBits(3);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            EntityLivingBase entitylivingbase = this.blizz.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            this.firingState = 0;
        }

        /**
         * Resets the task
         */
        public void resetTask()
        {
            this.blizz.setInAttackMode(false);
        }

        /**
         * Updates the task
         */
        public void updateTask()
        {
            --this.attackTime;
            EntityLivingBase entitylivingbase = this.blizz.getAttackTarget();
            double d0 = this.blizz.getDistanceSqToEntity(entitylivingbase);

            if (d0 < 4.0D)
            {
                if (this.attackTime <= 0)
                {
                    this.attackTime = 20;
                    this.blizz.attackEntityAsMob(entitylivingbase);
                }

                this.blizz.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
            }
            else if (d0 < 256.0D)
            {

                if (this.attackTime <= 0)
                {
                    ++this.firingState;

                    if (this.firingState == 1)
                    {
                        this.attackTime = 60;
                        this.blizz.setInAttackMode(true);
                    }
                    else if (this.firingState <= 4)
                    {
                        this.attackTime = 6;
                    }
                    else
                    {
                        this.attackTime = 80;
                        this.firingState = 0;
                        this.blizz.setInAttackMode(false);
                    }

                    if (this.firingState > 1)
                    {
                        this.blizz.playSound(SOUND_ATTACK, 2.0F, (this.blizz.rand.nextFloat() - this.blizz.rand.nextFloat()) * 0.2F + 1.0F);
                        for (int i = 0; i < 1; ++i)
                        {
                        	EntityBlizzBolt bolt = new EntityBlizzBolt(this.blizz.worldObj, this.blizz);
                            //bolt.posY = this.blizz.posY + (double)(this.blizz.height / 2.0F) + 0.5D;
                            bolt.setHeadingFromThrower(this.blizz, this.blizz.rotationPitch, this.blizz.rotationYaw, 0.0F, 0.95F, 0.0F);
                            this.blizz.worldObj.spawnEntityInWorld(bolt);
                        }
                    }
                }
                this.blizz.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
            }
            else
            {
                this.blizz.getNavigator().clearPathEntity();
                this.blizz.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
            }
            super.updateTask();
        }
    }
	
}
