package application.example.myfarm;

import java.util.Map;

public class ClientClass {

    private String name;
    private String city;
    private String imgUrl;
    private String emailId;
    private String status;
    private String agreementId;
    private String uid;
    private String upiId;
    private String type;
    private String age;
    private String phoneNumber;
    private Map<String,Object> bidslist;

    public ClientClass(){}

    public ClientClass(String name,String city,String imgUrl,String emailId,String status,String agreementId,String uid,String type,String age,String phoneNumber){
        this.name = name;
        this.city = city;
        this.imgUrl = imgUrl;
        this.emailId = emailId;
        this.status = status;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public Map<String, Object> getBidslist() {
        return bidslist;
    }

    public void setBidslist(Map<String, Object> bidslist) {
        this.bidslist = bidslist;
    }
}
