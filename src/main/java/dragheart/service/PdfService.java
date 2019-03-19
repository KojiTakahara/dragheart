package dragheart.service;

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import org.springframework.stereotype.Component;

@Component
public class PdfService {

    private static final int LEFT = 47;
    private static final int RIGHT = 325;
    private static final int START_MAIN = 711;
    private static final double START_HYPER = 194;
    private static final double START_GR = 324;
    private static final double LINE_SPACE = 17.2;
    private static final int FONT_SIZE = 9;

    private static final int NAME_X = 255;
    private static final int NAME_Y = 82;
    private static final int NAME_KANA_Y = 117;
    private static final int ID_X = 35;

    private static final int MAIN_MAX = 40;
    private static final int SPATIAL_MAX = 8;
    private static final int GR_MAX = 12;

    private static final String CIRCLE = "◯";
    private static final int CIRCLE_FONT_SIZE = 14;
    private static final int DECK_ON_X = 522;
    private static final double DECK_OFF_X = 545.5;
    private static final double DECK_Y = 15.5;

    public void writeName(PdfContentByte over, BaseFont bf, String name) {
        this.writeText(over, bf, name, NAME_X, NAME_Y);
    }

    public void writeNameKana(PdfContentByte over, BaseFont bf, String nameKana) {
        if (nameKana != null) {
            this.writeText(over, bf, nameKana, NAME_X, NAME_KANA_Y);
        }
    }

    public void writeId(PdfContentByte over, BaseFont bf, String id) {
        if (id != null) {
            this.writeText(over, bf, id, ID_X, NAME_Y);
        }
    }

    public void writeMainDeck(PdfContentByte over, BaseFont bf, String[] mainDeck) {
        writeCard(over, bf, mainDeck, (float) START_MAIN, MAIN_MAX);
    }

    public void writeHyperSpatial(PdfContentByte over, BaseFont bf, String[] hyperSpatial) {
        writeCard(over, bf, hyperSpatial, (float) START_HYPER, SPATIAL_MAX);
        float x = 0 < hyperSpatial.length ? DECK_ON_X : (float) DECK_OFF_X;
        writeCircle(over, bf, x, (float) (START_HYPER + DECK_Y));
    }

    public void writeHyperGR(PdfContentByte over, BaseFont bf, String[] hyperGR) {
        writeCard(over, bf, hyperGR, (float) START_GR, GR_MAX);
        float x = 0 < hyperGR.length ? DECK_ON_X : (float) DECK_OFF_X;
        writeCircle(over, bf, x, (float) (START_GR + DECK_Y));
    }

    public void writeForbiddenStar(PdfContentByte over, BaseFont bf, Boolean forbiddenStar) {
        writeCircle(over, bf, forbiddenStar ? 244 : (float) 267.5, 365);
    }

    private void writeCard(PdfContentByte over, BaseFont bf, String[] cards, float y, int max) {
        float x = LEFT;
        float tempY = y;
        for (int i = 0; i < cards.length; i++) {
            if (max / 2 <= i) {
                x = RIGHT;
            }
            if (max / 2 == i) {
                tempY = y;
            }
            writeText(over, bf, cards[i], x, tempY);
            tempY -= LINE_SPACE;
        }
    }

    public void writeText(PdfContentByte over, BaseFont bf, String text, float x, float y) {
        over.setFontAndSize(bf, FONT_SIZE);
        over.setLineWidth(0.2);
        over.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE);
        over.showTextAligned(Element.ALIGN_TOP, text, x, y, 0);
    }

    private void writeCircle(PdfContentByte over, BaseFont bf, float x, float y) {
        over.setFontAndSize(bf, CIRCLE_FONT_SIZE);
        over.setLineWidth(0.2);
        over.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE);
        over.showTextAligned(Element.ALIGN_TOP, CIRCLE, x, y, 0);
    }

    public boolean validate(String[] mainDeck, String[] hyperSpatial, String[] hyperGR) {
        if (mainDeck.length != MAIN_MAX) {
            return false;
        }
        if (SPATIAL_MAX < hyperSpatial.length) {
            return false;
        }
        if (0 < hyperGR.length) {
            if (GR_MAX != hyperGR.length) {
                return false;
            }
        }
        return true;
    }
}
