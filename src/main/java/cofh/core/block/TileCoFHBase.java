package cofh.core.block;

import java.util.UUID;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.fml.relauncher.Side;
import cofh.api.tileentity.ISecurable;
import cofh.api.tileentity.ISecurable.AccessMode;
import cofh.core.CoFHProps;
import cofh.core.RegistrySocial;
import cofh.core.network.PacketCoFHBase;
import cofh.core.network.PacketHandler;
import cofh.core.network.PacketTile;
import cofh.core.util.CoreUtils;
import cofh.lib.util.helpers.SecurityHelper;
import cofh.lib.util.helpers.ServerHelper;

import com.mojang.authlib.GameProfile;

public abstract class TileCoFHBase extends TileEntity {

	@Override
	public void onChunkUnload() {

		if (!tileEntityInvalid) {
			invalidate(); // this isn't called when a tile unloads. guard incase it is in the future
		}
	}

	public abstract String getName();

	public abstract int getType();

	public void blockBroken() {

	}

	public void blockDismantled() {

		blockBroken();
	}

	public void blockPlaced() {

	}

	public void markChunkDirty() {

		worldObj.getChunkFromBlockCoords(this.pos).setModified(true);
	}

	public void callNeighborStateChange() {

		worldObj.notifyNeighborsOfStateChange(this.pos, getBlockType());
	}

	public void callNeighborTileChange() {

		worldObj.updateComparatorOutputLevel(this.pos, this.getBlockType());
	}

	public void onNeighborBlockChange() {

	}

	public void onNeighborTileChange(BlockPos pos) {

	}

	public int getComparatorInput(IBlockState blockstate) {

		return 0;
	}

	public int getLightValue() {

		return 0;
	}

	public boolean canPlayerAccess(EntityPlayer player) {

		if (!(this instanceof ISecurable)) {
			return true;
		}
		AccessMode access = ((ISecurable) this).getAccess();
		String name = player.getName();
		if (access.isPublic() || (CoFHProps.enableOpSecureAccess && CoreUtils.isOp(name))) {
			return true;
		}

		GameProfile profile = ((ISecurable) this).getOwner();
		UUID ownerID = profile.getId();
		if (SecurityHelper.isDefaultUUID(ownerID)) {
			return true;
		}

		UUID otherID = SecurityHelper.getID(player);
		if (ownerID.equals(otherID)) {
			return true;
		}
		return access.isRestricted() && RegistrySocial.playerHasAccess(name, profile);
	}

	public boolean canPlayerDismantle(EntityPlayer player) {

		return true;
	}

	public boolean isUseable(EntityPlayer player) {

		return player.getDistanceSq(pos) <= 64D && worldObj.getTileEntity(pos) == this;
	}

	public boolean onWrench(EntityPlayer player, int hitSide) {

		return false;
	}

	protected final boolean timeCheck() {

		return worldObj.getTotalWorldTime() % CoFHProps.TIME_CONSTANT == 0;
	}

	protected final boolean timeCheckEighth() {

		return worldObj.getTotalWorldTime() % CoFHProps.TIME_CONSTANT_EIGHTH == 0;
	}

	/* NETWORK METHODS */
	public Packet<?> getDescriptionPacket() {

		return PacketHandler.toMCPacket(getPacket());
	}

	public PacketCoFHBase getPacket() {

		return new PacketTile(this);
	}

	public void sendDescPacket() {

		PacketHandler.sendToAllAround(getPacket(), this);
	}

	protected void updateLighting(IBlockState state) {

		int light2 = state.getLightValue(worldObj, pos), light1 = getLightValue();
		if (light1 != light2 && worldObj.checkLightFor(EnumSkyBlock.BLOCK, pos)) {
			worldObj.setBlockState(pos, state);
		}
	}

	public void sendUpdatePacket(Side side) {

		if (worldObj == null) {
			return;
		}
		if (side == Side.CLIENT && ServerHelper.isServerWorld(worldObj)) {
			PacketHandler.sendToAllAround(getPacket(), this);
		} else if (side == Side.SERVER && ServerHelper.isClientWorld(worldObj)) {
			PacketHandler.sendToServer(getPacket());
		}
	}

	/* GUI METHODS */
	public Object getGuiClient(InventoryPlayer inventory) {

		return null;
	}

	public Object getGuiServer(InventoryPlayer inventory) {

		return null;
	}

	public int getInvSlotCount() {

		return 0;
	}

	public boolean openGui(EntityPlayer player) {

		return false;
	}

	public void receiveGuiNetworkData(int i, int j) {

	}

	public void sendGuiNetworkData(Container container, IContainerListener player) {

	}

}
