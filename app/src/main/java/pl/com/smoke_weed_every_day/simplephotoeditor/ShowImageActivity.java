package pl.com.smoke_weed_every_day.simplephotoeditor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import java.util.Arrays;
public class ShowImageActivity extends Activity
{
	private String path;
	private Bitmap bitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showimage);
		Intent intent = getIntent();
		path = intent.getStringExtra("ImageToLoad");
		try
		{
			bitmap = BitmapFactory.decodeFile(path);
		}
		catch(Exception exc)
		{
			Log.d("XD", "EKSCEPCJA");
			throw new RuntimeException();
		}
		ImageView img = (ImageView) findViewById(R.id.bitmapViewer);
		img.setImageBitmap(bitmap);
	}
}
