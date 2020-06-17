package com.example.group6;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

public class ImageAdapter extends BaseAdapter {

    /**
     * Constructs an ImageAdapter (View) that connects to the Model and Controller
     * @param c Context
     */
    public ImageAdapter(Context c) {
        mContext = c;
        images = new ImageView[ROW_LENGTH][COLUMN_LENGTH];
        matrix_pattern = new boolean[ROW_LENGTH][COLUMN_LENGTH];
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
            imageView.setLayoutParams(new GridView.LayoutParams( 94, 94));

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
        imageView.setBackgroundResource(OFF);
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
        images[row][col].setBackgroundColor(OFF);
    }

    /**
     * Function to reset the puzzle Grid
     */
    public void resetGrid()
    {
        for (int x = 0; x < 64; x++)
        {
            //Get ImageView to reset to off.
            ImageView curChosen = (ImageView) this.getChangeable(x);
            if (curChosen != null)
            {
                //Reset to OFF
                curChosen.setBackgroundResource(OFF);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setMatrixPositions()
    {
        //Iterate through all positions possible and check if cell is turned on.
        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                ImageView curChosen = (ImageView) this.getChangeable(x,y);
                if (curChosen != null)
                {
                    GradientDrawable drawable = (GradientDrawable) curChosen.getBackground();
                    int[] color = drawable.getColors();
                    String hexColor = String.format("#%06X", (0xFFFFFF & color[0]));
                    //Set boolean value for the specific cell.
                    if (hexColor.equals("#FFEB3B")) matrix_pattern[x][y] = true;
                    else matrix_pattern[x][y] = false;
                }
            }
        }
    }

    public void loadMatrixPositions(boolean[][] pattern)
    {
        if (pattern != null) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if (pattern[x][y]) {
                        ImageView curChosen = (ImageView) this.getChangeable(x, y);

                        if (curChosen != null) {
                            curChosen.setBackgroundResource(ON);
                        }
                    }
                }
            }
        }
    }

    public boolean[][] getMatrixPattern()
    {
        return matrix_pattern;
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
    private final ImageView[][] images;
    private boolean[][] matrix_pattern;
    private int chosenPosition;

    private final static int OFF = R.drawable.off;
    private final static int ON = R.drawable.on;
}
