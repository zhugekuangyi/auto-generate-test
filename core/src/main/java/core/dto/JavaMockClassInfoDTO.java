package core.dto;

import core.common.BaseCanUserType;
import lombok.Data;

/**
 * 需要mock的类信息
 *
 * @author 楚其
 */
@Data
public class JavaMockClassInfoDTO extends BaseCanUserType {

    /**
     * 属性名称
     */
    private String name;
    /**
     * 父类类型
     */
    private String parentClassFullyType;
    /*
     * 需要mock的方法
     */
//    private List<JavaMockMethodInfoDTO> javaMockMethodInfoDTOList;
}