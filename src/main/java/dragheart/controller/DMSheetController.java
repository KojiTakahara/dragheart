package dragheart.controller;

import com.mashape.unirest.http.Unirest;
import dragheart.service.PdfService;

import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.multipdf.Overlay;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.servlet.http.*;

@RestController
@RequestMapping("/dmSheet")
public class DMSheetController {

    @Autowired
    private PdfService pdfService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void doPost(
            @RequestParam String name,
            @RequestParam(required = false) String nameKana,
            @RequestParam(required = false) String id,
            @RequestParam String[] mainDeck,
            @RequestParam(required = false) String[] hyperSpatial,
            @RequestParam(required = false) String[] hyperGR,
            @RequestParam(required = false) boolean forbiddenStar,
            @RequestParam(required = false) boolean teamSheet,
            @RequestParam(required = false) boolean teamName,
            @RequestParam(required = false) String seat,
            @RequestParam(required = false) boolean image,
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
            String tempPath = "temp.pdf";
            PDDocument tmp = new PDDocument();
            PDRectangle rectangle = PDRectangle.A4;
            PDPage tmpPage = new PDPage(rectangle);
            tmp.addPage(tmpPage);

            PDPageContentStream contentStream = new PDPageContentStream(tmp, tmpPage);
            PDFont font = getFont(tmp);
            contentStream.beginText();
            pdfService.writeName(contentStream, font, name);
            pdfService.writeNameKana(contentStream, font, nameKana);
            pdfService.writeId(contentStream, font, id);
            pdfService.writeMainDeck(contentStream, font, mainDeck);
            pdfService.writeHyperSpatial(contentStream, font, hyperSpatial);
            pdfService.writeHyperGR(contentStream, font, hyperGR);
            pdfService.writeForbiddenStar(contentStream, font, forbiddenStar);
            contentStream.endText();
            contentStream.close();
            tmp.save(tempPath);
            tmp.close();

            PDDocument document = getFile(getPdfFileNameByFormat(teamSheet));
            HashMap<Integer, String> overlayGuide = new HashMap<Integer, String>();
            overlayGuide.put(1, tempPath);
            Overlay overlay = new Overlay();
            overlay.setInputPDF(document);
            overlay.setOverlayPosition(Overlay.Position.FOREGROUND);
            overlay.overlay(overlayGuide);

            setOutputStream(image, document, res.getOutputStream());
            overlay.close();
            document.close();
            // callApi(playerName, mainDeck, hyperSpatial, format, deckId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        res.setContentType(image ? "image/png" : "application/pdf");
    }

    /**
     * 出力するデータのセット
     * imageがtrueの場合, PNG形式. それ以外の場合, PDF形式で出力する
     */
    private void setOutputStream(boolean image, PDDocument document, OutputStream os) throws IOException {
        if (image) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                ImageIO.write(bufferedImage, "png", os);
            }
            return;
        }
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(os);
        document.save(bufferedOutputStream);
    }

    /**
     * パスからPDFファイルを取得する
     */
    private PDDocument getFile(String filePath) throws IOException {
        Resource resource = new ClassPathResource(filePath);
        RandomAccessRead accessRead = new RandomAccessBuffer(resource.getInputStream());
        PDFParser pdfParser = new PDFParser(accessRead);
        pdfParser.parse();
        return pdfParser.getPDDocument();
    }

    private PDFont getFont(PDDocument tempDocument) throws IOException {
        Resource resource = new ClassPathResource("corp_round_v1.ttf");
        return PDType0Font.load(tempDocument, resource.getInputStream(), true);
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
