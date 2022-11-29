package practice.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtils extends org.apache.commons.lang3.StringUtils {

	private StringUtils() {
    }

	/**
	 * <pre>
	 * 判断指定的字符串str是否以withStr开头，比较方式忽略大小写
	 * 例如：
	 * startsIgroneCaseWith("abcde", "Abc") 返回true
	 * startsIgroneCaseWith("abcde", "abc") 返回true
	 * startsIgroneCaseWith("abcde", " abc") 返回false
	 * startsIgroneCaseWith("abcde", null) 返回false
	 * startsIgroneCaseWith("abcde", "") 返回true
	 * </pre>
	 * 
	 * @param str
	 * @param withStr
	 * @return
	 */
	public static boolean startsIgroneCaseWith(String str, String withStr) {
		if (str == null || withStr == null)
			return false;
		else
			return str.regionMatches(true, 0, withStr, 0, withStr.length());
	}



	/**
	 * <pre>
	 * 判断指定的字符串str是否以withStr结尾，比较方式忽略大小写
	 * 例如：
	 * endsIgroneCaseWith("abcde", "Cde") 返回true
	 * endsIgroneCaseWith("abcde", "cde") 返回true
	 * endsIgroneCaseWith("abcde", "cde ") 返回false
	 * endsIgroneCaseWith("abcde", null) 返回false
	 * endsIgroneCaseWith("abcde", "") 返回true
	 * </pre>
	 * 
	 * @param str
	 * @param withStr
	 * @return
	 */
	public static boolean endsIgroneCaseWith(String str, String withStr) {
		if (str == null || withStr == null)
			return false;
		else
			return str.regionMatches(true, str.length() - withStr.length(),
					withStr, 0, withStr.length());
	}

	/**
	 * 指定的字符串是否以空白符开头
	 * 
	 * @param str
	 * @return
	 */
	public static boolean startsWithWhiteSpace(String str) {
		if (str == null || str.length() < 1)
			return false;
		else
			return Character.isWhitespace(str.charAt(0));
	}

	/**
	 * 指定的字符串是否以空白符结尾
	 * 
	 * @param str
	 * @return
	 */
	public static boolean endsWithWhiteSpace(String str) {
		if (str == null || str.length() < 1)
			return false;
		else
			return Character.isWhitespace(str.charAt(str.length() - 1));
	}

	/**
	 * 指定的字符串是否以空白符开头
	 * 
	 * @param str
	 * @return
	 */
	public static boolean startsWithWhiteSpace(CharSequence str) {
		if (str == null || str.length() < 1)
			return false;
		else
			return Character.isWhitespace(str.charAt(0));
	}

	/**
	 * 指定的字符串是否以空白符结尾
	 * 
	 * @param str
	 * @return
	 */
	public static boolean endsWithWhiteSpace(CharSequence str) {
		if (str == null || str.length() < 1)
			return false;
		else
			return Character.isWhitespace(str.charAt(str.length() - 1));
	}

	/*
	 * 功能描述: 删除指定的前导、后导子串（大小写敏感）. <br>例子：
	 * <br>StringUtils.trim("and and username = '123' and password = 'abc' and "
	 * ,"and "); <br>结果：username = '123' and password = 'abc'
	 * 
	 * @param sourceStr String 待删除的字符串
	 * 
	 * @param ch String 子串
	 * 
	 * @return String 处理后的字符串
	 */
	public static String trim(String sourceStr, String ch) {
		if (null == sourceStr || "".equals(sourceStr.trim())) {
			return sourceStr;
		}
		if (null == ch || "".equals(ch)) {
			return sourceStr;
		}

		if (ch.length() > sourceStr.length()) {
			return sourceStr;
		}

		if (sourceStr.indexOf(ch) < 0) {
			return sourceStr;
		}

		// 删除前导
		int chLen = ch.length();

		/*
		 * StringBuffer strBuf = new StringBuffer(sourceStr);
		 * while(strBuf.indexOf(ch,0) == 0){ //表示还有前导
		 * strBuf.replace(0,chLen,""); }
		 * 
		 * //删除后导 int strBufLen = strBuf.length();
		 * while(strBuf.indexOf(ch,(strBufLen - chLen)) == strBufLen - chLen){
		 * strBuf.replace(strBuf.length() - chLen,strBuf.length(),""); strBufLen
		 * = strBuf.length(); }
		 */

		while (sourceStr.indexOf(ch, 0) == 0) { // 表示还有前导
			sourceStr = sourceStr.substring(chLen);
		}

		int strLen = sourceStr.length();
		while (sourceStr.indexOf(ch, (strLen - chLen)) == (strLen - chLen)) { // 表示还有后导
			sourceStr = sourceStr.substring(0, strLen - chLen);
			strLen = sourceStr.length();
		}

		return sourceStr;
	}

	/**
	 * 功能描述: 用指定的分隔符把字符串数组合并成一个字符串. 数组为null或长度为0时返回空字符串 <BR>
	 * 例子： <BR>
	 * String[] array = {"1",null,"","3"}; <BR>
	 * StringUtils.arrayToString(array,"|"); <BR>
	 * 返回结果：1|||3
	 * 
	 * @param array
	 *            String[] 待合并的数组
	 * @param splitStr
	 *            String 数组各元素合并后，它们之间的分隔符，为null时用空字符串代替
	 * @return String 合并后的字符串
	 */
	public static String arrayToString(String[] array, String splitStr) {
		if (null == array || 0 == array.length) {
			return "";
		}
		if (null == splitStr) {
			splitStr = "";
		}
		StringBuilder strBuf = new StringBuilder("");
		int Len = array.length;
		for (int i = 0; i < Len - 1; i++) {
			strBuf.append((null == array[i]) ? "" : array[i]).append(splitStr);
		}
		strBuf.append((null == array[Len - 1]) ? "" : array[Len - 1]);

		return strBuf.toString();
	}

	/**
	 * 功能描述: 用默认分隔符 | 把字符串数组合并成一个字符串. 数组为null或长度为0时返回空字符串
	 * 
	 * @param array
	 *            String[] 待合并的数组
	 * @return String 合并后的字符串
	 */
	public static String arrayToString(String[] array) {
		return arrayToString(array, "|");
	}

	/**
	 * 将null字符串转换为空串
	 * 
	 * @param sourceStr
	 *            待处理的字符串
	 * @return String 处理的的字符串
	 */
	public static String toVisualString(String sourceStr) {
		if (sourceStr == null || sourceStr.equals("")) {
			return "";
		} else {
			return sourceStr;
		}
	}

	/**
	 * 将字段填充到指定的长度
	 * 
	 * @param sourceStr
	 *            String 操作字符串
	 * @param length
	 *            int 指定长度
	 * @param withChar
	 *            char 填充的字符
	 * @param isPadToEnd
	 *            boolean 是否填充在字符的尾部 true ：是 ,false:填充在头部
	 * @return String
	 */
	public static String pad(String sourceStr, int length, char withChar,
			boolean isPadToEnd) {
		if (sourceStr == null) {
			return null;
		}
		if (sourceStr.length() >= length) {
			return sourceStr;
		}

		StringBuilder sb = new StringBuilder(sourceStr);
		for (int i = 0; i < length - sourceStr.length(); i++) {
			if (isPadToEnd) {
				sb.append(withChar);
			} else {
				sb.insert(0, withChar);
			}
		}
		return sb.toString();

	}

	/**
	 * 数字字符串转化为整数
	 * 
	 * @param intStr
	 *            String 待转化的数字字符串
	 * @param intDef
	 *            int 当intStr为空或空字符串时返回的缺省值
	 * @return int 返回由数字字符串转化成的数字，当intStr为空或空字符串时，返回缺省值intDef
	 */
	public static int getInt(String intStr, int intDef) {

		if (null == intStr || "".equals(intStr.trim())) {
			return intDef;
		}

		int intRetu = intDef;

		Double db = new Double(intStr);
		intRetu = db.intValue();
		return intRetu;
	}

	/**
	 * 数字字符串转化为整数，在转化时发生异常，则返回0
	 * 
	 * @param intStr
	 *            String 待转化的数字字符串
	 * @return int 返回由数字字符串转化成的整数，当intStr为空或空字符串时，返回0
	 */
	public static int getInt(String intStr) {
		return getInt(intStr, 0);
	}

	/**
	 * 过滤回车换行
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		if (str == null || "".equals(str.trim())) {
			return "";
		}
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(str);
		String after = m.replaceAll("");
		return after;
	}

	/**
	 * 将无效的windows文件名字符替换成下划线
	 * 
	 * @param fileName
	 * @return
	 */
	public static String replaceInvalidFileNameChars(String fileName) {
		String regex = "[\\\\/*?<>:\"|]";
		return Pattern.compile(regex).matcher(fileName).replaceAll("_");
	}

	public static String arrayToString(Object[] array) {
		if (array == null)
			return "null";
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		boolean first = true;
		for (Object obj : array) {
			if (first)
				first = false;
			else
				sb.append(",");
			sb.append(obj);
		}
		sb.append("]");
		return sb.toString();
	}

	public static String toString(Object obj) {
		if (obj == null) {
			return "null";
		} else if (obj.getClass().isArray()) {
			return arrayToString((Object[]) obj);
		} else {
			return obj.toString();
		}
	}

	public static int compare(String str1, String str2) {
		if (str1 == null) {
			if (str2 == null)
				return 0;
			else
				return -1;
		} else {
			if (str2 == null)
				return 1;
			else
				return str1.compareTo(str2);
		}
	}

	public static interface ReplacementGetter {
		String getReplacement(String matchStr);
	}

	/**
	 * 字符串替换
	 * 
	 * @param input
	 *            要替换的字符串
	 * @param matchRegex
	 *            匹配要替换的内容的正则表达式
	 * @param replacementGetter
	 *            替换过程中，通过调用此接口查询匹配的原文对应的替换内容
	 * @return
	 */
	public static StringBuffer replaceString(CharSequence input,
			String matchRegex, ReplacementGetter replacementGetter) {
		Pattern pattern = Pattern.compile(matchRegex);
		StringBuffer buf = new StringBuffer();
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			String str = matcher.group();
			String replacement = replacementGetter.getReplacement(str);
			matcher.appendReplacement(buf, replacement);
		}
		matcher.appendTail(buf);
		return buf;
	}

	public static void main(String args[]) {
		System.out.println(startsIgroneCaseWith("abcd", "Ab"));
		System.out.println(startsIgroneCaseWith("abcd", "cD"));
		System.out.println(startsIgroneCaseWith("abcd", ""));
		System.out.println(endsIgroneCaseWith("abcd", "Ab"));
		System.out.println(endsIgroneCaseWith("abcd", "cD"));
	}
	
	public static List<String> splitToList(String source,String pattern)
	{
        List<String> tempArray = new ArrayList<String>();
        if (!isEmpty(source))
        {
            String[] tempIds = source.split(pattern);
            for (String s : tempIds) {
                if (!StringUtils.isBlank(s)) {
                    tempArray.add(s);
                }
            }
        }
        
        return tempArray;
	}
	
    public static String arrayToStringNoPrefix(Object[] array)
    {
        if (array == null)
            return null;
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object obj : array)
        {
            if (first)
                first = false;
            else
                sb.append(",");
            sb.append(obj);
        }
        return sb.toString();
    }
    
    public static Map<String,String> oracleIn(List<? extends Object> list)
    {
        Map<String,String> params = new HashMap<String, String>();
        
        if (list == null || list.isEmpty())
            return params;
        
        //超过1000字节 
        String sql = "select column_value from table(STR2VARLIST(:ids0))";
        
        StringBuilder sb = new StringBuilder();
        int i = 0;
        boolean first = true;
        for (Object obj : list)
        {
            if (sb.length() + String.valueOf(obj).length() > 1000)
            {
                params.put("ids" + i, sb.toString());
                if (i > 0)
                {
                    sql = sql + " union select column_value from table(STR2VARLIST(:ids" + i + "))";
                }
                i ++;
                sb = new StringBuilder();
                sb.append(obj);
            }
            else
            {
                if (first)
                {
                    first = false;
                }
                else
                {
                    sb.append(",");
                }
                
                sb.append(obj);
            }
        }
        
        if (i > 0)
        {
            sql = sql + " union select column_value from table(STR2VARLIST(:ids" + i + "))";
        }
        
        params.put("ids" + i, sb.toString());
        
        params.put("sql", sql);
        return params;
    }
    
    public static Set<String> splitToSet(String source, String pattern)
    {
        Set<String> tempArray = new HashSet<String>();
        if (!isEmpty(source))
        {
            String[] tempIds = source.split(pattern);
            for (String s : tempIds)
            {
                if (!StringUtils.isBlank(s))
                {
                    tempArray.add(s);
                }
            }
        }
        
        return tempArray;
    }

    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }
    public static String[] tokenizeToStringArray(
            String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

        if (str == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }
    public static String[] toStringArray(Collection<String> collection) {
        if (collection == null) {
            return null;
        }
        return collection.toArray(new String[collection.size()]);
    }

    /**
     * 字符串首字母大写
     * @param str
     * @return
     */
    public static String firstLetterUpperCase(String str) {
        char[] cs = str.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

}
