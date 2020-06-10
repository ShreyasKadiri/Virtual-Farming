package application.example.myfarm;

import java.util.ArrayList;
import java.util.Map;

public class FarmerClass {
    private String name;
    private String city;
    private String imgUrl;
    private String farmImgUrl;
    private String emailId;
    private String status;
    private String agreementId;
    private String uid;
    private String type;
    private String cropsGrown;
    private String upiId;
    private String irrigation;
    private String age;
    private String phoneNumber;
    private String basePrice;
    private Map<String,Object> bidsList;


    public FarmerClass(){}

    public FarmerClass(String name,String city,String imgUrl,String farmImgUrl,String emailId,String status,String agreementId,String uid,String type,String cropsGrown,String upiId,String irrigation,String age,String phoneNumber,String basePrice){
        this.name = name;
        this.city = city;
        this.imgUrl = imgUrl;
        this.farmImgUrl = farmImgUrl;
        this.emailId = emailId;
        this.status = status;
        this.agreementId = agreementId;
        this.uid = uid;
        this.type = type;
        this.cropsGrown = cropsGrown;
        this.upiId = upiId;
        this.irrigation = irrigation;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.basePrice = basePrice;
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

    public String getFarmImgUrl() {
        return farmImgUrl;
    }

    public void setFarmImgUrl(String farmImgUrl) {
        this.farmImgUrl = farmImgUrl;
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

    public String getCropsGrown() {
        return cropsGrown;
    }

    public void setCropsGrown(String cropsGrown) {
        this.cropsGrown = cropsGrown;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getIrrigation() {
        return irrigation;
    }

    public void setIrrigation(String irrigation) {
        this.irrigation = irrigation;
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

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public Map<String, Object> getBidsList() {
        return bidsList;
    }

    public void setBidsList(Map<String, Object> bidsList) {
        this.bidsList = bidsList;
    }
}
