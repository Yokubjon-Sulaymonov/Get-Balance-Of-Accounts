package com.willhill.transcript;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class BalanceResponse {

    private Double playableBalance;
    private String currencyCode;
    private Double accountBalance;
    private List<BonusBalances> bonusBalances;
}
