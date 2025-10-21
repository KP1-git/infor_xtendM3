/**
 * README
 *
 * Name: EXT101MI.DelHead
 * Description: Delete a record in FGDIBH
 * Date                         Changed By                    Description
 * 20250616                     a.ferre@hetic3.fr     		création
 */
public class DelHead extends ExtendM3Transaction {
	private final MIAPI mi
	private final ProgramAPI program
	private final DatabaseAPI database
	private final UtilityAPI utility

	public DelHead(MIAPI mi, DatabaseAPI database, UtilityAPI utility, ProgramAPI program) {
		this.mi = mi
		this.program = program
		this.database = database
		this.utility = utility
	}

	public void main() {
		Integer cono = mi.in.get("CONO")
		String  divi = (mi.inData.get("DIVI") == null) ? "" : mi.inData.get("DIVI").trim()
		String  btab = (mi.inData.get("BTAB") == null) ? "" : mi.inData.get("BTAB").trim()

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

		if(btab.isBlank()) {
			mi.error("Table de base d'affectation est obligatoire")
			return
		}

		DBAction fgdibhRecord = database.table("FGDIBH").index("00").build()
		DBContainer fgdibhContainer = fgdibhRecord.createContainer()
		fgdibhContainer.setInt("BXCONO", cono)
		fgdibhContainer.setString("BXDIVI", divi)
		fgdibhContainer.setString("BXBTAB", btab)


		boolean deleted = false
		boolean found = fgdibhRecord.readLock(fgdibhContainer, { LockedResult delRecoord ->
			deleted = delRecoord.delete()
		})

		int nrOfRecords = mi.getMaxRecords() <= 0 || mi.getMaxRecords() >= 10000 ? 10000: mi.getMaxRecords()
		
		if(!found)
		{
			mi.error("L'enregistrement n'existe pas.")
			return
		}else {
			DBAction fgdibdRecord = database.table("FGDIBD").index("00").build()
			DBContainer fgdibdContainer = fgdibdRecord.createContainer()
			fgdibdContainer.setInt("BYCONO", cono)
			fgdibdContainer.setString("BYDIVI", divi)
			fgdibdContainer.setString("BYBTAB", btab)

			fgdibdRecord.readAll(fgdibdContainer, 3, nrOfRecords,{ DBContainer container ->
				fgdibdRecord.readLock(container, { LockedResult lockedResult ->
					lockedResult.delete()
				})
			})

		}
	}
}