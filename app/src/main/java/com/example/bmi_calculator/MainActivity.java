package com.example.bmi_calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    EditText weightInput, heightSingleInput, feetInput, inchInput;
    RadioGroup weightUnitGroup, heightTypeGroup;
    RadioButton kgBtn, lbBtn, cmBtn, meterBtn, feetInchBtn;
    LinearLayout feetInchLayout, resultLayoutBg;
    TextView bmiResult, bmiAdvice, bmiStatusLabel;
    CardView resultCard;
    Button calcBtn;
    ImageView resetIcon;

    final double NORMAL_MIN = 18.5;
    final double NORMAL_MAX = 24.9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weightInput = findViewById(R.id.weightInput);
        heightSingleInput = findViewById(R.id.heightSingleInput);
        feetInput = findViewById(R.id.feetInput);
        inchInput = findViewById(R.id.inchInput);
        weightUnitGroup = findViewById(R.id.weightUnitGroup);
        heightTypeGroup = findViewById(R.id.heightTypeGroup);
        kgBtn = findViewById(R.id.kgBtn);
        lbBtn = findViewById(R.id.lbBtn);
        cmBtn = findViewById(R.id.cmBtn);
        meterBtn = findViewById(R.id.meterBtn);
        feetInchBtn = findViewById(R.id.feetInchBtn);
        feetInchLayout = findViewById(R.id.feetInchLayout);

        resultCard = findViewById(R.id.resultCard);
        resultLayoutBg = findViewById(R.id.resultLayoutBg);
        bmiResult = findViewById(R.id.bmiResult);
        bmiAdvice = findViewById(R.id.bmiAdvice);
        bmiStatusLabel = findViewById(R.id.bmiStatusLabel);

        calcBtn = findViewById(R.id.calcBtn);
        resetIcon = findViewById(R.id.resetIcon);

        weightInput.setHint("Enter weight");

        heightTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.feetInchBtn) {
                feetInchLayout.setVisibility(View.VISIBLE);
                heightSingleInput.setVisibility(View.GONE);
            } else {
                feetInchLayout.setVisibility(View.GONE);
                heightSingleInput.setVisibility(View.VISIBLE);
                heightSingleInput.setHint(checkedId == R.id.cmBtn ? "Height in cm" : "Height in meters");
            }
        });

        calcBtn.setOnClickListener(v -> calculateBMI());

        resetIcon.setOnClickListener(v -> {
            weightInput.setText("");
            heightSingleInput.setText("");
            feetInput.setText("");
            inchInput.setText("");
            resultCard.setVisibility(View.GONE);
            kgBtn.setChecked(true);
            weightInput.setHint("Enter weight in kg");
            Toast.makeText(this, "Reset Done", Toast.LENGTH_SHORT).show();
        });
    }

    void calculateBMI() {
        if (weightInput.getText().toString().isEmpty()) {
            weightInput.setError("Enter weight");
            return;
        }

        double weight = Double.parseDouble(weightInput.getText().toString());
        if (lbBtn.isChecked()) weight = weight * 0.453592;

        double heightMeters = 0;

        if (feetInchBtn.isChecked()) {
            if (feetInput.getText().toString().isEmpty() || inchInput.getText().toString().isEmpty()) {
                Toast.makeText(this, "Enter feet and inches", Toast.LENGTH_SHORT).show();
                return;
            }
            double totalInches = (Double.parseDouble(feetInput.getText().toString()) * 12) + Double.parseDouble(inchInput.getText().toString());
            heightMeters = totalInches * 0.0254;
        } else {
            if (heightSingleInput.getText().toString().isEmpty()) {
                heightSingleInput.setError("Enter height");
                return;
            }
            heightMeters = Double.parseDouble(heightSingleInput.getText().toString());
            if (cmBtn.isChecked()) heightMeters /= 100;
        }

        double bmi = weight / (heightMeters * heightMeters);
        resultCard.setVisibility(View.VISIBLE);
        bmiResult.setText(String.format("%.2f", bmi));

        String status;
        int color;

        if (bmi < 18.5) {
            status = "Underweight";
            color = Color.parseColor("#FF9800");
        } else if (bmi < 25) {
            status = "Normal";
            color = Color.parseColor("#4CAF50");
        } else {
            status = "Overweight";
            color = Color.parseColor("#F44336");
        }

        bmiStatusLabel.setText(status);
        bmiStatusLabel.setBackgroundColor(color);
        bmiResult.setTextColor(color);

        resultLayoutBg.setBackgroundColor(Color.argb(30, Color.red(color), Color.green(color), Color.blue(color)));


        bmiAdvice.setTextColor(color);

        resultLayoutBg.setBackgroundColor(Color.argb(20, Color.red(color), Color.green(color), Color.blue(color)));

        double minWeight = NORMAL_MIN * heightMeters * heightMeters;
        double maxWeight = NORMAL_MAX * heightMeters * heightMeters;

        if (bmi < 18.5) {
            bmiAdvice.setText(String.format("You need to GAIN around %.1f kg.", minWeight - weight));
        } else if (bmi > 24.9) {
            bmiAdvice.setText(String.format("You need to LOSE around %.1f kg.", weight - maxWeight));
        } else {
            bmiAdvice.setText("Normal BMI! Keep it up.");
        }
    }
}