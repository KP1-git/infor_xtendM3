/****************************************************************************************
 Extension Name: EXT103MI/AddHead
 Type: ExtendM3Transaction
 Script Author: FERRE Adrien
 Date: 26/02/2026
 Description: Add record FGDISH
    
 Revision History:
 Name                    		Date             Version          Description of Changes
 First creation FERRE Adrien 	26/02/2026       1.0              Creation 
 d.decosterd                    23/03/2026       1.1              Add missing return after some mi.error, replace def by List<String> and use rdri insteadof divi for CARDRI value
******************************************************************************************/

public class AddHead extends ExtendM3Transaction {
	private final MIAPI mi
	private final ProgramAPI program
	private final DatabaseAPI database
	private final UtilityAPI utility
	private final MICallerAPI miCaller

	/*
	 * Transaction EXT103MI/AddHead Interface
	 * @param mi - Infor MI Interface
	 * @param database - Infor Database Interface
	 * @param utility - Utility Interface
	 * @program program - ProgramAPI Interface
	 * @MICallerAPI - MICallerAPI Interface
	 */
	public AddHead(MIAPI mi, DatabaseAPI database, UtilityAPI utility, ProgramAPI program, MICallerAPI miCaller) {
		this.mi = mi
		this.program = program
		this.database = database
		this.utility = utility
		this.miCaller = miCaller

	}

	public void main() {
		Integer cono = mi.in.get("CONO")
		String  divi = (mi.inData.get("DIVI") == null) ? "" : mi.inData.get("DIVI").trim()
		String  stab = (mi.inData.get("STAB") == null) ? "" : mi.inData.get("STAB").trim()
		String  tx40 = (mi.inData.get("TX40") == null) ? "" : mi.inData.get("TX40").trim()
		String  tx15 = (mi.inData.get("TX15") == null) ? "" : mi.inData.get("TX15").trim()
		String  aih1 = (mi.inData.get("AIH1") == null) ? " " : mi.inData.get("AIH1")
		String  aih2 = (mi.inData.get("AIH2") == null) ? " " : mi.inData.get("AIH2")
		String  aih3 = (mi.inData.get("AIH3") == null) ? " " : mi.inData.get("AIH3")
		String  aih4 = (mi.inData.get("AIH4") == null) ? " " : mi.inData.get("AIH4")
		String  aih5 = (mi.inData.get("AIH5") == null) ? " " : mi.inData.get("AIH5")
		String  aih6 = (mi.inData.get("AIH6") == null) ? " " : mi.inData.get("AIH6")
		String  aih7 = (mi.inData.get("AIH7") == null) ? " " : mi.inData.get("AIH7")
		String  rdri = (mi.inData.get("RDRI") == null) ? "" : mi.inData.get("RDRI").trim()

		List<String> aihList = [aih1, aih2, aih3, aih4, aih5, aih6, aih7]

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

		if(stab.isBlank()) {
			mi.error("Table de base d'affectation est obligatoire")
			return
		}

		if(tx40.isBlank()) {
			mi.error("Désignation est obligatoire")
			return
		}

		aihList.eachWithIndex { value, index ->
			if (!(value?.trim() == "" || value == "+")) {
				mi.error("Valeur invalide pour aih${index + 1} : '${value}'")
				return
			}
		}

		if (!aihList.any { it == "+" }) {
			mi.error("Aucune des variables aih1 à aih7 ne contient '+'")
			return
		}

		if(!rdri.isBlank()) {
			DBAction fcavdfRecord = database.table("FCAVDF").index("00").build()
			DBContainer fcavdfContainer = fcavdfRecord.createContainer()
			fcavdfContainer.setInt("CACONO", cono)
			fcavdfContainer.setString("CADIVI", divi)
			fcavdfContainer.setString("CARDRI", rdri)
			if(!fcavdfRecord.read(fcavdfContainer)){
				mi.error("RDRI n'éxiste pas.")
				return
			}
		}

		DBAction fgdishRecord = database.table("FGDISH").index("00").build()
		DBContainer fgdishContainer = fgdishRecord.createContainer()
		fgdishContainer.setInt("BFCONO", cono)
		fgdishContainer.setString("BFDIVI", divi)
		fgdishContainer.setString("BFSTAB", stab)
		
		if(!fgdishRecord.read(fgdishContainer)){
			fgdishContainer.setString("BFTX40", tx40)
			fgdishContainer.setString("BFTX15", tx15)
			fgdishContainer.setChar("BFAIH1", aih1.toCharacter())
			fgdishContainer.setChar("BFAIH2", aih2.toCharacter())
			fgdishContainer.setChar("BFAIH3", aih3.toCharacter())
			fgdishContainer.setChar("BFAIH4", aih4.toCharacter())
			fgdishContainer.setChar("BFAIH5", aih5.toCharacter())
			fgdishContainer.setChar("BFAIH6", aih6.toCharacter())
			fgdishContainer.setChar("BFAIH7", aih7.toCharacter())
			fgdishContainer.setString("BFRDRI", rdri)
			fgdishContainer.set("BFRGDT", (Integer) utility.call("DateUtil", "currentDateY8AsInt"))
			fgdishContainer.set("BFLMDT", (Integer) utility.call("DateUtil", "currentDateY8AsInt"))
			fgdishContainer.set("BFCHID", program.getUser())
			fgdishContainer.set("BFRGTM", (Integer) utility.call("DateUtil", "currentTimeAsInt"))
			fgdishContainer.set("BFCHNO", 1)
			fgdishRecord.insert(fgdishContainer)
		}else
		{
			mi.error("Enregistrement existe déjà.")
			return
		}


	}
}
