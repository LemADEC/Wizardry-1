package com.teamwizardry.wizardry.api.spell;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Created by LordSaad.
 */
public class SpellStack {

    @NotNull
    public Deque<ItemStack> children = new ArrayDeque<>();
    public int height, width, depth, maxWidth = 30, maxHeight = 30, maxDepth = 30;

    public Module head;

    public Module[][][] grid = new Module[maxWidth][maxHeight][maxDepth];

    public SpellStack(List<ItemStack> inventory) {
        Module head = ModuleRegistry.INSTANCE.getModule(inventory.get(0));

        if (head != null && head.getModuleType() == ModuleType.SHAPE) {
            this.head = head;

            for (ItemStack stack : inventory) {
                Module module = ModuleRegistry.INSTANCE.getModule(stack);
                if (module == null) {
                    height++;
                    width = 0;
                    depth = 0;
                } else {
                    if (module.getModuleType() == ModuleType.MODIFIER) depth++;
                    else width++;
                    grid[height][width][depth] = module;
                    module.height = height;
                    module.width = width;
                    module.depth = depth;
                    module.children = children = new ArrayDeque<>(inventory);
                }
            }
        }

        height = 0;
        width = 0;
        depth = 0;
    }
}