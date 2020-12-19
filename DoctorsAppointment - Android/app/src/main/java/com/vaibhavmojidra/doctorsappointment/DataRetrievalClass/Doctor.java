package com.vaibhavmojidra.doctorsappointment.DataRetrievalClass;

public class Doctor {
    private String Address,City,FirstName,Id,LastName,MobileNo,Specialization;

    public Doctor() {
    }

    public Doctor(String address, String city, String firstName, String id, String lastName, String mobileNo, String specialization) {
        Address = address;
        City = city;
        FirstName = firstName;
        Id = id;
        LastName = lastName;
        MobileNo = mobileNo;
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

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getSpecialization() {
        return Specialization;
    }

    public void setSpecialization(String specialization) {
        Specialization = specialization;
    }
}
