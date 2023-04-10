package dragheart.service;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.util.Matrix;
import org.springframework.stereotype.Component;

@Component
public class PdfService {

    protected static int LEFT = 47;
    protected static int RIGHT = 325;
    protected static int START_MAIN = 711;
    protected static double START_HYPER = 194;
    protected static double START_GR = 324;
    protected static double LINE_SPACE = 17.2;
    protected static int FONT_SIZE = 9;

    protected static int NAME_X = 255;
    protected static int NAME_Y = 82;
    protected static int NAME_KANA_Y = 117;
    protected static int ID_X = 35;

    protected static int MAIN_MAX = 40;
    protected static int SPATIAL_MAX = 8;
    protected static int GR_MAX = 12;

    protected static String CIRCLE = "◯";
    protected static int CIRCLE_FONT_SIZE = 14;
    protected static int DECK_ON_X = 522;
    protected static double DECK_OFF_X = 545.5;
    protected static double DECK_Y = 15.5;
    protected static double FORBIDDENSTAR_ON_X = 244;
    protected static double FORBIDDENSTAR_OFF_X = 267.5;
    protected static double FORBIDDENSTAR_Y = 365;
    protected static double ZERON_ON_X = 516;
    protected static double ZERON_OFF_X = 539.5;
    protected static double ZERON_Y = 365;

    public void writeName(PDPageContentStream contentStream, PDFont font, String name) {
        if (name != null) {
            this.writeText(contentStream, font, name, NAME_X, NAME_Y);
        }
    }

    public void writeNameKana(PDPageContentStream contentStream, PDFont font, String nameKana) {
        if (nameKana != null) {
            this.writeText(contentStream, font, nameKana, NAME_X, NAME_KANA_Y);
        }
    }

    public void writeId(PDPageContentStream contentStream, PDFont font, String id) {
        if (id != null) {
            this.writeText(contentStream, font, id, ID_X, NAME_Y);
        }
    }

    public void writeMainDeck(PDPageContentStream contentStream, PDFont font, String[] mainDeck) {
        writeCard(contentStream, font, mainDeck, (float) START_MAIN, MAIN_MAX);
    }

    public void writeHyperSpatial(PDPageContentStream contentStream, PDFont font, String[] hyperSpatial) {
        writeCard(contentStream, font, hyperSpatial, (float) START_HYPER, SPATIAL_MAX);
        float x = 0 < hyperSpatial.length ? DECK_ON_X : (float) DECK_OFF_X;
        writeCircle(contentStream, font, x, (float) (START_HYPER + DECK_Y));
    }

    public void writeHyperGR(PDPageContentStream contentStream, PDFont font, String[] hyperGR) {
        writeCard(contentStream, font, hyperGR, (float) START_GR, GR_MAX);
        float x = 0 < hyperGR.length ? DECK_ON_X : (float) DECK_OFF_X;
        writeCircle(contentStream, font, x, (float) (START_GR + DECK_Y));
    }

    public void writeForbiddenStar(PDPageContentStream contentStream, PDFont font, Boolean forbiddenStar) {
        double x = forbiddenStar ? FORBIDDENSTAR_ON_X : FORBIDDENSTAR_OFF_X;
        writeCircle(contentStream, font, (float) x, (float) FORBIDDENSTAR_Y);
    }

    public void writeZeron(PDPageContentStream contentStream, PDFont font, Boolean zeron) {
        double x = zeron ? ZERON_ON_X : ZERON_OFF_X;
        writeCircle(contentStream, font, (float) x, (float) ZERON_Y);
    }

    private void writeCard(PDPageContentStream contentStream, PDFont font, String[] cards, float y, int max) {
        float x = LEFT;
        float tempY = y;
        for (int i = 0; i < cards.length; i++) {
            if (max / 2 <= i) {
                x = RIGHT;
            }
            if (max / 2 == i) {
                tempY = y;
            }
            if (40 < cards.length) {
                if (19 < i && i <= 40)
                    x = RIGHT - 92;
                if (39 < i && i <= 60) {
                    x = RIGHT + 88;
                    if (i == 40)
                        tempY = y;
                }
            }
            writeText(contentStream, font, cards[i], x, tempY);
            tempY -= LINE_SPACE;
        }
    }

    public void writeText(PDPageContentStream contentStream, PDFont font, String text, float x, float y) {
        try {
            contentStream.setFont(font, FONT_SIZE);
            contentStream.setNonStrokingColor(0, 0, 0);
            contentStream.setTextMatrix(Matrix.getRotateInstance(0, x, y));
            contentStream.newLine();
            contentStream.showText(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeCircle(PDPageContentStream contentStream, PDFont font, float x, float y) {
        try {
            contentStream.setFont(font, CIRCLE_FONT_SIZE);
            contentStream.setNonStrokingColor(255, 0, 0);
            contentStream.setTextMatrix(Matrix.getRotateInstance(0, x, y));
            contentStream.newLine();
            contentStream.showText(CIRCLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean validate(String[] mainDeck, String[] hyperSpatial, String[] hyperGR) {
        if (mainDeck.length < 40 && 60 < mainDeck.length) {
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
