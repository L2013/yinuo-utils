package com.yinuo.utils.toolbox;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReadConfig;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author liangliang
 * 对于比较通常的CSV格式（包含标题栏，且标题栏为一行），可以采用该类简化操作
 * 如果标题栏字段命名与类的字段名存在自然映射（要求标题名为SOME_FIELD, 字段名为someField）
 * 则可不必传入标题栏各个字段的名称
 * 如果标题栏字段命名与类的字段名不能直接映射，则需依次指定各列映射到的类的字段名称
 */
public class CsvKit {
    private static Map<String, String> toLowerCamelCaseMap(Map<String, String> maps) {
        Map<String, String> result = MapUtil.newHashMap();
        for (Map.Entry<String, String> entity : maps.entrySet()) {
            result.put(StrUtil.toCamelCase(entity.getKey().toLowerCase()), entity.getValue());
        }
        return result;
    }

    public static <T> List<T> readBeanListFromCsv(String pathName, Class<T> beanClass) {
        return readBeanListFromCsv(getCsvDataFromFile(pathName), beanClass);
    }

    public static <T> List<T> readBeanListFromCsv(CsvData csvData, Class<T> beanClass) {
        List<T> list = new ArrayList<T>();
        for (CsvRow row : csvData.getRows()) {
            list.add(BeanUtil.fillBeanWithMap(toLowerCamelCaseMap(row.getFieldMap()), ReflectUtil.newInstanceIfPossible(beanClass), true));
        }
        return list;
    }

    public static <T> List<T> readBeanListFromCsvData(CsvData csvData, String[] keys, Class<T> beanClass) {
        List<CsvRow> rows = csvData.getRows();
        List<T> list = new ArrayList<T>(rows.size());
        for (int r = 1; r < rows.size(); r++) {
            CsvRow row = rows.get(r);
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < keys.length; i++) {
                map.put(keys[i], StrUtil.nullToEmpty(row.get(i)).trim());
            }
            list.add(BeanUtil.fillBeanWithMap(map, ReflectUtil.newInstanceIfPossible(beanClass), true));
        }
        return list;
    }

    public static <T> List<T> readBeanListFromCsv(String pathName, String[] keys, Class<T> beanClass) {
        return readBeanListFromCsvData(getCsvDataFromFile(pathName), keys, beanClass);
    }

    public static CsvData getCsvDataFromFile(String pathName) {
        return getCsvDataFromFile(pathName, StandardCharsets.UTF_8);
    }

    public static CsvData getCsvDataFromFile(String pathName, Charset charset) {
        CsvReadConfig config = CsvReadConfig.defaultConfig();
        config.setContainsHeader(true);
        return CsvUtil.getReader(config).read(new File(pathName), charset);
    }

    public static void exportResultToCsv(List<Map<String, Object>> maps, String pathName) {
        Set<String> keys = maps.get(0).keySet();
        String[] titles = keys.toArray(new String[keys.size()]);
        exportResultToCsv(maps, titles, pathName);
    }

    public static void exportResultToCsv(List<Map<String, Object>> maps, String[] titles, String pathName) {
        exportResultToCsv(maps, titles, titles, pathName);
    }

    public static void exportResultToCsv(List<Map<String, Object>> maps, String[] titles, String[] keys, String pathName) {
        List<String[]> lines = new ArrayList<String[]>();
        lines.add(titles);
        for (Map<String, Object> m : maps) {
            String[] line = new String[keys.length];
            for (int i = 0; i < keys.length; i++) {
                Object obj = m.get(keys[i]);
                line[i] = obj == null ? "" : String.valueOf(obj);
            }
            lines.add(line);
        }
        CsvUtil.getWriter(new File(pathName), StandardCharsets.UTF_8).write(lines);
    }

    public static <T> List<T> parseToBeanList(CsvData csvData, String[] fields, Class<T> beanClass) throws Exception {
        List<T> list = new ArrayList<T>(csvData.getRowCount());
        List<CsvRow> rows = csvData.getRows();
        CsvRow row = null;
        for (int r = 1; r < rows.size(); r++) {
            row = rows.get(r);
            if (row.size() != fields.length) {
                String msg = String.format("row data length mismatch titles length! %d vs %d"
                        , row.size(), fields.length);
                throw new Exception(msg);
            }
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < row.size(); i++) {
                map.put(fields[i], row.get(i) == null ? null : row.get(i).trim());
            }
            T t = BeanUtil.mapToBean(map, beanClass, true);
            list.add(t);
        }
        return list;
    }
}
