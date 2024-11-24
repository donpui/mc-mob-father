package com.minecraft.fathermob;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class BookAttack extends ThrowableItemProjectile {
    public BookAttack(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);

        this.setDeltaMovement(this.getDeltaMovement().scale(0.05));
    }

    public BookAttack(Level level, LivingEntity shooter) {
        super(ModEntities.BOOK_ATTACK.get(), shooter, level);
    }

    public BookAttack(Level level, double x, double y, double z) {
        super(ModEntities.BOOK_ATTACK.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.BOOK;
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);

        if (!this.level().isClientSide) {
            Entity target = hitResult.getEntity();
            Entity owner = this.getOwner();

            if (owner != null) {
                // Pass the owner entity to create the damage source
                target.hurt(ModDamageSources.bookAttack(owner), 5.0F);
            }

            // Spawn particles on hit
            for(int i = 0; i < 8; ++i) {
                this.level().addParticle(
                        net.minecraft.core.particles.ParticleTypes.ITEM_SNOWBALL,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        0.0D,
                        0.0D,
                        0.0D
                );
            }
        }

        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);

        if (!this.level().isClientSide) {
            // Spawn particles on block hit
            for(int i = 0; i < 8; ++i) {
                this.level().addParticle(
                        net.minecraft.core.particles.ParticleTypes.ITEM_SNOWBALL,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        0.0D,
                        0.0D,
                        0.0D
                );
            }
        }

        this.discard();
    }

    @Override
    public void setOwner(Entity owner) {
        super.setOwner(owner);
        if (owner != null) {
            // Slow down the velocity by scaling it
            double factor = 0.05; // Adjust to your liking (e.g., 0.5 means half speed)
            this.setDeltaMovement(this.getDeltaMovement().scale(factor));
        }
    }

    @Override
    public void tick() {
        super.tick();

        // Optional: Add trailing particles as the book moves
//        if (this.level().isClientSide) {
//            this.level().addParticle(
//                    net.minecraft.core.particles.ParticleTypes.ITEM_SNOWBALL,
//                    this.getX(),
//                    this.getY(),
//                    this.getZ(),
//                    0.0D,
//                    0.0D,
//                    0.0D
//            );
//        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}