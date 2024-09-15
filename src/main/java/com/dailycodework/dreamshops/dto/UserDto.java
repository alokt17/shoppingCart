package com.dailycodework.dreamshops.dto;

import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.model.Order;
import lombok.Data;

import javax.smartcardio.Card;
import java.util.List;
@Data
public class UserDto {
    private Long id;
    private String firstname;
    private String lastName;
    private String email;
    private List<OrderDto> orders;
    private CartDto cart;

}
