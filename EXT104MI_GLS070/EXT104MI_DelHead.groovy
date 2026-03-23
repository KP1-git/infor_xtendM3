/****************************************************************************************
 Extension Name: EXT104MI/DelHead
 Type: ExtendM3Transaction
 Script Author: FERRE Adrien
 Date: 26/02/2026
 Description: Delete record FGDIST
    
 Revision History:
 Name                    		Date             Version          Description of Changes
 First creation FERRE Adrien 	26/02/2026       1.0              Creation 
 d.decosterd                    23/03/2026       1.1              Add missing return after some mi.error.
******************************************************************************************/

public class DelHead extends ExtendM3Transaction {
	private final MIAPI mi
	private final ProgramAPI program
	private final DatabaseAPI database
	private final UtilityAPI utility
	private final MICallerAPI miCaller

	/*
	 * Transaction EXT104MI/DelHead Interface
	 * @param mi - Infor MI Interface
	 * @param database - Infor Database Interface
	 * @param utility - Utility Interface
	 * @program program - ProgramAPI Interface
	 * @MICallerAPI - MICallerAPI Interface
	 */
	public DelHead(MIAPI mi, DatabaseAPI database, UtilityAPI utility, ProgramAPI program, MICallerAPI miCaller) {
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
			return
		}

		int mois = valeurStr[4..5].toInteger()
		if (!(mois in 1..12)) {
			mi.error("Valeur pas au bon format : les deux derniers chiffres doivent représenter un mois entre 01 et 12")
			return
		}


		DBAction fgdistRecord = database.table("FGDIST").index("00").build()
		DBContainer fgdistContainer = fgdistRecord.createContainer()
		fgdistContainer.setInt("BUCONO", cono)
		fgdistContainer.setString("BUDIVI", divi)
		fgdistContainer.setString("BUDTMP", dtmp)
		fgdistContainer.setInt("BUDSPR", dspr)

		boolean deleted = false
		boolean found = fgdistRecord.readLock(fgdistContainer, { LockedResult delRecoord ->
			deleted = delRecoord.delete()
		})

		int nrOfRecords = mi.getMaxRecords() <= 0 || mi.getMaxRecords() >= 10000 ? 10000: mi.getMaxRecords()

		if(!found)
		{
			mi.error("L'enregistrement n'existe pas.")
			return
		}else {
			DBAction fgdiseRecord = database.table("FGDISE").index("00").build()
			DBContainer fgdiseContainer = fgdiseRecord.createContainer()
			fgdiseContainer.setInt("BVCONO", cono)
			fgdiseContainer.setString("BVDIVI", divi)
			fgdiseContainer.setString("BVDTMP", dtmp)
			fgdiseContainer.setInt("BVDSPR", dspr)

			fgdiseRecord.readAll(fgdiseContainer, 3, nrOfRecords,{ DBContainer container ->
				fgdiseRecord.readLock(container, { LockedResult lockedResult ->
					lockedResult.delete()
				})
			})

		}

	}
}