package com.minecraft.fathermob;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.damagesource.DamageSource;

public class Father extends AbstractSkeleton implements RangedAttackMob {
    // Don't initialize bookAttackGoal as a field, create it in registerGoals
    private int ambientSoundCooldown = 0;

    public Father(EntityType<? extends AbstractSkeleton> entityType, Level level) {
        super(entityType, level);
        this.clearDefaultEquipment();
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOOK));
    }

    @Override
    protected void registerGoals() {
        // Create the bookAttackGoal here instead of as a field
        RangedAttackGoal bookAttackGoal = new RangedAttackGoal(this, 1.0D, 60, 15.0F);

        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(4, bookAttackGoal);
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, net.minecraft.world.entity.player.Player.class, true));
    }

    private void clearDefaultEquipment() {
        this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide && this.isAlive()) {
            if (ambientSoundCooldown > 0) {
                ambientSoundCooldown--;
            } else if (this.random.nextInt(100) == 0) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                        ModSounds.FATHER_AMBIENT.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
                ambientSoundCooldown = 200;
            }
        }
    }

    @Override
    protected SoundEvent getStepSound() {
        return ModSounds.FATHER_STEP.get();
    }

    @Override
    protected boolean isSunBurnTick() {
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
        return null;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);

        // Add 10 bookshelf items to the drops
        for (int i = 0; i < 10; i++) {
            this.spawnAtLocation(Items.BOOKSHELF);
        }
    }

    @Override
    public void performRangedAttack(net.minecraft.world.entity.LivingEntity target, float distanceFactor) {
        if (!this.level().isClientSide) {
            BookAttack bookAttack = new BookAttack(this.level(), this);

            double dx = target.getX() - this.getX();
            double dy = target.getEyeY() - 1.1D - bookAttack.getY();
            double dz = target.getZ() - this.getZ();
            double distance = Math.sqrt(dx * dx + dz * dz);

            bookAttack.shoot(dx, dy + distance * 0.2D, dz, 0.8F, 1.0F);

            this.level().addFreshEntity(bookAttack);
        }
    }



    public static AttributeSupplier.Builder createAttributes() {
        return AbstractSkeleton.createAttributes()
                .add(Attributes.MAX_HEALTH, 50.0)
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, 0.2)
                .add(Attributes.FOLLOW_RANGE, 50.0);
    }
}