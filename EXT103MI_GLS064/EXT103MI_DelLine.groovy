/****************************************************************************************
 Extension Name: EXT103MI/DelLine
 Type: ExtendM3Transaction
 Script Author: FERRE Adrien
 Date: 26/02/2026
 Description: Delete record FGDISD
    
 Revision History:
 Name                    		Date             Version          Description of Changes
 First creation FERRE Adrien 	26/02/2026       1.0              Creation 

******************************************************************************************/

public class DelLine extends ExtendM3Transaction {
	private final MIAPI mi
	private final ProgramAPI program
	private final DatabaseAPI database
	private final UtilityAPI utility
	private final MICallerAPI miCaller

	/*
	 * Transaction EXT103MI/DelLine Interface
	 * @param mi - Infor MI Interface
	 * @param database - Infor Database Interface
	 * @param utility - Utility Interface
	 * @program program - ProgramAPI Interface
	 * @MICallerAPI - MICallerAPI Interface
	 */
	public DelLine(MIAPI mi, DatabaseAPI database, UtilityAPI utility, ProgramAPI program, MICallerAPI miCaller) {
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
		Integer bbln = mi.in.get("BBLN")
		
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
		
		if(bbln == null || bbln == 0) {
			mi.error("Numéro de ligne doit être renseigné.")
			return
		}
		
		DBAction fgdisdRecord = database.table("FGDISD").index("00").build()
		DBContainer fgdisdContainer = fgdisdRecord.createContainer()
		fgdisdContainer.setInt("BGCONO", cono)
		fgdisdContainer.setString("BGDIVI", divi)
		fgdisdContainer.setString("BGSTAB", stab)
		fgdisdContainer.setInt("BGBBLN", bbln)
		
		boolean deleted = fgdisdRecord.readLock(fgdisdContainer, { LockedResult delRecoord ->
			delRecoord.delete()
		})

		if(!deleted)
		{
			mi.error("L'enregistrement n'existe pas.")
			return
		}
	}
}