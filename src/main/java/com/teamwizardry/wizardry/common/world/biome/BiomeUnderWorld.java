package com.teamwizardry.wizardry.common.world.biome;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import com.teamwizardry.wizardry.Wizardry;
import com.teamwizardry.wizardry.api.util.RandUtil;
import com.teamwizardry.wizardry.common.entity.EntityFairy;
import com.teamwizardry.wizardry.common.entity.EntitySpiritWight;
import com.teamwizardry.wizardry.common.entity.EntityUnicorn;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Created by Demoniaque44
 */
public class BiomeUnderWorld extends Biome {

	public BiomeUnderWorld(BiomeProperties properties) {
		super(properties);
		properties.setRainDisabled();

		this.topBlock = Blocks.AIR.getDefaultState();
		this.fillerBlock = Blocks.AIR.getDefaultState();
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		spawnableMonsterList.clear();
		spawnableCaveCreatureList.clear();
		modSpawnableLists.clear();
	}

	@Override
	public void decorate(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos) {

	}

	@SubscribeEvent
	public void onTickPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.player.world.isRemote) return;
		if (event.player.world.provider.getDimensionType() != Wizardry.underWorld) return;
		if (!event.player.world.getGameRules().getBoolean("doMobSpawning")) return;

		if (RandUtil.nextInt(300) == 0 && getEntityCount(EntityFairy.class, event.player.getPosition(), event.player.world, 64) < 15) {
			BlockPos pos = new BlockPos(event.player.posX + RandUtil.nextInt(-64, 64), RandUtil.nextInt(110, 150), event.player.posZ + RandUtil.nextInt(-64, 64));
			if (event.player.world.getBiome(pos) == this && event.player.world.isAirBlock(pos)) {
				EntityFairy entity = new EntityFairy(event.player.world);
				entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
				event.player.world.spawnEntity(entity);
			}
		}

		if (RandUtil.nextInt(500) == 0 && getEntityCount(EntitySpiritWight.class, event.player.getPosition(), event.player.world, 128) < 1) {
			BlockPos pos = new BlockPos(event.player.posX + RandUtil.nextInt(-128, 128), event.player.posY + RandUtil.nextInt(-10, 10), event.player.posZ + RandUtil.nextInt(-128, 128));
			if (event.player.world.getBiome(pos) == this && pos.getDistance((int) event.player.posX, (int) event.player.posY, (int) event.player.posZ) > 64) {
				EntitySpiritWight entity = new EntitySpiritWight(event.player.world);
				entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
				entity.enablePersistence();
				event.player.world.spawnEntity(entity);
			}
		}

		if (RandUtil.nextInt(500) == 0 && getEntityCount(EntityUnicorn.class, event.player.getPosition(), event.player.world, 64) < 3) {
			
			MutableBlockPos pos = new BlockPos.MutableBlockPos(event.player.getPosition());
			
			double distance;
			int tries = 0;
			do
			{
				Vec3d offset = new Vec3d(RandUtil.nextDouble(30, 64), 0, 0).rotateYaw(RandUtil.nextFloat((float) Math.PI * 2));
				pos.add(offset.x, offset.y, offset.z);
				pos.setY(event.player.world.getChunk(pos).getHeight(pos) + 1);
				distance = pos.getDistance((int) event.player.posX, (int) event.player.posY, (int) event.player.posZ);
			}
			while ((distance < 30 || distance > 64) && tries++ < 300);
			
			if (tries <= 300)
			{
				EntityUnicorn entity = new EntityUnicorn(event.player.world);
				entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
				event.player.world.spawnEntity(entity);
			}
		}
	}

	private int getEntityCount(Class<? extends Entity> entity, BlockPos pos, World world, int range) {
		List<Entity> entities = world.getEntitiesWithinAABB(entity, new AxisAlignedBB(pos).grow(range));
		return entities.size();
	}
}
