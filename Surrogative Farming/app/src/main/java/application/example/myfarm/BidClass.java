package application.example.myfarm;

public class BidClass {
    private String farmerUid;
    private String clientUid;
    private String bidAmount;
    private String bidUid;

    public BidClass(){}

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

    public String getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(String bidAmount) {
        this.bidAmount = bidAmount;
    }

    public String getBidUid() {
        return bidUid;
    }

    public void setBidUid(String bidUid) {
        this.bidUid = bidUid;
    }
}
