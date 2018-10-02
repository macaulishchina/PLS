package top.macaulish.spring.view

import com.lowagie.text.Document
import com.lowagie.text.pdf.PdfWriter
import org.springframework.web.servlet.view.document.AbstractPdfView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import com.lowagie.text.pdf.PdfPTable


/**
 *@author huyidong
 *@date 2018/10/2
 */
class PdfViewTemplate : AbstractPdfView() {
    override fun buildPdfDocument(model: MutableMap<String, Any>, document: Document, writer: PdfWriter, request: HttpServletRequest, response: HttpServletResponse) {
        val person = model["person"] as Person

        val table = PdfPTable(4)

        table.addCell("Person Id")
        table.addCell("First Name")
        table.addCell("Last Name")
        table.addCell("Age")

        table.addCell(person.personId.toString())
        table.addCell(person.firstName)
        table.addCell(person.lastName)
        table.addCell(person.age.toString())

        document.add(table)
    }

    class Person(val personId: Int, val firstName: String, val lastName: String, val age: Int)

}