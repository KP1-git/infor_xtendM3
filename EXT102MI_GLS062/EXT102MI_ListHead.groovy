/**
 * README
 *
 * Name: EXT102MI.ListHead
 * Description: list records in FGDITH
 * Date                         Changed By                    Description
 * 20250624                     d.decosterd@hetic3.fr     		création
 */
public class ListHead extends ExtendM3Transaction {
	private final MIAPI mi
	private final DatabaseAPI database
	private final ProgramAPI program
	private final UtilityAPI utility

	public ListHead(MIAPI mi, DatabaseAPI database, ProgramAPI program, UtilityAPI utility) {
		this.mi = mi
		this.database = database
		this.program = program
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

		DBAction fgdithRecord = database.table("FGDITH").index("00").selectAllFields().build()
		DBContainer fgdithContainer = fgdithRecord.createContainer()
		fgdithContainer.setInt("BZCONO", cono)
		fgdithContainer.setString("BZDIVI", divi)
		
		int nrOfRecords = mi.getMaxRecords() <= 0 || mi.getMaxRecords() >= 10000? 10000: mi.getMaxRecords()
		fgdithRecord.readAll(fgdithContainer, 2,nrOfRecords, { DBContainer container ->
			mi.getOutData().put("CONO", cono.toString())
			mi.getOutData().put("DIVI", divi)
			mi.getOutData().put("TTAB", container.getString("BZTTAB"))
			mi.getOutData().put("TX40", container.getString("BZTX40"))
			mi.getOutData().put("TX15", container.getString("BZTX15"))
			mi.getOutData().put("DIMT", container.getInt("BZDIMT").toString())
			mi.getOutData().put("BDTP", container.getInt("BZBDTP").toString())
			mi.getOutData().put("BDPC", container.get("BZBDPC").toString())
			mi.getOutData().put("RGDT", container.getInt("BZRGDT").toString())
			mi.getOutData().put("RGTM", container.getInt("BZRGTM").toString())
			mi.getOutData().put("LMDT", container.getInt("BZLMDT").toString())
			mi.getOutData().put("CHNO", container.getInt("BZCHNO").toString())
			mi.getOutData().put("CHID", container.getString("BZCHID"))
			
			mi.write()
		})
	}
}
