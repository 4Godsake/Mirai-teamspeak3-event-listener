package cn.rapdog.ts3eventlistener.utils;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author rapdog
 */
public class RegxUtil {
    /**
     * 利用其预编译功能，加快正则匹配速度
     */
    public static Pattern dynamic = Pattern.compile(".*\\$\\{event\\.(.+?)\\}.*");
    public static Pattern dynamicLimitCount = Pattern.compile("\\$\\{event\\.(.+?)\\}");

    /**
     * 判断内容中是否包含动态参数(${key}形式的)
     *
     * @param content 要判断的内容
     * @return
     */
    public static boolean isContainsDynamicParameter(String content) {
        return dynamic.matcher(content).matches();
    }

    /**
     * 按照动态内容的参数出现顺序,将参数放到List中
     *
     * @param content
     * @return
     */
    public static List<String> getKeyListByContent(String content) {
        Set<String> paramSet = new LinkedHashSet<>();
        Matcher m = dynamicLimitCount.matcher(content);
        while (m.find()) {
            paramSet.add(m.group(1));
        }
        return new ArrayList<>(paramSet);
    }


    public static void main(String[] args) {
        //测试代码
        String content = "「${event.reasonmsg}」离开了TS，并大喊：“下播！”";
        System.out.println(isContainsDynamicParameter(content));
        List<String> keyListByContent = getKeyListByContent(content);
        System.out.println("内容中的动态参数为:");
        keyListByContent.forEach(System.out::println);
    }
}