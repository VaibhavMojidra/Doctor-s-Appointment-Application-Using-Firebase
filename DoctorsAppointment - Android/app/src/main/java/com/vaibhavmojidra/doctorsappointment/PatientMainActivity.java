package com.vaibhavmojidra.doctorsappointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vaibhavmojidra.doctorsappointment.DataRetrievalClass.UserDetails;
import com.vaibhavmojidra.doctorsappointment.PatientFragments.MyAppointmentFragment;
import com.vaibhavmojidra.doctorsappointment.PatientFragments.PatientSearchDiseaseFragment;
import com.vaibhavmojidra.doctorsappointment.PatientFragments.PatientSearchDoctorsFragment;
import com.vaibhavmojidra.doctorsappointment.PatientFragments.PendingAppointmentFragment;

public class PatientMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_patient);
        progressDialog=new ProgressDialog(PatientMainActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        FirebaseDatabase.getInstance().getReference().child("UserDetails").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                if(snapshot.exists()){
                    final UserDetails userDetails=snapshot.getValue(UserDetails.class);
                    if(userDetails.getUserType().trim().equalsIgnoreCase("Patient")){
                        ReusableFunctionsAndObjects.setValues(userDetails.getFirstName()+" "+userDetails.getLastName(),userDetails.getEmail(),userDetails.getMobileNo());
                        TextView name=findViewById(R.id.name);
                        name.setText(userDetails.getFirstName()+" "+userDetails.getLastName());
                        name=findViewById(R.id.iniTv);
                        name.setText(userDetails.getFirstName().charAt(0)+""+userDetails.getLastName().charAt(0));
                        drawerLayout=findViewById(R.id.drawer_layout);
                        Toolbar toolbar=findViewById(R.id.toolBar);
                        setSupportActionBar(toolbar);
                        navigationView=findViewById(R.id.navigation_view);
                        navigationView.setNavigationItemSelectedListener(PatientMainActivity.this);
                        SwitchCompat switchCompat=(SwitchCompat)navigationView.getMenu().findItem(R.id.nav_switch).getActionView();
                        if(PatientMainActivity.this.getSharedPreferences("STORAGE",MODE_PRIVATE).getBoolean("IS_DARKMODE_ENABLED",false)){
                            switchCompat.setChecked(true);
                        }else{
                            switchCompat.setChecked(false);
                        }
                        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked){
                                    PatientMainActivity.this.getSharedPreferences("STORAGE",MODE_PRIVATE).edit().putBoolean("IS_DARKMODE_ENABLED",true).apply();
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                }else{
                                    PatientMainActivity.this.getSharedPreferences("STORAGE",MODE_PRIVATE).edit().putBoolean("IS_DARKMODE_ENABLED",false).apply();
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                }
                            }
                        });
                        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(PatientMainActivity.this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
                        drawerLayout.addDrawerListener(toggle);
                        toggle.syncState();
                        loadFragment(new PatientSearchDiseaseFragment(), "Search disease",R.id.search_disease);
                    }else{
                        logout();
                    }
                }else{
                    logout();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        String t = "";
        int ID=0;
        switch (item.getItemId()){
            case R.id.search_disease:
                fragment = new PatientSearchDiseaseFragment();
                t = "Search disease";
                ID=R.id.search_disease;
                break;
            case R.id.search_doctor:
                fragment = new PatientSearchDoctorsFragment();
                t = "Search doctors";
                ID=R.id.search_doctor;
                break;
            case R.id.pending_apt:
                fragment = new PendingAppointmentFragment();
                t = "Pending Appointments";
                ID=R.id.pending_apt;
                break;
            case R.id.apt:
                fragment = new MyAppointmentFragment();
                t = "My Appointments";
                ID=R.id.apt;
                break;
            case R.id.logout:
                new AlertDialog.Builder(PatientMainActivity.this).setMessage("Are you sure you want to logout?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                }).setNegativeButton("No",null).show();
            break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return loadFragment(fragment,t,ID);
    }

    private boolean loadFragment(Fragment fragment, String title, int IDD) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_Container, fragment).commit();
            getSupportActionBar().setTitle(Html.fromHtml("<font>" + title + "</font>"));
            navigationView.setCheckedItem(IDD);
            return true;
        }
        return false;
    }

    private void logout(){
        PatientMainActivity.this.getSharedPreferences("STORAGE",MODE_PRIVATE).edit().putBoolean("IS_DARKMODE_ENABLED",false).apply();
        PatientMainActivity.this.getSharedPreferences("STORAGE",MODE_PRIVATE).edit().putString("USER_TYPE","NON").apply();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(PatientMainActivity.this,AskDoctorPatient.class));
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}