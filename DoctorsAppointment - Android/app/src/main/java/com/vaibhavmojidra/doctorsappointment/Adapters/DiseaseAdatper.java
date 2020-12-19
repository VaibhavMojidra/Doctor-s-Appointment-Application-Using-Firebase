package com.vaibhavmojidra.doctorsappointment.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vaibhavmojidra.doctorsappointment.DataRetrievalClass.DiseaseAndSymptoms;
import com.vaibhavmojidra.doctorsappointment.R;
import java.util.List;

public class DiseaseAdatper extends RecyclerView.Adapter<DiseaseAdatper.ViewHolder> {

    private Context context;
    private List<DiseaseAndSymptoms> diseaseAndSymptoms;

    public DiseaseAdatper(Context context, List<DiseaseAndSymptoms> diseaseAndSymptoms) {
        this.context = context;
        this.diseaseAndSymptoms = diseaseAndSymptoms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_disease_or_doctor,parent,false);
        return new DiseaseAdatper.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiseaseAndSymptoms diseaseAndSymptom=diseaseAndSymptoms.get(position);
        holder.diseasename.setText(diseaseAndSymptom.getDiseaseName());
        holder.symptom.setText("Symptoms: "+diseaseAndSymptom.getSymptoms());
        holder.info.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return diseaseAndSymptoms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView diseasename,symptom,info;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            diseasename=itemView.findViewById(R.id.disease_or_doctor_name);
            symptom=itemView.findViewById(R.id.symptoms_or_spl);
            info=itemView.findViewById(R.id.info);
        }
    }
}

