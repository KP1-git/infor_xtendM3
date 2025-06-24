/**
 * README
 *
 * Name: EXT102MI.DelHead
 * Description: Remove a record from FGDITH
 * Date                         Changed By                    Description
 * 20250623                     d.decosterd@hetic3.fr     		création
 */
public class DelHead extends ExtendM3Transaction {
	private final MIAPI mi;
	private final DatabaseAPI database
	private final ProgramAPI program
	private final UtilityAPI utility

	public DelHead(MIAPI mi, DatabaseAPI database, ProgramAPI program, UtilityAPI utility) {
		this.mi = mi;
		this.database = database
		this.program = program
		this.utility = utility
	}

	public void main() {
		Integer cono = mi.in.get("CONO")
		String  divi = (mi.inData.get("DIVI") == null) ? "" : mi.inData.get("DIVI").trim()
		String  ttab = (mi.inData.get("TTAB") == null) ? "" : mi.inData.get("TTAB").trim()

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

		DBAction fgdithRecord = database.table("FGDITH").index("00").selection("BZTXID").build()
		DBContainer fgdithContainer = fgdithRecord.createContainer()
		fgdithContainer.setInt("BZCONO", cono)
		fgdithContainer.setString("BZDIVI", divi)
		fgdithContainer.setString("BZTTAB", ttab)

		boolean deleted = false;
		Long txid = 0;
		boolean found = fgdithRecord.readLock(fgdithContainer, { LockedResult delRecoord ->
			txid = delRecoord.getLong("BZTXID")
			deleted = delRecoord.delete()
		})

		if(deleted) {
			if(txid != 0) {
				if(utility.call("TextFieldUtil", "del", database,"FSYTXH","TH", cono, divi, txid)) {
					utility.call("TextFieldUtil", "del", database,"FSYTXL","TL", cono, divi, txid)
				}
			}
			DBAction fgditdRecord = database.table("FGDITD").index("00").build()
			DBContainer fgditdContainer = fgditdRecord.createContainer()
			fgditdContainer.setInt("BECONO", cono)
			fgditdContainer.setString("BEDIVI", divi)
			fgditdContainer.setString("BETTAB", ttab)

			fgditdRecord.readAll(fgditdContainer, 3, 10000,{ DBContainer container ->
				fgditdRecord.readLock(container, { LockedResult lockedResult ->
					lockedResult.delete()
				})
			})
		}else if(found){
			mi.error("Erreur à la suppression de l'enregistrement.")
			return
		}

		if(!found)
		{
			mi.error("L'enregistrement n'existe pas.")
			return
		}

	}
}