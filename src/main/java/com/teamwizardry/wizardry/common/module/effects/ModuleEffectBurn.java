package com.teamwizardry.wizardry.common.module.effects;

import com.teamwizardry.wizardry.api.spell.SpellData;
import com.teamwizardry.wizardry.api.spell.SpellRing;
import com.teamwizardry.wizardry.api.spell.attribute.Attributes;
import com.teamwizardry.wizardry.api.spell.module.ModuleEffect;
import com.teamwizardry.wizardry.api.spell.module.ModuleModifier;
import com.teamwizardry.wizardry.api.spell.module.RegisterModule;
import com.teamwizardry.wizardry.api.util.BlockUtils;
import com.teamwizardry.wizardry.api.util.RandUtil;
import com.teamwizardry.wizardry.client.fx.LibParticles;
import com.teamwizardry.wizardry.common.module.modifiers.ModuleModifierExtendTime;
import com.teamwizardry.wizardry.common.module.modifiers.ModuleModifierIncreaseAOE;
import com.teamwizardry.wizardry.init.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;

import static com.teamwizardry.wizardry.api.spell.SpellData.DefaultKeys.FACE_HIT;

/**
 * Created by Demoniaque.
 */
@RegisterModule
public class ModuleEffectBurn extends ModuleEffect {

	@Nonnull
	@Override
	public String getID() {
		return "effect_burn";
	}

	@Override
	public ModuleModifier[] applicableModifiers() {
		return new ModuleModifier[]{new ModuleModifierIncreaseAOE(), new ModuleModifierExtendTime()};
	}

	@Override
	public boolean run(@Nonnull SpellData spell, @Nonnull SpellRing spellRing) {
		World world = spell.world;
		Entity targetEntity = spell.getVictim();
		BlockPos targetPos = spell.getTargetPos();
		Entity caster = spell.getCaster();
		EnumFacing facing = spell.getData(FACE_HIT);

		double strength = spellRing.getModifier(Attributes.AREA, 2, 16) / 2.0;
		double time = spellRing.getModifier(Attributes.DURATION, 100, 1000);

		if (!tax(this, spell, spellRing)) return false;

		if (targetEntity != null) {
			targetEntity.setFire((int) time);
			world.playSound(null, targetEntity.getPosition(), ModSounds.FIRE, SoundCategory.NEUTRAL, RandUtil.nextFloat(0.35f, 0.75f), RandUtil.nextFloat(0.35f, 1.5f));
		}

		if (targetPos != null) {
			for (int x = (int) strength; x >= -strength; x--)
				for (int y = (int) strength; y >= -strength; y--)
					for (int z = (int) strength; z >= -strength; z--) {
						BlockPos pos = targetPos.add(x, y, z);
						double dist = pos.getDistance(targetPos.getX(), targetPos.getY(), targetPos.getZ());
						if (dist > strength) continue;
						if (facing != null) {
							if (!world.isAirBlock(pos.offset(facing))) continue;
							BlockUtils.placeBlock(world, pos.offset(facing), Blocks.FIRE.getDefaultState(), caster instanceof EntityPlayer ? (EntityPlayerMP) caster : null);
							world.playSound(null, targetPos, ModSounds.FIRE, SoundCategory.NEUTRAL, 0.5f, RandUtil.nextFloat());
						} else for (EnumFacing face : EnumFacing.VALUES) {
							if (world.isAirBlock(pos.offset(face)) || world.getBlockState(pos.offset(face)).getBlock() == Blocks.SNOW_LAYER) {
								BlockUtils.placeBlock(world, pos.offset(face), Blocks.AIR.getDefaultState(), caster instanceof EntityPlayer ? (EntityPlayerMP) caster : null);
								world.playSound(null, targetPos, ModSounds.FIRE, SoundCategory.NEUTRAL, 0.5f, RandUtil.nextFloat());
							}
						}
					}
		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void render(@Nonnull SpellData spell, @NotNull SpellRing spellRing) {
		World world = spell.world;
		Vec3d position = spell.getTarget();

		if (position == null) return;

		Color color = getPrimaryColor();
		if (RandUtil.nextBoolean()) color = getSecondaryColor();

		LibParticles.EFFECT_BURN(world, position, color);
	}
}
