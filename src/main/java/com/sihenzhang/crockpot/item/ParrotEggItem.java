package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.entity.ThrownParrotEgg;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class ParrotEggItem extends Item implements ProjectileItem {
    private final Parrot.Variant variant;

    public ParrotEggItem(Parrot.Variant variant, Item.Properties properties) {
        super(properties);
        this.variant = variant;
        DispenserBlock.registerProjectileBehavior(this);
    }

    public Parrot.Variant getVariant() {
        return variant;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        var itemStack = player.getItemInHand(hand);
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.EGG_THROW,
                SoundSource.PLAYERS,
                0.5F,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (level instanceof ServerLevel serverLevel) {
            Projectile.spawnProjectileFromRotation(ThrownParrotEgg::new, serverLevel, itemStack, player, 0.0F, 1.5F, 1.0F);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        itemStack.consume(1, player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
        return new ThrownParrotEgg(level, position.x(), position.y(), position.z(), itemStack);
    }
}
