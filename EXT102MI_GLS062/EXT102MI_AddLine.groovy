/**
 * README
 *
 * Name: EXT102MI.AddLine
 * Description: Add a record in FGDITD
 * Date                         Changed By                    Description
 * 20250624                     d.decosterd@hetic3.fr     		création
 */
public class AddLine extends ExtendM3Transaction {
	private final MIAPI mi
	private final DatabaseAPI database
	private final ProgramAPI program
	private final UtilityAPI utility
	private final MICallerAPI miCaller
	private final MessageAPI message
	private final LoggerAPI logger

	public AddLine(MIAPI mi, DatabaseAPI database, ProgramAPI program, UtilityAPI utility, MICallerAPI miCaller, MessageAPI message, LoggerAPI logger) {
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
		Integer bltp =  mi.in.get("BLTP")
		String tx40 = (mi.inData.get("TX40") == null) ? "" : mi.inData.get("TX40").trim()
		String tfa1 = (mi.inData.get("TFA1") == null) ? "" : mi.inData.get("TFA1").trim()
		String tfa2 = (mi.inData.get("TFA2") == null) ? "" : mi.inData.get("TFA2").trim()
		String tfa3 = (mi.inData.get("TFA3") == null) ? "" : mi.inData.get("TFA3").trim()
		String tfa4 = (mi.inData.get("TFA4") == null) ? "" : mi.inData.get("TFA4").trim()
		String tfa5 = (mi.inData.get("TFA5") == null) ? "" : mi.inData.get("TFA5").trim()
		String tfa6 = (mi.inData.get("TFA6") == null) ? "" : mi.inData.get("TFA6").trim()
		String tfa7 = (mi.inData.get("TFA7") == null) ? "" : mi.inData.get("TFA7").trim()
		String ofdi = (mi.inData.get("OFDI") == null) ? "" : mi.inData.get("OFDI").trim()
		Float dipe = mi.in.get("DIPE")
		Float dirs = mi.in.get("DIRS")
		String stab = (mi.inData.get("STAB") == null) ? "" : mi.inData.get("STAB").trim()


		if(cono == null) {
			mi.error("La CONO est obligatoire.")
			return
		}

		if(divi.isBlank()) {
			mi.error("L'api devrait être utilisé localement, DIVI obligatoire.")
			return
		}

		if(!utility.call("CheckUtil", "checkConoExist", database, cono)) {
			mi.error("La division est inexistante.")
			return
		}

		if(ttab.isBlank()) {
			mi.error("Table cible reventilation est obligatoire")
			return
		}

		if(bbln == null) {
			mi.error("Ligne de reventilation est obligatoire.")
			return
		}

		if(bbln <=0) {
			mi.error("Ligne de reventilation doit être supérieur à 0.")
			return
		}

		if(bltp == null) {
			mi.error("Type ligne de reventilation est obligatoire.")
			return
		}

		if( bltp < 1 || bltp > 2) {
			mi.error("Type ligne de reventilation "+bltp+" est invalide.")
			return
		}

		DBAction fgdithRecord = database.table("FGDITH").index("00").selection("BZDIMT","BZBDTP").build()
		DBContainer fgdithContainer = fgdithRecord.createContainer()
		fgdithContainer.setInt("BZCONO", cono)
		fgdithContainer.setString("BZDIVI", divi)
		fgdithContainer.setString("BZTTAB", ttab)

		if(!fgdithRecord.read(fgdithContainer)) {
			mi.error("Aucune entête trouvée.")
			return
		}

		if(bltp == 2 && fgdithContainer.getInt("BZDIMT") == 2) {
			mi.error("Type ligne de reventilation 2 n'est pas valide pour méthode de reventilation 2.")
			return
		}

		if(tx40.isBlank()) {
			mi.error("Désignation est obligatoire")
			return
		}

		int crdi = -1
		boolean accDim2Prevented = false
		boolean accDim3Prevented = false
		boolean accDim4Prevented = false
		boolean accDim5Prevented = false
		boolean accDim6Prevented = false
		boolean accDim7Prevented = false

		Map<String, String> crs750MIParameters =  [DIVI:divi]
		miCaller.call("CRS750MI", "GetGLSettings", crs750MIParameters , { Map<String, String> response ->
			if(response.containsKey("error")) {
				mi.error(response.errorMessage)
				return
			}
			crdi = Integer.parseInt(response.get("CRDI"))
			accDim2Prevented = response.get("PBF2") == "1"
			accDim3Prevented = response.get("PBF3") == "1"
			accDim4Prevented = response.get("PBF4") == "1"
			accDim5Prevented = response.get("PBF5") == "1"
			accDim6Prevented = response.get("PBF6") == "1"
			accDim7Prevented = response.get("PBF7") == "1"
		})

		int bdtp = fgdithContainer.getInt("BZBDTP")
		boolean in61 = !(crdi ==1 && (bdtp == 5 || bdtp == 6))

		if(!in61) {
			if(!ofdi.isBlank()) {
				if(bltp != 1) {
					mi.error("Offset division is only allowed for line type 1")
					return
				}
				DBAction cmfadvRecord = database.table("CMFADV").index("00").build()
				DBContainer cmfadvContainer = cmfadvRecord.createContainer()
				cmfadvContainer.setInt("M1CONO", cono)
				cmfadvContainer.setString("M1DIVI", divi)
				cmfadvContainer.setString("M1ADIV", ofdi)
				if(!cmfadvRecord.read(cmfadvContainer)) {
					mi.error("Division "+ofdi+" n'est pas autorisée")
					return
				}
			}
		}

		String diviToUse = !ofdi.isBlank() ? ofdi : divi

		if(!tfa1.trim().equals("=") && !tfa1.trim().equals("+") || bdtp == 7) {
			int a1ch = 0
			int a2ch = tfa2.trim().equals("=") || tfa2.trim().equals("+") ? 1 : 0
			int a3ch = tfa3.trim().equals("=") || tfa3.trim().equals("+") ? 1 : 0
			int a4ch = tfa4.trim().equals("=") || tfa4.trim().equals("+") ? 1 : 0
			int a5ch = tfa5.trim().equals("=") || tfa5.trim().equals("+") ? 1 : 0
			int a6ch = tfa6.trim().equals("=") || tfa6.trim().equals("+") ? 1 : 0
			int a7ch = tfa7.trim().equals("=") || tfa7.trim().equals("+") ? 1 : 0

			boolean isValid = isValidAccountingItems(cono, diviToUse, Integer.parseInt(program.LDAZD.get("CMTP").toString()), tfa1, tfa2, tfa3, tfa4, tfa5, tfa6, tfa7, a1ch, a2ch, a3ch, a4ch, a5ch, a6ch, a7ch)
			if(!isValid) {
				return
			}
		}

		if(bdtp != 7) {
			if(bdtp == 3 || bdtp == 4) {
				if(bltp ==1 && stab.isBlank()) {
					mi.error("Table de sélection reventilation doit être renseigné.")
					return
				}
				if(!stab.isBlank()) {
					DBAction fgdishRecord = database.table("FGDISH").index("00").selection("BFRDRI","BFAIH1","BFAIH2","BFAIH3","BFAIH4","BFAIH5","BFAIH6","BFAIH7").build()
					DBContainer fgdishContainer = fgdishRecord.createContainer()
					fgdishContainer.setInt("BFCONO", cono)
					fgdishContainer.setString("BFDIVI", divi)
					fgdishContainer.setString("BFSTAB", stab)

					boolean readFgdish = fgdishRecord.read(fgdishContainer)
					//   - Check record
					if(!readFgdish) {
						String errorMessage = message.getMessage("WSTAB03",[stab])
						mi.error(errorMessage)
						return
					}
					if(!fgdishContainer.getString("BFRDRI").isBlank()&& bdtp != 4) {
						String errorMessage = message.getMessage("GL06313",[stab])
						mi.error(errorMessage)
						return
					}

					//   Dimension mark'+' in select table must correspond to code here
					if(fgdishContainer.getChar("BFAIH1") == '+' && !tfa1.trim().equals("+") || tfa1.trim().equals("+") && fgdishContainer.getChar("BFAIH1") != '+') {
						String errorMessage = message.getMessage("GL06307", ["Accounting dimension 1"])
						mi.error(errorMessage)
						return
					}
					if(fgdishContainer.getChar("BFAIH2") == '+' && !tfa2.trim().equals("+") || tfa2.trim().equals("+") && fgdishContainer.getChar("BFAIH2") != '+') {
						String errorMessage = message.getMessage("GL06307", ["Accounting dimension 2"])
						mi.error(errorMessage)
						return
					}
					if(fgdishContainer.getChar("BFAIH3") == '+' && !tfa3.trim().equals("+") || tfa3.trim().equals("+") && fgdishContainer.getChar("BFAIH3") != '+') {
						String errorMessage = message.getMessage("GL06307", ["Accounting dimension 3"])
						mi.error(errorMessage)
						return
					}
					if(fgdishContainer.getChar("BFAIH4") == '+' && !tfa4.trim().equals("+") || tfa4.trim().equals("+") && fgdishContainer.getChar("BFAIH4") != '+') {
						String errorMessage = message.getMessage("GL06307", ["Accounting dimension 4"])
						mi.error(errorMessage)
						return
					}
					if(fgdishContainer.getChar("BFAIH5") == '+' && !tfa5.trim().equals("+") || tfa5.trim().equals("+") && fgdishContainer.getChar("BFAIH5") != '+') {
						String errorMessage = message.getMessage("GL06307", ["Accounting dimension 5"])
						mi.error(errorMessage)
						return
					}
					if(fgdishContainer.getChar("BFAIH6") == '+' && !tfa6.trim().equals("+") || tfa6.trim().equals("+") && fgdishContainer.getChar("BFAIH6") != '+') {
						String errorMessage = message.getMessage("GL06307", ["Accounting dimension 6"])
						mi.error(errorMessage)
						return
					}
					if(fgdishContainer.getChar("BFAIH7") == '+' && !tfa7.trim().equals("+") || tfa7.trim().equals("+") && fgdishContainer.getChar("BFAIH7") != '+') {
						String errorMessage = message.getMessage("GL06307", ["Accounting dimension 7"])
						mi.error(errorMessage)
						return
					}
				}
			}else {
				if(!stab.isBlank()) {
					mi.error("Table de sélection reventilation doit être renseigné.")
					return
				}
			}
			if(tfa1.trim().equals("+") && stab.isBlank()) {
				String errorMessage = message.getMessage("GL06308", ["Accounting dimension 1"])
				mi.error(errorMessage)
				return
			}
			if(tfa2.trim().equals("+") && stab.isBlank()) {
				String errorMessage = message.getMessage("GL06308", ["Accounting dimension 2"])
				mi.error(errorMessage)
				return
			}
			if(tfa3.trim().equals("+") && stab.isBlank()) {
				String errorMessage = message.getMessage("GL06308", ["Accounting dimension 3"])
				mi.error(errorMessage)
				return
			}
			if(tfa4.trim().equals("+") && stab.isBlank()) {
				String errorMessage = message.getMessage("GL06308", ["Accounting dimension 4"])
				mi.error(errorMessage)
				return
			}
			if(tfa5.trim().equals("+") && stab.isBlank()) {
				String errorMessage = message.getMessage("GL06308", ["Accounting dimension 5"])
				mi.error(errorMessage)
				return
			}
			if(tfa6.trim().equals("+") && stab.isBlank()) {
				String errorMessage = message.getMessage("GL06308", ["Accounting dimension 6"])
				mi.error(errorMessage)
				return
			}
			if(tfa7.trim().equals("+") && stab.isBlank()) {
				String errorMessage = message.getMessage("GL06308", ["Accounting dimension 7"])
				mi.error(errorMessage)
				return
			}
		}

		if(tfa2.isBlank() && accDim2Prevented) {
			String errorMessage = message.getMessage("S_00971", ["2"])
			mi.error(errorMessage)
			return
		}
		if(tfa3.isBlank() && accDim2Prevented) {
			String errorMessage = message.getMessage("S_00971", ["3"])
			mi.error(errorMessage)
			return
		}
		if(tfa4.isBlank() && accDim2Prevented) {
			String errorMessage = message.getMessage("S_00971", ["4"])
			mi.error(errorMessage)
			return
		}
		if(tfa5.isBlank() && accDim2Prevented) {
			String errorMessage = message.getMessage("S_00971", ["5"])
			mi.error(errorMessage)
			return
		}
		if(tfa6.isBlank() && accDim2Prevented) {
			String errorMessage = message.getMessage("S_00971", ["6"])
			mi.error(errorMessage)
			return
		}
		if(tfa7.isBlank() && accDim2Prevented) {
			String errorMessage = message.getMessage("S_00971", ["7"])
			mi.error(errorMessage)
			return
		}

		if(dipe != null) {
			//   - Check relative distribution percentage
			int numberOfDiggits = utility.call("NumberUtil", "getNumberOfDigits", dipe)

			if(numberOfDiggits > 3) {
				mi.error("relative allocation percentage ne peux avoir plus de trois chiffres avant la virgule.")
				return
			}

			int numberOfDecimal = utility.call("NumberUtil", "getNumberOfDecimals", dipe)
			if(numberOfDecimal > 3) {
				mi.error("relative allocation percentage ne peux avoir plus de trois chiffres après la virgule.")
				return
			}

			if(dipe < 0) {
				String errorMessage = message.getMessage("GL06301", [dipe.toString()])
				mi.error(errorMessage)
				return
			}
		}

		if((bltp == 1 && bdtp != 2 && bdtp != 6 || bdtp == 7) && dipe == null) {
			String errorMessage = message.getMessage("WDI5102", [])
			mi.error(errorMessage)
			return
		}

		if(bdtp != 7) {
			//   - Check relative distribution share
			if(dirs != null) {
				int numberOfDiggits = utility.call("NumberUtil", "getNumberOfDigits", dirs)

				if(numberOfDiggits > 13) {
					mi.error("relative allocation percentage ne peux avoir plus de treize chiffres avant la virgule.")
					return
				}

				int numberOfDecimal = utility.call("NumberUtil", "getNumberOfDecimals", dirs)
				if(numberOfDecimal > 2) {
					mi.error("relative allocation percentage ne peux avoir plus de deux chiffres après la virgule.")
					return
				}

				if(dirs < 0) {
					String errorMessage = message.getMessage("GL06302", [dirs.toString()])
					mi.error(errorMessage)
					return
				}
			}

			if(bdtp == 2 || bdtp == 6 ) {
				if(dirs == null || dirs < 0 ) {
					String errorMessage = message.getMessage("WDI5102", [])
					mi.error(errorMessage)
					return
				}
			}else {
				if(dirs != null && dirs > 0) {
					String errorMessage = message.getMessage("GL06304", [])
					mi.error(errorMessage)
					return
				}
			}
		}

		DBAction fgditdRecord = database.table("FGDITD").index("00").build()
		DBContainer fgditdContainer = fgditdRecord.createContainer()
		fgditdContainer.setInt("BECONO", cono)
		fgditdContainer.setString("BEDIVI", divi)
		fgditdContainer.setString("BETTAB", ttab)
		fgditdContainer.setInt("BEBBLN", bbln)

		if(!fgditdRecord.read(fgditdContainer)){
			fgditdContainer.setInt("BEBLTP", bltp)
			fgditdContainer.setString("BETX40", tx40)
			fgditdContainer.setString("BETFA1", tfa1)
			fgditdContainer.setString("BETFA2", tfa2)
			fgditdContainer.setString("BETFA3", tfa3)
			fgditdContainer.setString("BETFA4", tfa4)
			fgditdContainer.setString("BETFA5", tfa5)
			fgditdContainer.setString("BETFA6", tfa6)
			fgditdContainer.setString("BETFA7", tfa7)
			fgditdContainer.setString("BEOFDI", ofdi)
			fgditdContainer.set("BEDIPE", dipe)
			fgditdContainer.set("BEDIRS", dirs)
			fgditdContainer.setString("BESTAB", stab)
			fgditdContainer.set("BERGDT", (Integer) utility.call("DateUtil", "currentDateY8AsInt"))
			fgditdContainer.set("BERGTM", (Integer) utility.call("DateUtil", "currentTimeAsInt"))
			fgditdContainer.set("BELMDT", (Integer) utility.call("DateUtil", "currentDateY8AsInt"))
			fgditdContainer.set("BECHID", program.getUser())
			fgditdContainer.set("BECHNO", 1)
			fgditdRecord.insert(fgditdContainer)
		}else
		{
			mi.error("Enregistrement existe déjà.")
			return
		}

	}

