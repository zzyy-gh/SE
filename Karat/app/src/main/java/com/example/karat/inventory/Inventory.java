package com.example.karat.inventory;

import java.util.ArrayList;

class Inventory{
    private static Inventory single_instance = null;
    public static ArrayList<Listing> inventoryList = new ArrayList<>();

    private Inventory() {}
    
    public ArrayList<Listing> getList(){
        return inventoryList;
    }

    public void addListing(double price, double discount, String location, String name, String category) {
        inventoryList.add(new Listing(price, discount, location, name, category));
    }

    public int purchase(int listingId){
        for (Listing l: inventoryList) {
            if (listingId == l.getListingId()){
                if (l.available == true){
                    l.quantity--;
                    if (l.quantity == 0) {
                        l.setListingAvailable(false);
                    }
                    return 1;
                }
            }
        }    
        return 0;
    }

    public void removeListing(int listingId){
        for (listing l: inventoryList) {
            if (listingId == l.getListingId()){
                if (l.available == true){
                    l.setListingAvailable(false);
                }
            }
        }
    }



    public static Inventory getInstance(){
        if(single_instance == null){
            single_instance = new Inventory();
        }

        return single_instance;
    }
}