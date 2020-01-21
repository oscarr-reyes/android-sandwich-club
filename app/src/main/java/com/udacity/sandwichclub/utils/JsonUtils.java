package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonUtils {
	private static final String TAG = JsonUtils.class.getSimpleName();


	public static Sandwich parseSandwichJson(String json) {
		Sandwich sandwich = new Sandwich();

		try {
			JSONObject sandwichObject = new JSONObject(json);

			parseSandwichName(sandwich, sandwichObject.getJSONObject("name"));

			if (sandwichObject.has("placeOfOrigin"))
				sandwich.setPlaceOfOrigin(sandwichObject.getString("placeOfOrigin"));
			if (sandwichObject.has("description"))
				sandwich.setDescription(sandwichObject.getString("description"));
			if (sandwichObject.has("image")) sandwich.setImage(sandwichObject.getString("image"));

			if (sandwichObject.has("ingredients")) {
				final JSONArray jsonArray = sandwichObject.getJSONArray("ingredients");

				sandwich.setIngredients(parseJsonArray(jsonArray));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.w(TAG, String.format("Unable to parse JSON object. reason: %s", e.getMessage()));

			return null;
		}

		return sandwich;
	}

	/**
	 * Parses an array of strings into a list of strings
	 * @param array The JSON Array of strings
	 * @return The list of strings from the array
	 */
	private static List<String> parseJsonArray(JSONArray array) {
		List<String> list = new ArrayList<>();

		try {
			for (int i = 0; i < array.length(); i++) {
				final String element = array.getString(i);

				list.add(element);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.w(TAG, String.format("Unable to parse array. reason: %s", e.getMessage()));

			return Collections.emptyList();
		}

		return list;
	}

	/**
	 * Parses JSON sub-object "name" into the instance's fields
	 * @param instance The instance where the values are going to be set
	 * @param object The JSON object where the values are going to get extracted from
	 * @throws JSONException If one of the expected values does not exist in the sub-object
	 */
	private static void parseSandwichName(Sandwich instance, JSONObject object) throws JSONException {
		if (object != null) {
			if (object.has("mainName")) {
				instance.setMainName(object.getString("mainName"));
			}

			if (object.has("alsoKnownAs")) {
				final List<String> array = parseJsonArray(object.getJSONArray("alsoKnownAs"));

				instance.setAlsoKnownAs(array);
			}
		}
	}
}
