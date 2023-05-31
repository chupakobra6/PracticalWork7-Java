package com.example.practicalwork7;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView resultTextView, solutionTextView;
    MaterialButton buttonAC, buttonDelete, buttonChangeSign, buttonDivide, buttonMultiply, buttonMinus, buttonPlus, buttonPercent, buttonComma, buttonEqual;
    MaterialButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    MaterialButton buttonDegree, buttonSquare;

    private MyViewModel myViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        resultTextView = findViewById(R.id.textview_result);
        solutionTextView = findViewById(R.id.textview_solution);

        assignId(buttonAC, R.id.button_ac);
        assignId(buttonDelete, R.id.button_delete);
        assignId(buttonChangeSign, R.id.button_change_sign);
        assignId(buttonDivide, R.id.button_divide);
        assignId(buttonMultiply, R.id.button_multiply);
        assignId(buttonMinus, R.id.button_minus);
        assignId(buttonPlus, R.id.button_plus);
        assignId(buttonPercent, R.id.button_percent);
        assignId(buttonComma, R.id.button_comma);
        assignId(buttonEqual, R.id.button_equal);
        assignId(button0, R.id.button_0);
        assignId(button1, R.id.button_1);
        assignId(button2, R.id.button_2);
        assignId(button3, R.id.button_3);
        assignId(button4, R.id.button_4);
        assignId(button5, R.id.button_5);
        assignId(button6, R.id.button_6);
        assignId(button7, R.id.button_7);
        assignId(button8, R.id.button_8);
        assignId(button9, R.id.button_9);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            assignId(buttonDegree, R.id.button_degree);
            assignId(buttonSquare, R.id.button_square);
        }
    }

    void assignId(MaterialButton button, int id) {
        button = findViewById(id);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();

        if (buttonText.equals(",")) {
            buttonText = ".";
        }
        String dataToCalculate = solutionTextView.getText().toString();

        try {
            if (buttonText.equals("AC")) {
                solutionTextView.setText("");
                resultTextView.setText("0");
                return;
            }

            if (buttonText.equals("0") && solutionTextView.getText().toString().equals("0")) {
                return;
            }
            if (dataToCalculate.equals("0")) {
                dataToCalculate = "";
            }

            if (buttonText.equals("/")) {
                String currentText = solutionTextView.getText().toString();
                if (currentText.endsWith("/")) {
                    return;
                }
            }

            if (buttonText.equals("*")) {
                String currentText = solutionTextView.getText().toString();
                if (currentText.endsWith("*")) {
                    return;
                }
            }

            if (buttonText.equals("-")) {
                String currentText = solutionTextView.getText().toString();
                if (currentText.endsWith("-")) {
                    return;
                }
            }

            if (buttonText.equals("+")) {
                String currentText = solutionTextView.getText().toString();
                if (currentText.endsWith("+")) {
                    return;
                }
            }

            if (buttonText.equals("!")) {
                int value = Integer.parseInt(dataToCalculate);
                int factorial = 1;
                for (int i = 1; i <= value; i++) {
                    factorial *= i;
                }
                dataToCalculate = String.valueOf(factorial);
                solutionTextView.setText(dataToCalculate);
                resultTextView.setText(dataToCalculate);
                return;
            }

            if (buttonText.equals("+/-")) {
                if (dataToCalculate.startsWith("-")) {
                    dataToCalculate = dataToCalculate.substring(1);
                } else {
                    dataToCalculate = "-" + dataToCalculate;
                }
                solutionTextView.setText(dataToCalculate);
                resultTextView.setText(dataToCalculate);
                return;
            }

            if (buttonText.equals("**2")) {
                Double value = Double.parseDouble(dataToCalculate);
                value = Math.pow(value, 2);
                dataToCalculate = value.toString();
                solutionTextView.setText(dataToCalculate);
                resultTextView.setText(dataToCalculate);
                return;
            }

            if (buttonText.equals("%")) {
                Double value = Double.parseDouble(dataToCalculate);
                value /= 100;
                dataToCalculate = value.toString();
                solutionTextView.setText(dataToCalculate);
                resultTextView.setText(dataToCalculate);
                return;
            }

            if (buttonText.equals("C")) {
                dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
                if (dataToCalculate.length() == 1 || dataToCalculate.length() == 0) {
                    dataToCalculate = "0";
                }
            } else {
                if (buttonText.equals(",") && dataToCalculate.contains(".")) {
                    return;
                }
                dataToCalculate = dataToCalculate + buttonText;
            }

            if (buttonText.equals("=")) {
                solutionTextView.setText(resultTextView.getText());
                resultTextView.setText("");
                return;
            }

            solutionTextView.setText(dataToCalculate);
        } catch (Exception exception) {
            solutionTextView.setText("Error");
        }

        String finalResult = getResult(dataToCalculate);

        if (!finalResult.equals("Error")) {
            resultTextView.setText(finalResult);
        }
    }

    String getResult(String data) {
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            String finalResult = context.evaluateString(scriptable, data, "Javascript", 1, null).toString();

            if (finalResult.endsWith(".0")) {
                finalResult = finalResult.replace(".0", "");
            }

            return finalResult;
        } catch (Exception exception) {

            return "Error";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String savedSolution = myViewModel.getSavedSolution();
        String savedResult = myViewModel.getSavedResult();

        solutionTextView.setText(savedSolution);
        resultTextView.setText(savedResult);
    }

    @Override
    protected void onPause() {
        super.onPause();

        myViewModel.setData(solutionTextView.getText().toString(), resultTextView.getText().toString());
    }
}