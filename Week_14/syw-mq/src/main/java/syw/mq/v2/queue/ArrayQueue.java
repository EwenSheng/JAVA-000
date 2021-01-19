package syw.mq.v2.queue;

/**
 * @author: Ewen-Sheng
 * @date: 2021/1/18 21:30
 * @description:
 */
public class ArrayQueue<T> {

    /**
     * 数组队列
     */
    private T[] items;

    /**
     * 数组大小
     */
    private int capacity = 0;

    /**
     * 表示对头的下标
     */
    /*private int front = 0;*/

    /**
     * 表示对尾下标
     */
    private int rear = 0;

    /**
     * @author: Ewen
     * @date: 2021/1/18 21:34
     * @param: [capacity]
     * @return:
     * @description: 申请一个大小为capacity的数组
     */
    public ArrayQueue(int capacity) {
        items = (T[]) new Object[capacity];
        this.capacity = capacity;
    }

    /**
     * @author: Ewen
     * @date: 2021/1/18 21:32
     * @param: [item]
     * @return: boolean
     * @description: 入队
     */
    public boolean offer(T t) {
        /* 表示队列已经满了 */
        if (rear == this.capacity) {
            return false;
        }
        items[rear] = t;
        ++rear;
        return true;
    }

    /**
     * @author: Ewen
     * @date: 2021/1/18 21:33
     * @param: []
     * @return: java.lang.String
     * @description: 出队
     */
    public T poll(int front) {
        /*表示队列为空 */
        if (front == rear) {
            return null;
        }
        /*String ret = items[front];
        ++front;
        return ret;*/
        return items[front];
    }
}
