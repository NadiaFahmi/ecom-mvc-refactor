package com.ecommerce.repository;

import com.ecommerce.model.entities.Cart;
import com.ecommerce.model.entities.CartItem;


import java.util.ArrayList;
import java.util.List;

public class FakeCartRepository extends CartRepository{

    List<CartItem> items = new ArrayList<>();


    public void saveItem(CartItem item) {

        items.add(item);
    }

    public void saveCartItems(Cart cart, List<CartItem> itemsLoaded){
        items = new ArrayList<>(itemsLoaded);
//       items.addAll(itemsLoaded) ;
    }

    @Override
    public List<CartItem> loadCartItemsFromFile(Cart cart) {
        return new ArrayList<>(items);
    }
    public void clear(){
        items.clear();
    }

}
