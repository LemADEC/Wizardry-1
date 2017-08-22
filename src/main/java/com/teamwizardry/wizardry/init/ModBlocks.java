package com.teamwizardry.wizardry.init;


import com.teamwizardry.wizardry.common.block.*;
import com.teamwizardry.wizardry.common.block.wisdomwood.*;
import com.teamwizardry.wizardry.common.fluid.BlockFluidMana;
import com.teamwizardry.wizardry.common.fluid.BlockFluidNacre;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Created by Saad on 3/24/2016.
 */
@Mod.EventBusSubscriber
public class ModBlocks {

	public static Material NACRE_MATERIAL = new MaterialNacre(MapColor.WATER);
	public static Material MANA_MATERIAL = new MaterialMana(MapColor.WATER);

	public static BlockFluidMana FLUID_MANA;
	public static BlockFluidNacre FLUID_NACRE;

	public static BlockCraftingPlate CRAFTING_PLATE;
	public static BlockMagiciansWorktable MAGICIANS_WORKTABLE;
	public static BlockManaBattery MANA_BATTERY;
	public static BlockPearlHolder PEARL_HOLDER;
	public static BlockCloud CLOUD;
	public static BlockManaMagnet MANA_MAGNET;
	public static BlockLight LIGHT;
	public static BlockJar JAR;

	public static BlockNacre NACRE;
	public static BlockNacreBrick NACRE_BRICK;

	public static BlockWisdomWoodLog WISDOM_WOOD_LOG;
	public static BlockWisdomWoodPlanks WISDOM_WOOD_PLANKS;
	public static BlockWisdomWoodSlab WISDOM_WOOD_SLAB;
	public static BlockWisdomWoodStairs WISDOM_WOOD_STAIRS;
	public static BlockWisdomWoodTrapdoor WISDOM_WOOD_TRAPDOOR;
	public static BlockWisdomWoodDoor WISDOM_WOOD_DOOR;
	public static BlockWisdomWoodFence WISDOM_WOOD_FENCE;
	public static BlockWisdomWoodFenceGate WISDOM_WOOD_FENCE_GATE;
	public static BlockWisdomWoodPigmentedPlanks WISDOM_WOOD_PIGMENTED_PLANKS;

	public static BlockTorikkiGrass TORIKKI_GRASS;

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Block> evt) {
		IForgeRegistry<Block> r = evt.getRegistry();

		r.register(FLUID_MANA = new BlockFluidMana());
		r.register(FLUID_NACRE = new BlockFluidNacre());
	}

	public static void init() {

		CRAFTING_PLATE = new BlockCraftingPlate();
		MAGICIANS_WORKTABLE = new BlockMagiciansWorktable();
		MANA_BATTERY = new BlockManaBattery();
		PEARL_HOLDER = new BlockPearlHolder();
		CLOUD = new BlockCloud();
		NACRE = new BlockNacre();
		NACRE_BRICK = new BlockNacreBrick();
		MANA_MAGNET = new BlockManaMagnet();
		LIGHT = new BlockLight();
		JAR = new BlockJar();

		WISDOM_WOOD_LOG = new BlockWisdomWoodLog();
		WISDOM_WOOD_PLANKS = new BlockWisdomWoodPlanks();
		WISDOM_WOOD_SLAB = new BlockWisdomWoodSlab();
		WISDOM_WOOD_STAIRS = new BlockWisdomWoodStairs();
		WISDOM_WOOD_TRAPDOOR = new BlockWisdomWoodTrapdoor();
		WISDOM_WOOD_DOOR = new BlockWisdomWoodDoor();
		WISDOM_WOOD_FENCE = new BlockWisdomWoodFence();
		WISDOM_WOOD_FENCE_GATE = new BlockWisdomWoodFenceGate();
		WISDOM_WOOD_PIGMENTED_PLANKS = new BlockWisdomWoodPigmentedPlanks();

		TORIKKI_GRASS = new BlockTorikkiGrass();
	}

	public static void initModel() {
		CRAFTING_PLATE.initModel();
		MANA_BATTERY.initModel();
		PEARL_HOLDER.initModel();
		JAR.initModel();
	}
}
