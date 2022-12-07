package com.safeking.shop.domain.user.web.request.signuprequest;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class AgreementInfo {
    @NotNull
    private Boolean userAgreement;
    @NotNull
    private Boolean infoAgreement;

}
