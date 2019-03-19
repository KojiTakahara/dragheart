package dragheart.controller;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.mashape.unirest.http.Unirest;
import dragheart.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.servlet.http.*;

@RestController
@RequestMapping("/dmSheet")
public class DMSheetController {

    @Autowired
    private PdfService pdfService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void doPost(@RequestParam String name,
                       @RequestParam(required = false) String nameKana,
                       @RequestParam(required = false) String id,
                       @RequestParam String[] mainDeck,
                       @RequestParam(required = false) String[] hyperSpatial,
                       @RequestParam(required = false) String[] hyperGR,
                       @RequestParam(required = false) boolean forbiddenStar,
                       @RequestParam(required = false) boolean teamSheet,
                       @RequestParam(required = false) boolean teamName,
                       @RequestParam(required = false) String seat,
                       HttpServletResponse res) throws IOException {
        if (hyperSpatial == null) {
            hyperSpatial = new String[0];
        }
        if (hyperGR == null) {
            hyperGR = new String[0];
        }
        if (!pdfService.validate(mainDeck, hyperSpatial, hyperGR)) {
            this.validateMessage(res);
            return;
        }
        try {
            PdfReader reader = new PdfReader(getPdfFileNameByFormat(teamSheet));
            PdfStamper pdfStamper = new PdfStamper(reader, res.getOutputStream());
            BaseFont bf = BaseFont.createFont("HeiseiKakuGo-W5", "UniJIS-UCS2-H", BaseFont.NOT_EMBEDDED);
            PdfContentByte over = pdfStamper.getOverContent(1);
            over.beginText();
            pdfService.writeName(over, bf, name);
            pdfService.writeNameKana(over, bf, nameKana);
            pdfService.writeId(over, bf, id);
            pdfService.writeMainDeck(over, bf, mainDeck);
            pdfService.writeHyperSpatial(over, bf, hyperSpatial);
            pdfService.writeHyperGR(over, bf, hyperGR);
            pdfService.writeForbiddenStar(over, bf, forbiddenStar);
            over.endText();
            pdfStamper.close();
            // callApi(playerName, mainDeck, hyperSpatial, format, deckId);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        res.setContentType("application/pdf");
    }

    /**
     * PDFファイル名を返す
     */
    private String getPdfFileNameByFormat(Boolean teamSheet) {
        if (teamSheet) {
            return "decksheet_team.pdf";
        }
        return "decksheet_single.pdf";
    }

    private void callApi(String playerName, String[] mainDeck, String[] hyperSpatial, String format) {
        Unirest.post("http://1-1-0.dm-decksheet.appspot.com/api/deck")
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .field("mainDeck", Arrays.toString(mainDeck))
                .field("hyperSpatial", Arrays.toString(hyperSpatial))
                .field("format", format)
                .field("playerName", playerName)
                .asJsonAsync();
    }

    private void validateMessage(HttpServletResponse res) throws IOException {
        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"status\":400,\"error\":\"Bad Request\",");
        sb.append("\"message\":\"Parameter is invalid\"");
        sb.append("}");
        PrintWriter out = res.getWriter();
        out.println(new String(sb));
        out.close();
        res.setContentType("application/json");
    }

}
