package de.devlight.api.dtos;

public class UpdateColorDto {
    private String pattern;
    private String[] colors = new String[10];

    public UpdateColorDto(String pattern, String color) {
        this.pattern = pattern;
        this.colors[0] = color;
    }
}
