package com.niku.moneymate

import com.niku.moneymate.category.Category
import com.niku.moneymate.currency.MainCurrency
import dagger.Component
import dagger.Module

@Component()
interface AppComponent {
    //fun currency(): MainCurrency
    //fun category(): Category
}

@Module
class AppModule {

}