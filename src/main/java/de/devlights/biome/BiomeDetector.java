package de.devlights.biome;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.devlights.api.Api;
import de.devlights.utils.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.Biomes;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class BiomeDetector {

    private final HashMap<String, Color> biomeMappings = new HashMap<>();
    private final HashMap<Category, Color> categoryMappings = new HashMap<>();

    Biome currentBiome;
    Minecraft mc = Minecraft.getInstance();


    public BiomeDetector(){
        readBiomeMappings();
        readCategoryMappings();
    }

    private void readBiomeMappings(){
        ResourceLocation file = new ResourceLocation("devlights" ,"biome_mappings.json");
        JsonObject jsonMappings = readJson(file);
        for(Map.Entry<String, JsonElement> entry : jsonMappings.entrySet()){
            try {
                RegistryKey<Biome> registryKey = (RegistryKey<Biome>) Biomes.class.getDeclaredField(entry.getKey()).get(null);
                biomeMappings.put(registryKey.getLocation().toString(), new Color(entry.getValue().getAsString()));
            } catch (NoSuchFieldException  | IllegalAccessException  e) {
                e.printStackTrace();
            }
        }
    }

    private void readCategoryMappings(){
        ResourceLocation file = new ResourceLocation("devlights" ,"biome_category_mappings.json");
        JsonObject jsonMappings = readJson(file);
        for(Map.Entry<String, JsonElement> entry : jsonMappings.entrySet()){
            try {
                Category category = (Category) Category.class.getDeclaredField(entry.getKey()).get(null);
                categoryMappings.put(category, new Color(entry.getValue().getAsString()));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    public void run(){
        run(false);
    }

    public void run(boolean force) {
        Biome biome = getPlayerBiome();
        if (currentBiome == null || !currentBiome.equals(biome) || force) {
            currentBiome = biome;
            Api.setSingleColor(getColorForBiome(biome));
        }

    }

    public Biome getPlayerBiome(){
        if (mc.player == null || mc.world == null) return null;
        return mc.world.getBiome(mc.player.getPosition());
    }


    private Color getColorForBiome(Biome biome){

        ResourceLocation biomeLocation = getLocationFromBiome(biome);
        if(biomeLocation == null) return new Color("#1de9b6");

        String biomeLocationPath = biomeLocation.toString();
        Color color;

        // try to get biome color
        color = biomeMappings.get(biomeLocationPath);
        if(color != null) return color;

        // try to get category color
        color = categoryMappings.get(biome.getCategory());
        if(color != null) return color;

        System.out.println("Biome color and category unknown to current config file!");
        return new Color("#1de9b6");




    }

    public Color getColor(){
        return getColorForBiome(getPlayerBiome());
    }

    private ResourceLocation getLocationFromBiome(Biome biome){
        if(Minecraft.getInstance().world == null) return  null;
        return Minecraft.getInstance().world.func_241828_r().getRegistry(Registry.BIOME_KEY).getKey(biome);
    }


    private JsonObject readJson(ResourceLocation location){
       try {
           String rawData = inputStreamToString(Minecraft.getInstance().getResourceManager().getResource(location).getInputStream());
           return new Gson().fromJson(rawData, JsonObject.class);
       } catch (IOException e) {
           e.printStackTrace();
       }
       return new JsonObject();
    }

    private String inputStreamToString(InputStream stream){
        try {
            return IOUtils.toString(stream, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }
}
