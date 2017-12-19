package com.servicehub.Employee;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.servicehub.Controller.Activity_indicator;
import com.servicehub.Controller.Configr;
import com.servicehub.Controller.Connection;
import com.servicehub.Custom_Compo.Custom_Edittext_Bold;
import com.servicehub.Json_Model.JSON;
import com.servicehub.Model.Model_profile;
import com.servicehub.R;
import com.servicehub.Shared.Pref_Master;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Employee_profile extends AppCompatActivity {


    ImageView back;
    ImageView edit_profile;
    CircleImageView profile;
    private int GALLERY_INTENT_CALLED = 1;
    private static final int READ_EXTERNAL_STORAGE = 2;
    private int GALLERY_KITKAT_INTENT_CALLED = 3;
    public static Bitmap bitmap;
    Context context = this;
    Pref_Master pref;
    private static final String TAG = "Profile";
    String var = "";
    Activity_indicator obj_dialog;
    ArrayList<Model_profile> arrayList = new ArrayList<>();
    ArrayList<Model_profile> updatelist = new ArrayList<>();
    Custom_Edittext_Bold et_name, et_email, et_mobileno;
    RelativeLayout r_save;
    RatingBar rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_profile);
        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        showPermiosssion();


        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edit_profile = (ImageView) findViewById(R.id.edit_profile);
        profile = (CircleImageView) findViewById(R.id.profile);
        et_name = (Custom_Edittext_Bold) findViewById(R.id.et_name);
        et_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_name.setCursorVisible(true);
            }
        });
        et_email = (Custom_Edittext_Bold) findViewById(R.id.et_email);
        et_mobileno = (Custom_Edittext_Bold) findViewById(R.id.et_mobileno);

        rate = (RatingBar) findViewById(R.id.rate);

        r_save = (RelativeLayout) findViewById(R.id.r_save);
        r_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validate();
            }
        });
        Profile();

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });

    }

    public void showPermiosssion() {
        if (Build.VERSION.SDK_INT >= 19) {
            String[] s = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions((Activity) context, s, READ_EXTERNAL_STORAGE);
                } else {
                    ActivityCompat.requestPermissions((Activity) context, s, READ_EXTERNAL_STORAGE);
                }
            }
        }
    }

    private void Validate() {
        updatelist.clear();
        Model_profile modelProfile = new Model_profile();
        modelProfile.setName(et_name.getText().toString());
        modelProfile.setEmail(et_email.getText().toString());
        modelProfile.setMobile(et_mobileno.getText().toString());
        updatelist.add(modelProfile);
        updatedata();

    }

    public void openImageChooser() {
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("image/jpeg");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_INTENT_CALLED);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/jpeg");
            startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GALLERY_INTENT_CALLED && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            // Get the path from the Uri
            String path = getPathFromURI(uri);
            Log.i(TAG, "Image Path : " + path);
            // Set the image in ImageView

            Toast.makeText(context, "Image Selected", Toast.LENGTH_SHORT).show();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            var = getStringImage(bitmap);
            profile.setImageURI(uri);


        } else if (requestCode == GALLERY_KITKAT_INTENT_CALLED && resultCode == RESULT_OK) {
            Uri originalUri = data.getData();
            int takeFlags = data.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getContentResolver().takePersistableUriPermission(originalUri, takeFlags);
            }

            String id = originalUri.getLastPathSegment().split(":")[1];
            final String[] imageColumns = {MediaStore.Images.Media.DATA};
            final String imageOrderBy = null;
            Uri uri = getUri();
            String selectedImagePath = "path";
            Cursor imageCursor = managedQuery(uri, imageColumns,
                    MediaStore.Images.Media._ID + "=" + id, null, imageOrderBy);
            if (imageCursor.moveToFirst()) {
                selectedImagePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            // Get the path from the Uri
            String path = getPathFromURI(uri);
            Log.i(TAG, "Image Path : " + path);
            // Set the image in ImageView
            Toast.makeText(context, "Image Selected", Toast.LENGTH_SHORT).show();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), originalUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            var = getStringImage(bitmap);
            profile.setImageBitmap(bitmap);
            Log.e("path", selectedImagePath); // use selectedImagePath
        }
    }

    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void Profile() {

        obj_dialog.show();

        String url = Configr.app_url + "myprofile/" + pref.getStr_Userid();

        Map<String, String> params = new HashMap<>();

        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    if (res_flag.equals("200")) {

                        JSONObject jarray = new JSONObject(jobj.getString("data"));
                        JSONArray jarray1 = jarray.getJSONArray("user");

                        for (int i = 0; i < jarray1.length(); i++) {
                            JSONObject jsonObject = jarray1.getJSONObject(i);
                            Model_profile model = new Model_profile();
                            model.setUsertype_id(jsonObject.getString("usertypeid"));
                            model.setName(jsonObject.getString("name"));
                            model.setEmail(jsonObject.getString("email"));
                            model.setMobile(jsonObject.getString("mobile"));
                            model.setImage(jsonObject.getString("userimg"));
                            model.setRate(jsonObject.getString("rate"));
                            arrayList.add(model);

                            et_name.setText(jsonObject.getString("name"));
                            et_email.setText(jsonObject.getString("email"));
                            et_mobileno.setText(jsonObject.getString("mobile"));

                            if (jsonObject.getString("userimg").equals("")) {
                                profile.setImageResource(R.drawable.default_imggg);
                            } else {
                                Glide.with(context).load(jsonObject.getString("userimg")).into(profile);
                            }
                            rate.setRating(Float.parseFloat(jsonObject.getString("rate")));
                        }

                    } else {
                        Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                obj_dialog.dismiss();
            }
        };
        Connection.getconnectionVolley(url, params, Configr.getHeaderParam(), context, lis_res, lis_error);
    }

    public void updatedata() {

        obj_dialog.show();

        String url = Configr.app_url + "profileupdate";
        String json = "";

        json = JSON.add_3(updatelist, pref, "user");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);
        param.put("userimg", var);
        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                obj_dialog.dismiss();

                try {

                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    String otp = "", userid = "";

                    if (res_flag.equals("200")) {
                        Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                obj_dialog.dismiss();

            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_res, lis_error);
    }

}
