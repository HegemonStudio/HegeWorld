package com.hegemonstudio.hegeworld.api.util;

import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class ChatUtil {

    public static final int MINUTES_PER_HOUR = 60;
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

    public static final List<String> BLOCKED_WORDS = Arrays.asList("chuj", "chuja", "chujek", "chujem", "chujnia", "chujowa", "chujowe", "chujowy", "chuju", "cipa", "cipe", "cipeczka", "cipie", "cipsko", "cipunia", "cipą", "cipę", "dojebac", "dojebal", "dojebala", "dojebalam", "dojebalem", "dojebać", "dojebał", "dojebała", "dojebałam", "dojebałem", "dojebie", "dojebie", "dojebię", "dopieprzac", "dopieprzać", "dopierdala", "dopierdalac", "dopierdalajacy", "dopierdalający", "dopierdalal", "dopierdalala", "dopierdalać", "dopierdalał", "dopierdalała", "dopierdole", "dopierdoli", "dopierdoli", "dopierdolic", "dopierdolil", "dopierdolić", "dopierdolił", "dopierdolę", "huj", "huja", "huje", "hujek", "hujem", "hujnia", "huju", "jebac", "jebak", "jebaka", "jebal", "jebal", "jebana", "jebana", "jebancu", "jebane", "jebanej", "jebani", "jebanka", "jebankiem", "jebanko", "jebany", "jebanych", "jebanym", "jebanymi", "jebanymi", "jebaną", "jebać", "jebał", "jebał", "jebańcu", "jebcie", "jebia", "jebia", "jebiaca", "jebiacego", "jebiacej", "jebiacy", "jebie", "jebie", "jebią", "jebią", "jebiąca", "jebiącego", "jebiącej", "jebiący", "jebię", "jebliwy", "jebna", "jebnac", "jebnal", "jebnać", "jebnela", "jebnie", "jebnij", "jebną", "jebnąc", "jebnąć", "jebnął", "jebnęła", "jebut", "koka", "kokaina", "koorwa", "kurestwo", "kurew", "kurewska", "kurewska", "kurewski", "kurewski", "kurewskiej", "kurewsko", "kurewską", "kurewstwo", "kurwa", "kurwaa", "kurwach", "kurwami", "kurwami", "kurwe", "kurwiarz", "kurwic", "kurwica", "kurwidołek", "kurwie", "kurwik", "kurwiki", "kurwil", "kurwiska", "kurwiszcze", "kurwiszon", "kurwiszona", "kurwiszonem", "kurwiszony", "kurwiący", "kurwić", "kurwił", "kurwo", "kurwy", "kurwą", "kurwę", "kutas", "kutasa", "kutasach", "kutasami", "kutasem", "kutasie", "kutasow", "kutasy", "kutasów", "kórwa", "marihuana", "marihuaną", "matkojebca", "matkojebca", "matkojebcach", "matkojebcami", "matkojebcy", "matkojebcą", "mefedron", "nabarłożyć", "najebac", "najebal", "najebala", "najebana", "najebane", "najebany", "najebaną", "najebać", "najebał", "najebała", "najebia", "najebie", "najebią", "naopierdalac", "naopierdalal", "naopierdalala", "naopierdalać", "naopierdalał", "naopierdalała", "naopierdalała", "napierdalac", "napierdalajacy", "napierdalający", "napierdalać", "napierdolic", "napierdolić", "nawpierdalac", "nawpierdalal", "nawpierdalala", "nawpierdalać", "nawpierdalał", "nawpierdalała", "obsrywac", "obsrywajacy", "obsrywający", "obsrywać", "odpieprzac", "odpieprzać", "odpieprzy", "odpieprzyl", "odpieprzyla", "odpieprzył", "odpieprzyła", "odpierdalac", "odpierdalajaca", "odpierdalajacy", "odpierdalająca", "odpierdalający", "odpierdalać", "odpierdol", "odpierdoli", "odpierdoli", "odpierdolic", "odpierdolil", "odpierdolila", "odpierdolić", "odpierdolił", "odpierdolił", "odpierdoliła", "opieprzający", "opierdala", "opierdalac", "opierdalajacy", "opierdalający", "opierdalać", "opierdol", "opierdola", "opierdoli", "opierdolic", "opierdolić", "opierdolą", "piczka", "pieprzniety", "pieprznięty", "pieprzony", "pierdel", "pierdlu", "pierdol", "pierdola", "pierdola", "pierdolaca", "pierdolacy", "pierdole", "pierdolec", "pierdolenie", "pierdoleniem", "pierdoleniu", "pierdoli", "pierdolic", "pierdolicie", "pierdolil", "pierdolila", "pierdolisz", "pierdolić", "pierdolił", "pierdoliła", "pierdolnac", "pierdolnal", "pierdolnela", "pierdolnie", "pierdolniety", "pierdolnij", "pierdolnik", "pierdolnięty", "pierdolnięty", "pierdolnąć", "pierdolnął", "pierdolnęła", "pierdolona", "pierdolone", "pierdolony", "pierdolą", "pierdolą", "pierdoląca", "pierdolący", "pierdolę", "pierdołki", "pierdziec", "pierdzieć", "pierdzący", "pizda", "pizde", "pizdnac", "pizdnąć", "pizdu", "pizdzie", "pizdą", "pizdę", "piździe", "podpierdala", "podpierdalac", "podpierdalajacy", "podpierdalający", "podpierdalać", "podpierdoli", "podpierdolic", "podpierdolić", "pojeb", "pojeba", "pojebac", "pojebalo", "pojebami", "pojebanego", "pojebanemu", "pojebani", "pojebani", "pojebany", "pojebanych", "pojebanym", "pojebanymi", "pojebać", "pojebem", "popierdala", "popierdalac", "popierdalać", "popierdoleni", "popierdoli", "popierdolic", "popierdolić", "popierdolone", "popierdolonego", "popierdolonemu", "popierdolony", "popierdolonym", "porozpierdala", "porozpierdalac", "porozpierdalać", "poruchac", "poruchać", "przejebac", "przejebane", "przejebać", "przepierdala", "przepierdalac", "przepierdalajaca", "przepierdalajacy", "przepierdalająca", "przepierdalający", "przepierdalać", "przepierdolic", "przepierdolić", "przyjebac", "przyjebal", "przyjebala", "przyjebali", "przyjebać", "przyjebał", "przyjebała", "przyjebie", "przypieprzac", "przypieprzajaca", "przypieprzajacy", "przypieprzająca", "przypieprzający", "przypieprzać", "przypierdala", "przypierdalac", "przypierdalajacy", "przypierdalający", "przypierdalać", "przypierdoli", "przypierdolic", "przypierdolić", "qrwa", "rozjebac", "rozjebać", "rozjebała", "rozjebie", "rozjebią", "rozpierdala", "rozpierdalac", "rozpierdalać", "rozpierdole", "rozpierdoli", "rozpierdolic", "rozpierdolić", "rozpierducha", "skurwiel", "skurwiela", "skurwielem", "skurwielu", "skurwić", "skurwysyn", "skurwysyna", "skurwysynem", "skurwysynow", "skurwysynski", "skurwysynstwo", "skurwysynu", "skurwysyny", "skurwysynów", "skurwysyński", "skurwysyństwo", "spieprza", "spieprzac", "spieprzaj", "spieprzaja", "spieprzajaca", "spieprzajacy", "spieprzajcie", "spieprzają", "spieprzająca", "spieprzający", "spieprzać", "spierdala", "spierdalac", "spierdalajacy", "spierdalający", "spierdalal", "spierdalala", "spierdalalcie", "spierdalać", "spierdalał", "spierdalała", "spierdola", "spierdoli", "spierdolic", "spierdolić", "spierdoliła", "spierdoliło", "spierdolą", "srac", "sraj", "srajac", "srajacy", "srając", "srający", "srać", "sukinsyn", "sukinsynom", "sukinsynow", "sukinsynowi", "sukinsyny", "sukinsynów", "udupić", "ujebac", "ujebal", "ujebala", "ujebana", "ujebany", "ujebać", "ujebał", "ujebała", "ujebie", "upierdala", "upierdalac", "upierdalać", "upierdola", "upierdoleni", "upierdoli", "upierdoli", "upierdolic", "upierdolić", "upierdolą", "wjebac", "wjebać", "wjebia", "wjebie", "wjebiecie", "wjebiemy", "wjebią", "wkurwi", "wkurwi", "wkurwia", "wkurwia", "wkurwia", "wkurwiac", "wkurwiacie", "wkurwiacie", "wkurwiajaca", "wkurwiajacy", "wkurwiają", "wkurwiająca", "wkurwiający", "wkurwial", "wkurwiali", "wkurwiać", "wkurwiał", "wkurwic", "wkurwic", "wkurwicie", "wkurwimy", "wkurwią", "wkurwić", "wkurwić", "wpierdalac", "wpierdalajacy", "wpierdalający", "wpierdalać", "wpierdol", "wpierdolic", "wpierdolić", "wpizdu", "wyjebac", "wyjebac", "wyjebali", "wyjebać", "wyjebał", "wyjebała", "wyjebały", "wyjebia", "wyjebie", "wyjebie", "wyjebiecie", "wyjebiemy", "wyjebiesz", "wyjebią", "wyjebka", "wypieprza", "wypieprzac", "wypieprzal", "wypieprzala", "wypieprzać", "wypieprzał", "wypieprzała", "wypieprzy", "wypieprzyl", "wypieprzyla", "wypieprzył", "wypieprzyła", "wypierdal", "wypierdala", "wypierdalac", "wypierdalaj", "wypierdalal", "wypierdalala", "wypierdalać", "wypierdalać", "wypierdalał", "wypierdalała", "wypierdola", "wypierdoli", "wypierdolic", "wypierdolicie", "wypierdolil", "wypierdolila", "wypierdolili", "wypierdolimy", "wypierdolić", "wypierdolił", "wypierdoliła", "wypierdolą", "zajebac", "zajebali", "zajebana", "zajebane", "zajebani", "zajebany", "zajebanych", "zajebanym", "zajebanymi", "zajebać", "zajebała", "zajebia", "zajebial", "zajebiala", "zajebiał", "zajebie", "zajebią", "zapieprza", "zapieprzy", "zapieprzy", "zapieprzyc", "zapieprzycie", "zapieprzyl", "zapieprzyla", "zapieprzymy", "zapieprzysz", "zapieprzyć", "zapieprzył", "zapieprzyła", "zapieprzą", "zapierdala", "zapierdalac", "zapierdalaj", "zapierdalaja", "zapierdalajacy", "zapierdalajcie", "zapierdalający", "zapierdalala", "zapierdalali", "zapierdalać", "zapierdalał", "zapierdalała", "zapierdola", "zapierdoli", "zapierdolic", "zapierdolil", "zapierdolila", "zapierdolić", "zapierdolił", "zapierdoliła", "zapierdolą", "zapierniczający", "zapierniczać", "zasranym", "zasrać", "zasrywający", "zasrywać", "zesrywający", "zesrywać", "zjeb", "zjebac", "zjebal", "zjebala", "zjebali", "zjebana", "zjebać", "zjebał", "zjebała", "zjebią", "zjebka", "zjeby", "śmierdziel");

    private ChatUtil() {

    }

    public static void ClearChat(@NotNull Player player) {
        for (int i = 0; i < 100; i++) player.sendMessage(Component.space());
    }

    public static @NotNull String CenterText(@NotNull String text, int lineLength) {
        final StringBuilder builder = new StringBuilder(text);
        final int distance = (lineLength - text.length()) / 2;
        for (int i = 0; i < distance; i += 1) {
            builder.insert(0, ' ');
            builder.append(' ');
        }
        return builder.toString();
    }

    public static @NotNull String CenterMOTDLine(final String line) {
        return CenterText(line, 53);
    }

    /**
     * @deprecated Old solution
     * @param ticks The ticks (20t = 1s)
     * @return The formatted ticks to time string
     */
    @Deprecated
    public static @NotNull String GetTicksToTime(final int ticks) {
        if (ticks > 20 * 60) {
            if (ticks > 20 * 60 * 60) {
                NumberFormat nf = NumberFormat.getNumberInstance(java.util.Locale.US);
                nf.setMaximumFractionDigits(2);
                if (ticks > 20 * 60 * 60 * 24) {
                    return (nf.format((double) ticks / 20 / 60 / 60 / 24)) + "d";
                } else {
                    return (nf.format((double) ticks / 20 / 60 / 60)) + "h";
                }
            } else {
                return (ticks / 20 / 60) + "m";
            }
        } else {
            return (ticks / 20) + "s";
        }
    }

    /**
     * @deprecated Use {@link net.kyori.adventure.text.TranslatableComponent}
     * @param biome The biome
     * @return The name
     */
    @Deprecated
    public static String Format(final @NotNull Biome biome) {
        String materialName = biome.toString();
        materialName = materialName.replaceAll("_", " ");
        materialName = materialName.toLowerCase();
        return WordUtils.capitalizeFully(materialName);
    }

    /**
     * @deprecated Use {@link net.kyori.adventure.text.TranslatableComponent}
     * @param name The string
     * @return The name
     */
    @Deprecated
    public static String Format(final String name) {
        String materialName = name;
        materialName = materialName.replaceAll("_", " ");
        materialName = materialName.toLowerCase();
        return WordUtils.capitalizeFully(materialName);
    }

    /**
     * @deprecated Use {@link net.kyori.adventure.text.TranslatableComponent}
     * @param material The item type
     * @return The name
     */
    @Deprecated
    public static String Format(final @NotNull Material material) {
        String materialName = material.toString();
        materialName = materialName.replaceAll("_", " ");
        materialName = materialName.toLowerCase();
        return WordUtils.capitalizeFully(materialName);
    }

    /**
     * @deprecated Use {@link net.kyori.adventure.text.TranslatableComponent}
     * @param material The entity type
     * @return The name
     */
    @Deprecated
    public static String Format(final @NotNull EntityType material) {
        String materialName = material.toString();
        materialName = materialName.replaceAll("_", " ");
        materialName = materialName.toLowerCase();
        return WordUtils.capitalizeFully(materialName);
    }

    public static @NotNull String formatNumber(final double number) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pl-PL"));
        nf.setMaximumFractionDigits(2);
        return nf.format(number);
    }

    public static @NotNull String formatMoney(final double money) {
        NumberFormat nf = (money > 1000) ? NumberFormat.getCompactNumberInstance(java.util.Locale.US, NumberFormat.Style.SHORT) : NumberFormat.getNumberInstance(java.util.Locale.US);
        nf.setMaximumFractionDigits(2);
        return nf.format(money) + "$";
    }

    public static @NotNull String formatWPLN(double cost) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pl-PL"));
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(cost) + " PLN";
    }

    public static @NotNull String formatCoins(final int coins) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pl-PL"));
        nf.setMaximumFractionDigits(2);
        return nf.format(coins) + " ⛃";
    }

    public static @NotNull String formatCountingDown(final LocalDateTime expire) {
        final LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(expire)) return "już";

        LocalDateTime tempDate = now;

        long years = tempDate.until(expire, ChronoUnit.YEARS);
        tempDate = tempDate.plusYears(years);

        long months = tempDate.until(expire, ChronoUnit.MONTHS);
        tempDate = tempDate.plusMonths(months);

        long days = tempDate.until(expire, ChronoUnit.DAYS);
        tempDate = tempDate.plusDays(days);


        long hours = tempDate.until(expire, ChronoUnit.HOURS);
        tempDate = tempDate.plusHours(hours);

        long minutes = tempDate.until(expire, ChronoUnit.MINUTES);
        tempDate = tempDate.plusMinutes(minutes);

        long seconds = tempDate.until(expire, ChronoUnit.SECONDS);

        return MessageFormat.format("{0}d {1}h {2}m {3}s", days, hours, minutes, seconds);
    }
}