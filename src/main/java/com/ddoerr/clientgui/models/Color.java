package com.ddoerr.clientgui.models;

import com.google.common.base.Splitter;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

public class Color {
    private static final Pattern hexPattern = Pattern.compile("^#?(\\p{XDigit}{3}|\\p{XDigit}{6}|\\p{XDigit}{8})$");
    private static final Pattern rgbPattern = Pattern.compile("^(?:rgba?\\()?(\\d{1,3},\\d{1,3},\\d{1,3}(,\\d{1,3})?)\\)?$");
    private static final Pattern namePattern = Pattern.compile("^(\\w+) ?(\\d{1,3}%)?$");

    private static final Splitter twoSplitter = Splitter.fixedLength(2);
    private static final Splitter commaSplitter = Splitter.on(",");

    // clrs.cc
    public static final Color NAVY      = new Color(0x00, 0x1F, 0x3F, "NAVY");
    public static final Color BLUE      = new Color(0x00, 0x74, 0xD9, "BLUE");
    public static final Color AQUA      = new Color(0x7F, 0xDB, 0xFF, "AQUA");
    public static final Color TEAL      = new Color(0x39, 0xCC, 0xCC, "TEAL");
    public static final Color OLIVE     = new Color(0x3D, 0x99, 0x70, "OLIVE");
    public static final Color GREEN     = new Color(0x2E, 0xCC, 0x40, "GREEN");
    public static final Color LIME      = new Color(0x01, 0xFF, 0x70, "LIME");
    public static final Color YELLOW    = new Color(0xFF, 0xDC, 0x00, "YELLOW");
    public static final Color ORANGE    = new Color(0xFF, 0x85, 0x1B, "ORANGE");
    public static final Color RED       = new Color(0xFF, 0x41, 0x36, "RED");
    public static final Color MAROON    = new Color(0x85, 0x14, 0x4b, "MAROON");
    public static final Color FUCHSIA   = new Color(0xF0, 0x12, 0xBE, "FUCHSIA");
    public static final Color PURPLE    = new Color(0xB1, 0x0D, 0xC9, "PURPLE");
    public static final Color BLACK     = new Color(0x11, 0x11, 0x11, "BLACK");
    public static final Color GREY      = new Color(0xAA, 0xAA, 0xAA, "GREY");
    public static final Color SILVER    = new Color(0xDD, 0xDD, 0xDD, "SILVER");
    public static final Color WHITE     = new Color(0xFF, 0xFF, 0xFF, "WHITE");

    public static final Color TRANSPARENT = new Color(0x00, 0x00, 0x00, 0x00, "TRANSPARENT");

    private static final Color[] namedColors = new Color[] {
        NAVY, BLUE, AQUA, TEAL, OLIVE, GREEN, LIME, YELLOW, ORANGE, RED, MAROON, FUCHSIA, PURPLE, BLACK, GREY, SILVER, WHITE, TRANSPARENT
    };

    private final int alpha;
    private final int red;
    private final int green;
    private final int blue;

    private final String original;

    private static int[] splitIntoIntegers(String input, Splitter splitter, int radix) {
        Iterable<String> pieces = splitter.split(input);
        return StreamSupport
                .stream(pieces.spliterator(), false)
                .mapToInt((piece) -> Integer.parseInt(piece, radix))
                .toArray();
    }

    public static Color fromHex(String hex) {
        Matcher matcher = hexPattern.matcher(hex);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Input is not an hex string");
        }

        String value = matcher.group(1);
        if (value.length() == 3) {
            value = value.replaceAll(".", "$0$0");
        }

        if (value.length() == 6) {
            int[] integers = splitIntoIntegers(value, twoSplitter, 16);
            return new Color(integers[0], integers[1], integers[2], hex);
        }

        int[] integers = splitIntoIntegers(value, twoSplitter, 16);
        return new Color(integers[0], integers[1], integers[2], integers[3], hex);
    }

    public static Color fromRgb(String rgb) {
        Matcher rgbMatcher = rgbPattern.matcher(rgb);

        if (!rgbMatcher.matches()) {
            throw new IllegalArgumentException("Input is not an rgb string");
        }

        String value = rgbMatcher.group(1);
        int[] integers = splitIntoIntegers(value, commaSplitter, 10);

        if (integers.length == 3) {
            return new Color(integers[0], integers[1], integers[2], rgb);
        }

        return new Color(integers[1], integers[2], integers[3], integers[0], rgb);
    }

    public static Color fromFormatting(Formatting formatting) {
        if (!formatting.isColor()) {
            throw new IllegalArgumentException("Input has to be a color");
        }

        int colorValue = formatting.getColorValue();

        int red = colorValue >> 16 & 0xff;
        int green = colorValue >> 8 & 0xff;
        int blue = colorValue >> 0 & 0xff;

        return new Color(red, green, blue, formatting.toString());
    }

    public static Color fromFormatting(String formatCode) {
        char character = formatCode.charAt(0);

        if (character == '§') {
            character = formatCode.charAt(1);
        }

        Formatting formatting = Formatting.byCode(character);

        if (formatting == null) {
            throw new IllegalArgumentException("Input is an unknown color");
        }

        return fromFormatting(formatting);
    }

    public static Color fromName(String name) {
        Matcher matcher = namePattern.matcher(name);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Input is not in the correct name format");
        }

        String colorName = matcher.group(1);
        float alpha = -1;

        String percentage = matcher.group(2);
        if (percentage != null)
        {
            String onlyNumber = percentage.replace("%", "").trim();
            alpha = Integer.parseInt(onlyNumber);
        }

        for (Color color : namedColors) {
            if (color.original.equalsIgnoreCase(colorName)) {
                if (alpha == -1) {
                    return color;
                }

                return new Color((int)((alpha / 100) * 0xff), color.red, color.green, color.blue, name);
            }
        }

        throw new IllegalArgumentException("Input is not a known color");
    }

    public static Color parse(String input) {
        try {
            return fromHex(input);
        } catch (Exception ignored) {}

        try {
            return fromRgb(input);
        } catch (Exception ignored) {}

        try {
            return fromFormatting(input);
        } catch (Exception ignored) {}

        try {
            return fromName(input);
        } catch (Exception ignored) {}

        return null;
    }

    private Color(int alpha, int red, int green, int blue) {
        this(alpha, red, green, blue, null);
    }

    private Color(int red, int green, int blue) {
        this(0xff, red, green, blue, null);
    }

    private Color(int red, int green, int blue, String original) {
        this(0xff, red, green, blue, original);
    }

    private Color(int alpha, int red, int green, int blue, String original) {
        this.alpha = alpha;
        this.red = red;
        this.green = green;
        this.blue = blue;

        this.original = original;
    }

    public int toMinecraftColor() {
        return (alpha << 24) | (red << 16) | (green << 8) | (blue << 0);
    }

    public String toHexColor() {
        return "#" +
                Integer.toHexString(red) +
                Integer.toHexString(green) +
                Integer.toHexString(blue) +
                Integer.toHexString(alpha);
    }

    public int getAlpha() {
        return alpha;
    }

    public int getBlue() {
        return blue;
    }

    public int getGreen() {
        return green;
    }

    public int getRed() {
        return red;
    }

    @Override
    public String toString() {
        if (original != null) {
            return original;
        }

        return toHexColor();
    }

    public Color addAlpha(double alpha) {
        return new Color((int)(alpha * 0xff), red, green, blue, original);
    }
}
