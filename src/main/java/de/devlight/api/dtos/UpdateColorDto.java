package de.devlight.api.dtos;

import de.devlight.utils.Color;

public class UpdateColorDto {
    private String pattern;
    private String[] colors = new String[10];

    public UpdateColorDto(String pattern, Color color) {
        this.pattern = pattern;
        this.colors[0] = color.toString();
    }
}
