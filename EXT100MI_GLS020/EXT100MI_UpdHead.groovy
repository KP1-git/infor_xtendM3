/**
 * README
 *
 * Name: EXT100MI.UpdHead
 * Description: Update a record in FAACCH
 * Date                         Changed By                    Description
 * 20250616                     a.ferre@hetic3.fr     		création
 */
public class UpdHead extends ExtendM3Transaction {
	private final MIAPI mi
	private final ProgramAPI program
	private final DatabaseAPI database
	private final UtilityAPI utility
	
	public UpdHead(MIAPI mi, DatabaseAPI database, UtilityAPI utility, ProgramAPI program) {
	  this.mi = mi
	  this.program = program
	  this.database = database
	  this.utility = utility
	}
	
	public void main() {
		Integer cono = mi.in.get("CONO")
		String  divi = (mi.inData.get("DIVI") == null) ? "" : mi.inData.get("DIVI").trim()
		Integer rcno = mi.in.get("RCNO")
		String  tx40 = (mi.inData.get("TX40") == null) ? "" : mi.inData.get("TX40").trim()
		String  tx15 = (mi.inData.get("TX15") == null) ? "" : mi.inData.get("TX15").trim()
		
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
		
		if(tx40.isBlank()) {
			mi.error("Description doit être renseignée.")
			return
		}
		
		if(tx15.isBlank()) {
			tx15 = tx40.take(15)
	   }
		
		DBAction faacchRecord = database.table("FAACCH").index("00").selection("FHCHNO").build()
		DBContainer faacchContainer = faacchRecord.createContainer()
		faacchContainer.setInt("FHCONO", cono)
		faacchContainer.setString("FHDIVI", divi)
		faacchContainer.setInt("FHRCNO", rcno)
		
		boolean updatable = faacchRecord.readLock(faacchContainer, { LockedResult updateRecoord ->
			updateRecoord.setString("FHTX40", tx40)
			updateRecoord.setString("FHTX15", tx15)
			int CHNO = updateRecoord.getInt("FHCHNO")
			if(CHNO== 999) {CHNO = 0}
			CHNO++
			updateRecoord.set("FHLMDT", (Integer) utility.call("DateUtil", "currentDateY8AsInt"))
			updateRecoord.set("FHCHID", program.getUser())
			updateRecoord.setInt("FHCHNO", CHNO)
			updateRecoord.update()
		})

		if(!updatable)
		{
			mi.error("L'enregistrement n'existe pas.")
			return
		}
	}
  }