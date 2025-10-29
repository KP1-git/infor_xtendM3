/**
 * README
 *
 * Name: EXT101MI.AddLine
 * Description: Add a record in FGDIBD
 * Date                         Changed By                    Description
 * 20250623                     a.ferre@hetic3.fr     		création.
 */
public class AddLine extends ExtendM3Transaction {
	private final MIAPI mi
	private final ProgramAPI program
	private final DatabaseAPI database
	private final UtilityAPI utility
	private final MICallerAPI miCaller


	public AddLine(MIAPI mi, DatabaseAPI database, UtilityAPI utility, ProgramAPI program, MICallerAPI miCaller) {
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
		Integer bbln = mi.in.get("BBLN")
		String  tx40 = (mi.inData.get("TX40") == null) ? "" : mi.inData.get("TX40").trim()
		String  bfa1 = (mi.inData.get("BFA1") == null) ? "" : mi.inData.get("BFA1").trim()
		String  bfa2 = (mi.inData.get("BFA2") == null) ? "" : mi.inData.get("BFA2").trim()
		String  bfa3 = (mi.inData.get("BFA3") == null) ? "" : mi.inData.get("BFA3").trim()
		String  bfa4 = (mi.inData.get("BFA4") == null) ? "" : mi.inData.get("BFA4").trim()
		String  bfa5 = (mi.inData.get("BFA5") == null) ? "" : mi.inData.get("BFA5").trim()
		String  bfa6 = (mi.inData.get("BFA6") == null) ? "" : mi.inData.get("BFA6").trim()
		String  bfa7 = (mi.inData.get("BFA7") == null) ? "" : mi.inData.get("BFA7").trim()
		String  bta1 = (mi.inData.get("BTA1") == null) ? "" : mi.inData.get("BTA1").trim()
		String  bta2 = (mi.inData.get("BTA2") == null) ? "" : mi.inData.get("BTA2").trim()
		String  bta3 = (mi.inData.get("BTA3") == null) ? "" : mi.inData.get("BTA3").trim()
		String  bta4 = (mi.inData.get("BTA4") == null) ? "" : mi.inData.get("BTA4").trim()
		String  bta5 = (mi.inData.get("BTA5") == null) ? "" : mi.inData.get("BTA5").trim()
		String  bta6 = (mi.inData.get("BTA6") == null) ? "" : mi.inData.get("BTA6").trim()
		String  bta7 = (mi.inData.get("BTA7") == null) ? "" : mi.inData.get("BTA7").trim()
		String  obf1 = (mi.inData.get("OBF1") == null) ? "" : mi.inData.get("OBF1").trim()
		String  obf2 = (mi.inData.get("OBF2") == null) ? "" : mi.inData.get("OBF2").trim()
		String  obf3 = (mi.inData.get("OBF3") == null) ? "" : mi.inData.get("OBF3").trim()
		String  obt1 = (mi.inData.get("OBT1") == null) ? "" : mi.inData.get("OBT1").trim()
		String  obt2 = (mi.inData.get("OBT2") == null) ? "" : mi.inData.get("OBT2").trim()
		String  obt3 = (mi.inData.get("OBT3") == null) ? "" : mi.inData.get("OBT3").trim()

		String  fromRecordbfa1 = ""
		String  fromRecordbfa2 = ""
		String  fromRecordbfa3 = ""
		String  fromRecordbfa4 = ""
		String  fromRecordbfa5 = ""
		String  fromRecordbfa6 = ""
		String  fromRecordbfa7 = ""
		String  fromRecordtdiv = ""
		String  fromRecordbta1 = ""
		String  fromRecordbta2 = ""
		String  fromRecordbta3 = ""
		String  fromRecordbta4 = ""
		String  fromRecordbta5 = ""
		String  fromRecordbta6 = ""
		String  fromRecordbta7 = ""

		String xxbta1 = ""
		String xxbta2 = ""
		String xxbta3 = ""
		String xxbta4 = ""
		String xxbta5 = ""
		String xxbta6 = ""
		String xxbta7 = ""

		StringBuilder sb = new StringBuilder()
		for (int i = 0; i < 10; i++) {
			sb.append(Character.MAX_VALUE)
		}

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

		if(bbln == null || bbln == 0) {
			mi.error("Numéro de ligne doit être renseigné.")
			return
		}

		if(tx40.isBlank()) {
			mi.error("Désignation est obligatoire")
			return
		}

		if(bta1.isBlank()) {
			xxbta1 = sb
		}else {
			xxbta1 = bta1
		}

		if(bta2.isBlank()) {
			xxbta2 = sb
		}else {
			xxbta2 = bta2
		}

		if(bta3.isBlank()) {
			xxbta3 = sb
		}else {
			xxbta3 = bta3
		}

		if(bta4.isBlank()) {
			xxbta4 = sb
		}else {
			xxbta4 = bta4
		}

		if(bta5.isBlank()) {
			xxbta5 = sb
		}else {
			xxbta5 = bta5
		}

		if(bta6.isBlank()) {
			xxbta6 = sb
		}else {
			xxbta6 = bta6
		}

		if(bta7.isBlank()) {
			xxbta7 = sb
		}else {
			xxbta7 = bta7
		}

		if (bfa1.compareTo(xxbta1) > 0) {
			mi.error("La valeur From AIT1 est supérieur a la veleur TO AIT1.")
			return
		}

		if (bfa2.compareTo(xxbta2) > 0) {
			mi.error("La valeur From AIT2 est supérieur a la veleur TO AIT2.")
			return
		}

		if (bfa3.compareTo(xxbta3) > 0) {
			mi.error("La valeur From AIT3 est supérieur a la veleur TO AIT3.")
			return
		}

		if (bfa4.compareTo(xxbta4) > 0) {
			mi.error("La valeur From AIT4 est supérieur a la veleur TO AIT4.")
			return
		}

		if (bfa5.compareTo(xxbta5) > 0) {
			mi.error("La valeur From AIT5 est supérieur a la veleur TO AIT5.")
			return
		}

		if (bfa6.compareTo(xxbta6) > 0) {
			mi.error("La valeur From AIT6 est supérieur a la veleur TO AIT6.")
			return
		}

		if (bfa7.compareTo(xxbta7) > 0) {
			mi.error("La valeur From AIT7 est supérieur a la veleur TO AIT7.")
			return
		}


		(2..7).each { i ->
			def bfaValue = "bfa$i"
			if (bfaValue?.isBlank() && getParm(divi, i) == 1) {
				mi.error("Le segment comptable ${i} n'est pas autorisé au cumul CRS750 ")
				return
			}
		}

		//Boucle pour vérifier les chevauchements
		boolean error = false
		DBAction fgdibdRecordForCheck = database.table("FGDIBD").index("00").selectAllFields().build()
		DBContainer fgdibdContainerForCheck = fgdibdRecordForCheck.createContainer()
		fgdibdContainerForCheck.setInt("BYCONO", cono)
		fgdibdContainerForCheck.setString("BYDIVI", divi)
		fgdibdContainerForCheck.setString("BYBTAB", btab)
		
		int nrOfRecords = mi.getMaxRecords() <= 0 || mi.getMaxRecords() >= 10000 ? 10000: mi.getMaxRecords()
		

		fgdibdRecordForCheck.readAll(fgdibdContainerForCheck, 3, nrOfRecords,{ DBContainer container ->
			//if(!container.get("BYBBLN").toString().equals(bbln.toString())) {
				//FROM
				fromRecordbfa1 = container.get("BYBFA1").toString().trim()
				fromRecordbfa2 = container.get("BYBFA2").toString().trim()
				fromRecordbfa3 = container.get("BYBFA3").toString().trim()
				fromRecordbfa4 = container.get("BYBFA4").toString().trim()
				fromRecordbfa5 = container.get("BYBFA5").toString().trim()
				fromRecordbfa6 = container.get("BYBFA6").toString().trim()
				fromRecordbfa7 = container.get("BYBFA7").toString().trim()
				//TO
				fromRecordbta1 = container.get("BYBTA1").toString().trim()
				fromRecordbta2 = container.get("BYBTA2").toString().trim()
				fromRecordbta3 = container.get("BYBTA3").toString().trim()
				fromRecordbta4 = container.get("BYBTA4").toString().trim()
				fromRecordbta5 = container.get("BYBTA5").toString().trim()
				fromRecordbta6 = container.get("BYBTA6").toString().trim()
				fromRecordbta7 = container.get("BYBTA7").toString().trim()

				if(
				bfa1.compareTo(fromRecordbfa1) < 0 &&
						(bfa1.compareTo(fromRecordbta1) < 0 || fromRecordbta1.isBlank()) &&
						(xxbta1.compareTo(fromRecordbfa1) < 0 && !xxbta1.isBlank()) &&
						(xxbta1.compareTo(fromRecordbta1) < 0 || fromRecordbta1.isBlank())
						||
						bfa2.compareTo(fromRecordbfa2) < 0 &&
						(bfa2.compareTo(fromRecordbta2) < 0 || fromRecordbta2.isBlank()) &&
						(xxbta2.compareTo(fromRecordbfa2) < 0 && !xxbta2.isBlank()) &&
						(xxbta2.compareTo(fromRecordbta2) < 0 || fromRecordbta2.isBlank())
						||
						bfa3.compareTo(fromRecordbfa3) < 0 &&
						(bfa3.compareTo(fromRecordbta3) < 0 || fromRecordbta3.isBlank()) &&
						(xxbta3.compareTo(fromRecordbfa3) < 0 && !xxbta3.isBlank()) &&
						(xxbta3.compareTo(fromRecordbta3) < 0 || fromRecordbta3.isBlank())
						||
						bfa4.compareTo(fromRecordbfa4) < 0 &&
						(bfa4.compareTo(fromRecordbta4) < 0 || fromRecordbta4.isBlank()) &&
						(xxbta4.compareTo(fromRecordbfa4) < 0 && !xxbta4.isBlank()) &&
						(xxbta4.compareTo(fromRecordbta4) < 0 || fromRecordbta4.isBlank())
						||
						bfa5.compareTo(fromRecordbfa5) < 0 &&
						(bfa5.compareTo(fromRecordbta5) < 0 || fromRecordbta5.isBlank()) &&
						(xxbta5.compareTo(fromRecordbfa5) < 0 && !xxbta5.isBlank()) &&
						(xxbta5.compareTo(fromRecordbta5) < 0 || fromRecordbta5.isBlank())
						||
						bfa6.compareTo(fromRecordbfa6) < 0 &&
						(bfa6.compareTo(fromRecordbta6) < 0 || fromRecordbta6.isBlank()) &&
						(xxbta6.compareTo(fromRecordbfa6) < 0 && !xxbta6.isBlank()) &&
						(xxbta6.compareTo(fromRecordbta6) < 0 || fromRecordbta6.isBlank())
						||
						bfa7.compareTo(fromRecordbfa7) < 0 &&
						(bfa7.compareTo(fromRecordbta7) < 0 || fromRecordbta7.isBlank()) &&
						(xxbta7.compareTo(fromRecordbfa7) < 0 && !xxbta7.isBlank()) &&
						(xxbta7.compareTo(fromRecordbta7) < 0 || fromRecordbta7.isBlank())

						||

						bfa1.compareTo(fromRecordbfa1) > 0 &&
						(bfa1.compareTo(fromRecordbta1) > 0 && !fromRecordbta1.isBlank()) &&
						(xxbta1.compareTo(fromRecordbfa1) > 0 || xxbta1.isBlank()) &&
						(xxbta1.compareTo(fromRecordbta1) > 0 && !fromRecordbta1.isBlank())
						||
						bfa2.compareTo(fromRecordbfa2) > 0 &&
						(bfa2.compareTo(fromRecordbta2) > 0 && !fromRecordbta2.isBlank()) &&
						(xxbta2.compareTo(fromRecordbfa2) > 0 || xxbta2.isBlank()) &&
						(xxbta2.compareTo(fromRecordbta2) > 0 && !fromRecordbta2.isBlank())
						||
						bfa3.compareTo(fromRecordbfa3) > 0 &&
						(bfa3.compareTo(fromRecordbta3) > 0 && !fromRecordbta3.isBlank()) &&
						(xxbta3.compareTo(fromRecordbfa3) > 0 || xxbta3.isBlank()) &&
						(xxbta3.compareTo(fromRecordbta3) > 0 && !fromRecordbta3.isBlank())
						||
						bfa4.compareTo(fromRecordbfa4) > 0 &&
						(bfa4.compareTo(fromRecordbta4) > 0 && !fromRecordbta4.isBlank()) &&
						(xxbta4.compareTo(fromRecordbfa4) > 0 || xxbta4.isBlank()) &&
						(xxbta4.compareTo(fromRecordbta4) > 0 && !fromRecordbta4.isBlank())
						||
						bfa5.compareTo(fromRecordbfa5) > 0 &&
						(bfa5.compareTo(fromRecordbta5) > 0 && !fromRecordbta5.isBlank()) &&
						(xxbta5.compareTo(fromRecordbfa5) > 0 || xxbta5.isBlank()) &&
						(xxbta5.compareTo(fromRecordbta5) > 0 && !fromRecordbta5.isBlank())
						||
						bfa6.compareTo(fromRecordbfa6) > 0 &&
						(bfa6.compareTo(fromRecordbta6) > 0 && !fromRecordbta6.isBlank()) &&
						(xxbta6.compareTo(fromRecordbfa6) > 0 || xxbta6.isBlank()) &&
						(xxbta6.compareTo(fromRecordbta6) > 0 && !fromRecordbta6.isBlank())
						||
						bfa7.compareTo(fromRecordbfa7) > 0 &&
						(bfa7.compareTo(fromRecordbta7) > 0 && !fromRecordbta7.isBlank()) &&
						(xxbta7.compareTo(fromRecordbfa7) > 0 || xxbta7.isBlank()) &&
						(xxbta7.compareTo(fromRecordbta7) > 0 && !fromRecordbta7.isBlank())

						||

						fromRecordbfa1.compareTo(bfa1) < 0 &&
						(fromRecordbfa1.compareTo(xxbta1) < 0 || xxbta1.isBlank()) &&
						(fromRecordbta1.compareTo(bfa1) < 0 && !fromRecordbta1.isBlank()) &&
						(fromRecordbta1.compareTo(xxbta1) < 0 && !fromRecordbta1.isBlank())
						||
						fromRecordbfa2.compareTo(bfa2) < 0 &&
						(fromRecordbfa2.compareTo(xxbta2) < 0 || xxbta2.isBlank()) &&
						(fromRecordbta2.compareTo(bfa2) < 0 && !fromRecordbta2.isBlank()) &&
						(fromRecordbta2.compareTo(xxbta2) < 0 && !fromRecordbta2.isBlank())
						||
						fromRecordbfa3.compareTo(bfa3) < 0 &&
						(fromRecordbfa3.compareTo(xxbta3) < 0 || xxbta3.isBlank()) &&
						(fromRecordbta3.compareTo(bfa3) < 0 && !fromRecordbta3.isBlank()) &&
						(fromRecordbta3.compareTo(xxbta3) < 0 && !fromRecordbta3.isBlank())
						||
						fromRecordbfa4.compareTo(bfa4) < 0 &&
						(fromRecordbfa4.compareTo(xxbta4) < 0 || xxbta4.isBlank()) &&
						(fromRecordbta4.compareTo(bfa4) < 0 && !fromRecordbta4.isBlank()) &&
						(fromRecordbta4.compareTo(xxbta4) < 0 && !fromRecordbta4.isBlank())
						||
						fromRecordbfa5.compareTo(bfa5) < 0 &&
						(fromRecordbfa5.compareTo(xxbta5) < 0 || xxbta5.isBlank()) &&
						(fromRecordbta5.compareTo(bfa5) < 0 && !fromRecordbta5.isBlank()) &&
						(fromRecordbta5.compareTo(xxbta5) < 0 && !fromRecordbta5.isBlank())
						||
						fromRecordbfa6.compareTo(bfa6) < 0 &&
						(fromRecordbfa6.compareTo(xxbta6) < 0 || xxbta6.isBlank()) &&
						(fromRecordbta6.compareTo(bfa6) < 0 && !fromRecordbta6.isBlank()) &&
						(fromRecordbta6.compareTo(xxbta6) < 0 && !fromRecordbta6.isBlank())
						||
						fromRecordbfa7.compareTo(bfa7) < 0 &&
						(fromRecordbfa7.compareTo(xxbta7) < 0 || xxbta7.isBlank()) &&
						(fromRecordbta7.compareTo(bfa7) < 0 && !fromRecordbta7.isBlank()) &&
						(fromRecordbta7.compareTo(xxbta7) < 0 && !fromRecordbta1.isBlank())

						||

						fromRecordbfa1.compareTo(bfa1) > 0 &&
						(fromRecordbfa1.compareTo(xxbta1) > 0 && !xxbta1.isBlank()) &&
						(fromRecordbta1.compareTo(bfa1) > 0 || fromRecordbta1.isBlank()) &&
						(fromRecordbta1.compareTo(xxbta1) > 0 || fromRecordbta1.isBlank())
						||
						fromRecordbfa2.compareTo(bfa2) > 0 &&
						(fromRecordbfa2.compareTo(xxbta2) > 0 && !xxbta2.isBlank()) &&
						(fromRecordbta2.compareTo(bfa2) > 0 || fromRecordbta2.isBlank()) &&
						(fromRecordbta2.compareTo(xxbta2) > 0 || fromRecordbta2.isBlank())
						||
						fromRecordbfa3.compareTo(bfa3) > 0 &&
						(fromRecordbfa3.compareTo(xxbta3) > 0 && !xxbta3.isBlank()) &&
						(fromRecordbta3.compareTo(bfa3) > 0 || fromRecordbta3.isBlank()) &&
						(fromRecordbta3.compareTo(xxbta3) > 0 || fromRecordbta3.isBlank())
						||
						fromRecordbfa4.compareTo(bfa4) > 0 &&
						(fromRecordbfa4.compareTo(xxbta4) > 0 && !xxbta4.isBlank()) &&
						(fromRecordbta4.compareTo(bfa4) > 0 || fromRecordbta4.isBlank()) &&
						(fromRecordbta4.compareTo(xxbta4) > 0 || fromRecordbta4.isBlank())
						||
						fromRecordbfa5.compareTo(bfa5) > 0 &&
						(fromRecordbfa5.compareTo(xxbta5) > 0 && !xxbta5.isBlank()) &&
						(fromRecordbta5.compareTo(bfa5) > 0 || fromRecordbta5.isBlank()) &&
						(fromRecordbta5.compareTo(xxbta5) > 0 || fromRecordbta5.isBlank())
						||
						fromRecordbfa6.compareTo(bfa6) > 0 &&
						(fromRecordbfa6.compareTo(xxbta6) > 0 && !xxbta6.isBlank()) &&
						(fromRecordbta6.compareTo(bfa6) > 0 || fromRecordbta6.isBlank()) &&
						(fromRecordbta6.compareTo(xxbta6) > 0 || fromRecordbta6.isBlank())
						||
						fromRecordbfa7.compareTo(bfa7) > 0 &&
						(fromRecordbfa7.compareTo(xxbta7) > 0 && !xxbta7.isBlank()) &&
						(fromRecordbta7.compareTo(bfa7) > 0 || fromRecordbta7.isBlank()) &&
						(fromRecordbta7.compareTo(xxbta7) > 0 || fromRecordbta7.isBlank())
						) {

				}else {
					error = true
				}
			//}
		})
		
		if(error) {
			mi.error("Chevauchement des valeurs avec une ligne précédente.")
			return
		}
		
		DBAction fgdibhRecord = database.table("FGDIBH").index("00").build()
		DBContainer fgdibhContainer = fgdibhRecord.createContainer()
		fgdibhContainer.setInt("BXCONO", cono)
		fgdibhContainer.setString("BXDIVI", divi)
		fgdibhContainer.setString("BXBTAB", btab)
		
		if(!fgdibhRecord.read(fgdibhContainer)) {
			mi.error("Aucune entête trouvée.")
			return
		}
		
		DBAction fgdibdRecord = database.table("FGDIBD").index("00").build()
		DBContainer fgdibdContainer = fgdibdRecord.createContainer()
		fgdibdContainer.setInt("BYCONO", cono)
		fgdibdContainer.setString("BYDIVI", divi)
		fgdibdContainer.setString("BYBTAB", btab)
		fgdibdContainer.setInt("BYBBLN", bbln)


		if(!fgdibdRecord.read(fgdibdContainer)){
			fgdibdContainer.setString("BYTX40", tx40)
			fgdibdContainer.setString("BYBFA1", bfa1)
			fgdibdContainer.setString("BYBFA2", bfa2)
			fgdibdContainer.setString("BYBFA3", bfa3)
			fgdibdContainer.setString("BYBFA4", bfa4)
			fgdibdContainer.setString("BYBFA5", bfa5)
			fgdibdContainer.setString("BYBFA6", bfa6)
			fgdibdContainer.setString("BYBFA7", bfa7)
			fgdibdContainer.setString("BYBTA1", bta1)
			fgdibdContainer.setString("BYBTA2", bta2)
			fgdibdContainer.setString("BYBTA3", bta3)
			fgdibdContainer.setString("BYBTA4", bta4)
			fgdibdContainer.setString("BYBTA5", bta5)
			fgdibdContainer.setString("BYBTA6", bta6)
			fgdibdContainer.setString("BYBTA7", bta7)
			fgdibdContainer.setString("BYOBF1", obf1)
			fgdibdContainer.setString("BYOBF2", obf2)
			fgdibdContainer.setString("BYOBF3", obf3)
			fgdibdContainer.setString("BYOBT1", obt1)
			fgdibdContainer.setString("BYOBT2", obt2)
			fgdibdContainer.setString("BYOBT3", obt3)


			fgdibdContainer.set("BYRGDT", (Integer) utility.call("DateUtil", "currentDateY8AsInt"))
			fgdibdContainer.set("BYLMDT", (Integer) utility.call("DateUtil", "currentDateY8AsInt"))
			fgdibdContainer.set("BYCHID", program.getUser())
			fgdibdContainer.set("BYRGTM", (Integer) utility.call("DateUtil", "currentTimeAsInt"))
			fgdibdContainer.set("BYCHNO", 1)
			fgdibdRecord.insert(fgdibdContainer)
		}else
		{
			mi.error("Enregistrement existe déjà.")
			return
		}

	}

	/**
	 /**
	 * return parm PBFX from CRS750
	 * @param cono
	 * @param bjno
	 * @return true if no error.
	 */
	private int getParm(String divi, int accountingDimension) {

		Map<String, String> crs750MIParameters =  [DIVI:divi]
		miCaller.call("CR750MI", "GetGLSettings", crs750MIParameters , { Map<String, String> response ->
			if(response.containsKey("error")) {
				mi.error(response.errorMessage)
				return -1
			}
			if(response["PAR${accountingDimension}"].equals("0")) {
				return 0
			}else {
				return 1
			}
		})
	}
}