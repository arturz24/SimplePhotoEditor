package pl.com.smoke_weed_every_day.simplephotoeditor;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class Filter
{
	private int[][] filterMatrix;
	private int length;
	private int sum;
	public static int[][] shrinkFilter(int[][] notProcessed)
	{
		for(int i = 0; i < notProcessed.length ; i ++)
			if(notProcessed[i].length != notProcessed.length)
				throw new UnsupportedOperationException("Not a valid matrice dimensions!");
		int currentSize = notProcessed.length;
		int middle = notProcessed.length / 2;
		int i;
		outer:
		for(i = 0 ; i < middle ; i++)
		{
			for(int x = 0 ; x < currentSize ; x++)
			{
				if(notProcessed[i][i + x] != 0
						|| notProcessed[x + i][i] != 0
						|| notProcessed[notProcessed.length - 1 - i][x + i] != 0
						|| notProcessed[notProcessed.length - 1 - x - i][i] != 0)
					break outer;

			}
			currentSize -= 2;
		}
		int[][] shrinkedFilter = new int[currentSize][currentSize];
		for(int j = 0 ; j < currentSize ; j++)
			for(int k = 0; k < currentSize ; k++)
				shrinkedFilter[j][k] = notProcessed[j + i][k + i];

		return shrinkedFilter;
	}
	public Filter(final int[][] filterMatrix)
	{
		length = filterMatrix.length;
		this.filterMatrix = filterMatrix;
		for(int[] i : filterMatrix)
			for(int j : i)
				sum += j;
		if(sum == 0)
			sum = 1;
	}

	public Bitmap applyFilter(final Bitmap bitmap)
	{
		final int bmpWidth = bitmap.getWidth();
		int[] pixels = new int[bitmap.getHeight() * bitmap.getWidth()];
		int[] result = new int[bitmap.getHeight() * bitmap.getWidth()];
		bitmap.getPixels(pixels, 0, bmpWidth, 0, 0, bmpWidth, bitmap.getHeight());
		int hlr = (length)/2, currentFilterWeight;
		int rSum, gSum, bSum;
		for(int y = hlr; y < bitmap.getHeight() - hlr -1; y++)
			for(int x = hlr; x < bmpWidth - hlr -1; x++)
			{
				rSum = bSum = gSum = 0;
				for(int i = 0 ; i < length ; i++)
					for(int j = 0; j < length ; j++)
					{
						int pixel = pixels[(y + i - hlr) * bmpWidth + x + j - hlr];
						currentFilterWeight = filterMatrix[i][j];
						rSum += ((pixel >> 16) & 0xFF) * currentFilterWeight;
						gSum += ((pixel >> 8) & 0xFF) * currentFilterWeight;
						bSum += (pixel & 0xFF) * currentFilterWeight;
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
				if(rSum > 0xFF)
					rSum = 0xFF;
				if(gSum > 0xFF)
					gSum = 0xFF;
				if(bSum > 0xFF)
					bSum = 0xFF;
				int color = Color.argb(0xFF, rSum, gSum, bSum);
				result[y * bmpWidth + x] = color;
			}
		Bitmap resBmp = Bitmap.createBitmap(bmpWidth, bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		resBmp.setPixels(result, 0, bmpWidth, 0, 0, bmpWidth, bitmap.getHeight());
		return resBmp;
	}
}
