package cofh.thermalfoundation.block;

import java.util.ArrayList;

import cofh.api.core.IInitializer;
import cofh.core.fluid.BlockFluidCoFHBase;
import cofh.thermalfoundation.fluid.BlockFluidAerotheum;
import cofh.thermalfoundation.fluid.BlockFluidCoal;
import cofh.thermalfoundation.fluid.BlockFluidCryotheum;
import cofh.thermalfoundation.fluid.BlockFluidEnder;
import cofh.thermalfoundation.fluid.BlockFluidGlowstone;
import cofh.thermalfoundation.fluid.BlockFluidMana;
import cofh.thermalfoundation.fluid.BlockFluidPetrotheum;
import cofh.thermalfoundation.fluid.BlockFluidPyrotheum;
import cofh.thermalfoundation.fluid.BlockFluidRedstone;
import cofh.thermalfoundation.fluid.BlockFluidSteam;

public class TFBlocks {

	private static final ArrayList<IInitializer> BLOCKS = new ArrayList<IInitializer>();
	
	public static void preInit() {

		BLOCKS.add(blockOre = new BlockOre());
		BLOCKS.add(blockStorage = new BlockStorage());

		BLOCKS.add(blockFluidRedstone = new BlockFluidRedstone());
		BLOCKS.add(blockFluidGlowstone = new BlockFluidGlowstone());
		BLOCKS.add(blockFluidEnder = new BlockFluidEnder());
		BLOCKS.add(blockFluidPyrotheum = new BlockFluidPyrotheum());
		BLOCKS.add(blockFluidCryotheum = new BlockFluidCryotheum());
		BLOCKS.add(blockFluidAerotheum = new BlockFluidAerotheum());
		BLOCKS.add(blockFluidPetrotheum = new BlockFluidPetrotheum());
		BLOCKS.add(blockFluidMana = new BlockFluidMana());
		BLOCKS.add(blockFluidSteam = new BlockFluidSteam());
		BLOCKS.add(blockFluidCoal = new BlockFluidCoal());
		
		for (IInitializer i : BLOCKS) {
			i.preInit();
		}
	}

	public static void initialize() {
		for (IInitializer i : BLOCKS) {
			i.initialize();
		}
	}

	public static void postInit() {
		for (IInitializer i : BLOCKS) {
			i.postInit();
		}
	}

	public static BlockOre blockOre;
	public static BlockStorage blockStorage;

	public static BlockFluidCoFHBase blockFluidRedstone;
	public static BlockFluidCoFHBase blockFluidGlowstone;
	public static BlockFluidCoFHBase blockFluidEnder;
	public static BlockFluidCoFHBase blockFluidPyrotheum;
	public static BlockFluidCoFHBase blockFluidCryotheum;
	public static BlockFluidCoFHBase blockFluidAerotheum;
	public static BlockFluidCoFHBase blockFluidPetrotheum;
	public static BlockFluidCoFHBase blockFluidMana;
	public static BlockFluidCoFHBase blockFluidSteam;
	public static BlockFluidCoFHBase blockFluidCoal;

}
