package com.softdesign.devintensive.data.managers;


import android.content.Context;

import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.DevintensiveApplication;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class DataManager {
    private static DataManager INSTANSE = null;

    private Context mContext;
    private PreferensesManager mPreferensesManager;
    private RestService mRestService;



    public DataManager() {
        this.mPreferensesManager = new PreferensesManager();
        this.mContext = DevintensiveApplication.getContext();
        this.mRestService = ServiceGenerator.createService(RestService.class);
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

    public Context getContext() {
        return mContext;
    }
    //region ==================== Network ===========
    public Call<UserModelRes> loginUser(UserLoginReq userLoginReq){
        return mRestService.loginUser(userLoginReq);
    }

    public Call<ResponseBody> savePhoto(RequestBody body, MultipartBody.Part file){
        return mRestService.savePhoto(body, file);
    }
    public Call<UserListRes> getUserList(){
        return mRestService.getUserList();
    }
    //endregion
}
