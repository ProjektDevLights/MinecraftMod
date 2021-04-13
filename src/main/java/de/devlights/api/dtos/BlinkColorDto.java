package de.devlights.api.dtos;

import de.devlights.utils.Color;

public class BlinkColorDto {
    String color;
    int time;

    public BlinkColorDto(Color color, int time) {
        this.color = color.toString();
        this.time = time;
    }
}
