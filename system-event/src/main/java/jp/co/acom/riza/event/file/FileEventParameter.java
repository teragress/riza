package jp.co.acom.riza.event.file;
/**
 * ファイル受信イベントのパラメーター<br>
 * このパラメーターのjson形式で受け取る
 * @author developer
 *
 */
public class FileEventParameter {
	/**
	 * 受信ファイルPATH
	 */
	private String dataFilePath;
	/**
	 * 出力トピック名
	 */
	private String outTopic;
	/**
	 * ファイルデータの文字コード
	 */
	private String dataCharacterCode = "IBM-930";
	
	/**
	 * 受信ファイル形式のCOBOLコピー句ファイルPATH
	 */
	private String copyBookFilePath;
	
	/**
	 * @return 受信ファイルPATH
	 */
	public String getDataFilePath() {
		return dataFilePath;
	}

	/**
	 * @param dataFilePath 受信ファイルPATH
	 */
	public void setDataFilePath(String dataFilePath) {
		this.dataFilePath = dataFilePath;
	}

	/**
	 * @return 出力トピック名
	 */
	public String getOutTopic() {
		return outTopic;
	}

	/**
	 * @param outTopic 出力トピック名
	 */
	public void setOutTopic(String outTopic) {
		this.outTopic = outTopic;
	}

	/**
	 * @return ファイルデータの文字コード
	 */
	public String getDataCharacterCode() {
		return dataCharacterCode;
	}

	/**
	 * @param dataCharacterCode ファイルデータの文字コード
	 */
	public void setDataCharacterCode(String dataCharacterCode) {
		this.dataCharacterCode = dataCharacterCode;
	}

	/**
	 * @return 受信ファイル形式のCOBOLコピー句ファイルPATH
	 */
	public String getCopyBookFilePath() {
		return copyBookFilePath;
	}

	/**
	 * @param copyBookFilePath 受信ファイル形式のCOBOLコピー句ファイルPATH
	 */
	public void setCopyBookFilePath(String copyBookFilePath) {
		this.copyBookFilePath = copyBookFilePath;
	}
	
	

}
