package com.goldentechinfo.insectimageupload;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.goldentechinfo.insectimageupload.model.InsectRes;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements ProgressRequestBody.UploadCallbacks {

    private ImageView tv_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_image = findViewById(R.id.tv_image);
        findViewById(R.id.btn_camera).setOnClickListener(view -> {
            captureImage();
        });
        findViewById(R.id.btn_gallery).setOnClickListener(view -> {
            showChooser();
        });

        findViewById(R.id.btn_upload_to_server).setOnClickListener(view -> {
            if (null != uri) {
                File file = MyFileUtils.getFile(this, uri);
                saveImage(uri, file.getName());
            } else Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show();

        });

        if (savedInstanceState != null) {
            currentImagePath = savedInstanceState.getString(URI_INSTANCE_STATE_KEY);
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the image capture uri before the activity goes into background
        outState.putString(URI_INSTANCE_STATE_KEY, currentImagePath);
    }

    private static final String URI_INSTANCE_STATE_KEY = "saved_path";

    private int SELECT_FILE_TYPE = 2;
    private static final int GALLERY_REQUEST = 6384;
    private static final int IMAGE_REQUEST = 100;

    private void showChooser() {

        Intent target = null;
        if (SELECT_FILE_TYPE == 1) {
            target = MyFileUtils.createGetContentIntent(MyFileUtils.MIME_TYPE_ONLY_PDF, false);
        } else if (SELECT_FILE_TYPE == 2) {
            target = MyFileUtils.createGetContentIntent(MyFileUtils.MIME_TYPE_IMAGE, false);
        } else if (SELECT_FILE_TYPE == 3) {
            target = MyFileUtils.createGetContentIntent("", false);
        }
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, "Select File");
        try {
            startActivityForResult(intent, GALLERY_REQUEST);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }

    }

    private File imageFile = null;
    private String currentImagePath = null;

    private void captureImage() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            imageFile = getImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (imageFile != null) {
            Uri imageUri = FileProvider.getUriForFile(this,
                    MyFileUtils.AUTHORITY, imageFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                intent.setClipData(ClipData.newRawUri("", imageUri));
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            startActivityForResult(intent, IMAGE_REQUEST);
        }
    }

    private File getImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = timeStamp + "_";

        File imageFile = File.createTempFile(imageName, ".jpg",
                new File(MyFileUtils.getRootCacheDir(MainActivity.this)));

        currentImagePath = imageFile.getAbsolutePath();

        return imageFile;
    }

    private Uri uri;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {

                    Log.d(TAG, "onActivityResult: getData " + data.getData());
                    Log.d(TAG, "onActivityResult: getClipData " + data.getClipData());

                    if (data != null) {

                        try {
                            MyCompressor(data.getData());
                        } catch (Exception e) {
                            Log.d(TAG, "onActivityResult: Exception " + e.toString());
                        }

                    }
                }
                break;
            case IMAGE_REQUEST:
                if (resultCode == RESULT_OK) {
                    try {
                        File file = new File(currentImagePath);
                        MyCompressor(Uri.fromFile(file));
                    } catch (Exception e) {

                        Log.d(TAG, "onActivityResult: " + e.toString());
                    }

                }
                break;


        }
        super.

                onActivityResult(requestCode, resultCode, data);

    }

    private static final String TAG = "MainActivity";

    private void MyCompressor(Uri uri) {

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver()
                    , uri);

            if (bitmap == null) return;

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            Bitmap outBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas outCanvas = new Canvas(outBitmap);

            outCanvas.drawBitmap(bitmap, 0f, 0f, null);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bytes);

            try {

                File f = MyFileUtils.getFileFrom(this, uri);
//                MyFileUtils.getSelectedFilePathFromGallery(this, uri)
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
                MediaScannerConnection.scanFile(this, new String[]{f.getPath()},
                        new String[]{"image/jpeg"}, null);
                fo.close();
                Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

                Uri uri_p = Uri.fromFile(new File(f.getAbsolutePath()));
                setImage(uri_p);
                this.uri = uri_p;
//                tv_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            //handle exception
            Log.d("TAG", "File Saved Exception::--->");
        }
    }

    private void setImage(Uri uri) {
        Bitmap bitmap = uriToBitmap(uri);
        tv_image.setImageBitmap(bitmap);
    }

    private Bitmap uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    ProgressDialog pd;

    private void saveImage(Uri uri, String name) {

        pd = new ProgressDialog(this);
        pd.setMessage("Please wait...");
        pd.show();
        pd.setCancelable(false);

        // create list of file parts (photo, video, ...)
        List<MultipartBody.Part> parts = new ArrayList<>();

//        parts.add();

        InsectApi api = RestApI.getInstance(this).SetRetrofit();

        MultipartBody.Part partImage = prepareFilePart("file0", uri, name);

        Call<InsectRes> call = api.UploadInsect1(
                createPartFromString("8517923614")
                , createPartFromString("LOKESH KUMAr"),
                createPartFromString("52650"),
                createPartFromString("7"),
                createPartFromString("929"),
                createPartFromString("106"),
                createPartFromString("App"), partImage);

        call.enqueue(new Callback<InsectRes>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<InsectRes> call,
                                   @NonNull Response<InsectRes> response) {
                String m;
                try {
                    m = response.body().getResponse().getMessage();
                    Toast.makeText(MainActivity.this, "" + response.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, e.getMessage());
                    Toast.makeText(MainActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                    m = e.toString();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setMessage(m);
                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                alertDialogBuilder.show();

                pd.cancel();
            }

            @Override
            public void onFailure(@NonNull Call<InsectRes> call, @NonNull Throwable t) {
                Log.e(TAG, "File upload failed!", t);
                Toast.makeText(MainActivity.this, "file upload failed!", Toast.LENGTH_SHORT).show();
                pd.cancel();
            }
        });

    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MediaType.parse(MyFileUtils.MIME_TYPE_TEXT), descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri, String pdfFileName) {
        // use the FileUtils to get the actual file by uri
        File file = null;
        file = MyFileUtils.getFile(this, fileUri);
        ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
        // create RequestBody instance from file
        //RequestBody requestFile = RequestBody.create(MediaType.parse(FileUtils.MIME_TYPE_ALL), file);
        //String file_name = FileUtils.getFileName(this, fileUri);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, pdfFileName, fileBody);
    }

    @Override
    public void onProgressUpdate(int percentage) {
//        myProgressDialog.setProgress(percentage);
    }

    @Override
    public void onError() {
        Log.d(TAG, "onError: upload failed");
        Toast.makeText(MainActivity.this, "File upload failed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinish() {
//        myProgressDialog.setIndeterminate(false);
//        myProgressDialog.setProgress(100);
//        pd.dismiss();
    }

    @Override
    public void uploadStart() {
//        myProgressDialog.setIndeterminate(false);
//        myProgressDialog.setProgress(0);
    }


}