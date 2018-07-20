package com.hourstack.Utils;


import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    public static final String PREFERENCE_NAME = "com.hourstack";

    private Context context;
    private SharedPreferences sharedPreferences;

    private static final String KEY_AUTHTOKEN = "token";
    private static final String KEY_USER_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";

    private static final String KEY_FIRSTDAY = "hours_in_day1";
    private static final String KEY_SECONDDAY  = "hours_in_day2";
    private static final String KEY_THIRDDAY = "hours_in_day3";
    private static final String KEY_FOURTHDAY  = "hours_in_day4";
    private static final String KEY_FIFTHDAY  = "hours_in_day5";
    private static final String KEY_SIXTHTDAY  = "hours_in_day6";
    private static final String KEY_SEVENTHDAY = "hours_in_day7";
    private static final String KEY_DEFAULTALLOCATION= "default_allocation";

    private static final String KEY_CURRENT_WORKSPACE = "last_workspace_id";

    private static final String KEY_RUNNINGEVENTID = "eventid";
    private static final String KEY_STARTEDSECONDS= "startedseconds";
    private static final String KEY_ALLOCATEDSECONDS= "allocatedseconds";
    private static final String KEY_CURRENTSECONDS= "eventsecondscurrent";



    private static final String KEY_PROXIMITY= "proximity";
    private static final String KEY_TIMERREMINDER= "timerreminder";
    private static final String KEY_TIMERREMINDERTIME= "timerremindertime";
    private static final String KEY_OVERALLOCATION= "overallocation";
    private static final String KEY_TIMERLAT= "timerlat";
    private static final String KEY_TIMERLONG = "timerlong";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

    }


    public boolean isLogin() {
        return sharedPreferences.getString(KEY_NAME, "-1").equalsIgnoreCase("-1") ? false : true;
    }
    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, "");
    }

    public void setKeyUserId(String userId) {
        sharedPreferences.edit().putString(KEY_USER_ID, userId).apply();
    }

    public String getKeyAuthtoken() {
        return sharedPreferences.getString(KEY_AUTHTOKEN, "");
    }

    public void setKeyAuthtoken(String userId) {
        sharedPreferences.edit().putString(KEY_AUTHTOKEN, userId).apply();
    }

    public void setKeyName(String userId) {
        sharedPreferences.edit().putString(KEY_NAME, userId).apply();
    }

    public String getKeyName() {
        return sharedPreferences.getString(KEY_NAME, "");
    }


    public void setKeyEmail(String userId) {
        sharedPreferences.edit().putString(KEY_EMAIL, userId).apply();
    }

    public String getKeyEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    public void setKeyCurrentWorkspace(String userId) {
        sharedPreferences.edit().putString(KEY_CURRENT_WORKSPACE, userId).apply();
    }

    public String getKeyCurrentWorkspace() {
        return sharedPreferences.getString(KEY_CURRENT_WORKSPACE, "");
    }
    public void setKeyFirstday(String userId) {
        sharedPreferences.edit().putString(KEY_FIRSTDAY, userId).apply();
    }

    public String getKeyFirstday() {
        return sharedPreferences.getString(KEY_FIRSTDAY, "");
    }

    public void setKeySecondday(String userId) {
        sharedPreferences.edit().putString(KEY_SECONDDAY, userId).apply();
    }

    public String getKeySecondday() {
        return sharedPreferences.getString(KEY_SECONDDAY, "");
    }
    public void setKeyThirdday(String userId) {
        sharedPreferences.edit().putString(KEY_THIRDDAY, userId).apply();
    }

    public String getKeyThirdday() {
        return sharedPreferences.getString(KEY_THIRDDAY, "");
    }
    public void setKeyFourthday(String userId) {
        sharedPreferences.edit().putString(KEY_FOURTHDAY, userId).apply();
    }

    public String getKeyFourthday() {
        return sharedPreferences.getString(KEY_FOURTHDAY, "");
    }

    public void setKeyFifthday(String userId) {
        sharedPreferences.edit().putString(KEY_FIFTHDAY, userId).apply();
    }

    public String getKeyFifthday() {
        return sharedPreferences.getString(KEY_FIFTHDAY, "");
    }
    public void setKeySixthtday(String userId) {
        sharedPreferences.edit().putString(KEY_SIXTHTDAY, userId).apply();
    }

    public String getKeySixthtday() {
        return sharedPreferences.getString(KEY_SIXTHTDAY, "");
    }
    public void setKeySeventhday(String userId) {
        sharedPreferences.edit().putString(KEY_SEVENTHDAY, userId).apply();
    }

    public String getKeySeventhday() {
        return sharedPreferences.getString(KEY_SEVENTHDAY, "");
    }

    public void setKeyDefaultallocation(String userId) {
        sharedPreferences.edit().putString(KEY_DEFAULTALLOCATION, userId).apply();
    }

    public String getKeyDefaultallocation() {
        return sharedPreferences.getString(KEY_DEFAULTALLOCATION, "");
    }



    public void setKeyRunningeventid(String userId) {
        sharedPreferences.edit().putString(KEY_RUNNINGEVENTID, userId).apply();
    }

    public String getKeyRunningeventid(){
        return sharedPreferences.getString(KEY_RUNNINGEVENTID, "");
    }

    public void setKeyStartedseconds(String userId) {
        sharedPreferences.edit().putString(KEY_STARTEDSECONDS, userId).apply();
    }

    public String getKeyStartedseconds(){
        return sharedPreferences.getString(KEY_STARTEDSECONDS, "");
    }



    public void setKeyAllocatedseconds(String userId) {
        sharedPreferences.edit().putString(KEY_ALLOCATEDSECONDS, userId).apply();
    }

    public String getKeyAllocatedseconds(){
        return sharedPreferences.getString(KEY_ALLOCATEDSECONDS, "");
    }
    public void setKeyCurrentseconds(String userId) {
        sharedPreferences.edit().putString(KEY_CURRENTSECONDS, userId).apply();
    }

    public String getKeyCurrentseconds(){
        return sharedPreferences.getString(KEY_CURRENTSECONDS, "");
    }
    public void setKeyProximity(String userId) {
        sharedPreferences.edit().putString(KEY_PROXIMITY, userId).apply();
    }

    public String getKeyProximity(){
        return sharedPreferences.getString(KEY_PROXIMITY, "");
    }


    public void setKeyTimerreminder(String userId) {
        sharedPreferences.edit().putString(KEY_TIMERREMINDER, userId).apply();
    }

    public String getKeyTimerreminder(){
        return sharedPreferences.getString(KEY_TIMERREMINDER, "");
    }

    public void setKeyTimerremindertime(String userId) {
        sharedPreferences.edit().putString(KEY_TIMERREMINDERTIME, userId).apply();
    }

    public String getKeyTimerremindertime(){
        return sharedPreferences.getString(KEY_TIMERREMINDERTIME, "");
    }



    public void setKeyOverallocation(String userId) {
        sharedPreferences.edit().putString(KEY_OVERALLOCATION, userId).apply();
    }

    public String getKeyOverallocation(){
        return sharedPreferences.getString(KEY_OVERALLOCATION, "");
    }
    public void setKeyTimerlat(Double userId) {

        sharedPreferences.edit().putString(KEY_TIMERLAT, String.valueOf(userId)).apply();
    }

    public String getKeyTimerlat(){
        return sharedPreferences.getString(KEY_TIMERLAT, "");
    }
    public void setKeyTimerlong(Double userId) {

        sharedPreferences.edit().putString(KEY_TIMERLONG, String.valueOf(userId)).apply();
    }

    public String getKeyTimerlong(){
        return sharedPreferences.getString(KEY_TIMERLONG, "");
    }
    public boolean removeUserDetail() {

        return sharedPreferences.edit().clear().commit();



    }




}
