package com.vaibhavmojidra.doctorsappointment.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vaibhavmojidra.doctorsappointment.DataRetrievalClass.Doctor;
import com.vaibhavmojidra.doctorsappointment.PatientFragments.FixAppointment;
import com.vaibhavmojidra.doctorsappointment.R;

import java.util.List;

public class DoctorAdatper extends RecyclerView.Adapter<DoctorAdatper.ViewHolder> {

    private Context context;
    private List<Doctor> doctors;
    private Activity activity;

    public DoctorAdatper(Context context, List<Doctor> doctors, Activity activity) {
        this.context = context;
        this.doctors = doctors;
        this.activity=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_disease_or_doctor,parent,false);
        return new DoctorAdatper.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Doctor doc=doctors.get(position);
        holder.doctorname.setText(doc.getFirstName()+" "+doc.getLastName());
        holder.spl.setText("Specialization: "+doc.getSpecialization());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, FixAppointment.class).putExtra("NAME",doc.getFirstName()+" "+doc.getLastName()).putExtra("SPL",doc.getSpecialization()).putExtra("CITY",doc.getCity()).putExtra("ADDR",doc.getAddress()).putExtra("DOCID",doc.getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView doctorname,spl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorname=itemView.findViewById(R.id.disease_or_doctor_name);
            spl=itemView.findViewById(R.id.symptoms_or_spl);
        }
    }
}
