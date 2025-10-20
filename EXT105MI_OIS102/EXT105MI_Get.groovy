/**
 * README
 *
 * Name: EXT105MI.Get
 * Description: Get address for customer order
 * Date                         Changed By                    Description
 * 20250806                     d.decosterd@hetic3.fr     		création
 */
public class Get extends ExtendM3Transaction {
	private final MIAPI mi
	private final DatabaseAPI database
	private final LoggerAPI logger

	public Get(MIAPI mi, DatabaseAPI database, LoggerAPI logger) {
		this.mi = mi
		this.database = database
		this.logger = logger
	}

	public void main() {
		Integer cono = mi.in.get("CONO")
		String  orno = (mi.inData.get("ORNO") == null) ? "" : mi.inData.get("ORNO").trim()
		Integer adrt = mi.in.get("ADRT")

		if(cono == null) {
			mi.error("La CONO est obligatoire.")
			return
		}

		if(orno.isBlank()) {
			mi.error("Le numéro de commande est obligatoire.")
			return
		}

		DBAction ooheadRecord = database.table("OOHEAD").index("00").selection("OAINRC","OAADID","OADECU","OADIVI","OAMODL","OATEDL","OATEL2","OADLSP","OADSTX","OAYREF","OAROUT","OARODN","OACUNO").build()
		DBContainer ooheadContainer = ooheadRecord.createContainer()
		ooheadContainer.setInt("OACONO", cono)
		ooheadContainer.setString("OAORNO", orno)
		if(!ooheadRecord.read(ooheadContainer)) {
			mi.error("Le numéro de commande n'existe pas.")
			return
		}

		if(adrt == null) {
			mi.error("Le type d'adresse est obligatoire.")
			return
		}

		if(![1,3,6].contains(adrt)) {
			mi.error("Le type d'adresse ne peux avoir comme valeur 1,3 ou 6.")
			return
		}

		DBAction ooadreRecord = database.table("OOADRE").index("00").selection("ODADID","ODRODN","ODCUNM","ODCUA1","ODCUA2","ODCUA3","ODCUA4","ODPONO","ODTOWN","ODFRCO","ODPHNO","ODTFNO","ODYREF","ODCSCD","ODVRNO","ODEDES","ODROUT","ODULZO","ODECAR","ODHAFE","ODRASN","ODDLSP","ODADVI","ODSPLE","ODDSTX","ODMEAL","ODMODL","ODTEDL","ODGEOC","ODTAXC").build()
		DBContainer ooadreContainer = ooadreRecord.createContainer()
		ooadreContainer.setInt("ODCONO", cono)
		ooadreContainer.setString("ODORNO", orno)
		ooadreContainer.setInt("ODADRT", adrt)
		ooadreContainer.setString("ODADID", ooheadContainer.getString("OAADID"))

		boolean foundLevel1 = false

		if(adrt == 1) {
			foundLevel1 = ooadreRecord.read(ooadreContainer)
			if(foundLevel1) {
				completeWithOcusma(ooadreContainer, cono, ooheadContainer.getString("OADECU"))
			}
		}else {
			int read = ooadreRecord.readAll(ooadreContainer, 3, 1, { DBContainer container ->
				ooadreContainer = container.createCopy()
				completeWithOcusma(ooadreContainer, cono, ooheadContainer.getString("OADECU"))
			})
			foundLevel1 = read == 1
		}
		
		if(!foundLevel1) {
			mi.error("L'enregistrement n'existe pas.")
			return
		}

		if(adrt == 1) {
			ooadreContainer.setString("ODMODL", ooheadContainer.getString("OAMODL"))
			ooadreContainer.setString("ODTEDL", ooheadContainer.getString("OATEDL"))
			ooadreContainer.setString("ODTEL2", ooheadContainer.getString("OATEL2"))
			ooadreContainer.setString("ODDLSP", ooheadContainer.getString("OADLSP"))
			ooadreContainer.setString("ODDSTX", ooheadContainer.getString("OADSTX"))
			ooadreContainer.setString("ODYREF", ooheadContainer.getString("OAYREF"))
			ooadreContainer.setString("ODROUT", ooheadContainer.getString("OAROUT"))
			ooadreContainer.setInt("ODRODN", ooheadContainer.getInt("OARODN"))
		}
		mi.getOutData().put("CONO", ooadreContainer.getInt("ODCONO").toString())
		mi.getOutData().put("ORNO", ooadreContainer.getString("ODORNO"))
		mi.getOutData().put("ADRT", ooadreContainer.getInt("ODADRT").toString())
		mi.getOutData().put("ADID", ooadreContainer.getString("ODADID"))

		mi.getOutData().put("RODN", ooadreContainer.getInt("ODRODN").toString())
		mi.getOutData().put("CUNM", ooadreContainer.getString("ODCUNM"))
		mi.getOutData().put("CUA1", ooadreContainer.getString("ODCUA1"))
		mi.getOutData().put("CUA2", ooadreContainer.getString("ODCUA2"))
		mi.getOutData().put("CUA3", ooadreContainer.getString("ODCUA3"))
		mi.getOutData().put("CUA4", ooadreContainer.getString("ODCUA4"))
		mi.getOutData().put("PONO", ooadreContainer.getString("ODPONO"))
		mi.getOutData().put("TOWN", ooadreContainer.getString("ODTOWN"))
		mi.getOutData().put("FRCO", ooadreContainer.getString("ODFRCO"))
		mi.getOutData().put("PHNO", ooadreContainer.getString("ODPHNO"))
		mi.getOutData().put("TFNO", ooadreContainer.getString("ODTFNO"))
		mi.getOutData().put("YREF", ooadreContainer.getString("ODYREF"))
		mi.getOutData().put("CSCD", ooadreContainer.getString("ODCSCD"))
		mi.getOutData().put("VRNO", ooadreContainer.getString("ODVRNO"))
		mi.getOutData().put("EDES", ooadreContainer.getString("ODEDES"))
		mi.getOutData().put("ROUT", ooadreContainer.getString("ODROUT"))
		mi.getOutData().put("ULZO", ooadreContainer.getString("ODULZO"))
		mi.getOutData().put("ECAR", ooadreContainer.getString("ODECAR"))
		mi.getOutData().put("HAFE", ooadreContainer.getString("ODHAFE"))
		mi.getOutData().put("RASN", ooadreContainer.getString("ODRASN"))
		mi.getOutData().put("DLSP", ooadreContainer.getString("ODDLSP"))
		mi.getOutData().put("ADVI", ooadreContainer.getString("ODADVI"))
		mi.getOutData().put("DECU", ooheadContainer.getString("OADECU"))
		mi.getOutData().put("SPLE", ooadreContainer.getString("ODSPLE"))

		if(!ooadreContainer.getString("ODDSTX").isBlank())
			mi.getOutData().put("DSTX", ooadreContainer.getString("ODDSTX"))
		else if( !ooadreContainer.getString("ODDLSP").isBlank()){
			DBAction ocusasRecord = database.table("OCUSAS").index("00").selection("O2DSTX").build()
			DBContainer ocusasContainer = ocusasRecord.createContainer()
			ocusasContainer.setInt("O2CONO", cono)
			ocusasContainer.setString("O2CUNO", ooheadContainer.getString("OADECU"))
			ocusasContainer.setString("O2ADID", ooadreContainer.getString("ODADID"))
			ocusasContainer.setString("O2DLSP", ooadreContainer.getString("ODDLSP"))
			if(ocusasRecord.read(ocusasContainer)) {
				mi.getOutData().put("DSTX", ooadreContainer.getString("O2DSTX"))
			}
		}


		mi.write()
	}

