package com.asfursov.agrocom.settings;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.asfursov.agrocom.BuildConfig;
import com.asfursov.agrocom.R;
import com.asfursov.agrocom.state.AppData;


public class Settings {

    private static final Settings ourInstance = new Settings();

    private SharedPreferences settings;

    public static Settings getInstance() {
        refreshSettings();
        return ourInstance;
    }

    private static void refreshSettings() {
        ourInstance.getRefreshedSettings();
    }

    private void getRefreshedSettings() {
        Context context = AppData.getInstance().getContext();
        settings = context == null ? null : PreferenceManager.getDefaultSharedPreferences(context);
    }

    private Settings() {

    }

    public String getDBUrl() {
        StringBuilder URL = new StringBuilder();

        if (settings != null) {
            URL.append(settings.getString(
                    AppData.getInstance().getContext()
                            .getResources()
                            .getString(R.string.id_databaseUrl),
                    BuildConfig.BASE_URL));
        } else
            URL.append(BuildConfig.BASE_URL);

        if (!URL.substring(URL.length() - 1).contains("/")) URL.append("/");
        return URL.toString();

    }


    public String getDBUser() {

        if (settings != null) {

            return settings.getString(
                    AppData.getInstance().getContext()
                            .getResources()
                            .getString(R.string.id_httpUser),
                    BuildConfig.LOGIN);
        }
        return BuildConfig.LOGIN;
    }

    public String getDBPassword() {

        if (settings != null) {
            return settings.getString(
                    AppData.getInstance().getContext()
                            .getResources()
                            .getString(R.string.id_httpPassword),
                    BuildConfig.PASSWORD);
        }
        return BuildConfig.PASSWORD;
    }

}
//TODOL default values
//TODOL hide credentials
