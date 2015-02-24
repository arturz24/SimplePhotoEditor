package pl.com.smoke_weed_every_day.simplephotoeditor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;

public class ChooseFileActivity extends Activity {

    private Bitmap mFolderIcon;
    private Bitmap mImageIcon;

    private Stack<String> prevoiusPaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_file);

        prevoiusPaths=new Stack<String>();

        setIcons();
        String mainDirectory= Environment.getExternalStorageDirectory().getAbsolutePath();
        showFilesInDirectory(mainDirectory);
    }

    private void setIcons()
    {
        BitmapFactory.Options options=new BitmapFactory.Options();

        options.inSampleSize=4;
        mFolderIcon= BitmapFactory.decodeResource(this.getResources(),R.drawable.choose_file_folder_icon,options);

        options.inSampleSize=4;
        mImageIcon=BitmapFactory.decodeResource(this.getResources(),R.drawable.choose_file_image_icon,options);
    }

    private void showFilesInDirectory(String directoryPath)
    {
        ScrollView scrollView =(ScrollView)findViewById(R.id.displayOfFilesScroll);
        scrollView.scrollTo(0, 0);

        LinearLayout displayOfFiles=(LinearLayout)findViewById(R.id.displayOfFiles);
        displayOfFiles.removeAllViews();

        File currentDirectory=new File(directoryPath);
        File[] files=currentDirectory.listFiles();

        Arrays.sort(files,new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                if(lhs.isDirectory()==true )
                {
                    if(rhs.isDirectory()==true)
                    {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                    else
                        return -1;
                }
                else
                {
                    if(rhs.isDirectory()==false)
                    {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                    else
                        return 1;
                }
            }
        });

        for(int i=0;i<files.length;i++)
        {
            String extension = MimeTypeMap.getFileExtensionFromUrl(files[i].getAbsolutePath());
            if(files[i].isDirectory()==true || extension.toLowerCase().equals("jpg") || extension.toLowerCase().equals("png"))
                setFileInDisplay(files[i], currentDirectory, displayOfFiles);
        }


    }

    @Override
    public void onBackPressed() {
        if(prevoiusPaths.empty()==true)
            super.onBackPressed();
        else
            showFilesInDirectory(prevoiusPaths.pop());
    }

    private void setFileInDisplay(File file,File mainFile, LinearLayout displayOfFiles)
    {
        LinearLayout baner=new LinearLayout(this);
        baner.setOrientation(LinearLayout.HORIZONTAL);
        baner.setBackgroundColor(Color.DKGRAY);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,20,0,0);
        baner.setLayoutParams(params);

        ImageButton imageButton=new ImageButton(this);

        if(file.isDirectory())
        {
            imageButton.setImageBitmap(mFolderIcon);
            imageButton.setOnClickListener(new directoryClickListener(file.getAbsolutePath(),mainFile.getAbsolutePath()));
        }
        else
        {
            imageButton.setImageBitmap(mImageIcon);
            imageButton.setOnClickListener(new ImageClickListener(file.getAbsolutePath()));
        }


        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        imageButton.setLayoutParams(params);

        TextView textView =new TextView(this);
        textView.setText(file.getName());
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        textView.setTextSize(25.0f);

        baner.addView(imageButton);
        baner.addView(textView);
        displayOfFiles.addView(baner);
    }

    private void startShowingImage(String file)
    {
        Intent intent =new Intent(this,ShowImageActivity.class);
        intent.putExtra("ImageToLoad",file);
        startActivity(intent);
    }

    private class ImageClickListener implements  View.OnClickListener {
        private String file;

        public ImageClickListener(String file)
        {
            this.file=file;
        }

        @Override
        public void onClick(View v) {
            startShowingImage(file);
        }
    }

    private class directoryClickListener implements View.OnClickListener
    {
        private String path;
        private String previousPath;

        public directoryClickListener(String path,String previousPath)
        {
            this.path=path;
            this.previousPath=previousPath;
        }

        @Override
        public void onClick(View v) {
            prevoiusPaths.push(previousPath);
            showFilesInDirectory(path);
        }
    }

}