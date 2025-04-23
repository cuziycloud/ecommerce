package com.tdtu.DesignPattern.Jeweluxe.iterator;

import java.util.List;
import com.tdtu.DesignPattern.Jeweluxe.model.Cart;

public class CartList implements CartCollection {
    private List<Cart> carts;

    public CartList(List<Cart> carts) {
        this.carts = carts;
    }

    @Override
    public CartIterator createIterator() {
        return new CartListIterator(carts);
    }
}
