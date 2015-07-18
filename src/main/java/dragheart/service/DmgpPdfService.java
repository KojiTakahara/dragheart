package dragheart.service;

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import org.springframework.stereotype.Component;

@Component
public class DmgpPdfService {

    private static final int LEFT = 80;
    private static final int RIGHT = 350;
    private static final int START_MAIN = 612;
    private static final int START_HYPER = 92;

    public void writeMainDeck(PdfContentByte over, BaseFont bf, String[] mainDeck) {
        int x = LEFT;
        int y = START_MAIN;
        for (int i = 0; i < mainDeck.length; i++) {
            if (20 <= i) {
                x = RIGHT;
            }
            if (20 == i) {
                y = START_MAIN;
            }
            writeText(over, bf, mainDeck[i], x, y);
            y -= 22.5;
        }
    }

    public void writeHyperSpatial(PdfContentByte over, BaseFont bf, String[] hyperSpatial) {
        int x = LEFT;
        int y = START_HYPER;
        for (int i = 0; i < hyperSpatial.length; i++) {
            if (4 <= i) {
                x = RIGHT;
            }
            if (4 == i) {
                y = START_HYPER;
            }
            writeText(over, bf, hyperSpatial[i], x, y);
            y -= 22.5;
        }
    }

    public void writeText(PdfContentByte over, BaseFont bf, String text, int x, int y) {
        over.setFontAndSize(bf, getFontSize(text));
        over.showTextAligned(Element.ALIGN_TOP, text, x, y, 0);
    }

    private int getFontSize(String text) {
        int length = text.length();
        if (24 < length) {
            return 8;
        }
        if (17 < length) {
            return 10;
        }
        return 12;
    }

    public boolean validate(String[] mainDeck, String[] hyperSpatial) {
        if (mainDeck.length != 40) {
            return false;
        }
        if (8 < hyperSpatial.length) {
            return false;
        }
        return true;
    }
}
