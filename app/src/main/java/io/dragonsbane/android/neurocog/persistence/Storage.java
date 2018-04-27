package io.dragonsbane.android.neurocog.persistence;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Storage helper class.
 *
 */

public final class Storage {

    private static String externalAppDir = "/data/dragonsbane";

    private Storage() {}

    public static boolean externalStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState());
    }

    public static boolean externalStorageWriteable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static boolean writeExternalObject(String key, Object object) throws IOException {
        if(!externalStorageAvailable()) {
            Log.e(Storage.class.getName(), "External Storage not available.");
            return false;
        }

        if(!externalStorageWriteable()) {
            Log.e(Storage.class.getName(), "External Storage not writeable.");
            return false;
        }

        File externalRoot = Environment.getExternalStorageDirectory();
        File externalDir = new File(externalRoot + externalAppDir);
        boolean externalDirExists = externalDir.exists();
        if(!externalDirExists) {
            externalDirExists = externalDir.mkdir();
            if(!externalDirExists) {
                // Houston we gotta problem
                Log.e(Storage.class.getName(),"Unable to create external directory: "+externalDir.getAbsolutePath());
                return false;
            }
        }
        File file = null;
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            file = new File(externalDir, key);
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
        } catch (IOException e) {
            Log.w(Storage.class.getName(),"IOException caught writing object to external storage.",e);
            return false;
        } finally {
            try {
                if(oos != null) oos.close();
            } catch (Exception e) {
                Log.w(Storage.class.getName(),"Exception caught in Storage closing ObjectOutputStream.",e);
            }
            try {
                if(fos != null) fos.close();
            } catch (Exception e) {
                Log.w(Storage.class.getName(),"Exception caught in Storage closing FileOutputStream.",e);
            }
        }
        return true;
    }

    public static Object readExternalObject(String key) throws IOException {
        if(!externalStorageAvailable()) {
            Log.e(Storage.class.getName(), "External Storage not available.");
            return null;
        }
        File externalRoot = Environment.getExternalStorageDirectory();
        File externalDir = new File(externalRoot + externalAppDir);
        boolean externalDirExists = externalDir.exists();
        if(!externalDirExists) {
            Log.w(Storage.class.getName(),"External directory does not exist: "+externalDir.getAbsolutePath());
            return null;
        }
        Object object = null;
        File file = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            file = new File(externalDir, key);
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            object = ois.readObject();
        } catch (ClassNotFoundException e) {
            Log.w(Storage.class.getName(),"ClassNotFoundException caught reading object from external storage.",e);
        } finally {
            try {
                if(ois != null) ois.close();
            } catch (Exception e) {
                Log.w(Storage.class.getName(),"Exception caught in Storage closing ObjectInputStream.",e);
            }
            try {
                if(fis != null) fis.close();
            } catch (Exception e) {
                Log.w(Storage.class.getName(),"Exception caught in Storage closing FileInputStream.",e);
            }
        }
        return object;
    }

    public static void writeInternalObject(Context context, String key, Object object) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    public static Object readInternalObject(Context context, String key) throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        return object;
    }

    public static long lastModifiedDate(Context context, String key) throws IOException, ClassNotFoundException {
        File file = context.getFileStreamPath(key);
        if(file!=null && file.exists())
            return file.lastModified();
        else
            return 0;
    }
}
