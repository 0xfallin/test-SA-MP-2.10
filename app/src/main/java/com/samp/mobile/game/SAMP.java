package com.samp.mobile.game;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;

import com.joom.paranoid.Obfuscate;
import com.samp.mobile.game.ui.AttachEdit;
import com.samp.mobile.game.ui.CustomKeyboard;
import com.samp.mobile.game.ui.LoadingScreen;
import com.samp.mobile.game.ui.dialog.DialogManager;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Obfuscate
public class SAMP extends GTASA implements CustomKeyboard.InputListener {

    private static final String TAG = "SAMP";
    private static SAMP instance;

    private CustomKeyboard mKeyboard;
    private DialogManager mDialog;
    private AttachEdit mAttachEdit;
    private LoadingScreen mLoadingScreen;

    private File sampFolder;

    public static SAMP getInstance() {
        return instance;
    }

    // Native methods
    public native void sendDialogResponse(int dialogId, int dialogType, int response, byte[] text);
    public native void initializeSAMP(String gameFolderPath); // pass game folder to native
    public native void onInputEnd(byte[] str);
    public native void onEventBackPressed();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "SAMP onCreate");

        instance = this;

        // Initialize UI
        mKeyboard = new CustomKeyboard(this);
        mDialog = new DialogManager(this);
        mAttachEdit = new AttachEdit(this);
        mLoadingScreen = new LoadingScreen(this);

        // Ensure SAMP folder exists
        sampFolder = new File(getExternalFilesDir(null), "SAMP");
        if (!sampFolder.exists()) {
            sampFolder.mkdirs();
            Log.i(TAG, "Created SAMP folder at: " + sampFolder.getAbsolutePath());
        }

        try {
            // Pass full path to native initialization
            initializeSAMP(sampFolder.getAbsolutePath());
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Native initialization failed: " + e.getMessage());
        }
    }

    @Override
    public void OnInputEnd(String str) {
        byte[] bytes = null;
        try {
            bytes = str.getBytes("windows-1251");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Encoding error: " + e.getMessage());
        }

        try {
            onInputEnd(bytes);
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Native onInputEnd error: " + e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            onEventBackPressed();
        } catch (UnsatisfiedLinkError ignored) {}
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                onEventBackPressed();
            } catch (UnsatisfiedLinkError ignored) {}
        }
        return super.onKeyDown(keyCode, event);
    }

    // --- UI helper methods ---
    public void showKeyboard() {
        runOnUiThread(() -> mKeyboard.ShowInputLayout());
    }

    public void hideKeyboard() {
        runOnUiThread(() -> mKeyboard.HideInputLayout());
    }

    public void showEditObject() {
        runOnUiThread(() -> mAttachEdit.show());
    }

    public void hideEditObject() {
        runOnUiThread(() -> mAttachEdit.hide());
    }

    public void showLoadingScreen() {
        runOnUiThread(() -> mLoadingScreen.show());
    }

    public void hideLoadingScreen() {
        runOnUiThread(() -> mLoadingScreen.hide());
    }

    public void exitGame() {
        finishAndRemoveTask();
        System.exit(0);
    }
}
