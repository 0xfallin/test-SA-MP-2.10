package com.samp.mobile.game;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.joom.paranoid.Obfuscate;
import com.wardrumstudios.utils.WarMedia;

@Obfuscate
public class GTASA extends WarMedia {
    private static final String TAG = "GTASA";
    private boolean once = false;
    static String vmVersion;

    static {
        Log.i(TAG, "**** Loading SO's for SAMP 2.0");
        try {
            vmVersion = System.getProperty("java.vm.version");
            Log.i(TAG, "VM Version: " + vmVersion);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // Core SAMP 2.0 libraries
            System.loadLibrary("GTASA");
            System.loadLibrary("bass");
            System.loadLibrary("SAMP");

            // Optional libraries
            safeLoadLibrary("ImmEmulatorJ");
            safeLoadLibrary("cleo");
            safeLoadLibrary("SCAnd");
            safeLoadLibrary("TouchSenseSDK");
            safeLoadLibrary("AML");

        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Failed to load native libraries", e);
        }
    }

    private static void safeLoadLibrary(String libName) {
        try {
            System.loadLibrary(libName);
            Log.i(TAG, "Loaded library: " + libName);
        } catch (UnsatisfiedLinkError ignored) {
            Log.w(TAG, "Optional library not found: " + libName);
        }
    }

    /** Native methods exposed by SAMP 2.0 .so */
    public native void main();
    public native void setCurrentScreenSize(int width, int height);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (!once) once = true;
        Log.i(TAG, "GTASA onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.i(TAG, "GTASA onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "GTASA onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "GTASA onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "GTASA onStop");
        super.onStop();
    }

    @Override
    public void onRestart() {
        Log.i(TAG, "GTASA onRestart");
        super.onRestart();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "GTASA onDestroy");
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(TAG, "GTASA configuration changed");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    /** Optional stubs for removed Social Club or features */
    public void EnterSocialClub() {}
    public void ExitSocialClub() {}
    public void AfterDownloadFunction() {}

    /** App command stubs */
    public boolean ServiceAppCommand(String str, String str2) { return false; }
    public int ServiceAppCommandValue(String str, String str2) { return 0; }

    /** Static helper stubs */
    public static void staticEnterSocialClub() {}
    public static void staticExitSocialClub() {}
}
