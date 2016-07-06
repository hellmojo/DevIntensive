package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintensiveApplication;

import java.util.ArrayList;
import java.util.List;

public class PreferensesManager {

    private SharedPreferences mSharedPreferenses;
    private static final String[] USER_FIELDS = {ConstantManager.USER_PHONE_KEY, ConstantManager.USER_MAIL_KEY, ConstantManager.USER_VK_KEY, ConstantManager.USER_GIT_KEY, ConstantManager.USER_ABOUT_KEY};



    public PreferensesManager() {
        this.mSharedPreferenses = DevintensiveApplication.getsSharedPreferences();
    }

    public void saveUserProfileData(List<String> userFields){
        SharedPreferences.Editor editor = mSharedPreferenses.edit();
        for (int i = 0; i< USER_FIELDS.length;i++) {
            editor.putString(USER_FIELDS[i], userFields.get(i));
        }
        editor.apply();
    }
    public List<String> loadUserProfileData(){
        List<String> userFields = new ArrayList<>();
        userFields.add(mSharedPreferenses.getString(ConstantManager.USER_PHONE_KEY, "null"));
        userFields.add(mSharedPreferenses.getString(ConstantManager.USER_MAIL_KEY, "null"));
        userFields.add(mSharedPreferenses.getString(ConstantManager.USER_VK_KEY, "null"));
        userFields.add(mSharedPreferenses.getString(ConstantManager.USER_GIT_KEY, "null"));
        userFields.add(mSharedPreferenses.getString(ConstantManager.USER_ABOUT_KEY, "null"));
        return userFields;

    }

    public void SaveUserPhoto(Uri uri){
        SharedPreferences.Editor editor = mSharedPreferenses.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY, uri.toString());
        editor.apply();
    }
    public Uri loadUserPhoto(){

        return Uri.parse(mSharedPreferenses.getString(ConstantManager.USER_PHONE_KEY, "android.resource://com.softdesign.devintensive/drawable/userphoto" ));
    }
}