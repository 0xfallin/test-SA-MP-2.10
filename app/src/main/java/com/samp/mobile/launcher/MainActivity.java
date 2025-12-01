package com.samp.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

import com.samp.mobile.game.SAMP;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText editNick, editHost, editPort, editPassword;
    private Button btnSave, btnLaunch;

    private File settingsFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNick = findViewById(R.id.editNick);
        editHost = findViewById(R.id.editHost);
        editPort = findViewById(R.id.editPort);
        editPassword = findViewById(R.id.editPassword);
        btnSave = findViewById(R.id.btnSave);
        btnLaunch = findViewById(R.id.btnLaunch);

        // Path to settings.json
        File baseDir = new File(Environment.getExternalStorageDirectory(), "Android/data/com.samp.mobile/files/SAMP");
        if (!baseDir.exists()) baseDir.mkdirs();
        settingsFile = new File(baseDir, "settings.json");

        loadSettings();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSettings();
            }
        });

        btnLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSAMP();
            }
        });
    }

    private void loadSettings() {
        if (!settingsFile.exists()) return;

        try {
            FileInputStream fis = new FileInputStream(settingsFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }
            fis.close();

            String jsonStr = baos.toString("UTF-8");
            JSONObject json = new JSONObject(jsonStr);

            JSONObject client = json.getJSONObject("client");
            JSONObject server = client.getJSONObject("server");
            JSONObject settings = client.getJSONObject("settings");

            editHost.setText(server.getString("host"));
            editPort.setText(String.valueOf(server.getInt("port")));
            editPassword.setText(server.getString("password"));
            editNick.setText(settings.getString("nick_name"));

        } catch (IOException | JSONException e) {
            Log.e(TAG, "Failed to load settings", e);
        }
    }

    private void saveSettings() {
        try {
            JSONObject json = new JSONObject();

            JSONObject client = new JSONObject();
            JSONObject server = new JSONObject();
            JSONObject settings = new JSONObject();

            // Server info
            server.put("host", editHost.getText().toString());
            server.put("port", Integer.parseInt(editPort.getText().toString()));
            server.put("password", editPassword.getText().toString());

            // User settings
            settings.put("nick_name", editNick.getText().toString());
            settings.put("new_interface", false);
            settings.put("timestamp", false);
            settings.put("fast_connect", false);
            settings.put("voice_chat", false);
            settings.put("display_fps", false);
            settings.put("cleo", false);
            settings.put("fps_limit", 30);
            settings.put("chat_strings", 5);

            client.put("server", server);
            client.put("settings", settings);
            json.put("client", client);

            // Write to file
            FileOutputStream fos = new FileOutputStream(settingsFile);
            fos.write(json.toString(4).getBytes("UTF-8"));
            fos.close();

            Log.i(TAG, "Settings saved successfully");

        } catch (IOException | JSONException e) {
            Log.e(TAG, "Failed to save settings", e);
        }
    }

    private void launchSAMP() {
        try {
            Intent intent = new Intent(this, SAMP.class);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Failed to launch SAMP", e);
        }
    }
}
