package com.teamwizardry.wizardry.client.gui.worktable;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.teamwizardry.librarianlib.features.gui.GuiBase;
import com.teamwizardry.librarianlib.features.gui.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.components.*;
import com.teamwizardry.librarianlib.features.gui.mixin.gl.GlMixin;
import com.teamwizardry.librarianlib.features.math.Vec2d;
import com.teamwizardry.librarianlib.features.network.PacketHandler;
import com.teamwizardry.librarianlib.features.sprite.Sprite;
import com.teamwizardry.librarianlib.features.sprite.Texture;
import com.teamwizardry.wizardry.Wizardry;
import com.teamwizardry.wizardry.api.spell.Module;
import com.teamwizardry.wizardry.api.spell.ModuleRegistry;
import com.teamwizardry.wizardry.api.spell.ModuleType;
import com.teamwizardry.wizardry.api.spell.SpellRecipeConstructor;
import com.teamwizardry.wizardry.common.network.PacketSendSpellToBook;
import com.teamwizardry.wizardry.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

/**
 * Created by Saad on 6/17/2016.
 */
public class WorktableGui extends GuiBase {
	private static final Texture BACKGROUND_TEXTURE = new Texture(new ResourceLocation(Wizardry.MODID, "textures/gui/worktable/table_background.png"));
	private static final Sprite BACKGROUND_SPRITE = BACKGROUND_TEXTURE.getSprite("bg", 480, 224);
	private static final Sprite SCROLL_BAR = new Sprite(new ResourceLocation(Wizardry.MODID, "textures/gui/worktable/scroll_bar.png"));
	private static final Sprite SCROLL_BAR_BAR = new Sprite(new ResourceLocation(Wizardry.MODID, "textures/gui/worktable/scroll_bar_bar.png"));
	private static final Sprite SIDE_BAR = new Sprite(new ResourceLocation(Wizardry.MODID, "textures/gui/worktable/sidebar.png"));
	ComponentVoid paper;
	BiMap<GuiComponent, UUID> paperComponents = HashBiMap.create();
	HashMap<UUID, UUID> componentLinks = new HashMap<>();
	private HashSet<Module> compiledSpell = new HashSet<>();

	public WorktableGui() {
		super(480, 224);

		ComponentRect rect = new ComponentRect(0, 0, 40000, 40000);
		rect.getColor().setValue(new Color(0xB3000000, true));
		GlMixin.INSTANCE.transform(rect).setValue(new Vec3d(0, 0, -30));
		getFullscreenComponents().add(rect);

		ComponentSprite background = new ComponentSprite(BACKGROUND_SPRITE, 0, 0);
		GlMixin.INSTANCE.transform(background).setValue(new Vec3d(0, 0, 10));
		getMainComponents().add(background);

		paper = new ComponentVoid(180, 19, 180, 188);
		getMainComponents().add(paper);

		ComponentSprite shapes = new ComponentSprite(SIDE_BAR, 29, 31, 48, 80);
		GlMixin.INSTANCE.transform(shapes).setValue(new Vec3d(0, 0, -10));
		ComponentGrid scrollShapes = addModules(shapes, ModuleType.SHAPE);
		addScrollbar(shapes, scrollShapes, 77, 31);
		getMainComponents().add(shapes);

		ComponentSprite effects = new ComponentSprite(SIDE_BAR, 93, 31, 48, 80);
		GlMixin.INSTANCE.transform(effects).setValue(new Vec3d(0, 0, -10));
		ComponentGrid scrollEffects = addModules(effects, ModuleType.EFFECT);
		addScrollbar(effects, scrollEffects, 141, 31);
		getMainComponents().add(effects);

		ComponentSprite events = new ComponentSprite(SIDE_BAR, 29, 123, 48, 80);
		GlMixin.INSTANCE.transform(effects).setValue(new Vec3d(0, 0, -10));
		ComponentGrid scrollEvents = addModules(events, ModuleType.EVENT);
		addScrollbar(events, scrollEvents, 77, 123);
		getMainComponents().add(events);

		ComponentSprite modifiers = new ComponentSprite(SIDE_BAR, 93, 123, 48, 80);
		GlMixin.INSTANCE.transform(effects).setValue(new Vec3d(0, 0, -10));
		ComponentGrid scrollModifiers = addModules(modifiers, ModuleType.MODIFIER);
		addScrollbar(modifiers, scrollModifiers, 141, 123);
		getMainComponents().add(modifiers);

		ComponentSprite save = new ComponentSprite(new Sprite(new ResourceLocation(Wizardry.MODID, "textures/gui/worktable/button.png")), 395, 30, (int) (88 / 1.5), (int) (24 / 1.5));
		int width = Minecraft.getMinecraft().fontRenderer.getStringWidth("SAVE");
		int height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
		ComponentText textSave = new ComponentText(395 + (save.getSize().getXi() / 2) - width / 2, 30 + (save.getSize().getYi() / 2) - height / 2);
		textSave.getText().setValue("SAVE");
		save.BUS.hook(GuiComponent.MouseClickEvent.class, (event) -> {

			HashSet<GuiComponent> heads = getHeads();
			compiledSpell.clear();
			for (GuiComponent component : heads) {
				Module module = compileModule(component);
				if (module == null) continue;
				compiledSpell.add(module);
			}

			SpellRecipeConstructor recipe = new SpellRecipeConstructor(compiledSpell);

			for (ItemStack stack : Minecraft.getMinecraft().player.inventory.mainInventory) {
				if (stack.getItem() == ModItems.BOOK) {
					int slot = Minecraft.getMinecraft().player.inventory.getSlotFor(stack);
					PacketHandler.NETWORK.sendToServer(new PacketSendSpellToBook(slot, recipe.getRecipeJson().toString()));
				}
			}
		});
		getMainComponents().add(save);
		getMainComponents().add(textSave);
	}

