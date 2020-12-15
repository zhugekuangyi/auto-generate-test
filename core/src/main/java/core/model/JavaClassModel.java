/*
 * uifuture.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package core.model;

import lombok.Data;

import java.util.List;

/**
 *
 * 类信息
 * @author chuqi
 */
@Data
public class JavaClassModel {
    /**
     * 该类的的属性变量名称
     */
    private String name;
    /**
     * 类型 - 全限定名
     */
    private String fullyType;
    /**
     * 类型 - 非全限定名
     */
    private String type;
    /**
     * 父类类型
     */
    private String parentFullyTypeType;

    /**
     * 类中方法
     */
    private List<JavaMethodModel> javaMethodModelList;


}