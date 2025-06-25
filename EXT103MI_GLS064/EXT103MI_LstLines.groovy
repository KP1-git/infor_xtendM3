/**
 * README
 *
 * Name: EXT103MI.LstLines
 * Description: List records in FGDISD
 * Date                         Changed By                    Description
 * 20250623                     a.ferre@hetic3.fr     		création
 */
public class LstLines extends ExtendM3Transaction {
	private final MIAPI mi
	private final ProgramAPI program
	private final DatabaseAPI database
	private final UtilityAPI utility
	private final MICallerAPI miCaller

	public LstLines(MIAPI mi, DatabaseAPI database, UtilityAPI utility, ProgramAPI program, MICallerAPI miCaller) {
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
		
		DBAction fgdisdRecord = database.table("FGDISD").index("00").selectAllFields().build()
		DBContainer fgdisdContainer = fgdisdRecord.createContainer()
		fgdisdContainer.setInt("BGCONO", cono)
		fgdisdContainer.setString("BGDIVI", divi)
		fgdisdContainer.setString("BGSTAB", stab)
		
		int nrOfRecords = mi.getMaxRecords() <= 0 || mi.getMaxRecords() >= 10000? 10000: mi.getMaxRecords()
		fgdisdRecord.readAll(fgdisdContainer, 2, nrOfRecords,{ DBContainer container ->
			mi.getOutData().put("CONO", cono.toString())
			mi.getOutData().put("DIVI", divi)
			mi.getOutData().put("STAB", container.getString("BGSTAB"))
			mi.getOutData().put("TX40", container.getString("BGTX40"))
			mi.getOutData().put("SFA1", container.getString("BGSFA1"))
			mi.getOutData().put("SFA2", container.getString("BGSFA2"))
			mi.getOutData().put("SFA3", container.getString("BGSFA3"))
			mi.getOutData().put("SFA4", container.getString("BGSFA4"))
			mi.getOutData().put("SFA5", container.getString("BGSFA5"))
			mi.getOutData().put("SFA6", container.getString("BGSFA6"))
			mi.getOutData().put("SFA7", container.getString("BGSFA7"))
			mi.getOutData().put("STA1", container.getString("BGSTA1"))
			mi.getOutData().put("STA2", container.getString("BGSTA2"))
			mi.getOutData().put("STA3", container.getString("BGSTA3"))
			mi.getOutData().put("STA4", container.getString("BGSTA4"))
			mi.getOutData().put("STA5", container.getString("BGSTA5"))
			mi.getOutData().put("STA6", container.getString("BGSTA6"))
			mi.getOutData().put("STA7", container.getString("BGSTA7"))
			mi.getOutData().put("OBF1", container.getString("BGOBF1"))
			mi.getOutData().put("OBF2", container.getString("BGOBF2"))
			mi.getOutData().put("OBF3", container.getString("BGOBF3"))
			mi.getOutData().put("OBT1", container.getString("BGOBT1"))
			mi.getOutData().put("OBT2", container.getString("BGOBT2"))
			mi.getOutData().put("OBT3", container.getString("BGOBT3"))
			mi.getOutData().put("TXID", container.get("BGTXID").toString())
			mi.getOutData().put("RGDT", container.get("BGRGDT").toString())
			mi.getOutData().put("RGTM", container.get("BGRGTM").toString())
			mi.getOutData().put("LMDT", container.get("BGLMDT").toString())
			mi.getOutData().put("CHNO", container.get("BGCHNO").toString())
			mi.getOutData().put("CHID", container.getString("BGCHID") )
			

			
			mi.write()
		})
		

	}
}