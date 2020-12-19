package com.vaibhavmojidra.doctorsappointment.DataRetrievalClass;

public class DiseaseAndSymptoms {
    private String DiseaseName,Symptoms;
    public DiseaseAndSymptoms() {
    }

    public DiseaseAndSymptoms(String diseaseName, String symptoms) {
        DiseaseName = diseaseName;
        Symptoms = symptoms;
    }

    public String getDiseaseName() {
        return DiseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        DiseaseName = diseaseName;
    }

    public String getSymptoms() {
        return Symptoms;
    }

    public void setSymptoms(String symptoms) {
        Symptoms = symptoms;
    }
}