	public UUID getUUID(GuiComponent component) {
		return paperComponents.get(component);
	}

	public GuiComponent getComponent(UUID uuid) {
		return paperComponents.inverse().get(uuid);
	}

	@Nullable
	private Module compileModule(@Nullable GuiComponent component) {
		if (component == null) return null;

		Module module = getModule(component);
		if (module == null) return null;

		if (!componentLinks.containsKey(getUUID(component))) return module;
		UUID uuidChild = componentLinks.get(getUUID(component));

		GuiComponent childComp = getComponent(uuidChild);
		if (childComp == null) return module;

		Module child = getModule(childComp);
		if (child == null) return module;

		module.nextModule = compileModule(childComp);

		return module;
	}

	private HashSet<GuiComponent> getHeads() {
		HashSet<GuiComponent> heads = new HashSet<>();
		for (GuiComponent component : paperComponents.keySet()) {
			if (componentLinks.containsValue(getUUID(component))) continue;
			heads.add(component);
		}

		return heads;
	}

	public int getLinksTo(GuiComponent comp) {
		UUID main = paperComponents.get(comp);
		int count = 0;
		for (UUID uuid : componentLinks.values()) {
			if (main.equals(uuid)) {
				count++;
			}
		}
		return count;
	}

	private ComponentGrid addModules(ComponentSprite parent, ModuleType type) {
		ComponentGrid grid = new ComponentGrid(0, 0, 16, 16, 3);
		parent.add(grid);

		for (Module module : ModuleRegistry.INSTANCE.getModules(type)) {
			TableModule item = new TableModule(this, parent, module.copy(), false);
			grid.add(item.component);
		}
		return grid;
	}

	private void addScrollbar(ComponentSprite parent, ComponentGrid gridView, int x, int y) {
		ComponentSprite scrollBar = new ComponentSprite(SCROLL_BAR_BAR, x, y, 5, 80);
		ComponentSprite bar = new ComponentSprite(SCROLL_BAR, 1, 0, 3, 11);
		scrollBar.BUS.hook(GuiComponent.MouseDragEvent.class, (event) -> {
			if (!event.getComponent().getMouseOver() && !parent.getMouseOver()) return;
			Vec2d mouse = event.getComponent().getParent().unTransformChildPos(event.getComponent(), event.getMousePos());
			double clamp = MathHelper.clamp(mouse.getY(), y + 5.5, y + 79 - 5.5);
			double extra = (gridView.getChildren().size() % 3) * 16;
			if (gridView.getChildren().size() <= 15) return;

			bar.setPos(new Vec2d(bar.getPos().getX(), (clamp - y - 5.5)));
			double sub = bar.getPos().getY() - y;
			double percent = sub / 79.0;
			gridView.setPos(new Vec2d(0, (extra * (1 - percent) - extra) - 1 - 5.5));
		});
		scrollBar.BUS.hook(GuiComponent.MouseWheelEvent.class, (event) -> {
			if (!event.getComponent().getMouseOver() && !parent.getMouseOver()) return;
			int dir = event.getDirection().ydirection * -16;
			double barPos = bar.getPos().getY();
			double clamp = MathHelper.clamp(barPos + dir, 0, 68);
			double extra = (gridView.getChildren().size() % 3) * 16;
			if (gridView.getChildren().size() <= 15) return;
			bar.setPos(new Vec2d(bar.getPos().getX(), clamp));
			double sub = bar.getPos().getY() - y;
			double percent = sub / 68.0;
			gridView.setPos(new Vec2d(0, (extra * (1 - percent) - extra) - 1 - 5.5));
		});
		scrollBar.add(bar);
		getMainComponents().add(scrollBar);
	}

	@Nullable
	public Module getModule(@Nullable GuiComponent component) {
		if (component == null) return null;
		for (Object object : component.getTags()) {
			if (object instanceof Module) return ((Module) object).copy();
		}
		return null;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
