/**
 * README
 *
 * Name: EXT100MI.DelHead
 * Description: Delete a record in FAACCH
 * Date                         Changed By                    Description
 * 20250616                     a.ferre@hetic3.fr     		création
 */
public class DelHead extends ExtendM3Transaction {
	private final MIAPI mi
	private final ProgramAPI program
	private final DatabaseAPI database
	private final UtilityAPI utility

	public DelHead(MIAPI mi, ProgramAPI program, DatabaseAPI database, UtilityAPI utility) {
		this.mi = mi
		this.program = program
		this.database = database
		this.utility = utility
	}

	public void main() {
		Integer cono = mi.in.get("CONO")
		String  divi = (mi.inData.get("DIVI") == null) ? "" : mi.inData.get("DIVI").trim()
		Integer rcno = mi.in.get("RCNO")
		
		if(cono == null) {
			mi.error("La CONO est obligatoire.")
			return
		}
		
		if(divi.isBlank()) {
			mi.error("L'api devrait être utilisé localement, DIVI obligatoire.")
			return
		}
		
		if(rcno == null || rcno == 0) {
			mi.error("Le numéro d'enregistrement est obligatoire.")
			return
		}
		
		DBAction faacchRecord = database.table("FAACCH").index("00").build()
		DBContainer faacchContainer = faacchRecord.createContainer()
		faacchContainer.setInt("FHCONO", cono)
		faacchContainer.setString("FHDIVI", divi)
		faacchContainer.setInt("FHRCNO", rcno)	
		
		boolean deleted = false
		boolean found = faacchRecord.readLock(faacchContainer, { LockedResult delRecoord ->
			deleted = delRecoord.delete()
		})

		int nrOfRecords = 5000
		if(!found)
		{
			mi.error("L'enregistrement n'existe pas.")
			return
		}else {
			DBAction faaccbRecord = database.table("FAACCB").index("00").build()
			DBContainer faaccbContainer = faaccbRecord.createContainer()
			faaccbContainer.setInt("FBCONO", cono)
			faaccbContainer.setString("FBDIVI", divi)
			faaccbContainer.setInt("FBRCNO", rcno)
			
			
			faaccbRecord.readAll(faaccbContainer, 3, nrOfRecords,{ DBContainer container ->
				faaccbRecord.readLock(container, { LockedResult lockedResult ->
					lockedResult.delete()
				})
			})

		}
		

	}
}