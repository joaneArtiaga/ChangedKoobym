package com.example.joane14.myapplication.Activities;



import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Joane14 on 27/07/2017.
 */


public class GsonDateDeserializer implements JsonDeserializer<Date> {

    private static GsonDateDeserializer instance;

    public static GsonDateDeserializer getInstance() {
        if (instance == null) {
            instance = new GsonDateDeserializer();
        }

        return instance;
    }

    private static final String[] DATE_FORMATS = new String[]{
            "yyyy-MM-dd"
    };

    @Override
    public Date deserialize(JsonElement jsonElement, Type typeOF,
                            JsonDeserializationContext context) throws JsonParseException {
        for (String format : DATE_FORMATS) {
            try {
                return new SimpleDateFormat(format, Locale.US).parse(jsonElement.getAsString());
            } catch (ParseException e) {
            }
        }
        throw new JsonParseException("Unparseable date: \"" + jsonElement.getAsString()
                + "\". Supported formats: " + Arrays.toString(DATE_FORMATS));
    }

}