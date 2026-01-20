/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Model;

/**
 * CartItem class holds information about each item in the cart.
 * It includes a reference to the laundry package and its quantity.
 */
public class CartItem {

    private laundryPackage laundryPackage;
    private int quantity;

    public CartItem(laundryPackage laundryPackage, int quantity) {
        if (laundryPackage == null) {
            throw new IllegalArgumentException("LaundryPackage cannot be null");
        }
        this.laundryPackage = laundryPackage;
        this.quantity = quantity;
    }

    public laundryPackage getLaundryPackage() {
        return laundryPackage;
    }

    public void setLaundryPackage(laundryPackage laundryPackage) {
        this.laundryPackage = laundryPackage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
