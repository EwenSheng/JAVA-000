package calculation;

/**
 * @author: Ewen
 * @program: java-advanced
 * @date: 2020/10/18 16:45
 * @description:
 */
public class MovingAverage {

    private int count = 0;

    private double sum = 0.0d;

    public void submit(double value) {
        this.count += 2;
        this.sum += count;
    }

    public double getAvg() {
        if (0 == count) {
            return sum;
        }
        return this.sum / this.count;
    }
}
