package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

	public static final String EXTRA_POSITION = "extra_position";
	private static final int DEFAULT_POSITION = -1;

	private Sandwich sandwich;
	private LinearLayout linearLayout;
	private ImageView imageView;
	private TextView originText;
	private TextView knownAsText;
	private TextView ingredientsText;
	private TextView description;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_detail);

		Intent intent = this.getIntent();
		if (intent == null) {
			closeOnError();
		}

		int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);

		if (position == DEFAULT_POSITION) {
			// EXTRA_POSITION not found in intent
			closeOnError();
			return;
		}

		String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
		String json = sandwiches[position];

		this.sandwich = JsonUtils.parseSandwichJson(json);

		if (this.sandwich == null) {
			// Sandwich data unavailable
			closeOnError();
			return;
		}

		this.setTitle(this.sandwich.getMainName());
		this.loadViews();
	}

	@Override
	protected void onStart() {
		super.onStart();

		// Load sandwich image
		Picasso.with(this)
			.load(sandwich.getImage())
			.into(this.imageView, new Callback() {
				@Override
				public void onSuccess() {
					populateUI();

					linearLayout.setVisibility(View.VISIBLE);
				}

				@Override
				public void onError() {
					closeOnError();
				}
			});
	}

	private void closeOnError() {
		finish();
		Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
	}

	private void loadViews() {
		this.imageView = this.findViewById(R.id.image_iv);
		this.originText = this.findViewById(R.id.origin_tv);
		this.knownAsText = this.findViewById(R.id.also_known_tv);
		this.ingredientsText = this.findViewById(R.id.ingredients_tv);
		this.description = this.findViewById(R.id.description_tv);
		this.linearLayout = this.findViewById(R.id.linear_layout);
	}

	private void populateUI() {
		this.originText.setText(this.sandwich.getPlaceOfOrigin());
		this.knownAsText.setText(String.join(", ", this.sandwich.getAlsoKnownAs()));
		this.ingredientsText.setText(String.join(", ", this.sandwich.getIngredients()));
		this.description.setText(this.sandwich.getDescription());
	}
}
