/**
 * README
 *
 * Name: EXT103MI.AddLine
 * Description: Add a record in FGDISD
 * Date                         Changed By                    Description
 * 20250620                     a.ferre@hetic3.fr     		création
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
		String  stab = (mi.inData.get("STAB") == null) ? "" : mi.inData.get("STAB").trim()
		Integer bbln = mi.in.get("BBLN")
		String  tx40 = (mi.inData.get("TX40") == null) ? "" : mi.inData.get("TX40").trim()
		String  sfa1 = (mi.inData.get("SFA1") == null) ? "" : mi.inData.get("SFA1").trim()
		String  sfa2 = (mi.inData.get("SFA2") == null) ? "" : mi.inData.get("SFA2").trim()
		String  sfa3 = (mi.inData.get("SFA3") == null) ? "" : mi.inData.get("SFA3").trim()
		String  sfa4 = (mi.inData.get("SFA4") == null) ? "" : mi.inData.get("SFA4").trim()
		String  sfa5 = (mi.inData.get("SFA5") == null) ? "" : mi.inData.get("SFA5").trim()
		String  sfa6 = (mi.inData.get("SFA6") == null) ? "" : mi.inData.get("SFA6").trim()
		String  sfa7 = (mi.inData.get("SFA7") == null) ? "" : mi.inData.get("SFA7").trim()
		String  sta1 = (mi.inData.get("STA1") == null) ? "" : mi.inData.get("STA1").trim()
		String  sta2 = (mi.inData.get("STA2") == null) ? "" : mi.inData.get("STA2").trim()
		String  sta3 = (mi.inData.get("STA3") == null) ? "" : mi.inData.get("STA3").trim()
		String  sta4 = (mi.inData.get("STA4") == null) ? "" : mi.inData.get("STA4").trim()
		String  sta5 = (mi.inData.get("STA5") == null) ? "" : mi.inData.get("STA5").trim()
		String  sta6 = (mi.inData.get("STA6") == null) ? "" : mi.inData.get("STA6").trim()
		String  sta7 = (mi.inData.get("STA7") == null) ? "" : mi.inData.get("STA7").trim()
		String  obf1 = (mi.inData.get("OBF1") == null) ? "" : mi.inData.get("OBF1").trim()
		String  obf2 = (mi.inData.get("OBF2") == null) ? "" : mi.inData.get("OBF2").trim()
		String  obf3 = (mi.inData.get("OBF3") == null) ? "" : mi.inData.get("OBF3").trim()
		String  obt1 = (mi.inData.get("OBT1") == null) ? "" : mi.inData.get("OBT1").trim()
		String  obt2 = (mi.inData.get("OBT2") == null) ? "" : mi.inData.get("OBT2").trim()
		String  obt3 = (mi.inData.get("OBT3") == null) ? "" : mi.inData.get("OBT3").trim()
		
		String xxsta1 = ""
		String xxsta2 = ""
		String xxsta3 = ""
		String xxsta4 = ""
		String xxsta5 = ""
		String xxsta6 = ""
		String xxsta7 = ""
		String xxobt1 = ""
		String xxobt2 = ""
		String xxobt3 = ""
		
		
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

		if(stab.isBlank()) {
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
		
		if(sta1.isBlank()) {
			xxsta1 = sb
		}else {
			xxsta1 = sta1
		}

		if(sta2.isBlank()) {
			xxsta2 = sb
		}else {
			xxsta2 = sta2
		}

		if(sta3.isBlank()) {
			xxsta3 = sb
		}else {
			xxsta3 = sta3
		}

		if(sta4.isBlank()) {
			xxsta4 = sb
		}else {
			xxsta4 = sta4
		}

		if(sta5.isBlank()) {
			xxsta5 = sb
		}else {
			xxsta5 = sta5
		}

		if(sta6.isBlank()) {
			xxsta6 = sb
		}else {
			xxsta6 = sta6
		}

		if(sta7.isBlank()) {
			xxsta7 = sb
		}else {
			xxsta7 = sta7
		}
		
		if(obt1.isBlank()) {
			xxobt1 = sb
		}else {
			xxobt1 = obt1
		}
		
		if(obt2.isBlank()) {
			xxobt2 = sb
		}else {
			xxobt2 = obt2
		}
		
		
		if(obt3.isBlank()) {
			xxobt3 = sb
		}else {
			xxobt3 = obt3
		}
		
		
		
		if (sfa1.compareTo(xxsta1) > 0) {
			mi.error("La valeur From AIT1 est supérieur a la veleur TO AIT1.")
			return
		}

		if (sfa2.compareTo(xxsta2) > 0) {
			mi.error("La valeur From AIT2 est supérieur a la veleur TO AIT2.")
			return
		}

		if (sfa3.compareTo(xxsta3) > 0) {
			mi.error("La valeur From AIT3 est supérieur a la veleur TO AIT3.")
			return
		}

		if (sfa4.compareTo(xxsta4) > 0) {
			mi.error("La valeur From AIT4 est supérieur a la veleur TO AIT4.")
			return
		}

		if (sfa5.compareTo(xxsta5) > 0) {
			mi.error("La valeur From AIT5 est supérieur a la veleur TO AIT5.")
			return
		}

		if (sfa6.compareTo(xxsta6) > 0) {
			mi.error("La valeur From AIT6 est supérieur a la veleur TO AIT6.")
			return
		}

		if (sfa7.compareTo(xxsta7) > 0) {
			mi.error("La valeur From AIT7 est supérieur a la veleur TO AIT7.")
			return
		}
		
		if (obf1.compareTo(xxobt1) > 0) {
			mi.error("La valeur From OBF1 est supérieur a la veleur TO OBT1.")
			return
		}
		
		if (obf2.compareTo(xxobt2) > 0) {
			mi.error("La valeur From OBF2 est supérieur a la veleur TO OBT2.")
			return
		}
		
		if (obf2.compareTo(xxobt3) > 0) {
			mi.error("La valeur From OBF3 est supérieur a la veleur TO OBT3.")
			return
		}
		
		DBAction fgdishRecord = database.table("FGDISH").index("00").build()
		DBContainer fgdishContainer = fgdishRecord.createContainer()
		fgdishContainer.setInt("BFCONO", cono)
		fgdishContainer.setString("BFDIVI", divi)
		fgdishContainer.setString("BFSTAB", stab)
		if(!fgdishRecord.read(fgdishContainer)) {
			mi.error("Aucune entête trouvée.")
			return
		}
		
		DBAction fgdisdRecord = database.table("FGDISD").index("00").build()
		DBContainer fgdibdContainer = fgdisdRecord.createContainer()
		fgdibdContainer.setInt("BGCONO", cono)
		fgdibdContainer.setString("BGDIVI", divi)
		fgdibdContainer.setString("BGSTAB", stab)
		fgdibdContainer.setInt("BGBBLN", bbln)
		
		if(!fgdisdRecord.read(fgdibdContainer)){
			fgdibdContainer.setString("BGTX40", tx40)
			fgdibdContainer.setString("BGSFA1", sfa1)
			fgdibdContainer.setString("BGSFA2", sfa2)
			fgdibdContainer.setString("BGSFA3", sfa3)
			fgdibdContainer.setString("BGSFA4", sfa4)
			fgdibdContainer.setString("BGSFA5", sfa5)
			fgdibdContainer.setString("BGSFA6", sfa6)
			fgdibdContainer.setString("BGSFA7", sfa7)
			fgdibdContainer.setString("BGSTA1", xxsta1)
			fgdibdContainer.setString("BGSTA2", xxsta2)
			fgdibdContainer.setString("BGSTA3", xxsta3)
			fgdibdContainer.setString("BGSTA4", xxsta4)
			fgdibdContainer.setString("BGSTA5", xxsta5)
			fgdibdContainer.setString("BGSTA6", xxsta6)
			fgdibdContainer.setString("BGSTA7", xxsta7)
			fgdibdContainer.setString("BGOBF1", obf1)
			fgdibdContainer.setString("BGOBF2", obf2)
			fgdibdContainer.setString("BGOBF3", obf3)
			fgdibdContainer.setString("BGOBT1", xxobt1)
			fgdibdContainer.setString("BGOBT2", xxobt2)
			fgdibdContainer.setString("BGOBT3", xxobt3)
			
			fgdibdContainer.set("BGRGDT", (Integer) utility.call("DateUtil", "currentDateY8AsInt"))
			fgdibdContainer.set("BGLMDT", (Integer) utility.call("DateUtil", "currentDateY8AsInt"))
			fgdibdContainer.set("BGCHID", program.getUser())
			fgdibdContainer.set("BGRGTM", (Integer) utility.call("DateUtil", "currentTimeAsInt"))
			fgdibdContainer.set("BGCHNO", 1)
			fgdisdRecord.insert(fgdibdContainer)
		}else
		{
			mi.error("Enregistrement existe déjà.")
			return
		}
		
		
	}
}