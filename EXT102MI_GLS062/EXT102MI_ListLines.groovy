/**
 * README
 *
 * Name: EXT102MI.LstLines
 * Description: List records from FGDITD
 * Date                         Changed By                    Description
 * 20250701	                    a.ferre@hetic3.fr     		création
 */
public class ListLines extends ExtendM3Transaction {
	private final MIAPI mi
	private final DatabaseAPI database
	private final ProgramAPI program
	private final UtilityAPI utility
	private final MICallerAPI miCaller
	private final MessageAPI message
	private final LoggerAPI logger

	public ListLines(MIAPI mi, DatabaseAPI database, ProgramAPI program, UtilityAPI utility, MICallerAPI miCaller, MessageAPI message, LoggerAPI logger) {
		this.mi = mi
		this.database = database
		this.program = program
		this.utility = utility
		this.miCaller = miCaller
		this.message = message
		this.logger = logger
	}

	public void main() {
		Integer cono = mi.in.get("CONO")
		String divi = (mi.inData.get("DIVI") == null) ? "" : mi.inData.get("DIVI").trim()
		String ttab = (mi.inData.get("TTAB") == null) ? "" : mi.inData.get("TTAB").trim()
		Integer bbln = mi.in.get("BBLN")
		
		if(cono == null) {
			mi.error("La CONO est obligatoire.")
			return
		}
		
		if(!utility.call("CheckUtil", "checkConoExist", database, cono)) {
			mi.error("La division est inexistante.")
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

		

		if(ttab.isBlank()) {
			mi.error("Table cible reventilation est obligatoire")
			return
		}

		DBAction fgditdRecord = database.table("FGDITD").index("00").selectAllFields().build()
		DBContainer fgditdContainer = fgditdRecord.createContainer()
		fgditdContainer.setInt("BECONO", cono)
		fgditdContainer.setString("BEDIVI", divi)
		fgditdContainer.setString("BETTAB", ttab)
		
		int nrOfRecords = mi.getMaxRecords() <= 0 || mi.getMaxRecords() >= 10000? 10000: mi.getMaxRecords()
		fgditdRecord.readAll(fgditdContainer, 2, nrOfRecords,{ DBContainer container ->
			mi.getOutData().put("CONO", cono.toString())
			mi.getOutData().put("DIVI", divi)
			mi.getOutData().put("TTAB", ttab)
			mi.getOutData().put("BBLN", container.get("BEBBLN").toString())
			mi.getOutData().put("BLTP", container.get("BEBLTP").toString())
			mi.getOutData().put("TX40", container.getString("BETX40"))
			mi.getOutData().put("TFA1", container.getString("BETFA1"))
			mi.getOutData().put("TFA2", container.getString("BETFA2"))
			mi.getOutData().put("TFA3", container.getString("BETFA3"))
			mi.getOutData().put("TFA4", container.getString("BETFA4"))
			mi.getOutData().put("TFA5", container.getString("BETFA5"))
			mi.getOutData().put("TFA6", container.getString("BETFA6"))
			mi.getOutData().put("TFA7", container.getString("BETFA7"))
			mi.getOutData().put("OFDI", container.getString("BEOFDI"))
			mi.getOutData().put("DIPE", container.get("BEDIPE").toString())
			mi.getOutData().put("DIRS", container.get("BEDIRS").toString())
			mi.getOutData().put("STAB", container.getString("BESTAB"))
			mi.getOutData().put("TXID", container.get("BETXID").toString())

			mi.getOutData().put("RGDT", container.get("BERGDT").toString())
			mi.getOutData().put("RGTM", container.get("BERGTM").toString())
			mi.getOutData().put("LMDT", container.get("BELMDT").toString())
			mi.getOutData().put("CHNO", container.get("BECHNO").toString())
			mi.getOutData().put("CHID", container.getString("BECHID") )
			
			mi.write()
		})
		

	}
}