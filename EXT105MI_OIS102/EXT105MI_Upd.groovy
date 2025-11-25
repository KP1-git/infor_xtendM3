/**
 * README
 *
 * Name: EXT105MI.Upd
 * Description: Update an addressesfrom a customer order
 * Date                         Changed By                    Description
 * 20250818                     d.decosterd@hetic3.fr     		création
 * 20251125						 d.decosterd@hetic3.fr			remove unused variable ooadreRecord and ooadreContainer
 */
public class Upd extends ExtendM3Transaction {
	private final MIAPI mi
	private final DatabaseAPI database
	private final ProgramAPI program
	private final MessageAPI message
	private final UtilityAPI utility

	public Upd(MIAPI mi, DatabaseAPI database, ProgramAPI program, MessageAPI message, UtilityAPI utility) {
		this.mi = mi
		this.database = database
		this.program = program
		this.message = message
		this.utility = utility
	}

	public void main() {
		Integer cono = mi.in.get("CONO")
		String divi = (mi.inData.get("DIVI") == null)? "" : mi.inData.get("DIVI").trim()
		String orno = (mi.inData.get("ORNO") == null)? "" : mi.inData.get("ORNO").trim()
		Integer adrt = mi.in.get("ADRT")
		String adid = (mi.inData.get("ADID") == null)? "" : mi.inData.get("ADID").trim()
		String cunm = (mi.inData.get("CUNM") == null)? "" : mi.inData.get("CUNM").trim()
		String cua1 = (mi.inData.get("CUA1") == null)? "" : mi.inData.get("CUA1").trim()
		String cua2 = (mi.inData.get("CUA2") == null)? "" : mi.inData.get("CUA2").trim()
		String cua3 = (mi.inData.get("CUA3") == null)? "" : mi.inData.get("CUA3").trim()
		String cua4 = (mi.inData.get("CUA4") == null)? "" : mi.inData.get("CUA4").trim()
		String pono = (mi.inData.get("PONO") == null)? "" : mi.inData.get("PONO").trim()
		String phno = (mi.inData.get("PHNO") == null)? "" : mi.inData.get("PHNO").trim()
		String tfno = (mi.inData.get("TFNO") == null)? "" : mi.inData.get("TFNO").trim()
		String yref = (mi.inData.get("YREF") == null)? "" : mi.inData.get("YREF").trim()
		String meal = (mi.inData.get("MEAL") == null)? "" : mi.inData.get("MEAL").trim()
		String cscd = (mi.inData.get("CSCD") == null)? "" : mi.inData.get("CSCD").trim()
		String vrno = (mi.inData.get("VRNO") == null)? "" : mi.inData.get("VRNO").trim()
		String edes = (mi.inData.get("EDES") == null)? "" : mi.inData.get("EDES").trim()
		String rout = (mi.inData.get("ROUT") == null)? "" : mi.inData.get("ROUT").trim()
		Integer rodn = mi.in.get("RODN")
		String ulzo = (mi.inData.get("ULZO") == null)? "" : mi.inData.get("ULZO").trim()
		String ecf1 = (mi.inData.get("ECF1") == null)? "" : mi.inData.get("ECF1").trim()
		String ecf2 = (mi.inData.get("ECF2") == null)? "" : mi.inData.get("ECF2").trim()
		String ecf3 = (mi.inData.get("ECF3") == null)? "" : mi.inData.get("ECF3").trim()
		String ecf4 = (mi.inData.get("ECF4") == null)? "" : mi.inData.get("ECF4").trim()
		String ecf5 = (mi.inData.get("ECF5") == null)? "" : mi.inData.get("ECF5").trim()
		Integer geoc = mi.in.get("GEOC")
		String taxc = (mi.inData.get("TAXC") == null)? "" : mi.inData.get("TAXC").trim()
		String ecar = (mi.inData.get("ECAR") == null)? "" : mi.inData.get("ECAR").trim()
		String hafe = (mi.inData.get("HAFE") == null)? "" : mi.inData.get("HAFE").trim()
		String dlsp = (mi.inData.get("DLSP") == null)? "" : mi.inData.get("DLSP").trim()
		String dstx = (mi.inData.get("DSTX") == null)? "" : mi.inData.get("DSTX").trim()
		String modl = (mi.inData.get("MODL") == null)? "" : mi.inData.get("MODL").trim()
		String tedl = (mi.inData.get("TEDL") == null)? "" : mi.inData.get("TEDL").trim()
		String tel2 = (mi.inData.get("TEL2") == null)? "" : mi.inData.get("TEL2").trim()
		String town = (mi.inData.get("TOWN") == null)? "" : mi.inData.get("TOWN").trim()
		String advi = (mi.inData.get("ADVI") == null)? "" : mi.inData.get("ADVI").trim()
		Integer bop1 = mi.in.get("BOP1")
		String frco = (mi.inData.get("FRCO") == null)? "" : mi.inData.get("FRCO").trim()
		String sple = (mi.inData.get("SPLE") == null)? "" : mi.inData.get("SPLE").trim()
		String rasn = (mi.inData.get("RASN") == null)? "" : mi.inData.get("RASN").trim()
		Integer wf01 = mi.in.get("WF01")

		if(cono == null) {
			mi.error("La CONO est obligatoire.")
			return
		}

		if(orno.isBlank()) {
			mi.error("Le numéro de commande est obligatoire.")
			return
		}

		DBAction ooheadRecord = database.table("OOHEAD").index("00").selection("OAADID","OAORTP","OATEDL","OAMODL","OAROUT","OARODN","OAINRC","OADECU","OACUNO","OAORTP","OAPYNO","OALNCD").build()
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
			mi.error("Le type d'adresse ne peux avoir comme valeur que 1,3 ou 6.")
			return
		}