	/**
	 * Complete missing value with values from OCUSMA	
	 * @param ooadreContainer The DBContainer with the partial values
	 * @param cono The used cono
	 * @param cuno The used cuno
	 * @return
	 */
	private completeWithOcusma(DBContainer ooadreContainer, int cono, String cuno) {
		DBAction ocusmaRecord = database.table("OCUSMA").index("00").selection("OKPHNO","OKTFNO","OKYREF","OKMEAL","OKEDES","OKROUT","OKRODN","OKULZO","OKHAFE","OKRASN","OKFRCO","OKSPLE","OKMODL","OKTEDL","OKTOWN","OKCSCD","OKGEOC","OKTAXC").build()
		DBContainer ocusmaContainer = ocusmaRecord.createContainer()
		ocusmaContainer.setInt("OKCONO", cono)
		ocusmaContainer.setString("OKCUNO", cuno)
		if(ocusmaRecord.read(ocusmaContainer)) {
			if(ooadreContainer.getString("ODPHNO").isBlank())
				ooadreContainer.setString("ODPHNO", ocusmaContainer.getString("OKPHNO"))
			if(ooadreContainer.getString("ODTFNO").isBlank())
				ooadreContainer.setString("ODTFNO", ocusmaContainer.getString("OKTFNO"))
			if(ooadreContainer.getString("ODYREF").isBlank())
				ooadreContainer.setString("ODYREF", ocusmaContainer.getString("OKYREF"))
			if(ooadreContainer.getString("ODMEAL").isBlank())
				ooadreContainer.setString("ODMEAL", ocusmaContainer.getString("OKMEAL"))
			if(ooadreContainer.getString("ODEDES").isBlank())
				ooadreContainer.setString("ODEDES", ocusmaContainer.getString("OKEDES"))
			if(ooadreContainer.getString("ODROUT").isBlank()) {
				ooadreContainer.setString("ODROUT", ocusmaContainer.getString("OKROUT"))
				ooadreContainer.setInt("ODRODN", ocusmaContainer.getInt("OKRODN"))
			}
			if(ooadreContainer.getString("ODULZO").isBlank())
				ooadreContainer.setString("ODULZO", ocusmaContainer.getString("OKULZO"))
			if(ooadreContainer.getString("ODHAFE").isBlank())
				ooadreContainer.setString("ODHAFE", ocusmaContainer.getString("OKHAFE"))
			if(ooadreContainer.getString("ODRASN").isBlank())
				ooadreContainer.setString("ODRASN", ocusmaContainer.getString("OKRASN"))
			if(ooadreContainer.getString("ODFRCO").isBlank())
				ooadreContainer.setString("ODFRCO", ocusmaContainer.getString("OKFRCO"))
			if(ooadreContainer.getString("ODSPLE").isBlank())
				ooadreContainer.setString("ODSPLE", ocusmaContainer.getString("OKSPLE"))
			if(ooadreContainer.getString("ODMODL").isBlank())
				ooadreContainer.setString("ODMODL", ocusmaContainer.getString("OKMODL"))
			if(ooadreContainer.getString("ODTEDL").isBlank())
				ooadreContainer.setString("ODTEDL", ocusmaContainer.getString("OKTEDL"))
			if(ooadreContainer.getString("ODTOWN").isBlank())
				ooadreContainer.setString("ODTOWN", ocusmaContainer.getString("OKTOWN"))
			if(ooadreContainer.getString("ODCSCD").isBlank())
				ooadreContainer.setString("ODCSCD", ocusmaContainer.getString("OKCSCD"))
			if(ooadreContainer.getInt("ODGEOC") == 0)
				ooadreContainer.setInt("ODGEOC", ocusmaContainer.getInt("OKGEOC"))
			if(ooadreContainer.getString("ODTAXC").isBlank())
				ooadreContainer.setString("ODTAXC", ocusmaContainer.getString("OKTAXC"))
		}
	}

