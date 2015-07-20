package dragheart.service;

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import org.springframework.stereotype.Component;

@Component
public class ShitamatiPdfService {

    private static final int LEFT = 158;
    private static final int RIGHT = 426;
    private static final int START_MAIN = 657;
    private static final int START_HYPER = 190;
    private static final double DEFF = 21.1;

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
            y -= DEFF;
        }
    }

    public void writeHyperSpatial(PdfContentByte over, BaseFont bf, String[] hyperSpatial) {
        int x = LEFT;
        int y = START_HYPER;
        for (int i = 0; i < hyperSpatial.length; i++) {
            writeText(over, bf, hyperSpatial[i], x, y);
            y -= DEFF;
        }
    }

    public void writeMainColors(PdfContentByte over, BaseFont bf, String[] colors) {
        int x = LEFT;
        int y = START_MAIN;
        for (int i = 0; i < colors.length; i++) {
            if (20 <= i) {
                x = RIGHT;
            }
            if (20 == i) {
                y = START_MAIN;
            }
            over.setFontAndSize(bf, 17);
            writeColor(over, colors[i], x, y);
            y -= DEFF;
        }
    }

    public void writeHyperColors(PdfContentByte over, BaseFont bf, String[] colors) {
        int x = LEFT;
        int y = START_HYPER;
        for (int i = 0; i < colors.length; i++) {
            over.setFontAndSize(bf, 17);
            writeColor(over, colors[i], x, y);
            y -= DEFF;
        }
    }

    private void writeColor(PdfContentByte over, String color, int x, int y) {
        x -= 102;
        y -= 3;
        if (color.indexOf("light") != -1) {
            over.showTextAligned(Element.ALIGN_TOP, "◯", x, y, 0);
        }
        if (color.indexOf("water") != -1) {
            over.showTextAligned(Element.ALIGN_TOP, "◯", x + 17, y, 0);
        }
        if (color.indexOf("darkness") != -1) {
            over.showTextAligned(Element.ALIGN_TOP, "◯", x + 34, y, 0);
        }
        if (color.indexOf("fire") != -1) {
            over.showTextAligned(Element.ALIGN_TOP, "◯", x + 51, y, 0);
        }
        if (color.indexOf("nature") != -1) {
            over.showTextAligned(Element.ALIGN_TOP, "◯", x + 68, y, 0);
        }
        if (color.indexOf("lwd") != -1) { // 白青黒
            over.showTextAligned(Element.ALIGN_TOP, "◯", x, y, 0);
            over.showTextAligned(Element.ALIGN_TOP, "◯", x + 17, y, 0);
            over.showTextAligned(Element.ALIGN_TOP, "◯", x + 34, y, 0);
        }
        if (color.indexOf("lwf") != -1) { // 白青赤
            over.showTextAligned(Element.ALIGN_TOP, "◯", x, y, 0);
            over.showTextAligned(Element.ALIGN_TOP, "◯", x + 17, y, 0);
            over.showTextAligned(Element.ALIGN_TOP, "◯", x + 51, y, 0);
        }
        if (color.indexOf("lwn") != -1) { // 白青緑
            over.showTextAligned(Element.ALIGN_TOP, "◯", x, y, 0);
            over.showTextAligned(Element.ALIGN_TOP, "◯", x + 17, y, 0);
            over.showTextAligned(Element.ALIGN_TOP, "◯", x + 68, y, 0);
        }
    }

    public void writeText(PdfContentByte over, BaseFont bf, String text, int x, int y) {
        over.setFontAndSize(bf, getFontSize(text));
        over.showTextAligned(Element.ALIGN_TOP, text, x, y, 0);
    }

    private int getFontSize(String text) {
        int length = text.length();
        if (24 < length) {
            return 4;
        }
        if (17 < length) {
            return 6;
        }
        return 8;
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