		DBAction ooadreRecord = database.table("OOADRE").index("00").selection("ODTEDL","ODMODL","ODROUT","ODRODN","ODCSCD",
			"ODDLSP","ODEDES","ODECAR","ODPONO","ODFRCO","ODYREF").build()
		DBContainer ooadreContainer = ooadreRecord.createContainer()
		ooadreContainer.setInt("ODCONO", cono)
		ooadreContainer.setString("ODORNO", orno)
		ooadreContainer.setInt("ODADRT", adrt)
		ooadreContainer.setString("ODADID",adid)

		if(!ooadreRecord.read(ooadreContainer)) {
			mi.error("Enregistrement n'existe pas.")
			return
		}

		String usedTedl = tedl
		if(tedl.isBlank()) {
			usedTedl = ooadreContainer.getString("ODTEDL")
		}
		String usedModl = modl
		if(modl.isBlank()) {
			usedModl = ooadreContainer.getString("ODMODL")
		}
		String usedRout = rout
		if(rout.isBlank()) {
			usedRout = ooadreContainer.getString("ODROUT")
		}
		Integer usedRodn = rodn
		if(rodn == null) {
			usedRodn = ooadreContainer.getInt("ODRODN")
		}
		String usedCscd = cscd
		if(cscd.isBlank()) {
			usedCscd = ooadreContainer.getString("ODCSCD")
		}
		String usedDlsp = dlsp
		if(dlsp.isBlank()) {
			usedDlsp = ooadreContainer.getString("ODDLSP")
		}
		String usedEdes = edes
		if(edes) {
			usedEdes = ooadreContainer.getString("ODEDES")
		}
		String usedEcar = ecar
		if(ecar.isBlank()) {
			usedEcar = ooadreContainer.getString("ODECAR")
		}
		String usedPono = pono
		if(pono.isBlank()) {
			usedPono = ooadreContainer.getString("ODPONO")
		}
		String usedFrco = frco
		if(frco.isBlank()) {
			usedFrco = ooadreContainer.getString("ODFRCO")
		}
		String usedYref = yref
		if(yref.isBlank()) {
			usedYref = ooadreContainer.getString("ODYREF")
		}


		if(wf01 != null && (wf01 <0 || wf01 > 1)) {
			mi.error("Le champ WF01 ne prend comme valeur que 0 ou 1")
			return
		}