	/**
	 * Get address from OCUSAD
	 * @param ooadreContainer The DBContainer to replace value
	 * @param cono The cono value
	 * @param cuno The cuno value
	 * @return
	 */
	private useValuesFromOcusad(DBContainer ooadreContainer, int cono, String cuno) {
		DBAction ocusadRecord = database.table("OCUSAD").index("00").selection("OPCUNM","OPCUA1","OPCUA2","OPCUA3","OPCUA4","OPPONO","OPPHNO","OPTFNO","OPYREF","OPMEAL","OPCSCD","OPVRNO","OPEDES","OPROUT","OPRODN","OPULZO","OPECAR","OPHAFE","OPRASN","OPFRCO","OPSPLE","OPMODL","OPTEDL","OPTOWN","OPADVI","OPGEOC","OPTAXC").build()
		DBContainer ocusadContainer = ocusadRecord.createContainer()
		ocusadContainer.setInt("OPCONO", cono)
		ocusadContainer.setString("OPCUNO",cuno)
		ocusadContainer.setInt("OPADRT", ooadreContainer.getInt("ODADRT"))
		ocusadContainer.setString("OPADID",ooadreContainer.getString("ODADID"))
		if(ocusadRecord.read(ocusadContainer)) {
			ooadreContainer.setString("ODCUNM", ocusadContainer.getString("OPCUNM"))
			ooadreContainer.setString("ODCUA1", ocusadContainer.getString("OPCUA1"))
			ooadreContainer.setString("ODCUA2", ocusadContainer.getString("OPCUA2"))
			ooadreContainer.setString("ODCUA3", ocusadContainer.getString("OPCUA3"))
			ooadreContainer.setString("ODCUA4", ocusadContainer.getString("OPCUA4"))
			ooadreContainer.setString("ODPONO", ocusadContainer.getString("OPPONO"))
			ooadreContainer.setString("ODPHNO", ocusadContainer.getString("OPPHNO"))
			ooadreContainer.setString("ODTFNO", ocusadContainer.getString("OPTFNO"))
			ooadreContainer.setString("ODYREF", ocusadContainer.getString("OPYREF"))
			ooadreContainer.setString("ODMEAL", ocusadContainer.getString("OPMEAL"))
			ooadreContainer.setString("ODCSCD", ocusadContainer.getString("OPCSCD"))
			ooadreContainer.setString("ODVRNO", ocusadContainer.getString("OPVRNO"))
			ooadreContainer.setString("ODEDES", ocusadContainer.getString("OPEDES"))
			ooadreContainer.setString("ODROUT", ocusadContainer.getString("OPROUT"))
			ooadreContainer.setInt("ODRODN", ocusadContainer.getInt("OPRODN"))
			ooadreContainer.setString("ODULZO", ocusadContainer.getString("OPULZO"))
			ooadreContainer.setString("ODECAR", ocusadContainer.getString("OPECAR"))
			ooadreContainer.setString("ODHAFE", ocusadContainer.getString("OPHAFE"))
			ooadreContainer.setString("ODRASN", ocusadContainer.getString("OPRASN"))
			ooadreContainer.setString("ODFRCO", ocusadContainer.getString("OPFRCO"))
			ooadreContainer.setString("ODSPLE", ocusadContainer.getString("OPSPLE"))
			ooadreContainer.setString("ODMODL", ocusadContainer.getString("OPMODL"))
			ooadreContainer.setString("ODTEDL", ocusadContainer.getString("OPTEDL"))
			ooadreContainer.setString("ODTOWN", ocusadContainer.getString("OPTOWN"))
			ooadreContainer.setString("ODADVI", ocusadContainer.getString("OPADVI"))
			ooadreContainer.setInt("ODGEOC", ocusadContainer.getInt("OPGEOC"))
			ooadreContainer.setString("ODTAXC", ocusadContainer.getString("OPTAXC"))
		}

	}

