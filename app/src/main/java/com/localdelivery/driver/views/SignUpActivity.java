package com.localdelivery.driver.views;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.localdelivery.driver.R;
import com.localdelivery.driver.controller.ModelManager;
import com.localdelivery.driver.model.Constants;
import com.localdelivery.driver.model.Event;
import com.localdelivery.driver.model.Operations;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cc.cloudist.acplibrary.ACProgressFlower;

public class SignUpActivity extends AppCompatActivity {

    Toolbar toolbar;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String covertedImage,userChoosenTask;
    EditText edit_firstName, edit_lastName, editEmail, editPassword, editMobile, edit_vehicleType;
    ImageView driverimage;
    Context context;
    ACProgressFlower dialog;
    CallbackManager callbackManager;
    String status1 ="simple";
    String id="";
    SharedPreferences userdetailSharedP,loginShared;
    SharedPreferences.Editor editor,editor2;

    String first_name,last_name,email,password="",mobile="",vehicle_type="" ,profileimage,lat,lng,devicetoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        context = this;
 // get the device token  using the firbase and it used  for push notification
        devicetoken = FirebaseInstanceId.getInstance().getToken();
        Log.e("devicetoken of ","the android device"+devicetoken);

        initViews();

        driverimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                hideKeybord(v);
            }
        });
        convertImageToBase64();
        Intent intent = getIntent();
        if (intent!=null){


            String login_status = intent.getStringExtra("facebooklogin");
            if (login_status!=null) {
                first_name = intent.getStringExtra("firstname");
                last_name = intent.getStringExtra("lastname");
                email = intent.getStringExtra("emailid");
                profileimage = intent.getStringExtra("profileimage");
                Picasso.with(this)
                        .load(profileimage)
                        .into(driverimage);

                Log.e("userr detail", first_name + "\n" + last_name + "\n" + email + "\n" + profileimage + "\n" +vehicle_type);

                dialog = new ACProgressFlower.Builder(this).build();
                dialog.show();
                ModelManager.getInstance().getSignUpManager().getSignUpDetails(context, Operations.getSignUpDetails(context, email,
                        first_name, last_name, password, mobile, devicetoken, "A", "driver", vehicle_type, "12.1212121", "12.313112133", profileimage));
            }
            else {


            }

        }



    }

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
                } else {
                    //code for deny
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
        intent.setAction(Intent.ACTION_GET_CONTENT);//
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
        else {

        }

        //convertImageToBase64(bm);
    }

