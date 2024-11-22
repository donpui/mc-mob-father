package com.minecraft.fathermob;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.damagesource.DamageSource;


public class Father extends AbstractSkeleton {

    private int ambientSoundCooldown = 0;

    public Father(EntityType<? extends AbstractSkeleton> entityType, Level level) {
        super(entityType, level);

        // Remove default equipment by setting hands empty
        this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
    }

    private void clearDefaultEquipment() {
        // Explicitly clear equipment slots for main hand and off-hand
        this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        // Play ambient sound occasionally
        if (!this.level().isClientSide && this.isAlive()) {
            System.out.println("[DEBUG] aiStep called for Father entity at " + this.getX() + ", " + this.getY() + ", " + this.getZ());
            if (ambientSoundCooldown > 0) {
                ambientSoundCooldown--;
                System.out.println("[DEBUG] Ambient sound cooldown: " + ambientSoundCooldown);
            } else if (this.random.nextInt(100) == 0) { // 1% chance per tick
                System.out.println("[DEBUG] Playing ambient sound: " + ModSounds.FATHER_AMBIENT.get());
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                        ModSounds.FATHER_AMBIENT.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
                ambientSoundCooldown = 200; // Cooldown of 200 ticks (10 seconds)
            }
        }
    }

    @Override
    protected SoundEvent getStepSound() {
        // Fetch the sound event
        SoundEvent stepSound = ModSounds.FATHER_STEP.get();

        // Log debug information
        if (stepSound != null) {
            System.out.println("[DEBUG] getStepSound called. Returning: " + ModSounds.FATHER_STEP.getId());
        } else {
            System.out.println("[DEBUG] getStepSound called but stepSound is null!");
        }

        // Return the fetched sound event
        return stepSound;
    }

    @Override
    protected boolean isSunBurnTick() {
        // Prevent the Father mob from burning in sunlight
        return false;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.FATHER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.FATHER_DEATH.get();
    }


    @Override
    protected SoundEvent getAmbientSound() {
        // Optional: Return null to disable the default ambient sound
        return null;
    }

    @Override
    public void performRangedAttack(net.minecraft.world.entity.LivingEntity target, float distanceFactor) {
        // Create an apple projectile (replacing the default arrow)
        ThrowableItemProjectile appleProjectile = new ThrowableItemProjectile(EntityType.SNOWBALL, this.getCommandSenderWorld()) {
            @Override
            protected Item getDefaultItem() {
                return Items.APPLE; // Use an apple as the projectile
            }
        };

        // Set the position and trajectory of the apple
        appleProjectile.setPos(this.getX(), this.getEyeY() - 0.1, this.getZ());
        double dx = target.getX() - this.getX();
        double dy = (target.getEyeY() - 1) - appleProjectile.getY();
        double dz = target.getZ() - this.getZ();
        double distance = Math.sqrt(dx * dx + dz * dz);
        appleProjectile.shoot(dx, dy + distance * 0.2, dz, 1.5F, 1.0F);

        // Add the apple projectile to the world
        this.getCommandSenderWorld().addFreshEntity(appleProjectile);
    }

    public static AttributeSupplier.Builder createAttributes() {
        // Define the Father mob's attributes
        return AbstractSkeleton.createAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)      // Set max health to 20
                .add(Attributes.MOVEMENT_SPEED, 0.25) // Set movement speed
                .add(Attributes.ATTACK_DAMAGE, 1.0);  // Set melee attack damage
    }
}
