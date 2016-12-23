package cofh.core.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public abstract class EntityCoFHArrow extends EntityArrow implements IEntityAdditionalSpawnData {

	public EntityCoFHArrow(World world) {

		super(world);
	}

	public EntityCoFHArrow(World world, double x, double y, double z) {

		super(world, x, y, z);
	}

	public EntityCoFHArrow(World world, EntityLivingBase shooter, EntityLivingBase target, float speed, float variance) {

		super(world, shooter);
	}

	@Override
	public void onUpdate() {

		if (worldObj.isRemote) {
			return;
		}
		super.onUpdate();
	}

	/* IEntityAdditionalSpawnData */
	@Override
	public void writeSpawnData(ByteBuf buffer) {

		buffer.writeFloat(prevRotationPitch = rotationPitch);
		buffer.writeFloat(prevRotationYaw = rotationYaw);
	}

	@Override
	public void readSpawnData(ByteBuf buffer) {

		prevRotationPitch = rotationPitch = buffer.readFloat();
		prevRotationYaw = rotationYaw = buffer.readFloat();
	}

	@Override
	protected ItemStack getArrowStack()
	{
		// TODO Auto-generated method stub
		return null;
	}
	public abstract ResourceLocation getEntityTexture();

}
