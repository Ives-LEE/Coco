package idv.leeicheng.coco.main;

public class Stack {

    String[] stack;
    public int index;

    Stack(int max) {
        index = -1;
        stack = new String[max];
    }

    public void push(String data) {
        stack[++index] = data;
    }

    public String pop() {
        return stack[index--];
    }

    public String peep() {
        return stack[index];
    }

}
