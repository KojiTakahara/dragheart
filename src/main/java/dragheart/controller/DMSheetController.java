package dragheart.controller;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.mashape.unirest.http.Unirest;
import dragheart.service.DmgpPdfService;
import dragheart.service.PdfService;
import dragheart.service.ShitamatiPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.http.*;

@RestController
@RequestMapping("/dmSheet")
public class DMSheetController {

    @Autowired
    private PdfService pdfService;
    @Autowired
    private DmgpPdfService dmgpPdfService;
    @Autowired
    private ShitamatiPdfService shitamatiPdfService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void doPost(@RequestParam String playerName,
                       @RequestParam String[] mainDeck,
                       @RequestParam(required = false) String[] hyperSpatial,
                       @RequestParam(required = false) String format,
                       @RequestParam(required = false) String deckId,
                       HttpServletResponse res) throws IOException {
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

            if (getPdfFileNameByFormat(format).equals("dmgp-1st-entrysheet.pdf")) {
                pdfService.writeText(over, bf, playerName, 375, 792);
                dmgpPdfService.writeMainDeck(over, bf, mainDeck);
                dmgpPdfService.writeHyperSpatial(over, bf, hyperSpatial);
            } else {
                pdfService.writeText(over, bf, playerName, 280, 798);
                pdfService.writeMainDeck(over, bf, mainDeck);
                pdfService.writeHyperSpatial(over, bf, hyperSpatial);
            }
            over.endText();
            pdfStamper.close();
            callApi(mainDeck, hyperSpatial, format, deckId);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        res.setContentType("application/pdf");
    }

    /**
     * formatに合わせたPDFファイル名を返す
     */
    private String getPdfFileNameByFormat(String format) {
        if (format == null || format.equals("")) {
            return "decksheet.pdf";
        }
        if (format.equals("dmgp_01")) {
            return "dmgp-1st-entrysheet.pdf";
        }
        return "decksheet.pdf";
    }

    @RequestMapping(value = "/kousien", method = RequestMethod.POST)
    public void kousien(@RequestParam String playerName,
                        @RequestParam String[] mainDeck,
                        @RequestParam String[] mainColors,
                        @RequestParam(required = false) String[] hyperSpatial,
                        @RequestParam(required = false) String[] hyperColors,
                        HttpServletResponse res) throws IOException {
        if (!pdfService.validate(mainDeck, hyperSpatial)) {
            res.setStatus(400);
            return;
        }
        try {
            PdfReader reader = new PdfReader("shitamati.pdf");
            PdfStamper pdfStamper = new PdfStamper(reader, res.getOutputStream());
            BaseFont bf = BaseFont.createFont("HeiseiKakuGo-W5", "UniJIS-UCS2-H", BaseFont.NOT_EMBEDDED);
            PdfContentByte over = pdfStamper.getOverContent(1);
            over.beginText();
            shitamatiPdfService.writeText(over, bf, playerName, 280, 798);
            shitamatiPdfService.writeMainDeck(over, bf, mainDeck);
            shitamatiPdfService.writeMainColors(over, bf, mainColors);
            shitamatiPdfService.writeHyperSpatial(over, bf, hyperSpatial);
            shitamatiPdfService.writeHyperColors(over, bf, hyperColors);
            over.endText();
            pdfStamper.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        res.setContentType("application/pdf");
    }

    private void callApi(String[] mainDeck, String[] hyperSpatial, String format, String deckId) {
        Unirest.post("http://localhost:8080/api/deck")
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
//                .field("mainDeck", mainDeck)
//                .field("hyperSpatial", hyperSpatial)
//                .field("format", format)
//                .field("deckId", deckId)
                .body("{" +
                        "\"format\":\"" + format + "\"," +
                        "\"mainDeck\":\"" + Arrays.toString(mainDeck) + "\"," +
                        "\"hyperSpatial\":\"" + Arrays.toString(hyperSpatial) + "\"," +
                        "\"deckId\":\"" + deckId + "\"" +
                        "}")
                .asJsonAsync();
    }

}