		if(!tedl.isEmpty() || !modl.isEmpty() || !rout.isEmpty() || rodn != null) {
			DBAction ootypeRecord = database.table("OOTYPE").index("00").selection("OOOT35").build()
			DBContainer ootypeContainer = ootypeRecord.createContainer()
			ootypeContainer.setInt("OOCONO", cono)
			ootypeContainer.setString("OOORTP", ooheadContainer.getString("OAORTP"))
			if(ootypeRecord.read(ootypeContainer)) {
				if(ootypeContainer.getInt("OOOT35") == 4) {
					if(!ooheadContainer.get("OAADID").equals(adid) || !ooheadContainer.getString("OATEDL").equals(usedTedl) || !ooheadContainer.getString("OAMODL").equals(usedModl)
							|| !ooheadContainer.getString("OAROUT").equals(usedRout) || !ooheadContainer.getInt("OARODN") == usedRodn) {
						DBAction odheadRecord = database.table("ODHEAD").index("00").selection("UAORST").build()
						DBContainer odheadContainer = odheadRecord.createContainer()
						odheadContainer.setInt("UACONO", cono)
						odheadContainer.setString("UAORNO", orno)

						boolean checkDlyStatus = false

						odheadRecord.readAll(odheadContainer, 2, 1000, { DBContainer container ->
							int orst = Integer.parseInt(container.getString("UAORST"))
							if( orst >= 20 && orst <= 30) {
								checkDlyStatus = true
							}
						})

						if(checkDlyStatus) {
							if(!ooheadContainer.get("OAADID").equals(adid))
								mi.error(message.getMessage("S_00876", ["ADID"]))
							if(!ooheadContainer.getString("OATEDL").equals(usedTedl))
								mi.error(message.getMessage("S_00876", ["TEDL"]))
							if(!ooheadContainer.get("OAMODL").equals(usedModl))
								mi.error(message.getMessage("S_00876", ["MODL"]))
							if(!ooheadContainer.get("OAROUT").equals(usedRout))
								mi.error(message.getMessage("S_00876", ["ROUT"]))
							if(!ooheadContainer.get("OARODN").equals(usedRodn))
								mi.error(message.getMessage("S_00876", ["RODN"]))
							return
						}

					}
				}
			}
		}

		DBAction ocusadRecord = database.table("OCUSAD").index("00").build()
		DBContainer ocusadContainer = ocusadRecord.createContainer()
		ocusadContainer.setInt("OPCONO", cono)
		ocusadContainer.setString("OPADID", adid)
		ocusadContainer.setInt("OPADRT", adrt)
		switch (adrt) {
			case 1:
				ocusadContainer.setString("OPCUNO", ooheadContainer.getString("OAINRC"))
				break
			case 3:
				ocusadContainer.setString("OPCUNO", ooheadContainer.getString("OADECU"))
				break
			default:
				ocusadContainer.setString("OPCUNO", ooheadContainer.getString("OACUNO"))
				break
		}

		boolean foundCusad = ocusadRecord.read(ocusadContainer)
		if(!foundCusad && !adid.isBlank()) {
			//   MSGID=WAD1001 Address number &1 is invalid
			mi.error(message.getMessage("WAD1001", [adid]))
			return
		}

		if(adrt == 1 && !advi.isBlank()) {
			DBAction cishviRecord = database.table("CISHVI").index("00").build()
			DBContainer cishviContainer = cishviRecord.createContainer()
			cishviContainer.setInt("ILCONO", cono)
			cishviContainer.setString("ILADVI", advi)
			if(!cishviRecord.read(cishviContainer)) {
				//   MSGID=WAD2203 Via address &1 does not exist
				mi.error(message.getMessage("WAD2203", [advi]))
				return
			}
		}

		if(!wf01 && !dlsp.isEmpty() && !adid.isEmpty()) {
			DBAction ocusasRecord = database.table("OCUSAS").index("00").build()
			DBContainer ocusasContainer = ocusasRecord.createContainer()
			ocusasContainer.setInt("O2CONO", cono)
			ocusasContainer.setString("O2CUNO", ooheadContainer.getString("OADECU"))
			ocusasContainer.setString("O2ADID", adid)
			ocusasContainer.setString("O2DLSP", dlsp)

			if(!ocusasRecord.read(ocusasContainer)) {
				mi.error(message.getMessage("OI_0126", [dlsp]))
				return
			}

		}

