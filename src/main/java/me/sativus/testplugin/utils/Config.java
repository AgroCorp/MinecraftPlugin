package me.sativus.testplugin.utils;

public final class Config {
    public static String host;
    public static int port;
    public static String database;
    public static String username;
    public static String password;
    
    public static String emailHost;
    public static int emailPort;
    public static String emailUsername;
    public static String emailPassword;
    public static String emailFrom;

    public static Double onlineTimeMoney;
    public static Integer onlineTimeMinutes;


    public static enum Daytimes {
        DAY(1000L),
        NIGHT(13000L),
        MIDNIGHT(18000L),
        NOON(6000L);

        private final long time;

        Daytimes(long time) {
            this.time = time;
        }
        public long getTime() {
            return time;
        }
    }

    public static enum Weathers {
        CLEAR,
        RAIN,
        THUNDER
    }

}
