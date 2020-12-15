package plugin;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import core.common.BaseConstant;
import core.common.ConfigConstant;
import core.util.PackageUtil;
import core.util.StringUtil;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import plugin.base.AbstractPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 构建自动化测试
 *
 * @author chuqi
 */
@Mojo(name = "test")
public class UnittestPlugin extends AbstractPlugin {
    /**
     * 需要测试的项目包名
     */
    @Parameter
    private String testPackageName;
    /**
     * 作者
     */
    @Parameter(defaultValue = "")
    private String author;
    /**
     * 是否获取子包下的类
     */
    @Parameter(defaultValue = "true")
    private Boolean isGetChildPackage;

    /**
     * 配置是否mock掉父类以及自身测试类非测试的方法(默认true)
     */
    @Parameter(defaultValue = "true")
    private Boolean isMockThisOtherMethod;

    /**
     * 配置是否设置基础类型的值随机生成(默认false)
     */
    @Parameter(defaultValue = "false")
    private Boolean isSetBasicTypesRandomValue;

    /**
     * 需要mock掉的包，包下所有类的方法都会被mock
     * v1.1.2 将不再支持进行配置进行mock的包
     */
    @Parameter
    @Deprecated()
    private String mockPackage;

    /**
     * 不需要初始化默认值的类的包名
     * 在参数初始化的时候，进行赋值为null的类，在该包下的类不会进行赋值
     * 不需要复制为NULL
     */
    @Parameter
    @Deprecated()
    private String skipPackages;

    /**
     * 其他的项目名称，一个项目下多个项目模块。项目模块的路径
     * 不需要进行配置，插件进行依赖即可
     */
    @Parameter
    @Deprecated
    private String otherProjectName;

    /**
     * 配置字符串随机值的位数（例如："10"，表示10位随机字母/数字字符）
     * V1.1.2+
     */
    @Parameter(defaultValue = "10")
    private String setStringRandomRange;
    /**
     * 配置int/Integer类型随机值的范围（例如："0,1000"，表示[0,1000)范围的int数值，配置固定的值可配置为"0",则int值固定为0）
     * V1.1.2+
     */
    @Parameter(defaultValue = "0,1000")
    private String setIntRandomRange;
    /**
     * 配置long/Long类型随机值的范围(配置规则与setIntRandomRange类似)
     * V1.1.2+
     */
    @Parameter(defaultValue = "0,10000")
    private String setLongRandomRange;
    /**
     * 配置boolean/Boolean类型随机值的范围（例如：配置为"true"/"false"表示为固定的值，其他任意值表示true和false随机）
     * V1.1.2+
     */
    @Parameter(defaultValue = "true,false")
    private String setBooleanRandomRange;

