package com.wxr.gameh.constant;

public class Config {
    public static final boolean DEBUG = false;

    private static String baseUrl = "http://np.197734.com/api/";
    private static String debugBaseUrl = "http://np.197734.com/api/";
    public static String APPID = "?app_id=12";
    public static final String INIT_URL = getBaseUrl() + "index/init" + APPID;
    public static final String INDEX_URL = getBaseUrl() + "jump/index"+ APPID;
    public static final String JUMP_NUM_LOG = getBaseUrl() + "jump/jump_num_log"+ APPID;
    public static final String RECORD_URL = getBaseUrl() + "jump/user_jump_list"+ APPID;

    public static String getBaseUrl() {
        return (DEBUG ? debugBaseUrl : baseUrl);
    }
}
