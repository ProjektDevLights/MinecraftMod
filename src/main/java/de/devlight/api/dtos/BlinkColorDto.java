package de.devlight.api.dtos;

import de.devlight.utils.Color;

public class BlinkColorDto {
    String color;
    int time;

    public BlinkColorDto(Color color, int time) {
        this.color = color.toString();
        this.time = time;
    }
}