		if(program.getLDAZD().get("MXDR") == 1) {
			if(adid.isBlank() && edes.isEmpty() && usedEdes.isBlank() && adrt == 1) {
				DBAction csytabRecord = database.table("CSYTAB").index("10").build()
				DBContainer csytabContainer = csytabRecord.createContainer()
				csytabContainer.setInt("CTCONO", cono)
				csytabContainer.setString("CTSTCO", "EDES")
				csytabContainer.setString("CTSTKY", usedEdes)
				if(!csytabRecord.read(csytabContainer)) {
					//   MSGID=OI_0114 Order type requires place in delivery address - place is
					mi.error(message.getMessage("OI_0114", null))
					return
				}
			}
		}

		DBAction oodfltRecord = database.table("OODFLT").index("00").selection("UJROUT","UJYREF").build()
		DBContainer oodfltContainer = oodfltRecord.createContainer()
		oodfltContainer.setInt("UJCONO", cono)
		oodfltContainer.setString("UJORTP", ooheadContainer.getString("OAORTP"))

		String defaultRoute = ""
		boolean foundOodflt = oodfltRecord.read(oodfltContainer)
		if(foundOodflt) {
			defaultRoute = oodfltContainer.getString("UJROUT")
		}

		if(!modl.isEmpty() || !edes.isEmpty() || !pono.isEmpty() || !ecar.isEmpty()) {
			if(adrt == 1) {
				if(!ooheadContainer.getString("OAADID").equals(adid) || !ooheadContainer.getString("OAMODL").equals(usedModl) ||
						!ooadreContainer.getString("ODEDES").equals(usedEdes) || !ooadreContainer.getString("ODPONO").equals(usedPono) ||
						!ooadreContainer.getString("ODECAR").equals(usedEcar)) {
					if(foundOodflt && ( defaultRoute.equals("*SYSH") || defaultRoute.equals("*MSYSH") )) {
						if(!validRoute(cono, usedRout, usedRodn, usedModl))
							return
					}
				}
			}
		}

		String xtcscd = ""
		String adft = ""
		String pvcmcn = ""
		String yhvpon = ""
		String xtecar = ""

		// Check that country code exist
		DBAction csytabRecord = database.table("CSYTAB").index("00").selection("CTTX15","CTPARM").build()
		DBContainer csytabContainer = csytabRecord.createContainer()
		csytabContainer.setInt("CTCONO", cono)
		csytabContainer.setString("CTSTCO", "CSCD")
		csytabContainer.setString("CTSTKY", usedCscd)
		boolean foundRecord = csytabRecord.read(csytabContainer)
		if(!foundRecord) {
			if(!cscd.isEmpty()) {
				mi.error(message.getMessage("WCS0103", [usedCscd]))
				return
			}
		}else {
			xtcscd = csytabContainer.getString("CTTX15")
			adft = csytabContainer.getString("CTPARM").substring(44, 46)
			pvcmcn = csytabContainer.getString("CTPARM").substring(41, 43)
			yhvpon = csytabContainer.getString("CTPARM").substring(49, 50)
		}


		// Check that country and state is a valid combination
		if(!ecar.isEmpty()) {
			DBAction csystsRecord = database.table("CSYSTS").index("00").selection("CKTX15").build()
			DBContainer csystsContainer = csystsRecord.createContainer()
			csystsContainer.setInt("CKCONO", cono)
			csystsContainer.setString("CKCSCD", usedCscd)
			csystsContainer.setString("CKECAR", usedEcar)
			if(!csystsRecord.read(csystsContainer)) {
				mi.error(message.getMessage("XEC0001", [usedEcar,usedCscd]))
				return
			}
			xtecar = csystsContainer.getString("CKTX15")
		}

		// Postal Code validation
		if(!pono.isEmpty() && yhvpon.equals("1")) {
			DBAction opotabRecord = database.table("OPOTAB").index("10").build()
			DBContainer opotabContainer = opotabRecord.createContainer()
			opotabContainer.setInt("OPCONO", cono)
			opotabContainer.setString("OPPONO", pono)
			int read = opotabRecord.readAll(opotabContainer,2, 1,{  })
			if(read == 0) {
				//  MSGID=S_01331 Postal code <> does not exists
				mi.error(message.getMessage("S_01331", [pono]))
				return
			}
			opotabContainer.setString("OPCSCD", usedCscd)
			read = opotabRecord.readAll(opotabContainer,3, 1,{  })
			if(read == 0) {
				// MSGID=S_01333 Postal code &1 must belong to the country &2
				mi.error(message.getMessage("S_01333", [pono,usedCscd]))
				return
			}
		}