	/**
	 * Get address from OCUSMA
	 * @param ooadreContainer The DBContainer to replace value
	 * @param cono The cono value
	 * @param cuno The cuno value
	 * @return
	 */
	private useValuesFromOcusma(DBContainer ooadreContainer, int cono, String cuno) {
		DBAction ocusmaRecord = database.table("OCUSMA").index("00").selection("OKADID","OKCUNM","OKCUA1","OKCUA2","OKCUA3","OKCUA4","OKPONO","OKPHNO","OKTFNO","OKYREF","OKMEAL","OKCSCD","OKVRNO","OKEDES","OKROUT","OKRODN","OKULZO","OKECAR","OKHAFE","OKRASN","OKFRCO","OKSPLE","OKMODL","OKTEDL","OKTOWN","OKADVI","OKGEOC","OKTAXC",).build()
		DBContainer ocusmaContainer = ocusmaRecord.createContainer()
		ocusmaContainer.setInt("OKCONO", cono)
		ocusmaContainer.setString("OKCUNO",cuno )
		if(ocusmaRecord.read(ocusmaContainer)) {
			ooadreContainer.setString("ODADID", ocusmaContainer.getString("OKADID"))
			ooadreContainer.setString("ODCUNM", ocusmaContainer.getString("OKCUNM"))
			ooadreContainer.setString("ODCUA1", ocusmaContainer.getString("OKCUA1"))
			ooadreContainer.setString("ODCUA2", ocusmaContainer.getString("OKCUA2"))
			ooadreContainer.setString("ODCUA3", ocusmaContainer.getString("OKCUA3"))
			ooadreContainer.setString("ODCUA4", ocusmaContainer.getString("OKCUA4"))
			ooadreContainer.setString("ODPONO", ocusmaContainer.getString("OKPONO"))
			ooadreContainer.setString("ODPHNO", ocusmaContainer.getString("OKPHNO"))
			ooadreContainer.setString("ODTFNO", ocusmaContainer.getString("OKTFNO"))
			ooadreContainer.setString("ODYREF", ocusmaContainer.getString("OKYREF"))
			ooadreContainer.setString("ODMEAL", ocusmaContainer.getString("OKMEAL"))
			ooadreContainer.setString("ODCSCD", ocusmaContainer.getString("OKCSCD"))
			ooadreContainer.setString("ODVRNO", ocusmaContainer.getString("OKVRNO"))
			ooadreContainer.setString("ODEDES", ocusmaContainer.getString("OKEDES"))
			ooadreContainer.setString("ODROUT", ocusmaContainer.getString("OKROUT"))
			ooadreContainer.setInt("ODRODN", ocusmaContainer.getInt("OKRODN"))
			ooadreContainer.setString("ODULZO", ocusmaContainer.getString("OKULZO"))
			ooadreContainer.setString("ODECAR", ocusmaContainer.getString("OKECAR"))
			ooadreContainer.setString("ODHAFE", ocusmaContainer.getString("OKHAFE"))
			ooadreContainer.setString("ODRASN", ocusmaContainer.getString("OKRASN"))
			ooadreContainer.setString("ODFRCO", ocusmaContainer.getString("OKFRCO"))
			ooadreContainer.setString("ODSPLE", ocusmaContainer.getString("OKSPLE"))
			ooadreContainer.setString("ODMODL", ocusmaContainer.getString("OKMODL"))
			ooadreContainer.setString("ODTEDL", ocusmaContainer.getString("OKTEDL"))
			ooadreContainer.setString("ODTOWN", ocusmaContainer.getString("OKTOWN"))
			ooadreContainer.setString("ODADVI", ocusmaContainer.getString("OKADVI"))
			ooadreContainer.setInt("ODGEOC", ocusmaContainer.getInt("OKGEOC"))
			ooadreContainer.setString("ODTAXC", ocusmaContainer.getString("OKTAXC"))
		}

	}
}