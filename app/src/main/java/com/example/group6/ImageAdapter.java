package com.example.group6;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

    /**
     * Constructs an ImageAdapter (View) that connects to the Model and Controller
     * @param c Context
     */
    public ImageAdapter(Context c) {
        mContext = c;
        images = new ImageView[ROW_LENGTH][COLUMN_LENGTH];
    }

    @Override
    public int getCount() {
        return GRID_SIZE;
    }

    @Override
    public Object getItem(int position) {
        return images[position/8][position%8];
    }

    @Override
    public long getItemId(int position) {
        return position+1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(Resources.getSystem().getDisplayMetrics().widthPixels / 8, Resources.getSystem().getDisplayMetrics().widthPixels / 8));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
            imageView.setCropToPadding(true);
        }
        else
        {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(R.drawable.blank);

        images[position/8][position%8] = imageView;
        imageView.setBackgroundColor(NORMAL);
        return imageView;
    }

    /**
     * Changes the image to the image that represents the value in the model
     * @param row row number
     * @param column column number
     * @param value value in given position
     */
    public void changeEntry(int row, int column, int value)
    {
        images[row][column].setImageResource(R.drawable.blank);
    }

    /**
     * Gets the ImageView if it's allowed to be altered
     * @param position Position in the grid
     * @return ImageView
     */
    public View getChangeable(int position)
    {
        return getChangeable(position/8, position%8);
    }

    private View getChangeable(int row, int col)
    {
        if (row < ROW_LENGTH && row >=0 && col < COLUMN_LENGTH && col >=0)
        {
            return images[row][col];
        }

        return null;
    }

    /**
     * Gets the position of the chosen entry
     * @return position = integer from 0 to 80 inclusive
     */
    public int getChosenPosition() {return chosenPosition;}

    /**
     * Changes the image to a blank image to represent the deletion of the entry
     * @param row row number
     * @param col column number
     */
    public void deleteEntry(int row, int col)
    {
        images[row][col].setImageResource(R.drawable.blank);
    }

    /**
     * Function to reset the puzzle Grid
     */
    public void resetGrid()
    {
        //Iterate through all positions possible and reset them. 9x9 = 81
        for (int x = 0; x < 64; x++)
        {
            //Get ImageView to reset to blank.
            ImageView curChosen = (ImageView) this.getChangeable(x);
            if (curChosen != null)
            {
                //Reset to blank if it's not one of the default values
                curChosen.setImageResource(R.drawable.blank);
            }
        }
    }

    /**
     * Sets the chosen entry to a given position
     * @param position position
     */
    public void setChosen(int position)
    {
        chosenPosition = position;
    }



    private Context mContext;
    private final static int GRID_SIZE = 64;
    private final static int ROW_LENGTH = 8;
    private final static int COLUMN_LENGTH = 8;
    private final static int SQUARE_LENGTH = 3;
    private final ImageView[][] images;
    private int chosenPosition;

    private final static int NORMAL = Color.GRAY;
    private final static int CLASH = Color.RED;
    private final static int INCORRECT = Color.RED;
}
