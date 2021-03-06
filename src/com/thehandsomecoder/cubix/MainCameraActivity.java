package com.thehandsomecoder.cubix;

import android.app.Activity;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainCameraActivity extends Activity
{

    private Camera mCamera;
    private CameraPreview mPreview;
    private String TAG = "MainCameraActivity";

    private Camera.PictureCallback mPicture = new Camera.PictureCallback()
    {

        @Override
        public void onPictureTaken(byte[] data, Camera camera)
        {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null)
            {
                Log.d(TAG, "Error creating media file, check storage permissions: ");
                return;
            }

            try
            {
                Log.i(TAG, "Taking picture: ");
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Toast.makeText(getApplicationContext(), "Photo Added", 5);
            }
            catch (FileNotFoundException e)
            {
                Log.i(TAG, "File not found: " + e.getMessage());
            }
            catch (IOException e)
            {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main);


        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.camera_preview);
        frameLayout.addView(mPreview);

        RelativeLayout relativeLayoutControls = (RelativeLayout) findViewById(R.id.controls_layout);
        relativeLayoutControls.bringToFront();

        RelativeLayout relativeLayoutSensorsData = (RelativeLayout) findViewById(R.id.sensors_data_layout);
        relativeLayoutSensorsData.bringToFront();

        // Add a listener to the Capture button
        Button captureButton = (Button) findViewById(R.id.button_log);
        captureButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // get an image from the camera
                takePhoto();

            }
        }
        );

    }


    private void takePhoto()
    {
        Camera.PictureCallback pictureCB = new Camera.PictureCallback()
        {
            public void onPictureTaken(byte[] data, Camera cam)
            {
                new SavePhotoTask().execute(data);
                cam.startPreview();
            }
        };
        mCamera.takePicture(null, null, pictureCB);
    }

    class SavePhotoTask extends AsyncTask<byte[], String, String>
    {
        @Override
        protected String doInBackground(byte[]... data)
        {
            File picFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (picFile == null)
            {
                Log.e(TAG, "Error creating media file; are storage permissions correct?");
                return null;
            }
            byte[] photoData = data[0];
            try
            {
                FileOutputStream fos = new FileOutputStream(picFile);
                fos.write(photoData);
                fos.close();
            }
            catch (FileNotFoundException e)
            {
                Log.e(TAG, "File not found: " + e.getMessage());
                e.getStackTrace();
            }
            catch (IOException e)
            {
                Log.e(TAG, "I/O error with file: " + e.getMessage());
                e.getStackTrace();
            }
            return null;
        }
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        releaseCamera();

    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance()
    {
        Camera c = null;
        try
        {
            c = Camera.open();
        }
        catch (Exception e)
        {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private void releaseCamera()
    {
        if (mCamera != null)
        {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }


    public static final int MEDIA_TYPE_IMAGE = 1;


    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type)
    {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type)
    {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
            {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        }
        else
        {
            return null;
        }

        return mediaFile;
    }

}