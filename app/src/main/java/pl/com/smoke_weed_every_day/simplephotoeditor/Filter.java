package pl.com.smoke_weed_every_day.simplephotoeditor;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class Filter
{
	private int[] filterMatrix;
	private int length;
	private int lengthRoot;
	private int sum;
	public Filter(final int[] filterMatrix)
	{
		double root = Math.sqrt((double)filterMatrix.length);
		Log.d("XD:", Double.toString(root));
		if((root - (int)root) != 0)
			throw new UnsupportedOperationException("Not a valid matrice dimension!");
		length = filterMatrix.length;
		lengthRoot = (int)root;
		this.filterMatrix = filterMatrix;
		for(int i : filterMatrix)
			sum += i;
		if(sum == 0)
			sum = 1;
	}

	public Bitmap applyFilter(final Bitmap bitmap)
	{
		Bitmap bitmapCopy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		int hlr = lengthRoot/2, currentFilterWeight;
		int rSum, gSum, bSum;
		for(int y = hlr; y < bitmap.getHeight() - hlr ; y++)
			for(int x = hlr; x < bitmap.getWidth() - hlr ; x++)
			{
				rSum = bSum = gSum = 0;
				for(int i = 0 ; i < length ; i++)
				{
					int pixel = bitmap.getPixel(x - 1 + i % lengthRoot, y - 1 + i / lengthRoot);
					currentFilterWeight = filterMatrix[i];
					rSum += Color.red(pixel) * currentFilterWeight;
					gSum += Color.green(pixel) * currentFilterWeight;
					bSum += Color.blue(pixel) * currentFilterWeight;
				}
				rSum /= sum;
				gSum /= sum;
				bSum /= sum;
				if(rSum < 0)
					rSum = 0;
				if(gSum < 0)
					gSum = 0;
				if(bSum < 0)
					bSum = 0;
				if(rSum >= 0xFF)
					rSum = 0;
				if(gSum >= 0xFF)
					gSum = 0;
				if(bSum >= 0xFF)
					bSum = 0;
				int color = Color.argb(0xFF, rSum, gSum, bSum);
				//Log.d("XD", "r " + rSum + " g " + gSum + " b " + bSum + " color " + color);
				bitmapCopy.setPixel(x, y, color);
			}
		return bitmapCopy;
	}
}
