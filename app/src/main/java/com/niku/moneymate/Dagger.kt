package com.niku.moneymate

import com.niku.moneymate.currency.MainCurrency
import dagger.Component
import dagger.Module

@Component()
interface AppComponent {
    fun currency(): MainCurrency
}

@Module
class AppModule {

}