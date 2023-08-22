package com.doshacorp.emulatorchecker;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

public class EmulatorChecker {

    private final Context mContext;
    private boolean loggingEnabled = true;

    public EmulatorChecker(Context context) {
        mContext = context;
    }

    /**
     * Run all the emulator detection checks.
     *
     * @return true, we think there's a good *indication* of emulator | false good *indication* of no emulator (could still be cloaked)
     */
    public boolean isEmulator() {

        String qemu = null;
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            Method getProp = systemProperties.getMethod("get", String.class);
            qemu = (String) getProp.invoke(systemProperties, "ro.kernel.qemu");


        } catch (ClassNotFoundException e) {
            // Do nothing - this will happen on non-Android systems
        } catch (NoSuchMethodException e) {
            // Do nothing - this will happen on non-Android systems
        } catch (IllegalAccessException e) {
            // Do nothing - this will happen on non-Android systems
        } catch (InvocationTargetException e) {
            // Do nothing - this will happen on non-Android systems
        }






        return (runningOnAndroidStudioEmulator() == true
                || hasKnownEmulatorMacAddress() == true
                || hasKnownEmulatorPackage(mContext) == true
                || hasKnownEmulatorFiles() == true
                || sensors_and_resolutions_are_emulator(mContext) == true
                || isBootloaderUnlocked() == true
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.HARDWARE.equals("vbox86")
                || Build.HARDWARE.toLowerCase().contains("nox")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MODEL.toLowerCase().contains("droid4x")
                || Build.MODEL.contains("VirtualBox")
                || "QC_Reference_Phone" == Build.BOARD && !"Xiaomi".equalsIgnoreCase(Build.MANUFACTURER) //bluestacks
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.MANUFACTURER.contains("Bluestacks")
                || Build.MODEL.contains("Bluestacks")
                || Build.MODEL.toLowerCase().contains("bluestacks")
                || Build.MANUFACTURER.equals("unknown")
                || Runtime.getRuntime().availableProcessors() < 2
                || Build.HOST == "Build2" //MSI App Player
                || System.getProperties().getProperty("ro.kernel.qemu") == "1"
                || qemu.equals("1")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator")
                || Build.PRODUCT.toLowerCase().contains("nox")
                || Build.BOARD.toLowerCase().contains("nox")
                || Build.BOOTLOADER.toLowerCase().contains("nox")
                || Build.SERIAL.toLowerCase().contains("nox")
                || Build.HARDWARE.toLowerCase().contains("intel"));
    }

    public boolean runningOnAndroidStudioEmulator() {
        return Build.FINGERPRINT.startsWith("google/sdk_gphone")
                && Build.FINGERPRINT.endsWith(":user/release-keys")
                && Build.MANUFACTURER == "Google" && Build.PRODUCT.startsWith("sdk_gphone") && Build.BRAND == "google"
                && Build.MODEL.startsWith("sdk_gphone");
    }



    public  boolean hasKnownEmulatorFiles() {
        return (new File("/system/bin/qemu-props").exists()
                || new File("/system/bin/microvirt-prop").exists()
                || new File("/system/bin/microvirt-prop-64").exists()
                || new File("/system/bin/droid4x-prop").exists()
                || new File("/system/bin/windroyed-prop").exists());
    }

    public  boolean hasKnownEmulatorMacAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                byte[] hardwareAddress = networkInterface.getHardwareAddress();
                if (hardwareAddress != null && hardwareAddress.length == 6) {
                    if (hardwareAddress[0] == (byte) 0x08 && hardwareAddress[1] == (byte) 0x00 && hardwareAddress[2] == (byte) 0x27) {
                        return true;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return false;
    }
    public  boolean hasKnownEmulatorPackage(Context context) {
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.contains("com.bluestacks")
                    || packageInfo.packageName.contains("com.genymotion")
                    || packageInfo.packageName.contains("com.virtualbox")) {
                return true;
            }
        }
        return false;
    }

    public  boolean isBootloaderUnlocked() {
        String bootloader = Build.BOOTLOADER;
        return bootloader != null && bootloader.toLowerCase().contains("unlocked");
    }

    public  boolean sensors_and_resolutions_are_emulator(Context context) {
        boolean isEmulator = false;
        if (Build.PRODUCT.equals("sdk") || Build.PRODUCT.equals("google_sdk") || Build.PRODUCT.equals("sdk_x86") || Build.PRODUCT.equals("vbox86p")) {
            isEmulator = true;
            Log.e("####check5.1", "true");

        } else {
            SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            if (sensorManager != null) {
                Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                if (sensor == null) {
                    Log.e("####check5.2", "true");
                    isEmulator = true;
                }
            }

            if (!isEmulator) {
                DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                if (metrics != null) {
                    int densityDpi = metrics.densityDpi;
                    float xdpi = metrics.xdpi;
                    float ydpi = metrics.ydpi;
                    float screenWidthInches = metrics.widthPixels / xdpi;
                    float screenHeightInches = metrics.heightPixels / ydpi;
                    double screenSizeInches = Math.sqrt(Math.pow(screenWidthInches, 2) + Math.pow(screenHeightInches, 2));
                }


            }
        }


        return isEmulator;
    }

}
