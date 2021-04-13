package de.devlights.utils;

public class Color {

    private int r;
    private int g;
    private int b;

    public Color(String hex) {
        switch (hex.length()) {
            case 3:
                r = Integer.parseInt(String.valueOf(hex.charAt(0)) + String.valueOf(hex.charAt(0)), 16);
                g = Integer.parseInt(String.valueOf(hex.charAt(1)) + String.valueOf(hex.charAt(1)), 16);
                b = Integer.parseInt(String.valueOf(hex.charAt(2)) + String.valueOf(hex.charAt(2)), 16);
                break;
            case 4:
                r = Integer.parseInt(String.valueOf(hex.charAt(1)) + String.valueOf(hex.charAt(1)), 16);
                g = Integer.parseInt(String.valueOf(hex.charAt(2)) + String.valueOf(hex.charAt(2)), 16);
                b = Integer.parseInt(String.valueOf(hex.charAt(3)) + String.valueOf(hex.charAt(3)), 16);
                break;
            case 6:
                r = Integer.parseInt(hex.substring(0, 2), 16);
                g = Integer.parseInt(hex.substring(2, 4), 16);
                b = Integer.parseInt(hex.substring(4, 6), 16);
                break;
            case 7:
                r = Integer.parseInt(hex.substring(1, 3), 16);
                g = Integer.parseInt(hex.substring(3, 5), 16);
                b = Integer.parseInt(hex.substring(5, 7), 16);
                break;
        }
    }

    public Color(int red, int green, int blue) {
        r = red;
        g = green;
        b = blue;
    }

    public String getHex() {
        return String.format("%02X", r) + String.format("%02X", g) + String.format("%02X", b);
    }

    public String toString() {
        return "#" + getHex();
    }

}
