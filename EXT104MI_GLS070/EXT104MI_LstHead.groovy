/**
 * README
 *
 * Name: EXT104MI.LstHead
 * Description: List records from FGDIST
 * Date                         Changed By                    Description
 * 20250625                     a.ferre@hetic3.fr     		création
 */public class LstHead extends ExtendM3Transaction {
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
		
		DBAction fgdistRecord = database.table("FGDIST").index("00").selectAllFields().build()
		DBContainer fgdistContainer = fgdistRecord.createContainer()
		fgdistContainer.setInt("BUCONO", cono)
		fgdistContainer.setString("BUDIVI", divi)
		int nrOfRecords = mi.getMaxRecords() <= 0 || mi.getMaxRecords() >= 10000? 10000: mi.getMaxRecords()
		fgdistRecord.readAll(fgdistContainer, 2, nrOfRecords,{ DBContainer container ->
			mi.getOutData().put("CONO", cono.toString())
			mi.getOutData().put("DIVI", divi)
			mi.getOutData().put("DTMP", container.getString("BUDTMP"))
			mi.getOutData().put("DSPR", container.get("BUDSPR").toString())
			mi.getOutData().put("TX40", container.getString("BUTX40"))
			mi.getOutData().put("TX15", container.getString("BUTX15"))
			mi.getOutData().put("ATTP", container.get("BUATTP").toString())
			mi.getOutData().put("RGDT", container.get("BURGDT").toString())
			mi.getOutData().put("RGTM", container.get("BURGTM").toString())
			mi.getOutData().put("LMDT", container.get("BULMDT").toString())
			mi.getOutData().put("CHNO", container.get("BUCHNO").toString())
			mi.getOutData().put("CHID", container.getString("BUCHID") )

			mi.write()
		})

	}
}
