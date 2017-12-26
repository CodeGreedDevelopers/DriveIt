package com.codegreed_devs.driveit;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MyAccountActivity extends AppCompatActivity {
    TextView top_name,name,phone,email;
    String display_name,display_email,display_phone;
    Uri profile_url;
    CircleImageView profile;
    ImageView ic_edt_name,ic_edt_phone,ic_edt_email;

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
        display_phone=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

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

        if (display_name.isEmpty()){
            top_name.setText(R.string.add_name);
            name.setText(R.string.add_name);
            name.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            name.setTextSize(15);
        }else {
            top_name.setText(display_name);
            name.setText(display_name);

        }
        if (display_phone.isEmpty()){
            phone.setText(R.string.add_phone);
            phone.setTextSize(15);
            phone.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        }else {
            phone.setText(display_phone);

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
                            Toast.makeText(MyAccountActivity.this, "Username can't be empty", Toast.LENGTH_SHORT).show();
                        }else{
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

                            email.setText(edt_email);
                            b.dismiss();

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


        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
//                            DisplayInfo();

                        }else {
                            Toast.makeText(MyAccountActivity.this, "Error updating name", Toast.LENGTH_SHORT).show();
                            DisplayInfo();
                        }
                    }

                });

    }
    public void UpdatePhone(){

    }
    public void UpdateEmail(){

    }
}
