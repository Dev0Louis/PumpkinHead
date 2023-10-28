package dev.louis.pumpkinhead.mixin;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class PumpkinsEverywhereMixin {
    @Shadow public abstract Random getRandom();

    @Unique
    private static final List<ItemStack> headReplacements = List.of(
            Items.PUMPKIN.getDefaultStack(),
            Items.CARVED_PUMPKIN.getDefaultStack(),
            Items.JACK_O_LANTERN.getDefaultStack()
    );

    private ItemStack headReplacement = headReplacements.get(this.getRandom().nextInt(headReplacements.size()));

    @Inject(method = "getEquipmentChanges", at = @At("RETURN"), cancellable = true)
    public void pumpkinsEverywhere(CallbackInfoReturnable<@Nullable Map<EquipmentSlot, ItemStack>> cir) {
        var map = cir.getReturnValue();
        if(map == null) map = Maps.newEnumMap(EquipmentSlot.class);
        map.put(EquipmentSlot.HEAD, headReplacement);
        cir.setReturnValue(map);
        //equipmentChanges.putIfAbsent(EquipmentSlot.HEAD, Items.PUMPKIN.getDefaultStack());
    }
}
