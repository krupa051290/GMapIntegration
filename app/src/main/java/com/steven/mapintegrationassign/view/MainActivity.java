package com.steven.mapintegrationassign.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.steven.mapintegrationassign.OkHttpConnection;
import com.steven.mapintegrationassign.R;
import com.steven.mapintegrationassign.model.LoginInfo;
import com.steven.mapintegrationassign.model.Utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {


    Button btnSignIn;
    EditText etUserName, etPassword;
    String loginResponse;

    int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    int REQUEST_PHONE_STATE = 2;
    CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        init();
        // Verify that all required contact permissions have been granted.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {
            // Contacts permissions have not been granted.

            requestWriteStoragePermission();
        }
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLoginRequest();
            }
        });
    }

    private void init()
    {
        etUserName = (EditText) findViewById(R.id.edUserName);
        etPassword = (EditText) findViewById(R.id.edPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
    }

    private void sendLoginRequest()
    {
        try
        {

            String username, password;
            username = etUserName.getText().toString().trim();
            password = etPassword.getText().toString().trim();

            if (username.equals("") || username.length() == 0)
            {
                Utils.showDialog(this, "Warning", "Please enter UserName");
            }
            else if (password.equals("") || password.length() == 0)
            {
                Utils.showDialog(this, "Warning", "Please enter Password");
            }
            else
            {
                getLoginResponse(username,password);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    private void getLoginResponse(String uname, String pass)
    {
    LoginInfo info = new LoginInfo(uname,pass, Utils.getVersionNo(this));
    Gson gson = new Gson();
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    RequestBody body = RequestBody.create(JSON, gson.toJson(info));
    Request request = new Request.Builder()
            .url("Test URL")
            .post(body)
            .build();

    OkHttpConnection.httpClient.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            } else {

                loginResponse = response.body().string();
                // do something wih the result
                Log.i("response: ", "" + loginResponse);

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,loginResponse, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,MapActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }
    });
}

    private void requestWriteStoragePermission() {
        // BEGIN_INCLUDE(contacts_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.
            Log.i("TAG",
                    "Displaying writeExternalStorage permission rationale to provide additional context.");

            // Display a SnackBar with an explanation and a button to trigger the request.
            Snackbar.make(coordinatorLayout, "Please grant permission to access database",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat
                                    .requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    })
                    .show();
        } else {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat .requestPermissions(MainActivity.this,  new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        }
        // END_INCLUDE(contacts_permission_request)

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PHONE_STATE){
            Log.i("TAG", "Received response for contact permissions request.");

            // We have requested multiple permissions for contacts, so all of them need to be
            // checked.

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                Log.i("TAG", "PHONE STATE permission has now been granted");
                Snackbar.make(coordinatorLayout,"PHONE STATE permission has now been granted",
                        Snackbar.LENGTH_SHORT).show();
            } else {
                Log.i("TAG", "PHONE STATE permission has now been granted");
                Snackbar.make(coordinatorLayout, "PHONE STATE permission has not been granted",
                        Snackbar.LENGTH_SHORT).show();

            }
            // END_INCLUDE(permission_result)

        }
        else if(requestCode == REQUEST_WRITE_EXTERNAL_STORAGE){
            Log.i("TAG", "Received response for contact permissions request.");

            // We have requested multiple permissions for contacts, so all of them need to be
            // checked.

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                Log.i("TAG", "Write storage permission has now been granted");
                Snackbar.make(coordinatorLayout,"Database permission has now been granted",
                        Snackbar.LENGTH_SHORT).show();
            } else {
                Log.i("TAG", "write storage permission has now been granted");
                Snackbar.make(coordinatorLayout, "Database permission has not been granted",
                        Snackbar.LENGTH_SHORT).show();

            }


        }
        else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
}
