package pl.com.smoke_weed_every_day.simplephotoeditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Arrays;
public class ShowImageActivity extends Activity
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
				Filter f = new Filter(
					new int[]
							{
								2, -1, 2,
								-1,  -4, -1,
								2, -1, 2,
							}
				);
				Bitmap bitmapCopy = f.applyFilter(bitmap);
				/*int[][] filter =
					{
						{-1,-1,-1},
						{-1, 8,-1},
						{-1,-1,-1},
					};
				for(int x = 1 ; x < bitmap.getWidth() - 1; x++)
					for(int y = 1 ; y < bitmap.getHeight() - 1; y++)
					{
						double rSum = 0, gSum = 0, bSum = 0;
						for(int fX = 0 ; fX < 3 ; fX++)
							for(int fY = 0 ; fY < 3 ; fY ++)
							{
								int current = bitmap.getPixel(x - 1 + fX, y - 1 + fY);
								double fWeight = (double)filter[fX][fY];
								rSum += fWeight * Color.red(current);
								gSum += fWeight * Color.green(current);
								bSum += fWeight * Color.blue(current);

								//sum += (double)filter[fX][fY] * (bitmap.getPixel(x - 1 + fX, y - 1 + fY) & 0x00FFFFFF);
							}
						//double divider = 27.0; //If summed masks value equals 0 divide by 1
						//rSum /= divider;
						//gSum /= divider;
						//bSum /= divider;
						if(rSum < 0)
							rSum += 255;
						if(rSum > 0xFF)
							rSum -= 0xFF;
						if(gSum < 0)
							gSum += 255;
						if(gSum > 0xFF)
							gSum -= 0xFF;
						if(bSum < 0)
							bSum += 255;
						if(bSum > 0xFF)
							bSum -= 0xFF;
						//int pixel = Color.argb((bitmap.getPixel(x, y) & 0xFF000000) >> 24, (int)rSum, (int)gSum, (int)bSum);
						int pixel = 0xFF000000 | (int)rSum << 16 | (int)gSum << 8 | (int)bSum;
						bitmapCopy.setPixel(x, y, pixel);
					}*/
				ImageView img = (ImageView) findViewById(R.id.bitmapViewer);
				img.setImageBitmap(bitmapCopy);
			}
		});
		manipulationButtons.addView(b);
	}
}
