/**
 * README
 *
 * Name: EXT103MI.LstHead
 * Description: List records in FGDISH
 * Date                         Changed By                    Description
 * 20250624                     a.ferre@hetic3.fr     		création
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
		
		DBAction fgdishRecord = database.table("FGDISH").index("00").selectAllFields().build()
		DBContainer fgdishContainer = fgdishRecord.createContainer()
		fgdishContainer.setInt("BFCONO", cono)
		fgdishContainer.setString("BFDIVI", divi)
		
		int nrOfRecords = mi.getMaxRecords() <= 0 || mi.getMaxRecords() >= 10000? 10000: mi.getMaxRecords()
		fgdishRecord.readAll(fgdishContainer, 2, nrOfRecords,{ DBContainer container ->
			mi.getOutData().put("CONO", cono.toString())
			mi.getOutData().put("DIVI", divi)
			mi.getOutData().put("STAB", container.getString("BFSTAB"))
			mi.getOutData().put("TX40", container.getString("BFTX40"))
			mi.getOutData().put("TX15", container.getString("BFTX15"))
			mi.getOutData().put("AIH1", container.get("BFAIH1").toString())
			mi.getOutData().put("AIH2", container.get("BFAIH2").toString())
			mi.getOutData().put("AIH3", container.get("BFAIH3").toString())
			mi.getOutData().put("AIH4", container.get("BFAIH4").toString())
			mi.getOutData().put("AIH5", container.get("BFAIH5").toString())
			mi.getOutData().put("AIH6", container.get("BFAIH6").toString())
			mi.getOutData().put("AIH7", container.get("BFAIH7").toString())
			mi.getOutData().put("RDRI", container.getString("BFRDRI"))
			mi.getOutData().put("TXID", container.get("BFTXID").toString())
			mi.getOutData().put("RGDT", container.get("BFRGDT").toString())
			mi.getOutData().put("RGTM", container.get("BFRGTM").toString())
			mi.getOutData().put("LMDT", container.get("BFLMDT").toString())
			mi.getOutData().put("CHNO", container.get("BFCHNO").toString())
			mi.getOutData().put("CHID", container.getString("BFCHID") )

			
			mi.write()
		})
		

	}
}