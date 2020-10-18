package calculation;

/**
 * @author: Ewen
 * @program: java-advanced
 * @date: 2020/10/18 16:48
 * @description: 选做）、自己写一个简单的 Hello.java，里面需要涉及基本类型，四则运行，if 和 for，然后自己分析一下对应的字节码，有问题群里讨论
 */
public class ShowLocalVariableTest {

    public static void main(String[] args) {

        MovingAverage movingAverage = new MovingAverage();

        for (int index = 1; index < 3; index++) {
            movingAverage.submit(index);
        }

        double avg = movingAverage.getAvg();
    }
}
