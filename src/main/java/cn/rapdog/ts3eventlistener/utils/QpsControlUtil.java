package cn.rapdog.ts3eventlistener.utils;


import java.util.Arrays;
import java.util.concurrent.TimeUnit;


public class QpsControlUtil {
    /**
     * 接受请求窗口
     */
    private Long[] accessWindow;
    /**
     * qp5s限制次数
     */
    private static final int limit = 1;
    /**
     * 指针位置
     */
    private int curPosition;
    /**
     * 时间间隔
     */
    private static final long PERIOD = TimeUnit.SECONDS.toMillis(5);

    private final Object lock = new Object();

    private static class SingletonHolder {
        private static final QpsControlUtil SINGLETON = new QpsControlUtil();
    }

    /**
     * 单例方法入口
     * @return 单例类
     */
    public static QpsControlUtil getInstance() {
        return SingletonHolder.SINGLETON;
    }

    /**
     * 1秒内最多20次请求
     */
    public QpsControlUtil() {
        curPosition = 0;
        accessWindow = new Long[limit];
        Arrays.fill(accessWindow, 0L);
    }

    public boolean isPass() {
        long curTime = System.currentTimeMillis();
        synchronized (lock) {
            if (curTime >= PERIOD + accessWindow[curPosition]) {
                accessWindow[curPosition++] = curTime;
                curPosition = curPosition % limit;
                return true;
            } else {
                return false;
            }
        }
    }
}