package de.devlight.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.devlight.utils.Color;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class Api {
    private static String baseUrl = "http://devlight";
    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static Future<Boolean> setSingleColor(Color color) {
        System.out.println(color);
        return executor.submit(() -> {

            String body = "{\"pattern\": \"plain\", \"colors\": [\"" + color.getHex() + "\"] }";
            System.out.println(body);
            HttpResponse<String> res = Unirest.patch(baseUrl + "/tags/minecraft/color")
                    .header("Content-Type", "application/json").body(body).asString();
            System.out.println(res.getBody());
            return res.isSuccess();
        });
    }

    public static Future<Boolean> blink(Color color, int time) {
        return executor.submit(() -> {
            System.out.println("send");
            String body = "{\"color\": \"" + color.getHex() + "\", \"time\": " + time + "}";
            HttpResponse<String> res = Unirest.post(baseUrl + "/tags/minecraft/blink")
                    .header("Content-Type", "application/json").body(body).asString();
            return res.isSuccess();
        });
    }

    public static Future<Boolean> turnOff() {
        return executor.submit(() -> {
            HttpResponse<String> res = Unirest.patch(baseUrl + "/tags/minecraft/off").asString();
            return res.isSuccess();
        });
    }

    public static Future<Boolean> turnOn() {
        return executor.submit(() -> {
            HttpResponse<String> res = Unirest.patch(baseUrl + "/tags/minecraft/on").asString();
            return res.isSuccess();
        });
    }
}