// All wight are intilizeing here ............

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

    }

    public void registerBtn(View v) {


        vehicle_type = edit_vehicleType.getText().toString();
        if (status1.equals("facebook")) {


            if (vehicle_type.isEmpty()) {

                edit_vehicleType.setError("please fill the vechile type");
                return;
            }

                Log.e("id of the user", id);


                dialog = new ACProgressFlower.Builder(this).build();
                dialog.show();
             ModelManager.getInstance().getUpdateManager().getupdatedetail(context, Operations.updatevichletype(context, id, vehicle_type));

                //ModelManager.getInstance().getSignUpManager().getSignUpDetails(context, Operations.updatevichletype(context, id, edit_vehicleType.getText().toString()));


            return;
        }
            status1 ="simple";
            first_name = edit_firstName.getText().toString();
            last_name = edit_lastName.getText().toString();
            email = editEmail.getText().toString();
            password = editPassword.getText().toString();
            mobile = editMobile.getText().toString();
            if (first_name.isEmpty() || last_name.isEmpty() || email.isEmpty() || password.isEmpty() || mobile.isEmpty() || vehicle_type.isEmpty()) {
                Toast.makeText(context, "Please fill all the details", Toast.LENGTH_SHORT).show();
                return;
            }
            dialog = new ACProgressFlower.Builder(this).build();
            dialog.show();
            ModelManager.getInstance().getSignUpManager().getSignUpDetails(context, Operations.getSignUpDetails(context,
                    email, first_name, last_name, password, mobile, devicetoken, "A", "driver", vehicle_type, "12.1212121", "12.313112133", "profile_pic"));



        }

    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(Event event) {
        if (dialog.isShowing()){

            dialog.dismiss();

        }

        switch (event.getKey()) {

            case Constants.ACCOUNT_REGISTERED:
                Toast.makeText(context, event.getValue(), Toast.LENGTH_SHORT).show();

                break;
            case Constants.ACCOUNT_NOT_REGISTERED:
                Toast.makeText(context, event.getValue(), Toast.LENGTH_SHORT).show();
                break;
            case  Constants.vechile_check:
                String status = event.getValue();
                Log.e("status",status);

                String[] split = status.split(",");
                String completestatus = split[split.length-2];
                 id = split[split.length-1];
                Log.e("complete status",completestatus);
                Log.e("iddd",id);
                if (completestatus.equals("false")) {
                    edit_firstName.setText(first_name);
                    edit_lastName.setText(last_name);
                    editEmail.setText(email);
                    editPassword.setClickable(false);
                    editPassword.setEnabled(false);
                    editMobile.setEnabled(false);
                    editMobile.setClickable(false);
                    edit_firstName.setClickable(false);
                    editEmail.setClickable(false);
                    edit_lastName.setClickable(false);
                    edit_firstName.setEnabled(false);
                    edit_lastName.setEnabled(false);
                    editEmail.setEnabled(false);
                    status1 = "facebook";
                }
                else {
                    if (password.length()>0){
                        Log.e("dimplereee","djjd");
                        Intent intent = new Intent(context,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {

                        ModelManager.getInstance().getUserDetailManager().getuserDeatil(context,Operations.getUserDeatil(context,id,"driver"));
                       /* Intent intent = new Intent(SignUpActivity.this, HomePageActivity.class);
                        startActivity(intent);
                        finish();*/
                    }
                }
                break;
            case Constants.update_status:
                dialog.dismiss();
                String updatestatus= event.getValue();
                Log.e("update starissss",updatestatus);
                String[] splittext = updatestatus.split(",");
                loginShared = getSharedPreferences("LOGINDETAIL", Context.MODE_PRIVATE);
                editor2 = loginShared.edit();
                editor2.putString("status","logged").apply();
                String id = splittext[splittext.length-2];
                String vehiclestatus = splittext[splittext.length-1];
                if (vehiclestatus.equals("true")){
                    dialog = new ACProgressFlower.Builder(this).build();
                    dialog.show();
                    ModelManager.getInstance().getUserDetailManager().getuserDeatil(context,Operations.getUserDeatil(context,id,"driver"));
                }
                else {

                    dialog.dismiss();
                    Toast.makeText(context, "your vehicle type is not update please update again", Toast.LENGTH_SHORT).show();
                }
                break;
            case Constants.userfullDetail:

                dialog.dismiss();

                Toast.makeText(context,"done"+event.getValue(),Toast.LENGTH_LONG).show();
                userdetailSharedP = getSharedPreferences("USERDEATIL", Context.MODE_PRIVATE);
                editor = userdetailSharedP.edit();
                Log.e("after login","you can get the user detail"+event.getValue());
                try {
                    JSONObject jsonObject = new JSONObject(event.getValue());
                    JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                    String userid = jsonObject1.getString("id");
                    String firstname = jsonObject1.getString("firstname");
                    String lastname = jsonObject1.getString("lastname");
                    String fullname = firstname+" "+lastname;
                    String emailid = jsonObject1.getString("email");
                    String mobile =  jsonObject1.getString("mobile");
                    String vehicletype = jsonObject1.getString("vehicle_type");
                    String profilepic = jsonObject1.getString("profile_pic");

/*-----------   save the all detail of the user  locally ---------------------------------*/

                    editor.putString("name",fullname).apply();
                    editor.putString("emailid",emailid).apply();
                    editor.putString("mobile",mobile).apply();
                    editor.putString("userid",userid).apply();
                    editor.putString("profilepic",profilepic).apply();

                    Intent intent1 = new Intent(context,HomePageActivity.class);
                    startActivity(intent1);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
