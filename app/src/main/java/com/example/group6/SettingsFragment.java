package com.example.group6;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.fragment.NavHostFragment;

public class SettingsFragment extends Fragment {

    private ImageAdapter imageAdapter;
    private FragmentActivity activity;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SettingsFragment.this)
                        .navigate(R.id.action_SettingsFragment_to_MainMenuFragment);
            }
        });

        activity = this.getActivity();
        final GridView grid = getView().findViewById(R.id.grid_led_matrix);
        imageAdapter = new ImageAdapter(activity);
        grid.setAdapter(imageAdapter);

        createSaveButton();
        createGridClickable(grid);
    }

    /**
     * Creates the button that saves the settings
     */
    private void createSaveButton()
    {
        Button reset_grid = getView().findViewById(R.id.button_save);
        reset_grid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                imageAdapter.setMatrixPositions();
                boolean[][] test = imageAdapter.getMatrixPattern();
            }
        });
    }

    /**
     * Creates the toggles/cells for the grid.
     */
    private void createGridClickable(GridView grid)
    {
        //Toggles the elements of the grid
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                if (imageAdapter.getChangeable(position) != null)
                {
                    ImageView lastChosen = (ImageView) imageAdapter.getItem(imageAdapter.getChosenPosition());
                    lastChosen.clearColorFilter();

                    ImageView image = (ImageView) imageAdapter.getItem(position);

                    //Check the color and toggle.
                    ColorDrawable drawable = (ColorDrawable) image.getBackground();
                    int color = drawable.getColor();
                    if (color == Color.YELLOW)
                        image.setBackgroundColor(Color.GRAY);
                    else
                        image.setBackgroundColor(Color.YELLOW);

                    imageAdapter.setChosen(position);

                    //image.setColorFilter(Color.CYAN);
                }
            }
        });
    }
}
