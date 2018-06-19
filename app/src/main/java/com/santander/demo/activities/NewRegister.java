package com.santander.demo.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import android.support.v4.app.ActivityCompat;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.santander.demo.R;
import com.santander.demo.model.PresenterRegisterImpl;
import com.santander.demo.presenter.RegisterPresenter;
import com.santander.demo.view.RegisterView;

import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class NewRegister extends Activity implements View.OnClickListener,RegisterView {

    private static final String TAG = NewRegister.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;
    EditText etNacimiento,etUsuario,etPassword,etEmail,etName,etFirstName,etSecondName,etDateBirday;
     Spinner spSex,spCountry,spState;

    double longitudeGPS, latitudeGPS;
    TextView longitudeValueGPS, latitudeValueGPS;
    Button btRegister;
    private ProgressDialog pDialog;
    RegisterPresenter mRegisterPresenter;
    LocationManager locationManager;
    private ProgressBar mProgressBar;
    private SQLiteHandler db;
    String android_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_register);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        etNacimiento = (EditText) findViewById(R.id.etNacimiento);
        etUsuario =(EditText) findViewById(R.id.editTextUserName);
        etPassword =(EditText) findViewById(R.id.editTextPassword);
        etEmail =(EditText) findViewById(R.id.editTextEmail);
        etName=(EditText) findViewById(R.id.editTextFirstName);
        etFirstName =(EditText) findViewById(R.id.editTextFirstName);
        etSecondName =(EditText) findViewById(R.id.editTextLastNameM);
        etDateBirday=(EditText) findViewById(R.id.etNacimiento);

        spSex=(Spinner) findViewById(R.id.textInputSex);
        spState=(Spinner) findViewById(R.id.textInputEstado);
        spCountry=(Spinner) findViewById(R.id.textInputPais);




        longitudeValueGPS = (TextView) findViewById(R.id.textViewLongitud);
        latitudeValueGPS = (TextView) findViewById(R.id.textViewLatitud);

        btRegister=(Button) findViewById(R.id.buttonRegister);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);


        etNacimiento.setOnClickListener(this);
        btRegister.setOnClickListener(this);

        db = new SQLiteHandler(getApplicationContext());

        mRegisterPresenter= new PresenterRegisterImpl(NewRegister.this);



        ActivityCompat.requestPermissions(this, new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION },
                1);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        checkLocation();
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 2 * 20 * 1000, 10, locationListenerGPS);


    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etNacimiento:
                showDatePickerDialog();
                break;
            case R.id.buttonRegister:
                bRegister();
                break;

        }
    }

    private void bRegister(){
        String username=etUsuario.getText().toString();
        String password=etPassword.getText().toString();
        String email=etEmail.getText().toString();
        mProgressBar.setVisibility(View.VISIBLE);
        mRegisterPresenter.performRegister(username,password,email);

    }



    private void showDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");

    }

    @Override
    public void regiterValidacion() {
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), "Ingresa el usuario el password y email", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void registerExitoso() {
        mProgressBar.setVisibility(View.GONE);
        String sexo=(String) spSex.getSelectedItem();
        sexo=encode(android_id,sexo);
        String estado=(String) spState.getSelectedItem();
        estado=encode(android_id,estado);
        String pais=(String) spCountry.getSelectedItem();
        pais=encode(android_id,pais);
        String log= longitudeValueGPS.getText().toString();
        String lat= latitudeValueGPS.getText().toString();
        String user= etUsuario.getText().toString();
        user=encode(android_id,user);
        String pass= etPassword.getText().toString();
        pass=encode(android_id,pass);
        String email = etEmail.getText().toString();
        email=encode(android_id,email);
        String nombre =  etName.getText().toString();
        nombre=encode(android_id,nombre);
        String ape_pat = etFirstName.getText().toString();
        ape_pat=encode(android_id,ape_pat);
        String ape_mat = etSecondName.getText().toString();
        ape_mat=encode(android_id,ape_mat);
        String fecha=etNacimiento.getText().toString();
        fecha=encode(android_id,fecha);
        db.addUser(user,email,nombre, ape_pat,ape_mat,fecha,sexo,pais,estado,lat,log, "");
        Toast.makeText(getApplicationContext(), "Se registro correctamente", Toast.LENGTH_SHORT).show();
        Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intentLogin);

    }

    @Override
    public void registerError(String error) {
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), "Error:"+error, Toast.LENGTH_SHORT).show();

    }


    public class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

             return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            if (month < 10){
                etNacimiento.setText(day+"/0"+month+"/"+year);
            }else{
                etNacimiento.setText(day+"/"+month+"/"+year);
            }

        }
    }
    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Activar Localizacion")
                .setMessage("Su ubicación esta desactivada.\npor favor active su ubicación " +
                        "usa esta app")
                .setPositiveButton("Configuración de ubicación", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    private final LocationListener locationListenerGPS = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeGPS = location.getLongitude();
            latitudeGPS = location.getLatitude();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    longitudeValueGPS.setText(longitudeGPS + "");
                    latitudeValueGPS.setText(latitudeGPS + "");
                    Toast.makeText(NewRegister.this, "GPS actualizado", Toast.LENGTH_SHORT).show();
                }
            });
        }
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }
        @Override
        public void onProviderDisabled(String s) {
        }
    };

    public String encode(String key, String data) {
        try {

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            return new String(Hex.encodeHex(sha256_HMAC.doFinal(data.getBytes("UTF-8"))));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