	/**
	 * Check validity of the value of tfa1
	 * @param cono
	 * @param divi
	 * @param tfa1
	 * @return true if valid
	 */
	private boolean isValidTfa1(int cono, String divi, String tfa1) {
		DBAction fpseudRecord = database.table("FPSEUD").index("00").selection("AIT1").build()
		DBContainer fpseudContainer = fpseudRecord.createContainer()
		fpseudContainer.setInt("EUCONO", cono)
		fpseudContainer.setString("EUDIVI", divi)
		fpseudContainer.setString("EUPSTM",tfa1)

		boolean read = fpseudRecord.read(fpseudContainer)
		if(!read) {
			fpseudContainer.clear("EUDIVI")
			read = fpseudRecord.read(fpseudContainer)
		}

		return read
	}

	/**
	 * check accounting items like in the batch CCHKAIT
	 * @return true if all checks are validated
	 */
	private boolean isValidAccountingItems(int cono, String divi, Integer cmtp,
			String tfa1, String tfa2, String tfa3, String tfa4, String tfa5, String tfa6, String tfa7,
			int a1ch, int a2ch, int a3ch, int a4ch, int a5ch, int a6ch, int a7ch) {
		if(tfa1.isBlank()) {
			mi.error("Segment comptable 1 est obligatoire")
			return false
		}
		DBAction fchaccRecord = database.table("FCHACC").index("00").selection("EAACR2","EAACR3","EAACR4","EAACR5","EAACR6","EAACR7","EALCCD").build()
		DBContainer fchaccContainer = fchaccRecord.createContainer()
		fchaccContainer.setInt("EACONO", cono)
		fchaccContainer.setString("EADIVI", divi)
		fchaccContainer.setInt("EAAITP", 1)
		fchaccContainer.setString("EAAITM", tfa1)

		boolean readfchacc = fchaccRecord.read(fchaccContainer)
		if(!readfchacc && !divi.isBlank() && (cmtp == 2 || cmtp == 4) ) {
			fchaccContainer.clear("EADIVI")
			readfchacc = fchaccRecord.read(fchaccContainer)
		}

		if(!readfchacc) {
			String errorMessage = message.getMessage("XAC0103",["COMPTE",tfa1])
			mi.error(errorMessage)
			return false
		}

		if(fchaccContainer.getInt("EALCCD") == 1) {
			String errorMessage = message.getMessage("XAI0101",["COMPTE",tfa1])
			mi.error(errorMessage)
			return false
		}

		boolean isValid = isValidAccountingItem("2", cono, divi, tfa1, a2ch, fchaccContainer.getInt("EAACR2"), tfa2, cmtp, "ETABLISS")
		if(!isValid)
			return false

		isValid = isValidAccountingItem("3", cono, divi, tfa1, a3ch, fchaccContainer.getInt("EAACR3"), tfa3, cmtp, "CCOUT")
		if(!isValid)
			return false

		isValid = isValidAccountingItem("4", cono, divi, tfa1, a4ch, fchaccContainer.getInt("EAACR4"), tfa4, cmtp, "PRODUIT")
		if(!isValid)
			return false

		isValid = isValidAccountingItem("5", cono, divi, tfa1, a5ch, fchaccContainer.getInt("EAACR5"), tfa5, cmtp, "AUXILIAIRE")
		if(!isValid)
			return false

		isValid = isValidAccountingItem("6", cono, divi, tfa1, a6ch, fchaccContainer.getInt("EAACR6"), tfa6, cmtp, "PROJET")
		if(!isValid)
			return false

		isValid = isValidAccountingItem("7", cono, divi, tfa1, a7ch, fchaccContainer.getInt("EAACR7"), tfa7, cmtp, "EL PROJET")
		if(!isValid)
			return false

		return true
	}

