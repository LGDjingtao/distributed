package org.example.mixed;

public class iptonumtest {

    // 将IP地址转成整形数字
    public static int strIpToIntIp(String strIP) {
        try {
            String[] strArray = strIP.split("\\.");
            int octet1 = Integer.parseInt(strArray[0]);
            int octet2 = Integer.parseInt(strArray[1]);
            int octet3 = Integer.parseInt(strArray[2]);
            int octet4 = Integer.parseInt(strArray[3]);

            // 计算整形数字
            long lHexTemp = (octet2 * 256 * 256) + (octet3 * 256) + octet4;
            String strHexTemp = String.format("%06X", lHexTemp);
            strHexTemp = String.format("%02X", octet1) + strHexTemp;

            return (int) Long.parseLong(strHexTemp, 16);
        } catch (Exception ex) {
            return 0;
        }
    }

    // 将整形数字的IP地址换成标准的IP地址串
    public static String intIpToStrIp(int intIP) {
        try {
            String strHexTemp = String.format("%08X", intIP);
            int octet1 = Integer.parseInt(strHexTemp.substring(0, 2), 16);
            int octet2 = Integer.parseInt(strHexTemp.substring(2, 4), 16);
            int octet3 = Integer.parseInt(strHexTemp.substring(4, 6), 16);
            int octet4 = Integer.parseInt(strHexTemp.substring(6, 8), 16);

            return octet1 + "." + octet2 + "." + octet3 + "." + octet4;
        } catch (Exception exp) {
            return intIP + "," + exp.getMessage();
        }
    }

    public static void main(String[] args) {
        // 示例用法
        String ipAddress = "192.168.4.41";
        int intIp = strIpToIntIp(ipAddress);
        System.out.println("IP地址转整形数字: " + intIp);

        String ipFromInt = intIpToStrIp(intIp);
        System.out.println("整形数字转IP地址: " + ipFromInt);
    }
}
