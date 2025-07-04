/**
 * README
 *
 * Name: EXT102MI.AddHead
 * Description: Add a record in FGDITH
 * Date                         Changed By                    Description
 * 20250623                     d.decosterd@hetic3.fr     		création
 */
public class AddHead extends ExtendM3Transaction {
	private final MIAPI mi
	private final DatabaseAPI database
	private final ProgramAPI program
	private final UtilityAPI utility

	public AddHead(MIAPI mi, DatabaseAPI database, ProgramAPI program, UtilityAPI utility) {
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

		if(!utility.call("CheckUtil", "checkConoExist", database, cono)) {
			mi.error("La division est inexistante.")
			return
		}

		if(ttab.isBlank()) {
			mi.error("Table cible reventilation est obligatoire")
			return
		}

		if(tx40.isBlank()) {
			mi.error("Désignation est obligatoire")
			return
		}

		if(dimt == null) {
			mi.error("Méthode de reventilation est obligatoire")
			return
		}

		if( dimt < 1 || dimt > 3) {
			mi.error("Méthode de reventilation "+dimt+" est invalide.")
			return
		}

		if(bdtp == null) {
			mi.error("Type d'affectation est obligatoire")
			return
		}

		if(bdtp <1 || bdtp > 7) {
			mi.error("Type d'affectation "+bdtp+" est invalide.")
			return
		}

		if(bdpc == null)
			bdpc = 0

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

		if(tx15.isBlank()) {
			tx15 = tx40.substring(0, 15)
		}

		DBAction fgdithRecord = database.table("FGDITH").index("00").build()
		DBContainer fgdithContainer = fgdithRecord.createContainer()
		fgdithContainer.setInt("BZCONO", cono)
		fgdithContainer.setString("BZDIVI", divi)
		fgdithContainer.setString("BZTTAB", ttab)

		if(!fgdithRecord.read(fgdithContainer)){
			fgdithContainer.setString("BZTX40", tx40)
			fgdithContainer.setString("BZTX15", tx15)
			fgdithContainer.setInt("BZDIMT", dimt)
			fgdithContainer.setInt("BZBDTP", bdtp)
			fgdithContainer.set("BZBDPC", bdpc)
			fgdithContainer.set("BZRGDT", (Integer) utility.call("DateUtil", "currentDateY8AsInt"))
			fgdithContainer.set("BZLMDT", (Integer) utility.call("DateUtil", "currentDateY8AsInt"))
			fgdithContainer.set("BZCHID", program.getUser())
			fgdithContainer.set("BZRGTM", (Integer) utility.call("DateUtil", "currentTimeAsInt"))
			fgdithContainer.set("BZCHNO", 1)
			fgdithRecord.insert(fgdithContainer)
		}else
		{
			mi.error("Enregistrement existe déjà.")
			return
		}

	}
}