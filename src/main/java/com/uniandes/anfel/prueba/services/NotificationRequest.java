package com.uniandes.anfel.prueba.services;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificationRequest {
    @JsonProperty("seller_user_ip")
    private String sellerUserIp;
    @JsonProperty("buyer_user_ip")
    private String buyerUserIp;
    @JsonProperty("notification-id")
    private String notificationId;
    @JsonProperty("notification-message")
    private String notificationMessage;

    public String getSellerUserIp() { return sellerUserIp; }
    public void setSellerUserIp(String sellerUserIp) { this.sellerUserIp = sellerUserIp; }

    public String getBuyerUserIp() { return buyerUserIp; }
    public void setBuyerUserIp(String buyerUserIp) { this.buyerUserIp = buyerUserIp; }

    public String getNotificationId() { return notificationId; }
    public void setNotificationId(String notificationId) { this.notificationId = notificationId; }

    public String getNotificationMessage() { return notificationMessage; }
    public void setNotificationMessage(String notificationMessage) { this.notificationMessage = notificationMessage; }
}
