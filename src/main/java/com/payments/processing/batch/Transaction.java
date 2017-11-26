package com.payments.processing.batch;

/**
 * Created by Aman on 11/19/2017.
 */
public class Transaction {

    private String transactionId;
    private String merchantId;
    private String transactionAmt;
    private String merchantName;

    public Transaction(String transactionId, String merchantId, String transactionAmt) {
        this.transactionId = transactionId;
        this.merchantId = merchantId;
        this.transactionAmt = transactionAmt;
    }

    public Transaction() {
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getTransactionAmt() {
        return transactionAmt;
    }

    public void setTransactionAmt(String transactionAmt) {
        this.transactionAmt = transactionAmt;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", transactionAmt='" + transactionAmt + '\'' +
                ", merchantName='" + merchantName + '\'' +
                '}';
    }
}
