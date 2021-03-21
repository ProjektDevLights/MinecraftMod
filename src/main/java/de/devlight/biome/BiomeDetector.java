package de.devlight.biome;

import de.devlight.api.Api;
import de.devlight.utils.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.Biomes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BiomeDetector {

    private static final Logger LOGGER = LogManager.getLogger();

    Biome currentBiome;

    Minecraft mc = Minecraft.getInstance();

    public BiomeDetector() {
    }

    public void run() {
        PlayerEntity player = mc.player;
        Biome biome = mc.world.getBiomeManager().getBiome(player.getPosition());
        if (currentBiome == null || !currentBiome.equals(biome)) {
            currentBiome = biome;
            setColorWithBiome(biome);
        }

    }

    public void setColorWithBiome(Biome biome) {
        if (biome.getCategory().equals(Category.BEACH)) {
            LOGGER.info("beach");
            Api.setSingleColor(new Color("#ffc700"));

        }
        if (biome.getCategory().equals(Category.DESERT)) {
            LOGGER.info("desert category");
            Api.setSingleColor(new Color("#ffff00"));

        }
        if (biome.getCategory().equals(Category.EXTREME_HILLS)) {
            LOGGER.info("extreme hills category");
            Api.setSingleColor(new Color("#ffffff"));

        }
        if (biome.getCategory().equals(Category.FOREST)) {
            LOGGER.info("forest category");
            Api.setSingleColor(new Color("#006408"));
        }
        if (biome.getCategory().equals(Category.ICY)) {
            Api.setSingleColor(new Color("#00b0ff"));
            LOGGER.info("icy category");
        }
        if (biome.getCategory().equals(Category.JUNGLE)) {
            LOGGER.info("jungle category");
            Api.setSingleColor(new Color("#4fff00"));

        }
        if (biome.getCategory().equals(Category.MESA)) {
            Api.setSingleColor(new Color("#ff5100"));

            LOGGER.info("mesa category");
        }
        if (biome.getCategory().equals(Category.MUSHROOM)) {
            Api.setSingleColor(new Color("#78451f"));

            LOGGER.info("mushroom category");
        }
        if (biome.getCategory().equals(Category.NETHER)) {
            // TODO different biomes
            LOGGER.info("nether category");
            if (compareBiomeToKey(biome, Biomes.NETHER_WASTES)) {
                Api.setSingleColor(new Color("#380400"));
            }
            if (compareBiomeToKey(biome, Biomes.SOUL_SAND_VALLEY)) {
                Api.setSingleColor(new Color("#00233f"));
            }
            if (compareBiomeToKey(biome, Biomes.CRIMSON_FOREST)) {
                Api.setSingleColor(new Color("#340700"));
            }
            if (compareBiomeToKey(biome, Biomes.WARPED_FOREST)) {
                Api.setSingleColor(new Color("#0f0058"));
            }
            if (compareBiomeToKey(biome, Biomes.BASALT_DELTAS)) {
                Api.setSingleColor(new Color("#381f5b"));
            }
        }
        if (biome.getCategory().equals(Category.OCEAN)) {
            LOGGER.info("ocean category");
            Api.setSingleColor(new Color("#0019ff"));

        }
        if (biome.getCategory().equals(Category.PLAINS)) {
            LOGGER.info("plains category");
            Api.setSingleColor(new Color("#abff00"));

        }
        if (biome.getCategory().equals(Category.RIVER)) {
            LOGGER.info("river category");
            Api.setSingleColor(new Color("#005fff"));

        }
        if (biome.getCategory().equals(Category.SAVANNA)) {
            Api.setSingleColor(new Color("#ccff00"));
            LOGGER.info("savana category");
        }
        if (biome.getCategory().equals(Category.SWAMP)) {
            LOGGER.info("swamp category");
            Api.setSingleColor(new Color("#007102"));

        }
        if (biome.getCategory().equals(Category.TAIGA)) {
            LOGGER.info("taiga category");
            Api.setSingleColor(new Color("#00ff7d"));

        }
        if (biome.getCategory().equals(Category.THEEND)) {
            LOGGER.info("the end category");
            Api.setSingleColor(new Color("#5f0089"));

        }
    }

    private boolean compareBiomeToKey(Biome biome, RegistryKey<Biome> key) {
        return Minecraft.getInstance().world.func_241828_r().getRegistry(Registry.BIOME_KEY).getKey(biome)
                .equals(key.getLocation());

    }
}
