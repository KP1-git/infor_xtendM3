/**
 * README
 *
 * Name: EXT101MI.UpdHead
 * Description: Update a record in FGDIBH
 * Date                         Changed By                    Description
 * 20250620                     a.ferre@hetic3.fr     		création
 */
public class UpdHead extends ExtendM3Transaction {
	private final MIAPI mi
	private final ProgramAPI program
	private final DatabaseAPI database
	private final UtilityAPI utility
	private final MICallerAPI miCaller

	public UpdHead(MIAPI mi, DatabaseAPI database, UtilityAPI utility, ProgramAPI program, MICallerAPI miCaller) {
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
		String  tx40 = (mi.inData.get("TX40") == null) ? "" : mi.inData.get("TX40").trim()
		String  tx15 = (mi.inData.get("TX15") == null) ? "" : mi.inData.get("TX15").trim()
		Integer aetp = mi.in.get("AETP")
		String  rdri = (mi.inData.get("RDRI") == null) ? "" : mi.inData.get("RDRI").trim()
		String  rduv = (mi.inData.get("RDUV") == null) ? "" : mi.inData.get("RDUV").trim()
		Integer rdut = mi.in.get("RDUT")
		String  rdrv = (mi.inData.get("RDRV") == null) ? "" : mi.inData.get("RDRV").trim()
		Integer rdrt = mi.in.get("RDRT")
		Integer rdsr = mi.in.get("RDSR")



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


		if(getParm(divi) == 0) {
			mi.error("Le paramètre 210 du CAS900 doit être activé pour utilisé l'API")
			return
		}
		
		DBAction fgdibhRecord = database.table("FGDIBH").index("00").selection("BXCHNO").build()
		DBContainer fgdibhContainer = fgdibhRecord.createContainer()
		fgdibhContainer.setInt("BXCONO", cono)
		fgdibhContainer.setString("BXDIVI", divi)
		fgdibhContainer.setString("BXBTAB", btab)
		
		boolean updatable = fgdibhRecord.readLock(fgdibhContainer, { LockedResult updateRecord ->
			updateRecord.setString("BXTX40", tx40)
			updateRecord.setString("BXTX15", tx15)
			updateRecord.set("BXAETP", aetp)
			updateRecord.setString("BXRDRI", rdri)
			updateRecord.setString("BXRDUV", rduv)
			updateRecord.set("BXRDUT", rdut)
			updateRecord.setString("BXRDRV", rdrv)
			updateRecord.set("BXRDRT", rdrt)
			updateRecord.set("BXRDSR", rdsr)
			updateRecord.set("BXRGDT", (Integer) utility.call("DateUtil", "currentDateY8AsInt"))
			updateRecord.set("BXLMDT", (Integer) utility.call("DateUtil", "currentDateY8AsInt"))
			updateRecord.set("BXCHID", program.getUser())
			updateRecord.set("BXRGTM", (Integer) utility.call("DateUtil", "currentTimeAsInt"))
			updateRecord.set("BXCHNO", 1)
			int CHNO = updateRecord.getInt("BXCHNO")
			if(CHNO== 999) {CHNO = 0}
			CHNO++
			updateRecord.set("BXLMDT", (Integer) utility.call("DateUtil", "currentDateY8AsInt"))
			updateRecord.set("BXCHID", program.getUser())
			updateRecord.setInt("BXCHNO", CHNO)
			updateRecord.update()
		})

		if(!updatable)
		{
			mi.error("L'enregistrement n'existe pas.")
			return
		}
	}


	/**
	 /**
	 * return parm 210 from CAS900
	 * @param cono
	 * @param bjno
	 * @return true if no error.
	 */
	private int getParm(String divi) {
		int result = 0
		String parm = ""
		Map<String, String> crs175MIParameters =  [DIVI:divi,STCO:"CAS900"]
		miCaller.call("CRS175MI", "GetGeneralParm", crs175MIParameters , { Map<String, String> response ->
			if(response.containsKey("error")) {
				mi.error(response.errorMessage)
				return result
			}
			parm = response.PARM
		});


		return Character.getNumericValue(parm.charAt(88))
	}
}

