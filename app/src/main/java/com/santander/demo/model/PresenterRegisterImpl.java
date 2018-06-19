package com.santander.demo.model;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.santander.demo.activities.AppController;
import com.santander.demo.activities.NewRegister;
import com.santander.demo.presenter.RegisterPresenter;
import com.santander.demo.view.RegisterView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PresenterRegisterImpl implements RegisterPresenter{
    RegisterView mRegisterView;
    private static final String TAG = NewRegister.class.getSimpleName();

    public PresenterRegisterImpl(RegisterView registerView){
        this.mRegisterView=registerView;
    }
    @Override
    public void performRegister(String username, String password, String email) {
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)){
            mRegisterView.regiterValidacion();

        }else{
            registerUser(username,email,password);
        }
    }
    private void registerUser(final String name, final String email,
                              final String password) {
        String tag_string_req = "req_register";
        String url="https://canditossoft.000webhostapp.com/services/insertuser.php?username="+name+"&userpass="+password+"&mail="+email;



        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.d(TAG, "Registro responde: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);


                    JSONObject dtoObj = jObj.getJSONObject("dto");
                    if(dtoObj!=null){
                        String id = dtoObj.getString("id");
                        mRegisterView.registerExitoso();
                    }else{
                        JSONObject errorObj = jObj.getJSONObject("error");
                        String id = errorObj.getString("errCode");
                        String errorMsj = errorObj.getString("errMsg");
                        mRegisterView.registerError(errorMsj);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {


            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registro Error: " + error.getMessage());
                mRegisterView.registerError(error.getMessage());
            }
        }) {



        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
