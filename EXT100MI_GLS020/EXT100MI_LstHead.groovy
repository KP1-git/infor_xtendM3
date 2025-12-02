/**
 * README
 *
 * Name: EXT100MI.LstHead
 * Description: List records in FAACCH
 * Date                         Changed By                    Description
 * 20250616                     a.ferre@hetic3.fr     		création
 */
public class LstHead extends ExtendM3Transaction {

	private final MIAPI mi 
	private final ProgramAPI program 
	private final DatabaseAPI database 
	private final UtilityAPI utility 

	public LstHead(MIAPI mi, ProgramAPI program, DatabaseAPI database, UtilityAPI utility) {
		this.mi = mi 
		this.program = program 
		this.database = database 
		this.utility = utility 
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

		DBAction faacchRecord = database.table("FAACCH").index("00").selectAllFields().build()
		DBContainer faacchContainer = faacchRecord.createContainer()
		faacchContainer.setInt("FHCONO", cono)
		faacchContainer.setString("FHDIVI", divi)

		int nrOfRecords = 5000
		faacchRecord.readAll(faacchContainer, 2, nrOfRecords,{ DBContainer container ->
			mi.getOutData().put("CONO", cono.toString()) 
			mi.getOutData().put("DIVI", divi) 
			mi.getOutData().put("RCNO", container.get("FHRCNO").toString()) 
			mi.getOutData().put("TX40", container.getString("FHTX40")) 
			mi.getOutData().put("TX15", container.getString("FHTX15")) 
			mi.getOutData().put("RGDT", container.get("FHRGDT").toString()) 
			mi.getOutData().put("RGTM", container.get("FHRGTM").toString()) 
			mi.getOutData().put("LMDT", container.get("FHLMDT").toString()) 
			mi.getOutData().put("CHNO", container.get("FHCHNO").toString()) 
			mi.getOutData().put("CHID", container.getString("FHCHID") )
			
			mi.write() 
		}) 

	}
}
