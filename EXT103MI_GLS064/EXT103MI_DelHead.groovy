/**
 * README
 *
 * Name: EXT103MI.DelHead
 * Description: Delete a record in FGDISH
 * Date                         Changed By                    Description
 * 20250624                     a.ferre@hetic3.fr     		création
 */
public class DelHead extends ExtendM3Transaction {
	private final MIAPI mi
	private final ProgramAPI program
	private final DatabaseAPI database
	private final UtilityAPI utility
	private final MICallerAPI miCaller

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
		String  stab = (mi.inData.get("STAB") == null) ? "" : mi.inData.get("STAB").trim()

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

		if(stab.isBlank()) {
			mi.error("Table de base d'affectation est obligatoire")
			return
		}

		DBAction fgdishRecord = database.table("FGDISH").index("00").build()
		DBContainer fgdishContainer = fgdishRecord.createContainer()
		fgdishContainer.setInt("BFCONO", cono)
		fgdishContainer.setString("BFDIVI", divi)
		fgdishContainer.setString("BFSTAB", stab)	
		
		boolean deleted = false
		boolean found = fgdishRecord.readLock(fgdishContainer, { LockedResult delRecoord ->
			deleted = delRecoord.delete()
		})

		
		if(!found)
		{
			mi.error("L'enregistrement n'existe pas.")
			return
		}else {
			DBAction fgdisdRecord = database.table("FGDISD").index("00").build()
			DBContainer fgdisdContainer = fgdisdRecord.createContainer()
			fgdisdContainer.setInt("BGCONO", cono)
			fgdisdContainer.setString("BGDIVI", divi)
			fgdisdContainer.setString("BGSTAB", stab)
			
			fgdisdRecord.readAll(fgdisdContainer, 3, 10000,{ DBContainer container ->
				fgdisdRecord.readLock(container, { LockedResult lockedResult ->
					lockedResult.delete()
				})
			})

		}
	}
}