package core.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 楚其
 */
@Data
public class BaseCanUserType {
    /**
     * 参数类型 - 简称
     */
    private String type;

    /**
     * 全限定名称，类型
     */
    private String fullyType;

    /**
     * 是否能够使用简称
     */
    private Boolean canUserType=false;


    /**
     * 是否是接口
     */
    private Boolean isInterface = false;
    /**
     * 接口的实现类类型 - 简称
     */
    private List<String> subClassTypeList = new ArrayList<>();

    /**
     * 子实现类的简称
     */
    private String subClassType;
    /**
     * 子实现类的全限定名称
     */
    private String subClassFullyType;
    /**
     * 子类是否能够使用简称
     */
    private Boolean subClassCanUserType=false;

}