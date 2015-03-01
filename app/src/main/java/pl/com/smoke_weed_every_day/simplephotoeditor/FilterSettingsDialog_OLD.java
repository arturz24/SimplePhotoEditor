package pl.com.smoke_weed_every_day.simplephotoeditor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

public class FilterSettingsDialog_OLD
{
	private static final int MATRIX_DIM = 7;

	public static AlertDialog.Builder getBuilder(Context context)
	{
		AlertDialog.Builder b =  new AlertDialog.Builder(context);
		TableLayout tableLayout = new TableLayout(context);
		tableLayout.setLayoutParams(new TableLayout.LayoutParams(7, 7));
		tableLayout.setPadding(1,1,1,1);
		for(int y = 0; y < MATRIX_DIM ; y++)
		{
			TableRow tr = new TableRow(context);
			for(int x = 0 ; x < MATRIX_DIM ; x++)
			{
				EditText editText = new EditText(context);
				editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
				editText.setWidth(80);
				editText.setHeight(100);
				editText.setId(x*y);
				tr.addView(editText, 80, 100);
			}
			tableLayout.addView(tr);
		}
		b.setTitle("Filter Settings")
		.setMessage("Choose your filter mask size and its values!")
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which)
			{

			}
		})
		.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which)
			{
				// do nothing
			}
		})
		.setView(tableLayout);
		return b;
	}

}