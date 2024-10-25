package com.example.cal3;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView solution;
    private String currentInput = "";
    private double operandA = 0;
    private double operandB = 0;
    private String operator = "";
    private boolean isOperatorPressed = false;
    private boolean isRadianMode = false; // 初始为 degree to radian 模式
    private DecimalFormat decimalFormat = new DecimalFormat("#.######");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        solution = findViewById(R.id.solution);

        // 数字按钮监听
        findViewById(R.id.one).setOnClickListener(v -> appendNumber("1"));
        findViewById(R.id.two).setOnClickListener(v -> appendNumber("2"));
        findViewById(R.id.three).setOnClickListener(v -> appendNumber("3"));
        findViewById(R.id.four).setOnClickListener(v -> appendNumber("4"));
        findViewById(R.id.five).setOnClickListener(v -> appendNumber("5"));
        findViewById(R.id.six).setOnClickListener(v -> appendNumber("6"));
        findViewById(R.id.seven).setOnClickListener(v -> appendNumber("7"));
        findViewById(R.id.eight).setOnClickListener(v -> appendNumber("8"));
        findViewById(R.id.nine).setOnClickListener(v -> appendNumber("9"));
        findViewById(R.id.zero).setOnClickListener(v -> appendNumber("0"));
        findViewById(R.id.point).setOnClickListener(v -> appendNumber("."));

        // 运算符按钮监听
        findViewById(R.id.plus).setOnClickListener(v -> selectOperator("+"));
        findViewById(R.id.minus).setOnClickListener(v -> selectOperator("-"));
        findViewById(R.id.mult).setOnClickListener(v -> selectOperator("*"));
        findViewById(R.id.dev).setOnClickListener(v -> selectOperator("/"));

        // 三角函数按钮监听
        findViewById(R.id.SIN).setOnClickListener(v -> calculateTrigFunction("sin"));
        findViewById(R.id.COS).setOnClickListener(v -> calculateTrigFunction("cos"));
        findViewById(R.id.TG).setOnClickListener(v -> calculateTrigFunction("tan"));

        // 等号按钮监听
        findViewById(R.id.equal).setOnClickListener(v -> {
            if (isOperatorPressed) {
                // 如果按下的是运算符，使用结果更新 operandA
                double result = calculateResult();
                solution.setText(decimalFormat.format(result));
                operandA = result;  // 更新 operandA 为计算结果
                currentInput = "";   // 清空当前输入，等待新的输入
            } else {
                // 处理等号的计算逻辑
                calculateResult(); // 直接计算结果并更新显示
            }
        });

        // 清除按钮监听
        findViewById(R.id.CLR).setOnClickListener(v -> clearInput());

        // Radian/Decimal 切换按钮
        findViewById(R.id.RD).setOnClickListener(v -> toggleMode());
    }

    private void appendNumber(String number) {
        currentInput += number;
        solution.setText(currentInput);
    }

    private void selectOperator(String selectedOperator) {
        // 如果当前输入为空，尝试从 TextView 获取结果作为 operandA
        if (currentInput.isEmpty() && !solution.getText().toString().isEmpty()) {
            operandA = Double.parseDouble(solution.getText().toString()); // 从 TextView 获取上次结果
        } else if (!currentInput.isEmpty()) {
            operandA = Double.parseDouble(currentInput); // 正常输入的数字作为 operandA
        }

        // 记录运算符
        operator = selectedOperator;

        // 清空 currentInput 以便输入新的数字
        currentInput = "";
        isOperatorPressed = true;
    }

    private double calculateResult() {
        // 确保有有效输入
        if (currentInput.isEmpty() || !isOperatorPressed) {
            return 0; // 如果没有输入或没有运算符，返回0
        }

        operandB = Double.parseDouble(currentInput);
        double result = 0;

        // 根据运算符计算结果
        switch (operator) {
            case "+":
                result = operandA + operandB;
                break;
            case "-":
                result = operandA - operandB;
                break;
            case "*":
                result = operandA * operandB;
                break;
            case "/":
                if (operandB != 0) {
                    result = operandA / operandB;
                } else {
                    solution.setText("Error");
                    return 0;
                }
                break;
        }

        // 清理输入并更新状态
        currentInput = "";
        isOperatorPressed = false; // 重置运算符状态
        return result; // 返回计算结果
    }

    private void calculateTrigFunction(String function) {
        // 从 TextView 直接获取输入值
        String textViewValue = solution.getText().toString();

        // 检查 TextView 是否为空
        if (!textViewValue.isEmpty()) {
            double input = Double.parseDouble(textViewValue); // 将 TextView 的值转换为 double
            double result = 0;

            if (!isRadianMode) {
                input = Math.toRadians(input);  // 如果是角度模式，转换为弧度
            }
            switch (function) {
                case "sin":
                    result = Math.sin(input);
                    break;
                case "cos":
                    result = Math.cos(input);
                    break;
                case "tan":
                    result = Math.tan(input);
                    break;
            }
            solution.setText(decimalFormat.format(result));
            currentInput = "";
        }
    }

    private void toggleMode() {

        isRadianMode = !isRadianMode;  // 切换模式

        // 更新显示模式文本
        String modeText = isRadianMode ? "Radian to Degree" : "Degree to Radian";
        solution.setText(modeText); // 只显示模式文本

        // 如果切换到度数模式，确保转换值为角度（如果当前有值）
        if (currentInput.isEmpty()) {
            return; // 如果当前没有输入，直接返回
        }

        double value = Double.parseDouble(currentInput);
        if (isRadianMode) {
            // 如果切换到弧度模式，进行角度到弧度的转换
            value = Math.toRadians(value);
        } else {
            // 如果切换到度数模式，进行弧度到角度的转换
            value = Math.toDegrees(value);
        }

        // 显示转换后的值
        solution.setText(decimalFormat.format(value)); // 更新显示为转换后的值
        currentInput = ""; // 清空输入以便用户可以输入新的值
    }

    private void clearInput() {
        currentInput = "";
        operandA = 0;
        operandB = 0;
        operator = "";
        isOperatorPressed = false;
        isRadianMode = false; // 重置为默认模式
        solution.setText("");
    }
}