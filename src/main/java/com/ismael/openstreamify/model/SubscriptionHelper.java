package com.ismael.openstreamify.model;

import com.google.gson.Gson;
import nl.martijndwars.webpush.Subscription;

public class SubscriptionHelper {

    public static Subscription fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Subscription.class);
    }
}
