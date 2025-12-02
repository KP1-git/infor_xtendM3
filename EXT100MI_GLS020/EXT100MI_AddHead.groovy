/**
 * README
 *
 * Name: EXT100MI.AddHead
 * Description: Add a record in FAACCH
 * Date                         Changed By                    Description
 * 20250616                     a.ferre@hetic3.fr     		création
 */
public class AddHead extends ExtendM3Transaction {
	private final MIAPI mi
	private final ProgramAPI program
	private final DatabaseAPI database
	private final UtilityAPI utility
	

	
	public AddHead(MIAPI mi, DatabaseAPI database, UtilityAPI utility, ProgramAPI program) {
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
		
		if(!utility.call("CheckUtil", "checkConoExist", database, cono)) {
			mi.error("La company est inexistante.")
			return
		}
		
		if(!utility.call("CheckUtil", "checkDiviExist", database, cono, divi)) {
			mi.error("La division est inexistante.")
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
		
		DBAction faacchRecord = database.table("FAACCH").index("00").build()
		DBContainer faacchContainer = faacchRecord.createContainer()
		faacchContainer.setInt("FHCONO", cono)
		faacchContainer.setString("FHDIVI", divi)
		faacchContainer.setInt("FHRCNO", rcno)
		
		if(!faacchRecord.read(faacchContainer)){
			faacchContainer.setString("FHTX40", tx40)
			faacchContainer.setString("FHTX15", tx15)
			faacchContainer.set("FHRGDT", (Integer) utility.call("DateUtil", "currentDateY8AsInt"))
			faacchContainer.set("FHLMDT", (Integer) utility.call("DateUtil", "currentDateY8AsInt"))
			faacchContainer.set("FHCHID", program.getUser())
			faacchContainer.set("FHRGTM", (Integer) utility.call("DateUtil", "currentTimeAsInt"))
			faacchContainer.set("FHCHNO", 1)
			faacchRecord.insert(faacchContainer)
		}else
		{
			mi.error("Enregistrement existe déjà.")
			return
		}	  
	}
  }
