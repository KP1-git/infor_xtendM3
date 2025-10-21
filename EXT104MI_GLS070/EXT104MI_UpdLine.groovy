/**
 * README
 *
 * Name: EXT104MI.UpdLine
 * Description: Update a record from FGDISE
 * Date                         Changed By                    Description
 * 20250625                     a.ferre@hetic3.fr     		création
 */
public class UpdLine extends ExtendM3Transaction {
	private final MIAPI mi
	private final ProgramAPI program
	private final DatabaseAPI database
	private final UtilityAPI utility
	private final MICallerAPI miCaller

	public UpdLine(MIAPI mi, DatabaseAPI database, UtilityAPI utility, ProgramAPI program, MICallerAPI miCaller, LoggerAPI logger) {
		this.mi = mi
		this.program = program
		this.database = database
		this.utility = utility
		this.miCaller = miCaller
	}

	public void main() {
		Integer cono = mi.in.get("CONO")
		String  divi = (mi.inData.get("DIVI") == null) ? "" : mi.inData.get("DIVI").trim()
		String  dtmp = (mi.inData.get("DTMP") == null) ? "" : mi.inData.get("DTMP").trim()
		Integer dspr = mi.in.get("DSPR")
		Integer dele = mi.in.get("DELE")
		String  tx40 = (mi.inData.get("TX40") == null) ? "" : mi.inData.get("TX40").trim()
		String  tx15 = (mi.inData.get("TX15") == null) ? "" : mi.inData.get("TX15").trim()
		Integer bdlv = mi.in.get("BDLV")
		String  cflv = (mi.inData.get("CFLV") == null) ? "" : mi.inData.get("CFLV").trim()
		String  ctlv = (mi.inData.get("CTLV") == null) ? "" : mi.inData.get("CTLV").trim()
		String  btab = (mi.inData.get("BTAB") == null) ? "" : mi.inData.get("BTAB").trim()
		String  ttab = (mi.inData.get("TTAB") == null) ? "" : mi.inData.get("TTAB").trim()
		Integer cabf = mi.in.get("CABF")
		Integer bun1 = mi.in.get("BUN1")
		String  bve1 = (mi.inData.get("BVE1") == null) ? "" : mi.inData.get("BVE1").trim()
		Integer casf = mi.in.get("CASF")
		Integer bun2 = mi.in.get("BUN2")
		String  bve2 = (mi.inData.get("BVE2") == null) ? "" : mi.inData.get("BVE2").trim()
		String  rduv = (mi.inData.get("RDUV") == null) ? "" : mi.inData.get("RDUV").trim()

		// Convertir l'entier en chaîne de caractères
		String valeurStr = dspr.toString()



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

		if(dtmp.isBlank()) {
			mi.error("Modèle de reventilation est obligatoire")
			return
		}

		// Vérification
		if (!(valeurStr ==~ /^\d{6}$/)) {
			mi.error("Valeur pas au bon format : doit contenir exactement 6 chiffres (AAAAMM)")
		}

		int mois = valeurStr[4..5].toInteger()
		if (!(mois in 1..12)) {
			mi.error("Valeur pas au bon format : les deux derniers chiffres doivent représenter un mois entre 01 et 12")
		}

		if(dele == 0 || dele == null) {
			mi.error("Element d'allocation doit être renseigné")
			return
		}

		if(tx40.isBlank()) {
			mi.error("Désignation est obligatoire")
			return
		}

		if(!cflv.isBlank()) {
			if (!cflv?.trim() || (cflv ==~ /\d{2}/ && cflv.toInteger() >= 0 && cflv.toInteger() <= 98)) {
				if(!(cflv.toInteger() < bdlv)) {
					mi.error("Valeur de CFLV invalide : '${cflv}' (doit être inférieur à BDLV")
				}
			} else {
				mi.error("Valeur de CFLV invalide : '${cflv}' (doit être vide ou entre '00' et '98')")
			}
		}

		if(!ctlv.isBlank()) {
			if (!ctlv?.trim() || (ctlv ==~ /\d{2}/ && ctlv.toInteger() >= 0 && ctlv.toInteger() <= 98)) {
				if(!(ctlv.toInteger() < bdlv)) {
					mi.error("Valeur de CTLV invalide : '${ctlv}' (doit être inférieur à BDLV")
				}
			} else {
				mi.error("Valeur de CTLV invalide : '${ctlv}' (doit être vide ou entre '00' et '98')")
			}
		}

		// Comparer uniquement si les deux valeurs sont renseignées (non nulles et non vides)
		if (cflv?.trim() && ctlv?.trim()) {
			if (ctlv.toInteger() < cflv.toInteger()) {
				mi.error("La valeur de CTLV (${ctlv}) ne peut pas être inférieure à celle de CFLV (${cflv})")
			}
		}

		if(btab) {
			mi.error("table de base d'affectation est obligatoire")
			return
		}

		Integer aetp = 0
		DBAction fgdibhRecord = database.table("FGDIBH").index("00").selection("BXAETP").build()
		DBContainer fgdibhContainer = fgdibhRecord.createContainer()
		fgdibhContainer.setInt("BXCONO", cono)
		fgdibhContainer.setString("BXDIVI", divi)
		fgdibhContainer.setString("BXBTAB", btab)
		if(!fgdibhRecord.read(fgdibhContainer)){
			mi.error("table affectation de base n'existe pas.")
			return
		}else {
			aetp = fgdibhContainer.get("BXAETP")
			if(aetp == 2 && !cflv.isBlank()) {
				mi.error("CFLV doit être à blanc avec AETP = 2 en GLS060")
				return
			}

			if(aetp == 2 && !ctlv.isBlank()) {
				mi.error("CTLV doit être à blanc avec AETP = 2 en GLS060")
				return
			}

			Integer attp = 0
			DBAction fgdistRecord = database.table("FGDIST").index("00").selection("BUATTP").build()
			DBContainer fgdistContainer = fgdistRecord.createContainer()
			fgdistContainer.setInt("BUCONO", cono)
			fgdistContainer.setString("BUDIVI", divi)
			fgdistContainer.setString("BUDTMP", dtmp)
			fgdistContainer.setInt("BUDSPR", dspr)
			if(fgdistRecord.read(fgdistContainer)){
				attp = fgdistContainer.get("BUATTP")
				if(aetp == 2 && attp != 2) {
					mi.error("Si Base d'affection est de type 2, modèle Affectation doit être égal à 1")
					return
				}
			}else {
				mi.error("En tête compromise.")
				return
			}

		}

		if(ttab) {
			mi.error("table cible d'affectation est obligatoire")
			return
		}

		Integer dimt = 0
		Integer bdtp = 0


		DBAction fgdithRecord = database.table("FGDITH").index("00").selection("BZDIMT", "BZBDTP", "BZAETP").build()
		DBContainer fgdithContainer = fgdithRecord.createContainer()
		fgdithContainer.setInt("BZCONO", cono)
		fgdithContainer.setString("BZDIVI", divi)
		fgdithContainer.setString("BZTTAB", ttab)
		if(!fgdithRecord.read(fgdithContainer)){
			mi.error("table affectation cible n'existe pas.")
			return
		}else {
			dimt = fgdithContainer.get("BZDIMT")
			bdtp = fgdithContainer.get("BZBDTP")
			if(dimt != 1) {
				mi.error("Seul les tables affectation cible avec méthode affectation 1 sont autorisée")
				return
			}else {
				if(bdtp != 7 && aetp ==2) {
					mi.error("Type d'affactation cible doit être à 7 si AETP = 2")
					return
				}
			}
		}

		if(cabf != 3 && aetp == 2) {
			mi.error("Calcul base from doit être = 3 et type d'affectation = 2")
			return
		}

		if(cabf == 1 || cabf == 2 || cabf == 3 && aetp == 2) {

		}else {
			mi.error("Calcul base de reventilation comptes est invalide")
			return
		}

		if(cabf == 2 || bun1 != 0 || !bve1.isBlank()) {

			DBAction fbdudefRecord = database.table("FBDUDEF").index("00").build()
			DBContainer fbdudefContainer = fbdudefRecord.createContainer()
			fbdudefContainer.setInt("BDCONO", cono)
			fbdudefContainer.setString("BDDIVI", divi)
			fbdudefContainer.setInt("BDBUN1", bun1)
			fbdudefContainer.setString("BDBVE1", bve1)


			if(!fbdudefRecord.read(fbdudefContainer)){
				mi.error("Budget/version n'éxiste pas.")
				return
			}
		}

		if(casf < 0 || casf > 5) {
			mi.error("Début de sélection du calcul doit être 1 valeur de 1 à 5")
			return
		}

		if(casf != 0) {
			if(bdtp != 3 && bdtp != 4) {
				mi.error("Type affactation pour table cible doit être 3 ou 4 si selection calcul différent de 0")
				return
			}
		}

		if(casf == 0) {
			if(bdtp == 3 || bdtp == 4) {
				mi.error("Type affactation pour table cible doit être différent 3 ou 4 si selection calcul = 0")
				return
			}
		}

		String stab = ""
		String rdri = ""
		if(casf > 0 && casf <= 5) {
			DBAction fgditdRecord = database.table("FGDITD").index("00").selection("BESTAB").build()
			DBContainer fgditdContainer = fgditdRecord.createContainer()
			fgditdContainer.setInt("BECONO", cono)
			fgditdContainer.setString("BEDIVI", divi)
			fgditdContainer.setString("BETTAB", ttab)
			
			int nrOfRecords = mi.getMaxRecords() <= 0 || mi.getMaxRecords() >= 10000 ? 10000: mi.getMaxRecords()

			fgditdRecord.readAll(fgditdContainer, 3, nrOfRecords,{ DBContainer container ->
				stab = container.get("BESTAB").toString()
				DBAction fgdishRecord = database.table("FGDISH").index("00").selection("BFRDRI").build()
				DBContainer fgdishContainer = fgdishRecord.createContainer()
				fgdishContainer.setInt("BFCONO", cono)
				fgdishContainer.setString("BFDIVI", divi)
				fgdishContainer.setString("BFSTAB", stab)

				if(fgdishRecord.read(fgdishContainer)){
					rdri = fgdishContainer.get("BFRDRI")
					if(casf == 1 && !rdri.isBlank() || casf == 2 && !rdri.isBlank()) {
						mi.error("si selection calcul = 1 ou 2, Inducteur de ressources ne doit pas être utilisé")
						return
					}

					if(casf == 3 && rdri.isBlank() || casf == 4 && rdri.isBlank() || casf == 5 && rdri.isBlank()) {
						mi.error("si selection calcul = 3,4 ou 5, Inducteur de ressources doit être utilisé")
						return
					}
				}

			})
		}

		if(casf == 2 || bun2 != 0 || !bve2.isBlank()) {

			DBAction fbdudefRecord_2 = database.table("FBDUDEF").index("00").build()
			DBContainer fbdudefContainer_2 = fbdudefRecord_2.createContainer()
			fbdudefContainer_2.setInt("BDCONO", cono)
			fbdudefContainer_2.setString("BDDIVI", divi)
			fbdudefContainer_2.setInt("BDBUN2", bun2)
			fbdudefContainer_2.setString("BDBVE2", bve2)


			if(!fbdudefRecord_2.read(fbdudefContainer_2)){
				mi.error("Budget/version n'éxiste pas.")
				return
			}
		}

		if(casf == 1 && !rduv.isBlank() || casf == 2 && !rduv.isBlank()  || casf == 3  && !rduv.isBlank() || casf == 5 && !rduv.isBlank()) {
			mi.error("Version unité ne peut pas être renseigné.")
			return
		}

		if(casf == 4 && rduv.isBlank() ) {
			mi.error("Version unité doit  être renseigné.")
			return
		}

		if(casf == 4) {
			DBAction csytabRecord = database.table("CSYTAB").index("00").build()
			DBContainer csytabContainer = csytabRecord.createContainer()
			csytabContainer.setInt("CTCONO", cono)
			csytabContainer.setString("CTDIVI", divi)
			csytabContainer.setString("CTSTCO", "RDUV")
			csytabContainer.setString("STKY", rduv)
			csytabContainer.setString("LNCD", "")
			if(!csytabRecord.read(csytabContainer)){
				mi.error("Unité de ressource n'existe pas.")
				return
			}
		}
		
		DBAction fgdiseRecord = database.table("FGDISE").index("00").selection("BVCHNO").build()
		DBContainer fgdiseContainer = fgdiseRecord.createContainer()
		fgdiseContainer.setInt("BVCONO", cono)
		fgdiseContainer.setString("BVDIVI", divi)
		fgdiseContainer.setString("BVDTMP", divi)
		fgdiseContainer.setInt("BVDSPR", dspr)
		fgdiseContainer.setInt("BVDELE", dele)

		boolean updatable = fgdiseRecord.readLock(fgdiseContainer, { LockedResult updateRecord ->
			updateRecord.setString("BVTX40", tx40)
			updateRecord.setString("BVTX15", tx15)
			updateRecord.set("BVBDLV", bdlv)
			updateRecord.setString("BVCFLV", cflv)
			updateRecord.setString("BVCTLV", ctlv)
			updateRecord.setString("BVBTAB", btab)
			updateRecord.setString("BVTTAB", ttab)
			updateRecord.set("BVCABF", cabf)
			updateRecord.set("BVBUN1", bun1)
			updateRecord.setString("BVBVE1", bve1)
			updateRecord.set("BVCASF", casf)
			updateRecord.set("BVBUN2", bun2)
			updateRecord.setString("BVBVE2", bve2)
			updateRecord.setString("BVRDUV", rduv)
			
			int CHNO = updateRecord.getInt("BVCHNO")
			if(CHNO== 999) {CHNO = 0}
			CHNO++
			updateRecord.set("BVLMDT", (Integer) utility.call("DateUtil", "currentDateY8AsInt"))
			updateRecord.set("BVCHID", program.getUser())
			updateRecord.setInt("BVCHNO", CHNO)
			updateRecord.update()
		})

		if(!updatable)
		{
			mi.error("L'enregistrement n'existe pas.")
			return
		}

	}
}