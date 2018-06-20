package com.udacity.nd.projects.bakingapp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udacity.nd.projects.bakingapp.data.Recipe;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    public static List<Recipe> parseRecipes(JSONArray jsonStr) throws IOException {
        if (jsonStr.length() == 0)
            return null;

        ObjectMapper om = new ObjectMapper();
        List<Recipe> recipes = new ArrayList<>();

        for (int i = 0; i < jsonStr.length(); i++) {
            Recipe recipe = om.readValue(jsonStr.optJSONObject(i).toString(), Recipe.class);
            recipes.add(recipe);
        }

        return recipes;
    }

    public static Recipe toRecipe(String jsonStr) throws IOException {
        if (jsonStr.length() == 0)
            return null;

        ObjectMapper om = new ObjectMapper();
        return om.readValue(jsonStr, Recipe.class);

    }

    public static String toJsonString(Recipe recipe) throws IOException {
        if (recipe == null)
            return null;

        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(recipe);

    }
}
