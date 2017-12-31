package com.codegreed_devs.driveit;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MyAccountActivity extends AppCompatActivity {
    TextView top_name,name,phone,email;
    String display_name,display_email;
    String display_phone="";
    Uri profile_url;
    CircleImageView profile;
    ImageView ic_edt_name,ic_edt_phone,ic_edt_email;
    SweetAlertDialog pDialog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        //finding views
        top_name= findViewById(R.id.top_name);
        name= findViewById(R.id.name);
        phone= findViewById(R.id.phone);
        email= findViewById(R.id.email);
        profile=findViewById(R.id.profile);
        ic_edt_name=findViewById(R.id.ic_edt_name);
        ic_edt_phone=findViewById(R.id.ic_edt_phone);
        ic_edt_email=findViewById(R.id.ic_edt_email);

        //Get user info
        display_name= FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        display_email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        profile_url= FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        try{
            display_phone=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        }catch (Exception f){
            display_phone="";
        }


        DisplayInfo();

        ic_edt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeNameDialog();
            }
        });
        ic_edt_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeEmailDialog();

            }
        });
        ic_edt_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePhoneDialog();

            }
        });




    }

    public void DisplayInfo(){

        if (display_name!=null){
            if (display_name.isEmpty()){
                top_name.setText(R.string.add_name);
                name.setText(R.string.add_name);
                name.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                name.setTextSize(15);
                Toast.makeText(this, "I'm first", Toast.LENGTH_SHORT).show();
            }else{
                top_name.setText(display_name);
                name.setText(display_name);
            }

        }else {
            top_name.setText(R.string.add_name);
            name.setText(R.string.add_name);
            name.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            name.setTextSize(15);
            Toast.makeText(this, "I'm second", Toast.LENGTH_SHORT).show();


        }
        if (display_phone!=null){
            if(display_phone.isEmpty()){
                phone.setText(R.string.add_phone);
                phone.setTextSize(15);
                phone.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            }else {
                phone.setText(display_phone);
            }

        }else {
            phone.setText(R.string.add_phone);
            phone.setTextSize(15);
            phone.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        }
        if (display_email.isEmpty()){
            email.setText(R.string.add_eamil);
            email.setTextSize(15);
            email.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        }else {
            email.setText(display_email);

        }


        if (profile_url != null) {
            Picasso
                    .with(getBaseContext())
                    .load(profile_url)
                    .transform(new CropCircleTransformation())
                    .resize(512, 512)
                    .centerCrop()
                    .placeholder(R.drawable.avatar)
                    .into(profile);

        }else {
            Picasso
                    .with(getBaseContext())
                    .load(R.drawable.avatar)
                    .transform(new CropCircleTransformation())
                    .resize(512, 512)
                    .centerCrop()
                    .into(profile);

            //Else It will display the default dp
        }

    }
    public void ChangeNameDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final EditText edt = new EditText(this);
        edt.setText(name.getText());
        if (name.getText().equals("Add name")){
            edt.setText("");

        }

        dialogBuilder.setTitle(Html.fromHtml("<font color='#000000'>Update name</font>"));
        dialogBuilder.setMessage(Html.fromHtml("<font color='#000000'>Input your new name</font>"));
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.setNegativeButton("Cancel", null);
        dialogBuilder.setView(edt);
        edt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        edt.setSelection(edt.getText().length());

        final AlertDialog b = dialogBuilder.create();
        b.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String edt_name=edt.getText().toString();
                        if (edt_name.isEmpty()){
                            b.dismiss();
                            new SweetAlertDialog(MyAccountActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error")
                                    .setContentText("Username can't be empty!")
                                    .show();
                        }else if(edt_name.equals(display_name)){
                            b.dismiss();
                            new SweetAlertDialog(MyAccountActivity.this)
                                    .setTitleText("No changes made")
                                    .show();
                        }else{
                            pDialog = new SweetAlertDialog(MyAccountActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Updating");
                            pDialog.setCancelable(false);
                            pDialog.show();
                            name.setText(edt_name);
                            UpdateName(edt_name);
                            b.dismiss();

                        }

                    }
                });
            }
        });
        b.show();
    }
    public void ChangePhoneDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final EditText edt = new EditText(this);
        edt.setText(phone.getText());
        if (phone.getText().equals("Add mobile number")){
            edt.setText("");

        }
        edt.setInputType(InputType.TYPE_CLASS_PHONE);
        edt.setSelection(edt.getText().length());

        dialogBuilder.setTitle(Html.fromHtml("<font color='#000000'>Update name</font>"));
        dialogBuilder.setMessage(Html.fromHtml("<font color='#000000'>Input your new phone number</font>"));
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.setNegativeButton("Cancel", null);
        dialogBuilder.setView(edt);

        final AlertDialog b = dialogBuilder.create();
        b.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String edt_phone=edt.getText().toString();

                            phone.setText(edt_phone);
                            b.dismiss();
                            pDialog = new SweetAlertDialog(MyAccountActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Updating");
                            pDialog.setCancelable(false);
                            pDialog.show();
                            UpdatePhone(edt_phone);

                    }
                });
            }
        });
        b.show();
    }
    public void ChangeEmailDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final EditText edt = new EditText(this);
        edt.setText(email.getText());
        if (email.getText().equals("Add email address")){
            edt.setText("");

        }
        edt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        edt.setSelection(edt.getText().length());

        dialogBuilder.setTitle(Html.fromHtml("<font color='#000000'>Update name</font>"));
        dialogBuilder.setMessage(Html.fromHtml("<font color='#000000'>Input your new email address</font>"));
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.setNegativeButton("Cancel", null);
        dialogBuilder.setView(edt);

        final AlertDialog b = dialogBuilder.create();
        b.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String edt_email=edt.getText().toString();
                            UpdateEmail(edt_email);
                        b.dismiss();
                        pDialog = new SweetAlertDialog(MyAccountActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Updating");
                        pDialog.setCancelable(false);
                        pDialog.show();
                            email.setText(edt_email);


                    }
                });
            }
        });
        b.show();
    }
    public void UpdateName(String new_name){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(new_name)
                .build();

        assert user != null;
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            DisplayInfo();

                            pDialog.dismiss();
                            new SweetAlertDialog(MyAccountActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success")
                                    .setContentText("Name updated")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            Intent refresh=getIntent();
                                            finish();
                                            overridePendingTransition(0,0);
                                            startActivity(refresh);
                                            overridePendingTransition(0,0);

                                        }
                                    })
                                    .show();


                        }else {
                            pDialog.dismiss();
                            new SweetAlertDialog(MyAccountActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error")
                                    .setContentText("Something went wrong!")
                                    .show();
                            DisplayInfo();
                        }
                    }

                });

    }
    public void UpdatePhone(String new_phone){
        pDialog.dismiss();
        new SweetAlertDialog(MyAccountActivity.this, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("In progress")
                .setContentText("This feature is still in development\uD83D\uDE0A")
                .setConfirmText("Ok")
                .show();



    }
    public void UpdateEmail(String new_email){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(new_email)
//        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            DisplayInfo();
                            pDialog.dismiss();

                            new SweetAlertDialog(MyAccountActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Success")
                                    .setContentText("Check your email to activate your new email address")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {

                                            SignOut();
                                        }
                                    })
                                    .show();

                        }else {
                            pDialog.dismiss();
                            new SweetAlertDialog(MyAccountActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error")
                                    .setContentText("Something went wrong!")
                                    .show();
                            DisplayInfo();
                        }
                    }

                });

    }
    public void SignOut(){
        FirebaseAuth.getInstance().signOut();

        // Return to sign in
        Intent intent=new Intent(MyAccountActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();


    }

}
