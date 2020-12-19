package com.vaibhavmojidra.doctorsappointment.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.vaibhavmojidra.doctorsappointment.DataRetrievalClass.PatientAppointmentRequest;
import com.vaibhavmojidra.doctorsappointment.PatientFragments.PendingAppointmentFragment;
import com.vaibhavmojidra.doctorsappointment.PatientMainActivity;
import com.vaibhavmojidra.doctorsappointment.R;
import com.vaibhavmojidra.doctorsappointment.ReusableFunctionsAndObjects;
import java.util.List;

public class PendingAppointmentAdapter extends RecyclerView.Adapter<PendingAppointmentAdapter.ViewHolder> {

    private Context context;
    private List<PatientAppointmentRequest> appointmentRequestList;
    private ProgressDialog progressDialog;
    public PendingAppointmentAdapter(Context context, List<PatientAppointmentRequest> appointmentRequestList) {
        this.context = context;
        this.appointmentRequestList = appointmentRequestList;
        progressDialog= new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_patient_apt,parent,false);
        return new PendingAppointmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PatientAppointmentRequest request=appointmentRequestList.get(position);
        holder.doc_name.setText(request.getName());
        holder.spl.setText("Specialization: "+request.getSpecialization());
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context).setCancelable(false).setMessage("Are you sure you want to cancel the appointment of Dr. "+request.getName()+" for "+request.getDateAndTime()+"?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.setMessage("Cancelling...");
                                progressDialog.show();
                                FirebaseDatabase.getInstance().getReference().child("PendingDocAppointments").child(request.getDocID()).child(request.getDoctorAppointKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            FirebaseDatabase.getInstance().getReference().child("PendingPatientAppointments").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(request.getPatientAppointKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        progressDialog.dismiss();
                                                        Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                                                        ((PatientMainActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_Container, new PendingAppointmentFragment(),"Pending Appointments").addToBackStack(null).commit();
                                                    }else {
                                                        progressDialog.dismiss();
                                                        ReusableFunctionsAndObjects.showMessageAlert(context, "Network Error", "Make sure you are connected to internet.", "OK",(byte)0);
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    ReusableFunctionsAndObjects.showMessageAlert(context, "Network Error", "Make sure you are connected to internet.", "OK",(byte)0);
                                                }
                                            });
                                        }else {
                                            progressDialog.dismiss();
                                            ReusableFunctionsAndObjects.showMessageAlert(context, "Network Error", "Make sure you are connected to internet.", "OK",(byte)0);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        ReusableFunctionsAndObjects.showMessageAlert(context, "Network Error", "Make sure you are connected to internet.", "OK",(byte)0);
                                    }
                                });
                            }
                        }).setNegativeButton("No",null).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentRequestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView doc_name,spl;
        AppCompatButton cancel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            doc_name=itemView.findViewById(R.id.doc_name);
            cancel=itemView.findViewById(R.id.cancel);
            spl=itemView.findViewById(R.id.spl);
        }
    }
}
