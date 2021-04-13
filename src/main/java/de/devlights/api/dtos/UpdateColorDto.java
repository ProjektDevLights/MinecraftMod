package de.devlights.api.dtos;

import de.devlights.utils.Color;

public class UpdateColorDto {
    private String pattern;
    private String[] colors = new String[10];
    private Integer timeout = null;

    public UpdateColorDto(String pattern, Color color) {
        this.pattern = pattern;
        this.colors[0] = color.toString();
    }

    public UpdateColorDto(String pattern, Integer timeout) {
        this.pattern = pattern;
        this.timeout = timeout;
    }

    public UpdateColorDto(String pattern, Color color, Integer timeout) {
        this.pattern = pattern;
        this.colors[0] = color.toString();
        this.timeout = timeout;
    }
}
