package com.localdelivery.driver.views;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.localdelivery.driver.R;
import com.localdelivery.driver.controller.ModelManager;
import com.localdelivery.driver.model.Config;
import com.localdelivery.driver.model.Constants;
import com.localdelivery.driver.model.Event;
import com.localdelivery.driver.model.LDPreferences;
import com.localdelivery.driver.model.Operations;
import com.localdelivery.driver.model.Utility;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnCancelListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import dmax.dialog.SpotsDialog;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String covertedImage,userChoosenTask;
    EditText edit_firstName, edit_lastName, editEmail, editPassword, editMobile, edit_vehicleType;
    ImageView driverimage;
    Context context;
    android.app.AlertDialog progress_dialog;
    CallbackManager callbackManager;
    String id="";

    String first_name,last_name,email,password="",mobile="",vehicle_type="" ,profileimage,lat,lng,devicetoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        context = this;

        devicetoken = FirebaseInstanceId.getInstance().getToken();

        LDPreferences.putString(context, "device_token", devicetoken);


        initViews();



       // checkLogin();


    }

   /* void checkLogin() {
        Intent intent = getIntent();

        String login_status = intent.getStringExtra("facebooklogin");
        if (login_status!=null) {
            first_name = intent.getStringExtra("firstname");
            last_name = intent.getStringExtra("lastname");
            email = intent.getStringExtra("emailid");
            profileimage = intent.getStringExtra("profileimage");
            Picasso.with(this)
                    .load(profileimage)
                    .into(driverimage);

            Log.e("user detail", first_name + "\n" + last_name + "\n" + email + "\n" + profileimage + "\n" +vehicle_type);

            dialog = new ACProgressFlower.Builder(this).build();
            dialog.show();
            ModelManager.getInstance().getSignUpManager().getSignUpDetails(context, Operations.getSignUpDetails(context, email,
                    first_name, last_name, password, mobile, devicetoken, "A", "driver", vehicle_type, "30.710252502759392", "76.70373268425465", profileimage));
        }
    }*/
    //  the image converted into base64 code ...............................

    public  void convertImageToBase64() {
        Bitmap bit = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_icon_driver_pic);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byte[] ba = bao.toByteArray();
        covertedImage = Base64.encodeToString(ba, 0);
        Log.e("converted Image", "" + covertedImage);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case com.localdelivery.driver.model.Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                }
                break;
        }
    }
    /* hide the keyboard from the UI ..*/

    public void hideKeybord(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    /* select image from gallery and capture from camera..*/
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = com.localdelivery.driver.model.Utility.checkPermission(SignUpActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        driverimage.setImageBitmap(thumbnail);
        byte[] byteArray = bytes.toByteArray();
        covertedImage = Base64.encodeToString(byteArray, 0);
        Log.e("camera images",covertedImage);

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                byte[] byteArray = bytes.toByteArray();
                covertedImage = Base64.encodeToString(byteArray, 0);
                driverimage.setImageBitmap(bm);
                Log.e("From gallery",covertedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void initViews() {

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Driver Registration");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }
        callbackManager = CallbackManager.Factory.create();

        edit_firstName = (EditText)findViewById(R.id.edit_firstName);
        edit_lastName = (EditText)findViewById(R.id.edit_lastName);
        editEmail = (EditText)findViewById(R.id.editEmail);
        editPassword = (EditText)findViewById(R.id.editPassword);
        editMobile = (EditText)findViewById(R.id.editMobile);
        edit_vehicleType = (EditText)findViewById(R.id.edit_vehicleType);
        driverimage = (ImageView)findViewById(R.id.driverimage);
        driverimage.setOnClickListener(this);
        convertImageToBase64();

        edit_vehicleType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                DialogPlus dialog = DialogPlus.newDialog(context)
                        .setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, new String[]{"car","cycle","bus","truck"}))
                        .setExpanded(false)
                        .setPadding(20,20,20,20)
                        .setGravity(Gravity.TOP)
                        .setOnCancelListener(new OnCancelListener() {
                            @Override
                            public void onCancel(DialogPlus dialog) {
                                dialog.dismiss();
                            }
                        })
                        .setOnBackPressListener(new OnBackPressListener() {
                            @Override
                            public void onBackPressed(DialogPlus dialogPlus) {

                                dialogPlus.dismiss();

                            }
                        })
                        .create();
                dialog.show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.driverimage) {
            selectImage();
            hideKeybord(view);
        }
    }

    public void registerBtn(View v) {

        vehicle_type = edit_vehicleType.getText().toString();

        first_name = edit_firstName.getText().toString();
        last_name = edit_lastName.getText().toString();
        email = editEmail.getText().toString();
        password = editPassword.getText().toString();
        mobile = editMobile.getText().toString();


        if (first_name.isEmpty() || last_name.isEmpty() || email.isEmpty() || password.isEmpty() || mobile.isEmpty() || vehicle_type.isEmpty()) {
            Toast.makeText(context, "Please fill all the details", Toast.LENGTH_SHORT).show();
            return;
        }

        else if (first_name.isEmpty())

            edit_firstName.setError("required");

        else if (email.isEmpty())

            editEmail.setError("required");

        else if (!Utility.emailValidator(email))

            Toast.makeText(context, "Fill valid email id", Toast.LENGTH_SHORT).show();

        else if (password.isEmpty())

            editPassword.setError("Required");

        else if (mobile.isEmpty())

            editMobile.setError("Required");

        else if (vehicle_type.isEmpty())

            edit_vehicleType.setError("Required");



        else {

            progress_dialog = new SpotsDialog(context);
            progress_dialog.setTitle("Updating...");
            progress_dialog.show();

            ModelManager.getInstance().getSignUpManager().getSignUpDetails(context,Config.signUp_url,
                    Operations.simpleuserRegister(context,email,password,first_name,last_name,edit_vehicleType.getText().toString(),
                            Config.user_type,devicetoken,Config.device_type,covertedImage,mobile));


        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(context);
    }

    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(context);
    }

    @Subscribe
    public void onEvent(Event event) {

        switch (event.getKey()) {
            case Constants.ACCOUNT_REGISTERED:
                progress_dialog.dismiss();
                Intent intent = new Intent(context,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case Constants.ACCOUNT_NOT_REGISTERED:
                progress_dialog.dismiss();
                Toast.makeText(context, ""+event.getValue(), Toast.LENGTH_SHORT).show();
                break;
            case Constants.SERVER_ERROR:
                Toast.makeText(context,"please check your internet connection",Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);

            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

}