		if(!frco.isBlank()) {
			DBAction uscntyRecord = database.table("USCNTY").index("00").build()
			DBContainer uscntyContainer = uscntyRecord.createContainer()
			uscntyContainer.setInt("CNCONO", cono)
			uscntyContainer.setString("CNCSCD", usedCscd)
			uscntyContainer.setString("CNECAR", usedEcar)
			uscntyContainer.setString("CNFRCO", frco)
			if(!uscntyRecord.read(uscntyContainer)) {
				//   MSGID=WSMC103 County &1 does not exist
				mi.error(message.getMessage("WSMC103", [frco]))
				return
			}
		}


		if(!cscd.isBlank() && !adft.isBlank()) {
			DBAction csyadfRecord = database.table("CSYADF").index("00").selection("CXADED","CXADE4","CXNOOC").build()
			DBContainer csyadfContainer = csyadfRecord.createContainer()
			csyadfContainer.setInt("CXCONO", cono)
			csyadfContainer.setString("CXADFT", adft)
			if(!csyadfRecord.read(csyadfContainer)) {
				mi.error(message.getMessage("WADF003", [adft]))
				return
			}

			String aded = csyadfContainer.getString("CXADED")
			String ade4 = csyadfContainer.getString("CXADE4")
			if( ecar.isBlank() && (aded.indexOf("&2") > -1 || ade4.indexOf("&2") > -1
					|| aded.indexOf("&6") > -1 || ade4.indexOf("&6") > -1) ) {
				mi.error(message.getMessage("WEC2402", null))
				return
			}
			if(pono.isBlank() && (aded.indexOf("&3") > -1 || ade4.indexOf("&3") > -1)) {
				mi.error(message.getMessage("WPO0102", null))
				return
			}
			if(town.isBlank() && (aded.indexOf("&1") > -1 || ade4.indexOf("&1") > -1)) {
				mi.error(message.getMessage("WTO0302", null))
				return
			}

			String formatedAdr3 = ""
			String formatedAdr4 = ""

			String iiso = csytabContainer.getString("CTPARM").substring(4,7)
			//Format address field
			if(!aded.isBlank()) {
				formatedAdr3 = formatAddress(aded, town, ecar, xtecar, pono, usedCscd, xtcscd, iiso, csyadfContainer.getInt("CXNOOC"))
			}

			if(!ade4.isBlank()) {
				formatedAdr4 = formatAddress(ade4, town, ecar, xtecar, pono, usedCscd, xtcscd, iiso, csyadfContainer.getInt("CXNOOC"))
			}

			if(!formatedAdr3.isBlank()) {
				cua3 = formatedAdr3
			}
			if(!formatedAdr4.isBlank()) {
				cua4 = formatedAdr4
			}
		}

		//   Check VAT registration depending on parm CRS082
		if(!vrno.isBlank() && !pvcmcn.isBlank() ) {
			//TODO CCHKFNB a trop de fonctions possible
		}

		if(geoc != null && geoc != 0) {
			DBAction cgeojuRecord = database.table("CGEOJU").index("00").build()
			DBContainer cgeojuContainer = cgeojuRecord.createContainer()
			cgeojuContainer.setInt("T0CONO", cono)
			cgeojuContainer.setInt("T0GEOC", geoc)
			if(!cgeojuRecord.read(cgeojuContainer)) {
				//   MSGID=WGE1003 Geographical code &1 does not exist
				mi.error(message.getMessage("WGE1003", [geoc.toString()]))
				return
			}
		}

		//   Validity on tax code
		if(!taxc.isBlank()) {
			csytabContainer.setInt("CTCONO", cono)
			csytabContainer.setString("CTSTCO", "TAXC")
			csytabContainer.setString("CTSTKY", taxc)
			if(!csytabRecord.read(csytabContainer)) {
				//   MSGID=WTA0301 Tax code &1 is invalid
				mi.error(message.getMessage("WTA0301", [taxc]))
				return
			}
		}

