package com.softdesign.devintensive.data.managers;


public class DataManager {
    private static DataManager INSTANSE = null;

    private PreferensesManager mPreferensesManager;
    public DataManager() {
        this.mPreferensesManager = new PreferensesManager();
    }

    public static DataManager getInstanse(){
        if(INSTANSE == null){
            INSTANSE = new DataManager();
        }
        return INSTANSE;
    }

    public PreferensesManager getmPreferensesManager() {
        return mPreferensesManager;
    }
}
