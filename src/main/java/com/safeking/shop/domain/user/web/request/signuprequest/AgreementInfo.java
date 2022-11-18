package com.safeking.shop.domain.user.web.request.signuprequest;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AgreementInfo {
    @NotNull
    private Boolean userAgreement;
    @NotNull
    private Boolean infoAgreement;

}
