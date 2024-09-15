package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.exception.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.cart.CartItemService;
import com.dailycodework.dreamshops.service.cart.ICartService;
import com.dailycodework.dreamshops.service.user.IUserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {

    private final CartItemService cartItemService;
    private final ICartService cartService;
    private final IUserService userService;


    @PostMapping("/item/add")

    public ResponseEntity<ApiResponse>addItemToCart(@RequestParam Long productId,@RequestParam Integer quantity){
        try {
            User user= userService.getAuthenticatedUser();
            Cart cart=cartService.initializerNewCart(user);

            cartItemService.addItemToCart(cart.getId(),productId,quantity);
                    return ResponseEntity.ok(new ApiResponse("Add Item Success",null));
                }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }catch(JwtException e){
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(),null));
        }
    }



//    public ResponseEntity<ApiResponse>addItemToCart(@RequestParam(required = false) Long cartId,
//                                                    @RequestParam Long productId,
//                                                    @RequestParam Integer quantity){
//           try {
//               if (cartId==null) {
//                   cartId = cartService.initializerNewCart();
//               }
//                   cartItemService.addItemToCart(cartId,productId,quantity);
//                   return ResponseEntity.ok(new ApiResponse("Add Item Success",null));
//               }
//            catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
//        }
//    }

    @DeleteMapping("/{cart}/{cartId}/{item}/{itemId}/remove")

    public ResponseEntity<ApiResponse>removeItemFromCart(@PathVariable Long cartId,@PathVariable Long itemId){
        try {
            cartItemService.removeItemFromCart(cartId,itemId);
            return ResponseEntity.ok(new ApiResponse("Remove Item Success",null));
        } catch (ResourceNotFoundException e) {
           return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

@PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse>updateItemQuantity(@PathVariable Long cartId,
                                                         @PathVariable Long itemId,
                                                         @RequestParam Integer quantity){
        try {
            cartItemService.updateItemQuantity(cartId,itemId,quantity);
            return ResponseEntity.ok(new ApiResponse("Update Item Success",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("e.getMessage",null));
        }

    }
}
