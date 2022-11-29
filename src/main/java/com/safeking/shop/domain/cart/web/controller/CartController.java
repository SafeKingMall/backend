package com.safeking.shop.domain.cart.web.controller;

import com.safeking.shop.domain.cart.domain.repository.CartRepository;
import com.safeking.shop.domain.cart.domain.service.CartService;
import com.safeking.shop.domain.cart.web.query.repository.CartQueryRepository;
import com.safeking.shop.domain.cart.web.query.service.CartQueryService;
import com.safeking.shop.domain.cart.web.request.BasicRequest;
import com.safeking.shop.domain.cart.web.response.CartItemResponse;
import com.safeking.shop.domain.cart.web.response.CartResponse;
import com.safeking.shop.global.jwt.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class CartController {
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final CartQueryService cartQueryService;

    @PostMapping("user/cart")
    public void createCart(HttpServletRequest request){
        //merge 후에 회원이 생성시에 자동 생성으로 바꾸고 한번만 생성되도록 하자
        cartService.createCart(TokenUtils.getUsername(request));
    }

    @PostMapping("user/cartItem")
    public void putCart(HttpServletRequest request, @RequestBody BasicRequest basicRequest){

        cartService.putCart(TokenUtils.getUsername(request), basicRequest.getItemId(), basicRequest.getCount());
    }

    @PatchMapping("user/cartItem")
    public void updateCart(HttpServletRequest request,@RequestBody BasicRequest basicRequest){

        cartService.updateCartItem(TokenUtils.getUsername(request),basicRequest.getItemId(), basicRequest.getCount());
    }

    @DeleteMapping("user/cartItem")
    public void deleteCart(HttpServletRequest request,Long... itemId){

        cartService.deleteCartItemFromCart(TokenUtils.getUsername(request),itemId);
    }
    
    @GetMapping("user/cart")
    public CartResponse showCartList(HttpServletRequest request){
        return cartQueryService.showCart(TokenUtils.getUsername(request));
    }

    @DeleteMapping("/user/cart/{username}")
    public void deleteCart(@PathVariable String username){

        cartService.deleteCart(username);

    }


}