    public static void main(String[] args) throws IOException {
        JavaProjectBuilder builder = new JavaProjectBuilder();
        builder.addSource(new File("/Users/chuqi/Desktop/github/auto-generate-test-maven-plugin/agt-core/src/main/java/com/uifuture/maven/plugins/core/model/JavaClassModel.java"));
        JavaClass javaClass1 = builder.getClassByName("JavaClassModel");
        JavaClass javaClass2 = builder.getClassByName("com.uifuture.maven.plugins.core.model.JavaClassModel");
        System.out.println(javaClass1 + "====" + javaClass2);
        JavaClass javaClass3 = builder.getClassByName("JavaProjectBuilder");
        JavaClass javaClass4 = builder.getClassByName("com.thoughtworks.qdox.JavaProjectBuilder");
        System.out.println(javaClass3 + "====" + javaClass4);
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        //设置配置值
        super.execute();
        ConfigConstant.CONFIG_ENTITY.setTestPackageName(testPackageName);
        ConfigConstant.CONFIG_ENTITY.setAuthor(author);
        ConfigConstant.CONFIG_ENTITY.setIsGetChildPackage(isGetChildPackage);
        ConfigConstant.CONFIG_ENTITY.setMockPackage(mockPackage);
        ConfigConstant.CONFIG_ENTITY.setSkipPackages(skipPackages);
        ConfigConstant.CONFIG_ENTITY.setOtherProjectName(otherProjectName);
        ConfigConstant.CONFIG_ENTITY.setIsMockThisOtherMethod(isMockThisOtherMethod);
        ConfigConstant.CONFIG_ENTITY.setIsSetBasicTypesRandomValue(isSetBasicTypesRandomValue);
        ConfigConstant.CONFIG_ENTITY.setSetStringRandomRange(setStringRandomRange);
        ConfigConstant.CONFIG_ENTITY.setSetBooleanRandomRange(setBooleanRandomRange);
        ConfigConstant.CONFIG_ENTITY.setSetIntRandomRange(setIntRandomRange);
        ConfigConstant.CONFIG_ENTITY.setSetLongRandomRange(setLongRandomRange);

        InitPlugin.downFile();

        getLog().info("开始生成自动化测试代码" +
                      "\n" + ConfigConstant.CONFIG_ENTITY
        );

        if(StringUtil.isEmpty(testPackageName)){
            getLog().error("testPackageName必须进行配置（需要测试的项目包名）"
            );
            return;
        }

        if (StringUtil.isNotEmpty(skipPackages)) {
            //无法进行预加载的类，设置参数值为null，外部包
            Collections.addAll(BaseConstant.skipPackage, skipPackages.split(";"));
        }

        Map<String, List<String>> javaListMap = new HashMap<>();
        //如果配置的是java类，直接使用该类
        if(testPackageName.endsWith(".java")){
            List<String> javaList = new ArrayList<>();
            testPackageName = testPackageName.substring(0,testPackageName.lastIndexOf(".java"));
            String classPathName = basedir + "/src/main/java/"+ testPackageName.replace(".", "/")+".java";
            javaList.add(classPathName);
            javaListMap.put(testPackageName.substring(0, testPackageName.lastIndexOf(".")), javaList);
        } else {
            //遍历包名，多个包的情况
            if (testPackageName.contains(";")) {
                String[] packageNames = testPackageName.split(";");
                for (String packageName : packageNames) {
                    if (StringUtil.isEmpty(packageName)) {
                        continue;
                    }
                    //获取该包下所有的类
                    List<String> javaList = PackageUtil.getClassName(basedir.getPath(), packageName, isGetChildPackage);
                    javaListMap.put(packageName, javaList);
                }

            } else {
                //获取该包下所有的类
                List<String> javaList = PackageUtil.getClassName(basedir.getPath(), testPackageName, isGetChildPackage);
                javaListMap.put(testPackageName, javaList);
            }

        }
        //初始化类源
        initJavaProjectBuilder();

        //遍历javaListMap  
        for (String packageName : javaListMap.keySet()) {
            //设置测试包名
            ConfigConstant.CONFIG_ENTITY.setTestPackageName(packageName);
            List<String> javaList = javaListMap.get(packageName);
            getLog().info("包名为：" + packageName + "，获取的所有类名为：" + javaList);
            //class文件绝对路径
            for (String javaName : javaList) {
                getLog().info("当前文件路径：" + javaName);
                if (javaName.endsWith("$1")) {
                    //跳过
                    getLog().info("跳过:" + javaName);
                    continue;
                }
                //文件路径
                GenJava.genTest(javaName);
            }
        }
    }

    /**
     * 初始化类源
     */
    private void initJavaProjectBuilder() {
        //读取包下所有的java类文件
        String mainJava = basedir.getPath() + BaseConstant.JAVA_MAIN_SRC;
        BaseConstant.javaProjectBuilder.addSourceTree(new File(mainJava));
        getLog().info("加载当前模块的类：" + mainJava);
        //读取包下所有的测试的java类文件
        String testJava = basedir.getPath() + BaseConstant.JAVA_TEST_SRC;
        BaseConstant.javaProjectBuilder.addSourceTree(new File(testJava));
        getLog().info("加载当前模块的测试类：" + testJava);

        //加载其他模块的类
//        if (StringUtil.isNotEmpty(otherProjectName)) {
//            for (String name : otherProjectName.split(";")) {
//                String fileName = basedir.getPath().substring(0, basedir.getPath().lastIndexOf("/") + 1) + name + BaseConstant.JAVA_MAIN_SRC;
//                BaseConstant.javaProjectBuilder.addSourceTree(new File(fileName));
//                getLog().info("加载其他模块的类：" + fileName);
//            }
//        }

        //需要删除 不需要配置需要mock的包
//        if (StringUtil.isNotEmpty(mockPackage)) {
//            //需要mock的包
//            String mockJava = basedir + BaseConstant.JAVA_MAIN_SRC + mockPackage.replace(".", "/");
//            //获取包下所有的类
//            List<String> javaList = PackageUtil.getClassNameByFile(mockJava, true);
//            for (String path : javaList) {
//                path = path.replace("/", ".");
//                path = path.substring(path.indexOf(mockPackage), path.lastIndexOf("."));
//                BaseConstant.mockJavaSet.add(path);
//            }
//            getLog().info("需要mock的所有类：" + BaseConstant.mockJavaSet);
//        }
    }


}