package de.danielclasen.minecraft.forge.extendedFarming;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import de.danielclasen.minecraft.forge.extendedFarming.utils.BlockItemMatcher;
import de.danielclasen.minecraft.forge.extendedFarming.utils.BlockItemMatcherRegistry;

@Mod(modid = "ExtendedFarming", name = "Extended Farming", version = "0.0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { "EFFarmArea","EFParticle","EFSoundEffect" }, packetHandler = PacketHandler.class)
public class ExtendedFarming {

	public final static Block seedPlanterBlock = new SeedReplanterBlock(500, 1,
			Material.piston).setHardness(0.5F)
			.setStepSound(Block.soundWoodFootstep).setBlockName("seedPlanter")
			.setCreativeTab(CreativeTabs.tabBlock);
	
	public final static Item wateringCanItem = new WateringCanItem(5000);
	

	public final static BlockItemMatcherRegistry blockItemMatcherRegistry = new BlockItemMatcherRegistry();
	
	// The instance of your mod that Forge uses.
	@Instance("ExtendedFarming")
	public static ExtendedFarming instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "de.danielclasen.minecraft.forge.extendedFarming.client.ClientProxy", serverSide = "de.danielclasen.minecraft.forge.extendedFarming.CommonProxy")
	public static CommonProxy proxy;

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		// Stub Method
	}

	@Init
	public void load(FMLInitializationEvent event) {
		
		blockItemMatcherRegistry.add(new BlockItemMatcher(59, 295, true));
		blockItemMatcherRegistry.add(new BlockItemMatcher(141, 391, true));
		blockItemMatcherRegistry.add(new BlockItemMatcher(142, 392, true));
				

		LanguageRegistry.addName(seedPlanterBlock, "Seed Replanter");
		
		LanguageRegistry.addName(wateringCanItem, "Watering Can");
		
		MinecraftForge.setBlockHarvestLevel(seedPlanterBlock, "axe", 0);

		GameRegistry.registerBlock(seedPlanterBlock, "seedPlanter");

		GameRegistry.registerTileEntity(TileEntityTiny.class,
				"SeedReplanterContainer");

		NetworkRegistry.instance().registerGuiHandler(this,
				new SeedReplanterGuiHandler());
		
		proxy.registerRenderers();
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
		// Stub Method
	}
	
	public ExtendedFarming getInstance(){
		return this;
	}

}