package com.safeking.shop.domain.payment.web;

import com.safeking.shop.domain.payment.web.client.dto.request.AuthData;
import com.safeking.shop.domain.payment.web.client.dto.response.AccessToken;
import com.safeking.shop.domain.payment.web.client.dto.response.IamportResponse;
import com.safeking.shop.domain.payment.web.client.dto.response.PagedPaymentAnnotation;
import retrofit2.Call;
import retrofit2.http.*;

public interface Iamport {

	@POST
	Call<IamportResponse<AccessToken>> token(
			@Body AuthData authData);

	// 결제내역 단건 조회
	@GET("/payments/{imp_uid}")
	Call<IamportResponse<PagedPaymentAnnotation>> paymentByImpUid(
			@Header("Authorization") String token,
			@Path("imp_uid") String imp_uid
	);
}
