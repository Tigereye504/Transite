package net.tigereye.transite.register;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import net.tigereye.transite.Transite;

import java.util.Arrays;

public class TransiteOreFeatures {
    private static final int TRANSITE_ORE_VEIN_SIZE = 4;
    private static final int TRANSITE_ORE_SPAWN_ATTEMPTS = 30;
    private static final int TRANSITE_LOWER_Y = 0;
    private static final int TRANSITE_UPPER_Y = 21;

    private static final int DEEPSLATE_TRANSITE_ORE_VEIN_SIZE = 4;
    private static final int DEEPSLATE_TRANSITE_ORE_SPAWN_ATTEMPTS = 30;
    private static final int DEEPSLATE_TRANSITE_LOWER_Y = -20;
    private static final int DEEPSLATE_TRANSITE_UPPER_Y = 0;

    private static final ConfiguredFeature<?, ?> OVERWORLD_TRANSITE_ORE_CONFIGURED_FEATURE = new ConfiguredFeature
            (Feature.ORE, new OreFeatureConfig(
                    OreConfiguredFeatures.STONE_ORE_REPLACEABLES,
                    TransiteItems.TRANSITE_ORE.getDefaultState(),
                    TRANSITE_ORE_VEIN_SIZE)); // vein size

    public static PlacedFeature OVERWORLD_TRANSITE_ORE_PLACED_FEATURE = new PlacedFeature(
            RegistryEntry.of(OVERWORLD_TRANSITE_ORE_CONFIGURED_FEATURE),
            Arrays.asList(
                    CountPlacementModifier.of(TRANSITE_ORE_SPAWN_ATTEMPTS), // number of veins per chunk
                    SquarePlacementModifier.of(), // spreading horizontally
                    HeightRangePlacementModifier.uniform(YOffset.fixed(TRANSITE_LOWER_Y), YOffset.fixed(TRANSITE_UPPER_Y))
            )); // height

    private static final ConfiguredFeature<?, ?> OVERWORLD_DEEPSLATE_TRANSITE_ORE_CONFIGURED_FEATURE = new ConfiguredFeature
            (Feature.ORE, new OreFeatureConfig(
                    OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES,
                    TransiteItems.DEEPSLATE_TRANSITE_ORE.getDefaultState(),
                    DEEPSLATE_TRANSITE_ORE_VEIN_SIZE)); // vein size

    public static PlacedFeature OVERWORLD_DEEPSLATE_TRANSITE_ORE_PLACED_FEATURE = new PlacedFeature(
            RegistryEntry.of(OVERWORLD_DEEPSLATE_TRANSITE_ORE_CONFIGURED_FEATURE),
            Arrays.asList(
                    CountPlacementModifier.of(DEEPSLATE_TRANSITE_ORE_SPAWN_ATTEMPTS), // number of veins per chunk
                    SquarePlacementModifier.of(), // spreading horizontally
                    HeightRangePlacementModifier.uniform(YOffset.fixed(DEEPSLATE_TRANSITE_LOWER_Y), YOffset.fixed(DEEPSLATE_TRANSITE_UPPER_Y))
            )); // height

    public static void register() {
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
                new Identifier(Transite.MODID, "overworld_transite_ore"), OVERWORLD_TRANSITE_ORE_CONFIGURED_FEATURE);
        Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier(Transite.MODID, "overworld_transite_ore"),
                OVERWORLD_TRANSITE_ORE_PLACED_FEATURE);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY,
                        new Identifier(Transite.MODID, "overworld_transite_ore")));

        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
                new Identifier(Transite.MODID, "overworld_deepslate_transite_ore"), OVERWORLD_DEEPSLATE_TRANSITE_ORE_CONFIGURED_FEATURE);
        Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier(Transite.MODID, "overworld_deepslate_transite_ore"),
                OVERWORLD_DEEPSLATE_TRANSITE_ORE_PLACED_FEATURE);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY,
                        new Identifier(Transite.MODID, "overworld_deepslate_transite_ore")));
    }
}
