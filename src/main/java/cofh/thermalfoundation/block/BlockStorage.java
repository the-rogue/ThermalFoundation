package cofh.thermalfoundation.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import cofh.api.core.IInitializer;
import cofh.lib.util.helpers.ItemHelper;
import cofh.thermalfoundation.ThermalFoundation;

public class BlockStorage extends Block implements IInitializer {
	public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.<EnumType>create("variant", EnumType.class);

	public BlockStorage() {

		super(Material.IRON);
		setHardness(5.0F);
		setResistance(10.0F);
		setSoundType(SoundType.METAL);
		setCreativeTab(ThermalFoundation.tabCommon);
		setUnlocalizedName("thermalfoundation:block");
		setRegistryName("block");
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.Copper));

		setHarvestLevel("pickaxe", 2);
		setHarvestLevel("pickaxe", 1, getStateFromType(EnumType.Copper));
		setHarvestLevel("pickaxe", 1, getStateFromType(EnumType.Tin));
		setHarvestLevel("pickaxe", 3, getStateFromType(EnumType.Mithril));
		setHarvestLevel("pickaxe", 3, getStateFromType(EnumType.Enderium));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {VARIANT});
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
	    return getDefaultState().withProperty(VARIANT, EnumType.getTypeFromMeta(meta));
	}
	
	public IBlockState getStateFromType(EnumType type) {
	    return getDefaultState().withProperty(VARIANT, type);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
	    return state.getValue(VARIANT).getMeta();
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {

		for (int i = 0; i < EnumType.values().length; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public int getLightValue(IBlockState state) {

		return state.getValue(VARIANT).getLightvalue();
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess world, BlockPos pos, EnumFacing side) 
	{
		return blockState.getValue(VARIANT) == EnumType.Signalum ? 15 : 0;
	}

	@Override
	public float getBlockHardness(IBlockState state, World worldIn, BlockPos pos) {

		return state.getValue(VARIANT).getHardness();
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		return world.getBlockState(pos).getValue(VARIANT).getResistance();
	}

	@Override
	public int damageDropped(IBlockState state) {

		return getMetaFromState(state);
	}

	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {

		return false;
	}

	@Override
	public boolean canProvidePower(IBlockState state) {

		return true;
	}

	@Override
	public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
		return true;
	}

	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {

		return true;
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
	    return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(world.getBlockState(pos)));
	}

	/* IInitializer */
	@Override
	public boolean preInit() {

		GameRegistry.register(this);
		GameRegistry.register(new ItemBlockStorage(this).setRegistryName(this.getRegistryName()));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(this), 
				new ResourceLocation("thermalfoundation:blockcopper"), 
				new ResourceLocation("thermalfoundation:blocktin"),
				new ResourceLocation("thermalfoundation:blocksilver"),
				new ResourceLocation("thermalfoundation:blocklead"),
				new ResourceLocation("thermalfoundation:blocknickel"),
				new ResourceLocation("thermalfoundation:blockplatinum"),
				new ResourceLocation("thermalfoundation:blockmithril"),
				new ResourceLocation("thermalfoundation:blockelectrum"),
				new ResourceLocation("thermalfoundation:blockinvar"),
				new ResourceLocation("thermalfoundation:blockbronze"),
				new ResourceLocation("thermalfoundation:blocksignalum"),
				new ResourceLocation("thermalfoundation:blocklumium"),
				new ResourceLocation("thermalfoundation:blockenderium")
		);

		blockCopper = new ItemStack(this, 1, 0);
		blockTin = new ItemStack(this, 1, 1);
		blockSilver = new ItemStack(this, 1, 2);
		blockLead = new ItemStack(this, 1, 3);
		blockNickel = new ItemStack(this, 1, 4);
		blockPlatinum = new ItemStack(this, 1, 5);
		blockMithril = new ItemStack(this, 1, 6);
		blockElectrum = new ItemStack(this, 1, 7);
		blockInvar = new ItemStack(this, 1, 8);
		blockBronze = new ItemStack(this, 1, 9);
		blockSignalum = new ItemStack(this, 1, 10);
		blockLumium = new ItemStack(this, 1, 11);
		blockEnderium = new ItemStack(this, 1, 12);

		ItemHelper.registerWithHandlers("blockCopper", blockCopper);
		ItemHelper.registerWithHandlers("blockTin", blockTin);
		ItemHelper.registerWithHandlers("blockSilver", blockSilver);
		ItemHelper.registerWithHandlers("blockLead", blockLead);
		ItemHelper.registerWithHandlers("blockNickel", blockNickel);
		ItemHelper.registerWithHandlers("blockPlatinum", blockPlatinum);
		ItemHelper.registerWithHandlers("blockMithril", blockMithril);
		ItemHelper.registerWithHandlers("blockElectrum", blockElectrum);
		ItemHelper.registerWithHandlers("blockInvar", blockInvar);
		ItemHelper.registerWithHandlers("blockBronze", blockBronze);
		ItemHelper.registerWithHandlers("blockSignalum", blockSignalum);
		ItemHelper.registerWithHandlers("blockLumium", blockLumium);
		ItemHelper.registerWithHandlers("blockEnderium", blockEnderium);

		return true;
	}

	@Override
	public boolean initialize() {

		for (EnumType e : EnumType.values()) {
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(this), e.getMeta(), new ModelResourceLocation("thermalfoundation:block" + e.getName(), "inventory"));
		}
		return true;
	}

	@Override
	public boolean postInit() {

		ItemHelper.addStorageRecipe(blockCopper, "ingotCopper");
		ItemHelper.addStorageRecipe(blockTin, "ingotTin");
		ItemHelper.addStorageRecipe(blockSilver, "ingotSilver");
		ItemHelper.addStorageRecipe(blockLead, "ingotLead");
		ItemHelper.addStorageRecipe(blockNickel, "ingotNickel");
		ItemHelper.addStorageRecipe(blockPlatinum, "ingotPlatinum");
		ItemHelper.addStorageRecipe(blockMithril, "ingotMithril");
		ItemHelper.addStorageRecipe(blockElectrum, "ingotElectrum");
		ItemHelper.addStorageRecipe(blockInvar, "ingotInvar");
		ItemHelper.addStorageRecipe(blockBronze, "ingotBronze");
		ItemHelper.addStorageRecipe(blockSignalum, "ingotSignalum");
		ItemHelper.addStorageRecipe(blockLumium, "ingotLumium");
		ItemHelper.addStorageRecipe(blockEnderium, "ingotEnderium");

		return true;
	}

	public static ItemStack blockCopper;
	public static ItemStack blockTin;
	public static ItemStack blockSilver;
	public static ItemStack blockLead;
	public static ItemStack blockNickel;
	public static ItemStack blockPlatinum;
	public static ItemStack blockMithril;
	public static ItemStack blockElectrum;
	public static ItemStack blockInvar;
	public static ItemStack blockBronze;
	public static ItemStack blockSignalum;
	public static ItemStack blockLumium;
	public static ItemStack blockEnderium;

    public static enum EnumType implements IStringSerializable {
    	Copper(0, "copper", 0, 0, 5, 6),
    	Tin(1, "tin", 0, 0, 5, 6),
    	Silver(2, "silver", 4, 0, 5, 6),
    	Lead(3, "lead", 0, 0, 4, 12),
    	Nickel(4, "nickel", 0, 0, 10, 6),
    	Platinum(5, "platinum", 4, 1, 5, 6),
    	Mithril(6, "mithril", 8, 2, 30, 120),
    	Electrum(7, "electrum", 0, 0, 4, 6),
    	Invar(8, "invar", 0, 0, 20, 12),
    	Bronze(9, "bronze", 0, 0, 5, 6),
    	Signalum(10, "signalum", 7, 1, 5, 9),
    	Lumium(11, "lumium", 15, 1, 5, 9),
    	Enderium(12, "enderium", 4, 2, 40, 120);
    	
        private static final EnumType[] META_LOOKUP = new EnumType[values().length];
        private final int meta;
        /** The EnumType's name. */
        private final String name;
        private final int lightvalue;
        private final int rarity;
        private final float hardness;
        private final float resistance;
        
        private EnumType(int meta, String name, int lightvalue, int rarity, float hardness, float resistance) 
        {
        	this.meta = meta;
        	this.name = name;
        	this.lightvalue = lightvalue;
        	this.rarity = rarity;
        	this.hardness = hardness;
        	this.resistance = resistance;
        }
		@Override
		public String getName()
		{
			return name;
		}
		@Override
		public String toString() 
		{
			return getName();
		}
		public int getMeta()
		{
			return meta;
		}
		public int getLightvalue()
		{
			return lightvalue;
		}
		public int getRarity()
		{
			return rarity;
		}
		public float getHardness()
		{
			return hardness;
		}
		public float getResistance()
		{
			return resistance;
		}
		public static EnumType getTypeFromMeta(int meta) 
		{
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
		}
        static
        {
            for (EnumType enumtype : values())
            {
                META_LOOKUP[enumtype.getMeta()] = enumtype;
            }
        }
    	
    }
}
