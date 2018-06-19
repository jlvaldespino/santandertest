package com.santander.demo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.util.HashMap;

public class SessionManager {

    private static String TAG = SessionManager.class.getSimpleName();
    public static final String KEY_EMAIL = "email";
    
    public static final String KEY_NAME="name";
    

    
    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "SessionLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		user.put(KEY_NAME, pref.getString(KEY_NAME, null));
		
		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
		
		return user;
	}
    
    public void createLoginSession(String name,String email){
    	
    	editor.putBoolean(KEY_IS_LOGGEDIN, true);
    	
    	editor.putString(KEY_NAME, name);
    	
    	editor.putString(KEY_EMAIL, email);
    	
    	editor.commit();
    	
    }
    
    public void checkLogin(){
		if(!this.isLoggedIn()){
			Intent i = new Intent(_context, LoginActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			_context.startActivity(i);
		}
		
	}
    
    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "Session Modificada!");
    }
    public void logoutUser(){
        
        editor.clear();
        editor.commit();
         
        Intent i = new Intent(_context, LoginActivity.class);
         
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         
        _context.startActivity(i);
    }


    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}

