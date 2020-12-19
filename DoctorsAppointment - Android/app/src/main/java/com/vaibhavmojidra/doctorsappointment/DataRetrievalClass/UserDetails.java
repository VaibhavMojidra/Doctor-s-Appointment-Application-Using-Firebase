package com.vaibhavmojidra.doctorsappointment.DataRetrievalClass;

public class UserDetails {
    private String Email,FirstName,LastName,MobileNo,UserType;

    public UserDetails() {
    }

    public UserDetails(String email, String firstName, String lastName, String mobileNo, String userType) {
        Email = email;
        FirstName = firstName;
        LastName = lastName;
        MobileNo = mobileNo;
        UserType = userType;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
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

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }
}
