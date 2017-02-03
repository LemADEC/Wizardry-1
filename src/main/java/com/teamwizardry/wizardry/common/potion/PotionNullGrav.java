package com.teamwizardry.wizardry.common.potion;

import com.teamwizardry.librarianlib.common.base.PotionMod;
import com.teamwizardry.wizardry.init.ModPotions;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by LordSaad.
 */
public class PotionNullGrav extends PotionMod {

	public PotionNullGrav() {
		super("nullify_gravity", false, 0xFFFFFF);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

	@SubscribeEvent
	public void onLivingTick(LivingUpdateEvent event) {
		PotionEffect effect = event.getEntityLiving().getActivePotionEffect(ModPotions.NULLIFY_GRAVITY);
		if (effect == null) return;

		event.getEntityLiving().setNoGravity(effect.getDuration() > 2);
	}
}
