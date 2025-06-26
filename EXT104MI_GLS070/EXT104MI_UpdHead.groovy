/**
 * README
 *
 * Name: EXT104MI.UpdHead
 * Description: Update a record in FGDIST
 * Date                         Changed By                    Description
 * 20250625                     a.ferre@hetic3.fr     		création
 */
public class UpdHead extends ExtendM3Transaction {
	private final MIAPI mi
	private final ProgramAPI program
	private final DatabaseAPI database
	private final UtilityAPI utility
	private final MICallerAPI miCaller

	public UpdHead(MIAPI mi, DatabaseAPI database, UtilityAPI utility, ProgramAPI program, MICallerAPI miCaller) {
		this.mi = mi
		this.program = program
		this.database = database
		this.utility = utility
		this.miCaller = miCaller
	}

	public void main() {
		Integer cono = mi.in.get("CONO")
		String  divi = (mi.inData.get("DIVI") == null) ? "" : mi.inData.get("DIVI").trim()
		String  dtmp = (mi.inData.get("DTMP") == null) ? "" : mi.inData.get("DTMP").trim()
		Integer dspr = mi.in.get("DSPR")
		String  tx40 = (mi.inData.get("TX40") == null) ? "" : mi.inData.get("TX40").trim()
		String  tx15 = (mi.inData.get("TX15") == null) ? "" : mi.inData.get("TX15").trim()
		Integer attp = mi.in.get("ATTP")

		// Convertir l'entier en chaîne de caractères
		String valeurStr = dspr.toString()



		if(cono == null) {
			mi.error("La CONO est obligatoire.")
			return
		}

		if(divi.isBlank()) {
			mi.error("L'api devrait être utilisé localement, DIVI obligatoire.")
			return
		}

		DBAction cmndivRecord = database.table("CMNDIV").index("00").build()
		DBContainer cmdivContainer = cmndivRecord.createContainer()
		cmdivContainer.setInt("CCCONO", cono)
		cmdivContainer.setString("CCDIVI", divi)


		if(!cmndivRecord.read(cmdivContainer)){
			mi.error("DIVI n'éxiste pas.")
			return
		}

		if(dtmp.isBlank()) {
			mi.error("Modèle de reventilation est obligatoire")
			return
		}

		// Vérification
		if (!(valeurStr ==~ /^\d{6}$/)) {
			mi.error("Valeur pas au bon format : doit contenir exactement 6 chiffres (AAAAMM)")
		}

		int mois = valeurStr[4..5].toInteger()
		if (!(mois in 1..12)) {
			mi.error("Valeur pas au bon format : les deux derniers chiffres doivent représenter un mois entre 01 et 12")
		}

		if(tx40.isBlank()) {
			mi.error("Désignation est obligatoire")
			return
		}
		
		if(attp != 1 && attp != 2) {
			mi.error("Type modèle affection doit être 1 ou 2")
			return
		}
		
		DBAction fgdistRecord = database.table("FGDIST").index("00").selection("BUCHNO").build()
		DBContainer fgdistContainer = fgdistRecord.createContainer()
		fgdistContainer.setInt("BUCONO", cono)
		fgdistContainer.setString("BUDIVI", divi)
		fgdistContainer.setString("BUDTMP", dtmp)
		fgdistContainer.setInt("BUDSPR", dspr)
		
		
		
		boolean updatable = fgdistRecord.readLock(fgdistContainer, { LockedResult updateRecord ->
			updateRecord.setString("BUTX40", tx40)
			updateRecord.setString("BUTX15", tx15)
			updateRecord.set("BUATTP", attp)

			
			int CHNO = updateRecord.getInt("BUCHNO")
			if(CHNO== 999) {CHNO = 0}
			CHNO++
			updateRecord.set("BULMDT", (Integer) utility.call("DateUtil", "currentDateY8AsInt"))
			updateRecord.set("BUCHID", program.getUser())
			updateRecord.setInt("BUCHNO", CHNO)
			updateRecord.update()
		})

		if(!updatable)
		{
			mi.error("L'enregistrement n'existe pas.")
			return
		}

	}
}