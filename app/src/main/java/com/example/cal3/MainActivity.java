package com.example.cal3;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
//new
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.DecimalFormat;
//finish
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView solution;
    private String currentInput = "";
    private double operandA = 0;
    private double operandB = 0;
    private String operator = "";
    private boolean isOperatorPressed = false;
    private boolean isRadianMode = false;
    private DecimalFormat decimalFormat = new DecimalFormat("#.######");

//override??
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

        // 运算符按钮监听 check press
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
                double result = calculateResult(); // 这里可以调用计算结果的方法
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

        // Radian/Decimal 切换按钮 toggmode??
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

    // ok fins aqui

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
        String modeText = isRadianMode ? "Radian" : "Degree";
        solution.setText(modeText);  // 在切换时更新显示
    }

    private void clearInput() {
        currentInput = "";
        operandA = 0;
        operandB = 0;
        operator = "";
        isOperatorPressed = false;
        solution.setText("");


        //default
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}