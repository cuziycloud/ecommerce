package com.tdtu.DesignPattern.Jeweluxe.iterator;

import java.util.List;
import com.tdtu.DesignPattern.Jeweluxe.model.Cart;

public class CartListIterator implements CartIterator {
    private List<Cart> cartList;
    private int index = 0;

    public CartListIterator(List<Cart> cartList) {
        this.cartList = cartList;
    }

    @Override
    public boolean hasNext() {
        return index < cartList.size();
    }

    @Override
    public Object next() {
        if (this.hasNext()) {
            return cartList.get(index++);
        }
        return null;
    }
}