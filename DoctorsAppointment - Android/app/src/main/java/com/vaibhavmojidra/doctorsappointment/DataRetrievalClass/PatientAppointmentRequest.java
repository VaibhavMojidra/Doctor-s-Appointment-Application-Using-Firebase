package com.vaibhavmojidra.doctorsappointment.DataRetrievalClass;

public class PatientAppointmentRequest {
    private String Address,City,DateAndTime,DocID,DoctorAppointKey,Name,PatientAppointKey,Specialization;

    public PatientAppointmentRequest() {
    }

    public PatientAppointmentRequest(String address, String city, String dateAndTime, String docID, String doctorAppointKey, String name, String patientAppointKey, String specialization) {
        Address = address;
        City = city;
        DateAndTime = dateAndTime;
        DocID = docID;
        DoctorAppointKey = doctorAppointKey;
        Name = name;
        PatientAppointKey = patientAppointKey;
        Specialization = specialization;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getDateAndTime() {
        return DateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        DateAndTime = dateAndTime;
    }

    public String getDocID() {
        return DocID;
    }

    public void setDocID(String docID) {
        DocID = docID;
    }

    public String getDoctorAppointKey() {
        return DoctorAppointKey;
    }

    public void setDoctorAppointKey(String doctorAppointKey) {
        DoctorAppointKey = doctorAppointKey;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPatientAppointKey() {
        return PatientAppointKey;
    }

    public void setPatientAppointKey(String patientAppointKey) {
        PatientAppointKey = patientAppointKey;
    }

    public String getSpecialization() {
        return Specialization;
    }

    public void setSpecialization(String specialization) {
        Specialization = specialization;
    }
}
