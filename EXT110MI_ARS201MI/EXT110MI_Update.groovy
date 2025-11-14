import java.time.LocalDate
import java.time.format.DateTimeFormatter
/**
 * README
 *
 * Name: EXT110MI.Update
 * Description: Update RVDT and SMCD on customer invoice
 * Date                         Changed By                    Description
 * 20251027                     d.decosterd@hetic3.fr     		création
 */

public class Update extends ExtendM3Transaction {
	private final MIAPI mi
	private final DatabaseAPI database
	private final ProgramAPI program
	private final MessageAPI message

	public Update(MIAPI mi, DatabaseAPI database, ProgramAPI program, MessageAPI message) {
		this.mi = mi
		this.database = database
		this.program = program
		this.message = message
	}

	public void main() {
		String  divi = (mi.inData.get("DIVI") == null) ? "" : mi.inData.get("DIVI").trim()
		String  pyno = (mi.inData.get("PYNO") == null) ? "" : mi.inData.get("PYNO").trim()
		String  cuno = (mi.inData.get("CUNO") == null) ? "" : mi.inData.get("CUNO").trim()
		String  cino = (mi.inData.get("CINO") == null) ? "" : mi.inData.get("CINO").trim()
		Integer inyr = mi.in.get("INYR")
		Integer rvdt = mi.in.get("RVDT")
		String  smcd = (mi.inData.get("SMCD") == null) ? "" : mi.inData.get("SMCD").trim()

		int cono = program.LDAZD.get("CONO")

		DBAction cmmdivRecord = database.table("CMNDIV").index("00").build()
		DBContainer cmmdivContainer = cmmdivRecord.createContainer()
		cmmdivContainer.setInt("CCCONO", cono)
		cmmdivContainer.setString("CCDIVI", divi)
		if(!cmmdivRecord.read(cmmdivContainer)) {
			// MSGID=WDI0103 Division &1 does not exist
			String errorMessage = message.getMessage("WDI0103", [divi])
			mi.error(errorMessage)
			return
		}

		DBAction fsledgRecord = database.table("FSLEDG").index("10").selection("ESRECO","ESIIST","ESCUAM","ESCHNO").build()
		DBContainer fsledgContainer = fsledgRecord.createContainer()
		fsledgContainer.setInt("ESCONO", cono)
		fsledgContainer.setString("ESDIVI", divi)
		fsledgContainer.setString("ESPYNO", pyno)
		fsledgContainer.setString("ESCINO", cino)
		fsledgContainer.setInt("ESINYR", inyr)
		fsledgContainer.setString("ESCUNO", cuno)

		if(fsledgRecord.readAll(fsledgContainer, 6, 1, {}) == 0) {
			// MSGID=XRE0103 Record does not exist
			String errorMessage = message.getMessage("XRE0103", [])
			mi.error(errorMessage)
			return

		}

		if(rvdt == null && smcd.isBlank()) {
			mi.error("Veuillez saisir un représentant ou une date d'échéance.")
			return
		}

		if(!smcd.isBlank()) {
			DBAction csytabRecord = database.table("CSYTAB").index("00").build()
			DBContainer csytabContainer = csytabRecord.createContainer()
			csytabContainer.setInt("CTCONO", cono)
			csytabContainer.setString("CTSTCO", "SMCD")
			csytabContainer.setString("CTSTKY", smcd)

			if(!csytabRecord.read(csytabContainer)) {
				mi.error("Le code représentant"+smcd+" n'existe pas.")
				return
			}
		}

		double invoiceTotal = 0d

		fsledgRecord.readAll(fsledgContainer, 5, 1000, {  DBContainer container ->
			// Calculate not reversed records

			if (!(container.getInt("ESRECO") == 9 && container.getInt("ESIIST") == 8)) {
				invoiceTotal += container.getDouble("ESCUAM")
			}
		})

		if( invoiceTotal < 1e-2) {
			// MSGID=AR20109 The invoice is paid
			String errorMessage = message.getMessage("AR20109", [])
			mi.error(errorMessage)
			return
		}

		int today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")).toInteger()

		fsledgRecord.readAll(fsledgContainer, 6, 1000, {  DBContainer container ->
			DBAction fsledgUpdateRecord = database.table("FSLEDG").index("10").build()
			fsledgUpdateRecord.readLock(container, {  LockedResult entry ->
				if(rvdt != null)
					entry.setInt("ESRVDT", rvdt)
				if(!smcd.isBlank())
					entry.setString("ESSMCD", smcd)
				int CHNO = entry.getInt("ESCHNO")
				if(CHNO == 999) {CHNO = 0}
				CHNO++
				entry.setInt("ESLMDT", today)
				entry.setString("ESCHID", program.getUser())
				entry.setInt("ESCHNO", CHNO)
				entry.update()
			})

		})
	}
}
