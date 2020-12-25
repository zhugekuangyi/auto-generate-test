package core.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 一个被测试类的信息
 * @author 楚其
 */
@Data
public class JavaGenInfoModel {
    /**
     * 当前类的属性名称
     */
    private String modelNameLowerCamel;
    /**
     * 类信息存储
     * key - 类的全限定名称
     * value - 类信息
     */
    private Map<String,JavaClassModel> javaClassModelMap = new HashMap<>();

    /**
     * key - 属性变量名称
     * value - 属性类型的全限定名称
     */
    private Map<String,String> fieldFullyTypeNameMap = new HashMap<>();

    /**
     * key - 类型简称
     * value - 类型的全限定名称
     */
    private Map<String,String> fullyTypeNameMap = new HashMap<>();

    /**
     * mock的类信息
     * key - 属性变量名称 + "." + 方法名称（暂时不支持重名方法）
     * value - 属性类型的全限定名称
     */
    private Map<String,String> mockFullyTypeNameMap = new HashMap<>();

    /**
     * 需要导入的包  如果有多个，全部使用全限定名，在该map中的，表示没有简称相同的类
     * key - 变量名-简称
     * value - 全限定名称
     */
    private Map<String, Set<String>> implementsJavaPackageMap = new HashMap<>();

    /**
     * 测试方法名称出现的次数，如果有多个重名方法，方法后面接上数字
     */
    private Map<String, Integer> methodMap = new HashMap<>();

}