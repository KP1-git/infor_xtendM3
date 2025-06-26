/**
 * README
 *
 * Name: EXT104MI.DelLine
 * Description: Delete a record from FGDISE
 * Date                         Changed By                    Description
 * 20250625                     a.ferre@hetic3.fr     		création
 */
public class DelLine extends ExtendM3Transaction {
	private final MIAPI mi
	private final ProgramAPI program
	private final DatabaseAPI database
	private final UtilityAPI utility
	private final MICallerAPI miCaller
	
	public DelLine(MIAPI mi, DatabaseAPI database, UtilityAPI utility, ProgramAPI program, MICallerAPI miCaller, LoggerAPI logger) {
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
		Integer dele = mi.in.get("DELE")
		
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

		if(dele == 0 || dele == null) {
			mi.error("Element d'allocation doit être renseigné")
			return
		}
		
		DBAction fgdiseRecord = database.table("FGDISE").index("00").build()
		DBContainer fgdiseContainer = fgdiseRecord.createContainer()
		fgdiseContainer.setInt("BVCONO", cono)
		fgdiseContainer.setString("BVDIVI", divi)
		fgdiseContainer.setString("BVDTMP", divi)
		fgdiseContainer.setInt("BVDSPR", dspr)
		fgdiseContainer.setInt("BVDELE", dele)
		
		boolean deleted = fgdiseRecord.readLock(fgdiseContainer, { LockedResult delRecoord ->
			delRecoord.delete()
		})

		if(!deleted)
		{
			mi.error("L'enregistrement n'existe pas.")
			return
		}
	}
  }
