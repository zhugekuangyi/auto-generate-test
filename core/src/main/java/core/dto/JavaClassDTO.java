package core.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 类DTO
 */
@Data
public class JavaClassDTO {

    /**
     * 被测试的类名
     */
    private String modelNameUpperCamel;

    /**
     * 被测试的类名 - 首字母小写
     */
    private String modelNameLowerCamel;

    /**
     * 测试类名
     */
    private String modelNameUpperCamelTestClass;

    /**
     * 测试类名 - 首字母小写
     */
    private String modelNameLowerCamelTestClass;

    /**
     * 当前日期
     */
    private String date;

    /**
     * 作者
     */
    private String author;

    /**
     * 类方法
     */
    private List<JavaMethodDTO> javaMethodDTOList = new ArrayList<>();

    /**
     * 需要导入的包 - 简称相同的类，不会被导入
     */
    private List<JavaImplementsDTO> javaImplementsDTOList = new ArrayList<>();

    /**
     * 被测试类包名
     */
    private String packageName;

    /**
     * 需要引入的mcok类
     */
    private List<JavaMockClassInfoDTO> javaMockClassInfoDTOList = new ArrayList<>();

}