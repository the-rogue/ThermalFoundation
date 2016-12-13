package cofh.lib.util.helpers;

import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * This class contains various helper functions related to Entities.
 *
 * @author King Lemming
 *
 */
public class EntityHelper {

	private EntityHelper() {

	}

	public static int getEntityFacingCardinal(EntityLivingBase living) {

		int quadrant = cofh.lib.util.helpers.MathHelper.floor(living.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

		switch (quadrant) {
		case 0:
			return 2;
		case 1:
			return 5;
		case 2:
			return 3;
		default:
			return 4;
		}
	}

	public static EnumFacing getEntityFacingEnumFacing(EntityLivingBase living) {

		return EnumFacing.VALUES[getEntityFacingCardinal(living)];
	}

	public static void transferEntityToDimension(Entity entity, int dimension, PlayerList playerlist) {

		if (entity instanceof EntityPlayerMP) {
			transferPlayerToDimension((EntityPlayerMP) entity, dimension, playerlist);
			return;
		}
		WorldServer worldserver = playerlist.getServerInstance().worldServerForDimension(entity.dimension);
		entity.dimension = dimension;
		WorldServer worldserver1 = playerlist.getServerInstance().worldServerForDimension(entity.dimension);
		worldserver.removeEntityDangerously(entity);
		if (!entity.getPassengers().isEmpty()) {
			for (Entity e : entity.getPassengers()) {
				e.dismountRidingEntity();
			}
		}
		if (entity.getRidingEntity() != null) {
			entity.dismountRidingEntity();
		}
		entity.isDead = false;
		transferEntityToWorld(entity, worldserver, worldserver1);
	}

	public static void transferEntityToWorld(Entity entity, WorldServer oldWorld, WorldServer newWorld) {

		WorldProvider pOld = oldWorld.provider;
		WorldProvider pNew = newWorld.provider;
		double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
		double x = entity.posX * moveFactor;
		double z = entity.posZ * moveFactor;

		oldWorld.theProfiler.startSection("placing");
		x = MathHelper.clamp(x, -29999872, 29999872);
		z = MathHelper.clamp(z, -29999872, 29999872);

		if (entity.isEntityAlive()) {
			entity.setLocationAndAngles(x, entity.posY, z, entity.rotationYaw, entity.rotationPitch);
			entity.forceSpawn = true;
			newWorld.spawnEntityInWorld(entity);
			entity.forceSpawn = false;
			newWorld.updateEntityWithOptionalForce(entity, false);
		}

		oldWorld.theProfiler.endSection();

		entity.setWorld(newWorld);
	}

	public static void transferPlayerToDimension(EntityPlayerMP player, int dimension, PlayerList playerlist) {

		int oldDim = player.dimension;
		WorldServer worldserver = playerlist.getServerInstance().worldServerForDimension(player.dimension);
		player.dimension = dimension;
		WorldServer worldserver1 = playerlist.getServerInstance().worldServerForDimension(player.dimension);
		player.connection.sendPacket(new SPacketRespawn(player.dimension, player.worldObj.getDifficulty(), player.worldObj.getWorldInfo()
				.getTerrainType(), player.interactionManager.getGameType()));
		worldserver.removeEntityDangerously(player);
		if (!player.getPassengers().isEmpty()) {
			for (Entity e : player.getPassengers()) {
				e.dismountRidingEntity();
			}
		}
		if (player.getRidingEntity() != null) {
			player.dismountRidingEntity();
		}
		player.isDead = false;
		transferEntityToWorld(player, worldserver, worldserver1);
		playerlist.preparePlayer(player, worldserver);
		player.connection.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
		player.interactionManager.setWorld(worldserver1);
		playerlist.updateTimeAndWeatherForPlayer(player, worldserver1);
		playerlist.syncPlayerInventory(player);
		Iterator<PotionEffect> iterator = player.getActivePotionEffects().iterator();

		while (iterator.hasNext()) {
			PotionEffect potioneffect = iterator.next();
			player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), potioneffect));
		}
		FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldDim, dimension);
	}

	public static void transferEntityToDimension(Entity entity, double x, double y, double z, int dimension, PlayerList playerlist) {

		if (entity instanceof EntityPlayerMP) {
			transferPlayerToDimension((EntityPlayerMP) entity, x, y, z, dimension, playerlist);
			return;
		}
		if (!entity.getPassengers().isEmpty()) {
			for (Entity e : entity.getPassengers()) {
				e.dismountRidingEntity();
			}
		}
		if (entity.getRidingEntity() != null) {
			entity.dismountRidingEntity();
		}
		WorldServer worldserver = playerlist.getServerInstance().worldServerForDimension(entity.dimension);
		entity.dimension = dimension;
		WorldServer worldserver1 = playerlist.getServerInstance().worldServerForDimension(entity.dimension);
		worldserver.removeEntityDangerously(entity);
		entity.isDead = false;
		transferEntityToWorld(entity, x, y, z, worldserver, worldserver1);
	}

	public static void transferEntityToWorld(Entity entity, double x, double y, double z, WorldServer oldWorld, WorldServer newWorld) {

		oldWorld.theProfiler.startSection("placing");
		x = MathHelper.clamp(x, -29999872, 29999872);
		z = MathHelper.clamp(z, -29999872, 29999872);

		if (entity.isEntityAlive()) {
			entity.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch);
			entity.forceSpawn = true;
			newWorld.spawnEntityInWorld(entity);
			entity.forceSpawn = false;
			newWorld.updateEntityWithOptionalForce(entity, false);
		}

		oldWorld.theProfiler.endSection();

		entity.setWorld(newWorld);
	}

	public static void transferPlayerToDimension(EntityPlayerMP player, double x, double y, double z, int dimension, PlayerList playerlist) {

		int oldDim = player.dimension;
		WorldServer worldserver = playerlist.getServerInstance().worldServerForDimension(player.dimension);
		player.dimension = dimension;
		WorldServer worldserver1 = playerlist.getServerInstance().worldServerForDimension(player.dimension);
		player.connection.sendPacket(new SPacketRespawn(player.dimension, player.worldObj.getDifficulty(), player.worldObj.getWorldInfo()
				.getTerrainType(), player.interactionManager.getGameType()));
		worldserver.removeEntityDangerously(player);
		if (!player.getPassengers().isEmpty()) {
			for (Entity e : player.getPassengers()) {
				e.dismountRidingEntity();
			}
		}
		if (player.getRidingEntity() != null) {
			player.dismountRidingEntity();
		}
		player.isDead = false;
		transferEntityToWorld(player, x, y, z, worldserver, worldserver1);
		playerlist.preparePlayer(player, worldserver);
		player.connection.setPlayerLocation(x, y, z, player.rotationYaw, player.rotationPitch);
		player.interactionManager.setWorld(worldserver1);
		playerlist.updateTimeAndWeatherForPlayer(player, worldserver1);
		playerlist.syncPlayerInventory(player);
		Iterator<PotionEffect> iterator = player.getActivePotionEffects().iterator();

		while (iterator.hasNext()) {
			PotionEffect potioneffect = iterator.next();
			player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), potioneffect));
		}
		FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldDim, dimension);
	}

}
