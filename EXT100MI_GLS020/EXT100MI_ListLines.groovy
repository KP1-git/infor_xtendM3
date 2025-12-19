/**
 * README
 *
 * Name: EXT100MI.LstLines
 * Description: List records from FAACCB
 * Date                         Changed By                    Description
 * 20250616                     a.ferre@hetic3.fr     		création
 */
public class ListLines extends ExtendM3Transaction {
	private final MIAPI mi
	private final ProgramAPI program
	private final DatabaseAPI database
	private final UtilityAPI utility

	public ListLines(MIAPI mi, ProgramAPI program, DatabaseAPI database, UtilityAPI utility) {
		this.mi = mi
		this.program = program
		this.database = database
		this.utility = utility
	}

	public void main() {
		Integer cono = mi.in.get("CONO")
		String  divi = (mi.inData.get("DIVI") == null) ? "" : mi.inData.get("DIVI").trim()
		Integer rcno = mi.in.get("RCNO")
		
		if(cono == null) {
			mi.error("La CONO est obligatoire.")
			return
		}

		if(divi.isBlank()) {
			mi.error("L'api devrait être utilisé localement, DIVI obligatoire.")
			return
		}
		
		if(rcno == null || rcno == 0) {
			mi.error("Le numéro d'enregistrement est obligatoire.")
			return
		}
		
		DBAction faaccbRecord = database.table("FAACCB").index("00").selectAllFields().build()
		DBContainer faaccbContainer = faaccbRecord.createContainer()
		faaccbContainer.setInt("FBCONO", cono)
		faaccbContainer.setString("FBDIVI", divi)
		faaccbContainer.setInt("FBRCNO", rcno)
		int nrOfRecords = 5000
		faaccbRecord.readAll(faaccbContainer, 3, nrOfRecords,{ DBContainer container ->
			mi.getOutData().put("CONO", cono.toString())
			mi.getOutData().put("DIVI", divi)
			mi.getOutData().put("RCNO", container.get("FBRCNO").toString())
			mi.getOutData().put("RGLN", container.get("FBRGLN").toString())
			mi.getOutData().put("TX15", container.getString("FBTX15"))
			mi.getOutData().put("FDIV", container.getString("FBFDIV"))
			mi.getOutData().put("BFA1", container.getString("FBBFA1"))
			mi.getOutData().put("BFA2", container.getString("FBBFA2"))
			mi.getOutData().put("BFA3", container.getString("FBBFA3"))
			mi.getOutData().put("BFA4", container.getString("FBBFA4"))
			mi.getOutData().put("BFA5", container.getString("FBBFA5"))
			mi.getOutData().put("BFA6", container.getString("FBBFA6"))
			mi.getOutData().put("BFA7", container.getString("FBBFA7"))
			mi.getOutData().put("TDIV", container.getString("FBTDIV"))
			mi.getOutData().put("BTA1", container.getString("FBBTA1"))
			mi.getOutData().put("BTA2", container.getString("FBBTA2"))
			mi.getOutData().put("BTA3", container.getString("FBBTA3"))
			mi.getOutData().put("BTA4", container.getString("FBBTA4"))
			mi.getOutData().put("BTA5", container.getString("FBBTA5"))
			mi.getOutData().put("BTA6", container.getString("FBBTA6"))
			mi.getOutData().put("BTA7", container.getString("FBBTA7"))
			mi.getOutData().put("BNA2", container.getString("FBBNA2"))
			mi.getOutData().put("BNA3", container.getString("FBBNA3"))
			mi.getOutData().put("BNA4", container.getString("FBBNA4"))
			mi.getOutData().put("BNA5", container.getString("FBBNA5"))
			mi.getOutData().put("BNA6", container.getString("FBBNA6"))
			mi.getOutData().put("BNA7", container.getString("FBBNA7"))
			mi.getOutData().put("NRC2", container.get("FBNRC2").toString())
			mi.getOutData().put("NRC3", container.get("FBNRC3").toString())
			mi.getOutData().put("NRC4", container.get("FBNRC4").toString())
			mi.getOutData().put("NRC5", container.get("FBNRC5").toString())
			mi.getOutData().put("NRC6", container.get("FBNRC6").toString())
			mi.getOutData().put("NRC7", container.get("FBNRC7").toString())
			
			mi.getOutData().put("RGDT", container.get("FBRGDT").toString())
			mi.getOutData().put("RGTM", container.get("FBRGTM").toString())
			mi.getOutData().put("LMDT", container.get("FBLMDT").toString())
			mi.getOutData().put("CHNO", container.get("FBCHNO").toString())
			mi.getOutData().put("CHID", container.getString("FBCHID") )
			
			mi.write()
		})
	}
}