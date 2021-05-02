package de.devlights.events;

import de.devlights.api.Api;
import de.devlights.biome.BiomeDetector;
import de.devlights.utils.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Date;
import java.util.concurrent.ExecutionException;

@OnlyIn(Dist.CLIENT)
public class EventHandlersIngame {

    private final BiomeDetector biomeDetector = new BiomeDetector();
    private boolean isSleeping;
    private int playerXpLevel;
    private double lightLevelRatio;
    private Date nextUpdate = new Date();
    private boolean inVehicle;


    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        Date now = new Date();
        if (isRightPlayer(event.player) && event.phase.equals(Phase.END) && now.getTime() > nextUpdate.getTime()) {
            runEventChecks();
        }
    }
    @SubscribeEvent
    public void onHotbarRender(RenderGameOverlayEvent.Pre event){
        // currently in vehicle
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR) return;
        if(!(new Date().getTime() > nextUpdate.getTime())) return;
        Date next = new Date();
        next.setTime(next.getTime() + 400);
        nextUpdate = next;
        PlayerEntity player = Minecraft.getInstance().player;
        if(player == null) return;
        if (player.isPassenger()) {
            inVehicle = true;
            runRidingAction(player);
            // just got out of vehicle
        } else if (inVehicle) {
            inVehicle = false;
            biomeDetector.run(true);
            // not in vehicle
        } else {
            biomeDetector.run();
        }
    }

    private void runEventChecks(){
        PlayerEntity player = Minecraft.getInstance().player;
        if(player == null) return;
        if (player.isSleeping() != this.isSleeping) runSleepAction(player);
        if (player.experienceLevel != playerXpLevel) runXpAction(player);
        runBrightnessCheckAction(player);
    }


    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if(isRightPlayer(event.getEntityLiving())){
            Api.turnOff();
        }
    }

    @SubscribeEvent
    void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event){
        if(isRightPlayer(event.getPlayer())){
            try {
                Api.turnOn().get();
            } catch (InterruptedException | ExecutionException exception) {
                exception.printStackTrace();
            }
            biomeDetector.run(true);
        }
    }


    @SubscribeEvent
    public void onChatMessageReceived(ClientChatReceivedEvent event){
        String message = event.getMessage().getString().toLowerCase();
        if(message.contains("party")){
            if(message.contains("end")){
                biomeDetector.run(true);
            } else {
                Api.startParty();}
        }
    }

    private void runRidingAction(PlayerEntity player) {

        double distTraveledLastTickX = Minecraft.getInstance().player.getPosX() - Minecraft.getInstance().player.prevPosX;
        double distTraveledLastTickZ = Minecraft.getInstance().player.getPosZ() - Minecraft.getInstance().player.prevPosZ;
        double currentSpeed = pythagoras(distTraveledLastTickX, distTraveledLastTickZ);

        int timeout = (int) Math.max(30, Math.min(currentSpeed*20*125, 1500))*-1 + 1530;
        Api.setRunnerColor(biomeDetector.getColor(), timeout);
    }

    private double pythagoras(double a, double b){
        return Math.sqrt(a*a+b*b);
    }

    private void runSleepAction(PlayerEntity player){
            this.isSleeping = player.isSleeping();
            if (this.isSleeping) {
                Api.turnOff();
            } else {
                Api.turnOn();
            }
    }

    private void runXpAction(PlayerEntity player){
            this.playerXpLevel = player.experienceLevel;
            Api.blink(new Color("#00ff00"), 1500);
            Date next = new Date();
            next.setTime(next.getTime() + 1500);
            nextUpdate = next;
    }

    private void runBrightnessCheckAction(PlayerEntity player){

        ClientWorld world = Minecraft.getInstance().world;
        if(world == null) return;
        double sunLightReductionRatioByRain = 1 - world.getRainStrength(1.0f) * 5 / 16;
        double sunLightReductionRatioByThunder = 1 - world.getThunderStrength(1.0f) * 5 / 16;
        double sunLightReductionRatioByTime = 0.5
                + 2 * MathHelper.clamp(MathHelper.cos(world.getCelestialAngleRadians(1.0f)), -0.25, 0.25);
        int sunLightReduction = (int) ((1
                - sunLightReductionRatioByRain * sunLightReductionRatioByThunder * sunLightReductionRatioByTime)
                * 11);
        int blockLight = world.getChunkProvider().getLightManager().getLightEngine(LightType.BLOCK)
                .getLightFor(player.getPosition());
        int skyLight = world.getLightManager().getLightEngine(LightType.SKY).getLightFor(player.getPosition());
        int sunLight = Math.max(skyLight - sunLightReduction, 0);

        double curlightLevelRatio = (float) Math.max(1, Math.min(sunLight + blockLight, 16)) / 16.0f;
        if (lightLevelRatio != curlightLevelRatio) {
            lightLevelRatio = curlightLevelRatio;
            Api.setBrightness((int) (255 * lightLevelRatio));

            Date next = new Date();
            next.setTime(next.getTime() + 250);
            nextUpdate = next;
        }
    }

    private boolean isRightPlayer(LivingEntity player) {
        if (!(player instanceof PlayerEntity)) {
            return false;
        }
        PlayerEntity minecraftPlayer = Minecraft.getInstance().player;
        if(minecraftPlayer == null) return false;
        return minecraftPlayer.equals(player);
    }
}
