import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class MyClassLoader extends ClassLoader {

    public static void main(String[] args) throws Exception {
        // 自定义类加载器
        MyClassLoader myClassLoader = new MyClassLoader();
        // 加载Hello.xlass
        Class<?> clazz = myClassLoader.findClass("Hello", ".xlass");
        // 实例化
        Object instance = clazz.newInstance();
        // 获取hello方法
        Method helloMethod = clazz.getMethod("hello");
        // 执行方法
        helloMethod.invoke(instance);
    }

    /**
     * 加载类
     *
     * @param name
     * @param suffix
     * @return
     * @throws ClassNotFoundException
     */
    protected Class<?> findClass(String name, String suffix) throws Exception {
        try (InputStream inputStream = this.getClass().getResourceAsStream(name + suffix)) {
            // 获取文件有多少字节
            int length = inputStream.available();
            // 新建byte[]存放字节
            byte[] byteArr = new byte[length];
            // 读取
            inputStream.read(byteArr);
            // 解码
            byte[] lastArr = decode(byteArr);
            // 调用底层加载类
            return defineClass(name, lastArr, 0, lastArr.length);
        } catch (IOException e) {
            throw new Exception(name + suffix, e);
        }
    }

    /**
     * 解码
     *
     * @param byteArr
     * @return
     */
    private static byte[] decode(byte[] byteArr) {
        // 返回字节
        byte[] targetArr = new byte[byteArr.length];
        // 每个字节 = (255-当前字节)
        for (int i = 0; i < byteArr.length; i++) {
            targetArr[i] = (byte) (255 - byteArr[i]);
        }
        // 返回
        return targetArr;
    }
}
