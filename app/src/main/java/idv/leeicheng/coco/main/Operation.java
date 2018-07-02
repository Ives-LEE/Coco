package idv.leeicheng.coco.main;

public class Operation {

    static String ADDITION = "+";
    static String SUBSTRACTION = "-";
    static String DIVISION = "×";
    static String TIMES = "÷";
    public static String compute(String str){
        Stack stack = new Stack(str.length());
        String[] data = transfer(str);
        for (int i = 0; i < data.length; i++) {
            if (data[i] == null) break;
            if (data[i].equals(ADDITION) || data[i].equals(SUBSTRACTION) || data[i].equals(DIVISION) || data[i].equals(TIMES)) {
                long b = Long.valueOf(stack.pop());
                long a = Long.valueOf(stack.pop());
                if (data[i].equals("+"))
                    stack.push("" + (a + b));
                else if (data[i].equals("-"))
                    stack.push("" + (a - b));
                else if (data[i].equals("×"))
                    stack.push("" + (a * b));
                else if (data[i].equals("÷"))
                    stack.push("" + (a / b));
            } else
                stack.push(data[i]);

        }
        return stack.pop();
    }
    public static String[] transfer(String data) {
        Stack stack = new Stack(data.length());
        int index = 0;
        String[] answer = new String[data.length()];
        for (int i = 0; i < data.length(); i++) {

            String opr = returnSplit(data, i);
            if (opr.length() > 1) {
                i += (opr.length() - 1);
            }
            if (opr.equals(ADDITION) || opr.equals(SUBSTRACTION) || opr.equals(TIMES) || opr.equals(DIVISION)) {
                while (stack.index != -1 && priority(opr) <= priority(stack.peep())) {
                    answer[index++] = stack.pop();
                }
                stack.push(opr);
            } else {
                answer[index++] = opr;
            }
        }
        while (stack.index != -1) {
            answer[index++] = stack.pop();
        }
        return answer;
    }

    public static String returnSplit(String data, int index)
    {
        String thisString = "";
        String s = String.valueOf(data.charAt(index));

        if (s.equals(ADDITION) || s.equals(SUBSTRACTION) || s.equals(TIMES) || s.equals(DIVISION)) {
            return s;
        } else {
            do {
                thisString += s;
                if (index + 1 == data.length()) break;
                s = String.valueOf(data.charAt(++index));
            }
            while (!s.equals(ADDITION) && !s.equals(SUBSTRACTION) && !s.equals(TIMES) && !s.equals(DIVISION));
            return thisString;
        }
    }

    public static int priority(String opr)
    {
        if (opr.equals(TIMES) || opr.equals(DIVISION)) return 2;
        else if (opr.equals(ADDITION) || opr.equals(SUBSTRACTION)) return 1;
        else return 0;
    }

}
