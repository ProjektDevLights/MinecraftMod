package de.devlight.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.devlight.api.dtos.BlinkColorDto;
import de.devlight.api.dtos.BrightnessDto;
import de.devlight.api.dtos.UpdateColorDto;
import de.devlight.utils.Color;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class Api {
    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static Future<Boolean> setSingleColor(Color color) {
        System.out.println(color);
        return executor.submit(() -> {
            HttpResponse<String> res = Unirest.patch("/tags/minecraft/color").body(new UpdateColorDto("plain", color))
                    .asString();
            return res.isSuccess();
        });
    }

    public static Future<Boolean> blink(Color color, int time) {
        return executor.submit(() -> {
            HttpResponse<String> res = Unirest.post("/tags/minecraft/blink").body(new BlinkColorDto(color, time))
                    .asString();
            return res.isSuccess();
        });
    }

    public static Future<Boolean> setBrightness(int brightness) {
        return executor.submit(() -> {
            // not implemented on server
            HttpResponse<String> res = Unirest.patch("/tags/minecraft/brightness").body(new BrightnessDto(brightness))
                    .asString();
            return res.isSuccess();
        });
    }

    public static Future<Boolean> turnOff() {
        return executor.submit(() -> {
            HttpResponse<String> res = Unirest.patch("/tags/minecraft/off")
                    .body("{}" /* because global content is json */).asString();
            System.out.println(res.getBody());
            return res.isSuccess();
        });
    }

    public static Future<Boolean> turnOn() {
        return executor.submit(() -> {
            HttpResponse<String> res = Unirest.patch("/tags/minecraft/on").body("{}").asString();
            System.out.println(res.getBody());

            return res.isSuccess();
        });
    }
}
