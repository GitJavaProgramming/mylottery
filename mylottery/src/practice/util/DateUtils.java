package practice.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    /** 数据库存储的时间格式串，如yyyymmdd 或yyyymmddHHMiSS */
    public static final int DB_STORE_DATE = 1;

    /** 用连字符-分隔的时间时间格式串，如yyyy-mm-dd 或yyyy-mm-dd HH:Mi:SS */
    public static final int HYPHEN_DISPLAY_DATE = 2;

    /** 用连字符.分隔的时间时间格式串，如yyyy.mm.dd 或yyyy.mm.dd HH:Mi:SS */
    public static final int DOT_DISPLAY_DATE = 3;

    /** 用中文字符分隔的时间格式串，如yyyy年mm月dd 或yyyy年mm月dd HH:Mi:SS */
    public static final int CN_DISPLAY_DATE = 4;

    /***** 数据库存储时间格式串,如yyyymmddHHMissSS **/
    public static final int DB_LONG_DATE = 5;
    
    public static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static Date setDateTime(Date date, int hour, int minute, int second) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        return c.getTime();
    }
    
    public static Date getNextDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }
    
    public static String getDateTimeStr(Date date) {
        return DATETIME_FORMAT.format(date);
    }

    /**
     * 返回当前时间
     * 
     * @return
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 得到精确到秒的格式化当前时间串
     * 
     * @return 当前时间格式化时间串
     */
    public static String getCurrTimeStr(int formatType) {
        return getTimeStr(new Date(), formatType);
    }

    public static String getCurrTimeStr() {
        return getCurrTimeStr(HYPHEN_DISPLAY_DATE);
    }

    /**
     * 得到精确到秒的格式化时间串
     * 
     * @param date 指定时间
     * @return 指定时间的格式化时间串
     */
    public static String getTimeStr(Date date, int formatType) {
        if (formatType < DB_STORE_DATE || formatType > DB_LONG_DATE) {
            throw new IllegalArgumentException("时间格式化类型不是合法的值。");
        }
        else {
            String formatStr = null;
            switch (formatType) {
            case DB_STORE_DATE:
                formatStr = "yyyyMMddHHmmss";
                break;
            case HYPHEN_DISPLAY_DATE:
                formatStr = "yyyy'-'MM'-'dd HH:mm:ss";
                break;
            case DOT_DISPLAY_DATE:
                formatStr = "yyyy.MM.dd HH:mm:ss";
                break;
            case CN_DISPLAY_DATE:
                formatStr = "yyyy'年'MM'月'dd'日' HH:mm:ss";
                break;
            case DB_LONG_DATE:
                formatStr = "yyyyMMddHHmmssSS";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
            return sdf.format(date);
        }
    }

    /**
     * 得到精确到天的当前格式化日期串
     * 
     * @return
     */
    public static String getCurrDateStr(int formatType) {
        return getDateStr(new Date(), formatType);
    }

    public static String getCurrDateStr() {
        return getCurrDateStr(HYPHEN_DISPLAY_DATE);
    }

    /**
     * 得到精确到天的指定时间格式化日期串
     * 
     * @param date 指定时间
     * @return 指定时间格式化日期串
     */
    public static String getDateStr(Date date, int formatType) {

        if (formatType < DB_STORE_DATE || formatType > CN_DISPLAY_DATE) {
            throw new IllegalArgumentException("时间格式化类型不是合法的值。");
        }
        else {
            String formatStr = null;
            switch (formatType) {
            case DB_STORE_DATE:
                formatStr = "yyyyMMdd";
                break;
            case HYPHEN_DISPLAY_DATE:
                formatStr = "yyyy-MM-dd";
                break;
            case DOT_DISPLAY_DATE:
                formatStr = "yyyy.MM.dd";
                break;
            case CN_DISPLAY_DATE:
                formatStr = "yyyy'年'MM'月'dd'日'";
                break;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
            return sdf.format(date);
        }
    }

    /**
     * 得到精确到月的当前时间格式化年月串
     * 
     * @return 精确到月当前时间格式化年月串
     */
    public static String getYearMonthStr(int formatType) {
        return getYearMonthStr(new Date(), formatType);
    }

    /**
     * 得到精确到月的指定时间格式化年月串
     * 
     * @param date 指定的时间
     * @return 精确到月当前时间格式化年月串
     */
    public static String getYearMonthStr(Date date, int formatType) {
        if (formatType < DB_STORE_DATE || formatType > CN_DISPLAY_DATE) {
            throw new IllegalArgumentException("时间格式化类型不是合法的值。");
        }
        else {
            String formatStr = null;
            switch (formatType) {
            case DB_STORE_DATE:
                formatStr = "yyyyMM";
                break;
            case HYPHEN_DISPLAY_DATE:
                formatStr = "yyyy-MM";
                break;
            case DOT_DISPLAY_DATE:
                formatStr = "yyyy.MM";
                break;
            case CN_DISPLAY_DATE:
                formatStr = "yyyy'年'MM'月'";
                break;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
            return sdf.format(date);
        }
    }

    /**
     * 将数据库存储的日期格式串转换为各种显示的格式
     * 
     * @param dateStr 最小6位，最大14位的数据库存储格式时间串如:20041212
     * @return 格式化的时间串
     */
    public static String toDisplayStr(String dateStr, int formatType) {
        if (formatType < DB_STORE_DATE || formatType > CN_DISPLAY_DATE) {
            throw new IllegalArgumentException("时间格式化类型不是合法的值。");
        }
        if (dateStr == null || dateStr.length() < 6 || dateStr.length() > 14 || formatType == DB_STORE_DATE) {
            return StringUtils.toVisualString(dateStr);
        }
        else {
            char[] charArr;
            switch (formatType) {
            case HYPHEN_DISPLAY_DATE:
                charArr = new char[] { '-', '-', ' ', ':', ':' };
                break;
            case DOT_DISPLAY_DATE:
                charArr = new char[] { '.', '.', ' ', ':', ':' };
                break;
            case CN_DISPLAY_DATE:
                charArr = new char[] { '年', '月', ' ', ':', ':' };
                break;
            default:
                charArr = new char[] { '-', '-', ' ', ':', ':' };
            }
            try {
                SimpleDateFormat sdf_1 = null;
                SimpleDateFormat sdf_2 = null;
                switch (dateStr.length()) {
                case 6:
                    sdf_1 = new SimpleDateFormat("yyyyMM");
                    sdf_2 = new SimpleDateFormat("yyyy'" + charArr[0] + "'MM");
                    break;
                case 8:
                    sdf_1 = new SimpleDateFormat("yyyyMMdd");
                    sdf_2 = new SimpleDateFormat("yyyy'" + charArr[0] + "'MM'" + charArr[1] + "'dd");
                    break;
                case 10:
                    sdf_1 = new SimpleDateFormat("yyyyMMddHH");
                    sdf_2 = new SimpleDateFormat("yyyy'" + charArr[0] + "'MM'" + charArr[1] + "'dd'" + "+charArr[2]" + "'HH");
                    break;
                case 12:
                    sdf_1 = new SimpleDateFormat("yyyyMMddHHmm");
                    sdf_2 = new SimpleDateFormat("yyyy'" + charArr[0] + "'MM'" + charArr[1] + "'dd'" + charArr[2] + "'HH'" + charArr[3] + "'mm");
                    break;
                case 14:
                    sdf_1 = new SimpleDateFormat("yyyyMMddHHmmss");
                    sdf_2 = new SimpleDateFormat("yyyy'" + charArr[0] + "'MM'" + charArr[1] + "'dd'" + charArr[2] + "'HH'" + charArr[3] + "'mm'" + charArr[4] + "'ss");
                    break;
                default:
                    return dateStr;
                }
                return sdf_2.format(sdf_1.parse(dateStr));
            }
            catch (ParseException ex) {
                return dateStr;
            }
        }
    }

    /**
     * 将显示格式的时间字符串转换为数据库存储的类型
     * 
     * @param dateStr 最小4位，最大19位。显示的时间格式时间串如:2004-12-12
     * @return 数据库存储的时间字符串
     */
    public static String toStoreStr(String dateStr) {
        if (dateStr == null || dateStr.trim().equals("")) {
            return "";
        }
        StringBuilder strBuf = new StringBuilder();
        for (int i = 0; i < dateStr.length(); i++) {
            if (dateStr.charAt(i) >= '0' && dateStr.charAt(i) <= '9') {
                strBuf.append(dateStr.charAt(i));
            }
        }
        return strBuf.toString();
    }

    /**
     * <b>功能描述：</b> 把时间转换成14位的字符串，不足位数补充 0 或者 9
     * 
     * @param dateStr String
     * @return String
     */
    public static String toStoreStr14(String dateStr, boolean bMax) {
        if (dateStr == null || dateStr.trim().equals("")) {
            return "";
        }
        String strBuf = toStoreStr(dateStr);
        String retStr = strBuf.toString();
        if (bMax) {
            retStr = StringUtils.pad(retStr, 14, '9', true);
        }
        else {
            retStr = StringUtils.pad(retStr, 14, '0', true);
        }
        return retStr;
    }

    /**
     * 将生日存储的时间格式转化为年龄（周岁，小数点后不计）
     * 
     * @param birthdayStr 生日字段 "yyyymmdd"
     * @return 年龄
     */
    public static String birthdayToAge(String birthdayStr) {
        if (birthdayStr == null || birthdayStr.length() < 6) {
            return "";
        }
        else {
            int birthYear = Integer.parseInt(birthdayStr.substring(0, 4));
            int birthMonth = Integer.parseInt(birthdayStr.substring(4, 6));
            Calendar cal = new GregorianCalendar();
            int currYear = cal.get(Calendar.YEAR);
            int currMonth = cal.get(Calendar.MONTH);
            int age = currYear - birthYear;
            age -= (currMonth < birthMonth) ? 1 : 0;
            return "" + age;
        }
    }

    /**
     * 功能描述: 将年龄转换为生日
     * 
     * @param age int
     * @return String
     */
    public static String ageToBirthday(int age) {

        String currDateStr = DateUtils.getYearMonthStr(DB_STORE_DATE);
        int currDateInt = Integer.parseInt(currDateStr);
        int ageDateInt = currDateInt - age * 100;
        return String.valueOf(ageDateInt);
    }

    /**
     * 功能描述: 增加一年
     * 
     * @return String
     */
    public static String nextAgeDate(String birDate) {

        int currDateInt = Integer.parseInt(birDate);
        int ageDateInt = currDateInt + 100;
        return String.valueOf(ageDateInt);
    }

    /**
     * @param dateTimeStr String 格式化的时间串
     * @param detal int 增加或减少的时间
     * @param field int 参见Calendar中关于时间字段属性的定义
     * @return String 返回的
     */
    public static String add(String dateTimeStr, int formatType, int detal, int field) {
        if (dateTimeStr == null || dateTimeStr.length() < 6) {
            return dateTimeStr;
        }
        else {
            try {
                String formatStr = null;
                switch (formatType) {
                case DB_STORE_DATE:
                    formatStr = "yyyyMMddHHmmss";
                    break;
                case HYPHEN_DISPLAY_DATE:
                    formatStr = "yyyy-MM-dd HH:mm:ss";
                    break;
                case DOT_DISPLAY_DATE:
                    formatStr = "yyyy.MM.dd HH:mm:ss";
                    break;
                case CN_DISPLAY_DATE:
                    formatStr = "yyyy'年'MM'月' HH：mm：ss";
                    break;
                default:
                    formatStr = "yyyyMMddHHmmss";
                    break;
                }

                formatStr = formatStr.substring(0, dateTimeStr.length());
                SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
                Date d = sdf.parse(dateTimeStr);
                GregorianCalendar g = new GregorianCalendar();
                g.setTime(d);
                g.add(field, detal);
                d = g.getTime();
                return sdf.format(d);
            }
            catch (ParseException ex) {
                ex.printStackTrace();
                return dateTimeStr;
            }
        }
    }

    // /**
    // * @param date Date 时间
    // * @param detal int 增加的时间
    // * @param field int 参见Calendar中关于时间字段属性的定义
    // * @return Date
    // */
    // public static Date add(Date date, int detal, int field)
    // {
    // Calendar g = new GregorianCalendar();
    // g.setTime(date);
    // g.add(field, detal);
    // return g.getTime();
    // }
    /**
     * 日期、时间格式化
     * 
     * @param date Date 将要被格式化的日期对象
     * @param outFmt String 返回样式，参照类说明，如：yyyy年MM月dd日
     * @return String 格式化后的日期、时间字符串，data为null时返回null，outFmt非法时返回yyyyMMdd格式
     */
    public static String getDateFormat(Date date, String outFmt) {
        if (null == date) {
            return null;
        }
        if (null == outFmt || "".equals(outFmt.trim())) { // outFmt非法
            outFmt = "yyyyMMdd";
        }

        String retu = null;
        SimpleDateFormat dateFormat;
        try {
            dateFormat = new SimpleDateFormat(outFmt);
        }
        catch (IllegalArgumentException iaex) { // outFmt非法
            dateFormat = new SimpleDateFormat("yyyyMMdd");
        }
        retu = dateFormat.format(date);

        dateFormat = null;

        return retu;
    }

    /**
     * 把日期时间对象格式化为yyyyMMdd样式
     * 
     * @param date Date 将要被格式化的日期对象
     * @return String 格式化后的日期、时间字符串，如：20041001
     */
    public static String getDateFormat(Date date) {
        return getDateFormat(date, "yyyyMMdd");
    }

    /**
     * 把系统当前日期时间格式化为指定的样式
     * 
     * @param outFmt String 返回样式，参照类说明，如：yyyy年MM月dd日
     * @return String 格式化后的日期、时间字符串，如：2004年10月01日
     */
    public static String getDateFormat(String outFmt) {
        return getDateFormat(new Date(), outFmt);
    }

    /**
     * 把系统当前日期时间格式化为默认样式yyyyMMdd
     * 
     * @return String 格式化后的日期、时间字符串，如：20041001
     */
    public static String getDateFormat() {
        return getDateFormat(new Date(), "yyyyMMdd");
    }

    /**
     * 日期、时间格式化
     * 
     * @param millis long the number of milliseconds（毫秒） since January 1, 1970, 00:00:00 GMT.
     * @param outFmt String 返回样式，参照类说明，如：yyyy年MM月dd日
     * @return String 格式化后的日期、时间字符串
     */
    public static String getDateFormat(long millis, String outFmt) {
        Date d = new Date(millis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        String retu = getDateFormat(calendar.getTime(), outFmt);
        calendar = null;
        return retu;
    }

    /**
     * 日期、时间格式化
     * 
     * @param datestr String 存在一定格式的日期、时间字符串，如：20041001、200410011503
     * @param inFmt String 对datestr参数格式说明，参照类说明，如：yyyyMMdd、yyyyMMddHHmm
     * @param outFmt String 返回样式，参照类说明，如：yyyy年MM月dd日
     * @throws ParseException 当datestr不能格式化为inFmt格式时抛出此异常
     * @return String 格式化后的日期、时间字符串，如：2004年10月01日、2004年10月01日 <BR>
     *         输出样式outFmt非法时，使用yyyyMMdd格式输出
     */
    public static String getDateFormat(String datestr, String inFmt, String outFmt) throws ParseException {
        if (null == datestr || "".equals(datestr.trim())) {
            return datestr;
        }

        if (null == inFmt || "".equals(inFmt.trim())) {
            return datestr;
        }

        if (null == outFmt || "".equals(outFmt.trim())) { // 输出样式非法
            outFmt = "yyyyMMdd";
        }

        Date inDate = getDate(datestr, inFmt);

        if (null == inDate) { // 根据inFmt分析datestr时抛出异常
            return datestr;
        }

        String retu = getDateFormat(inDate, outFmt);
        inDate = null;
        return retu;
    }

    /**
     * 把日期时间字符串，按inFmt样式转化为日期对象，然后格式化为默认样式yyyyMMdd
     *
     * @param datestr String 存在一定格式的日期、时间字符串，如：20041001、200410011503
     * @param inFmt String 对datestr参数格式说明，参照类说明，如：yyyyMMdd、yyyyMMddHHmm
     * @throws ParseException 当datestr不能格式化为inFmt格式时抛出此异常
     * @return String 格式化后的日期、时间字符串，如：20041001、20041001
     */
    public static String getDateFormat(String datestr, String inFmt) throws ParseException {
        return getDateFormat(datestr, inFmt, "yyyyMMdd");
    }

    /**
     * 根据inFmt的样式，日期时间字符串转化为日期时间对象
     *
     * @param datestr String 日期时间字符串，如：20041001、2004年10月01日 15:03
     * @param inFmt String 对datestr参数格式说明，参照类说明，如yyyyMMdd、yyyy年MM月dd日 HH:mm
     * @throws ParseException 当datestr不能格式化为inFmt格式时抛出此异常
     * @return Date 日期时间对象，格式inFmt非法时，使用yyyyMMdd格式
     */
    public static Date getDate(String datestr, String inFmt) throws ParseException {
        if (null == datestr || "".equals(datestr.trim())) {
            return null;
        }

        if (null == inFmt || "".equals(inFmt.trim())) { // inFmt非法
            inFmt = "yyyyMMdd";
        }

        Date inDate = null;

        // 依据inFmt格式把日期字符串转化为日期对象
        SimpleDateFormat inDateFormat = new SimpleDateFormat(inFmt);
        inDateFormat.setLenient(true);
        inDate = inDateFormat.parse(datestr);

        inDateFormat = null;
        return inDate;
    }

    /**
     * 对日期时间对象进行调整，实现如昨天是几号，去年的今天星期几等. <BR>
     * 例子：
     *
     * <pre>
     * <blockquote>
     * 计算去年今天星期几
     * Date date = DateUtils.addDate(new Date(),Calendar.YEAR,-1);
     * System.out.println(DateUtils.getDateFormat(date,"E"));
     * 打印60天后是什么日期，并显示为 yyyy-MM-dd 星期
     * Date date = DateUtils.addDate(new Date(),Calendar.DATE,60);
     * System.out.println(DateUtils.getDateFormat(date,"yyyy-MM-dd E"));
     * </blockquote>
     * </pre>
     *
     * @param date Date 需要调整的日期时间对象
     * @param CALENDARFIELD int 对日期时间对象以什么单位进行调整：
     *
     *            <pre>
     * <blockquote>
     * 年 Calendar.YEAR
     * 月 Calendar.MONTH
     * 日 Calendar.DATE
     * 时 Calendar.HOUR
     * 分 Calendar.MINUTE
     * 秒 Calendar.SECOND
     * </blockquote>
     * </pre>
     * @param amount int 调整数量，>0表向后调整（明天，明年），<0表向前调整（昨天，去年）
     * @return Date 调整后的日期时间对象
     */
    public static Date addDate(Date date, int CALENDARFIELD, int amount) {
        if (null == date) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(CALENDARFIELD, amount);
        return calendar.getTime();
    }

    /**
     * 对日期时间对象进行调整.
     *
     * @param datestr String 需要调整的日期时间字符串，它的格式为yyyyMMdd
     * @param CALENDARFIELD int 对日期时间对象以什么单位进行调整
     * @param amount int 调整数量
     * @throws ParseException 当datestr不能格式化为yyyyMMdd格式时抛出此异常
     * @return Date 调整后的日期时间对象
     * @see #addDate(Date, int, int)
     */
    public static Date addDate(String datestr, int CALENDARFIELD, int amount) throws ParseException {
        return addDate(getDate(datestr, "yyyyMMdd"), CALENDARFIELD, amount);
    }

    /**
     * 对日期时间对象进行调整.
     *
     * @param datestr String 需要调整的日期时间字符串，它的格式为yyyyMMdd
     * @param CALENDARFIELD int 对日期时间对象以什么单位进行调整
     * @param amount int 调整数量
     * @throws ParseException 当datestr不能格式化为yyyyMMdd格式时抛出此异常
     * @return Date 调整后的日期时间对象
     * @see #addDate(Date, int, int)
     */
    public static Date addDate(String datestr, int CALENDARFIELD, int amount, String format) throws ParseException {
        if(StringUtils.isBlank(format)){
            return addDate(getDate(datestr, "yyyyMMdd"), CALENDARFIELD, amount);
        }else{
            return addDate(getDate(datestr, format), CALENDARFIELD, amount);
        }
    }
    /**
     * 根据出生日期，计算出在某一个日期的年龄
     *
     * @param birthday Date 出生日期时间对象
     * @param date2 Date 计算日期对象
     * @return int 返回date2那一天出生日期为birthday的年龄，如果birthday大于date2则返回-1
     */
    public static int getAge(Date birthday, Date date2) {
        if (null == birthday || null == date2) {
            return -1;
        }

        if (birthday.after(date2)) { // birthday大于date2
            return -1;
        }

        int ibdYear = StringUtils.getInt(getDateFormat(birthday, "yyyy"), -1);
        int idate2Year = StringUtils.getInt(getDateFormat(date2, "yyyy"), -1);

        if (ibdYear < 0 || idate2Year < 0) {
            return -1;
        }
        if (ibdYear > idate2Year) {
            return -1;
        }

        return idate2Year - ibdYear + 1;
    }

    /**
     * 根据出生日期，计算出当前的年龄
     *
     * @param birthday Date 出生日期时间对象
     * @return int 返回出生日期为birthday的年龄，如果birthday大于当前系统日期则返回-1
     */
    public static int getAge(Date birthday) {
        return getAge(birthday, new Date());
    }

    /**
     * 根据出生日期，计算出当前的年龄
     *
     * @param birthdaystr String 出生日期时间字符串，其格式一定为yyyyMMdd
     * @throws ParseException 当datestr不能格式化为yyyyMMdd格式时抛出此异常
     * @return int 返回出生日期为birthday的年龄，如果birthday大于当前系统日期则返回-1
     */
    public static int getAge(String birthdaystr) throws ParseException {
        return getAge(getDate(birthdaystr, "yyyyMMdd"));
    }

    public static Date getCurDate() {
        return Calendar.getInstance().getTime();
    }

    public static String getCurDay(Date date) {
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }

    /**
     * 格式化
     *
     * @param str 字符
     *
     * @return Date
     * @throws ParseException 异常
     */
    public static Date parse(final String str) throws ParseException
    {
        final Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
        return date;
    }
    
    /**
     * 
     * <p>
     * 	计算指定日期到今天的天数
     * </p> 
     * @return 返回值说明
     */
    public static Long betweenDay(String beginDate) throws ParseException{
    	SimpleDateFormat   df   =   new   SimpleDateFormat("yyyy-MM-dd");
    	Date   cDate   =   df.parse(beginDate);
		Date   dDate   =   new Date();  
		return (dDate.getTime()-cDate.getTime())/(24*60*60*1000);
    }
    
    /**
     * 
     * <p>
     *  计算指定日期到今天的天数
     * </p> 
     * @return 返回值说明
     */
    public static Long betweenDay(Date destDate){
        Date   dDate   =   new Date();  
        return (destDate.getTime()-dDate.getTime())/(24*60*60*1000);
    }
    
    /**
     * 
     * <p>
     *  计算指定日期到今天的天数
     * </p> 
     * @return 返回值说明
     */
    public static Long subDay(Date day1,Date day2){
        return (day1.getTime()-day2.getTime())/(24*60*60*1000);
    }
    
    public static String dateToStr(String dateFormat, Date date){
    	//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }
    
    //获取几天前日期
    public static String getDateBefore(Date d,int day){  
	   Calendar now =Calendar.getInstance();  
	   now.setTime(d);  
	   now.set(Calendar.DATE,now.get(Calendar.DATE)-day);
	   SimpleDateFormat   sdf   =   new   SimpleDateFormat("yyyyMMdd");
	   return sdf.format(now.getTime());  
	}
    
    /**
     * 
     * <p>
     * 校验日期是否失效
     * </p>
     * 
     * @return 返回值说明
     */
    public static boolean validDateExpired(Date date)
        throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sdf.format(new Date());
        Date current = sdf.parse(now);
        return current.after(date);
    }
    
    /**
     * 某一个月第一天和最后一天
     * 
     * @param date
     * @return
     */
    public static Map<String, Date> getFirstday_Lastday_Month(Date date)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Date theDate = calendar.getTime();
        
        // 上个月第一天
        GregorianCalendar gcLast = (GregorianCalendar)Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
        day_first = str.toString();
        
        // 上个月最后一天
        calendar.add(Calendar.MONTH, 1); // 加一个月
        calendar.set(Calendar.DATE, 1); // 设置为该月第一天
        calendar.add(Calendar.DATE, -1); // 再减一天即为上个月最后一天
        String day_last = df.format(calendar.getTime());
        StringBuffer endStr = new StringBuffer().append(day_last).append(" 23:59:59");
        day_last = endStr.toString();
        
        Map<String, Date> map = new HashMap<String, Date>();
        try
        {
            map.put("first", getDate(day_first, "yyyy-MM-dd HH:mm:ss"));
            map.put("last", getDate(day_last, "yyyy-MM-dd HH:mm:ss"));
        }
        catch (ParseException e)
        {
            throw new RuntimeException("日期转化异常");
        }
        
        return map;
    }

    public static Date getFirstdayOfMonth(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Date theDate = calendar.getTime();
        
        // 上个月第一天
        GregorianCalendar gcLast = (GregorianCalendar)Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        return gcLast.getTime();
    }
    
    public static Date getLastdayOfMonth(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 上个月最后一天
        calendar.add(Calendar.MONTH, 1); // 加一个月
        calendar.set(Calendar.DATE, 1); // 设置为该月第一天
        calendar.add(Calendar.DATE, -1); // 再减一天即为上个月最后一天
        return calendar.getTime();
    }
    /**
     * <p>
     * Description:[清理指定格式为的数据]
     * </p> 
     * @return 返回值说明
     */
    public static Date getFormatDate(Date date, String format){
        try
        {
            if(StringUtils.isBlank(format)){
                format = "yyyy-MM-dd";
            }
            return getDate(getDateFormat(date, format), format);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * <p>
     * Description:[清理指定格式为的数据]
     * </p> 
     * @return 返回值说明
     */
    public static int getDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }
}

