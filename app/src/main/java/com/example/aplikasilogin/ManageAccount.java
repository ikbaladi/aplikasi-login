package com.example.aplikasilogin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class ManageAccount extends AppCompatActivity {

    EditText edEmail, edUsername, edPassword, edUlangPassword;
    Spinner spRole;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        edEmail = findViewById(R.id.ed_email);
        edUsername = findViewById(R.id.ed_username);
        edPassword = findViewById(R.id.ed_password);
        edUlangPassword = findViewById(R.id.ed_ulangpassword);
        spRole = findViewById(R.id.sp_role);
        progressBar = findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.INVISIBLE);
    }

    // implementasi event onClick pada Button yaitu onCreateAccount()
    public void onCreateAccount (View view) {

        //Mengambil data inputan user dari kompomen view dan menyimpan dalam variabel

        String email = edEmail.getText().toString();
        String password = edPassword.getText().toString();
        String ulangPassword = edUlangPassword.getText().toString();
        String username = edUsername.getText().toString();
        String role = spRole.getSelectedItem().toString();

        // Melakukan pengecekan nilai inputan komponen sebelum dipros es untuk menjalankan fungsi createAccount

        if (email.isEmpty() || password.isEmpty() || ulangPassword.isEmpty() || username.isEmpty() || role.isEmpty()) {
            // Akan dijalankan apabila terdapat field yang kosong
            Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_LONG).show();
        } else {
            // Akan dijalankan apabila semua field sudah terisi
            // pertama akan menampilkan progressbar kemudian memanggi 1 method Create Account

            progressBar.setVisibility(View.VISIBLE);
            createAccount(email, username, password, role);
        }
    }

    // Membuat method create Account dengan parameter data inputan email, username, password dan role
    private void createAccount(String email, String username, String password, String role) {

        // Menjalankan method POST menggunakan fungsi Lib AndroidNetworking dan url sesuai dengan endpoint.CREATELOGIN
        AndroidNetworking.post(Endpoint.CREATE_LOGIN)
            .addBodyParameter("email", email)
            .addBodyParameter("username", username)
            .addBodyParameter("password", password)
            .addBodyParameter("role", role)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {

                @Override
                public void onResponse(JSONObject response) {
                    // Akan dieksekusi Jika berhasil menjalankan method POST
                    // membuat progresbar menjadi tidak terlihat
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.d("ManageAccount", "Create Acccount:" + response);

                    try {
                        // mengambil nilai respponse API dengan key "message"
                        String result = response.getString("message");

                        // Cek nilai response API
                        if (result.contains("login data successfully added")){
                            new AlertDialog.Builder(ManageAccount.this)
                                    .setMessage("Berhasil Menambahkan Data")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int which){
                                            dialog.dismiss();
                                            edEmail.getText().clear();
                                            edUsername.getText().clear();
                                            edPassword.getText().clear();
                                            edUlangPassword.getText().clear();
                                        }
                                    }).show();
                        } else if (result.contains("required field is empty")) {
                            progressBar.setVisibility(View.INVISIBLE);
                            new AlertDialog.Builder(ManageAccount.this)
                                    .setMessage("Gagal Menambahkan Data")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int which){
                                            dialog.dismiss();
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onError(ANError anError) {
                    Log.d("ManageAccount", ": Create Account "+ anError.getErrorBody());
                }
            });
    }
}