	/*
	 * check accounting item i like in the batch CCHKAIT
	 * @return true if checks for accounting item i are validated
	 */
	private boolean isValidAccountingItem(String i, int cono, String divi, String tfa1, int ach, int acr, String tfa, int cmtp, String fieldLabel ) {
		if(ach == 1) {
			return true;
		}

		if( acr ==1 || acr == 2|| acr == 3|| acr == 4) {
			if(tfa.isBlank()) {
				String errorMessage = message.getMessage("XAC0102",[fieldLabel, tfa])
				mi.error(errorMessage)
				return false
			}
		}

		if(acr == 5 && !tfa.isBlank()) {
			String errorMessage = message.getMessage("XAC0101",[fieldLabel, tfa])
			mi.error(errorMessage)
			return false
		}

		DBAction fchaccRecord = database.table("FCHACC").index("00").selection("EAACR"+i,"EALCCD","EADIVI").build()
		DBContainer fchaccContainer = fchaccRecord.createContainer()
		fchaccContainer.setInt("EACONO", cono)
		fchaccContainer.setString("EADIVI", divi)
		fchaccContainer.setInt("EAAITP", Integer.parseInt(i))
		fchaccContainer.setString("EAAITM", tfa)

		boolean readfchacc = fchaccRecord.read(fchaccContainer)
		if(!readfchacc && !divi.isBlank() && (cmtp == 2 || cmtp == 4) ) {
			fchaccContainer.clear("EADIVI")
			readfchacc = fchaccRecord.read(fchaccContainer)
		}

		if(!readfchacc && (acr == 3 || acr == 4)) {
			String errorMessage = message.getMessage("XAC0103",[fieldLabel, tfa])
			mi.error(errorMessage)
			return false
		}

		if(!readfchacc && !tfa.isBlank() && (acr == 7 || acr == 8)) {
			String errorMessage = message.getMessage("XAC0103",[fieldLabel, tfa])
			mi.error(errorMessage)
			return false
		}

		if(tfa.isBlank()) {
			return true
		}

		if(fchaccContainer.getInt("EALCCD")== 1) {
			String errorMessage = message.getMessage("XAI0101",[fieldLabel, tfa])
			mi.error(errorMessage)
			return false
		}

		if(acr == 2 || acr ==4 && acr ==6 && acr ==8) {
			ExpressionFactory fchchkExpressionFactory = database.getExpressionFactory("FCHCHK")
			fchchkExpressionFactory =  fchchkExpressionFactory.ge("ECAITF", acr.toString()).and(fchchkExpressionFactory.le("ECAITT", acr.toString()))
			DBAction fchchkRecord = database.table("FCHCHK").index("00").matching(fchchkExpressionFactory).build()
			DBContainer fchchkContainer = fchaccRecord.createContainer()
			fchchkContainer.setInt("ECCONO", cono)
			if(readfchacc) {
				fchchkContainer.setString("ECDIVI", fchaccContainer.getString("EADIVI"))
			}else {
				fchchkContainer.setString("ECDIVI", divi)
			}
			fchchkContainer.setInt("ECAITP", 1)
			fchchkContainer.setString("ECAITM", tfa1)
			fchchkContainer.setInt("ECCHPU", 1)
			fchchkContainer.setInt("ECAITV", 2)
			fchchkContainer.setInt("ECACRC", fchaccContainer.getInt("EAACR"+i))

			boolean readFchchk = fchchkRecord.readAll(fchchkContainer, 7, 1,{ })
			if(!readFchchk) {
				String errorMessage = message.getMessage("XAI0106",[fieldLabel, tfa])
				mi.error(errorMessage)
				return false
			}
		}
		return true
	}
}