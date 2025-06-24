/**
 * README
 *
 * Name: EXT101MI.DelLine
 * Description: Del a record in FGDIBD
 * Date                         Changed By                    Description
 * 20250623                     a.ferre@hetic3.fr     		création
 */
public class DelLine extends ExtendM3Transaction {
	private final MIAPI mi
	private final ProgramAPI program
	private final DatabaseAPI database
	private final UtilityAPI utility
	private final MICallerAPI miCaller
	
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
		String  btab = (mi.inData.get("BTAB") == null) ? "" : mi.inData.get("BTAB").trim()
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

		if(btab.isBlank()) {
			mi.error("Table de base d'affectation est obligatoire")
			return
		}

		if(bbln == null || bbln == 0) {
			mi.error("Numéro de ligne doit être renseigné.")
			return
		}
		
		DBAction fgdibdRecord = database.table("FGDIBD").index("00").build()
		DBContainer fgdibdContainer = fgdibdRecord.createContainer()
		fgdibdContainer.setInt("BYCONO", cono)
		fgdibdContainer.setString("BYDIVI", divi)
		fgdibdContainer.setString("BYBTAB", btab)
		fgdibdContainer.setInt("BYBBLN", bbln)
		
		boolean deleted = fgdibdRecord.readLock(fgdibdContainer, { LockedResult delRecoord ->
			delRecoord.delete()
		})

		if(!deleted)
		{
			mi.error("L'enregistrement n'existe pas.")
			return
		}
	  
	}
  }
