package com.niku.moneymate.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface TransactionType {
    int OUTCOME = -1;
    int INCOME = 1;
}
