package com.example.aarushi.myproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    /**
     * This method returns an int value
     *
     * @param str
     */
    public static int getInt(String str) {
        int value = 0;

        if (str == null || str.equalsIgnoreCase(""))
            return value;

        str = str.replace(",", "");

        if (str.contains("."))
            return (int) getFloat(str);

        try {
            value = Integer.parseInt(str);
        } catch (Exception e) {
            value = (int) getFloat(str);
            // LogUtils.error("StringUtils", "Error occurred while parsing as integer"+e.toString());
        }
        return value;
    }

    /**
     * This method returns an int value
     *
     * @param str
     */
    public static boolean getBoolean(String str) {
        boolean value = false;

        if (str == null || str.equalsIgnoreCase(""))
            return value;

        try {
            value = Boolean.parseBoolean(str);
        } catch (Exception e) {
            // LogUtils.error("StringUtils", "Error occurred while parsing as boolean"+e.toString());
        }
        return value;
    }


    public String getImageNameFromUrl(String url) {
        String imgName = "";

        if (url.contains("*/")) {
            imgName = url.substring(url.indexOf("/img/") + 5);
            imgName = imgName.replace("/", "");
            imgName = imgName.replace("*", "");
        } else {
            imgName = url.substring(url.lastIndexOf("/"));

            imgName = imgName.substring(0, imgName.indexOf("."));
        }

        return imgName;
    }

    /**
     * This method returns an String value
     *
     * @param str
     */
    public static String getString(boolean str) {
        String value = "";
        try {
            value = String.valueOf((str));
        } catch (Exception e) {
            // LogUtils.error("StringUtils", "Error occurred while getString"+e.toString());
        }
        return value;
    }

    /**
     * This method returns an String value
     *
     * @param str
     */
    public static String getString(int str) {
        String value = "";
        try {
            value = String.valueOf((str));
        } catch (Exception e) {
            //	LogUtils.error("StringUtils", "Error occurred while getString"+e.toString());
        }
        return value;
    }

    /**
     * This method returns boolean if given String is a valid email
     *
     * @param string
     */
    public static boolean isValidEmail(String string) {
        final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        );
        Matcher matcher = EMAIL_ADDRESS_PATTERN.matcher(string);
        boolean value = matcher.matches();
        return value;
    }

    /**
     * This method returns float value
     *
     * @param string
     */
    public static float getFloat(String string) {
        float value = 0f;

        if (string == null || string.equalsIgnoreCase("") || string.equalsIgnoreCase("."))
            return value;

        string = string.replace(",", "");

        try {
            value = Float.parseFloat(string);
        } catch (Exception e) {
            //	LogUtils.error("StringUtils", "Error occurred while getFloat"+e.toString());
        }

        return value;
    }

    /**
     * This method returns long value
     *
     * @param string
     */
    public static long getLong(String string) {
        long value = 0;

        if (string == null || string.equalsIgnoreCase(""))
            return value;

        string = string.replace(",", "");

        try {
            value = Long.parseLong(string);
        } catch (Exception e) {
            // LogUtils.error("StringUtils", "Error occurred while getLong"+e.toString());
        }

        return value;
    }

    /**
     * This method returns int value
     *
     * @param str
     */
    public static int toInt(String str) {
        int value = -1;

        if (str == null || str.equalsIgnoreCase(""))
            return value;

        try {
            value = Integer.parseInt(str);
        } catch (Exception e) {
            // LogUtils.error("StringUtils", "Error occurred while toInt"+e.toString());
        }
        return value;
    }

    /**
     * This method returns a String value
     *
     * @param checkString
     */
    public static String checkString(String checkString) {
        return checkString != null ? checkString : "";
    }

    public static String setSuperScriptForNumber(int num) {
        String supStr = num + "";

        switch (num) {
            case 1:
                supStr += "<sup><small>st</small></sup>";
                break;
            case 2:
                supStr += "<sup><small>nd</small></sup>";
                break;
            case 3:
                supStr += "<sup><small>rd</small></sup>";
                break;
            default:
                supStr += "<sup><small>th</small></sup>";
        }

        return supStr;
    }

    public static String getDateOfTimestamp(long timeStamp) {
        java.text.DateFormat objFormatter = new SimpleDateFormat("MM-dd-yyy");

        Calendar objCalendar = Calendar.getInstance();

        objCalendar.setTimeInMillis(timeStamp * 1000);//edit
        String result = objFormatter.format(objCalendar.getTime());
        objCalendar.clear();
        return result;

    }
    public static boolean isOnline(Context context) {
        if (!CheckIfNull(context)) {
            ConnectivityManager conMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

            if (netInfo == null || !netInfo.isConnected()
                    || !netInfo.isAvailable()) {

                return false;
            }
        }

        return true;
    }

    public static void showToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }
    public static <T> boolean CheckIfNull(T objectToCheck) {
        return objectToCheck == null ? true : false;
    }

    public static void savePreferencesString(Context context, String key,
                                             String value) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }


    public static void write(String data, BufferedWriter out) throws IOException {
        out.write(data);
        out.newLine();
    }


    /**
     * This method returns an int value
     *
     * @param str
     */
    public static double getDouble(String str) {
        double value = 0;

        if (str == null || str.equalsIgnoreCase(""))
            return value;

        else if (str.contains(","))
            str = str.replace(",", "");

        try {
            value = Double.parseDouble(str);
        } catch (Exception e) {
            // LogUtils.error("StringUtils", "Error occurred while parsing as integer"+e.toString());
        }
        return value;
    }

    /**
     * method to convert Stream to String
     *
     * @param inputStream
     * @return String
     * @throws IOException
     */
    public static String convertStreamToString(InputStream inputStream) throws IOException {
        //Initialize variables
        String responce = "";

        if (inputStream != null) {
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                //Reader
                Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    //writing
                    writer.write(buffer, 0, n);
                }
                responce = writer.toString();
                writer.close();
            } finally {
                //closing InputStream
                inputStream.close();
            }

            return responce;
        } else {
            return "";
        }
    }

    public static String getUniqueUUID() {
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        return randomUUIDString;
    }

    /**
     * This method returns an String value
     *
     * @param floatValue
     */
    public static String getStringFromDouble(double floatValue) {
        String value = "";
        try {
            value = String.valueOf(floatValue);
        } catch (Exception e) {
        }

        if (!value.equalsIgnoreCase("")) {
            if (value.split("\\.").length == 1)
                return value + ".00";
            else {
                if (value.split("\\.")[1].length() == 2)
                    return value;
                else if (value.split("\\.")[1].length() == 1)
                    return value + "0";
                else if (value.split("\\.")[1].length() > 2)
                    return value.split("\\.")[0] + "." + value.split("\\.")[1].substring(0, 2);
            }
        }
        return value;
    }

    public static String removeSpecialCharacters(String specialChar) {
        String formatString = "";
        try {
            formatString = specialChar.replaceAll("[\\p{P}\\p{S}\\ ]", "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return formatString;
    }

    public final static boolean isValidEmails(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    /**
     * Regex to validate the mobile number
     * mobile number should be of 10 digits length
     *
     * @param mobile
     * @return
     */
    public static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }
}
