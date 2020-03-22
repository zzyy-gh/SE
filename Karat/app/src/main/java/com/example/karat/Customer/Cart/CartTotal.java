package com.example.karat.Customer.Cart;
import com.example.karat.Customer.COrder.Receipt;

import java.util.ArrayList;

public class CartTotal {

    /* Attributes */

    protected static int cartAmount = 0;
    private static ArrayList<Cart> cartlist = new ArrayList<>();
    private static ArrayList<String> cartlistname = new ArrayList<>();
    /* Getters */

    public static int getCartAmount() {
        return cartAmount;
    }
    public static ArrayList<Cart> getCartlist() {
        return cartlist;
    }
    public static ArrayList<String> getNamelist(){
        return cartlistname;
    }


    /* Constructors */

    CartTotal(){
    }

    /* Methods */


    public static void addtoCart(double price, String name, int quantity,int mImageResource) {

        cartlist.add(new Cart(price, name, quantity, mImageResource));
        cartAmount++;
    }

}


