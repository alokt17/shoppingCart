package com.dailycodework.dreamshops.service.cart;

import com.dailycodework.dreamshops.exception.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.model.CartItem;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.CartItemRepository;
import com.dailycodework.dreamshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {

    private final CartItemRepository cartItemRepository;
    private final IProductService productService;
    private final ICartService cartService;
    private final CartRepository cartRepository;


    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        // 1. Get the cart
        Cart cart = cartService.getCart(cartId);

        // 2. Get the product
        Product product = productService.getProductById(productId);

        // 3. Check if the product is already in the cart
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (cartItem == null) {
            // If no existing CartItem, create a new one
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setUnitPrice(product.getPrice());
        }

        // Update the quantity and total price
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setTotalPrice(); // Ensure this method calculates the total price using unitPrice and quantity

        // Add or update the CartItem in the cart
        cart.addItem(cartItem);

        // Save the CartItem and Cart
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart=cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId,productId);
                         cart.removeItem(itemToRemove);
                         cartRepository.save(cart);
                             }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart=cartService.getCart(cartId);
        cart.getItems()
                .stream()
                .filter(item-> item
                        .getProduct().getId().equals(productId)).findFirst()
                .ifPresent(item-> {item.setQuantity(quantity);
                item.setUnitPrice(item.getProduct().getPrice());
                item.setTotalPrice();

                });
        BigDecimal totalAmount=cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId){
        Cart cart= cartService.getCart(cartId);
        return cart.getItems().stream().filter(item->item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(()-> new ResourceNotFoundException("Product not Found"));
    }
}
