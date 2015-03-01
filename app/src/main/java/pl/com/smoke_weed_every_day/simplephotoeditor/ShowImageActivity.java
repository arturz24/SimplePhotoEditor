package pl.com.smoke_weed_every_day.simplephotoeditor;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Arrays;

public class ShowImageActivity extends FragmentActivity implements FilterSettingsDialog.DialogListener
{
	private String path;
	private Bitmap bitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showimage);
		Intent intent = getIntent();
		path = intent.getStringExtra("ImageToLoad");
		try {
			bitmap = BitmapFactory.decodeFile(path);
			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888 ,true);
		} catch (Exception exc) {
			Log.d("XD", "EKSCEPCJA");
			throw new RuntimeException();
		}
		ImageView img = (ImageView) findViewById(R.id.bitmapViewer);
		img.setImageBitmap(bitmap);

		LinearLayout manipulationButtons = (LinearLayout) findViewById(R.id.manipulationButtons);
		Context c = manipulationButtons.getContext();
		Button b = new Button(c);
		b.setText("Czarno Biały");
		b.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				for(int x = 0 ; x < bitmap.getWidth(); x++)
					for(int y = 0 ; y < bitmap.getHeight(); y++)
					{
						int pixel = bitmap.getPixel(x, y);
						int desaturated = (int)(0.2126 * ((pixel & 0x00FF0000) >> 16)  + 0.7152*((pixel & 0x0000FF00) >> 8) + 0.0722*(pixel & 0x000000FF));
						desaturated = desaturated > 255 ? 255 : desaturated;
						pixel &= 0xFF000000;
						pixel = pixel | desaturated | desaturated << 8 | desaturated << 16;
						bitmap.setPixel(x, y, pixel);
					}
				ImageView img = (ImageView) findViewById(R.id.bitmapViewer);
				img.setImageBitmap(bitmap);
			}
		});
		manipulationButtons.addView(b);

		b = new Button(c);
		b.setText("Inwersja");
		b.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				for(int x = 0 ; x < bitmap.getWidth(); x++)
					for(int y = 0 ; y < bitmap.getHeight(); y++)
					{
						int pixel = bitmap.getPixel(x, y);
						int pixel2 = pixel & 0x00FFFFFF;
						pixel &= 0xFF000000;
						pixel2 = 0x00FFFFFF - pixel2;
						pixel = pixel2 | pixel;
						bitmap.setPixel(x, y, pixel);
					}
				ImageView img = (ImageView) findViewById(R.id.bitmapViewer);
				img.setImageBitmap(bitmap);
			}
		});
		manipulationButtons.addView(b);

		b = new Button(c);
		b.setText("LAPLASJAN KURWA");



		b.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				DialogFragment df = new FilterSettingsDialog();
				df.show(getFragmentManager(), "df");
			}
		});
		manipulationButtons.addView(b);
	}

	@Override
	public void onDialogPositiveClick(FilterSettingsDialog dialog)
	{
		int[][] shrinked = Filter.shrinkFilter(dialog.getSavedFilter());
		final Filter f = new Filter(shrinked);

		Bitmap bitmapCopy = f.applyFilter(bitmap);
		ImageView img = (ImageView) findViewById(R.id.bitmapViewer);
		img.setImageBitmap(bitmapCopy);
		bitmap = bitmapCopy;
	}
	public void onDialogNegativeClick(FilterSettingsDialog dialog)
	{

	}
}
