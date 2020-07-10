package jp.co.acom.riza.cep.message.util;

public class RizaCepMessageUtil {
    /**
     * 実行中のメソッド名を取得します。
     * 
     * @return メソッド名
     */
    public static String getMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

}