/**
 * README
 *
 * Name: EXT105MI.Del
 * Description: Del address for customer order
 * Date                         Changed By                    Description
 * 20250818                     d.decosterd@hetic3.fr     		création
 */
public class Del extends ExtendM3Transaction {
	private final MIAPI mi
	private final DatabaseAPI database
	private final LoggerAPI logger

	public Del(MIAPI mi, DatabaseAPI database, LoggerAPI logger) {
		this.mi = mi
		this.database = database
		this.logger = logger
	}

	public void main() {
		Integer cono = mi.in.get("CONO")
		String  orno = (mi.inData.get("ORNO") == null) ? "" : mi.inData.get("ORNO").trim()
		Integer adrt = mi.in.get("ADRT")
		String  adid = (mi.inData.get("ADID") == null) ? "" : mi.inData.get("ADID").trim()
		
		if(cono == null) {
			mi.error("La CONO est obligatoire.")
			return
		}

		if(orno.isBlank()) {
			mi.error("Le numéro de commande est obligatoire.")
			return
		}

		if(adrt == null) {
			mi.error("Le type d'adresse est obligatoire.")
			return
		}

		if(![1,3,6].contains(adrt)) {
		 mi.error("Le type d'adresse ne peux avoir comme valeur que 1,3 ou 6.")
		 return
		 }
		
		DBAction ooadreRecord = database.table("OOADRE").index("00").build()
		DBContainer ooadreContainer = ooadreRecord.createContainer()
		ooadreContainer.setInt("ODCONO", cono)
		ooadreContainer.setString("ODORNO", orno)
		ooadreContainer.setInt("ODADRT", adrt)
		ooadreContainer.setString("ODADID", adid)
		boolean deleted = ooadreRecord.readLock(ooadreContainer, { LockedResult delRecoord ->
			delRecoord.delete()
		})

		if(!deleted)
		{
			mi.error("L'enregistrement n'existe pas.")
			return
		}
	}

}