package com.vector.caffe.util;

import java.util.List;
import java.util.Map;

/**
 * Created by guo on 17-6-13.
 */

public interface DBBase<T> {

    List<Map<String, String>> queryAll();

    List<Map<String, String>> queryByCloumn();

    long insert(T t);

    long updateById(T t);

    long deleteById(T t);
}
