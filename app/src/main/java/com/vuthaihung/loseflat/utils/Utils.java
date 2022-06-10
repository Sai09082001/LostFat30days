package com.vuthaihung.loseflat.utils;

import com.vuthaihung.loseflat.data.model.DayHistoryModel;
import com.vuthaihung.loseflat.data.shared.AppSettings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static List<DayHistoryModel> getCurrentWeek() {
        ArrayList<DayHistoryModel> result = new ArrayList<>();
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.set(Calendar.DAY_OF_WEEK, 1);

        int first = AppSettings.getInstance().getFirstDayOfWeek();
        if (first == Calendar.SATURDAY) {
            cal.add(Calendar.DAY_OF_YEAR, -1);
        } else if (first == Calendar.MONDAY) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        for (int i = 0; i < 7; i++) {
            result.add(new DayHistoryModel(cal));
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        return result;
    }

    public static String convertStringTime(int value) {
        int mins = value / 60;
        int seconds = value % 60;
        return String.format("%02d:%02d", mins, seconds);
    }

    public static float convertKgToLbs(float value) {
        return value * 2.20462f;
    }

    public static float convertLbsToKg(float value) {
        return value / 2.20462f;
    }

    public static float convertCmToFt(float value) {
        return value / 30.48f;
    }

    public static float convertCmToInch(float value) {
        return value / 2.54f;
    }

    public static float convertFtToCm(float value) {
        float height = value * 30.48f;
        if (height >= 164.89f && height <= 165.1f) {
            height = 165f;
        }
        return height;
    }

    public static float convertInchToCm(float value) {
        return value * 2.54f;
    }

    public static float calculatorBMI(float height, float weight) {
        return weight / (height * height / 10000);
    }

    public static String formatWeight(float value) {
        return String.format(Locale.US, "%.02f", value);
    }

    public static String formatWaistline(float value) {
        return String.format(Locale.US, "%.02f", value);
    }

    public static String randomString(int len) {
        String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }
        return sb.toString();
    }

    public static int randomInt() {
        Random random = new Random();
        return random.nextInt(50000) + 101;
    }

    public static boolean isValidateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static String changeUpperString(String name){
        String capitalizedString="";
        if(!name.trim().equals("")){
            capitalizedString = name.substring(0,1).toUpperCase() + name.substring(1);
        }
        return capitalizedString;
    }
}
