package application.example.myfarm;

import java.util.HashMap;

public class AgreementClass {

    private  String bidAmount;
    private  String farmerUid;
    private String clientUid;
    private String agreementUid;
    private HashMap<String,ChatClass> chatList;
    private HashMap<String,ReportClass> reportList;
    private HashMap<String,PaymentClass> paymentList;


    public AgreementClass(){

    }

    public String getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(String bidAmount) {
        this.bidAmount = bidAmount;
    }

    public String getFarmerUid() {
        return farmerUid;
    }

    public void setFarmerUid(String farmerUid) {
        this.farmerUid = farmerUid;
    }

    public String getClientUid() {
        return clientUid;
    }

    public void setClientUid(String clientUid) {
        this.clientUid = clientUid;
    }

    public String getAgreementUid() {
        return agreementUid;
    }

    public void setAgreementUid(String agreementUid) {
        this.agreementUid = agreementUid;
    }

    public HashMap<String, ChatClass> getChatList() {
        return chatList;
    }

    public void setChatList(HashMap<String, ChatClass> chatList) {
        this.chatList = chatList;
    }

    public HashMap<String, ReportClass> getReportList() {
        return reportList;
    }

    public void setReportList(HashMap<String, ReportClass> reportList) {
        this.reportList = reportList;
    }

    public HashMap<String, PaymentClass> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(HashMap<String, PaymentClass> paymentList) {
        this.paymentList = paymentList;
    }
}
