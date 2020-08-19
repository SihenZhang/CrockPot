package com.sihenzhang.crockpot;

import com.sihenzhang.crockpot.base.CrockPotIngredientManager;
import com.sihenzhang.crockpot.client.gui.screen.CrockPotScreen;
import com.sihenzhang.crockpot.integration.ModIntegrationTheOneProbe;
import com.sihenzhang.crockpot.loot.CrockPotSeedsDropModifier;
import com.sihenzhang.crockpot.network.NetworkManager;
import com.sihenzhang.crockpot.network.PacketSyncCrockpotIngredients;
import com.sihenzhang.crockpot.recipe.RecipeManager;
import com.sihenzhang.crockpot.registry.CrockPotRegistry;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.function.Supplier;

@Mod(CrockPot.MOD_ID)
public final class CrockPot {
    public static final String MOD_ID = "crockpot";

    public static final ItemGroup ITEM_GROUP = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(CrockPotRegistry.crockPotBasicBlockItem.get());
        }
    };

    public static final CrockPotIngredientManager INGREDIENT_MANAGER = new CrockPotIngredientManager();
    public static final RecipeManager RECIPE_MANAGER = new RecipeManager();

    public CrockPot() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CrockPotConfig.COMMON_CONFIG);
        CrockPotRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        CrockPotRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        CrockPotRegistry.TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CrockPotRegistry.CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
        MinecraftForge.EVENT_BUS.addListener(this::onAnimalAppear);
        MinecraftForge.EVENT_BUS.addListener(this::onEntityInteract);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerLoggedIn);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetupEvent);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::sendIMCMessage);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(GlobalLootModifierSerializer.class, this::registerModifierSerializers);

        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLCommonSetupEvent e) -> NetworkManager.registerPackets());
    }

    public void sendIMCMessage(InterModEnqueueEvent event) {
        ModList modList = ModList.get();
        if (modList.isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", ModIntegrationTheOneProbe::new);
        }
    }

    public void onServerStarting(FMLServerAboutToStartEvent event) {
        event.getServer().getResourceManager().addReloadListener(INGREDIENT_MANAGER);
        event.getServer().getResourceManager().addReloadListener(RECIPE_MANAGER);
    }

    public void onClientSetupEvent(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(CrockPotRegistry.crockPotContainer.get(), CrockPotScreen::new);
    }

    public void onAnimalAppear(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof AnimalEntity) {
            AnimalEntity animalEntity = (AnimalEntity) event.getEntity();
            if ((animalEntity.getNavigator() instanceof GroundPathNavigator) || (animalEntity.getNavigator() instanceof FlyingPathNavigator)) {
                boolean alreadySetUp = false;
                for (Goal goal : animalEntity.goalSelector.goals) {
                    if (goal instanceof TemptGoal) {
                        TemptGoal temptGoal = (TemptGoal) goal;
                        if (temptGoal.isTempting(new ItemStack(CrockPotRegistry.powCake.get()))) {
                            alreadySetUp = true;
                            break;
                        }
                    }
                }
                if (!alreadySetUp) {
                    animalEntity.goalSelector.addGoal(3, new TemptGoal(animalEntity, 0.8D, false, Ingredient.fromItems(CrockPotRegistry.powCake.get())));
                }
            }
        }
    }

    public void registerModifierSerializers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().register(new CrockPotSeedsDropModifier.Serializer().setRegistryName(new ResourceLocation(CrockPot.MOD_ID, "crockpot_seeds_drop")));
    }

    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof CowEntity) {
            CowEntity cow = (CowEntity) event.getTarget();
            PlayerEntity player = event.getPlayer();
            ItemStack stack = event.getItemStack();
            if (stack.getItem() == Items.GLASS_BOTTLE && !cow.isChild()) {
                player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
                if (stack.isEmpty()) {
                    player.setHeldItem(event.getHand(), new ItemStack(CrockPotRegistry.milkBottle.get()));
                } else if (!player.inventory.addItemStackToInventory(new ItemStack(CrockPotRegistry.milkBottle.get()))) {
                    player.dropItem(new ItemStack(CrockPotRegistry.milkBottle.get()), false);
                }
            }
        }
    }

    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(
                () -> (ServerPlayerEntity) event.getEntity()),
                new PacketSyncCrockpotIngredients(INGREDIENT_MANAGER.serialize())
        );
        if (CrockPotConfig.SPAWN_WITH_BOOK.get()) {
            CompoundNBT playerData = event.getPlayer().getPersistentData();
            CompoundNBT data = event.getPlayer().getPersistentData().getCompound(PlayerEntity.PERSISTED_NBT_TAG);
            if (!data.getBoolean("crock_pot_book")) {
                ItemHandlerHelper.giveItemToPlayer(event.getPlayer(), PatchouliAPI.instance.getBookStack(new ResourceLocation(CrockPot.MOD_ID, "book")));
                data.putBoolean("crock_pot_book", true);
                playerData.put(PlayerEntity.PERSISTED_NBT_TAG, data);
            }
        }
    }
}