		if(!rout.isEmpty() || rodn != null || !modl.isEmpty()) {
			//   Validity on route departure
			if(usedRout.isBlank() && usedRodn == 0 && !defaultRoute.equals("*SYSH") && !defaultRoute.equals("*MSYSH") ||
					adrt == 3 ||
					adrt == 1 && usedRout.isBlank() && usedRodn == 0 && defaultRoute.equals("*SYSH") &&
					ooheadContainer.getString("OAROUT").equals(usedRout) && ooheadContainer.getInt("OARODN") == usedRodn) {

			}else if(adrt == 1) {
				if(!validRoute(cono, usedRout, usedRodn, usedModl))
					return
			}
		}


		if(!edes.isEmpty()) {
			//   Validity on place of unload
			if(adrt== 1 && program.getLDAZD().get("MXDR") == 1) {
				csytabRecord = database.table("CSYTAB").index("00").selection("CTPARM").build()
				csytabContainer = csytabRecord.createContainer()
				csytabContainer.setInt("CTCONO", cono)
				csytabContainer.setString("CTSTCO", "EDES")
				csytabContainer.setString("CTSTKY", edes)
				if(!csytabRecord.read(csytabContainer)) {
					//   MSGID=WWH0903 Place &1 does not exist
					mi.error(message.getMessage("WWH0903", [edes]))
					return
				}

				String yqcscd = csytabContainer.getString("CTPARM").substring(2,5)
				if(!usedCscd.equals(yqcscd) && !edes.isBlank()) {
					//   MSGID=OI00203 Place of unload must have same country code, namely &1
					mi.error(message.getMessage("OI00203", [usedCscd]))
					return
				}
			}
		}

		if(!modl.isEmpty()) {
			//   Validity on mode of delivery
			if(adrt!=3) {
				csytabRecord = database.table("CSYTAB").index("10").build()
				csytabContainer = csytabRecord.createContainer()
				csytabContainer.setInt("CTCONO", cono)
				csytabContainer.setString("CTSTCO", "MODL")
				csytabContainer.setString("CTSTKY", modl)
				csytabContainer.setString("CTLNCD", ooheadContainer.getString("OALNCD"))
				if(!csytabRecord.read(csytabContainer)) {
					//   MSGID=WWH0903 Place &1 does not exist
					mi.error(message.getMessage("WMO0103", [modl]))
					return
				}

			}
		}

		//   Validity ship via address - if delivery address
		//   Validity harbour/airport - if delivery address
		if(adrt != 3) {
			if(program.getLDAZD().get("ISTA") == 1) {
				if(!hafe.isBlank()) {
					csytabRecord = database.table("CSYTAB").index("00").build()
					csytabContainer = csytabRecord.createContainer()
					csytabContainer.setString("CTSTCO", "HAFE")
					csytabContainer.setString("CTSTKY", hafe)
					if(!csytabRecord.read(csytabContainer)) {
						//   MSGID=WHAFE03 Harbor or airport &1 does not exist
						mi.error(message.getMessage("WHAFE03", [hafe]))
						return
					}
				}

				//   Validity rail station - if delivery address
				if(!rasn.isBlank()) {
					DBAction usrwsnRecord = database.table("USRWSN").index("00").build()
					DBContainer usrwsnContainer = usrwsnRecord.createContainer()
					usrwsnContainer.setInt("RWCONO", cono)
					usrwsnContainer.setString("RWRASN", rasn)
					if(!usrwsnRecord.read(usrwsnContainer)) {
						//   MSGID=WRASN03 Rail station &1 does not exist
						mi.error(message.getMessage("WRASN03", [rasn]))
						return
					}
				}
			}

			if(!tedl.isBlank()) {
				//   Validity on terms of delivery
				csytabRecord = database.table("CSYTAB").index("00").build()
				csytabContainer = csytabRecord.createContainer()
				csytabContainer.setInt("CTCONO", cono)
				csytabContainer.setString("CTSTCO", "TEDL")
				csytabContainer.setString("CTSTKY", tedl)
				if(!csytabRecord.read(csytabContainer)) {
					//   MSGID=WTE0103 Delivery terms &1 does not exist
					mi.error(message.getMessage("WTE0103", [tedl]))
					return
				}
			}

			if(usedYref.isBlank() && adrt == 1 && !foundCusad && foundOodflt) {
				String ujyref = oodfltContainer.getString("UJYREF")
				if(ujyref.equals("*M") || ujyref.equals("*MYREF") || ujyref.equals("*MPHONO")) {
					//   MSGID=WYR0102 Your reference 1 must be entered
					mi.error(message.getMessage("WYR0102", null))
					return
				}
			}

		}

