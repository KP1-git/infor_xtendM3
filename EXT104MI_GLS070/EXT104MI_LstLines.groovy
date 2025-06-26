/**
 * README
 *
 * Name: EXT104MI.LstLines
 * Description: List records from FGDISE
 * Date                         Changed By                    Description
 * 20250625                     a.ferre@hetic3.fr     		création
 */
public class LstLines extends ExtendM3Transaction {
	private final MIAPI mi
	private final ProgramAPI program
	private final DatabaseAPI database
	private final UtilityAPI utility
	private final MICallerAPI miCaller


	public LstLines(MIAPI mi, DatabaseAPI database, UtilityAPI utility, ProgramAPI program, MICallerAPI miCaller, LoggerAPI logger) {
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

		DBAction fgdiseRecord = database.table("FGDISE").index("00").selectAllFields().build()
		DBContainer fgdiseContainer = fgdiseRecord.createContainer()
		fgdiseContainer.setInt("BVCONO", cono)
		fgdiseContainer.setString("BVDIVI", divi)
		fgdiseContainer.setString("BVDTMP", divi)
		fgdiseContainer.setInt("BVDSPR", dspr)
		
		int nrOfRecords = mi.getMaxRecords() <= 0 || mi.getMaxRecords() >= 10000? 10000: mi.getMaxRecords()
		fgdiseRecord.readAll(fgdiseContainer, 2, nrOfRecords,{ DBContainer container ->
			mi.getOutData().put("CONO", cono.toString())
			mi.getOutData().put("DIVI", divi)
			mi.getOutData().put("DTMP", container.getString("BVDTMP"))
			mi.getOutData().put("DSPR", container.get("BVDSPR").toString())
			mi.getOutData().put("DELE", container.get("BVDELE").toString())
			mi.getOutData().put("TX40", container.getString("BVTX40"))
			mi.getOutData().put("TX15", container.getString("BVTX15"))
			mi.getOutData().put("BDLV", container.get("BVBDLV").toString())
			mi.getOutData().put("CFLV", container.getString("BVCFLV"))
			mi.getOutData().put("CTLV", container.getString("BVCTLV"))
			mi.getOutData().put("BTAB", container.getString("BVBTAB"))
			mi.getOutData().put("TTAB", container.getString("BVTTAB"))
			mi.getOutData().put("CABF", container.get("BVCABF").toString())
			mi.getOutData().put("BUN1", container.get("BVBUN1").toString())
			mi.getOutData().put("BVE1", container.getString("BVBVE1"))
			mi.getOutData().put("CASF", container.get("BVCASF").toString())
			mi.getOutData().put("BUN2", container.get("BVBUN2").toString())
			mi.getOutData().put("BVE2", container.getString("BVBVE2"))
			mi.getOutData().put("RDUV", container.getString("BVRDUV"))
			
			mi.getOutData().put("RGDT", container.get("BVRGDT").toString())
			mi.getOutData().put("RGTM", container.get("BVRGTM").toString())
			mi.getOutData().put("LMDT", container.get("BVLMDT").toString())
			mi.getOutData().put("CHNO", container.get("BVCHNO").toString())
			mi.getOutData().put("CHID", container.getString("BVCHID") )
			
			mi.write()
		})

	}
}