package com.example.aplikasilogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import static com.example.aplikasilogin.Endpoint.CEK_LOGIN;

public class MainActivity extends AppCompatActivity {
    EditText edUsername, edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edUsername = findViewById(R.id.ed_username);
        edPassword = findViewById(R.id.ed_password);
    }

    public void prosesLogin(View v) {
        String username = edUsername.getText().toString();
        String password = edPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "username dan password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            edUsername.setFocusable(true);
        } else {
            verifikasilogin(username, password);
        }
    }
    private void verifikasilogin (String username, String password){
        AndroidNetworking.post(CEK_LOGIN)
                .addBodyParameter("username", username)
                .addBodyParameter("password", password)
                .setPriority(Priority.MEDIUM)
                .setTag("Verifikasi Login")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray itemresponse = response.getJSONArray("result");
                    if (itemresponse.length()>0){
                        JSONObject value = itemresponse.getJSONObject(0);
                        String email = value.getString("email");
                        Toast.makeText(MainActivity.this, "Login berhasil " + email, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this,"Username atau password salah", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(ANError anError) {
                Log.d("MainActivity", ": Verifikasi Login "+ anError);
                Toast.makeText(MainActivity.this, "Error",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void buatAkun(View view) {
        // Beralih ke activity pendaftaran (misalnya, RegisterActivity)
        Intent intent = new Intent(this, ManageAccount.class);
        startActivity(intent);
    }
}