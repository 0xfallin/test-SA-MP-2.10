package com.samp.mobile.game;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.joom.paranoid.Obfuscate;
import com.samp.mobile.game.ui.AttachEdit;
import com.samp.mobile.game.ui.CustomKeyboard;
import com.samp.mobile.game.ui.LoadingScreen;
import com.samp.mobile.game.ui.dialog.DialogManager;

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

    public native void sendDialogResponse(int i, int i2, int i3, byte[] str);
    public native void initializeSAMP();
    public native void onInputEnd(byte[] str);
    public native void onEventBackPressed();

    public static SAMP getInstance() {
        return instance;
    }

    @Override
    public void OnInputEnd(String str) {
        byte[] toReturn = null;
        try {
            toReturn = str.getBytes("windows-1251");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            onInputEnd(toReturn);
        } catch (UnsatisfiedLinkError e5) {
            Log.e(TAG, e5.getMessage());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "**** onCreate");
        super.onCreate(savedInstanceState);

        mKeyboard = new CustomKeyboard(this);
        mDialog = new DialogManager(this);
        mAttachEdit = new AttachEdit(this);
        mLoadingScreen = new LoadingScreen(this);

        instance = this;

        try {
            initializeSAMP(); // call 2.0 native
        } catch (UnsatisfiedLinkError e5) {
            Log.e(TAG, e5.getMessage());
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

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
