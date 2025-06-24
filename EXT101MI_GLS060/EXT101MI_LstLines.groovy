/**
 * README
 *
 * Name: EXT101MI.LstLines
 * Description: List records in FGDIBD
 * Date                         Changed By                    Description
 * 20250623                     a.ferre@hetic3.fr     		création
 */
public class LstLines extends ExtendM3Transaction {
	private final MIAPI mi
	private final ProgramAPI program
	private final DatabaseAPI database
	private final UtilityAPI utility
	private final MICallerAPI miCaller
	
	public LstLines(MIAPI mi, DatabaseAPI database, UtilityAPI utility, ProgramAPI program, MICallerAPI miCaller) {
		this.mi = mi
		this.program = program
		this.database = database
		this.utility = utility
		this.miCaller = miCaller
	}
	
	public void main() {
		Integer cono = mi.in.get("CONO")
		String  divi = (mi.inData.get("DIVI") == null) ? "" : mi.inData.get("DIVI").trim()
		String  btab = (mi.inData.get("BTAB") == null) ? "" : mi.inData.get("BTAB").trim()
		
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

		if(btab.isBlank()) {
			mi.error("Table de base d'affectation est obligatoire")
			return
		}
		
		DBAction fgdibdRecord = database.table("FGDIBD").index("00").selectAllFields().build()
		DBContainer fgdibdContainer = fgdibdRecord.createContainer()
		fgdibdContainer.setInt("BYCONO", cono)
		fgdibdContainer.setString("BYDIVI", divi)
		fgdibdContainer.setString("BYBTAB", btab)
		
		int nrOfRecords = mi.getMaxRecords() <= 0 || mi.getMaxRecords() >= 10000? 10000: mi.getMaxRecords()
		fgdibdRecord.readAll(fgdibdContainer, 2, nrOfRecords,{ DBContainer container ->
			mi.getOutData().put("CONO", cono.toString())
			mi.getOutData().put("DIVI", divi)
			mi.getOutData().put("BTAB", container.getString("BYBTAB"))
			mi.getOutData().put("BBLN", container.get("BYBBLN").toString())
			mi.getOutData().put("TX40", container.getString("BYTX40"))
			mi.getOutData().put("BFA1", container.getString("BYBFA1"))
			mi.getOutData().put("BFA2", container.getString("BYBFA2"))
			mi.getOutData().put("BFA3", container.getString("BYBFA3"))
			mi.getOutData().put("BFA4", container.getString("BYBFA4"))
			mi.getOutData().put("BFA5", container.getString("BYBFA5"))
			mi.getOutData().put("BFA6", container.getString("BYBFA6"))
			mi.getOutData().put("BFA7", container.getString("BYBFA7"))
			mi.getOutData().put("BTA1", container.getString("BYBTA1"))
			mi.getOutData().put("BTA2", container.getString("BYBTA2"))
			mi.getOutData().put("BTA3", container.getString("BYBTA3"))
			mi.getOutData().put("BTA4", container.getString("BYBTA4"))
			mi.getOutData().put("BTA5", container.getString("BYBTA5"))
			mi.getOutData().put("BTA6", container.getString("BYBTA6"))
			mi.getOutData().put("BTA7", container.getString("BYBTA7"))
			mi.getOutData().put("BTA7", container.getString("BYBTA7"))
			mi.getOutData().put("OBF1", container.getString("BYOBF1"))
			mi.getOutData().put("OBF2", container.getString("BYOBF2"))
			mi.getOutData().put("OBF3", container.getString("BYOBF3"))
			mi.getOutData().put("OBT1", container.getString("BYOBT1"))
			mi.getOutData().put("OBT2", container.getString("BYOBT2"))
			mi.getOutData().put("OBT3", container.getString("BYOBT3"))
			mi.getOutData().put("TXID", container.get("BYTXID").toString())
			mi.getOutData().put("RGDT", container.get("BYRGDT").toString())
			mi.getOutData().put("RGTM", container.get("BYRGTM").toString())
			mi.getOutData().put("LMDT", container.get("BYLMDT").toString())
			mi.getOutData().put("CHNO", container.get("BYCHNO").toString())
			mi.getOutData().put("CHID", container.getString("BYCHID") )

			
			mi.write()
		})
		
	}
  }
