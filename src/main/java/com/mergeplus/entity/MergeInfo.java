package com.mergeplus.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：merge-plus
 * 类 名 称：MergeCache
 * 类 描 述：TODO
 * 创建时间：2020/10/21 1:59 下午
 * 创 建 人：chenyouhong
 */
@Data
public class MergeInfo {

    /**
     * 类名称
     */
    private String className;


    /**
     * 表字段信息列表
     */
    private List<FieldInfo> fieldList = new ArrayList<>();

    /**
     *
     * @return 返回
     */
    public String getClassName() {
        return className;
    }

    /**
     *
     * @param className className
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     *
     * @return 返回
     */
    public List<FieldInfo> getFieldList() {
        return fieldList;
    }

    /**
     *
     * @param fieldList fieldList
     */
    public void setFieldList(List<FieldInfo> fieldList) {
        this.fieldList = fieldList;
    }
}
