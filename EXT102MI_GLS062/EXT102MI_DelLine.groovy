/****************************************************************************************
 Extension Name: EXT102MI/DelLine
 Type: ExtendM3Transaction
 Script Author: a.ferre@hetic3.fr
 Date: 2025-07-01
 Description:
 * Delete a record from FGDITD
 Revision History:
 Name                    Date             Version          Description of Changes
 a.ferre@hetic3.fr       2025-07-01       1.0              création
 ******************************************************************************************/
public class DelLine extends ExtendM3Transaction {
	private final MIAPI mi
	private final DatabaseAPI database
	private final ProgramAPI program
	private final UtilityAPI utility
	private final MICallerAPI miCaller
	private final MessageAPI message

	/*
	 * Transaction EXT102MI/DelLine
	 * @param mi - Infor MI Interface
	 * @param database - Infor Database Interface
	 * @param program - Infor Program Interface
	 * @param utility - Infor Utility Interface
	 * @param miCaller - Infor MiCaller Interface
	 * @param message - Infor Message Interface
	 */
	public DelLine(MIAPI mi, DatabaseAPI database, ProgramAPI program, UtilityAPI utility, MICallerAPI miCaller, MessageAPI message) {
		this.mi = mi
		this.database = database
		this.program = program
		this.utility = utility
		this.miCaller = miCaller
		this.message = message
	}

	public void main() {
		Integer cono = mi.in.get("CONO")
		String divi = (mi.inData.get("DIVI") == null) ? "" : mi.inData.get("DIVI").trim()
		String ttab = (mi.inData.get("TTAB") == null) ? "" : mi.inData.get("TTAB").trim()
		Integer bbln = mi.in.get("BBLN")

		if(cono == null) {
			mi.error("La CONO est obligatoire.")
			return
		}

		if(!utility.call("CheckUtil", "checkConoExist", database, cono)) {
			mi.error("La division est inexistante.")
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



		if(ttab.isBlank()) {
			mi.error("Table cible reventilation est obligatoire")
			return
		}

		if(bbln == null) {
			mi.error("Ligne de reventilation est obligatoire.")
			return
		}

		if(bbln <=0) {
			mi.error("Ligne de reventilation doit être supérieur à 0.")
			return
		}

		DBAction fgditdRecord = database.table("FGDITD").index("00").build()
		DBContainer fgditdContainer = fgditdRecord.createContainer()
		fgditdContainer.setInt("BECONO", cono)
		fgditdContainer.setString("BEDIVI", divi)
		fgditdContainer.setString("BETTAB", ttab)
		fgditdContainer.setInt("BEBBLN", bbln)

		boolean deleted = fgditdRecord.readLock(fgditdContainer, { LockedResult delRecoord ->
			delRecoord.delete()
		})

		if(!deleted)
		{
			mi.error("L'enregistrement n'existe pas.")
			return
		}
	}
}