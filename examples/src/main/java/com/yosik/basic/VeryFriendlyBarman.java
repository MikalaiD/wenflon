package com.yosik.basic;

import org.springframework.stereotype.Component;

@Component
public class VeryFriendlyBarman implements Barman{
    @Override
    public String pourTheDrinks() {
        return "Here is your drink, mate. How is life?";
    }
}
