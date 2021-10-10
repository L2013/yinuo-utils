package com.yinuo.utils;

import java.util.function.Consumer;

/**
 * @author liang
 * 某些场景我们需要对对象进行一些处理后，然后返回它，代码如下：
 * Object o = new Object();
 * o.setX()
 * return o;
 * 采用ObjectFuncUtil后写法如下：
 * return ObjectFuncUtil.returnByDo(o,(t)-> t.setX());
 */
public class ObjectFuncUtil {
    public static <T> T returnByDo(T t, Consumer<T> consumer) {
        consumer.accept(t);
        return t;
    }
}
