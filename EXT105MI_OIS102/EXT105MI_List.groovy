/**
 * README
 *
 * Name: EXT105MI.List
 * Description: List addresses for customer order
 * Date                         Changed By                    Description
 * 20250808                     d.decosterd@hetic3.fr     		création
 */
public class List extends ExtendM3Transaction {
	private final MIAPI mi
	private final DatabaseAPI database
	private final MICallerAPI miCaller

	public List(MIAPI mi, DatabaseAPI database, MICallerAPI miCaller) {
		this.mi = mi
		this.database = database
		this.miCaller = miCaller
	}

	public void main() {
		Integer cono = mi.in.get("CONO")
		String  orno = (mi.inData.get("ORNO") == null) ? "" : mi.inData.get("ORNO").trim()

		if(cono == null) {
			mi.error("La CONO est obligatoire.")
			return
		}

		if(orno.isBlank()) {
			mi.error("Le numéro de commande est obligatoire.")
			return
		}

		DBAction ooheadRecord = database.table("OOHEAD").index("00").build()
		DBContainer ooheadContainer = ooheadRecord.createContainer()
		ooheadContainer.setInt("OACONO", cono)
		ooheadContainer.setString("OAORNO", orno)

		if(!ooheadRecord.read(ooheadContainer)) {
			mi.error("Le numéro de commande n'existe pas.")
			return
		}

		for (adrt in [1,3,6]) {
			writeLine(cono, orno, adrt)
		}
	}

	/**
	 * Call EXT105MI.Get and write the resulted line
	 * @param cono The Compagny
	 * @param orno The order
	 * @param adrt The address type
	 */
	private void writeLine(int cono, String orno, int adrt) {
		Map<String, String> ext105MIParameters =  [CONO:cono.toString(),ORNO:orno,ADRT:adrt.toString()]
		miCaller.call("EXT105MI", "Get", ext105MIParameters , { Map<String, String> response ->
			if(!response.containsKey("error")) {
				mi.getOutData().put("CONO", response.get("CONO"))
				mi.getOutData().put("ORNO", response.get("ORNO"))
				mi.getOutData().put("ADRT", response.get("ADRT"))
				mi.getOutData().put("ADID", response.get("ADID"))
				mi.getOutData().put("RODN", response.get("RODN"))
				mi.getOutData().put("CUNM", response.get("CUNM"))
				mi.getOutData().put("CUA1", response.get("CUA1"))
				mi.getOutData().put("CUA2", response.get("CUA2"))
				mi.getOutData().put("CUA3", response.get("CUA3"))
				mi.getOutData().put("CUA4", response.get("CUA4"))
				mi.getOutData().put("PONO", response.get("PONO"))
				mi.getOutData().put("TOWN", response.get("TOWN"))
				mi.getOutData().put("FRCO", response.get("FRCO"))
				mi.getOutData().put("PHNO", response.get("PHNO"))
				mi.getOutData().put("TFNO", response.get("TFNO"))
				mi.getOutData().put("YREF", response.get("YREF"))
				mi.getOutData().put("CSCD", response.get("CSCD"))
				mi.getOutData().put("VRNO", response.get("VRNO"))
				mi.getOutData().put("EDES", response.get("EDES"))
				mi.getOutData().put("ROUT", response.get("ROUT"))
				mi.getOutData().put("ULZO", response.get("ULZO"))
				mi.getOutData().put("ECAR", response.get("ECAR"))
				mi.getOutData().put("HAFE", response.get("HAFE"))
				mi.getOutData().put("RASN", response.get("RASN"))
				mi.getOutData().put("DLSP", response.get("DLSP"))
				mi.getOutData().put("ADVI", response.get("ADVI"))
				mi.getOutData().put("DECU", response.get("DECU"))
				mi.getOutData().put("SPLE", response.get("SPLE"))
				mi.getOutData().put("DSTX", response.get("DSTX"))
				mi.write()
			}
		})
	}
}