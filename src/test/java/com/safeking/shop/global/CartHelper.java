package com.safeking.shop.global;

import com.safeking.shop.domain.cart.domain.repository.CartItemRepository;
import com.safeking.shop.domain.cart.domain.repository.CartRepository;
import com.safeking.shop.domain.cart.domain.service.CartItemService;
import com.safeking.shop.domain.cart.domain.service.CartService;
import com.safeking.shop.domain.cart.web.query.repository.CartQueryRepository;
import com.safeking.shop.domain.item.domain.entity.Category;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.entity.ItemPhoto;
import com.safeking.shop.domain.item.domain.repository.CategoryRepository;
import com.safeking.shop.domain.item.domain.repository.ItemPhotoRepository;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Commit;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartHelper {
    @Autowired
    CartQueryRepository cartQueryRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CustomBCryPasswordEncoder encoder;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CartService cartService;
    @Autowired
    CartItemService cartItemService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    TestUserHelper userHelper;
    @Autowired
    ItemPhotoRepository itemPhotoRepository;

    public List<Long> createTemporaryCartItem() {
        //1. 카테고리를 생성
        Category category1 = Category.create("중대사고 예방", 1);
        Category category2=Category.create("중대사고 예방",2);
        categoryRepository.save(category1);
        categoryRepository.save(category2);

        //2. member 생성
        Member member = userHelper.createMember();
        Member admin = userHelper.createADMIN();
        //3. 장바구니 생성
        cartService.createCart(member);
        cartService.createCart(admin);

        //3. item 10개 생성
        List<Long> putItemIdList=new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Item item = new Item();
            item.setPrice(100);
            item.setName("item" + i);
            item.setQuantity(i);
            itemRepository.save(item);

            ItemPhoto fileName = ItemPhoto.create("fileName", item);
            itemPhotoRepository.save(fileName   );

            item.setCategory(i % 2 == 0 ? category1 : category2);

            cartItemService.putCart(i % 2 == 0 ? member.getUsername() : admin.getUsername(), item.getId(), 3);

            if(i % 2 == 0) putItemIdList.add(item.getId());
        }

        return putItemIdList;
    }
}
