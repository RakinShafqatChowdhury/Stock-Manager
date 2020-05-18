package com.example.stockmanager.Model;

public class product {
    private int id;
    private String productName;
    private int productQuantity;
    private String productSize;
    private String productDesc;
    private String productAddDate;

    public product() {
    }

    public product(int id, String productName, int productQuantity, String productSize, String productDesc, String productAddDate) {
        this.id = id;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productSize = productSize;
        this.productDesc = productDesc;
        this.productAddDate = productAddDate;
    }

    public product(String productName, int productQuantity, String productSize, String productDesc, String productAddDate) {
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productSize = productSize;
        this.productDesc = productDesc;
        this.productAddDate = productAddDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductAddDate() {
        return productAddDate;
    }

    public void setProductAddDate(String productAddDate) {
        this.productAddDate = productAddDate;
    }

}
