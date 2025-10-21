/**
 * README
 *
 * Name: EXT102MI.UpdHead
 * Description: Update a record in FGDITH
 * Date                         Changed By                    Description
 * 20250624                     d.decosterd@hetic3.fr     		création
 */
public class UpdHead extends ExtendM3Transaction {
	private final MIAPI mi
	private final DatabaseAPI database
	private final ProgramAPI program
	private final UtilityAPI utility

	public UpdHead(MIAPI mi, DatabaseAPI database, ProgramAPI program, UtilityAPI utility) {
		this.mi = mi
		this.database = database
		this.program = program
		this.utility = utility
	}

	public void main() {
		Integer cono = mi.in.get("CONO")
		String  divi = (mi.inData.get("DIVI") == null) ? "" : mi.inData.get("DIVI").trim()
		String  ttab = (mi.inData.get("TTAB") == null) ? "" : mi.inData.get("TTAB").trim()
		String  tx40 = (mi.inData.get("TX40") == null) ? "" : mi.inData.get("TX40").trim()
		String  tx15 = (mi.inData.get("TX15") == null) ? "" : mi.inData.get("TX15").trim()
		Integer dimt = mi.in.get("DIMT")
		Integer bdtp = mi.in.get("BDTP")
		Float bdpc = mi.in.get("BDPC")

		if(cono == null) {
			mi.error("La CONO est obligatoire.")
			return
		}

		if(divi.isBlank()) {
			mi.error("L'api devrait être utilisé localement, DIVI obligatoire.")
			return
		}

		if(ttab.isBlank()) {
			mi.error("Table cible reventilation est obligatoire")
			return
		}

		if( dimt != null && (dimt < 1 || dimt > 3)) {
			mi.error("Méthode de reventilation "+dimt+" est invalide.")
			return
		}

		if(bdtp != null && (bdtp <1 || bdtp > 7)) {
			mi.error("Type d'affectation "+bdtp+" est invalide.")
			return
		}

		if(bdpc != null) {
			if(bdpc<0) {
				mi.error("Frais généraux supplémentaires ne peut pas être négatif")
				return
			}

			int numberOfDiggits = utility.call("NumberUtil", "getNumberOfDigits", bdpc)

			if(numberOfDiggits > 3) {
				mi.error("Frais généraux supplémentaires ne peux avoir plus de trois chiffres avant la virgule.")
				return
			}

			int numberOfDecimal = utility.call("NumberUtil", "getNumberOfDecimals", bdpc)
			if(numberOfDecimal > 3) {
				mi.error("Frais généraux supplémentaires ne peux avoir plus de trois chiffres après la virgule.")
				return
			}
		}

		DBAction fgdithRecord = database.table("FGDITH").index("00").selection("BZCHNO").build()
		DBContainer fgdithContainer = fgdithRecord.createContainer()
		fgdithContainer.setInt("BZCONO", cono)
		fgdithContainer.setString("BZDIVI", divi)
		fgdithContainer.setString("BZTTAB", ttab)

		boolean updatable = fgdithRecord.readLock(fgdithContainer, { LockedResult updateRecord ->
			if(!tx40.isBlank())
				updateRecord.setString("BZTX40", tx40)
			if(!tx15.isBlank())
				updateRecord.setString("BZTX15", tx15)
			if(dimt != null)
				updateRecord.setInt("BZDIMT", dimt)
			if(bdtp != null)
				updateRecord.setInt("BZBDTP", bdtp)
			if(bdpc != null)
				updateRecord.set("BZBDPC", bdpc)
			int CHNO = updateRecord.getInt("BZCHNO")
			if(CHNO== 999) {CHNO = 0}
			CHNO++
			updateRecord.set("BZLMDT", (Integer) utility.call("DateUtil", "currentDateY8AsInt"))
			updateRecord.set("BZCHID", program.getUser())
			updateRecord.setInt("BZCHNO", CHNO)
			updateRecord.update()
		})

		if(!updatable){
			mi.error("L'enregistrement n'existe pas.")
			return
		}

	}
}