package holo.android.permission.sample;

import org.junit.Test;

import java.util.Stack;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
//        cal("(1+((2*3)-(5+4)))");
//
//        cal("1+2*3-(5+4)");
        cal("1+2*3-5+4-6+8");
    }

    private void cal(String temp) {
        Stack<String> ops = new Stack<>();
        Stack<Double> value = new Stack<>();

//        Stack<String>[] ff = new Stack<String>[4];

        for (int i = 0; i < temp.length(); i++) {
            char item = temp.charAt(i);

            if (item == '(') {

            } else if (item == '+' || item == '-' || item == '*' || item == '/') {
                if ((item == '+' || item == '-') && !ops.isEmpty() && value.size() > 1) {
                    double v1 = value.pop();
                    String op1 = ops.pop();
                    double v2 = value.pop();
                    System.out.println("计算" + 2 + op1 + v1);
                    //反括号求职
                    if (op1.equals("+")) {
                        value.push(v2 + v1);
                    } else if (op1.equals("-")) {
                        value.push(v2 - v1);
                    } else if (op1.equals("*")) {
                        value.push(v2 * v1);
                    } else if (op1.equals("/")) {
                        value.push(v2 / v1);
                    }

                }

                ops.push(String.valueOf(item));
            } else if (item == ')') {
                double v1 = value.pop();
                String op1 = ops.pop();
                double v2 = value.pop();
                System.out.println("计算" + v2 + op1 + v1);

                try {
                    //反括号求职
                    if (op1.equals("+")) {
                        value.push(v2 + v1);
                    } else if (op1.equals("-")) {
                        value.push(v2 - v1);
                    } else if (op1.equals("*")) {
                        value.push(v2 * v1);
                    } else if (op1.equals("/")) {
                        value.push(v2 / v1);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                value.push(Double.parseDouble(String.valueOf(item)));
            }
        }

        while (!ops.isEmpty()) {
            double v1 = value.pop();
            String op1 = ops.pop();
            double v2 = value.pop();
            System.out.println("计算" + v2 + op1 + v1);
            try {
                //反括号求职
                if (op1.equals("+")) {
                    value.push(v2 + v1);
                } else if (op1.equals("-")) {
                    value.push(v2 - v1);
                } else if (op1.equals("*")) {
                    value.push(v2 * v1);
                } else if (op1.equals("/")) {
                    value.push(v2 / v1);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("表达式" + temp + "=" + value.pop());
    }

}