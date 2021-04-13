package de.devlights.events;

import de.devlights.api.Api;
import de.devlights.biome.BiomeDetector;
import de.devlights.utils.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.LightType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

@OnlyIn(Dist.CLIENT)
public class EventHandlersIngame {

    private BiomeDetector biomeDetector = new BiomeDetector();
    private static final Logger LOGGER = LogManager.getLogger();
    private boolean isSleeping;
    private int playerXp;
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

    private void runEventChecks(){
        PlayerEntity player = Minecraft.getInstance().player;
        if (player.isSleeping() != this.isSleeping) runSleepAction(player);
        if (player.experienceTotal != playerXp) runXpAction(player);
        runBrighnessCheckAction(player);
        // currently in vehicle
        if (player.isPassenger()) {
            inVehicle = true;
            runRidingAction(player);
            // just got out of vehicle
        } else if(inVehicle){
            inVehicle = false;
            biomeDetector.run(true);
            // not in vehicle
        } else {
            biomeDetector.run();
        }
    }


    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event){
        if(isRightPlayer(event.getEntityLiving())){
            Api.blink(new Color("#ff0000"), 1000);
        }
    }


    @SubscribeEvent
    public void onChatMessageReceived(ClientChatReceivedEvent event){
        String messsage = event.getMessage().toString().toLowerCase();
        if(messsage.contains("party")){
            if(messsage.contains("end")){
                runEventChecks();
            } else Api.startParty();
        }
    }

    private void runRidingAction(PlayerEntity player) {
        Vector3d motion = player.getRidingEntity().getMotion();
        double motionAbs = pythagoras(motion.x, motion.z);
        // speed in range 30-1500, to turn it around *-1 and +1530 => 30 = 1500, 1500 = 30
        int timeout = (int) Math.max(30, Math.min(motionAbs*750, 1500))*-1 + 1530;
        System.out.println(motion);
        Api.setRunnerColor(biomeDetector.getColor(), timeout);
        Date next = new Date();
        next.setTime(next.getTime() + 200);
        nextUpdate = next;

    }

    private double pythagoras(double a, double b){
        return Math.sqrt(a*a+b*b);
    }
    private void runSleepAction(PlayerEntity player){
            System.out.println("sleeping");
            this.isSleeping = player.isSleeping();
            if (this.isSleeping) {
                Api.turnOff();
            } else {
                Api.turnOn();
            }
    }

    // does not work anymore (do just for every 5 level)
    private void runXpAction(PlayerEntity player){
            this.playerXp = player.experienceTotal;
            Api.blink(new Color("#00ff00"), 400);
            Date next = new Date();
            next.setTime(next.getTime() + 1000);
            nextUpdate = next;
    }

    private void runBrighnessCheckAction(PlayerEntity player){

        ClientWorld world = Minecraft.getInstance().world;

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
            next.setTime(next.getTime() + 500);
            nextUpdate = next;
        }
    }
    /*
     * @SubscribeEvent public void onPlayerHurt(LivingAttackEvent event) { if
     * (isRightPlayer(event.getEntityLiving())) { LOGGER.info("player hurt");
     * Api.blink(new Color("#ff0000"), 500); } }
     */

    private boolean isRightPlayer(LivingEntity player) {
        if (!(player instanceof PlayerEntity)) {
            return false;
        }
        return Minecraft.getInstance().player.equals(player);
    }
}
