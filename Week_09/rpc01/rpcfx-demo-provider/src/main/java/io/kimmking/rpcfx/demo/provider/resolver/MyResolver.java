package io.kimmking.rpcfx.demo.provider.resolver;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.kimmking.rpcfx.api.MyRpcfxResolver;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/16 14:32
 * @description:
 */
public class MyResolver {

    private static final String HALF_WIDTH_PERIOD = ".";

    private static final String CLASS_SUFFIX = ".class";

    private static final String CLASS_DIRECTORY_NAME = "classes";

    public static ConcurrentMap<Class<?>, Class<?>> initTargetClass(String packagePath) {

        List<String> classList = getClassNameByPackage(packagePath);

        ConcurrentMap<Class<?>, Class<?>> map = Maps.newConcurrentMap();

        classList.forEach(name -> {
            try {
                // key => interface Class , value => impl Class
                map.put(Class.forName(name).getInterfaces()[0], Class.forName(name));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("load class from name failed:" + name + e.getMessage());
            }
        });

        return map;
    }

    private static List<String> getClassNameByPackage(String packagePath) {

        packagePath = packagePath.replace(HALF_WIDTH_PERIOD, "/");

        URL url = Thread.currentThread().getContextClassLoader().getResource(packagePath);

        if (Objects.isNull(url)) {
            throw new RuntimeException("finds the resource fail!");
        }

        if ("file".equals(url.getProtocol())) {
            /*File file = new File("D:/WorkSpaceIdea/geek-school/job/JAVA-000/Week_09/rpc01/rpcfx-demo-provider/target/classes/io/kimmking/rpcfx/demo/provider/serivce/impl");*/
            File file = new File(url.getPath());
            return gotNameByFile(file.listFiles());
        }

        throw new RuntimeException("file system not support! cannot load！");
    }

    private static List<String> gotNameByFile(File[] files) {

        List<String> classNames = Lists.newArrayList();

        Arrays.stream(files)
                .forEach(file -> {
                    if (file.isDirectory()) {
                        classNames.addAll(gotNameByFile(new File(file.getPath()).listFiles()));
                    } else {
                        String current = file.getPath();
                        if (current.endsWith(CLASS_SUFFIX)) {
                            classNames.add(
                                    current.substring(current.indexOf(CLASS_DIRECTORY_NAME) + 8, current.lastIndexOf(HALF_WIDTH_PERIOD))
                                            .replace(File.separator, HALF_WIDTH_PERIOD)
                            );
                        }
                    }
                });

        return classNames;
    }

    public static void main(String[] args) {

        System.out.println(initTargetClass("io.kimmking.rpcfx.demo.provider.serivce.impl"));
    }
}
