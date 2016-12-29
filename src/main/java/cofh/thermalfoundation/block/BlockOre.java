package cofh.thermalfoundation.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import cofh.api.core.IInitializer;
import cofh.lib.util.helpers.ItemHelper;
import cofh.thermalfoundation.ThermalFoundation;
import cofh.thermalfoundation.item.TFItems;

public class BlockOre extends Block implements IInitializer {
	public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.<EnumType>create("variant", EnumType.class);

	public BlockOre() {

		super(Material.ROCK);
		setHardness(3.0F);
		setResistance(5.0F);
		setSoundType(SoundType.STONE);
		setCreativeTab(ThermalFoundation.tabCommon);
		setUnlocalizedName("thermalfoundation:ore");
		setRegistryName("ore");
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.Copper));

		setHarvestLevel("pickaxe", 2);
		setHarvestLevel("pickaxe", 1, getStateFromType(EnumType.Copper));
		setHarvestLevel("pickaxe", 1, getStateFromType(EnumType.Tin));
		setHarvestLevel("pickaxe", 3, getStateFromType(EnumType.Mithril));
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
	public int damageDropped(IBlockState state) {

		return getMetaFromState(state);
	}
	
	@Override
    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
    	return Item.getItemFromBlock(state.getBlock());
    }
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
	    return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(world.getBlockState(pos)));
	}


	/* IInitializer */
	@Override
	public boolean preInit() {

		GameRegistry.register(this);
		GameRegistry.register(new ItemBlockOre(this).setRegistryName(this.getRegistryName()));
		
		ModelBakery.registerItemVariants(Item.getItemFromBlock(this), 
				new ResourceLocation("thermalfoundation:orecopper"), 
				new ResourceLocation("thermalfoundation:oretin"),
				new ResourceLocation("thermalfoundation:oresilver"),
				new ResourceLocation("thermalfoundation:orelead"),
				new ResourceLocation("thermalfoundation:orenickel"),
				new ResourceLocation("thermalfoundation:oreplatinum"),
				new ResourceLocation("thermalfoundation:oremithril")
		);
		

		oreCopper = new ItemStack(this, 1, 0);
		oreTin = new ItemStack(this, 1, 1);
		oreSilver = new ItemStack(this, 1, 2);
		oreLead = new ItemStack(this, 1, 3);
		oreNickel = new ItemStack(this, 1, 4);
		orePlatinum = new ItemStack(this, 1, 5);
		oreMithril = new ItemStack(this, 1, 6);

		ItemHelper.registerWithHandlers("oreCopper", oreCopper);
		ItemHelper.registerWithHandlers("oreTin", oreTin);
		ItemHelper.registerWithHandlers("oreSilver", oreSilver);
		ItemHelper.registerWithHandlers("oreLead", oreLead);
		ItemHelper.registerWithHandlers("oreNickel", oreNickel);
		ItemHelper.registerWithHandlers("orePlatinum", orePlatinum);
		ItemHelper.registerWithHandlers("oreMithril", oreMithril);

		return true;
	}

	@Override
	public boolean initialize() {

		for (EnumType e : EnumType.values()) {
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(this), e.getMeta(), new ModelResourceLocation("thermalfoundation:ore" + e.getName(), "inventory"));
		}
		return true;
	}

	@Override
	public boolean postInit() {

		ItemHelper.addSmelting(TFItems.ingotCopper, oreCopper, 0.6F);
		ItemHelper.addSmelting(TFItems.ingotTin, oreTin, 0.7F);
		ItemHelper.addSmelting(TFItems.ingotSilver, oreSilver, 0.9F);
		ItemHelper.addSmelting(TFItems.ingotLead, oreLead, 0.8F);
		ItemHelper.addSmelting(TFItems.ingotNickel, oreNickel, 1.0F);
		ItemHelper.addSmelting(TFItems.ingotPlatinum, orePlatinum, 1.0F);
		ItemHelper.addSmelting(TFItems.ingotMithril, oreMithril, 1.5F);

		return true;
	}

	public static ItemStack oreCopper;
	public static ItemStack oreTin;
	public static ItemStack oreSilver;
	public static ItemStack oreLead;
	public static ItemStack oreNickel;
	public static ItemStack orePlatinum;
	public static ItemStack oreMithril;

    public static enum EnumType implements IStringSerializable {
    	Copper(0, "copper", 0, 0),
    	Tin(1, "tin", 0, 0),
    	Silver(2, "silver", 4, 0),
    	Lead(3, "lead", 0, 0),
    	Nickel(4, "nickel", 0, 0),
    	Platinum(5, "platinum", 4, 1),
    	Mithril(6, "mithril", 8, 2);
    	
        private static final EnumType[] META_LOOKUP = new EnumType[values().length];
        private final int meta;
        /** The EnumType's name. */
        private final String name;
        private final int lightvalue;
        private final int rarity;
        
        private EnumType(int meta, String name, int lightvalue, int rarity) 
        {
        	this.meta = meta;
        	this.name = name;
        	this.lightvalue = lightvalue;
        	this.rarity = rarity;
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
