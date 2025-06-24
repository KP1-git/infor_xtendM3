/**
 * README
 *
 * Name: TextFieldUtil
 * Description: Common textField helpers
 * Date                         Changed By                         Description
 * 20250623                     d.decosterd@hetic3.fr     		creation
 */
public class TextFieldUtil extends ExtendM3Utility {
	/**
	 *Del all record of the textField table with the TXID = txtID 
	 */
	public String del(DatabaseAPI database, String tableName, String tablePrefix, Integer cono, String divi, Long txtID ) {
		DBAction txtHeadRecord = database.table(tableName).index("00").build()
		DBContainer txtHeadContainer = txtHeadRecord.createContainer()
		txtHeadContainer.setInt(tablePrefix+"CONO", cono)
		txtHeadContainer.setString(tablePrefix+"DIVI", divi)
		txtHeadContainer.setLong(tablePrefix+"TXID", txtID)

		boolean hasDeleted = false 
		txtHeadRecord.readAll(txtHeadContainer, 3, 1000,{ DBContainer container ->
			txtHeadRecord.readLock(container, { LockedResult lockedResult ->
				if(lockedResult.delete()) {
					hasDeleted = true
				}
			})
		})
		return hasDeleted
	}
}