		ooadreContainer.setInt("ODCONO", cono)
		ooadreContainer.setString("ODORNO", orno)
		ooadreContainer.setInt("ODADRT", adrt)
		ooadreContainer.setString("ODADID", adid)
		boolean updatable = ooadreRecord.readLock(ooadreContainer, { LockedResult updateRecord ->
			if(!divi.isEmpty())
				updateRecord.setString("ODDIVI", divi)
			if(!cunm.isEmpty())
				updateRecord.setString("ODCUNM", cunm)
			if(!cua1.isEmpty())
				updateRecord.setString("ODCUA1", cua1)
			if(!cua2.isEmpty())
				updateRecord.setString("ODCUA2", cua2)
			if(!cua3.isEmpty())
				updateRecord.setString("ODCUA3", cua3)
			if(!cua4.isEmpty())
				updateRecord.setString("ODCUA4", cua4)
			if(!pono.isEmpty())
				updateRecord.setString("ODPONO", pono)
			if(!phno.isEmpty())
				updateRecord.setString("ODPHNO", phno)
			if(!tfno.isEmpty())
				updateRecord.setString("ODTFNO", tfno)
			if(!yref.isEmpty())
				updateRecord.setString("ODYREF", yref)
			if(!meal.isEmpty())
				updateRecord.setString("ODMEAL", meal)
			if(!cscd.isEmpty())
				updateRecord.setString("ODCSCD", cscd)
			if(!vrno.isEmpty())
				updateRecord.setString("ODVRNO", vrno)
			if(!edes.isEmpty())
				updateRecord.setString("ODEDES", edes)
			if(!rout.isEmpty())
				updateRecord.setString("ODROUT", rout)
			if(rodn != null)
				updateRecord.setInt("ODRODN", rodn)
			if(!ulzo.isEmpty())
				updateRecord.setString("ODULZO", ulzo)
			if(!ecf1.isEmpty())
				updateRecord.setString("ODECF1", ecf1)
			if(!ecf2.isEmpty())
				updateRecord.setString("ODECF2", ecf2)
			if(!ecf3.isEmpty())
				updateRecord.setString("ODECF3", ecf3)
			if(!ecf4.isEmpty())
				updateRecord.setString("ODECF4", ecf4)
			if(!ecf5.isEmpty())
				updateRecord.setString("ODECF5", ecf5)
			if(geoc != null)
				updateRecord.setInt("ODGEOC", geoc)
			if(!taxc.isEmpty())
				updateRecord.setString("ODTAXC", taxc)
			if(!ecar.isEmpty())
				updateRecord.setString("ODECAR", ecar)
			if(!hafe.isEmpty())
				updateRecord.setString("ODHAFE", hafe)
			if(!dlsp.isEmpty())
				updateRecord.setString("ODDLSP", dlsp)
			if(!dstx.isEmpty())
				updateRecord.setString("ODDSTX", dstx)
			if(!modl.isEmpty())
				updateRecord.setString("ODMODL", modl)
			if(!tedl.isEmpty())
				updateRecord.setString("ODTEDL", tedl)
			if(!tel2.isEmpty())
				updateRecord.setString("ODTEL2", tel2)
			if(!town.isEmpty())
				updateRecord.setString("ODTOWN", town)
			if(!advi.isEmpty())
				updateRecord.setString("ODADVI", advi)
			if(bop1 != null)
				updateRecord.setInt("ODBOP1", bop1)
			if(!frco.isEmpty())
				updateRecord.setString("ODFRCO", frco)
			if(!sple.isEmpty())
				updateRecord.setString("ODSPLE", sple)
			if(!rasn.isEmpty())
				updateRecord.setString("ODRASN", rasn)

			int CHNO = updateRecord.getInt("ODCHNO")
			if(CHNO== 999) {CHNO = 0}
			CHNO++
			updateRecord.set("ODLMDT", (Integer) utility.call("DateUtil", "currentDateY8AsInt"))
			updateRecord.set("ODCHID", program.getUser())
			updateRecord.set("ODCHNO", CHNO)
			updateRecord.update()
		})

