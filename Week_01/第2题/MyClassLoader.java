import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;

/**
 * @author: Ewen
 * @program: java-advanced
 * @date: 2020/10/17 11:31
 * @description:
 */
public class MyClassLoader extends ClassLoader {

    public static void main(String[] args) {

        try {

            Class<?> helloClass = new MyClassLoader().findClass("Hello");

            Object object = helloClass.newInstance();

            Method method = helloClass.getMethod("hello");

            method.invoke(object);

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) {

        File file = new File(this.getClass().getResource("/Hello.xlass").getPath());

        int length = (int) file.length();

        byte[] bytes = new byte[length];

        try {
            new FileInputStream(file).read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int index = 0; index < bytes.length; index++) {
            bytes[index] = (byte) (255 - bytes[index]);
        }

        return defineClass(name, bytes, 0, length);
    }

    private byte[] decode(String str) {
        return Base64.getDecoder().decode(str);
    }

}
