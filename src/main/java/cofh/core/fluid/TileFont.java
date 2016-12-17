package cofh.core.fluid;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.GameRegistry;
import cofh.api.tileentity.ISidedTexture;
import cofh.core.block.TileCoFHBase;

public class TileFont extends TileCoFHBase implements ISidedTexture {

	public static void initialize() {

		GameRegistry.registerTileEntity(TileFont.class, "cofh.Font");
	}

	Block baseBlock = Blocks.BEDROCK;
	ResourceLocation resourcelocation;

	Fluid fluid;
	BlockFluidBase fluidBlock;
	int amount = -1;
	int productionDelay = 100;
	long timeTracker;

	public TileFont() {

	}

	public boolean setFluid(Fluid fluid) {

		if (fluid.getBlock() != null) {
			this.fluid = fluid;
			this.fluidBlock = (BlockFluidBase) fluid.getBlock();
			return true;
		}
		return false;
	}

	public boolean setAmount(int amount) {

		if (amount > 0) {
			this.amount = amount;
			return true;
		}
		return false;
	}

	public boolean setBaseBlock(Block block) {

		if (block != null) {
			this.baseBlock = block;
			return true;
		}
		return false;
	}

	public boolean setResourceLocation(ResourceLocation resourcelocation) {

		if (resourcelocation != null) {
			this.resourcelocation = resourcelocation;
			return true;
		}
		return false;
	}

	@Override
	public String getName() {

		return "tile.cofh.font.name";
	}

	@Override
	public int getType() {

		return 0;
	}

	public void update() {

		long worldTime = worldObj.getTotalWorldTime();
		if (timeTracker >= worldTime) {
			return;
		}
		timeTracker = worldTime + productionDelay;

		int x = xCoord, y = yCoord, z = zCoord, meta = worldObj.getBlockMetadata(x, y, z);
		EnumFacing dir = EnumFacing.getOrientation(meta ^ 1);

		if (fluidBlock.canDisplace(worldObj, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ)) {
			worldObj.setBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, fluidBlock, 0, 3);
			worldObj.notifyBlockChange(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, getBlockType());

			if (amount > 0) {
				amount--;
			}
			if (amount == 0) {
				// if (worldObj.rand) TODO: depleted font state
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, meta | 8, 2);
			}
		}
	}

	/* NBT METHODS */
	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		super.readFromNBT(nbt);

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		return super.writeToNBT(nbt);

	}

	/* ISidedTexture */
	@Override
	public ResourceLocation getTexture(int side, int pass) {

		// FIXME: overlay an icon on side we eject fluids?
		return resourcelocation;
	}

}
