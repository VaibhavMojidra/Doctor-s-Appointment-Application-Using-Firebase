package com.vaibhavmojidra.doctorsappointment;

import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ReusableFunctionsAndObjects {

    public static String Name,Email,MobileNo;
    public static void setValues(String name,String email,String mobileNo){
        Name=name;
        Email=email;
        MobileNo=mobileNo;
    }
    public static void showMessageAlert(Context context, String title, String message, String buttonstring,byte type) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        if(type==1){
            alertDialog.setIcon(android.R.drawable.ic_dialog_info);
        }else{
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonstring, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}