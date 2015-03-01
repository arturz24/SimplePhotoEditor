package pl.com.smoke_weed_every_day.simplephotoeditor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.regex.Pattern;

public class FilterSettingsDialog extends DialogFragment
{
	private final int MATRIX_DIM = 7;
	private DialogListener dialogListener;
	private EditText[] editTextFields;
	public interface DialogListener
	{
		public void onDialogPositiveClick(FilterSettingsDialog dialog);
		public void onDialogNegativeClick(FilterSettingsDialog dialog);
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		editTextFields = new EditText[MATRIX_DIM * MATRIX_DIM];
		try
		{
			dialogListener = (DialogListener)activity;
		}
		catch (ClassCastException exception)
		{
			throw exception;
		}
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Activity currentActivity = getActivity();
		AlertDialog.Builder b = new AlertDialog.Builder(currentActivity);
		TableLayout tableLayout = new TableLayout(currentActivity);
		tableLayout.setLayoutParams(new TableLayout.LayoutParams(7, 7));
		tableLayout.setPadding(1,1,1,1);
		for(int y = 0; y < MATRIX_DIM ; y++)
		{
			TableRow tr = new TableRow(currentActivity);
			for(int x = 0 ; x < MATRIX_DIM ; x++)
			{
				EditText editText = new EditText(currentActivity);
				editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
				editText.setWidth(80);
				editText.setHeight(100);
				editText.addTextChangedListener(
					new TextWatcher() {
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after)
						{

						}

						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count)
						{

						}

						@Override
						public void afterTextChanged(Editable s)
						{

							if(!Pattern.matches("-?[0-9]{0,4}", s))
							{
								Log.d("ELO", "JESTĘ");
								s.delete(s.length()- 1, s.length());
							}
						}
					}
				);
				editTextFields[y * MATRIX_DIM + x] = editText;
				tr.addView(editText, 80, 100);
			}
			tableLayout.addView(tr);
		}
		b.setTitle("Filter Settings")
				.setMessage("Choose your filter mask size and its values!")
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which)
					{
						dialogListener.onDialogPositiveClick(FilterSettingsDialog.this);
					}
				})
				.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which)
					{
						dialogListener.onDialogNegativeClick(FilterSettingsDialog.this);
					}
				})
				.setView(tableLayout);
		return b.create();
	}

	public int[][] getSavedFilter()
	{
		int[][] savedFilter = new int[MATRIX_DIM][MATRIX_DIM];
		for(int x = 0 ; x < savedFilter.length ; x++)
		{
			for(int y = 0 ; y < savedFilter.length ; y++)
			{
				String text = editTextFields[y * MATRIX_DIM + x].getText().toString();
				savedFilter[y][x] = text.equals("") ? 0 : Integer.decode(text);
			}
		}
		return savedFilter;
	}
}
