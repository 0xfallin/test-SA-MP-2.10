package com.samp.mobile.launcher;

import android.content.Intent;
import android.os.Bundle;
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

    private File sampFolder;
    private File settingsFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI references
        editNick = findViewById(R.id.editNick);
        editHost = findViewById(R.id.editHost);
        editPort = findViewById(R.id.editPort);
        editPassword = findViewById(R.id.editPassword);
        btnSave = findViewById(R.id.btnSave);
        btnLaunch = findViewById(R.id.btnLaunch);

        // Prepare SAMP folder inside app-specific storage
        sampFolder = new File(getExternalFilesDir(null), "SAMP");
        if (!sampFolder.exists() && !sampFolder.mkdirs()) {
            Log.e(TAG, "Failed to create SAMP folder at: " + sampFolder.getAbsolutePath());
        }

        // Settings file path
        settingsFile = new File(sampFolder, "settings.json");

        loadSettings();

        btnSave.setOnClickListener(v -> saveSettings());

        btnLaunch.setOnClickListener(v -> launchSAMP());
    }

    private void loadSettings() {
        if (!settingsFile.exists()) return;

        try (FileInputStream fis = new FileInputStream(settingsFile);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }

            JSONObject json = new JSONObject(baos.toString("UTF-8"));
            JSONObject client = json.getJSONObject("client");
            JSONObject server = client.getJSONObject("server");
            JSONObject settings = client.getJSONObject("settings");

            editHost.setText(server.optString("host", ""));
            editPort.setText(String.valueOf(server.optInt("port", 7777)));
            editPassword.setText(server.optString("password", ""));
            editNick.setText(settings.optString("nick_name", "Player"));

        } catch (IOException | JSONException e) {
            Log.e(TAG, "Failed to load settings", e);
        }
    }

    private void saveSettings() {
        try {
            JSONObject client = new JSONObject();
            JSONObject server = new JSONObject();
            JSONObject settings = new JSONObject();

            server.put("host", editHost.getText().toString());
            server.put("port", Integer.parseInt(editPort.getText().toString()));
            server.put("password", editPassword.getText().toString());

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

            JSONObject root = new JSONObject();
            root.put("client", client);

            try (FileOutputStream fos = new FileOutputStream(settingsFile)) {
                fos.write(root.toString(4).getBytes("UTF-8"));
            }

            Log.i(TAG, "Settings saved to: " + settingsFile.getAbsolutePath());

        } catch (IOException | JSONException e) {
            Log.e(TAG, "Failed to save settings", e);
        }
    }

    private void launchSAMP() {
        if (!settingsFile.exists()) {
            Log.w(TAG, "Settings file missing, saving default settings first");
            saveSettings();
        }

        try {
            Intent intent = new Intent(this, SAMP.class);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Failed to launch SAMP", e);
        }
    }
}
