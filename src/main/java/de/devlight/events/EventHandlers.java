package de.devlight.events;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.devlight.api.Api;
import de.devlight.biome.BiomeDetector;
import de.devlight.utils.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandlers {

    private BiomeDetector biomeDetector = new BiomeDetector();
    private static final Logger LOGGER = LogManager.getLogger();
    private boolean isSleeping;
    private int playerXp;
    private static int delay = 2000;
    private Date nextUpdate = new Date();

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        Date now = new Date();
        if (isRightPlayer(event.player) && event.phase.equals(Phase.END) && now.getTime() > nextUpdate.getTime()) {
            biomeDetector.run();
            PlayerEntity player = event.player;
            if (player.isSleeping() != this.isSleeping) {
                this.isSleeping = player.isSleeping();
                if (this.isSleeping) {
                    Api.turnOff();
                } else {
                    Api.turnOn();
                }
            }
            if (player.experienceTotal != playerXp) {
                this.playerXp = player.experienceTotal;
                Api.blink(new Color("#00ff00"), 400);
                Date next = new Date();
                next.setTime(next.getTime() + 1000);
                nextUpdate = next;
            }
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
        return Minecraft.getInstance().player.equals((PlayerEntity) player);
    }
}
