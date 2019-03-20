package dragheart.service;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.util.Matrix;
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

    public void writeName(PDPageContentStream contentStream, PDFont font, String name) {
        this.writeText(contentStream, font, name, NAME_X, NAME_Y);
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
        writeCircle(contentStream, font, forbiddenStar ? 244 : (float) 267.5, 365);
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

    private void writeCircle(PDPageContentStream contentStream, PDFont font, float x, float y) {
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
