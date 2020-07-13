package jp.co.acom.riza.cep;

/**
 * CEPのコンスタント定義
 *
 * @author developer
 *
 */
public class CepConstants {

	/**
	 * CEPの開始メソッド名(URIの一部)
	 */
	public static final String CEP_START_METHOD = "start";
	/**
	 * CEPの終了メソッド名(URIの一部)
	 */
	public static final String CEP_END_METHOD = "end";
	/**
	 * CEPのベースURI取得キー
	 */
	public static final String CEP_BASE_URI = "CEP_BASE_URI";
	/**
	 * CEPの処理待ち期限(秒)取得キー
	 */
	public static final String CEP_EXPIRE_LIMIT = "CEP_EXPIRE_LIMIT";
	/**
	 * CEPの処理待ち期限(秒)デフォルト
	 */
	public static final int CEP_DEFAULT_EXPIRE_LIMIT = 30;
	/**
	 * CEPの無効化フラグ
	 */
	public static final String CEP_MOCK = "CEP_MOCK";
}
