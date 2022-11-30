package com.safeking.shop.domain.cart.web.controller;

import com.safeking.shop.domain.cart.domain.repository.CartItemRepository;
import com.safeking.shop.domain.cart.domain.repository.CartRepository;
import com.safeking.shop.domain.cart.domain.service.CartItemService;
import com.safeking.shop.domain.cart.domain.service.CartService;
import com.safeking.shop.domain.cart.web.query.repository.CartQueryRepository;
import com.safeking.shop.domain.cart.web.query.service.CartQueryService;
import com.safeking.shop.domain.cart.web.request.BasicRequest;
import com.safeking.shop.domain.cart.web.response.CartItemResponse;
import com.safeking.shop.domain.cart.web.response.CartResponse;
import com.safeking.shop.global.jwt.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class CartController {
    private final CartService cartService;
    private final CartQueryRepository cartQueryRepository;
    private final CartItemService cartItemService;

    @PostMapping("user/cartItem")
    public void putCart(HttpServletRequest request, @RequestBody BasicRequest basicRequest){

        cartItemService.putCart(TokenUtils.getUsername(request), basicRequest.getItemId(), basicRequest.getCount());
    }

    @PatchMapping("user/cartItem")
    public void updateCartItem(HttpServletRequest request,@RequestBody BasicRequest basicRequest){

        cartItemService.updateCartItem(TokenUtils.getUsername(request),basicRequest.getItemId(), basicRequest.getCount());
    }

    @DeleteMapping("user/cartItem")
    public void deleteCartItem(HttpServletRequest request,Long... itemId){

        cartItemService.deleteCartItemFromCart(TokenUtils.getUsername(request),itemId);
    }
    
    @GetMapping("user/cart")
    public Page<CartItemResponse> showCartList(HttpServletRequest request, @PageableDefault(page = 0, size = 15) Pageable pageable) {
        return cartQueryRepository.searchAll(TokenUtils.getUsername(request), pageable);
    }
    @DeleteMapping("/user/cart/{username}")
    public void deleteCart(@PathVariable String username){

        cartService.deleteCart(username);

    }


}
