/**
 * README
 *
 * Name: EXT100MI.UpdLine
 * Description: Update a record from FAACCB
 * Date                         Changed By                    Description
 * 20250616                     a.ferre@hetic3.fr     		création
 */
public class UpdLine extends ExtendM3Transaction {
	private final MIAPI mi
	private final ProgramAPI program
	private final DatabaseAPI database
	private final UtilityAPI utility
	private final LoggerAPI logger

	public UpdLine(MIAPI mi, DatabaseAPI database, UtilityAPI utility, ProgramAPI program, LoggerAPI logger) {
		this.mi = mi
		this.program = program
		this.database = database
		this.utility = utility
		this.logger = logger
	}

	public void main() {
		Integer cono = mi.in.get("CONO")
		String  divi = (mi.inData.get("DIVI") == null) ? "" : mi.inData.get("DIVI").trim()
		Integer rcno = mi.in.get("RCNO")
		Integer rgln = mi.in.get("RGLN")
		String  tx15 = (mi.inData.get("TX15") == null) ? "" : mi.inData.get("TX15").trim()
		String  fdiv = (mi.inData.get("FDIV") == null) ? "" : mi.inData.get("FDIV").trim()
		String  bfa1 = (mi.inData.get("BFA1") == null) ? "" : mi.inData.get("BFA1").trim()
		String  bfa2 = (mi.inData.get("BFA2") == null) ? "" : mi.inData.get("BFA2").trim()
		String  bfa3 = (mi.inData.get("BFA3") == null) ? "" : mi.inData.get("BFA3").trim()
		String  bfa4 = (mi.inData.get("BFA4") == null) ? "" : mi.inData.get("BFA4").trim()
		String  bfa5 = (mi.inData.get("BFA5") == null) ? "" : mi.inData.get("BFA5").trim()
		String  bfa6 = (mi.inData.get("BFA6") == null) ? "" : mi.inData.get("BFA6").trim()
		String  bfa7 = (mi.inData.get("BFA7") == null) ? "" : mi.inData.get("BFA7").trim()
		String  tdiv = (mi.inData.get("TDIV") == null) ? "" : mi.inData.get("TDIV").trim()
		String  bta1 = (mi.inData.get("BTA1") == null) ? "" : mi.inData.get("BTA1").trim()
		String  bta2 = (mi.inData.get("BTA2") == null) ? "" : mi.inData.get("BTA2").trim()
		String  bta3 = (mi.inData.get("BTA3") == null) ? "" : mi.inData.get("BTA3").trim()
		String  bta4 = (mi.inData.get("BTA4") == null) ? "" : mi.inData.get("BTA4").trim()
		String  bta5 = (mi.inData.get("BTA5") == null) ? "" : mi.inData.get("BTA5").trim()
		String  bta6 = (mi.inData.get("BTA6") == null) ? "" : mi.inData.get("BTA6").trim()
		String  bta7 = (mi.inData.get("BTA7") == null) ? "" : mi.inData.get("BTA7").trim()
		String  bna2 = (mi.inData.get("BNA2") == null) ? "" : mi.inData.get("BNA2").trim()
		String  bna3 = (mi.inData.get("BNA3") == null) ? "" : mi.inData.get("BNA3").trim()
		String  bna4 = (mi.inData.get("BNA4") == null) ? "" : mi.inData.get("BNA4").trim()
		String  bna5 = (mi.inData.get("BNA5") == null) ? "" : mi.inData.get("BNA5").trim()
		String  bna6 = (mi.inData.get("BNA6") == null) ? "" : mi.inData.get("BNA6").trim()
		String  bna7 = (mi.inData.get("BNA7") == null) ? "" : mi.inData.get("BNA7").trim()
		Integer  nrc2 = mi.in.get("NRC2")
		Integer  nrc3 = mi.in.get("NRC3")
		Integer  nrc4 = mi.in.get("NRC4")
		Integer  nrc5 = mi.in.get("NRC5")
		Integer  nrc6 = mi.in.get("NRC6")
		Integer  nrc7 = mi.in.get("NRC7")
		Integer  nrvt = mi.in.get("NRVT")

		String xxbta1 = ""
		String xxbta2 = ""
		String xxbta3 = ""
		String xxbta4 = ""
		String xxbta5 = ""
		String xxbta6 = ""
		String xxbta7 = ""

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
		String  fromRecordbna2 = ""
		String  fromRecordbna3 = ""
		String  fromRecordbna4 = ""
		String  fromRecordbna5 = ""
		String  fromRecordbna6 = ""
		String  fromRecordbna7 = ""


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
		
		if(fdiv.isBlank()) {
			fdiv = divi;
		}
		
		if(tdiv.isBlank()) {
			tdiv = divi;
		}

		if(rcno == null || rcno == 0) {
			mi.error("Le numéro d'enregistrement est obligatoire.")
			return
		}

		if(rgln == null || rgln == 0) {
			mi.error("Numéro de ligne doit être renseigné.")
			return
		}

		if(tx15.isBlank()) {
			mi.error("Nom doit être renseigné.")
			return
		}

		if(!bta1.isBlank()) {
			xxbta1 = bta1
		}

		if(!bta2.isBlank()) {
			xxbta2 = bta2
		}

		if(!bta3.isBlank()) {
			xxbta3 = bta3
		}

		if(!bta4.isBlank()) {
			xxbta4 = bta4
		}

		if(!bta5.isBlank()) {
			xxbta5 = bta5
		}

		if(!bta6.isBlank()) {
			xxbta6 = bta6
		}

		if(!bta7.isBlank()) {
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

		if(bna2.isBlank() && bna3.isBlank() && bna4.isBlank() && bna5.isBlank() && bna6.isBlank() && bna7.isBlank() && nrc2 == 0 && nrc3 == 0 && nrc3 == 0 && nrc5 == 0 && nrc6 == 0 && nrc7 == 0) {
			mi.error("Aucune valeur n'a été renseigné.")
			return
		}

		if(nrc2 == null || nrc3 == null || nrc4 == null || nrc5 == null || nrc6 == null || nrc7 == null) {
			mi.error("Les code remplacement NRC doivent être à 0 ou 1")
			return
		}



		//Boucle pour vérifier les chevauchements
		boolean error = false
		DBAction faaccbRecordForCheck = database.table("FAACCB").index("00").selectAllFields().build()
		DBContainer faaccbContainerForCheck = faaccbRecordForCheck.createContainer()
		faaccbContainerForCheck.setInt("FBCONO", cono)
		faaccbContainerForCheck.setString("FBDIVI", divi)
		
		int nrOfRecords = 5000

		faaccbRecordForCheck.readAll(faaccbContainerForCheck, 2, nrOfRecords,{ DBContainer container ->
			if(!container.get("FBRGLN").toString().equals(rgln.toString()) && !container.get("FBRCNO").toString().equals(rcno.toString())) {

				fromRecordbfa1 = container.get("FBBFA1").toString().trim()
				fromRecordbfa2 = container.get("FBBFA2").toString().trim()
				fromRecordbfa3 = container.get("FBBFA3").toString().trim()
				fromRecordbfa4 = container.get("FBBFA4").toString().trim()
				fromRecordbfa5 = container.get("FBBFA5").toString().trim()
				fromRecordbfa6 = container.get("FBBFA6").toString().trim()
				fromRecordbfa7 = container.get("FBBFA7").toString().trim()
				//TO
				fromRecordbta1 = container.get("FBBTA1").toString().trim()
				fromRecordbta2 = container.get("FBBTA2").toString().trim()
				fromRecordbta3 = container.get("FBBTA3").toString().trim()
				fromRecordbta4 = container.get("FBBTA4").toString().trim()
				fromRecordbta5 = container.get("FBBTA5").toString().trim()
				fromRecordbta6 = container.get("FBBTA6").toString().trim()
				fromRecordbta7 = container.get("FBBTA7").toString().trim()
				//Replace
				fromRecordbna2 = container.get("FBBNA2").toString().trim()
				fromRecordbna3 = container.get("FBBNA3").toString().trim()
				fromRecordbna4 = container.get("FBBNA4").toString().trim()
				fromRecordbna5 = container.get("FBBNA5").toString().trim()
				fromRecordbna6 = container.get("FBBNA6").toString().trim()
				fromRecordbna7 = container.get("FBBNA7").toString().trim()


				if(
				bfa1.compareTo(fromRecordbfa1) < 0 &&
						bfa1.compareTo(fromRecordbta1) < 0 &&
						xxbta1.compareTo(fromRecordbfa1) < 0 &&
						xxbta1.compareTo(fromRecordbta1) < 0
						||
						bfa2.compareTo(fromRecordbfa2) < 0 &&
						bfa2.compareTo(fromRecordbta2) < 0 &&
						xxbta2.compareTo(fromRecordbfa2) < 0 &&
						xxbta2.compareTo(fromRecordbta2) < 0
						||
						bfa3.compareTo(fromRecordbfa3) < 0 &&
						bfa3.compareTo(fromRecordbta3) < 0 &&
						xxbta3.compareTo(fromRecordbfa3) < 0 &&
						xxbta3.compareTo(fromRecordbta3) < 0
						||
						bfa4.compareTo(fromRecordbfa4) < 0 &&
						bfa4.compareTo(fromRecordbta4) < 0 &&
						xxbta4.compareTo(fromRecordbfa4) < 0 &&
						xxbta4.compareTo(fromRecordbta4) < 0
						||
						bfa5.compareTo(fromRecordbfa5) < 0 &&
						bfa5.compareTo(fromRecordbta5) < 0 &&
						xxbta5.compareTo(fromRecordbfa5) < 0 &&
						xxbta5.compareTo(fromRecordbta5) < 0
						||
						bfa6.compareTo(fromRecordbfa6) < 0 &&
						bfa6.compareTo(fromRecordbta6) < 0 &&
						xxbta6.compareTo(fromRecordbfa6) < 0 &&
						xxbta6.compareTo(fromRecordbta6) < 0
						||
						bfa7.compareTo(fromRecordbfa7) < 0 &&
						bfa7.compareTo(fromRecordbta7) < 0 &&
						xxbta7.compareTo(fromRecordbfa7) < 0 &&
						xxbta7.compareTo(fromRecordbta7) < 0
						||
						bfa1.compareTo(fromRecordbfa1) > 0 &&
						bfa1.compareTo(fromRecordbta1) > 0 &&
						xxbta1.compareTo(fromRecordbfa1) > 0 &&
						xxbta1.compareTo(fromRecordbta1) > 0
						||
						bfa2.compareTo(fromRecordbfa2) > 0 &&
						bfa2.compareTo(fromRecordbta2) > 0 &&
						xxbta2.compareTo(fromRecordbfa2) > 0 &&
						xxbta2.compareTo(fromRecordbta2) > 0
						||
						bfa3.compareTo(fromRecordbfa3) > 0 &&
						bfa3.compareTo(fromRecordbta3) > 0 &&
						xxbta3.compareTo(fromRecordbfa3) > 0 &&
						xxbta3.compareTo(fromRecordbta3) > 0
						||
						bfa4.compareTo(fromRecordbfa4) > 0 &&
						bfa4.compareTo(fromRecordbta4) > 0 &&
						xxbta4.compareTo(fromRecordbfa4) > 0 &&
						xxbta4.compareTo(fromRecordbta4) > 0
						||
						bfa5.compareTo(fromRecordbfa5) > 0 &&
						bfa5.compareTo(fromRecordbta5) > 0 &&
						xxbta5.compareTo(fromRecordbfa5) > 0 &&
						xxbta5.compareTo(fromRecordbta5) > 0
						||
						bfa6.compareTo(fromRecordbfa6) > 0 &&
						bfa6.compareTo(fromRecordbta6) > 0 &&
						xxbta6.compareTo(fromRecordbfa6) > 0 &&
						xxbta6.compareTo(fromRecordbta6) > 0
						||
						bfa7.compareTo(fromRecordbfa7) > 0 &&
						bfa7.compareTo(fromRecordbta7) > 0 &&
						xxbta7.compareTo(fromRecordbfa7) > 0 &&
						xxbta7.compareTo(fromRecordbta7) > 0
						||
						fromRecordbfa1.compareTo(bfa1) < 0 &&
						fromRecordbfa1.compareTo(xxbta1) < 0 &&
						fromRecordbta1.compareTo(bfa1) < 0 &&
						fromRecordbta1.compareTo(xxbta1) < 0
						||
						fromRecordbfa2.compareTo(bfa2) < 0 &&
						fromRecordbfa2.compareTo(xxbta2) < 0 &&
						fromRecordbta2.compareTo(bfa2) < 0 &&
						fromRecordbta2.compareTo(xxbta2) < 0
						||
						fromRecordbfa3.compareTo(bfa3) < 0 &&
						fromRecordbfa3.compareTo(xxbta3) < 0 &&
						fromRecordbta3.compareTo(bfa3) < 0 &&
						fromRecordbta3.compareTo(xxbta3) < 0
						||
						fromRecordbfa4.compareTo(bfa4) < 0 &&
						fromRecordbfa4.compareTo(xxbta4) < 0 &&
						fromRecordbta4.compareTo(bfa4) < 0 &&
						fromRecordbta4.compareTo(xxbta4) < 0
						||
						fromRecordbfa5.compareTo(bfa5) < 0 &&
						fromRecordbfa5.compareTo(xxbta5) < 0 &&
						fromRecordbta5.compareTo(bfa5) < 0 &&
						fromRecordbta5.compareTo(xxbta5) < 0
						||
						fromRecordbfa6.compareTo(bfa6) < 0 &&
						fromRecordbfa6.compareTo(xxbta6) < 0 &&
						fromRecordbta6.compareTo(bfa6) < 0 &&
						fromRecordbta6.compareTo(xxbta6) < 0
						||
						fromRecordbfa7.compareTo(bfa7) < 0 &&
						fromRecordbfa7.compareTo(xxbta7) < 0 &&
						fromRecordbta7.compareTo(bfa7) < 0 &&
						fromRecordbta7.compareTo(xxbta7) < 0
						||
						fromRecordbfa1.compareTo(bfa1) > 0 &&
						fromRecordbfa1.compareTo(xxbta1) > 0 &&
						fromRecordbta1.compareTo(bfa1) > 0 &&
						fromRecordbta1.compareTo(xxbta1) > 0
						||
						fromRecordbfa2.compareTo(bfa2) > 0 &&
						fromRecordbfa2.compareTo(xxbta2) > 0 &&
						fromRecordbta2.compareTo(bfa2) > 0 &&
						fromRecordbta2.compareTo(xxbta2) > 0
						||
						fromRecordbfa3.compareTo(bfa3) > 0 &&
						fromRecordbfa3.compareTo(xxbta3) > 0 &&
						fromRecordbta3.compareTo(bfa3) > 0 &&
						fromRecordbta3.compareTo(xxbta3) > 0
						||
						fromRecordbfa4.compareTo(bfa4) > 0 &&
						fromRecordbfa4.compareTo(xxbta4) > 0 &&
						fromRecordbta4.compareTo(bfa4) > 0 &&
						fromRecordbta4.compareTo(xxbta4) > 0
						||
						fromRecordbfa5.compareTo(bfa5) > 0 &&
						fromRecordbfa5.compareTo(xxbta5) > 0 &&
						fromRecordbta5.compareTo(bfa5) > 0 &&
						fromRecordbta5.compareTo(xxbta5) > 0
						||
						fromRecordbfa6.compareTo(bfa6) > 0 &&
						fromRecordbfa6.compareTo(xxbta6) > 0 &&
						fromRecordbta6.compareTo(bfa6) > 0 &&
						fromRecordbta6.compareTo(xxbta6) > 0
						||
						fromRecordbfa7.compareTo(bfa7) > 0 &&
						fromRecordbfa7.compareTo(xxbta7) > 0 &&
						fromRecordbta7.compareTo(bfa7) > 0 &&
						fromRecordbta7.compareTo(xxbta7) > 0
						) {

				}else {
					error = true
				}
			}
		})

		if(error) {
			mi.error("Chevauchement des valeurs avec une ligne précédente.")
			return
		}


		if (nrc2 > 1) {
			mi.error("La valeur NRC2 doit être égal à 0 ou 1.")
			return
		}

		if (nrc3 > 1) {
			mi.error("La valeur NRC3 doit être égal à 0 ou 1.")
			return
		}

		if (nrc4 > 1) {
			mi.error("La valeur NRC4 doit être égal à 0 ou 1.")
			return
		}

		if (nrc5 > 1) {
			mi.error("La valeur NRC5 doit être égal à 0 ou 1.")
			return
		}

		if (nrc6 > 1) {
			mi.error("La valeur NRC6 doit être égal à 0 ou 1.")
			return
		}

		if (nrc7 > 1) {
			mi.error("La valeur NRC7 doit être égal à 0 ou 1.")
			return
		}

		DBAction faaccbRecord = database.table("FAACCB").index("00").selection("FBCHNO").build()
		DBContainer faaccbContainer = faaccbRecord.createContainer()
		faaccbContainer.setInt("FBCONO", cono)
		faaccbContainer.setString("FBDIVI", divi)
		faaccbContainer.setInt("FBRCNO", rcno)
		faaccbContainer.setInt("FBRGLN", rgln)

		boolean updatable = faaccbRecord.readLock(faaccbContainer, { LockedResult updateRecoord ->
			updateRecoord.setString("FBTX15", tx15)
			updateRecoord.setString("FBFDIV", fdiv)
			updateRecoord.setString("FBBFA1", bfa1)
			updateRecoord.setString("FBBFA2", bfa2)
			updateRecoord.setString("FBBFA3", bfa3)
			updateRecoord.setString("FBBFA4", bfa4)
			updateRecoord.setString("FBBFA5", bfa5)
			updateRecoord.setString("FBBFA6", bfa6)
			updateRecoord.setString("FBBFA7", bfa7)
			updateRecoord.setString("FBTDIV", tdiv)
			updateRecoord.setString("FBBTA1", bta1)
			updateRecoord.setString("FBBTA2", bta2)
			updateRecoord.setString("FBBTA3", bta3)
			updateRecoord.setString("FBBTA4", bta4)
			updateRecoord.setString("FBBTA5", bta5)
			updateRecoord.setString("FBBTA6", bta6)
			updateRecoord.setString("FBBTA7", bta7)
			updateRecoord.setString("FBBNA2", bna2)
			updateRecoord.setString("FBBNA3", bna3)
			updateRecoord.setString("FBBNA4", bna4)
			updateRecoord.setString("FBBNA5", bna5)
			updateRecoord.setString("FBBNA6", bna6)
			updateRecoord.setString("FBBNA7", bna7)
			updateRecoord.set("FBNRC2", nrc2)
			updateRecoord.set("FBNRC3", nrc3)
			updateRecoord.set("FBNRC4", nrc4)
			updateRecoord.set("FBNRC5", nrc5)
			updateRecoord.set("FBNRC6", nrc6)
			updateRecoord.set("FBNRC7", nrc7)

			int CHNO = updateRecoord.getInt("FBCHNO")
			if(CHNO== 999) {CHNO = 0}
			CHNO++
			updateRecoord.set("FBLMDT", (Integer) utility.call("DateUtil", "currentDateY8AsInt"))
			updateRecoord.set("FBCHID", program.getUser())
			updateRecoord.setInt("FBCHNO", CHNO)
			updateRecoord.update()
		})

		if(!updatable)
		{
			mi.error("L'enregistrement n'existe pas.")
			return
		}
	}
}