		if(!updatable)
		{
			mi.error("La mise à jour à échouée.")
			return
		}
	}

	/**
	 * Validate the route
	 * @param cono
	 * @param rout
	 * @param rodn
	 * @param modl
	 * @return true if valid
	 */
	private boolean validRoute(int cono, String rout, int rodn, String modl) {
		if(rout.isBlank()) {
			//   MSGID=WRO1302 Route must be entered
			mi.error(message.getMessage("WRO1302", null))
			return false
		}
		DBAction drouteRecord = database.table("DROUTE").index("00").selection(null).build()
		DBContainer drouteContainer = drouteRecord.createContainer()
		drouteContainer.setInt("DRCONO", cono)
		drouteContainer.setString("DRROUT", rout)
		if(!drouteRecord.read(drouteContainer)) {
			//   MSGID=WRO1303 Route &1 does not exist
			mi.error(message.getMessage("WRO1303", [rout]))
			return false
		}
		if(drouteContainer.getInt("DRINOU") == 1) {
			//   MSGID=WRO1301 Route &1 is invalid
			mi.error(message.getMessage("WRO1301", [rout]))
			return false
		}

		if(rodn != null && rodn != 0) {
			DBAction droudiRecord = database.table("DROUDI").index("00").selection(null).build()
			DBContainer droudiContainer = droudiRecord.createContainer()
			droudiContainer.setInt("DSCONO", cono)
			droudiContainer.setString("DSROUT", rout)
			droudiContainer.setInt("DSRODN", rodn)

			if(!droudiRecord.read(droudiContainer)) {
				//   MSGID=WROD103 Route departure &1 does not exist
				mi.error(message.getMessage("WROD103", [rodn.toString()]))
				return false
			}

			if(modl.isBlank()) {
				modl = droudiContainer.getString("DSMMDL")
			}

			if(!droudiContainer.getString("DSMMDL").equals(modl)) {
				mi.error(message.getMessage("WROD101", [modl]))
				return false
			}
		}
		return true
	}

	/**
	 * Format address field
	 * @param addressLine address line to format
	 * @param town
	 * @param ecar
	 * @param pono
	 * @param cscd
	 * @param xtcscd
	 * @param xtecar
	 * @param iiso
	 * @param nooc
	 * @return formated address
	 */
	private String formatAddress(String addressLine, String town, String ecar, String txtecar, String pono, String cscd, String txtcscd, String iiso, int nooc) {
		StringBuilder xxadrn = new StringBuilder()
		int j =0
		boolean xxduck = false
		for (int i = 0; i < addressLine.length(); i++) {
			if (addressLine.charAt(i) == '&') {
				xxduck = true
			} else {
				if (xxduck) {
					switch (addressLine.charAt(i)) {
						case '1':
							xxadrn.append(town)
							j += town.length()
							break
						case '2':
							xxadrn.append(ecar)
							j +=  ecar.length()
							break
						case '3':
							xxadrn.append(pono)
							j += pono.length()
							break
						case '4':
							xxadrn.append(txtcscd)
							j += txtcscd.length()
							break
						case '5':
							xxadrn.append(iiso.substring(0, nooc))
							j += nooc
							break
						case '6':
							xxadrn.append(txtecar)
							j += txtecar.length()
							break
						case '7':
							xxadrn.append(cscd)
							j += cscd.length()
							break
						default:
							xxadrn.append(addressLine.substring(i, i + 1))
							j += 1
					}
					xxduck = false
				} else {
					xxadrn.append(addressLine.substring(i, i + 1))
					j += 1
				}
			}
		}
		return xxadrn.toString()
	}
}