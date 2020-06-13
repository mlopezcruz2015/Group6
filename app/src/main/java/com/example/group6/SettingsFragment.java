package com.example.group6;

import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.fragment.NavHostFragment;

public class SettingsFragment extends Fragment {

    private ImageAdapter imageAdapter;
    private FragmentActivity activity;
    private SharedPreferences prefs;
    private Settings settings;

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

        //Preferences is where the saved settings are.
        prefs = this.getActivity().getSharedPreferences("com.example.group6.ledsettings", this.getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        settings = new Settings(prefs, editor);

        final GridView grid = getView().findViewById(R.id.grid_led_matrix);
        imageAdapter = new ImageAdapter(activity);
        grid.setAdapter(imageAdapter);

        createSaveButton();
        createSetDefaultsButton();
        createGridClickable(grid);

        updateSettingsControls(settings.getUseCustomPattern(), settings.getSensitivity(), settings.getLEDPattern());
    }

    public void updateSettingsControls(boolean useCustomPattern, int sensitivity, boolean[][] LEDPattern)
    {
        updateUseCustomPatternSelection(useCustomPattern);
        updateSensitivitySelection(sensitivity);
        updateLEDPattern(LEDPattern);
    }

    private void updateLEDPattern(boolean[][] LEDPattern)
    {
        imageAdapter.loadMatrixPositions(LEDPattern);
    }

    /**
     * Creates the button that saves the settings
     */
    private void createSaveButton()
    {
        Button save_button = getView().findViewById(R.id.button_save);
        save_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                //Save ImageAdapter positions
                imageAdapter.setMatrixPositions();
                settings.setLEDPattern(imageAdapter.getMatrixPattern());

                //Save other settings
                settings.setUseCustomPattern(((RadioButton)getView().findViewById(R.id.radiobutton_custom_pattern)).isChecked());

                int rbID = ((RadioGroup)getView().findViewById(R.id.radiogroup_sensitivity)).getCheckedRadioButtonId();
                switch(rbID)
                {
                    case(R.id.radiobutton_low):
                        settings.setSensitivity(0);
                        break;
                    case(R.id.radiobutton_med):
                        settings.setSensitivity(1);
                        break;
                    case(R.id.radiobutton_high):
                        settings.setSensitivity(2);
                        break;
                    default:
                        settings.setSensitivity(1);
                        break;
                }

                new AlertDialog.Builder(getActivity())
                        .setTitle("Settings Saved Successfully!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                            }
                        }).show();
            }
        });
    }

    /**
     * Creates the button that sets to defaults
     */
    private void createSetDefaultsButton() {
        Button reset_defaults_button = getView().findViewById(R.id.button_reset_to_defaults);
        reset_defaults_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ((RadioButton) getView().findViewById(R.id.radiobutton_default_pattern)).setChecked(true);
                ((RadioButton) ((RadioGroup) getView().findViewById(R.id.radiogroup_sensitivity)).getChildAt(1)).setChecked(true);


            }
        });
    }

    public void updateUseCustomPatternSelection(boolean useCustomPattern)
    {
        RadioButton rb;
        if (useCustomPattern)
        {
            rb = getView().findViewById(R.id.radiobutton_custom_pattern);
        }
        else
        {
            rb = getView().findViewById(R.id.radiobutton_default_pattern);
        }

        rb.setChecked(true);
    }

    public void updateSensitivitySelection(int sensitivity)
    {
        if (sensitivity < 0)
            sensitivity = 0;
        else if (sensitivity > 2)
            sensitivity = 2;


        ((RadioButton) ((RadioGroup)this.getView().findViewById(R.id.radiogroup_sensitivity)).getChildAt(sensitivity)).setChecked(true);
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
