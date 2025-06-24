/**
 * README
 *
 * Name: EXT100MI.DelLine
 * Description: Delete a record from FAACCB
 * Date                         Changed By                    Description
 * 20250616                     a.ferre@hetic3.fr     		création
 */
public class DelLine extends ExtendM3Transaction {
	private final MIAPI mi
	private final ProgramAPI program
	private final DatabaseAPI database
	private final UtilityAPI utility


	public DelLine(MIAPI mi, DatabaseAPI database, UtilityAPI utility, ProgramAPI program) {
		this.mi = mi
		this.program = program
		this.database = database
		this.utility = utility

	}

	public void main() {
		Integer cono = mi.in.get("CONO")
		String  divi = (mi.inData.get("DIVI") == null) ? "" : mi.inData.get("DIVI").trim()
		Integer rcno = mi.in.get("RCNO")
		Integer rgln = mi.in.get("RGLN")
		
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
		
		if(rgln == null || rgln == 0) {
			mi.error("Le numéro de ligne est obligatoire.")
			return
		}
		
		DBAction faaccbRecord = database.table("FAACCB").index("00").build()
		DBContainer faaccbContainer = faaccbRecord.createContainer()
		faaccbContainer.setInt("FBCONO", cono)
		faaccbContainer.setString("FBDIVI", divi)
		faaccbContainer.setInt("FBRCNO", rcno)
		faaccbContainer.setInt("FBRGLN", rgln)
		
		boolean deleted = faaccbRecord.readLock(faaccbContainer, { LockedResult delRecoord ->
			delRecoord.delete()
		})

		if(!deleted)
		{
			mi.error("L'enregistrement n'existe pas.")
			return
		}
	}
}