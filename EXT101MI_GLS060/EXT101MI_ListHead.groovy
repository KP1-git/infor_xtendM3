/**
 * README
 *
 * Name: EXT101MI.LstHead
 * Description: List records in FGDIBH
 * Date                         Changed By                    Description
 * 20250620                     a.ferre@hetic3.fr     		création
 */
public class LstHead extends ExtendM3Transaction {
	private final MIAPI mi
	private final ProgramAPI program
	private final DatabaseAPI database
	private final UtilityAPI utility
	private final MICallerAPI miCaller

	public LstHead(MIAPI mi, DatabaseAPI database, UtilityAPI utility, ProgramAPI program, MICallerAPI miCaller) {
		this.mi = mi
		this.program = program
		this.database = database
		this.utility = utility
		this.miCaller = miCaller

	}
	
	public void main() {
		Integer cono = mi.in.get("CONO")
		String  divi = (mi.inData.get("DIVI") == null) ? "" : mi.inData.get("DIVI").trim()

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
		
		DBAction fgdibhRecord = database.table("FGDIBH").index("00").selectAllFields().build()
		DBContainer fgdibhContainer = fgdibhRecord.createContainer()
		fgdibhContainer.setInt("BXCONO", cono)
		fgdibhContainer.setString("BXDIVI", divi)
		
		int nrOfRecords = mi.getMaxRecords() <= 0 || mi.getMaxRecords() >= 10000? 10000: mi.getMaxRecords()
		fgdibhRecord.readAll(fgdibhContainer, 2, nrOfRecords,{ DBContainer container ->
			mi.getOutData().put("CONO", cono.toString())
			mi.getOutData().put("DIVI", divi)
			mi.getOutData().put("BTAB", container.getString("BXBTAB"))
			mi.getOutData().put("TX40", container.getString("BXTX40"))
			mi.getOutData().put("TX15", container.getString("BXTX15"))
			mi.getOutData().put("AETP", container.get("BXAETP").toString())
			mi.getOutData().put("RDRI", container.getString("BXRDRI"))
			mi.getOutData().put("RDUV", container.getString("BXRDUV"))
			mi.getOutData().put("RDUT", container.get("BXRDUT").toString())
			mi.getOutData().put("RDRV", container.getString("BXRDRV"))
			mi.getOutData().put("RDRT", container.get("BXRDRT").toString())
			mi.getOutData().put("RDSR", container.get("BXRDSR").toString())
			mi.getOutData().put("TXID", container.get("BXTXID").toString())
			mi.getOutData().put("RGDT", container.get("BXRGDT").toString())
			mi.getOutData().put("RGTM", container.get("BXRGTM").toString())
			mi.getOutData().put("LMDT", container.get("BXLMDT").toString())
			mi.getOutData().put("CHNO", container.get("BXCHNO").toString())
			mi.getOutData().put("CHID", container.getString("BXCHID") )

			
			mi.write()
		})
	}
}