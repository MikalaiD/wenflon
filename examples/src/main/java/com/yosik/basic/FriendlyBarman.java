package com.yosik.basic;

import org.springframework.stereotype.Component;

@Component
public class FriendlyBarman implements Barman{
    @Override
    public String pourTheDrinks() {
        return "Here is your drink, sir";
    }
}
