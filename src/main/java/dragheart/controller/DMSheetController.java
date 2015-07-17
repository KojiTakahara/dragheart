package dragheart.controller;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import dragheart.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import javax.servlet.http.*;

@RestController
@RequestMapping("/dmSheet")
public class DMSheetController {

    @Autowired
    private PdfService pdfService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void doPost(@RequestParam String playerName, @RequestParam String[] mainDeck, @RequestParam(required = false) String[] hyperSpatial, @RequestParam(required = false) String format, HttpServletResponse res) throws IOException {
        if (!pdfService.validate(mainDeck, hyperSpatial)) {
            res.setStatus(400);
            return;
        }
        try {
            PdfReader reader = new PdfReader(getPdfFileNameByFormat(format));
            PdfStamper pdfStamper = new PdfStamper(reader, res.getOutputStream());
            BaseFont bf = BaseFont.createFont("HeiseiKakuGo-W5", "UniJIS-UCS2-H", BaseFont.NOT_EMBEDDED);
            PdfContentByte over = pdfStamper.getOverContent(1);
            over.beginText();
            pdfService.writeText(over, bf, playerName, 280, 798);
            pdfService.writeMainDeck(over, bf, mainDeck);
            pdfService.writeHyperSpatial(over, bf, hyperSpatial);
            over.endText();
            pdfStamper.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        res.setContentType("application/pdf");
    }

    /**
     * formatに合わせたPDFファイル名を返す
     */
    private String getPdfFileNameByFormat(String format) {
        if (format == null || format == "") {
            return "decksheet.pdf";
        }
        if (format == "dmgp_01") {
            return "decksheet_dmgp_01.pdf";
        }
        return "decksheet.pdf";
    }

}
