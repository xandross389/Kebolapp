package com.inbitart.kebolapp.controller;

import android.support.annotation.NonNull;

import com.inbitart.kebolapp.model.Country;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by XandrOSS on 18/08/2016.
 */
public class CountryManager {

    /*
    * Return list of countries that has given countryCode
    * */
    public static List<String> resolveCountryCode(String countryCode) {
        List<String> countries = new ArrayList<>();

        if (countryCode.contains("+535")) {
            countries.add("Cuba");
        }
        if (countryCode.contains("+1")) {
            countries.add("Estados Unidos");
        }
        if (countryCode.contains("+49")) {
            countries.add("Alemania");
        }

        return countries;
    }
}
