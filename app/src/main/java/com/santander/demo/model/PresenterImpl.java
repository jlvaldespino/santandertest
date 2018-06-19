package com.santander.demo.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.santander.demo.activities.AppController;
import com.santander.demo.activities.LoginActivity;
import com.santander.demo.presenter.LoginPresenter;
import com.santander.demo.view.LoginView;

import org.json.JSONException;
import org.json.JSONObject;

public class PresenterImpl implements LoginPresenter {

    LoginView mloginView;
    private static final String TAG = LoginActivity.class.getSimpleName();


    public PresenterImpl(LoginView loginView){
        this.mloginView=loginView;
    }
    @Override
    public void performLogin(String usuario, String password) {

        if(TextUtils.isEmpty(usuario) || TextUtils.isEmpty(password)){
            mloginView.loginValidacion();
        }else{
            loginUser(usuario,password);
        }
    }

    private void loginUser(final String name,final String password) {
        String tag_string_req = "req_register";
        String url="https://canditossoft.000webhostapp.com/services/searchuser.php?user_name="+name+"&user_pass="+password;


        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.d(TAG, "Registro responde: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);

                    String obj= jObj.getString("dto");


                    if(obj!="null"){
                        JSONObject dtoObj = jObj.getJSONObject("dto");
                        String id = dtoObj.getString("id_u");
                        mloginView.loginExitoso();
                    }else{
                        JSONObject errorObj = jObj.getJSONObject("error");
                        String id = errorObj.getString("errCode");
                        String errorMsj = errorObj.getString("errMsg");
                        mloginView.loginError(errorMsj);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {


            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registro Error: " + error.getMessage());
                mloginView.loginError(error.getMessage());
            }
        }) {



        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }




}
