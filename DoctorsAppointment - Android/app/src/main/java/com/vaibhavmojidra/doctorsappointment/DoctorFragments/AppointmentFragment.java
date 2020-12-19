package com.vaibhavmojidra.doctorsappointment.DoctorFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vaibhavmojidra.doctorsappointment.Adapters.AppointmentAdapter;
import com.vaibhavmojidra.doctorsappointment.DataRetrievalClass.AppointmentRequest;
import com.vaibhavmojidra.doctorsappointment.R;
import com.vaibhavmojidra.doctorsappointment.ReusableFunctionsAndObjects;

import java.util.ArrayList;
import java.util.List;

public class AppointmentFragment extends Fragment {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private AppointmentAdapter appointmentAdapter;
    private List<AppointmentRequest> appointmentRequestList;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_common,container,false);
        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        appointmentRequestList=new ArrayList<>();
        progressDialog= new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...\nPlease wait...");
        progressDialog.show();
        FirebaseDatabase.getInstance().getReference().child("ConfirmedDocAppointments").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointmentRequestList.clear();
                for(DataSnapshot childsnapshot:snapshot.getChildren()){
                    appointmentRequestList.add(childsnapshot.getValue(AppointmentRequest.class));
                }
                appointmentAdapter=new AppointmentAdapter(getContext(),appointmentRequestList);
                recyclerView.setAdapter(appointmentAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                ReusableFunctionsAndObjects.showMessageAlert(getContext(),"Network Error",error.getMessage(),"Ok",(byte)0);
            }
        });
        return  view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.my_search_menu,menu);
        MenuItem menuItem=menu.findItem(R.id.search_bar);
        searchView=(SearchView)menuItem.getActionView();
        searchView.setQueryHint("Search Patients");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query!=null){
                    filter(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText!=null){
                    filter(newText);
                }
                return true;
            }
        });
    }
    private void filter(String s){
        List<AppointmentRequest> filteredlist=new ArrayList<>();
        for(AppointmentRequest symptoms:appointmentRequestList){
            if(symptoms.getName().toLowerCase().contains(s.toLowerCase())){
                filteredlist.add(symptoms);
            }
        }
        appointmentAdapter=new AppointmentAdapter(getContext(),filteredlist);
        recyclerView.setAdapter(appointmentAdapter);
    }
}
