package com.vaibhavmojidra.doctorsappointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout EmailTB;
    private TextInputLayout PassTB;
    private TextView forgetPass,gotToRegister;
    private AppCompatButton logInButton;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        EmailTB=findViewById(R.id.EmailTB);
        PassTB=findViewById(R.id.PassTB);
        forgetPass=findViewById(R.id.ForgetPass);

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(LoginActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.leftMargin=10;
                lp.rightMargin=10;
                input.setLayoutParams(lp);
                input.setHint("Email");

                new AlertDialog.Builder(LoginActivity.this).setTitle("Enter email ID").setView(input).setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        boolean isemailvalid=false;
                        if ((((input.getText()).toString()).trim()).equals("")) {
                            ReusableFunctionsAndObjects.showMessageAlert(LoginActivity.this,"Invalid Email","Please enter valid email.","Ok",(byte)0);
                            isemailvalid = false;
                        } else {
                            if (Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$").matcher(((input.getText()).toString()).trim()).matches()) {
                                isemailvalid = true;
                            } else {
                                ReusableFunctionsAndObjects.showMessageAlert(LoginActivity.this,"Invalid Email","Please enter valid email.","Ok",(byte)0);
                                isemailvalid = false;
                            }
                        }
                        if(isemailvalid){
                            progressDialog.setMessage("Please wait....");
                            progressDialog.show();
                            FirebaseAuth.getInstance().sendPasswordResetEmail(input.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    if(task.isSuccessful()){
                                        ReusableFunctionsAndObjects.showMessageAlert(LoginActivity.this,"Reset Link Sent","Password reset link has been sent to "+input.getText().toString().trim(),"Ok",(byte)1);
                                    }else{
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthInvalidUserException invalidEmail) {
                                            ReusableFunctionsAndObjects.showMessageAlert(LoginActivity.this, "Invalid User", "Account does'nt exists. Make sure you have activated your account", "OK",(byte)0);
                                        } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                            ReusableFunctionsAndObjects.showMessageAlert(LoginActivity.this, "Invalid Password", "Your password is incorrect", "OK",(byte)0);
                                        } catch (Exception e) {
                                            ReusableFunctionsAndObjects.showMessageAlert(LoginActivity.this, "Network Error", "Unable to login, Make sure you are connected to internet", "OK",(byte)0);
                                        }
                                    }
                                }
                            });
                        }

                    }
                }).setNegativeButton("Cancel",null).show();
            }
        });
        logInButton=findViewById(R.id.log_in);
        gotToRegister=findViewById(R.id.gotoregister);
        if(getIntent().getBooleanExtra("ForPatient",false)){
            gotToRegister.setVisibility(View.VISIBLE);
        }else{
            gotToRegister.setVisibility(View.GONE);
        }
        gotToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,PatientRegister.class));
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                finish();
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    progressDialog.setMessage("Authenticating, please wait...");
                    progressDialog.show();
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(EmailTB.getEditText().getText().toString().trim(),PassTB.getEditText().getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                                    if(getIntent().getBooleanExtra("ForPatient",false)){
                                        if(LoginActivity.this.getSharedPreferences("STORAGE",MODE_PRIVATE).edit().putString("USER_TYPE","PATIENT").commit()){
                                            progressDialog.dismiss();
                                            startActivity(new Intent(LoginActivity.this, PatientMainActivity.class));
                                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                            finish();
                                        }else{
                                            progressDialog.dismiss();
                                            startActivity(new Intent(LoginActivity.this, AskDoctorPatient.class));
                                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                            finish();
                                        }

                                    }else{
                                        if(LoginActivity.this.getSharedPreferences("STORAGE",MODE_PRIVATE).edit().putString("USER_TYPE","DOCTOR").commit()){
                                            progressDialog.dismiss();
                                            startActivity(new Intent(LoginActivity.this, DoctorMainActivity.class));
                                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                            finish();
                                        }else{
                                            progressDialog.dismiss();
                                            startActivity(new Intent(LoginActivity.this, AskDoctorPatient.class));
                                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                            finish();
                                        }
                                    }
                                }else{
                                    progressDialog.dismiss();
                                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                                    alertDialog.setTitle("Incomplete Activation");
                                    alertDialog.setMessage("It seems that you haven't completed the verification phase in activation process, please click the verification link sent to your email, do you want re-send the link?");
                                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            try{FirebaseAuth.getInstance().signOut();}catch (Exception e){}
                                            dialog.dismiss();
                                        }
                                    });
                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Resend", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            progressDialog.setMessage("Sending link, please wait...");
                                            progressDialog.show();
                                            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        progressDialog.dismiss();
                                                        try{FirebaseAuth.getInstance().signOut();}catch (Exception e){}
                                                        ReusableFunctionsAndObjects.showMessageAlert(LoginActivity.this, "Verify", "A verification link has been sent to "+EmailTB.getEditText().getText().toString().trim()+".Please click and verify to activate your account", "OK",(byte)1);
                                                    }else{
                                                        progressDialog.dismiss();
                                                        try{FirebaseAuth.getInstance().signOut();}catch (Exception e){}
                                                        ReusableFunctionsAndObjects.showMessageAlert(LoginActivity.this, "Network Error", "Unable to login, Make sure you are connected to internet", "OK",(byte)0);
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    ReusableFunctionsAndObjects.showMessageAlert(LoginActivity.this, "Network Error", "Unable to login, Make sure you are connected to internet", "OK",(byte)0);
                                                }
                                            });
                                        }
                                    });
                                    alertDialog.show();
                                }
                            }else{
                                progressDialog.dismiss();
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException invalidEmail) {
                                    ReusableFunctionsAndObjects.showMessageAlert(LoginActivity.this, "Invalid User", "Account does'nt exists. Make sure you have activated your account", "OK",(byte)0);
                                } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                    ReusableFunctionsAndObjects.showMessageAlert(LoginActivity.this, "Invalid Password", "Your password is incorrect", "OK",(byte)0);
                                } catch (Exception e) {
                                    ReusableFunctionsAndObjects.showMessageAlert(LoginActivity.this, "Network Error", "Unable to login, Make sure you are connected to internet", "OK",(byte)0);
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean isValid(){
        boolean isvalid = false, isemailvalid = false, ispasswordsvalid = false;
        if ((((PassTB.getEditText().getText()).toString()).trim()).equals("")) {
            PassTB.setErrorEnabled(true);
            PassTB.setError("Please enter password");
            PassTB.requestFocus();
            ispasswordsvalid = false;
        } else {
            ispasswordsvalid = true;
        }

        if ((((EmailTB.getEditText().getText()).toString()).trim()).equals("")) {
            EmailTB.setErrorEnabled(true);
            EmailTB.setError("Please enter email address");
            EmailTB.requestFocus();
            isemailvalid = false;
        } else {
            if (Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$").matcher(((EmailTB.getEditText().getText()).toString()).trim()).matches()) {
                isemailvalid = true;
            } else {
                EmailTB.setErrorEnabled(true);
                EmailTB.setError("Please enter a valid email address");
                EmailTB.requestFocus();
                isemailvalid = false;
            }
        }
        return (isemailvalid && ispasswordsvalid) ? true : false;
    }

    @Override
    public void onBackPressed() {
        if(getIntent().getBooleanExtra("ForPatient",false)){
            startActivity(new Intent(LoginActivity.this,PatientLoginRegisterChoice.class));
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            finish();
        }else{
            startActivity(new Intent(LoginActivity.this,AskDoctorPatient.class));
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            finish();
        }
    }
}