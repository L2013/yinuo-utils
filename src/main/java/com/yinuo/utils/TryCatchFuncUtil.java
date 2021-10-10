package com.yinuo.utils;

import com.yinuo.utils.base.Runner;
import lombok.extern.slf4j.Slf4j;

/**
 * @author liang
 * 简化try/catch语法书写，使代码更优雅
 * 使用前：try{System.out.println(10 / 0);}catch(Exception e){log.error(e.getMessage(),e);}
 * 使用后：TryCatchUtil.swallowException(() -> {System.out.println(10 / 0);});
 * 但因为使用了@FunctionalInterface,实际使用会受到一些限制，所以并不是所有场景都适用
 */
@Slf4j
public class TryCatchFuncUtil {
    public static void swallowException(Runner mainCode) {
        try {
            mainCode.run();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void swallowException(Runner mainCode, Runner finalCode) {
        try {
            mainCode.run();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            finalCode.run();
        }
    }

    public static void swallowThrowable(Runner mainCode) {
        try {
            mainCode.run();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void swallowThrowable(Runner mainCode, Runner finalCode) {
        try {
            mainCode.run();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        } finally {
            finalCode.run();
        }
    }
}
