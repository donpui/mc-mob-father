package com.minecraft.fathermob;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class FatherSpawnController {
    private static final Logger LOGGER = LoggerFactory.getLogger("FatherMob");
    private static final int SPAWN_START_TIME = 6000;
    private static final int SPAWN_END_TIME = 23000;
    private static final int SPAWN_ATTEMPT_INTERVAL = 12000;
    private static final double SPAWN_RADIUS = 12.0D;
    private static int tickCounter = 0;
    private static final int THIRTY_MINUTES_IN_TICKS = 36000; // 30 minutes in ticks
    private static boolean spawnTriggered = false;

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.level instanceof ServerLevel)) {
            return;
        }

        ServerLevel serverLevel = (ServerLevel) event.level;

        // Increment the tick counter
        tickCounter++;

        // Log every 10 seconds for debugging purposes
        if (tickCounter % 200 == 0) {
            LOGGER.info("Server uptime: " + (tickCounter / 20) + " seconds (" + tickCounter + " ticks)");
        }

        // Check if 30 minutes (36,000 ticks) have passed
        if (tickCounter >= THIRTY_MINUTES_IN_TICKS && !spawnTriggered) {
            LOGGER.info("30 minutes have passed since server start. Triggering Father spawn...");
            spawnTriggered = true; // Ensure this happens only once
            trySpawnFather(serverLevel);
        }

        // Additional spawning logic (e.g., time-of-day-based spawns)
        long timeOfDay = serverLevel.getDayTime() % 24000;

        if (timeOfDay >= SPAWN_START_TIME && timeOfDay <= SPAWN_END_TIME) {
            if (tickCounter % SPAWN_ATTEMPT_INTERVAL == 0) {
                LOGGER.info("Attempting spawn cycle at time: " + timeOfDay);
                trySpawnFather(serverLevel);
            }
        }
    }

    private static void trySpawnFather(ServerLevel level) {
        // Check if the current dimension is the overworld
//        if (!level.dimension().location().equals(Level.OVERWORLD.location())) {
//            LOGGER.info("Not in overworld, skipping Father spawn.");
//            return;
//        }

        // Ensure overworld dimension is loaded
        ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);
        if (overworld == null) {
            LOGGER.error("Overworld dimension is null, cannot spawn.");
            return;
        }

        List<ServerPlayer> players = new ArrayList<>(overworld.players());
        LOGGER.info("Checking spawn for " + players.size() + " players in overworld.");

        for (ServerPlayer player : players) {

            // Check existing Father mobs near player
            List<Father> nearbyFathers = overworld.getEntitiesOfClass(
                    Father.class,
                    player.getBoundingBox().inflate(SPAWN_RADIUS)
            );


            if (!nearbyFathers.isEmpty()) {
                LOGGER.info("Skipping spawn - Father already nearby.");
                continue;
            }

            for (int attempts = 0; attempts < 5; attempts++) {
                double angle = overworld.random.nextDouble() * Math.PI * 2;
                double distance = overworld.random.nextDouble() * SPAWN_RADIUS;

                double spawnX = player.getX() + Math.cos(angle) * distance;
                double spawnZ = player.getZ() + Math.sin(angle) * distance;
                double spawnY = findGroundLevel(overworld, spawnX, spawnZ);

                LOGGER.info("Spawn attempt " + (attempts + 1) + " at coordinates: " +
                        String.format("%.2f, %.2f, %.2f", spawnX, spawnY, spawnZ));

                if (spawnY != -1 && isValidSpawnLocation(overworld, spawnX, spawnY, spawnZ)) {
                    LOGGER.info("Valid spawn location found, attempting to spawn Father.");
                    spawnFather(overworld, new Vec3(spawnX, spawnY, spawnZ));
                    return;
                }
            }
        }
    }

    private static double findGroundLevel(ServerLevel level, double x, double z) {
        int maxY = level.getMaxBuildHeight();
        int minY = level.getMinBuildHeight();
        int foundY = -1;
        int airBlocksAbove = 0;
        boolean foundSurface = false;

        // Start from player's Y level ± 16 blocks instead of max height
        Player nearestPlayer = level.getNearestPlayer(x, minY, z, 100, false);
        if (nearestPlayer instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer) nearestPlayer;
            int playerY = (int) serverPlayer.getY();
            maxY = Math.min(level.getMaxBuildHeight(), playerY + 16);
            minY = Math.max(level.getMinBuildHeight(), playerY - 16);
        }


        // Scan from maxY downward to minY to find ground level
        for (int y = maxY; y >= minY; y--) {
            BlockPos pos = new BlockPos((int) Math.floor(x), y, (int) Math.floor(z));
            BlockState blockState = level.getBlockState(pos);


            // Check if the block is walkable
            boolean isWalkable = blockState.isCollisionShapeFullBlock(level, pos)
                    && !blockState.isAir(); // Ensure it's not air or non-collidable

            if (!isWalkable) {
                airBlocksAbove++;
            } else {
                if (airBlocksAbove >= 2) { // Ensure at least 2 air blocks above the solid block
                    BlockPos belowPos = pos.below();
                    BlockState belowBlockState = level.getBlockState(belowPos);
                    boolean hasSolidBelow = belowBlockState.isCollisionShapeFullBlock(level, belowPos)
                            && !belowBlockState.isAir(); // Check solid ground below

                    if (hasSolidBelow) {
                        foundY = y + 1; // Spawn on top of the block
                        foundSurface = true;
                        break;
                    }
                }
                airBlocksAbove = 0;
            }
        }

        if (foundSurface) {
            return foundY;
        }

        return level.getSeaLevel(); // Default fallback to sea level
    }

    private static boolean isValidSpawnLocation(ServerLevel level, double x, double y, double z) {
        BlockPos spawnPos = new BlockPos((int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(z));

        // Check if location is visible to any players
        for (ServerPlayer player : level.players()) {
            if (player.distanceToSqr(x, y, z) < 10 * 10) {
                LOGGER.info("Invalid spawn location - too close to player");
                return false;
            }
        }

        LOGGER.info("Valid spawn location found");
        return true;
    }

    private static void spawnFather(ServerLevel level, Vec3 pos) {
        Father father = ModEntities.FATHER.get().create(level);
        if (father != null) {
            father.moveTo(pos.x, pos.y, pos.z);
            father.finalizeSpawn(level, level.getCurrentDifficultyAt(father.blockPosition()),
                    MobSpawnType.NATURAL, null, null);
            level.addFreshEntity(father);
            LOGGER.info("Successfully spawned Father at " +
                    String.format("%.2f, %.2f, %.2f", pos.x, pos.y, pos.z));
        } else {
            LOGGER.error("Failed to create Father entity!");
        }
    }
}