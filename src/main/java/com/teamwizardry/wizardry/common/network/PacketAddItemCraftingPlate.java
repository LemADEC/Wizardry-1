package com.teamwizardry.wizardry.common.network;

import com.teamwizardry.librarianlib.core.LibrarianLib;
import com.teamwizardry.librarianlib.features.autoregister.PacketRegister;
import com.teamwizardry.librarianlib.features.network.PacketBase;
import com.teamwizardry.librarianlib.features.saving.Save;
import com.teamwizardry.wizardry.common.tile.TileCraftingPlate;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

@PacketRegister(Side.CLIENT)
public class PacketAddItemCraftingPlate extends PacketBase {

	@Save
	private BlockPos pos;
	@Save
	private ItemStack stack;

	public PacketAddItemCraftingPlate() {
	}

	public PacketAddItemCraftingPlate(BlockPos pos, ItemStack stack) {

		this.pos = pos;
		this.stack = stack;
	}

	@Override
	public void handle(@NotNull MessageContext ctx) {
		if (ctx.side.isServer()) return;

		World world = LibrarianLib.PROXY.getClientPlayer().world;
		if (world == null) return;
		if (!world.isBlockLoaded(pos)) return;

		TileEntity entity = world.getTileEntity(pos);
		if (entity instanceof TileCraftingPlate) {
			TileCraftingPlate plate = (TileCraftingPlate) entity;

			ItemHandlerHelper.insertItem(plate.realInventory.getHandler(), stack, false);
		}
	}
}
